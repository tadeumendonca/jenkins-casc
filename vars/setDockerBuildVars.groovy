def call(Map stageParams) { 
    env.ARTIFACT_NAME="${stageParams.appName}-${BUILD_ID}.tar.gz"
    env.IMAGE_NAME="${stageParams.appName}:${BUILD_ID}"
    env.BUILD_CONTAINER_ID="docker_build_${BUILD_TIMESTAMP}"

    echo "ARTIFACT_NAME=${ARTIFACT_NAME}"
    echo "IMAGE_NAME=${IMAGE_NAME}"
    echo "BUILD_CONTAINER_ID=${BUILD_CONTAINER_ID}"

    currentBuild.result = 'SUCCESS'
  }