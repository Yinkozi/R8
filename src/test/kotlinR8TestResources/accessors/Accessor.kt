// Copyright (c) 2018, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package accessors

class Accessor {
    companion object {
        private val property = "foo"

        fun accessPropertyFromCompanionClass() {
            println(property)
        }
    }

    fun accessPropertyFromOuterClass() {
        println(property)
    }
}

fun accessor_accessPropertyFromCompanionClass() {
    Accessor.accessPropertyFromCompanionClass()
}

fun accessor_accessPropertyFromOuterClass() {
    Accessor().accessPropertyFromOuterClass()
}