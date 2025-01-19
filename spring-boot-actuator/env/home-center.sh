#!/usr/bin/env bash

# MySQL
ssh 127.0.0.1 -L 3306:ds918:8306 -N -f
# Redis
ssh 127.0.0.1 -L 6379:ds918:6379 -N -f