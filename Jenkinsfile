pipeline {
    agent {
        docker { 
            image 'gradle:7.4.1-jdk17'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    stages {
        stage('Build and Test') {
            steps {
                sh 'whoami'
                sh 'id'
                sh 'ls -l /var/run/docker.sock'
            }
        }
    }
}
