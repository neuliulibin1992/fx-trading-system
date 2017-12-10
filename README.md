# Automated Forex Trading System


## Summary
a FX trading system based on microservices architecture.

It allows you to run:
- The registry
- An API Gateway
- Several microservices (fx rate service, etc.).
- The ELK stack (Elasticsearch, Logstash, Kibana) for log centralization
- _(future) Graphite/Grafana for metrics centralization_

#### **All working out of the box !**

It provides:
- Scripts to setup the apps
- `.yo-rc.json` files in fx-price-service directory that will be used to generate apps
- A `central-server-config/` directory that can be used to edit the registry's config server configuration but _only in dev profile_ (a git repository is used in prod profile)

It depends on [generator-jhipster-docker-compose](https://github.com/jhipster/generator-jhipster-docker-compose) to generate a global docker-compose file.

## How to test

### Setup and build
First, generate all apps from their _.yo-rc.json_. 
(Run the command from devops-scripts directory)

    ./setup-apps.sh
    
Then, generate samples entities from the _entities.jh_ JDL file in each app's directory

    ./setup-entities.sh
    
Then, generate entities for front end gateway. This step needs to be done manually.
    "cd gateway" & generate entities one by one (check the command in the below script).

    ./setup-gateway-entities.sh

Finally, build apps and generate docker images for them.  `mvn package docker:build -DskipTests=true`

    ./build-apps.sh
    
This script runs `mvn package docker:build -DskipTests=true` for all apps, the `app/src/main/docker/Dockerfile` is used by maven-docker plugin to build the docker image.

Generate a Docker-compose file:

    mkdir docker-compose
    cd docker-compose

    yo jhipster:docker-compose

And answer the questions.

To run all apps:
    
    docker-compose up
   
To shut down all apps:

    docker-compose down


### Run everything

Note: At any point in the process you can use `docker-compose logs appname` to view its logs.

#### Start the JHipster Eegistry (service discovery and configuration server)

- `docker-compose up -d jhipster-registry`: launch the registry
- Open `http://localhost:8761/` to view the Eureka console (new microservices instances will automatically register themselves and show up here)
- Open `http://localhost:8761/config/application-dev.yml` to have a look at the properties that are transfered to all apps in the dev profile. You can edit them in the `/central-server-config` directory.

#### ELK (log centralization)

- `docker-compose up -d elk-elasticsearch elk-logstash elk-kibana`
- Open Kibana: `http://localhost:5601`, all logs will show up here.

#### Gateway and microservices

Start the Gateway with:
- `docker-compose up -d gateway`

It should connect with the registry and show up in the Eureka console.
- Open the gateway's admin panel: `http://localhost:8080/#/gateway` (log in with admin/admin)

Also logs should have started to show up in Kibana.

Start fx-price-service with:
- `docker-compose up -d fx-price-service`

Start the other apps:
- `docker-compose up -d supplier-service invoice-service`

#### Scale your apps

You can scale an app by creating **multiple instances** of it (doesn't work on the gateway or other apps that have their ports binded to localhost):
- `docker-compose scale fx-price-service=2`
- `docker-compose scale fx-price-service=3`

Then wait for them to show up at `http://localhost:8761/` and `http://localhost:8080/#/gateway`.

#### Stop an app
- `docker-compose stop appname`


## Shutdown and clean up
- Simply run `docker-compose down`
The following commands may prove useful:
- `docker stop $(docker ps -a -q)`: Stop all running containers
- Then `docker rm $(docker ps -a -q)`: Remove all containers

## Clean everything
Run cleanup script

    ./cleanup.sh

## Deploy to Google Kubernetes engine
Refer to the doc in docs directory



## Setup Intellij
- After code is generated (setup_apps.sh, setup_entities.sh), import the pom file in root-module.
This is the parent module that will load all the child modules (i.e. fx price service, gateway etc.)

## Manual Code Analysis with Sonar Cloud
- Set up account in Sonar cloud, linked to github account. following the instructions:
https://about.sonarcloud.io/get-started/
- Run analysis:
mvn sonar:sonar \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=your_organization_key \
    -Dsonar.login=abcdef0123456789

For sonar.organization and sonar.login, use the organization key and the generated token from the sonar cloud my account area.


## TODO
- Switch between dev and prod ~~with an environment variable~~ different compose files.
- Boot up the database by extending src/main/docker/prod.yml
- (Bonus) Use log_driver to forward database logs to ELK through syslog
