// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.android.tools.r8.synthesis;

import com.android.tools.r8.graph.DexClass;
import com.android.tools.r8.graph.DexType;
import com.android.tools.r8.graph.GraphLens.NonIdentityGraphLens;
import java.util.function.Function;

/**
 * Base type for a reference to a synthetic item.
 *
 * <p>This class is internal to the synthetic items collection, thus package-protected.
 */
abstract class SyntheticReference {
  private final SynthesizingContext context;

  SyntheticReference(SynthesizingContext context) {
    this.context = context;
  }

  abstract SyntheticDefinition lookupDefinition(Function<DexType, DexClass> definitions);

  final SynthesizingContext getContext() {
    return context;
  }

  abstract DexType getHolder();

  abstract SyntheticReference rewrite(NonIdentityGraphLens lens);
}
