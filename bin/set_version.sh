#!/usr/bin/env bash

##
# 环境准备
##

if ! type mvn >/dev/null 2>&1; then
  MVN_CMD=./mvnw
else
  MVN_CMD=mvn
fi

cd `dirname $0`/..

tagtxt="v$1"
version="$1"
option2="$2"

CallRelease() {
    git flow release start $tagtxt

    $MVN_CMD versions:set "-DnewVersion=$version"
    $MVN_CMD versions:commit

    git add pom.xml
    git commit -m "chore: set version to $version"

    git flow release finish $tagtxt
}

CallPushAll() {
  echo "开始推送分支 develop"
  git push origin develop

  echo "开始推送分支 master"
  git push origin master

  echo "开始推送标签 $tagtxt"
  git push origin $tagtxt
}

if [[ $option2 == "-a" ]]; then
  CallRelease
  CallPushAll
  exit 0
fi

if [[ $option2 == "-f" ]]; then
  CallPushAll
else
  CallRelease
fi
