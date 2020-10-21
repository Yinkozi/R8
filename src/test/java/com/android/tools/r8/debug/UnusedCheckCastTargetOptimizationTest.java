// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.debug;

public class UnusedCheckCastTargetOptimizationTest {

  class Super {}

  class Subclass extends Super {}

  public static void main(String[] args) {
    Super[] b = new Subclass[10];
    Subclass[] c = (Subclass[]) b;
  }
}
