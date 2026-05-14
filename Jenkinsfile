pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        DOCKER_IMAGE = 'patient-health-record'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DOCKER_REGISTRY = 'your-dockerhub-username'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/YOUR_USERNAME/patient-health-record.git',
                    credentialsId: 'github-credentials'
                echo '✅ Code checked out successfully'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
                echo '✅ Build completed'
            }
            post {
                failure {
                    emailext(
                        to: 'team@hospital.com',
                        subject: "Build Failed - ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                        body: "Build failed. Please check console output."
                    )
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
                echo '✅ Tests executed'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    publishHTML([
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage Report'
                    ])
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                echo '✅ Package created'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}")
                    docker.build("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker stop patient-health-record || true
                    docker rm patient-health-record || true
                    docker run -d --name patient-health-record -p 8080:8080 \
                        ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                '''
            }
        }
    }

    post {
        success {
            emailext(
                to: 'admin@hospital.com',
                subject: "Build Success - ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: """
                    ✅ Build completed successfully!
                    
                    Application: Patient Health Record System
                    Version: ${DOCKER_TAG}
                    Docker Image: ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                    Access: http://localhost:8080
                    
                    Test Results: ${env.BUILD_URL}testReport/
                    Coverage Report: ${env.BUILD_URL}jacoco/
                """
            )
        }
        failure {
            emailext(
                to: 'admin@hospital.com',
                subject: "❌ Pipeline Failed - ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Pipeline failed. Check console: ${env.BUILD_URL}"
            )
        }
        always {
            cleanWs()
        }
    }
}
