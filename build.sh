#!/bin/bash

mvn clean install -Dmaven.test.skip=true
cp ./target/discordbot-1.0.jar app.jar

# build docker image
docker image build -t 'msdobot-server' .

# run docker-compose
docker-compose up