// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.ir.optimize.nonnull;

import com.android.tools.r8.NeverInline;

public class NonNullParamInterfaceImpl implements NonNullParamInterface {
  @NeverInline
  public int sum(NotPinnedClass arg1, NotPinnedClass arg2) {
    return arg1.field + arg2.field;
  }
}
