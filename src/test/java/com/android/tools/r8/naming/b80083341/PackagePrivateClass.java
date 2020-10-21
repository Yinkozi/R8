// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.naming.b80083341;

final class PackagePrivateClass {
  private static final boolean flag = false;

  private PackagePrivateClass() {}

  interface Itf<T> {
    boolean foo();
  }

  static class Impl<T> implements Itf<T> {
    private final Object[] objs;

    Impl(int size) {
      objs = new Object[size];
    }

    @Override
    public boolean foo() {
      return flag;
    }
  }
}
