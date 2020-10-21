// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.repackage.testclasses.repackagetest;

import com.android.tools.r8.NeverInline;
import com.android.tools.r8.NoStaticClassMerging;

@NoStaticClassMerging
public class ReachableClass {

  @NeverInline
  static void packagePrivateMethod() {
    System.out.println("ReachableClass.packagePrivateMethod()");
  }

  @NeverInline
  public static void publicMethod() {
    System.out.println("ReachableClass.publicMethod()");
  }
}
