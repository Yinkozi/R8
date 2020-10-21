// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.ir.optimize.lambda.kotlin;

import com.android.tools.r8.code.Const16;
import com.android.tools.r8.code.Const4;
import com.android.tools.r8.code.ReturnVoid;
import com.android.tools.r8.graph.AppInfoWithClassHierarchy;
import com.android.tools.r8.graph.AppView;
import com.android.tools.r8.graph.DexClass;
import com.android.tools.r8.graph.DexEncodedField;
import com.android.tools.r8.graph.DexEncodedMethod;
import com.android.tools.r8.graph.DexField;
import com.android.tools.r8.graph.DexItemFactory;
import com.android.tools.r8.graph.DexMethod;
import com.android.tools.r8.graph.DexType;
import com.android.tools.r8.graph.EnclosingMethodAttribute;
import com.android.tools.r8.graph.InnerClassAttribute;
import com.android.tools.r8.ir.analysis.type.TypeElement;
import com.android.tools.r8.ir.code.Invoke.Type;
import com.android.tools.r8.ir.code.Position;
import com.android.tools.r8.ir.code.ValueType;
import com.android.tools.r8.ir.optimize.lambda.LambdaGroup;
import com.android.tools.r8.ir.synthetic.SyntheticSourceCode;
import com.android.tools.r8.kotlin.Kotlin;
import com.android.tools.r8.shaking.AppInfoWithLiveness;
import com.android.tools.r8.utils.InternalOptions;
import com.android.tools.r8.utils.ThrowingConsumer;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.IntFunction;

// Represents a k-style lambda group created to combine several lambda classes
// generated by kotlin compiler for regular kotlin lambda expressions, like:
//
//      -----------------------------------------------------------------------
//      fun foo(m: String, v: Int): () -> String {
//        val lambda: (String, Int) -> String = { s, i -> s.substring(i) }
//        return { "$m: $v" }
//      }
//      -----------------------------------------------------------------------
//
// Regular stateless k-style lambda class structure looks like below:
// NOTE: stateless k-style lambdas do not always have INSTANCE field.
//
// -----------------------------------------------------------------------------------------------
// // signature Lkotlin/jvm/internal/Lambda;Lkotlin/jvm/functions/Function2<
//                          Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;>;
// final class lambdas/LambdasKt$foo$lambda1$1
//                  extends kotlin/jvm/internal/Lambda
//                  implements kotlin/jvm/functions/Function2  {
//
//     public synthetic bridge invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
//
//     public final invoke(Ljava/lang/String;I)Ljava/lang/String;
//       @Lorg/jetbrains/annotations/NotNull;() // invisible
//         @Lorg/jetbrains/annotations/NotNull;() // invisible, parameter 0
//
//     <init>()V
//
//     public final static Llambdas/LambdasKt$foo$lambda1$1; INSTANCE
//
//     static <clinit>()V
//
//     OUTERCLASS lambdas/LambdasKt foo (Ljava/lang/String;I)Lkotlin/jvm/functions/Function0;
//     final static INNERCLASS lambdas/LambdasKt$foo$lambda1$1 null null
// }
// -----------------------------------------------------------------------------------------------
//
// Regular stateful k-style lambda class structure looks like below:
//
// -----------------------------------------------------------------------------------------------
// // signature Lkotlin/jvm/internal/Lambda;Lkotlin/jvm/functions/Function0<Ljava/lang/String;>;
// final class lambdas/LambdasKt$foo$1
//                  extends kotlin/jvm/internal/Lambda
//                  implements kotlin/jvm/functions/Function0  {
//
//     public synthetic bridge invoke()Ljava/lang/Object;
//
//     public final invoke()Ljava/lang/String;
//       @Lorg/jetbrains/annotations/NotNull;() // invisible
//
//     <init>(Ljava/lang/String;I)V
//
//     final synthetic Ljava/lang/String; $m
//     final synthetic I $v
//
//     OUTERCLASS lambdas/LambdasKt foo (Ljava/lang/String;I)Lkotlin/jvm/functions/Function0;
//     final static INNERCLASS lambdas/LambdasKt$foo$1 null null
// }
// -----------------------------------------------------------------------------------------------
//
// Key k-style lambda class details:
//   - extends kotlin.jvm.internal.Lambda
//   - implements one of kotlin.jvm.functions.Function0..Function22, or FunctionN
//     see: https://github.com/JetBrains/kotlin/blob/master/libraries/
//                  stdlib/jvm/runtime/kotlin/jvm/functions/Functions.kt
//     and: https://github.com/JetBrains/kotlin/blob/master/spec-docs/function-types.md
//   - lambda class is created as an anonymous inner class
//   - lambda class carries generic signature and kotlin metadata attribute
//   - class instance fields represent captured values and have an instance constructor
//     with matching parameters initializing them (see the second class above)
//   - stateless lambda *may* be  implemented as a singleton with a static field storing the
//     only instance and initialized in static class constructor (see the first class above)
//   - main lambda method usually matches an exact lambda signature and may have
//     generic signature attribute and nullability parameter annotations
//   - optional bridge method created to satisfy interface implementation and
//     forwarding call to lambda main method
//
final class KStyleLambdaGroup extends KotlinLambdaGroup {
  private KStyleLambdaGroup(GroupId id) {
    super(id);
  }

