// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.shaking;

import com.android.tools.r8.CompilationFailedException;
import com.android.tools.r8.TestBase;
import com.android.tools.r8.shaking.libraryextendsprogram.Interface;
import com.android.tools.r8.shaking.libraryextendsprogram.Main;
import com.android.tools.r8.shaking.libraryextendsprogram.SubClass;
import com.android.tools.r8.shaking.libraryextendsprogram.SuperClass;
import com.android.tools.r8.utils.AndroidApp;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class LibraryExtendsProgramTest extends TestBase {

  @Test(expected = CompilationFailedException.class)
  public void libraryClassExtendsProgramClass() throws Exception {
    AndroidApp theApp = readClasses(ImmutableList.of(Main.class, SuperClass.class),
        ImmutableList.of(SubClass.class, Interface.class));
    compileWithR8(theApp, o -> o.ignoreMissingClasses = true);
  }

  @Test(expected = CompilationFailedException.class)
  public void libraryClassImplementsProgramInterface() throws Exception {
    AndroidApp theApp = readClasses(ImmutableList.of(Main.class, Interface.class),
        ImmutableList.of(SubClass.class, SuperClass.class));
    compileWithR8(theApp, o -> o.ignoreMissingClasses = true);
  }
}
