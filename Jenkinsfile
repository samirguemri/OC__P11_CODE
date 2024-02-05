pipeline {
    agent any
    stages {
        stage("verify tooling") {
            steps {
                sh '''
                docker version
                docker info
                docker compose version
                curl --version
                jq --version
                '''
            }
        }
        stage('Prune Docker data') {
            steps {
                sh 'docker system prune -a --volumes -f'
            }
        }
        stage('Build and Package speciality-service') {
            steps {
                dir('speciality-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('Build Docker images') {
            steps {
                sh 'docker-compose build'
            }
        }
        stage('Start containers') {
            steps {
                sh 'docker compose up -d --no-color --wait'
                sh 'docker compose ps'
            }
        }
        stage('Run tests') {
            steps {
                sh 'echo Run tests'
                //sh 'mvn clean test'
            }
        }
        stage('Deploy artifacts') {
            steps {
                //sh 'curl http://localhost:3000/param?query=demo | jq'
                sh 'echo Deploy artifacts'
            }
        }
        stage('Integration Tests') {
            steps {
                script {
                    // Implement your integration test commands here
                    // This could involve calling your services' endpoints to check their interaction with MongoDB and Kafka
                    echo 'Running integration tests...'
                }
            }
        }
    }
    post {
        always {
            sh 'docker compose down --remove-orphans -v'
            sh 'docker compose ps'
        }
        success {
            echo 'Build was successful!'
        }
        failure {
            echo 'Build failed.'
        }
    }
}