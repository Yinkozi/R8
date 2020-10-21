// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.ir.optimize.unusedarguments;

import com.android.tools.r8.TestBase;
import com.android.tools.r8.ToolHelper;
import com.android.tools.r8.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/** Regression test for b/127691114. */
@RunWith(Parameterized.class)
public class UnusedArgumentsInstanceConstructorCollisionTest extends TestBase {

  private final Backend backend;

  @Parameters(name = "Backend: {0}")
  public static Backend[] parameters() {
    return ToolHelper.getBackends();
  }

  public UnusedArgumentsInstanceConstructorCollisionTest(Backend backend) {
    this.backend = backend;
  }

  @Test
  public void test() throws Exception {
    String expectedOutput = StringUtils.lines("C");
    testForR8(backend)
        .addInnerClasses(UnusedArgumentsInstanceConstructorCollisionTest.class)
        .addKeepMainRule(TestClass.class)
        .run(TestClass.class)
        .assertSuccessWithOutput(expectedOutput);
  }

  static class TestClass {

    public static void main(String[] args) {
      new C();
      new C(new Message("A"));
      new C(new Message("B"), new Message("C"));
    }
  }

  static class C {

    public C() {}

    public C(Message message) {}

    public C(Message message, Message other) {
      System.out.println(other.value);
    }
  }

  static class Message {

    public final String value;

    public Message(String value) {
      this.value = value;
    }
  }
}
