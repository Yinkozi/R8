// Copyright (c) 2020, the R8 project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

package com.android.tools.r8.rewrite.serviceloaders;

import static com.android.tools.r8.DiagnosticsMatcher.diagnosticMessage;
import static org.hamcrest.CoreMatchers.containsString;

import com.android.tools.r8.DataEntryResource;
import com.android.tools.r8.TestBase;
import com.android.tools.r8.TestParameters;
import com.android.tools.r8.TestParametersCollection;
import com.android.tools.r8.graph.AppServices;
import com.android.tools.r8.origin.Origin;
import com.android.tools.r8.rewrite.serviceloaders.MissingServiceImplementationClassTest.Service;
import com.android.tools.r8.utils.StringUtils;
import java.util.ServiceLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// Test where a service implementation class is missing in presence of feature splits.
@RunWith(Parameterized.class)
public class MissingServiceImplementationClassWithFeatureTest extends TestBase {

  private final TestParameters parameters;

  @Parameters(name = "{0}")
  public static TestParametersCollection data() {
    return getTestParameters().withDexRuntimes().withAllApiLevels().build();
  }

  public MissingServiceImplementationClassWithFeatureTest(TestParameters parameters) {
    this.parameters = parameters;
  }

  @Test
  public void testR8() throws Exception {
    testForR8(parameters.getBackend())
        .addProgramClasses(TestClass.class, Service.class)
        .addKeepMainRule(TestClass.class)
        .addKeepClassAndMembersRules(FeatureClass.class)
        .addDataEntryResources(
            DataEntryResource.fromBytes(
                StringUtils.lines("MissingClass").getBytes(),
                AppServices.SERVICE_DIRECTORY_NAME + Service.class.getTypeName(),
                Origin.unknown()))
        .addFeatureSplit(FeatureClass.class)
        .allowDiagnosticWarningMessages()
        .setMinApi(parameters.getApiLevel())
        .compile()
        .inspectDiagnosticMessages(
            inspector -> {
              inspector.assertWarningsCount(1);
              inspector.assertAllWarningsMatch(
                  diagnosticMessage(
                      containsString(
                          "Unexpected reference to missing service implementation class in "
                              + AppServices.SERVICE_DIRECTORY_NAME
                              + Service.class.getTypeName()
                              + ": MissingClass.")));
            });
  }

  static class TestClass {

    public static void main(String[] args) {
      for (Service service : ServiceLoader.load(Service.class, TestClass.class.getClassLoader())) {
        service.greet();
      }
    }
  }

  public interface Service {

    void greet();
  }

  public static class FeatureClass {}
}
