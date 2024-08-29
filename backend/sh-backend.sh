#!/bin/bash

# 권한 부여 (아무나 실행할 수 있도록 설정)
chmod +x $0

docker logout
# Docker Hub 로그인 정보
DOCKER_HUB_USERNAME=
DOCKER_HUB_PASSWORD=

# Docker 로그인 명령어 실행
echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin

docker buildx create --name mybuilder --use
docker buildx inspect --bootstrap
docker buildx build --platform linux/amd64 -t gbds234/back:latest --push .

