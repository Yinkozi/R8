// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package inlining;

import inlining.Nullability.Factor;
import inlining.pkg.InterfaceImplementationContainer;
import inlining.pkg.OtherPublicClass;
import inlining.pkg.PublicClass;
import inlining.pkg.Subclass;

public class Inlining {

  private static void Assert(boolean value) {
    if (!value) {
      System.out.println("FAILURE");
    }
  }

  private static void Assert(int value) {
    if (value <= 0) {
      System.out.println("FAILURE");
    }
  }

  private static void fail(String msg) {
    System.out.println(msg);
    System.exit(1);
  }

  static void marker0() {
    System.err.printf("");
    System.err.printf("");
    System.err.printf("");
  }

  static void marker1() {
    System.err.printf("");
    System.err.printf("");
    System.err.printf("");
  }

  static void marker2() {
    System.err.printf("");
    System.err.printf("");
    System.err.printf("");
  }

  public static void main(String[] args) {
    // Ensure the simple methods are called at least three times, to not be inlined due to being
    // called only once or twice.
    Assert(intExpression());
    Assert(intExpression());
    Assert(intExpression());
    Assert(longExpression());
    Assert(longExpression());
    Assert(longExpression());
    Assert(doubleExpression());
    Assert(floatExpression());
    Assert(floatExpression());
    Assert(floatExpression());
    Assert(stringExpression());
    Assert(stringExpression());
    Assert(stringExpression());

    Assert(intArgumentExpression());
    Assert(intArgumentExpression());
    Assert(intArgumentExpression());
    Assert(longArgumentExpression());
    Assert(longArgumentExpression());
    Assert(longArgumentExpression());
    Assert(doubleArgumentExpression());
    Assert(doubleArgumentExpression());
    Assert(doubleArgumentExpression());
    Assert(floatArgumentExpression());
    Assert(floatArgumentExpression());
    Assert(floatArgumentExpression());
    Assert(stringArgumentExpression());
    Assert(stringArgumentExpression());
    Assert(stringArgumentExpression());

    Assert(intAddExpression());
    Assert(intAddExpression());
    Assert(intAddExpression());

    A b = new B(42);
    A a = new A(42);
    Assert(intCmpExpression(a, b));
    Assert(intCmpExpression(a, b));
    Assert(intCmpExpression(a, b));

    // This is only called once!
    Assert(onlyCalledOnce(10));

    // This is only called twice, and is quite small!
    Assert(onlyCalledTwice(1) == 2);
    Assert(onlyCalledTwice(1) == 2);

    InlineConstructor ic = InlineConstructor.create();
    Assert(ic != null);
    InlineConstructor ic2 = InlineConstructor.createMore();
    Assert(ic2 != null);
    InlineConstructorOfInner icoi = new InlineConstructorOfInner();
    Assert(icoi != null);

    // Check that super calls are processed correctly.
    new B(123).callMethodInSuper();

    // Inline calls to package private methods
    PublicClass.alsoCallsPackagePrivateMethod();
    OtherPublicClass.callsMethodThatCallsPackagePrivateMethod();
    // Inline calls to protected methods.
    PublicClass.callsProtectedMethod3();
    PublicClass.alsoReadsPackagePrivateField();
    OtherPublicClass.callsMethodThatCallsProtectedMethod();
    OtherPublicClass.callsMethodThatReadsFieldInPackagePrivateClass();
    Subclass.callsMethodThatCallsProtectedMethod();
    // Do not inline constructors which set final field.
    System.out.println(new InlineConstructorFinalField());

    // Call method three times to ensure it would not normally be inlined but force inline anyway.
    int aNumber = longMethodThatWeShouldNotInline("ha", "li", "lo");
    aNumber += longMethodThatWeShouldNotInline("zi", "za", "zo");
    aNumber += longMethodThatWeShouldNotInline("do", "de", "da");
    System.out.println(aNumber);

    // Call a method that contains a call to a protected method. Should not be inlined.
    aNumber = new SubClassOfPublicClass().public_protectedMethod(0);
    System.out.println(aNumber);

    marker0();
    marker0();
    marker0();
    marker0();
    marker0();

    Nullability n = new Nullability(2018);
    Assert(n.inlinable(a));
    Assert(n.notInlinable(a));
    Assert(n.conditionalOperator(a));
    Assert(n.moreControlFlows(a, Factor.ONE));
    Assert(n.inlinableWithPublicField(a));
    Assert(n.inlinableWithControlFlow(a));
    Assert(n.notInlinableDueToMissingNpe(a));
    Assert(n.notInlinableDueToSideEffect(a));
    Assert(n.notInlinableBecauseHidesNpe());
    try {
      Assert(n.notInlinableDueToMissingNpeBeforeThrow(new IllegalArgumentException()));
    } catch (IllegalArgumentException expected) {
      // Expected exception
    } catch (NullPointerException unexpected) {
      System.out.println(
          "Unexpected NullPointerException for notInlinableDueToMissingNpeBeforeThrow");
    } catch (Throwable unexpected) {
      System.out.println("Unexpected exception for notInlinableDueToMissingNpeBeforeThrow");
    }
    try {
      Assert(n.notInlinableOnThrow(new IllegalArgumentException()));
    } catch (IllegalArgumentException expected) {
      // Expected exception
    } catch (NullPointerException unexpected) {
      System.out.println("Unexpected NullPointerException for notInlinableOnThrow");
    } catch (Throwable unexpected) {
      System.out.println("Unexpected exception for notInlinableOnThrow");
    }

    marker1();
    marker1();
    marker1();
    marker1();
    marker1();

    n = maybeNull();
    ThrowingA aa = new ThrowingA(a.a());
    try {
      n.inlinable(aa);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      n.notInlinable(aa);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      n.conditionalOperator(aa);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      n.moreControlFlows(aa, Factor.TWO);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      n.inlinableWithPublicField(aa);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      n.inlinableWithControlFlow(aa);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      n.notInlinableDueToMissingNpe(aa);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      n.notInlinableDueToSideEffect(aa);
    } catch (NullPointerException npe) {
      // Expected!
    }
    try {
      Assert(n.notInlinableBecauseHidesNpe());
      fail("Should have thrown NullPointerException");
    } catch (NullPointerException expected) {
      // Expected exception
    }
    try {
      n.notInlinableDueToMissingNpeBeforeThrow(new IllegalArgumentException());
    } catch (NullPointerException npe) {
      // Expected!
    } catch (Throwable t) {
      System.out.println("Unexpected exception");
    }
    try {
      Assert(n.notInlinableDueToMissingNpeBeforeThrow(new IllegalArgumentException()));
    } catch (NullPointerException expected) {
      // Expected exception
    } catch (IllegalArgumentException unexpected) {
      System.out.println(
          "Unexpected IllegalArgumentException for notInlinableDueToMissingNpeBeforeThrow");
    } catch (Throwable unexpected) {
      System.out.println("Unexpected exception for notInlinableDueToMissingNpeBeforeThrow");
    }
    try {
      Assert(n.notInlinableOnThrow(new IllegalArgumentException()));
    } catch (NullPointerException expected) {
      // Expected exception
    } catch (IllegalArgumentException unexpected) {
      System.out.println("Unexpected IllegalArgumentException for notInlinableOnThrow");
    } catch (Throwable unexpected) {
      System.out.println("Unexpected exception for notInlinableOnThrow");
    }

    marker2();
    marker2();
    marker2();
    marker2();
    marker2();

    System.out.println(callInterfaceMethod(InterfaceImplementationContainer.getIFace()));
  }

