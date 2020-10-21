// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.shaking.ifrule;

import com.android.tools.r8.shaking.forceproguardcompatibility.ProguardCompatibilityTestBase;
import com.android.tools.r8.utils.codeinspector.CodeInspector;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IfOnAnnotationTest extends ProguardCompatibilityTestBase {
  private static final List<Class<?>> CLASSES =
      ImmutableList.of(
          UsedAnnotation.class, UnusedAnnotation.class,
          UsedAnnotationDependent.class, UnusedAnnotationDependent.class,
          AnnotationUser.class, MainUsesAnnotationUser.class);

  private final Shrinker shrinker;

  public IfOnAnnotationTest(Shrinker shrinker) {
    this.shrinker = shrinker;
  }

  @Parameters(name = "shrinker: {0}")
  public static Collection<Object> data() {
    return ImmutableList.of(Shrinker.PROGUARD6, Shrinker.R8, Shrinker.R8_CF);
  }

  @Test
  public void ifOnAnnotation_onTargetMethod_withoutNthWildcard() throws Exception {
    List<String> config = ImmutableList.of(
        "-keepattributes *Annotation*",
        "-keep class **.Main* {",
        "  public static void main(java.lang.String[]);",
        "}",
        // @UsedAnnotation <methods> -> UsedAnnotationDependent
        "-if class **.*User {",
        "  @**.UsedAnnotation <methods>;",
        "}",
        "-keep class **.UsedAnnotation*",
        // @UnusedAnnotation <methods> -> UnusedAnnotationDependent
        "-if class **.*User {",
        "  @**.UnusedAnnotation <methods>;",
        "}",
        "-keep class **.UnusedAnnotation*"
    );

    CodeInspector codeInspector = inspectAfterShrinking(shrinker, CLASSES, config);
    verifyClassesAbsent(codeInspector,
        UnusedAnnotation.class, UnusedAnnotationDependent.class);
    verifyClassesPresent(codeInspector,
        UsedAnnotation.class, UsedAnnotationDependent.class);
  }

  @Test
  public void ifOnAnnotation_onTargetMethod_withNthWildcard() throws Exception {
    List<String> config = ImmutableList.of(
        "-keepattributes *Annotation*",
        "-keep class **.Main* {",
        "  public static void main(java.lang.String[]);",
        "}",
        // @UsedAnnotation <methods> -> UsedAnnotationDependent
        "-if class **.*User {",
        "  @<1>.Used<2> <methods>;",
        "}",
        "-keep class <1>.Used<2>*",
        // @UnusedAnnotation <methods> -> UnusedAnnotationDependent
        "-if class **.*User {",
        "  @<1>.Unused<2> <methods>;",
        "}",
        "-keep class <1>.Unused<2>*"
    );

    CodeInspector codeInspector = inspectAfterShrinking(shrinker, CLASSES, config);
    verifyClassesAbsent(codeInspector,
        UnusedAnnotation.class, UnusedAnnotationDependent.class);
    verifyClassesPresent(codeInspector,
        UsedAnnotation.class, UsedAnnotationDependent.class);
  }

  @Test
  public void ifOnAnnotation_onDependentClass_withNthWildcard() throws Exception {
    List<String> config = ImmutableList.of(
        "-keepattributes *Annotation*",
        "-keep class **.Main* {",
        "  public static void main(java.lang.String[]);",
        "}",
        // @UsedAnnotation <methods> -> @UsedAnnotation
        "-if class **.AnnotationUser {",
        "  @**.*Annotation <methods>;",
        "}",
        "-keep @interface <2>.<3>Annotation",
        // @*Annotation -> @*Annotation class *AnnotationDependent
        "-if @interface **.*Annotation",
        "-keep @<1>.<2>Annotation class <1>.*"
    );

    CodeInspector codeInspector = inspectAfterShrinking(shrinker, CLASSES, config);
    verifyClassesAbsent(codeInspector,
        UnusedAnnotation.class, UnusedAnnotationDependent.class);
    verifyClassesPresent(codeInspector,
        UsedAnnotation.class, UsedAnnotationDependent.class);
  }

}
