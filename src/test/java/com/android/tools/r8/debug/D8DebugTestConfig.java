// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.debug;

import com.android.tools.r8.CompilationMode;
import com.android.tools.r8.D8Command;
import com.android.tools.r8.OutputMode;
import com.android.tools.r8.ToolHelper;
import com.android.tools.r8.utils.AndroidApiLevel;
import com.android.tools.r8.utils.AndroidApp;
import com.android.tools.r8.utils.InternalOptions;
import com.android.tools.r8.utils.ListUtils;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.junit.rules.TemporaryFolder;

/** Test configuration with utilities for compiling with D8 and adding results to the classpath. */
public class D8DebugTestConfig extends DexDebugTestConfig {

  public static AndroidApp d8Compile(List<Path> paths, Consumer<InternalOptions> optionsConsumer) {
    try {
      AndroidApiLevel minSdk = ToolHelper.getMinApiLevelForDexVm();
      D8Command.Builder builder = D8Command.builder();
      return ToolHelper.runD8(
          builder
              .addProgramFiles(paths)
              .setMinApiLevel(minSdk.getLevel())
              .setMode(CompilationMode.DEBUG)
              .addLibraryFiles(ToolHelper.getAndroidJar(minSdk)),
          optionsConsumer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public D8DebugTestConfig compileAndAddClasses(TemporaryFolder temp, Class... classes) {
    return compileAndAddClasses(temp, Arrays.asList(classes));
  }

  public D8DebugTestConfig compileAndAddClasses(TemporaryFolder temp, List<Class> classes) {
    return compileAndAdd(temp, ListUtils.map(classes, ToolHelper::getClassFileForTestClass), null);
  }

  public D8DebugTestConfig compileAndAdd(TemporaryFolder temp, Path... paths) {
    return compileAndAdd(temp, Arrays.asList(paths), null);
  }

  public D8DebugTestConfig compileAndAdd(TemporaryFolder temp, List<Path> paths) {
    return compileAndAdd(temp, paths, null);
  }

  public D8DebugTestConfig compileAndAdd(
      TemporaryFolder temp, List<Path> paths, Consumer<InternalOptions> optionsConsumer) {
    try {
      Path out = temp.newFolder().toPath().resolve("d8_compiled.jar");
      d8Compile(paths, optionsConsumer).write(out, OutputMode.DexIndexed);
      addPaths(out);
      return this;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
