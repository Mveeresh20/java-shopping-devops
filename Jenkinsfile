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
        sh 'docker build -t java-shopping-app:${BUILD_NUMBER} .'
    }
}
stage('Docker Run') {
    steps {
        echo 'Starting Docker container...'
        sh '''
            docker rm -f java-shopping-container 2>/dev/null || true

            docker run -d \
                --name java-shopping-container \
                -p 8082:8080 \
                java-shopping-app:${BUILD_NUMBER}
        '''
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

                docker tag java-shopping-app:${BUILD_NUMBER} \
                536697229262.dkr.ecr.ap-south-1.amazonaws.com/java-shopping-name:${BUILD_NUMBER}

                docker push \
                536697229262.dkr.ecr.ap-south-1.amazonaws.com/java-shopping-name:${BUILD_NUMBER}
            '''
        }
    }
}
stage('Test EC2 SSH') {
    steps {
        sshagent(['ec2-ssh-key']) {
            sh '''
                ssh -o StrictHostKeyChecking=no \
                ubuntu@15.206.187.247 \
                "hostname && whoami"
            '''
        }
    }
}
stage('Deploy with Ansible') {
    steps {
        sshagent(['ec2-ssh-key']) {
            sh '''
                ansible-playbook \
                -i ansible/inventory \
                ansible/playbook.yml -e image_tag=${BUILD_NUMBER} \
                -e previous_image_tag=$((BUILD_NUMBER - 1))
            '''
        }
    }
}
stage('Test Jenkins EKS Access') {
    steps {
        withCredentials([usernamePassword(
            credentialsId: 'credentials',
            usernameVariable: 'AWS_ACCESS_KEY_ID',
            passwordVariable: 'AWS_SECRET_ACCESS_KEY'
        )]) {
            sh '''
                aws sts get-caller-identity

                export KUBECONFIG="$WORKSPACE/kubeconfig"

                aws eks update-kubeconfig \
                  --region ap-south-1 \
                  --name java-shopping-eks

                kubectl get nodes
            '''
        }
    }
}
        }

    }

