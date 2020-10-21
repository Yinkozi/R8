// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.ir.optimize.classinliner.trivial;

public class ReferencedFields {
  private String a;
  private String b;

  public ReferencedFields(String a, String b) {
    this.a = a;
    this.b = b;
  }

  public ReferencedFields(String a) {
    this.a = a;
  }

  public String getConcat() {
    return a + " " + b;
  }

  public String getA() {
    return a;
  }

  public String getB() {
    return b;
  }
}
