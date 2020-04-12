#!/usr/bin/env bash

http :5000/api/loggers/core-server levels==INFO,DEBUG $*
