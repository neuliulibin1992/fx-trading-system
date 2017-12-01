#!/bin/bash

# Generate entities in front end gateway

cd ../gateway

entities=("FxRate"
"CustomerCategory" 
)
for entity in "${entities[@]}";
do
    ( yes "" | yo jhipster:entity $entity && wait) 
done
