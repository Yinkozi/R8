// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.regress.b78493232;

import static org.junit.Assume.assumeTrue;

import com.android.tools.r8.AsmTestBase;
import com.android.tools.r8.D8TestRunResult;
import com.android.tools.r8.R8TestRunResult;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestParametersCollection;
import com.android.tools.r8.TestRunResult;
import com.android.tools.r8.utils.StringUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// Variant of Regress78493232, but where the new-instance is forced to flow to a non-trivial phi
// function prior to the call to <init>. Due to the non-trivial phi this JVM code will not pass
// the verifier. This test is kept to allow inspection of the code path hit in D8/R8 for such
// inputs, but besides that just documents the behaviour on the various VMs.
@RunWith(Parameterized.class)
public class Regress78493232_WithPhi extends AsmTestBase {

  private static final String EXPECTED =
      StringUtils.lines(
          "After 0 iterations, expected \"java.security.SecureRandom\", but got \"null\"");

  private static final String MAIN = Regress78493232Dump_WithPhi.CLASS_NAME;
  private static final List<Class<?>> CLASSES = ImmutableList.of(Regress78493232Utils.class);
  private static final List<byte[]> CLASS_BYTES =
      ImmutableList.of(Regress78493232Dump_WithPhi.dump());

  private final TestParameters parameters;

  @Parameters(name = "{0}")
  public static TestParametersCollection data() {
    return getTestParameters().withAllRuntimesAndApiLevels().build();
  }

  public Regress78493232_WithPhi(TestParameters parameters) {
    this.parameters = parameters;
  }

  @Test
  public void testReference() throws Exception {
    assumeTrue(parameters.isCfRuntime());
    testForJvm()
        .noVerify()
        .addProgramClassFileData(CLASS_BYTES)
        .addProgramClasses(CLASSES)
        .run(parameters.getRuntime(), MAIN)
        .assertSuccessWithOutput(EXPECTED);
  }

  @Test
  public void testD8() throws Exception {
    assumeTrue(parameters.isDexRuntime());
    D8TestRunResult result =
        testForD8()
            .addProgramClasses(CLASSES)
            .addProgramClassFileData(CLASS_BYTES)
            .setMinApi(parameters.getApiLevel())
            .addOptionsModification(options -> options.testing.readInputStackMaps = false)
            .run(parameters.getRuntime(), MAIN);
    checkResult(result);
  }

  @Test
  public void testR8() throws Exception {
    testR8(true);
  }

  @Test
  public void testNoTreeShakingR8() throws Exception {
    testR8(false);
  }

  private void testR8(boolean treeShake) throws Exception {
    assumeTrue(parameters.isDexRuntime());
    R8TestRunResult result =
        testForR8(parameters.getBackend())
            .addProgramClasses(CLASSES)
            .addProgramClassFileData(CLASS_BYTES)
            .treeShaking(treeShake)
            .noMinification()
            .setMinApi(parameters.getApiLevel())
            .addOptionsModification(options -> options.testing.readInputStackMaps = false)
            .addKeepMainRule(MAIN)
            .run(parameters.getRuntime(), MAIN);
    checkResult(result);
  }

  private void checkResult(TestRunResult<?> result) {
    switch (parameters.getDexRuntimeVersion()) {
      case V6_0_1:
        result.assertSuccessWithOutput("Completed successfully after 1000 iterations\n");
        break;
      case V5_1_1:
        result.assertSuccessWithOutput(EXPECTED);
        break;
      default:
        result.assertFailureWithErrorThatThrows(VerifyError.class);
        break;
    }
  }
}
