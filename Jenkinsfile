pipeline {
    agent {
        label "localhost-agent" // any
    }

    environment {
        APP_NAME = "medhead-app"
        RELEASE = "1.0.0"
        DOCKER_USER = "samirguemri"
        DOCKER_PASS = "dockerhub-token"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
    }

    stages {

        stage('================ Cleanup workspace & Prune Docker data ================') {
            steps {
                script {
                    sh 'docker system prune -a' // Remove all unused containers, networks, images.  "--volumes -f" for volumes.
                }
            }
        }

        stage('================ Build and Package services ================') {
            steps {
                dir('back/speciality-service') {
                    sh 'mvn clean package -DskipTests'
                }
                dir('back/hospital-service') {
                    sh 'mvn clean package -DskipTests'
                }
                dir('back/destination-service') {
                    sh 'mvn clean package -DskipTests'
                }
                dir('back/notification-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('================ Run tests ================') {
            steps {
                dir('back/speciality-service') {
                    sh 'mvn test'
                }
            }
        }

        stage('================ Integration Tests ================') {
            steps {
                script {
                    // Implement your integration test commands here
                    // This could involve calling your services' endpoints to check their interaction with MongoDB and Kafka
                    echo 'Running integration tests...'
                }
            }
        }

        // stage('================ Sonarqube Analysis ================') {
        //     steps {
        //         script() {
        //             withSonarQubeEnv(credentialsId: 'sonarqube-token') {
        //                 sh 'mvn sonar:sonar'
        //             }
        //         }
        //     }
        // }

        // stage('================ Build Docker images ================') {
        //     steps {
        //         sh 'docker-compose build'
        //     }
        // }

        // stage('================ Start Docker containers ================') {
        //     steps {
        //         sh 'docker compose up -d --wait'
        //         sh 'docker compose ps'
        //     }
        // }

        stage("================ Build & Push Docker images ================") {
            steps {
                script {
                    dir('back/speciality-service') {
                        docker_image = docker.build("${DOCKER_USER}" + "/speciality-service")
                        docker.withRegistry('',DOCKER_PASS) {
                            docker_image.push("${IMAGE_TAG}")
                            docker_image.push('latest')
                        }
                    }

                    dir('back/hospital-service') {
                        docker_image = docker.build("${DOCKER_USER}" + "/hospital-service")
                        docker.withRegistry('',DOCKER_PASS) {
                            docker_image.push("${IMAGE_TAG}")
                            docker_image.push('latest')
                        }
                    }

                    dir('back/destination-service') {
                        docker_image = docker.build("${DOCKER_USER}" + "/destination-service")
                        docker.withRegistry('',DOCKER_PASS) {
                            docker_image.push("${IMAGE_TAG}")
                            docker_image.push('latest')
                        }
                    }

                    dir('back/notification-service') {
                        docker_image = docker.build("${DOCKER_USER}" + "/notification-service")
                        docker.withRegistry('',DOCKER_PASS) {
                            docker_image.push("${IMAGE_TAG}")
                            docker_image.push('latest')
                        }
                    }
                }
            }
        }

        stage('================ Trigger CD Pipeline ================') {
            steps {
                script {
                    sh 'echo Trigger CD Pipeline'
                    //sh "curl -v -k --user admin:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'https://jenkins.dev.dman.cloud/job/gitops-complete-pipeline/buildWithParameters?token=gitops-token'"
                }
            }
        }
    }

    post {
        always {
            //sh 'docker compose down --remove-orphans -v'
            sh 'docker image prune -a -f'
            sh 'docker image list -a'
        }
        success {
            echo 'Build was successful!'
        }
        failure {
            echo 'Build failed.'
        }
    }
}