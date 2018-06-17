#!/usr/bin/env groovy
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import hudson.model.*
import hudson.EnvVars
import groovy.json.JsonSlurperClassic
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import java.net.URL

// env.JAVA_HOME="${tool 'Java SE DK 8u131'}"
//   env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

/* Only keep the 10 most recent builds. */
def projectProperties = [
        [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']],
]

logRotator {
    numToKeep 30
}

def imageName = 'sfgroups/nettools'
def BUILD_ENV = 'dev'
def fromemail = 'noreply@sfgroups.com'
def toemail = 'builld@sfgroups.com'

if (!env.CHANGE_ID) {
    if (env.BRANCH_NAME == null) {
        projectProperties.add(pipelineTriggers([cron('H/30 * * * *')]))
    }
}

properties(projectProperties)

try {
    /* Assuming that wherever we're going to build, we have nodes labelled with
    * "Docker" so we can have our own isolated build environment
    */
    node('master') {

        currentBuild.result = "SUCCESS"
        timestamps {
            ansiColor('xterm') {
                def workspace = pwd()
                echo "Building Job at ${workspace}"

                /* def workspace = manager.build.getEnvVars()["WORKSPACE"]
                 String fileContents = new File('${workspace}/filename.txt').text*/
                manager.createSummary("folder.gif").appendText("TEst messsage")

                stage('Clean workspace') {
                    /* Running on a fresh Docker instance makes this redundant, but just in
                    * case the host isn't configured to give us a new Docker image for every
                    * build, make sure we clean things before we do anything
                    */
                    deleteDir()
                    sh 'ls -lah'
                }

                stage('Per-build') {
                    print "Environment will be : ${env.NODE_ENV}"
                    sh '[ -f /usr/bin/npm ] &&  npm version'
                    // echo sh(script: 'env|sort', returnStdout: true)
                }

                stage('Checkout source') {
                    /*
                    * For a standalone workflow script, we would use the `git` step
                    *
                    *
                    * git url: 'git://github.com/jenkinsci/jenkins.io',
                    *     branch: 'master'
                    */

                    /*
                    * Represents the SCM configuration in a "Workflow from SCM" project build. Use checkout
                    * scm to check out sources matching Jenkinsfile with the SCM details from
                    * the build that is executing this Jenkinsfile.
                    *
                    * when not in multibranch: https://issues.jenkins-ci.org/browse/JENKINS-31386
                    */
                    checkout scm
                }

                stage('Build site') {
                    /* If the agent can't gather resources and build the site in 60 minutes,
                    * something is very wrong
                    */
                    timeout(60) {
                        sh '''#!/usr/bin/env bash
                    set -o errexit
                    set -o nounset
                    set -o pipefail
                    set -o xtrace

                   env
                    '''
                    }
                }

                def container
                stage('Build docker image') {
                    dir('.') {
                        /* Only update docker tag when docker files change*/
                        def imageTag = sh(script: 'tar cf - . | md5sum', returnStdout: true).take(6)
                        echo "Creating the container ${imageName}:${imageTag}"
                        container = docker.build("${imageName}:${imageTag}")
                    }
                }

                stage('Push Image') {
                    /* The Jenkins which deploys doesn't use multibranch or GitHub Org Folders
                    */
                    if (env.BRANCH_NAME == null) {
                        stage('Publish docker image') {
                            docker.withRegistry('https://hub.docker.com', 'dockerhub') {
                                container.push()
                                container.push("latest")


                            }
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
        }
    }
}
catch (exc) {
    echo "Caught: ${exc}"
    currentBuild.result = "FAILURE"   
    mail subject: "${env.JOB_NAME} (${env.BUILD_NUMBER}) failed",
            body: "It appears that ${env.BUILD_URL} is failing, somebody should do something about that",
            to: toemail,
            replyTo: toemail,
            from: fromemail

    /* Rethrow to fail the Pipeline properly */
    throw exc
}

// vim: ft=groovy
