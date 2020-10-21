// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.retrace.mappings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.android.tools.r8.references.FieldReference;
import com.android.tools.r8.references.Reference;
import com.android.tools.r8.retrace.RetraceApi;
import com.android.tools.r8.retrace.RetraceFieldResult;
import com.android.tools.r8.utils.StringUtils;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Collectors;

public class FieldsWithSameMinifiedNameMapping implements MappingForTest {

  @Override
  public String mapping() {
    // TODO(b/169953605): Add enough information to the map to allow precise retracing.
    return StringUtils.lines(
        "foo.bar.Baz -> foo.bar.Baz:", "  java.lang.Object f1 -> a", "  java.lang.String f2 -> a");
  }

  public void inspect(RetraceApi retracer) {
    FieldReference f1FieldReference =
        Reference.field(
            Reference.classFromTypeName("foo.bar.Baz"),
            "f1",
            Reference.classFromTypeName("java.lang.Object"));
    FieldReference f2FieldReference =
        Reference.field(
            Reference.classFromTypeName("foo.bar.Baz"),
            "f2",
            Reference.classFromTypeName("java.lang.String"));

    FieldReference mappedF1FieldReference =
        Reference.field(
            Reference.classFromTypeName("foo.bar.Baz"),
            "a",
            Reference.classFromTypeName("java.lang.Object"));

    RetraceFieldResult result = retracer.retrace(mappedF1FieldReference);
    // TODO(b/169829306): Result should not be ambigious.
    assertTrue(result.isAmbiguous());

    List<FieldReference> retracedFields =
        result.stream()
            .map(f -> f.getField().asKnown().getFieldReference())
            .collect(Collectors.toList());
    assertEquals(ImmutableList.of(f1FieldReference, f2FieldReference), retracedFields);
  }
}
