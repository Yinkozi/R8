// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package uniquemembernames;

public abstract class BaseCls {

  protected int c;
  protected double f2;

  protected abstract int c();

  protected int foo() {
    return c * (int) f2;
  }

  protected double bar() {
    return c * f2;
  }

}
