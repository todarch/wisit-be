# wisit backend service

- http://localhost:7010/swagger-ui.html

### docker

```shell
# dockerize but do not publish
./gradlew jibDockerBuild
# export IMAGE=xyz/xyzx; ./gradlew jibDockerBuild
docker images | grep wisit
```

- run locally

```shell
docker images | grep wisit
docker run -rm -p 8080:8080 todar/wisit-be
```

