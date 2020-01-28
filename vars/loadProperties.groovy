def call(Map stageParams) { 
    // Load Properties File
    def props = readProperties file: 'jenkins.properties'
    
    // Add each property to Environment Variables 
    for (entry in props) {
        env["${entry.key}"] = "${entry.value}"
    }

  }