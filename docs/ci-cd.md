Continuous Integration & Continous Devliery with Jenkins and Heroku cloud

# Install Jenkins locally
Go to the official site https://jenkins.io/2.0/

Download the jenkins.war

started it with the following command.
java -jar jenkins.war --httpPort=9000

Browse to http://localhost:9000/ to install Jenkins

Create a pipeline for each service and gateway

# Jenkins cloud
Create a Jenkins instance in the cloud, 
i.e. aws / azure / google compute engine
An option would be to use Bitnami Jenkins to deploy the jenkins to the instance.

Create a pipeline for each service and gateway.
i.e. one pipeline for customer service,
one pipeline for supplier service.
in each pipeline, only build and test the corresponding service.
to do so, in Jenkins file, use dir () block get into the folder.
i.e. for customer service, to do 'clean'

    stage('clean') {
        dir('customer-service') {
            sh "pwd"
            sh "ls -a"
            sh "chmod +x mvnw"
            sh "./mvnw clean"
        }

    }


#Setup Jenkins on Google compute engine
From Google Cloud Platform, Use Google Deployment Management to install Jenkins image (Created by Google, not Bitnami)
Jenkins should be started automatically.
SSH into the server and install Git (apt-get install git) and JDK (https://www.mkyong.com/java/how-to-install-oracle-jdk-8-on-debian/)

Setting the JAVA_HOME Environment Variable
sudo nano /etc/environment
In this file, add the following line, making sure to replace the highlighted path with your own copied path.

/etc/environment
JAVA_HOME="/usr/lib/jvm/java-8-oracle"
Save and exit the file, and reload it.

source /etc/environment
You can now test whether the environment variable has been set by executing the following command:

echo $JAVA_HOME
This will return the path you just set.

## Setup Heroku for CD
install Heroku CLI following the instructions on the office web.
and login

    heroku login

After login, get api key

    heroku auth:token
    
copy the api key, and set the environment variable HEROKU_API_KEY

    sudo vim /etc/environment
    
Add a line to the environment file

    HEROKU_API_KEY="{apikey}"
replace the {apikey} with the real api key.

    source /etc/environment
   
then reboot the machine or reboot jenkins.

audo deployment should work for heroku after these settings.


# Deploy to Heoku from Jenkins pipeline

        stage('package and deploy') {
            dir('customer-service') {
                sh "./mvnw com.heroku.sdk:heroku-maven-plugin:1.1.1:deploy -DskipTests -Pprod -Dheroku.appName=bss-customer-svc"
            }
        }

"appName" is the app name on Heroku. if no app on heroku with name, create an empty app with the name on Heroku first.



# Set up Jenkins with docker and heroku tool installation
1. Start Jenkins using docker image
2. Install Heroku CLI in the jenkins docker container
    By default, root is disabled for jenkins container, we could either create a custom docker image on top of the official jenkins image,
    or install Heroku CLI in the jenkins container.
    To install Heroku CLI, tty into the container:
    docker exec -u 0 -it jenkins bash    (replace jenkins with the container's name)
    
    & and then install:
     wget https://cli-assets.heroku.com/branches/stable/heroku-linux-386.tar.gz -O heroku.tar.gz
     mkdir -p /usr/local/lib
     tar -xvzf heroku.tar.gz -C /usr/local/lib
     /usr/local/lib/heroku/install

    Then: 
        heroku
        Enter your Heroku credentials.
        Email: e*m*@example.com
        Password (typing will be hidden):
        Authentication successful.
        
        
        
# Code analysis with Sonar locally
1. start Sonar server locally using docker compose (going to service/gateway directory)
    
        cd customer-service
        docker-compose -f src/main/docker/sonar.yml up
    
2. Analyze the code
        
        ./mvnw sonar:sonar
        
3. The Sonar reports will be available at: http://localhost:9000

# auto deployment setup using docker
in Google Compute Engine, after setting up jenkins.
1. Install docker (for Debian 9, x86_64 Arch)
    https://docs.docker.com/engine/installation/linux/docker-ce/debian/#install-docker-ce-1

at the last step, run

    sudo docker run hello-world
   
to verify docker is installed correctly.


2. Solving Docker permission denied while trying to connect to the Docker daemon socket
run:
        
          sudo usermod -a -G docker $USER
            
then log out and log back in.
ref: https://techoverflow.net/2017/03/01/solving-docker-permission-denied-while-trying-to-connect-to-the-docker-daemon-socket/

2.1 (Jenkins) Add username "jenkins" to Docker group, so that jenkins script can run docker commands.(similar to step 2).
To check the username of jenkins server (in Jenkins build file, add "sh whoami")


        sudo usermod -a -G docker jenkins
   
3. docker login to be able to push docker images to the public docker registry (docker hub)


    docker login
    
then enter docker hub username and password

3.1 (Jenkins) CONFIGURING DOCKER HUB WITH JENKINS
To store the Docker image resulting from our build, we'll be using Docker Hub. You can sign up for a free account at 
https://hub.docker.com

We'll need to give Jenkins access to push the image to Docker Hub. For this, we'll create Credentials in Jenkins, and refer to them in the Jenkinsfile.

As you might have noticed in the above Jenkinsfile, we're using docker.withRegistry to wrap the app.push commands - this instructs Jenkins to log in to a specified registry with the specified credential id (docker-hub-credentials).

On the Jenkins front page, click on Credentials -> System -> Global credentials -> Add Credentials

Add your Docker Hub credentials as the type Username with password, with the ID docker-hub-credentials

ref: https://getintodevops.com/blog/building-your-first-docker-image-with-jenkins-2-guide-for-developers

4. docker login via jenkins job
if step 3 doesn't work, create a Jenkins freestyle job to perform docker login.
if "Build Environment" section, select "Use secrete text or files"
Bindings:
    select "Username and password (separated)", username variable: "DH_USERNAME", password variable: "DH_PASSWORD", credentials: select the credential set up in step 3.

in "Build" section, select "Execute Shell", and enter the shell script:

    #!/bin/bash
    docker login -u $DH_USERNAME -p $DH_PASSWORD

after this step, should be able to push docker image to docker hub using command such as: 

    sh "docker push pkcool/fxpriceservice"
    


# Deploy to Kubernetes from Jenkins (Google Compute Engine)
the steps to be performed in the google compute engine where jenkins is installed
1. install Google cloud SDK
follow: https://cloud.google.com/sdk/downloads#apt-get

2. Install plugins for Jenkins
 Kubernetes plugin and Google Authenticated Source plugin.
 
ref: https://cloud.google.com/solutions/configuring-jenkins-kubernetes-engine

3. Jenkins config

4. Enable access scope for the compute engine (service account: 979083845575-compute@developer.gserviceaccount.com)
otherwise, jenkins job will 

4.1 authenticate gcloud using the google account, instead of service account.
run:

    gcloud auth list
to see the authenticated accounts.


To expose Jhipster registry or jhipster console to external (assign an external IP to the service):
kubectl expose service jhipster-registry --type=LoadBalancer --name=exposed-registry
kubectl expose service jhipster-console --type=LoadBalancer --name=exposed-console



