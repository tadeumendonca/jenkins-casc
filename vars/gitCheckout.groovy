def call(Map stageParams) { 
    checkout([
        $class: 'GitSCM',
        branches: scm.branches,
        doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
        extensions: scm.extensions,
        userRemoteConfigs: scm.userRemoteConfigs
    ])
  }