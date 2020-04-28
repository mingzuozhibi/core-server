#!/usr/bin/env bash

cd `dirname $0`/..

mysql -uroot -p < config/dll.sql
