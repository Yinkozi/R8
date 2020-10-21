// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.shaking.forceproguardcompatibility.keepattributes;

import com.android.tools.r8.AssumeMayHaveSideEffects;

public class TestKeepAttributes {
  public static class InnerClass {

    @AssumeMayHaveSideEffects
    InnerClass() {}
  }

  private static void methodWithMemberClass() {
    class MemberClass {

      @AssumeMayHaveSideEffects
      private MemberClass() {}
    }

    new MemberClass();
  }

  public static void main(String[] args) {
    new InnerClass();
    methodWithMemberClass();
    System.out.print(TestKeepAttributes.class.getDeclaredClasses().length);
  }
}
