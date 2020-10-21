// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package lambdas_jstyle_runnable

private val liveChecker = object : Runnable {
  override fun run() {
    if (!Thread.currentThread().isInterrupted) {
      publish("liveChecker")
    }
  }
}

class Implementer1 {
  fun getRunnable() : Runnable {
    return liveChecker
  }
}
