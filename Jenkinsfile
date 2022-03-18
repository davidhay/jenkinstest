pipeline {
    agent {
        docker { 
            image 'davidxhay/jenkins-gradle-jdk17'
        }
    }
    stages {
        stage('Build and Test') {
            steps {
                sh 'whoami'
                sh 'id'
            }
        }
    }
}
