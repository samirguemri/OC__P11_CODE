pipeline {
    agent {
        label "localhost-agent" // any
    }
    environment {
        APP_NAME = "medhead-app"
        RELEASE = "1.0.0"
        DOCKER_USER = "samirguemri"
        DOCKER_PASS = "dockerhub-token"
        IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
    }
    stages {

        stage('================ Cleanup workspace & Prune Docker data ================') {
            steps {
                script {
                    sh 'docker system prune -a --volumes -f'
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
                        docker_image = docker.build("${IMAGE_NAME}")
                        docker.withRegistry('',DOCKER_PASS) {
                            docker_image.push("${IMAGE_TAG}")
                            docker_image.push('latest')
                        }
                    }
                }
                // dockerfile {
                //     filename 'Dockerfile'
                //     dir 'back/speciality-service'
                //     label 'medhead'
                //     label 'speciality-service'
                //     additionalBuildArgs  "--build-arg version=${RELEASE}"
                //     // args "-v /tmp:/tmp"
                //     registryUrl 'https://hub.docker.com/samirguemri/'
                //     registryCredentialsId 'dockerhub-token'
                // }
            }
        }

        stage('================ Deploy artifacts ================') {
            steps {
                //sh 'curl http://localhost:3000/param?query=demo | jq'
                sh 'echo Deploy artifacts'
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
        // always {
        //     sh 'docker compose down --remove-orphans -v'
        //     sh 'docker compose ps'
        // }
        success {
            echo 'Build was successful!'
        }
        failure {
            echo 'Build failed.'
        }
    }
}