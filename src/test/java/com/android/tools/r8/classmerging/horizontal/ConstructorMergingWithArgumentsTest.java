// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.classmerging.horizontal;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

import com.android.tools.r8.NeverClassInline;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.utils.codeinspector.ClassSubject;
import com.android.tools.r8.utils.codeinspector.MethodSubject;
import org.junit.Test;

public class ConstructorMergingWithArgumentsTest extends HorizontalClassMergingTestBase {
  public ConstructorMergingWithArgumentsTest(
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
        .enableNeverClassInliningAnnotations()
        .setMinApi(parameters.getApiLevel())
        .run(parameters.getRuntime(), Main.class)
        .assertSuccessWithOutputLines("foo hello", "bar world")
        .inspect(
            codeInspector -> {
              if (enableHorizontalClassMerging) {
                ClassSubject aClassSubject = codeInspector.clazz(A.class);

                assertThat(aClassSubject, isPresent());
                assertThat(codeInspector.clazz(B.class), not(isPresent()));

                MethodSubject initSubject = aClassSubject.init(String.class.getName(), "int");
                assertThat(initSubject, isPresent());
                // TODO(b/165517236): Explicitly check classes have been merged.
              } else {
                assertThat(codeInspector.clazz(A.class), isPresent());
                assertThat(codeInspector.clazz(B.class), isPresent());
              }
            });
  }

  @NeverClassInline
  public static class A {
    public A(String arg) {
      System.out.println("foo " + arg);
    }
  }

  @NeverClassInline
  public static class B {
    public B(String arg) {
      System.out.println("bar " + arg);
    }
  }

  public static class Main {
    public static void main(String[] args) {
      A a = new A("hello");
      B b = new B("world");
    }
  }
}
