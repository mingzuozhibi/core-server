#!/usr/bin/env bash

http :5000/api/logger/core-server levels==INFO,DEBUG $*
