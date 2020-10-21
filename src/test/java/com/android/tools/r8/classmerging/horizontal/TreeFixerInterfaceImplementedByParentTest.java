// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.classmerging.horizontal;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresent;
import static com.android.tools.r8.utils.codeinspector.Matchers.notIf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.android.tools.r8.NeverClassInline;
import com.android.tools.r8.NeverInline;
import com.android.tools.r8.NoHorizontalClassMerging;
import com.android.tools.r8.NoVerticalClassMerging;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.utils.codeinspector.ClassSubject;
import org.junit.Test;

/**
 * This test creates a conflict between Parent#foo(B) and C#foo(A), because A and B are merged.
 * Normally C#foo(A) would be renamed, but because it is an interface method it should not be
 * changed.
 */
public class TreeFixerInterfaceImplementedByParentTest extends HorizontalClassMergingTestBase {
  public TreeFixerInterfaceImplementedByParentTest(
      TestParameters parameters, boolean enableHorizontalClassMerging) {
    super(parameters, enableHorizontalClassMerging);
  }

  @Test
  public void testR8() throws Exception {
    testForR8(parameters.getBackend())
        .addInnerClasses(getClass())
        .addKeepMainRule(Main.class)
        .noMinification()
        .addOptionsModification(
            options -> options.enableHorizontalClassMerging = enableHorizontalClassMerging)
        .enableInliningAnnotations()
        .enableNeverClassInliningAnnotations()
        .enableNoVerticalClassMergingAnnotations()
        .enableNoHorizontalClassMergingAnnotations()
        .setMinApi(parameters.getApiLevel())
        .run(parameters.getRuntime(), Main.class)
        .assertSuccessWithOutputLines("print a: parent", "print b: i", "print b: e")
        .inspect(
            codeInspector -> {
              assertThat(codeInspector.clazz(A.class), isPresent());
              assertThat(
                  codeInspector.clazz(B.class), notIf(isPresent(), enableHorizontalClassMerging));

              ClassSubject parentClassSubject = codeInspector.clazz(Parent.class);
              assertThat(parentClassSubject, isPresent());
              // Parent #foo(A) is kept as is.
              if (enableHorizontalClassMerging) {
                // Parent#foo(B) is renamed to Parent#foo1 to prevent collision.
                assertThat(parentClassSubject.uniqueMethodWithFinalName("foo"), isPresent());
                assertThat(parentClassSubject.uniqueMethodWithFinalName("foo$1"), isPresent());
              }

              assertThat(codeInspector.clazz(C.class), isPresent());

              ClassSubject iClassSubject = codeInspector.clazz(I.class);
              assertThat(iClassSubject, isPresent());
              assertThat(iClassSubject.uniqueMethodWithFinalName("foo"), isPresent());
            });
  }

  @NeverClassInline
  @NoVerticalClassMerging
  public interface I {
    @NeverInline
    void foo(B b);
  }

  @NoVerticalClassMerging
  @NoHorizontalClassMerging
  public static class Parent {
    @NeverInline
    public void foo(B b) {
      b.print("i");
    }

    @NeverInline
    public void foo(A a) {
      a.print("parent");
    }
  }

  @NeverClassInline
  public static class A {
    @NeverInline
    public void print(String v) {
      System.out.println("print a: " + v);
    }
  }

  @NeverClassInline
  public static class B {
    @NeverInline
    public void print(String v) {
      System.out.println("print b: " + v);
    }
  }

  @NeverClassInline
  public static class C extends Parent implements I {}

  @NeverClassInline
  public static class E implements I {
    @NeverInline
    public void foo(B b) {
      b.print("e");
    }
  }

  public static class Main {
    @NeverInline
    public static void fooI(I i, B b) {
      i.foo(b);
    }

    public static void main(String[] args) {
      A a = new A();
      B b = new B();
      C c = new C();
      I i = c;
      c.foo(a);
      fooI(i, b);
      fooI(new E(), b);
    }
  }
}
