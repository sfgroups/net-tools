//https://jenkins.io/blog/2017/10/02/pipeline-templates-with-shared-libraries/
//https://github.com/jenkinsci/workflow-aggregator-plugin/blob/master/demo/repo/Jenkinsfile

node {
    stage('Test') {
        def reg = input(
                message: 'What is the reg value?',
                parameters: [
                        [$class: 'ChoiceParameterDefinition',
                         choices: 'Choice 1\nChoice 2\nChoice 3',
                         name: 'input',
                         description: 'A select box option']
                ])

        echo "Reg is ${reg}"
    }
}

// This shows a simple build wrapper example, using the Timestamper plugin.
node {
    properties([
            parameters([
                    string(name: 'submodule', defaultValue: ''),
                    string(name: 'submodule_branch', defaultValue: ''),
                    string(name: 'commit_sha', defaultValue: ''),
            ])
    ])

/* Accessible then with : params.submodule, params.submodule_branch...  */
    //manager.addShortText("deployed")
    currDate = new Date().dateTimeString
    manager.addShortText("undeployed: $currDate", "grey", "white", "0px", "white")
    manager.createSummary("gear2.gif").appendText("<h2>Successfully deployed</h2>", false)
}