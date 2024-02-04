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
    stage('Start container') {
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
    stage('Build artifacts') {
      steps {
        //sh 'mvn clean package'
        sh 'echo Build artifacts'
      }
    }
    stage('Deploy artifacts') {
      steps {
        //sh 'curl http://localhost:3000/param?query=demo | jq'
        sh 'echo Deploy artifacts'
      }
    }
  }
  post {
    always {
      sh 'docker compose down --remove-orphans -v'
      sh 'docker compose ps'
    }
  }
}