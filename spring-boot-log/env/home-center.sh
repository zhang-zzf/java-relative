#!/usr/bin/env bash

# MySQL
ssh 127.0.0.1 -L :3306:10.0.9.18:8306 -N -f
# Redis
ssh 127.0.0.1 -L :6379:10.0.9.18:6379 -N -f