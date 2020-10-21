// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.ir.optimize.devirtualize.invokeinterface;

public class A1 extends A {
  @Override
  public int get() throws RuntimeException {
    throw new RuntimeException("Expected to be discarded");
  }
}
