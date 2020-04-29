#!/usr/bin/env bash

cd `dirname $0`

mysql -uroot -p < sqls/dll.sql
