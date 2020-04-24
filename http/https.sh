#!/usr/bin/env bash

cmd="http --session $(cd `dirname $0`; pwd)/session.json $@"
echo $cmd; $cmd