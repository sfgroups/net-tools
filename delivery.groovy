pipeline {
    agent any
    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }
    stages {
        stage('Commit stage') {
            steps {
                task('Compile and package') {
                    echo 'Building...'
                    sleep 1
                    echo 'Successfully built project!'
                }

                task('Upload artifacts') {
                    sleep 1
                    echo 'Successfully uploaded artifacts!'
                }
            }
        }
        stage('Test stage') {
            steps {
                task('Run component tests') {
                    echo 'Running tests...'
                    sleep 1
                    echo 'Component tests finished!'
                }

                task('Run integration tests') {
                    sleep 1
                    echo 'Integration tests finished'
                }
            }
        }
        stage('Deploy') {
            steps {
                task('Deploy to UAT') {
                    echo 'Deploying to UAT...'
                    sleep 1
                    echo 'Successfully deployed to UAT'
                }

                task('Deploy to production') {
                    echo 'Deploying to production...'
                    sleep 1
                    echo 'Deployed to production!'
                }
            }
        }
    }
}
