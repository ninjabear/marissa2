#!/bin/bash

set -e

sbt docker

echo "image built, moving on"

ssh-keyscan -H $MARISSA2_HOSTNAME >> ~/.ssh/known_hosts

export SSHPASS=$MARISSA2_SSH_PWD

echo "uploading image"

docker save com.ninjabear/marissa2:latest | bzip2 | pv | sshpass -e ssh "$MARISSA2_SSH_USER@$MARISSA2_HOSTNAME" 'bunzip2 | docker load'

echo "restarting existing containers"

sshpass -e ssh "$MARISSA2_SSH_USER@$MARISSA2_HOSTNAME" "docker stop \$(docker ps -a -q)" || true

echo "running new build"

sshpass -e ssh "$MARISSA2_SSH_USER@$MARISSA2_HOSTNAME" "docker run -d --rm -e DISCORD_BOT_TOKEN -e BUILD_COMMIT=$TRAVIS_COMMIT com.ninjabear/marissa2 &"
