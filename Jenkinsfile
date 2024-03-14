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
                sh 'echo Build and Package backend services'
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
                sh 'echo Build and Package frontend application'
                dir('front/medhead-ui') {
                    sh 'npm install'
                }
            }
        }

        stage('================ Run tests ================') {
            steps {
                sh 'echo Test backend services'
                dir('back/speciality-service') {
                    sh 'mvn test'
                }
                dir('back/hospital-service') {
                    sh 'mvn test'
                }
                dir('back/destination-service') {
                    sh 'mvn test'
                }
                dir('back/notification-service') {
                    sh 'mvn test'
                }
            }
        }

        stage('================ Sonarqube Analysis ================') {
            steps {
                script {
                    sh 'Analyse code with Sonarqube'
                    // TODO : Analyse code with Sonarqube (hors scope PoC)
                }
            }
        }

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

                    dir('front/medhead-ui') {
                        docker_image = docker.build("${DOCKER_USER}" + "/medhead-ui")
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
                    // TODO : Trigger CD Pipeline (hors scope PoC)
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