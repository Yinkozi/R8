// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.desugar;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.android.tools.r8.DesugarTestConfiguration;
import com.android.tools.r8.TestBase;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestParametersCollection;
import com.android.tools.r8.ToolHelper;
import com.android.tools.r8.graph.DexMethod;
import com.android.tools.r8.utils.codeinspector.ClassSubject;
import com.android.tools.r8.utils.codeinspector.CodeInspector;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DesugarLambdaWithLocalClass extends TestBase {

  private List<String> EXPECTED_JAVAC_RESULT =
      ImmutableList.of("Hello from inside lambda$test$0", "Hello from inside lambda$testStatic$1");

  private List<String> EXPECTED_DESUGARED_RESULT =
      ImmutableList.of(
          "Hello from inside lambda$test$0$DesugarLambdaWithLocalClass$TestClass",
          "Hello from inside lambda$testStatic$1");

  @Parameterized.Parameters(name = "{0}")
  public static TestParametersCollection data() {
    return getTestParameters().withAllRuntimes().withAllApiLevelsAlsoForCf().build();
  }

  private final TestParameters parameters;

  public DesugarLambdaWithLocalClass(TestParameters parameters) {
    this.parameters = parameters;
  }

  static class Counter {
    private int count = 0;

    void increment() {
      count++;
    }

    int getCount() {
      return count;
    }
  }

  private void checkEnclosingMethod(CodeInspector inspector) {
    Counter counter = new Counter();
    inspector.forAllClasses(
        clazz -> {
          if (clazz.getFinalName().endsWith("$TestClass$1MyConsumerImpl")
              || clazz.getFinalName().endsWith("$TestClass$2MyConsumerImpl")) {
            counter.increment();
            assertTrue(clazz.isLocalClass());
            DexMethod enclosingMethod = clazz.getFinalEnclosingMethod();
            ClassSubject testClassSubject = inspector.clazz(TestClass.class);
            assertEquals(
                testClassSubject, inspector.clazz(enclosingMethod.holder.toSourceString()));
            assertThat(
                testClassSubject.uniqueMethodWithName(enclosingMethod.name.toString()),
                isPresent());
          }
        });
    assertEquals(2, counter.getCount());
  }

  @BeforeClass
  public static void checkExpectedJavacNames() throws Exception {
    CodeInspector inspector =
        new CodeInspector(
            ToolHelper.getClassFilesForInnerClasses(DesugarLambdaWithLocalClass.class));
    String outer = DesugarLambdaWithLocalClass.class.getTypeName();
    ClassSubject testClass = inspector.clazz(outer + "$TestClass");
    assertThat(testClass, isPresent());
    assertThat(testClass.uniqueMethodWithName("lambda$test$0"), isPresent());
    assertThat(testClass.uniqueMethodWithName("lambda$testStatic$1"), isPresent());
    assertThat(inspector.clazz(outer + "$TestClass$1MyConsumerImpl"), isPresent());
    assertThat(inspector.clazz(outer + "$TestClass$2MyConsumerImpl"), isPresent());
  }

  @Test
  public void testDesugar() throws Exception {
    testForDesugaring(parameters)
        .addInnerClasses(DesugarLambdaWithLocalClass.class)
        .run(parameters.getRuntime(), TestClass.class)
        .inspect(this::checkEnclosingMethod)
        .applyIf(
            DesugarTestConfiguration::isNotDesugared,
            r -> r.assertSuccessWithOutputLines(EXPECTED_JAVAC_RESULT))
        .applyIf(
            DesugarTestConfiguration::isDesugared,
            r -> r.assertSuccessWithOutputLines(EXPECTED_DESUGARED_RESULT));
  }

  @Test
  public void testR8() throws Exception {
    testForR8(parameters.getBackend())
        .addInnerClasses(DesugarLambdaWithLocalClass.class)
        .setMinApi(parameters.getApiLevel())
        // Keep the synthesized inner classes.
        .addKeepRules("-keep class **.*$TestClass$*MyConsumerImpl")
        // Keep the outer context: TestClass *and* the synthetic lambda methods.
        .addKeepRules("-keep class **.*$TestClass { private synthetic void lambda$*(*); }")
        .addKeepAttributes("InnerClasses", "EnclosingMethod")
        .addKeepMainRule(TestClass.class)
        .run(parameters.getRuntime(), TestClass.class)
        .inspect(this::checkEnclosingMethod)
        .assertSuccessWithOutputLines(
            parameters.isCfRuntime() ? EXPECTED_JAVAC_RESULT : EXPECTED_JAVAC_RESULT);
  }

  public interface MyConsumer<T> {
    void accept(T s);
  }

  public static class StringList extends ArrayList<String> {
    public void forEachString(MyConsumer<String> consumer) {
      for (String s : this) {
        consumer.accept(s);
      }
    }
  }

  public static class TestClass {

    public void test() {
      StringList list = new StringList();

      list.add("Hello ");
      list.add("from ");
      list.add("inside ");

      list.forEachString(
          s -> {
            class MyConsumerImpl implements MyConsumer<String> {
              public void accept(String s) {
                System.out.print(s);
                if (s.startsWith("inside")) {
                  if (getClass().getEnclosingMethod() == null) {
                    System.out.println("<null>");
                  } else {
                    System.out.println(getClass().getEnclosingMethod().getName());
                  }
                }
              }
            }
            new MyConsumerImpl().accept(s);
          });
    }

    public static void testStatic() {
      StringList list = new StringList();

      list.add("Hello ");
      list.add("from ");
      list.add("inside ");

      list.forEachString(
          s -> {
            class MyConsumerImpl implements MyConsumer<String> {
              public void accept(String s) {
                System.out.print(s);
                if (s.startsWith("inside")) {
                  if (getClass().getEnclosingMethod() == null) {
                    System.out.println("<null>");
                  } else {
                    System.out.println(getClass().getEnclosingMethod().getName());
                  }
                }
              }
            }
            new MyConsumerImpl().accept(s);
          });
    }

    public static void main(String[] args) {
      new TestClass().test();
      TestClass.testStatic();
    }
  }
}
