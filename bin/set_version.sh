#!/usr/bin/env bash

# 跳转到项目根目录
cd `dirname $0`/..

CallRelease() {
    git flow release start v$1

    ./mvnw versions:set -DnewVersion=$1
    ./mvnw versions:commit

    git add pom.xml
    git commit -m "chore: set version to v$1"

    git flow release finish v$1
}

CallPushAll() {
  echo "开始推送分支 develop"
  git push origin develop

  echo "开始推送分支 master"
  git push origin master

  echo "开始推送标签 v$1"
  git push origin v$1
}

if [ $2 -eq "-a" ]; then
  CallRelease
  CallPushAll
  exit 0
fi

if [ $2 -eq "-f" ]; then
  CallPushAll
else
  CallRelease
fi
