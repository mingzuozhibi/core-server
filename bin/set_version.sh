#!/usr/bin/env bash

# 项目根目录
cd `dirname $0`/..

# 准备发布新版本
git flow release start v$1

# 更新 pom.xml 版本号
./mvnw versions:set -DnewVersion=$1
./mvnw versions:commit

# 提交 pom.xml 版本号
git add .
git commit -m "chore: set version to v$1"

# 发布新版本
git flow release finish v$1