  @NeverInline
  private static Nullability maybeNull() {
    // Make sure that R8 cannot determine that this method always returns null.
    return System.currentTimeMillis() < 0 ? new Nullability(42) : null;
  }

  private static boolean intCmpExpression(A a, A b) {
    return a.a() == b.a();
  }

  @CheckDiscarded
  private static int callInterfaceMethod(IFace i) {
    return i.foo();
  }

  @CheckDiscarded
  private static int intConstantInline() {
    return 42;
  }

  @CheckDiscarded
  private static boolean intExpression() {
    return 42 == intConstantInline();
  }

  @CheckDiscarded
  private static long longConstantInline() {
    return 50000000000L;
  }

  @CheckDiscarded
  private static boolean longExpression() {
    return 50000000000L == longConstantInline();
  }

  @CheckDiscarded
  private static double doubleConstantInline() {
    return 42.42;
  }

  @CheckDiscarded
  private static boolean doubleExpression() {
    return 42.42 == doubleConstantInline();
  }

  @CheckDiscarded
  private static float floatConstantInline() {
    return 21.21F;
  }

  @CheckDiscarded
  private static boolean floatExpression() {
    return 21.21F == floatConstantInline();
  }

  @CheckDiscarded
  private static String stringConstantInline() {
    return "Fisk er godt";
  }

  private static boolean stringExpression() {
    return "Fisk er godt" == stringConstantInline();
  }

  @CheckDiscarded
  private static int intArgumentInline(int a, int b, int c) {
    return b;
  }

  @CheckDiscarded
  private static boolean intArgumentExpression() {
    return 42 == intArgumentInline(-2, 42, -1);
  }

  @CheckDiscarded
  private static long longArgumentInline(long a, long b, long c) {
    return c;
  }

  @CheckDiscarded
  private static boolean longArgumentExpression() {
    return 50000000000L == longArgumentInline(-2L, -1L, 50000000000L);
  }

  @CheckDiscarded
  private static double doubleArgumentInline(double a, double b, double c) {
    return a;
  }

  @CheckDiscarded
  private static boolean doubleArgumentExpression() {
    return 42.42 == doubleArgumentInline(42.42, -2.0, -1.0);
  }

  @CheckDiscarded
  private static float floatArgumentInline(float a, float b, float c) {
    return b;
  }

  @CheckDiscarded
  private static boolean floatArgumentExpression() {
    return 21.21F == floatArgumentInline(-2.0F, 21.21F, -1.0F);
  }

  @CheckDiscarded
  private static String stringArgumentInline(String a, String b, String c) {
    return c;
  }

  private static boolean stringArgumentExpression() {
    return "Fisk er godt" == stringArgumentInline("-1", "-1", "Fisk er godt");
  }

  @CheckDiscarded
  private static int intAddInline(int a, int b) {
    return a + b;
  }

  @CheckDiscarded
  private static boolean intAddExpression() {
    return 42 == intAddInline(21, 21);
  }

  @CheckDiscarded
  private static boolean onlyCalledOnce(int count) {
    int anotherCounter = 0;
    for (int i = 0; i < count; i++) {
      anotherCounter += i;
    }
    return anotherCounter > count;
  }

  @CheckDiscarded
  private static int onlyCalledTwice(int count) {
    return count > 0 ? count + 1 : count - 1;
  }

  @AlwaysInline
  @CheckDiscarded
  private static int longMethodThatWeShouldNotInline(String a, String b, String c) {
    String result = a + b + c + b + a + c + b;
    return result.length();
  }
}
