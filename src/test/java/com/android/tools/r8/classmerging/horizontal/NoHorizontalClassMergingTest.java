// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.classmerging.horizontal;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;

import com.android.tools.r8.NeverClassInline;
import com.android.tools.r8.NoHorizontalClassMerging;
import com.android.tools.r8.TestParameters;
import org.junit.Test;

public class NoHorizontalClassMergingTest extends HorizontalClassMergingTestBase {
  public NoHorizontalClassMergingTest(
      TestParameters parameters, boolean enableHorizontalClassMerging) {
    super(parameters, enableHorizontalClassMerging);
  }

  @Test
  public void testR8() throws Exception {
    testForR8(parameters.getBackend())
        .addInnerClasses(getClass())
        .addKeepMainRule(Main.class)
        .addOptionsModification(
            options -> options.enableHorizontalClassMerging = enableHorizontalClassMerging)
        .enableNoHorizontalClassMergingAnnotations()
        .enableNeverClassInliningAnnotations()
        .setMinApi(parameters.getApiLevel())
        .run(parameters.getRuntime(), Main.class)
        .assertSuccessWithOutputLines("a", "b")
        .inspect(
            codeInspector -> {
              if (enableHorizontalClassMerging) {
                assertThat(codeInspector.clazz(A.class), isPresent());
                assertThat(codeInspector.clazz(B.class), isPresent());
              } else {
                assertThat(codeInspector.clazz(A.class), isPresent());
                assertThat(codeInspector.clazz(B.class), isPresent());
              }
            });
  }

  @NeverClassInline
  public static class A {
    public A() {
      System.out.println("a");
    }
  }

  @NeverClassInline
  @NoHorizontalClassMerging
  public static class B {
    public B(String v) {
      System.out.println(v);
    }
  }

  public static class Main {
    public static void main(String[] args) {
      A a = new A();
      B b = new B("b");
    }
  }
}
