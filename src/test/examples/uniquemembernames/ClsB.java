// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package uniquemembernames;

public class ClsB extends BaseCls {

  @Override
  protected int c() {
    return foo() - c;
  }

  @Override
  protected double bar() {
    return f2 != 0 ? c / f2 : Double.MAX_VALUE;
  }

}