  @Override
  protected ClassBuilder getBuilder(DexItemFactory factory, InternalOptions options) {
    return new ClassBuilder(factory, options, "kotlin-style lambda group");
  }

  @Override
  public ThrowingConsumer<DexClass, LambdaStructureError> lambdaClassValidator(
      Kotlin kotlin, AppInfoWithClassHierarchy appInfo) {
    return new ClassValidator(kotlin, appInfo);
  }

  @Override
  protected String getGroupSuffix() {
    return "ks$";
  }

  // Specialized group id.
  final static class GroupId extends KotlinLambdaGroupId {
    GroupId(
        AppView<AppInfoWithLiveness> appView,
        String capture,
        DexType iface,
        String pkg,
        String signature,
        DexEncodedMethod mainMethod,
        InnerClassAttribute inner,
        EnclosingMethodAttribute enclosing) {
      super(appView, capture, iface, pkg, signature, mainMethod, inner, enclosing);
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof GroupId && computeEquals((KotlinLambdaGroupId) obj);
    }

    @Override
    String getLambdaKindDescriptor() {
      return "Kotlin k-style lambda group";
    }

    @Override
    public LambdaGroup createGroup() {
      return new KStyleLambdaGroup(this);
    }
  }

  // Specialized class validator.
  private final class ClassValidator extends KotlinLambdaClassValidator {
    ClassValidator(Kotlin kotlin, AppInfoWithClassHierarchy appInfo) {
      super(kotlin, KStyleLambdaGroup.this, appInfo);
    }

    @Override
    int getInstanceInitializerMaxSize(List<DexEncodedField> captures) {
      return captures.size() + 3;
    }

    @Override
    int validateInstanceInitializerEpilogue(
        com.android.tools.r8.code.Instruction[] instructions, int index)
        throws LambdaStructureError {
      if (!(instructions[index] instanceof Const4) &&
          !(instructions[index] instanceof Const16)) {
        throw structureError(LAMBDA_INIT_CODE_VERIFICATION_FAILED);
      }
      index++;
      if (!(instructions[index] instanceof com.android.tools.r8.code.InvokeDirect
              || instructions[index] instanceof com.android.tools.r8.code.InvokeDirectRange)
          || instructions[index].getMethod() != kotlin.functional.lambdaInitializerMethod) {
        throw structureError(LAMBDA_INIT_CODE_VERIFICATION_FAILED);
      }
      index++;
      if (!(instructions[index] instanceof ReturnVoid)) {
        throw structureError(LAMBDA_INIT_CODE_VERIFICATION_FAILED);
      }
      return index + 1;
    }
  }

  // Specialized class builder.
  private final class ClassBuilder extends KotlinLambdaGroupClassBuilder<KStyleLambdaGroup> {
    ClassBuilder(DexItemFactory factory, InternalOptions options, String origin) {
      super(KStyleLambdaGroup.this, factory, options, origin);
    }

    @Override
    protected DexType getSuperClassType() {
      return factory.kotlin.functional.lambdaType;
    }

    @Override
    SyntheticSourceCode createInstanceInitializerSourceCode(
        DexType groupClassType, DexMethod initializerMethod, Position callerPosition) {
      return new InstanceInitializerSourceCode(
          factory,
          groupClassType,
          group.getLambdaIdField(factory),
          id -> group.getCaptureField(factory, id),
          initializerMethod,
          factory.kotlin.functional.getArity(id.iface),
          callerPosition);
    }
  }

  // Specialized instance initializer code.
  private final static class InstanceInitializerSourceCode
      extends KotlinInstanceInitializerSourceCode {
    private final int arity;
    private final DexMethod lambdaInitializer;

    InstanceInitializerSourceCode(
        DexItemFactory factory,
        DexType lambdaGroupType,
        DexField idField,
        IntFunction<DexField> fieldGenerator,
        DexMethod method,
        int arity,
        Position callerPosition) {
      super(lambdaGroupType, idField, fieldGenerator, method, callerPosition);
      this.arity = arity;
      this.lambdaInitializer = factory.createMethod(factory.kotlin.functional.lambdaType,
          factory.createProto(factory.voidType, factory.intType), factory.constructorMethodName);
    }

    @Override
    void prepareSuperConstructorCall(int receiverRegister) {
      int arityRegister = nextRegister(ValueType.INT);
      add(builder -> builder.addConst(TypeElement.getInt(), arityRegister, arity));
      add(
          builder ->
              builder.addInvoke(
                  Type.DIRECT,
                  lambdaInitializer,
                  lambdaInitializer.proto,
                  Lists.newArrayList(ValueType.OBJECT, ValueType.INT),
                  Lists.newArrayList(receiverRegister, arityRegister),
                  false /* isInterface */));
    }
  }
}
