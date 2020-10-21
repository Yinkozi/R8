// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.debug;

import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LambdaTest extends DebugTestBase {

  private static final Path JAR = DebugTestBase.DEBUGGEE_JAVA8_JAR;
  private static final String SOURCE_FILE = "DebugLambda.java";

  @Parameters(name = "{0}")
  public static Collection configs() {
    ImmutableList.Builder<Object[]> builder = ImmutableList.builder();
    DelayedDebugTestConfig cfConfig = temp -> new CfDebugTestConfig(JAR);
    DelayedDebugTestConfig d8Config = temp -> new D8DebugTestConfig().compileAndAdd(temp, JAR);
    builder.add(new Object[]{"CF", cfConfig});
    builder.add(new Object[]{"D8", d8Config});
    return builder.build();
  }

  private final DebugTestConfig config;

  public LambdaTest(String name, DelayedDebugTestConfig delayedConfig) {
    this.config = delayedConfig.getConfig(temp);
  }

  @Test
  public void testLambda_ExpressionOnSameLine() throws Throwable {
    String debuggeeClass = "DebugLambda";
    String initialMethodName = "printInt";
    runDebugTest(
        config,
        debuggeeClass,
        breakpoint(debuggeeClass, initialMethodName),
        run(),
        checkMethod(debuggeeClass, initialMethodName),
        checkLine(SOURCE_FILE, 12),
        checkLocals("i"),
        checkNoLocal("j"),
        stepInto(INTELLIJ_FILTER),
        checkLine(SOURCE_FILE, 16),
        checkLocals("i", "j"),
        run());
  }

  @Test
  public void testLambda_StatementOnNewLine() throws Throwable {
    String debuggeeClass = "DebugLambda";
    String initialMethodName = "printInt3";
    runDebugTest(
        config,
        debuggeeClass,
        breakpoint(debuggeeClass, initialMethodName),
        run(),
        checkMethod(debuggeeClass, initialMethodName),
        checkLine(SOURCE_FILE, 32),
        checkLocals("i", "a", "b"),
        stepInto(INTELLIJ_FILTER),
        checkLine(SOURCE_FILE, 37),
        checkLocals("a", "b"),
        run());
  }

  @Test
  public void testLambda_StaticMethodReference_Trivial() throws Throwable {
    String debuggeeClass = "DebugLambda";
    String initialMethodName = "printInt2";
    runDebugTest(
        config,
        debuggeeClass,
        breakpoint(debuggeeClass, initialMethodName),
        run(),
        checkMethod(debuggeeClass, initialMethodName),
        checkLine(SOURCE_FILE, 20),
        stepInto(INTELLIJ_FILTER),
        config.isCfRuntime() ? LambdaTest::doNothing : stepInto(INTELLIJ_FILTER),
        checkMethod(debuggeeClass, "returnOne"),
        checkLine(SOURCE_FILE, 28),
        checkNoLocal(),
        run());
  }

  @Test
  public void testLambda_StaticMethodReference_NonTrivial() throws Throwable {
    String debuggeeClass = "DebugLambda";
    String initialMethodName = "testLambdaWithMethodReferenceAndConversion";
    runDebugTest(
        config,
        debuggeeClass,
        breakpoint(debuggeeClass, initialMethodName),
        run(),
        checkMethod(debuggeeClass, initialMethodName),
        checkLine(SOURCE_FILE, 46),
        stepInto(INTELLIJ_FILTER),
        inspect(t -> Assert.assertTrue(t.getMethodName().startsWith("lambda$"))),
        stepInto(INTELLIJ_FILTER),
        checkMethod(debuggeeClass, "concatObjects"),
        checkLine(SOURCE_FILE, 57),
        checkLocal("objects"),
        run());
  }

  private static void doNothing(JUnit3Wrapper jUnit3Wrapper) {
  }
}
