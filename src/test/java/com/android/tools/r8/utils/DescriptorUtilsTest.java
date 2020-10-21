// Copyright (c) 2016, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Paths;
import org.junit.Test;

public class DescriptorUtilsTest {

  @Test
  public void toShorty() {
    assertEquals("Z", DescriptorUtils.javaTypeToShorty("boolean"));
    assertEquals("B", DescriptorUtils.javaTypeToShorty("byte"));
    assertEquals("S", DescriptorUtils.javaTypeToShorty("short"));
    assertEquals("C", DescriptorUtils.javaTypeToShorty("char"));
    assertEquals("I", DescriptorUtils.javaTypeToShorty("int"));
    assertEquals("J", DescriptorUtils.javaTypeToShorty("long"));
    assertEquals("F", DescriptorUtils.javaTypeToShorty("float"));
    assertEquals("D", DescriptorUtils.javaTypeToShorty("double"));
    assertEquals("L", DescriptorUtils.javaTypeToShorty("int[]"));
    assertEquals("L", DescriptorUtils.javaTypeToShorty("int[][]"));
    assertEquals("L", DescriptorUtils.javaTypeToShorty("java.lang.Object"));
    assertEquals("L", DescriptorUtils.javaTypeToShorty("a.b.C"));
  }

  @Test
  public void toDescriptor() {
    assertEquals("Z", DescriptorUtils.javaTypeToDescriptor("boolean"));
    assertEquals("B", DescriptorUtils.javaTypeToDescriptor("byte"));
    assertEquals("S", DescriptorUtils.javaTypeToDescriptor("short"));
    assertEquals("C", DescriptorUtils.javaTypeToDescriptor("char"));
    assertEquals("I", DescriptorUtils.javaTypeToDescriptor("int"));
    assertEquals("J", DescriptorUtils.javaTypeToDescriptor("long"));
    assertEquals("F", DescriptorUtils.javaTypeToDescriptor("float"));
    assertEquals("D", DescriptorUtils.javaTypeToDescriptor("double"));
    assertEquals("[I", DescriptorUtils.javaTypeToDescriptor("int[]"));
    assertEquals("[[I", DescriptorUtils.javaTypeToDescriptor("int[][]"));
    assertEquals("Ljava/lang/Object;", DescriptorUtils.javaTypeToDescriptor("java.lang.Object"));
    assertEquals("La/b/C;", DescriptorUtils.javaTypeToDescriptor("a.b.C"));
  }

  @Test
  public void fromDescriptor() {
    String obj = "Ljava/lang/Object;";
    assertEquals("Object", DescriptorUtils.getUnqualifiedClassNameFromDescriptor(obj));
    assertEquals("java.lang.Object", DescriptorUtils.getClassNameFromDescriptor(obj));
    assertEquals("java.lang", DescriptorUtils.getPackageNameFromDescriptor(obj));
    assertEquals("java/lang/Object", DescriptorUtils.getClassBinaryNameFromDescriptor(obj));
  }

  @Test
  public void toJavaType() {
    assertEquals("boolean", DescriptorUtils.descriptorToJavaType("Z"));
    assertEquals("byte", DescriptorUtils.descriptorToJavaType("B"));
    assertEquals("short", DescriptorUtils.descriptorToJavaType("S"));
    assertEquals("char", DescriptorUtils.descriptorToJavaType("C"));
    assertEquals("int", DescriptorUtils.descriptorToJavaType("I"));
    assertEquals("long", DescriptorUtils.descriptorToJavaType("J"));
    assertEquals("float", DescriptorUtils.descriptorToJavaType("F"));
    assertEquals("double", DescriptorUtils.descriptorToJavaType("D"));
    assertEquals("int[]", DescriptorUtils.descriptorToJavaType("[I"));
    assertEquals("int[][]", DescriptorUtils.descriptorToJavaType("[[I"));
    assertEquals("java.lang.Object", DescriptorUtils.descriptorToJavaType("Ljava/lang/Object;"));
    assertEquals("a.b.C", DescriptorUtils.descriptorToJavaType("La/b/C;"));
  }

  @Test
  public void guessClassDescriptor() {
    String obj = "java/lang/Object.class";
    assertEquals("Ljava/lang/Object;", DescriptorUtils.guessTypeDescriptor(obj));
    String objBackslash = "java\\lang\\Object.class";
    assertEquals("Ljava\\lang\\Object;", DescriptorUtils.guessTypeDescriptor(objBackslash));
    String objFileSeparatorChar =
        "java" + File.separatorChar + "lang" + File.separatorChar + "Object.class";
    assertEquals("Ljava/lang/Object;",
        DescriptorUtils.guessTypeDescriptor(Paths.get(objFileSeparatorChar)));
  }
}
