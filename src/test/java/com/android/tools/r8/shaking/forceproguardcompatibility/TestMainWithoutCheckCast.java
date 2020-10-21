// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.shaking.forceproguardcompatibility;

public class TestMainWithoutCheckCast {

  public static void main(String[] args) throws Exception {
    String thisPackage = TestMainWithoutCheckCast.class.getPackage().getName();
    Object o = Class.forName(thisPackage + ".TestClassWithDefaultConstructor").newInstance();
    System.out.println("Instantiated " + o.getClass().getCanonicalName());
  }
}
