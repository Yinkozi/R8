// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.enumunboxing;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import com.android.tools.r8.Diagnostic;
import com.android.tools.r8.TestBase;
import com.android.tools.r8.TestDiagnosticMessages;
import com.android.tools.r8.utils.BooleanUtils;
import com.android.tools.r8.utils.InternalOptions;
import com.android.tools.r8.utils.StringUtils;
import java.util.List;

public class EnumUnboxingTestBase extends TestBase {

  // Default keep rule present in Android Studio.
  private static final String KEEP_ENUM_STUDIO =
      "-keepclassmembers enum * {\n"
          + " public static **[] values();\n"
          + " public static ** valueOf(java.lang.String);\n"
          + "}";

  public enum EnumKeepRules {
    NONE(""),
    STUDIO(KEEP_ENUM_STUDIO);

    private final String keepRules;

    public String getKeepRules() {
      return keepRules;
    }

    public boolean isStudio() {
      return this == STUDIO;
    }

    EnumKeepRules(String keepRules) {
      this.keepRules = keepRules;
    }
  }

  public void assertLines2By2Correct(String string) {
    List<String> lines = StringUtils.splitLines(string);
    assert lines.size() % 2 == 0;
    for (int i = 0; i < lines.size(); i += 2) {
      assertEquals(
          "Different lines: " + lines.get(i) + " || " + lines.get(i + 1) + "\n" + string,
          lines.get(i),
          lines.get(i + 1));
    }
  }

  protected void enableEnumOptions(InternalOptions options, boolean enumValueOptimization) {
    options.enableEnumValueOptimization = enumValueOptimization;
    options.enableEnumSwitchMapRemoval = enumValueOptimization;
    options.testing.enableEnumUnboxingDebugLogs = true;
  }

  protected void assertEnumIsUnboxed(
      Class<?> enumClass, String testName, TestDiagnosticMessages m) {
    assertTrue(enumClass.isEnum());
    assertEnumIsUnboxed(enumClass.getSimpleName(), testName, m);
  }

  protected void assertEnumIsUnboxed(String enumClass, String testName, TestDiagnosticMessages m) {
    Diagnostic diagnostic = m.getInfos().get(0);
    assertTrue(diagnostic.getDiagnosticMessage().startsWith("Unboxed enums"));
    assertTrue(
        StringUtils.joinLines(
            "Expected enum to be removed (" + testName + "):",
            m.getInfos().get(1).getDiagnosticMessage()),
        diagnostic.getDiagnosticMessage().contains(enumClass));
  }

  void assertEnumIsBoxed(Class<?> enumClass, String testName, TestDiagnosticMessages m) {
    assertTrue(enumClass.isEnum());
    Diagnostic diagnostic = m.getInfos().get(1);
    assertTrue(diagnostic.getDiagnosticMessage().startsWith("Boxed enums"));
    assertTrue(
        "Expected enum NOT to be removed (" + testName + ")",
        diagnostic.getDiagnosticMessage().contains(enumClass.getSimpleName()));
  }

  static List<Object[]> enumUnboxingTestParameters() {
    return buildParameters(
        getTestParameters().withDexRuntimes().withAllApiLevels().build(),
        BooleanUtils.values(),
        getAllEnumKeepRules());
  }

  protected static EnumKeepRules[] getAllEnumKeepRules() {
    return EnumKeepRules.values();
  }
}
