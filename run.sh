#!/bin/sh

if [ $# -ne 0 ]; then
  echo "No arguments accepted."
  exit 1
fi

mkdir -p "$PWD/results"

docker run -it --rm \
    -v $PWD/src/main/resources/opt/gatling/user-files/resources \
    -v $PWD/src/main/scala:/opt/gatling/user-files/simulations \
    -v $PWD/results:/opt/gatling/results \
    denvazh/gatling:3.2.1