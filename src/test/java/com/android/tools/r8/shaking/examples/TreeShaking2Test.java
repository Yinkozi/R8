// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.shaking.examples;

import com.android.tools.r8.TestParameters;
import com.android.tools.r8.shaking.TreeShakingTest;
import com.android.tools.r8.utils.codeinspector.ClassSubject;
import com.android.tools.r8.utils.codeinspector.CodeInspector;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TreeShaking2Test extends TreeShakingTest {

  @Parameters(name = "mode:{0}-{1} minify:{2}")
  public static List<Object[]> data() {
    return defaultTreeShakingParameters();
  }

  public TreeShaking2Test(Frontend frontend, TestParameters parameters, MinifyMode minify) {
    super(frontend, parameters, minify);
  }

  @Override
  protected String getName() {
    return "examples/shaking2";
  }

  @Override
  protected String getMainClass() {
    return "shaking2.Shaking";
  }

  @Test
  public void testKeeprules() throws Exception {
    runTest(
        TreeShaking2Test::shaking2SuperClassIsAbstract,
        null,
        null,
        ImmutableList.of("src/test/examples/shaking2/keep-rules.txt"));
  }

  @Test
  public void testKeeprulesdontshrink() throws Exception {
    runTest(
        null,
        null,
        TreeShakingTest::checkSameStructure,
        ImmutableList.of("src/test/examples/shaking2/keep-rules-dont-shrink.txt"));
  }

  @Test
  public void testKeeprulesprintusage() throws Exception {
    runTest(
        null, null, null, ImmutableList.of("src/test/examples/shaking2/keep-rules-printusage.txt"));
  }

  private static void shaking2SuperClassIsAbstract(CodeInspector inspector) {
    ClassSubject clazz = inspector.clazz("shaking2.SuperClass");
    Assert.assertTrue(clazz.isAbstract());
    Assert.assertTrue(clazz.method("void", "virtualMethod", Collections.emptyList()).isAbstract());
    Assert.assertTrue(
        clazz
            .method(
                "void",
                "virtualMethod2",
                ImmutableList.of("int", "int", "int", "int", "int", "int", "int", "int"))
            .isAbstract());
  }
}
