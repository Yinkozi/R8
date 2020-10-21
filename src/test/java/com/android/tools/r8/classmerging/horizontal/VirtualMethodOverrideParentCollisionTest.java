// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.shaking.horizontalclassmerging;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

import com.android.tools.r8.NeverClassInline;
import com.android.tools.r8.NeverInline;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.classmerging.horizontal.HorizontalClassMergingTestBase;
import org.junit.Test;

public class VirtualMethodOverrideParentCollisionTest extends HorizontalClassMergingTestBase {

  public VirtualMethodOverrideParentCollisionTest(
      TestParameters parameters, boolean enableHorizontalClassMerging) {
    super(parameters, enableHorizontalClassMerging);
  }

  @Test
  public void testR8() throws Exception {
    testForR8(parameters.getBackend())
        .addInnerClasses(this.getClass())
        .addKeepMainRule(Main.class)
        .addOptionsModification(
            options -> options.enableHorizontalClassMerging = enableHorizontalClassMerging)
        .enableInliningAnnotations()
        .enableNeverClassInliningAnnotations()
        .setMinApi(parameters.getApiLevel())
        .run(parameters.getRuntime(), Main.class)
        .assertSuccessWithOutputLines("foo", "bar", "foo", "parent")
        .inspect(
            codeInspector -> {
              if (enableHorizontalClassMerging) {
                assertThat(codeInspector.clazz(A.class), isPresent());
                assertThat(codeInspector.clazz(B.class), not(isPresent()));
                // TODO(b/165517236): Explicitly check classes have been merged.
              } else {
                assertThat(codeInspector.clazz(A.class), isPresent());
                assertThat(codeInspector.clazz(B.class), isPresent());
              }
            });
  }

  @NeverClassInline
  public static class Parent {
    @NeverInline
    public void foo() {
      System.out.println("parent");
    }
  }

  @NeverClassInline
  public static class A extends Parent {
    @NeverInline
    @Override
    public void foo() {
      System.out.println("foo");
    }
  }

  @NeverClassInline
  public static class B extends Parent {
    // TODO(b/164924717): remove non overlapping constructor requirement
    public B(String s) {}

    @NeverInline
    public void bar() {
      System.out.println("bar");
    }
  }

  public static class Main {
    @NeverInline
    static void callFoo(Parent p) {
      p.foo();
    }

    public static void main(String[] args) {
      A a = new A();
      a.foo();
      B b = new B("");
      b.bar();
      callFoo(a);
      callFoo(b);
    }
  }
}
