pipeline {
    agent {
        docker { 
            image 'davidxhay/jenkins-gradle-jdk17'
            args '-u gradle'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
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
