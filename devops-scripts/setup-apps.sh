#!/bin/bash

# Define the folders in which we will run `yo jhipster`

apps=("gateway" 
"fx-price-service")
for app in "${apps[@]}";
do
    ( cd ../$app && jhipster --force --with-entities )
done
