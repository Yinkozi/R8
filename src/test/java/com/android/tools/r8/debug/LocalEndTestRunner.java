// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.debug;

import com.android.tools.r8.ByteDataView;
import com.android.tools.r8.ClassFileConsumer;
import com.android.tools.r8.ClassFileConsumer.ArchiveConsumer;
import com.android.tools.r8.utils.DescriptorUtils;
import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.util.Collection;
import org.apache.harmony.jpda.tests.framework.jdwp.Value;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LocalEndTestRunner extends DebugTestBase {

  static final Class CLASS = LocalEndTest.class;
  static final String NAME = CLASS.getCanonicalName();
  static final String DESC = DescriptorUtils.javaTypeToDescriptor(NAME);
  static final String FILE = CLASS.getSimpleName() + ".java";

  private static Path inputJarCache = null;

  private final String name;
  private final DebugTestConfig config;

  @Parameters(name = "{0}")
  public static Collection<Object[]> setup() {
    DelayedDebugTestConfig cf = temp -> new CfDebugTestConfig().addPaths(getInputJar(temp));
    DelayedDebugTestConfig d8 =
        temp -> new D8DebugTestConfig().compileAndAdd(temp, getInputJar(temp));
    return ImmutableList.of(new Object[] {"CF", cf}, new Object[] {"D8", d8});
  }

  private static Path getInputJar(TemporaryFolder temp) {
    if (inputJarCache == null) {
      inputJarCache = temp.getRoot().toPath().resolve("input.jar");
      ClassFileConsumer jarWriter = new ArchiveConsumer(inputJarCache);
      jarWriter.accept(ByteDataView.of(LocalEndTestDump.dump()), DESC, null);
      jarWriter.finished(null);
    }
    return inputJarCache;
  }

  public LocalEndTestRunner(String name, DelayedDebugTestConfig config) {
    this.name = name;
    this.config = config.getConfig(temp);
  }

  @Test
  public void test() throws Throwable {
    Value xInitial = Value.createInt(42);
    Value xNormal = Value.createInt(7);
    Value xExceptional = xInitial;
    runDebugTest(
        config,
        NAME,
        breakpoint(NAME, "foo", 12),
        // Run to breakpoint for the non-throwing case.
        run(),
        checkLine(FILE, 12),
        checkLocal("x", xInitial),
        stepOver(),
        checkLine(FILE, 13),
        checkLocal("x", xInitial),
        stepOver(),
        checkLine(FILE, 14),
        checkLocal("x", xNormal),
        stepOver(),
        checkLine(FILE, 16),
        checkNoLocal("x"),
        // Run to breakpoint for the throwing case.
        run(),
        checkLine(FILE, 12),
        checkLocal("x", xInitial),
        stepOver(),
        checkLine(FILE, 14),
        checkLocal("x", xExceptional),
        stepOver(),
        checkLine(FILE, 16),
        checkNoLocal("x"),
        run());
  }
}
