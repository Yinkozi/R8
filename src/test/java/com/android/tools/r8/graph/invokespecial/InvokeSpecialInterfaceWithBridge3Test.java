// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.graph.invokespecial;

import static com.android.tools.r8.utils.DescriptorUtils.getBinaryNameFromJavaType;
import static org.junit.Assert.assertEquals;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import com.android.tools.r8.TestBase;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestRunResult;
import com.android.tools.r8.utils.BooleanUtils;
import com.android.tools.r8.utils.StringUtils;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InvokeSpecialInterfaceWithBridge3Test extends TestBase {

  private static final String EXPECTED = StringUtils.lines("Hello World!");

  private final TestParameters parameters;
  private final boolean isInterface;

  @Parameters(name = "{0}, itf:{1}")
  public static List<Object[]> data() {
    return buildParameters(
        getTestParameters().withAllRuntimesAndApiLevels().build(), BooleanUtils.values());
  }

  public InvokeSpecialInterfaceWithBridge3Test(TestParameters parameters, boolean isInterface) {
    this.parameters = parameters;
    this.isInterface = isInterface;
  }

  @Test
  public void testRuntime() throws Exception {
    TestRunResult<?> result =
        testForRuntime(parameters.getRuntime(), parameters.getApiLevel())
            .addProgramClasses(I.class, A.class, Main.class)
            .addProgramClassFileData(getClassWithTransformedInvoked())
            .run(parameters.getRuntime(), Main.class);
    if (parameters.isDexRuntime()) {
      // TODO(b/166210854): Runs but should have failed.
      result.assertSuccessWithOutput(EXPECTED);
    } else {
      result.assertFailureWithErrorThatThrows(IncompatibleClassChangeError.class);
    }
  }

  @Test
  public void testR8() throws Exception {
    testForR8(parameters.getBackend())
        .addProgramClasses(I.class, A.class, Main.class)
        .addProgramClassFileData(getClassWithTransformedInvoked())
        .addKeepMainRule(Main.class)
        .setMinApi(parameters.getApiLevel())
        .run(parameters.getRuntime(), Main.class)
        // TODO(b/166210854): Runs but should have failed.
        .assertSuccessWithOutput(EXPECTED);
  }

  private byte[] getClassWithTransformedInvoked() throws IOException {
    return transformer(B.class)
        .transformMethodInsnInMethod(
            "bar",
            (opcode, owner, name, descriptor, isInterface, continuation) -> {
              assertEquals(INVOKEVIRTUAL, opcode);
              assertEquals(owner, getBinaryNameFromJavaType(B.class.getTypeName()));
              continuation.visitMethodInsn(
                  INVOKESPECIAL,
                  getBinaryNameFromJavaType(I.class.getTypeName()),
                  name,
                  descriptor,
                  isInterface);
            })
        .transform();
  }

  public interface I {
    default void foo() {
      System.out.println("Hello World!");
    }
  }

  public static class A implements I {}

  public static class B extends A {

    public void bar() {
      foo(); // Will be rewritten to invoke-special I.foo()
    }
  }

  public static class Main {

    public static void main(String[] args) {
      new B().bar();
    }
  }
}
