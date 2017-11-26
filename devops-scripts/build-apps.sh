#!/bin/bash

#Define the folders in which we will run `mvn package docker:build -DskipTests=true`


apps=("gateway" 
"fx-price-service")
for app in "${apps[@]}";
do
    ( cd ../$app && mvn clean package -Pprod dockerfile:build -DskipTests )
done

wait;

echo "built all!"