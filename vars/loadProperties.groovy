def call(Map stageParams) { 
    def props = readProperties file: 'jenkins.properties'

    for (entry in props) {
        env["${entry.key}"] = "${entry.value}"
    }

  }