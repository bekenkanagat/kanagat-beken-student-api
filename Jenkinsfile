// Jenkins pipeline for kanagat-beken-student-api
// Author: Kanagat Beken
pipeline {
    agent any

    tools {
        // configured in Manage Jenkins -> Tools -> JDK installations (name: jdk17)
        jdk 'jdk17'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    environment {
        STUDENT_NAME = 'Kanagat Beken'
        IMAGE_NAME   = 'kanagat-beken-student-api'
        IMAGE_TAG    = 'v1.0-kanagat-beken'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Pipeline of ${STUDENT_NAME}: checking out source from GitHub ==="
                checkout scm
                sh 'git log -1 --oneline'
            }
        }

        stage('Build') {
            steps {
                echo "=== ${STUDENT_NAME}: building Spring Boot app with Gradle ==="
                sh 'chmod +x gradlew'
                sh './gradlew --no-daemon clean bootJar'
            }
        }

        stage('Test') {
            steps {
                echo "=== ${STUDENT_NAME}: running automated tests ==="
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "=== ${STUDENT_NAME}: building image ${IMAGE_NAME}:${IMAGE_TAG} ==="
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:build-${BUILD_NUMBER} .'
            }
        }

        stage('Verify') {
            steps {
                echo "=== ${STUDENT_NAME}: verifying image ==="
                sh '''
                    if docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} > /dev/null 2>&1; then
                        echo "OK: image ${IMAGE_NAME}:${IMAGE_TAG} exists"
                        docker images ${IMAGE_NAME}
                        docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} --format 'Created: {{.Created}}  Size: {{.Size}} bytes'
                    else
                        echo "ERROR: image ${IMAGE_NAME}:${IMAGE_TAG} not found"
                        exit 1
                    fi
                '''
            }
        }
    }

    post {
        success {
            echo "SUCCESS: pipeline of ${STUDENT_NAME} finished, image ${IMAGE_NAME}:${IMAGE_TAG} is ready"
        }
        failure {
            echo "FAILURE: pipeline of ${STUDENT_NAME} failed - check the stage logs above"
        }
    }
}
