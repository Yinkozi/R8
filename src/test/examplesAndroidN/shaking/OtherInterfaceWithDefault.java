// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package shaking;

public interface OtherInterfaceWithDefault extends OtherInterface {

  @Override
  default void bar() {
    System.out.println("bar from OtherInterfaceWithDefault");
  }
}
