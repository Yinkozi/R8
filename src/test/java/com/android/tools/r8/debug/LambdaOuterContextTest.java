// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.debug;

public class LambdaOuterContextTest {

  public interface Converter {
    String convert(int value);
  }

  public int outer;

  public LambdaOuterContextTest(int outer) {
    this.outer = outer;
  }

  public void foo(Converter converter) {
    System.out.println(converter.convert(outer));
  }

  public void bar(int arg) {
    foo(value -> {
      // Ensure lambda uses parts of the outer context, otherwise javac will optimize it out.
      return Integer.toString(outer + value + arg);
    });
  }

  public static void main(String[] args) {
    new LambdaOuterContextTest(args.length + 42).bar(args.length);
  }
}
