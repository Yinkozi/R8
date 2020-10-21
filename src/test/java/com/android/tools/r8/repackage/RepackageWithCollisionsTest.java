// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.repackage;

import static com.android.tools.r8.utils.codeinspector.Matchers.isPresentAndNotRenamed;
import static org.hamcrest.MatcherAssert.assertThat;

import com.android.tools.r8.TestParameters;
import com.android.tools.r8.utils.codeinspector.CodeInspector;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class RepackageWithCollisionsTest extends RepackageTestBase {

  public RepackageWithCollisionsTest(
      String flattenPackageHierarchyOrRepackageClasses, TestParameters parameters) {
    super(flattenPackageHierarchyOrRepackageClasses, parameters);
  }

  @Override
  public String getRepackagePackage() {
    return com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.Foo
        .class
        .getPackage()
        .getName();
  }

  @Test
  public void test() throws Exception {
    testForR8(parameters.getBackend())
        .addInnerClasses(getClass())
        .addProgramClasses(getTestClasses())
        .addKeepMainRule(TestClass.class)
        .addKeepRules("-keep class " + getRepackagePackage() + ".** { *; }")
        .addKeepAttributeInnerClassesAndEnclosingMethod()
        .apply(this::configureRepackaging)
        .enableNeverClassInliningAnnotations()
        .setMinApi(parameters.getApiLevel())
        .compile()
        .inspect(this::inspect)
        .run(parameters.getRuntime(), TestClass.class)
        .assertSuccessWithOutputLines(
            "first.Foo",
            "first.Foo$Bar",
            "first.first.Foo",
            "first.first.Foo$Bar",
            "second.Foo",
            "second.Foo$Bar",
            "destination.Foo",
            "destination.Foo$Bar",
            "destination.first.Foo",
            "destination.first.Foo$Bar");
  }

  private void inspect(CodeInspector inspector) {
    Iterator<Class<?>> testClassesIterator = getTestClasses().iterator();

    Class<?> firstFoo = testClassesIterator.next();
    assertThat(firstFoo, isRepackagedAsExpected(inspector, "a"));

    Class<?> firstFooBar = testClassesIterator.next();
    assertThat(firstFooBar, isRepackagedAsExpected(inspector, "a"));

    Class<?> firstFirstFoo = testClassesIterator.next();
    assertThat(firstFirstFoo, isRepackagedAsExpected(inspector, "b"));

    Class<?> firstFirstFooBar = testClassesIterator.next();
    assertThat(firstFirstFooBar, isRepackagedAsExpected(inspector, "b"));

    Class<?> secondFoo = testClassesIterator.next();
    assertThat(secondFoo, isRepackagedAsExpected(inspector, "c"));

    Class<?> secondBar = testClassesIterator.next();
    assertThat(secondBar, isRepackagedAsExpected(inspector, "c"));

    Class<?> destinationFoo = testClassesIterator.next();
    assertThat(inspector.clazz(destinationFoo), isPresentAndNotRenamed());

    Class<?> destinationFooBar = testClassesIterator.next();
    assertThat(inspector.clazz(destinationFooBar), isPresentAndNotRenamed());

    Class<?> destinationFirstFoo = testClassesIterator.next();
    assertThat(inspector.clazz(destinationFirstFoo), isPresentAndNotRenamed());

    Class<?> destinationFirstBar = testClassesIterator.next();
    assertThat(inspector.clazz(destinationFirstBar), isPresentAndNotRenamed());
  }

  private static List<Class<?>> getTestClasses() {
    return ImmutableList.of(
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.Foo.class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.Foo.Bar.class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.first.Foo
            .class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.first.Foo.Bar
            .class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.second.Foo.class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.second.Foo.Bar.class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.Foo
            .class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.Foo.Bar
            .class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.first.Foo
            .class,
        com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.first.Foo
            .Bar.class);
  }

  public static class TestClass {

    public static void main(String[] args) {
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.Foo();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.Foo.Bar();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.first.Foo();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.first.first.Foo
          .Bar();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.second.Foo();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.second.Foo.Bar();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.Foo();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.Foo
          .Bar();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.first
          .Foo();
      new com.android.tools.r8.repackage.testclasses.repackagewithcollisionstest.destination.first
          .Foo.Bar();
    }
  }
}
