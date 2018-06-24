/*
@Library('tools') import demo.Servers


servers = new Servers(this)
*/
jettyUrl = 'http://localhost:8081/'

stage('Dev') {
    node {
echo "Build"
    }
}

stage('QA') {

    echo "Test results: "
}

milestone 1
stage('Staging') {
    milestone 2
    input message: "Does ${jettyUrl}staging/ look good?"

}

milestone 3
stage ('Production') {
    echo "Deployed to ${jettyUrl}production/"
    /*lock(resource: 'production-server', inversePrecedence: true) {
        node {
            sh "wget -O - -S ${jettyUrl}staging/"
            echo 'Production server looks to be alive'
            servers.deploy 'production'
            echo "Deployed to ${jettyUrl}production/"
        }
    }*/
}
