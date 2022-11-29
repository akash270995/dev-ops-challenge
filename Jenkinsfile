pipeline{
    
    agent any 

    environment {
        imageName = "myphpapp"
        registryCredentials = "nexus"
        registry = "3.110.147.1:8081/"
        dockerImage = ''
    
    stages {
        
        stage('Git Checkout'){
            
            steps{
                
                script{
                    
                    git branch: 'master', url: 'https://gitlab.com/akashmoni2304/dev-ops-challenge.git'
                }
            }
        }

        stage('Static code analysis'){
            
            steps{
                
                script{
                    
                    withSonarQubeEnv(credentialsId: 'sonar-api') {
                        
                        sh 'mvn clean package sonar:sonar'
                    }
                   }
                    
                }
            }

            stage('Quality Gate Status'){
                
                steps{
                    
                    script{
                        
                        waitForQualityGate abortPipeline: false, credentialsId: 'sonar-api'
                    }
                }
            }

        stage('UNIT testing'){
            
            steps{
                
                script{
                    
                    sh 'mvn test'
                    junit 'target/surefire-reports/TEST-com.gameservice.outcome.OutcomeApplicationTests.xml'
                }
            }
        }

        stage('Building image') {

             steps{
                
                script {
                         
                       dockerImage = docker.build imageName
                }
            }           
        }
        
    }
}        
