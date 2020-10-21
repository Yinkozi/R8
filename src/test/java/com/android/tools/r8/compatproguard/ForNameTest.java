// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.compatproguard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.android.tools.r8.code.ConstString;
import com.android.tools.r8.code.InvokeStatic;
import com.android.tools.r8.code.ReturnVoid;
import com.android.tools.r8.graph.DexCode;
import com.android.tools.r8.smali.SmaliBuilder;
import com.android.tools.r8.utils.codeinspector.ClassSubject;
import com.android.tools.r8.utils.codeinspector.CodeInspector;
import com.android.tools.r8.utils.codeinspector.MethodSubject;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Test;

public class ForNameTest extends CompatProguardSmaliTestBase {

  private final String CLASS_NAME = "Example";
  private final static String BOO = "Boo";

  @Test
  public void forName_renamed() throws Exception {
    SmaliBuilder builder = new SmaliBuilder(CLASS_NAME);
    builder.addMainMethod(
        1,
        "const-string v0, \"" + BOO + "\"",
        "invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;",
        "move-result-object v0",
        "return-void");
    builder.addClass(BOO);

    List<String> pgConfigs = ImmutableList.of(
        keepMainProguardConfiguration(CLASS_NAME),
        "-dontshrink",
        "-dontoptimize");
    CodeInspector inspector = runCompatProguard(builder, pgConfigs);

    ClassSubject clazz = inspector.clazz(CLASS_NAME);
    assertTrue(clazz.isPresent());
    MethodSubject method = clazz.method(CodeInspector.MAIN);
    assertTrue(method.isPresent());

    DexCode code = method.getMethod().getCode().asDexCode();
    assertTrue(code.instructions[0] instanceof ConstString);
    ConstString constString = (ConstString) code.instructions[0];
    assertNotEquals(BOO, constString.getString().toString());
    assertTrue(code.instructions[1] instanceof InvokeStatic);
    assertTrue(code.instructions[2] instanceof ReturnVoid);
  }

  @Test
  public void forName_noMinification() throws Exception {
    SmaliBuilder builder = new SmaliBuilder(CLASS_NAME);
    builder.addMainMethod(
        1,
        "const-string v0, \"" + BOO + "\"",
        "invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;",
        "move-result-object v0",
        "return-void");
    builder.addClass(BOO);

    List<String> pgConfigs = ImmutableList.of(
        keepMainProguardConfiguration(CLASS_NAME),
        "-dontshrink",
        "-dontoptimize",
        "-dontobfuscate");
    CodeInspector inspector = runCompatProguard(builder, pgConfigs);

    ClassSubject clazz = inspector.clazz(CLASS_NAME);
    assertTrue(clazz.isPresent());
    MethodSubject method = clazz.method(CodeInspector.MAIN);
    assertTrue(method.isPresent());

    DexCode code = method.getMethod().getCode().asDexCode();
    assertTrue(code.instructions[0] instanceof ConstString);
    ConstString constString = (ConstString) code.instructions[0];
    assertEquals(BOO, constString.getString().toString());
    assertTrue(code.instructions[1] instanceof InvokeStatic);
    assertTrue(code.instructions[2] instanceof ReturnVoid);
  }

}
