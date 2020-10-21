# Copyright (c) 2017, the R8 project authors. Please see the AUTHORS file
# for details. All rights reserved. Use of this source code is governed by a
# BSD-style license that can be found in the LICENSE file.

import glob
import os
import utils

BASE = os.path.join(utils.THIRD_PARTY, 'gmscore')

V4_BASE = os.path.join(BASE, 'v4')
V5_BASE = os.path.join(BASE, 'v5')
V6_BASE = os.path.join(BASE, 'v6')
V7_BASE = os.path.join(BASE, 'v7')
V8_BASE = os.path.join(BASE, 'v8')

V9_BASE = os.path.join(BASE, 'gmscore_v9')
V9_PREFIX = os.path.join(V9_BASE, 'GmsCore_prod_alldpi_release_all_locales')

V10_BASE = os.path.join(BASE, 'gmscore_v10')
V10_PREFIX = os.path.join(V10_BASE, 'GmsCore_prod_alldpi_release_all_locales')

LATEST_BASE = os.path.join(BASE, 'latest')
LATEST_PREFIX = os.path.join(LATEST_BASE, 'GmsCore_prod_alldpi_release_all_locales')
ANDROID_L_API = '21'

# NOTE: we always use android.jar for SDK v25, later we might want to revise it
#       to use proper android.jar version for each of gmscore version separately.
ANDROID_JAR = utils.get_android_jar(25)

VERSIONS = {
  'v4': {
    'dex' : {
      'inputs' : glob.glob(os.path.join(V4_BASE, '*.dex')),
      'pgmap' : os.path.join(V4_BASE, 'proguard.map'),
      'libraries' : [ANDROID_JAR],
      'min-api' : ANDROID_L_API,
    }
  },
  'v5': {
    'dex' : {
      'inputs' : glob.glob(os.path.join(V5_BASE, '*.dex')),
      'pgmap' : os.path.join(V5_BASE, 'proguard.map'),
      'libraries' : [ANDROID_JAR],
      'min-api' : ANDROID_L_API,
    }
  },
  'v6': {
    'dex' : {
      'inputs' : glob.glob(os.path.join(V6_BASE, '*.dex')),
      'pgmap' : os.path.join(V6_BASE, 'proguard.map'),
      'libraries' : [ANDROID_JAR],
      'min-api' : ANDROID_L_API,
    }
  },
  'v7': {
    'dex' : {
      'inputs' : glob.glob(os.path.join(V7_BASE, '*.dex')),
      'pgmap' : os.path.join(V7_BASE, 'proguard.map'),
      'libraries' : [ANDROID_JAR],
      'min-api' : ANDROID_L_API,
    }
  },
  'v8': {
    'dex' : {
      'inputs' : glob.glob(os.path.join(V8_BASE, '*.dex')),
      'pgmap' : os.path.join(V8_BASE, 'proguard.map'),
      'libraries' : [ANDROID_JAR],
      'min-api' : ANDROID_L_API,
    }
  },
  'v9': {
    'dex' : {
      'flags': '--no-desugaring',
      'inputs': [os.path.join(V9_BASE, 'armv7_GmsCore_prod_alldpi_release.apk')],
      'main-dex-list': os.path.join(V9_BASE, 'main_dex_list.txt'),
      'pgmap': '%s_proguard.map' % V9_PREFIX,
    },
    'deploy' : {
      'pgconf': ['%s_proguard.config' % V9_PREFIX],
      'inputs': ['%s_deploy.jar' % V9_PREFIX],
      'min-api' : ANDROID_L_API,
    },
    'proguarded' : {
      'flags': '--no-desugaring',
      'inputs': ['%s_proguard.jar' % V9_PREFIX],
      'main-dex-list': os.path.join(V9_BASE, 'main_dex_list.txt'),
      'pgmap': '%s_proguard.map' % V9_PREFIX,
     }
  },
  'v10': {
    'dex' : {
      'flags': '--no-desugaring',
      'inputs': [os.path.join(V10_BASE, 'armv7_GmsCore_prod_alldpi_release.apk')],
      'main-dex-list': os.path.join(V10_BASE, 'main_dex_list.txt') ,
      'pgmap': '%s_proguard.map' % V10_PREFIX,
    },
    'deploy' : {
      'inputs': ['%s_deploy.jar' % V10_PREFIX],
      'pgconf': ['%s_proguard.config' % V10_PREFIX],
      'min-api' : ANDROID_L_API,
    },
    'proguarded' : {
      'flags': '--no-desugaring',
      'inputs': ['%s_proguard.jar' % V10_PREFIX],
      'main-dex-list': os.path.join(V10_BASE, 'main_dex_list.txt') ,
      'pgmap': '%s_proguard.map' % V10_PREFIX,
    }
  },
  'latest': {
    'deploy' : {
      'inputs': ['%s_deploy.jar' % LATEST_PREFIX],
      'pgconf': [
          '%s_proguard.config' % LATEST_PREFIX,
          '%s/proguardsettings/GmsCore_proguard.config' % utils.THIRD_PARTY],
      'min-api' : ANDROID_L_API,
    },
    'proguarded' : {
      'flags': '--no-desugaring',
      'inputs': ['%s_proguard.jar' % LATEST_PREFIX],
      'main-dex-list': os.path.join(LATEST_BASE, 'main_dex_list.txt') ,
      'pgmap': '%s_proguard.map' % LATEST_PREFIX,
    }
  },
}
