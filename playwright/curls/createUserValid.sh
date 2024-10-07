#!/bin/sh

curl -X POST -d @../../api-poc/src/test/resources/mappings/createUserValid.json --header "Content-Type:application/json" http://localhost:8080/__admin/mappings