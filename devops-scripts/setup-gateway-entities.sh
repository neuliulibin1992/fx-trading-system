#!/bin/bash

# Generate entities in front end gateway

cd ../gateway

entities=("FxRate"
"CurrencyMap"
)
for entity in "${entities[@]}";
do
    ( yes "" | jhipster entity $entity && wait)
done
