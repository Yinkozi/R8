// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.ir.optimize.inliner;

import static org.junit.Assert.assertEquals;

import com.android.tools.r8.TestBase;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestParametersCollection;
import com.android.tools.r8.TestRuntime;
import com.android.tools.r8.ToolHelper.DexVm;
import com.android.tools.r8.utils.codeinspector.InvokeInstructionSubject;
import com.android.tools.r8.utils.codeinspector.MethodSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// Test whether a single-caller method, called through a single-target library interface,
// is inlined.
@RunWith(Parameterized.class)
public class InlineLibraryInterfaceMethodTest extends TestBase {

  @Parameters(name = "{0}")
  public static TestParametersCollection data() {
    return getTestParameters().withDexRuntime(DexVm.ART_DEFAULT.getVersion()).build();
  }

  private final TestParameters testParameters;

  public InlineLibraryInterfaceMethodTest(TestParameters testParameters) {
    this.testParameters = testParameters;
  }

  @Test
  public void test() throws Exception {
    class Counts {
      private long run = 0;
      private long println = 0;
    }
    Counts counts = new Counts();
    TestRuntime testRuntime = testParameters.getRuntime();
    testForR8(Backend.DEX)
        .addInnerClasses(InlineLibraryInterfaceMethodTest.class)
        .addKeepMainRule(TestClass.class)
        .setMinApi(testRuntime)
        .noMinification()
        .run(testRuntime, TestClass.class)
        .assertSuccess()
        .inspect(
            inspector -> {
              MethodSubject methodSubject =
                  inspector.clazz(TestClass.class).uniqueMethodWithName("main").asMethodSubject();

              counts.run = countInvokesWithName(methodSubject, "run");
              counts.println = countInvokesWithName(methodSubject, "println");
            });

    // TODO(b/129044633) We expect 0 x run and 2 x println after we can inline those methods.
    assertEquals(2, counts.run);
    assertEquals(0, counts.println);
    // TODO(b/126323172) After this issue has been fixed, add test here to check the same with
    // desugared lambdas.
  }

  private static long countInvokesWithName(MethodSubject methodSubject, String name) {
    return methodSubject
        .streamInstructions()
        .filter(
            instruction ->
                instruction.isInvoke()
                    && ((InvokeInstructionSubject) instruction)
                        .invokedMethod()
                        .name
                        .toString()
                        .equals(name))
        .count();
  }

  // The test classes imitate desugared lambda classes.
  static class StatelessLambdaClass implements Runnable {
    public static final StatelessLambdaClass instance = new StatelessLambdaClass();

    @Override
    public void run() {
      TestClass.statelessLambdaImpl();
    }
  }

  static class StatefulLambdaClass implements Runnable {
    final String s;

    StatefulLambdaClass(String s) {
      this.s = s;
    }

    @Override
    public void run() {
      TestClass.statefulLambdaImpl(s);
    }
  }

  static class TestClass {
    public static int counter = 0;

    static void statelessLambdaImpl() {
      System.out.println("Running stateless lambda.");
      ++counter;
    }

    static void statefulLambdaImpl(String s) {
      System.out.println("Running stateful lambda: " + s + ".");
      counter += 10;
    }

    public static void main(String[] args) {
      Runnable runnable = StatelessLambdaClass.instance;
      runnable.run();

      String s = "lambda-state";
      runnable = new StatefulLambdaClass(s);
      runnable.run();

      if (counter != 11) {
        throw new RuntimeException("Counter is " + counter + ", expected: 11.");
      }
    }
  }
}
