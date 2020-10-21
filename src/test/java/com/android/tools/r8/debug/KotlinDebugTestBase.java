// Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.debug;

import java.util.Arrays;
import java.util.List;
import org.apache.harmony.jpda.tests.framework.jdwp.Frame.Variable;
import org.apache.harmony.jpda.tests.framework.jdwp.Location;

/**
 * A specialization for Kotlin-based tests which provides extra commands.
 */
public abstract class KotlinDebugTestBase extends DebugTestBase {

  protected final JUnit3Wrapper.Command kotlinStepOver() {
    return testBaseBeforeStep -> {
      final JUnit3Wrapper.DebuggeeState debuggeeStateBeforeStep = testBaseBeforeStep
          .getDebuggeeState();
      final int frameDepthBeforeStep = debuggeeStateBeforeStep.getFrameDepth();
      final Location locationBeforeStep = debuggeeStateBeforeStep.getLocation();
      final List<Variable> kotlinLvsBeforeStep = getVisibleKotlinInlineVariables(
          debuggeeStateBeforeStep);

      // This is the command that will be executed after the initial (normal) step over. If we
      // reach an inlined location, this command will step until reaching a non-inlined location.
      JUnit3Wrapper.Command commandAfterStep = testBaseAfterStep -> {
        // Get the new debuggee state (previous one is stale).
        JUnit3Wrapper.DebuggeeState debuggeeStateAfterStep = testBaseBeforeStep.getDebuggeeState();

        // Are we in the same frame ?
        final int frameDepthAfterStep = debuggeeStateAfterStep.getFrameDepth();
        final Location locationAfterStep = debuggeeStateAfterStep.getLocation();
        if (frameDepthBeforeStep == frameDepthAfterStep
            && locationBeforeStep.classID == locationAfterStep.classID
            && locationBeforeStep.methodID == locationAfterStep.methodID) {
          // We remain in the same method. Do we step into an inlined section ?
          List<Variable> kotlinLvsAfterStep = getVisibleKotlinInlineVariables(
              debuggeeStateAfterStep);
          if (kotlinLvsBeforeStep.isEmpty() && !kotlinLvsAfterStep.isEmpty()) {
            assert kotlinLvsAfterStep.size() == 1;

            // We're located in an inlined section. Instead of doing a classic step out, we must
            // jump out of the inlined section.
            Variable inlinedSectionLv = kotlinLvsAfterStep.get(0);
            testBaseAfterStep.enqueueCommandFirst(stepUntilOutOfInlineScope(inlinedSectionLv));
          }
        }
      };

      // Step over then check whether we need to continue stepping.
      testBaseBeforeStep.enqueueCommandsFirst(Arrays.asList(stepOver(), commandAfterStep));
    };
  }

  protected final JUnit3Wrapper.Command kotlinStepOut() {
    return wrapper -> {
      final List<Variable> kotlinLvsBeforeStep = getVisibleKotlinInlineVariables(
          wrapper.getDebuggeeState());

      JUnit3Wrapper.Command nextCommand;
      if (!kotlinLvsBeforeStep.isEmpty()) {
        // We are in an inline section. We need to step until being out of inline scope.
        assert kotlinLvsBeforeStep.size() == 1;
        final Variable inlinedSectionLv = kotlinLvsBeforeStep.get(0);
        nextCommand = stepUntilOutOfInlineScope(inlinedSectionLv);
      } else {
        nextCommand = stepOut();
      }
      wrapper.enqueueCommandFirst(nextCommand);
    };
  }

  private JUnit3Wrapper.Command stepUntilOutOfInlineScope(Variable inlineScopeLv) {
    return stepUntil(StepKind.OVER, StepLevel.LINE, debuggeeState -> {
      boolean inInlineScope = JUnit3Wrapper
          .inScope(debuggeeState.getLocation().index, inlineScopeLv);
      return !inInlineScope;
    });
  }

}
