String basePath = 'javaapps'
String repo = 'sheehan/gradle-example'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
}

pipelineJob("$basePath/gradle-example-build") { 
	
  def repo = ''
  def sshRepo = '' 

  description("Jenkins Nodejs App build Pipeline") 
  keepDependencies(false) 

  properties{ 
    githubProjectUrl (repo) 
    rebuild { 
      autoRebuild(false) 
    } 
  } 
  definition { 
    cpsScm { 
      scm { 
        git { 
          remote { url(sshRepo)
					credentials('key')
		  } 
          branches('master') 
          scriptPath('Jenkinsfile') 
          extensions { }  // required as otherwise it may try to tag the repo, which you may not want 
        } 
      } 
    } 
  }
  }
