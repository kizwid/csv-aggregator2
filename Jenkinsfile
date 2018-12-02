#!/usr/bin/env groovy

pipeline {

    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    tools {
        jdk 'jdk1.8.0_111'
        maven 'maven-3.3.9'
    }

    parameters {
        booleanParam(
                name: "RELEASE",
                description: "Build a release from current commit.",
                defaultValue: false)
    }

    stages {

        stage('Build and unit test') {
            steps {
                script {
                    def revision = getRevision()
                    bat "mvn " +
                            "clean install -Dresume=false " +
                            "-Darguments=\"-Dmaven.javadoc.failOnError=false\" -Drevision=${revision}"

                    //junit '**//*target/surefire-reports/TEST-*.xml'
                    //archive 'target*//*.jar'
                    //jacoco exclusionPattern: '**/*Test*.class', inclusionPattern: '**/*.class', maximumBranchCoverage: '80', maximumClassCoverage: '95', maximumComplexityCoverage: '80', maximumInstructionCoverage: '5000', maximumLineCoverage: '90', maximumMethodCoverage: '95', minimumBranchCoverage: '100', minimumClassCoverage: '100', minimumComplexityCoverage: '100', minimumInstructionCoverage: '6000', minimumLineCoverage: '100', minimumMethodCoverage: '100'
                }

            }
        }

        stage('Publish to nexus') {
            steps {
                script {
                    def revision = getRevision()
                    withCredentials([file(credentialsId: 'm2_settings', variable: 'MAVEN_SETTINGS')]) {

                            //no need to tag snapshot versions (currently svn does not like tagging - 'scm:tag')
                            def tagDirective = revision.endsWith("SNAPSHOT")?"":""

                            bat "mvn -s ${MAVEN_SETTINGS} " +
                            "deploy ${tagDirective} -Darguments=\"-Dmaven.javadoc.failOnError=false\" -Drevision=${revision} -Dmaven.tests.skip=true -DskipTests"
                        }

                }
            }
        }
    }
}

def getRevision() {
    def revisionNumber = env.BUILD_NUMBER;
    //all other branches are SNAPSHOTS
    if( !params.RELEASE){
        revisionNumber += "-SNAPSHOT"
    }
    return revisionNumber
}
