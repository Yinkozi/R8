// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.utils.codeinspector;

public class InstructionOffsetSubject {
  // For Dex backend, this is bytecode offset.
  // For CF backend, this is the index in the list of instruction.
  final int offset;

  InstructionOffsetSubject(int offset) {
    this.offset = offset;
  }

  @Override
  public String toString() {
    return "" + offset;
  }
}
