def buildLabel = "mylabel.${env.JOB_NAME}.${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')

podTemplate(
   label: buildLabel,
   containers: [
      containerTemplate(name: 'gradle', image: 'gradle:4.10.2-jdk11', command: 'cat', ttyEnabled: true),
      containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
      containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
      containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:latest', command: 'cat', ttyEnabled: true),
      containerTemplate(
              name: 'node',
              image: 'node:11-stretch',
              ttyEnabled: true,
              command: '/bin/bash',
              workingDir: '/home/jenkins',
              envVars: [
                      envVar(key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/'),
              ],
      ),
      containerTemplate(
              name: 'ibmcloud',
              image: 'garagecatalyst/ibmcloud-dev:1.0.1-root',
              ttyEnabled: true,
              command: '/bin/bash',
              workingDir: '/home/jenkins',
              envVars: [
                      envVar(key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/'),
                      secretEnvVar(key: 'APIKEY', secretName: 'ibmcloud-apikey', secretKey: 'password'),
                      secretEnvVar(key: 'RESOURCE_GROUP', secretName: 'ibmcloud-apikey', secretKey: 'resource_group'),
                      secretEnvVar(key: 'REGISTRY_URL', secretName: 'ibmcloud-apikey', secretKey: 'registry_url'),
                      secretEnvVar(key: 'REGISTRY_NAMESPACE', secretName: 'ibmcloud-apikey', secretKey: 'registry_namespace'),
                      secretEnvVar(key: 'REGION', secretName: 'ibmcloud-apikey', secretKey: 'region'),
                      secretEnvVar(key: 'CLUSTER_NAME', secretName: 'ibmcloud-apikey', secretKey: 'cluster_name'),
                      envVar(key: 'CHART_NAME', value: 'template-node-typescript'),
                      envVar(key: 'CHART_ROOT', value: 'chart'),
                      envVar(key: 'HOME', value: '/root'), // needed for the ibmcloud cli to find plugins
              ],
      )
   ],
   volumes: [
      hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
      hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')
   ],
   serviceAccount: 'jenkins'
) {
    node(buildLabel) {
        container(name: 'node', shell: '/bin/bash') {
            checkout scm
            stage('Setup') {
                sh '''
                    gradle clean
                '''
            }
            stage('Unit test') {
                sh '''
                    gradle test
                '''
            }
            stage('Lint and code coverage tests') {
                sh '''
                    gradle check
                '''
            }
            stage('Build') {
                sh '''
                    gradle assemble
                    # gradle build
                '''
            }
        }
        container(name: 'ibmcloud', shell: '/bin/bash') {
            stage('Build image') {
                sh '''
                    . ./env-config

                    npm i -g @garage-catalyst/ibm-garage-cloud-cli
                
                    echo "Building image: ${IMAGE_NAME}:${IMAGE_VERSION}-${IMAGE_BUILD_NUMBER}"
                    igc build --image ${IMAGE_NAME} --ver ${IMAGE_VERSION} --buildNumber ${IMAGE_BUILD_NUMBER}
                '''
            }
            stage('Deploy to DEV env') {
                sh '''
                    . ./env-config
                    
                    ENVIRONMENT_NAME=dev

                    npm i -g @garage-catalyst/ibm-garage-cloud-cli
                
                    echo "Deploying image: ${IMAGE_NAME}:${IMAGE_VERSION}-${IMAGE_BUILD_NUMBER}"
                    igc deploy --debug --image $IMAGE_NAME --ver $IMAGE_VERSION --buildNumber $IMAGE_BUILD_NUMBER --namespace $ENVIRONMENT_NAME
                '''
            }
        }
    }
}
