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
        stage('Start Application') {
    steps {
        echo 'Starting Java Shopping Application...'
        sh 'nohup java -jar target/java-shopping-devops-1.0.0.jar > app.log 2>&1 &'
        sleep 3
    }
}

        stage('Test') {
            steps {
                echo 'Testing Java Shopping Application...'
                sh './scripts/test.sh'
            }
        }
    
        stage('Docker Build') {
    steps {
        echo 'Building Docker image...'
        sh 'docker build -t java-shopping-app:1.0 .'
    }
}
stage('Docker Run') {
    steps {
        echo 'Starting Docker container...'
        sh 'docker run -d --name java-shopping-container -p 8082:8080 java-shopping-app:1.0'
    }
}
stage('Docker Test') {
    steps {
        echo 'Testing Docker container...'
        sh 'curl -f http://localhost:8082/health'
    }
}
stage('Docker Cleanup') {
    steps {
        echo 'Cleaning up Docker container...'
        sh 'docker stop java-shopping-container'
        sh 'docker rm java-shopping-container'
    }
}
stage('AWS Test') {
    steps {
        withCredentials([usernamePassword(
            credentialsId: 'credentials',
            usernameVariable: 'AWS_ACCESS_KEY_ID',
            passwordVariable: 'AWS_SECRET_ACCESS_KEY'
        )]) {
            sh 'aws sts get-caller-identity'
        }
    }
}
stage('Push to ECR') {
    steps {
        echo 'Logging in to AWS ECR and pushing Docker image...'

        withCredentials([usernamePassword(
            credentialsId: 'credentials',
            usernameVariable: 'AWS_ACCESS_KEY_ID',
            passwordVariable: 'AWS_SECRET_ACCESS_KEY'
        )]) {

            sh '''
                aws ecr get-login-password --region ap-south-1 |
                docker login --username AWS --password-stdin \
                536697229262.dkr.ecr.ap-south-1.amazonaws.com

                docker tag java-shopping-app:1.0 \
                536697229262.dkr.ecr.ap-south-1.amazonaws.com/java-shopping-name:1.0

                docker push \
                536697229262.dkr.ecr.ap-south-1.amazonaws.com/java-shopping-name:1.0
            '''
        }
    }
}
}
}
