// Copyright (c) 2019, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.naming.b139991218;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

// This is the generated bytecode for a kotlin data class:
// data class Alpha(val id: String = next())
// defined in com.android.tools.r8.naming.b139991218.Main.java.
public class Alpha implements Opcodes {

  public static byte[] dump() {

    ClassWriter classWriter = new ClassWriter(0);
    FieldVisitor fieldVisitor;
    MethodVisitor methodVisitor;
    AnnotationVisitor annotationVisitor0;

    classWriter.visit(
        V1_8,
        ACC_PUBLIC | ACC_FINAL | ACC_SUPER,
        "com/android/tools/r8/naming/b139991218/Alpha",
        null,
        "java/lang/Object",
        null);

    classWriter.visitSource("main.kt", null);

    {
      annotationVisitor0 = classWriter.visitAnnotation("Lkotlin/Metadata;", true);
      annotationVisitor0.visit("mv", new int[] {1, 1, 13});
      annotationVisitor0.visit("bv", new int[] {1, 0, 3});
      annotationVisitor0.visit("k", new Integer(1));
      {
        AnnotationVisitor annotationVisitor1 = annotationVisitor0.visitArray("d1");
        annotationVisitor1.visit(
            null,
            "\u0000 \n"
                + "\u0002\u0018\u0002\n"
                + "\u0002\u0010\u0000\n"
                + "\u0000\n"
                + "\u0002\u0010\u000e\n"
                + "\u0002\u0008\u0006\n"
                + "\u0002\u0010\u000b\n"
                + "\u0002\u0008\u0002\n"
                + "\u0002\u0010\u0008\n"
                + "\u0000\u0008\u0086\u0008\u0018\u00002\u00020\u0001B\u000f\u0012\u0008\u0008\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0009\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\u0008\u001a\u00020\u00002\u0008\u0008\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0009\u001a\u00020\n"
                + "2\u0008\u0010\u000b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u0009\u0010\u000c\u001a\u00020\r"
                + "H\u00d6\u0001J\u0009\u0010\u000e\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0008\n"
                + "\u0000\u001a\u0004\u0008\u0005\u0010\u0006");
        annotationVisitor1.visitEnd();
      }
      {
        AnnotationVisitor annotationVisitor1 = annotationVisitor0.visitArray("d2");
        annotationVisitor1.visit(null, "Lcom/android/tools/r8/naming/b139991218/Alpha;");
        annotationVisitor1.visit(null, "");
        annotationVisitor1.visit(null, "id");
        annotationVisitor1.visit(null, "");
        annotationVisitor1.visit(null, "(Ljava/lang/String;)V");
        annotationVisitor1.visit(null, "getId");
        annotationVisitor1.visit(null, "()Ljava/lang/String;");
        annotationVisitor1.visit(null, "component1");
        annotationVisitor1.visit(null, "copy");
        annotationVisitor1.visit(null, "equals");
        annotationVisitor1.visit(null, "");
        annotationVisitor1.visit(null, "other");
        annotationVisitor1.visit(null, "hashCode");
        annotationVisitor1.visit(null, "");
        annotationVisitor1.visit(null, "toString");
        annotationVisitor1.visitEnd();
      }
      annotationVisitor0.visitEnd();
    }
    {
      fieldVisitor =
          classWriter.visitField(ACC_PRIVATE | ACC_FINAL, "id", "Ljava/lang/String;", null, null);
      {
        annotationVisitor0 =
            fieldVisitor.visitAnnotation("Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      fieldVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC | ACC_FINAL, "getId", "()Ljava/lang/String;", null, null);
      {
        annotationVisitor0 =
            methodVisitor.visitAnnotation("Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitLineNumber(11, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable(
          "this", "Lcom/android/tools/r8/naming/b139991218/Alpha;", null, label0, label1, 0);
      methodVisitor.visitMaxs(1, 1);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;)V", null, null);
      methodVisitor.visitAnnotableParameterCount(1, false);
      {
        annotationVisitor0 =
            methodVisitor.visitParameterAnnotation(0, "Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitLdcInsn("id");
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "kotlin/jvm/internal/Intrinsics",
          "checkParameterIsNotNull",
          "(Ljava/lang/Object;Ljava/lang/String;)V",
          false);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLineNumber(11, label1);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitFieldInsn(
          PUTFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitInsn(RETURN);
      Label label2 = new Label();
      methodVisitor.visitLabel(label2);
      methodVisitor.visitLocalVariable(
          "this", "Lcom/android/tools/r8/naming/b139991218/Alpha;", null, label0, label2, 0);
      methodVisitor.visitLocalVariable("id", "Ljava/lang/String;", null, label0, label2, 1);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC | ACC_SYNTHETIC,
              "<init>",
              "(Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V",
              null,
              null);
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(ILOAD, 2);
      methodVisitor.visitInsn(ICONST_1);
      methodVisitor.visitInsn(IAND);
      Label label0 = new Label();
      methodVisitor.visitJumpInsn(IFEQ, label0);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLineNumber(11, label1);
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "com/android/tools/r8/naming/b139991218/Main",
          "access$next",
          "()Ljava/lang/String;",
          false);
      methodVisitor.visitVarInsn(ASTORE, 1);
      methodVisitor.visitLabel(label0);
      methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKESPECIAL,
          "com/android/tools/r8/naming/b139991218/Alpha",
          "<init>",
          "(Ljava/lang/String;)V",
          false);
      methodVisitor.visitInsn(RETURN);
      methodVisitor.visitMaxs(2, 4);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitInsn(ACONST_NULL);
      methodVisitor.visitInsn(ICONST_1);
      methodVisitor.visitInsn(ACONST_NULL);
      methodVisitor.visitMethodInsn(
          INVOKESPECIAL,
          "com/android/tools/r8/naming/b139991218/Alpha",
          "<init>",
          "(Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V",
          false);
      methodVisitor.visitInsn(RETURN);
      methodVisitor.visitMaxs(4, 1);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC | ACC_FINAL, "component1", "()Ljava/lang/String;", null, null);
      {
        annotationVisitor0 =
            methodVisitor.visitAnnotation("Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable(
          "this", "Lcom/android/tools/r8/naming/b139991218/Alpha;", null, label0, label1, 0);
      methodVisitor.visitMaxs(1, 1);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC | ACC_FINAL,
              "copy",
              "(Ljava/lang/String;)Lcom/android/tools/r8/naming/b139991218/Alpha;",
              null,
              null);
      {
        annotationVisitor0 =
            methodVisitor.visitAnnotation("Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitAnnotableParameterCount(1, false);
      {
        annotationVisitor0 =
            methodVisitor.visitParameterAnnotation(0, "Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitCode();
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitLdcInsn("id");
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "kotlin/jvm/internal/Intrinsics",
          "checkParameterIsNotNull",
          "(Ljava/lang/Object;Ljava/lang/String;)V",
          false);
      methodVisitor.visitTypeInsn(NEW, "com/android/tools/r8/naming/b139991218/Alpha");
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKESPECIAL,
          "com/android/tools/r8/naming/b139991218/Alpha",
          "<init>",
          "(Ljava/lang/String;)V",
          false);
      methodVisitor.visitInsn(ARETURN);
      Label label1 = new Label();
      methodVisitor.visitLabel(label1);
      methodVisitor.visitLocalVariable(
          "this", "Lcom/android/tools/r8/naming/b139991218/Alpha;", null, label0, label1, 0);
      methodVisitor.visitLocalVariable("id", "Ljava/lang/String;", null, label0, label1, 1);
      methodVisitor.visitMaxs(3, 2);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(
              ACC_PUBLIC | ACC_STATIC | ACC_SYNTHETIC,
              "copy$default",
              "(Lcom/android/tools/r8/naming/b139991218/Alpha;Ljava/lang/String;ILjava/lang/Object;)Lcom/android/tools/r8/naming/b139991218/Alpha;",
              null,
              null);
      {
        annotationVisitor0 =
            methodVisitor.visitAnnotation("Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(ILOAD, 2);
      methodVisitor.visitInsn(ICONST_1);
      methodVisitor.visitInsn(IAND);
      Label label0 = new Label();
      methodVisitor.visitJumpInsn(IFEQ, label0);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitVarInsn(ASTORE, 1);
      methodVisitor.visitLabel(label0);
      methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL,
          "com/android/tools/r8/naming/b139991218/Alpha",
          "copy",
          "(Ljava/lang/String;)Lcom/android/tools/r8/naming/b139991218/Alpha;",
          false);
      methodVisitor.visitInsn(ARETURN);
      methodVisitor.visitMaxs(2, 4);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
      {
        annotationVisitor0 =
            methodVisitor.visitAnnotation("Lorg/jetbrains/annotations/NotNull;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitCode();
      methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitMethodInsn(
          INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
      methodVisitor.visitLdcInsn("Alpha(id=");
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL,
          "java/lang/StringBuilder",
          "append",
          "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
          false);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL,
          "java/lang/StringBuilder",
          "append",
          "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
          false);
      methodVisitor.visitLdcInsn(")");
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL,
          "java/lang/StringBuilder",
          "append",
          "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
          false);
      methodVisitor.visitMethodInsn(
          INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
      methodVisitor.visitInsn(ARETURN);
      methodVisitor.visitMaxs(2, 1);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitInsn(DUP);
      Label label0 = new Label();
      methodVisitor.visitJumpInsn(IFNULL, label0);
      methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "hashCode", "()I", false);
      Label label1 = new Label();
      methodVisitor.visitJumpInsn(GOTO, label1);
      methodVisitor.visitLabel(label0);
      methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/String"});
      methodVisitor.visitInsn(POP);
      methodVisitor.visitInsn(ICONST_0);
      methodVisitor.visitLabel(label1);
      methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {Opcodes.INTEGER});
      methodVisitor.visitInsn(IRETURN);
      methodVisitor.visitMaxs(2, 1);
      methodVisitor.visitEnd();
    }
    {
      methodVisitor =
          classWriter.visitMethod(ACC_PUBLIC, "equals", "(Ljava/lang/Object;)Z", null, null);
      methodVisitor.visitAnnotableParameterCount(1, false);
      {
        annotationVisitor0 =
            methodVisitor.visitParameterAnnotation(
                0, "Lorg/jetbrains/annotations/Nullable;", false);
        annotationVisitor0.visitEnd();
      }
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      Label label0 = new Label();
      methodVisitor.visitJumpInsn(IF_ACMPEQ, label0);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitTypeInsn(INSTANCEOF, "com/android/tools/r8/naming/b139991218/Alpha");
      Label label1 = new Label();
      methodVisitor.visitJumpInsn(IFEQ, label1);
      methodVisitor.visitVarInsn(ALOAD, 1);
      methodVisitor.visitTypeInsn(CHECKCAST, "com/android/tools/r8/naming/b139991218/Alpha");
      methodVisitor.visitVarInsn(ASTORE, 2);
      methodVisitor.visitVarInsn(ALOAD, 0);
      methodVisitor.visitFieldInsn(
          GETFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitVarInsn(ALOAD, 2);
      methodVisitor.visitFieldInsn(
          GETFIELD, "com/android/tools/r8/naming/b139991218/Alpha", "id", "Ljava/lang/String;");
      methodVisitor.visitMethodInsn(
          INVOKESTATIC,
          "kotlin/jvm/internal/Intrinsics",
          "areEqual",
          "(Ljava/lang/Object;Ljava/lang/Object;)Z",
          false);
      methodVisitor.visitJumpInsn(IFEQ, label1);
      methodVisitor.visitLabel(label0);
      methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      methodVisitor.visitInsn(ICONST_1);
      methodVisitor.visitInsn(IRETURN);
      methodVisitor.visitLabel(label1);
      methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      methodVisitor.visitInsn(ICONST_0);
      methodVisitor.visitInsn(IRETURN);
      methodVisitor.visitMaxs(2, 3);
      methodVisitor.visitEnd();
    }
    classWriter.visitEnd();

    return classWriter.toByteArray();
  }
}
