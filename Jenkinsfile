pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/Manyaharish14/patient-health-record.git',
                    credentialsId: 'github-credentials'
                echo '✅ Code checked out successfully'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
                echo '✅ Build completed successfully'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
                echo '✅ Tests executed successfully'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
                echo '✅ Package created successfully'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
    }

    post {
        success {
            emailext(
                to: 'your-email@gmail.com',
                subject: "Build Success - ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Build completed successfully! Check console output at ${env.BUILD_URL}"
            )
            echo '🎉 Pipeline completed successfully!'
        }
        failure {
            emailext(
                to: 'your-email@gmail.com',
                subject: "❌ Pipeline Failed - ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Pipeline failed. Check console: ${env.BUILD_URL}"
            )
            echo '❌ Pipeline failed!'
        }
        always {
            cleanWs()
            echo 'Workspace cleaned up'
        }
    }
}
