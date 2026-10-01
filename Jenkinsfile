pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out Java Shopping Application...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building Java Shopping Application...'
                sh './scripts/build.sh'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing Java Shopping Application...'
                sh './scripts/test.sh'
            }
        }
    }
}
