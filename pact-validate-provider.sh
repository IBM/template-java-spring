#!/usr/bin/env bash

# Validates consumer pact against the provider swagger documentation
# This script is intended to be run on the service consumer to validate
# the provider

# Currently the script only supports a single provider whose endpoint is passed
# in as the first argument (or defaults to 'http://localhost:9080/v2/api-docs')
# This will be addressed in an upcoming update by providing a config file that
# maps Provider_Name=Provider_Endpoint.

pactDir="$1"
if [[ -z "$2" ]]; then
    swaggerEndpoint="http://localhost:9080/v2/api-docs"
else
    swaggerEndpoint="$2"
fi

# call validate service
ls ${pactDir}/*.json | while read pactFile; do
    echo -e "Testing ${pactFile} pact against swagger documentation: ${swaggerEndpoint}"
    ./node_modules/swagger-mock-validator/bin/swagger-mock-validator "${swaggerEndpoint}" "${pactFile}"
done
