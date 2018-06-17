#!groovy

node("master") {
    currentBuild.result = "SUCCESS"
    BUILD_ENV = 'dev'
    fromemail = 'noreply@sfgroups.com'
    toemail = 'builld@sfgroups.com'

    try {
        stage('Per-build') {
            print "Environment will be : ${env.NODE_ENV}"
            sh '[ -f /usr/bin/npm ] &&  npm version'
            // echo sh(script: 'env|sort', returnStdout: true)
        }

        stage('Check Out') {
            echo 'Check Out..'
            checkout scm
        }

        stage( 'Gradle Static Analysis') {
           echo "TODO"
        }

        ansiColor('xterm') {
            timestamps { // build gradle with Jenkin tools
                ansiColor('xterm') {
                    stage('Build Tool Gradle') {
                        withEnv(["PATH+MAVEN=${tool 'gradle'}/bin", "DOCKER_HOST=unix:///var/run/docker.sock", "JAVA_HOME=/etc/alternatives/java_sdk"]) {
                            echo 'Building..'
                            echo "DOCKER_HOST = $DOCKER_HOST , Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                            sh 'gradle --version'
                            sh 'gradle  imagePublish'
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            timestamps {
                ansiColor('xterm') {
                    echo 'Deploying....'
                    env.WORKSPACE = pwd()
                    def imagename = readFile "${env.WORKSPACE}/image-name.txt"

                    obj = readYaml file: 'app_info.yaml'
                    println obj.Servers.DEV
                    appname=obj.applicaton.applicationId
                    obj.Servers.DEV.each { host ->
                        println "Deploy on Host $host ....."
                        withCredentials([sshUserPrivateKey(credentialsId: 'webdeploy-dev', keyFileVariable: 'KEYFILE', passphraseVariable: '', usernameVariable: 'USERNAME')]) {
                            sh "ssh -o StrictHostKeyChecking=no -i $KEYFILE $USERNAME@${host} -x bin/deploy_docker_image.sh ${appname} ${imagename}"
                        }
                    }
					
					      //ansiblePlaybook colorized: true, credentialsId: '', forks: 10, inventory: 'win-hosts', limit: '', playbook: 'ping.yml', sudoUser: null, vaultCredentialsId: 'get_credentials_id_from_jenkins'
                }
            }
        }

		
        stage('Cleanup') {
            echo 'prune and cleanup'
            mail from: fromemail, replyTo: toemail, to: toemail,
                    subject: 'project build successful',
                    body: 'project build successful'
        }
    }
    catch (err) {
        currentBuild.result = "FAILURE"
        mail from: fromemail, replyTo: toemail, to: toemail,
                subject: 'project build failed',
                body: "project build error is here: ${env.BUILD_URL}"

        throw err
    }
}
