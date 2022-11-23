pipeline{
    
    agent any 
    
    stages {
        
        stage('Git Checkout'){
            
            steps{
                
                script{
                    
                    git branch: 'main', url: 'https://gitlab.com/akashmoni2304/dev-ops-challenge.git'
                }
            }
        }
    }
}        
