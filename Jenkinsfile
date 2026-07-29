pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-south-1'
        ECR_REGISTRY = '982920153818.dkr.ecr.ap-south-1.amazonaws.com'
        IMAGE_NAME = 'notificationservice'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        APP_SERVER = '15.252.45.161'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Login to Amazon ECR') {
            steps {
                withCredentials([[
                    \$class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-ecr-creds'
                ]]) {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} \
                        | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                    """
                }
            }
        }

        stage('Tag Image') {
            steps {
                sh """
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} \
                    ${ECR_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }

        stage('Push Image') {
            steps {
                sh """
                    docker push ${ECR_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }

        stage('Deploy to App Server') {
            steps {
                sshagent(credentials: ['app-server-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ec2-user@${APP_SERVER} '
                            aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}

                            cd /opt/microservices

                            docker compose pull notification-service

                            docker compose up -d notification-service
                        '
                    """
                }
            }
        }

    }

    post {
        success {
            echo 'Notification Service deployed successfully.'
        }

        failure {
            echo 'Deployment failed.'
        }

        always {
            sh 'docker image prune -f'
        }
    }
}