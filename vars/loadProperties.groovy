def call(Map stageParams) { 
    def props = readProperties file: 'jenkins.properties'

    props.each { 
      env[$it.key] = $it.value 
    }
  }