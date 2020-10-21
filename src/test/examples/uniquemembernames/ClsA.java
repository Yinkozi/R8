// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package uniquemembernames;

public class ClsA extends BaseCls {

  @Override
  protected int c() {
    return foo() + c;
  }

  @Override
  protected int foo() {
    return (int) bar();
  }

}
