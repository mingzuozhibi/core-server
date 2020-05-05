#!/usr/bin/env bash

# 跳转到项目根目录
cd `dirname $0`/..

version="$1"
version_text="v$1"
option2="$2"

if ! type "mvn" > /dev/null; then
  MVN_CMD=./mvnw
else
  MVN_CMD=mvn
fi

CallRelease() {
    git flow release start $version_text

    $MVN_CMD versions:set "-DnewVersion=$version"
    $MVN_CMD versions:commit

    git add pom.xml
    git commit -m "chore: set version to $version"

    git flow release finish $version_text
}

CallPushAll() {
  echo "开始推送分支 develop"
  git push origin develop

  echo "开始推送分支 master"
  git push origin master

  echo "开始推送标签 $version_text"
  git push origin $version_text
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
