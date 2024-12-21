#!/usr/bin/env bash

# MySQL
ssh admin@10.255.3.12 -L 127.0.0.1:3306:10.255.3.12:8306 -N -f
# Redis
ssh admin@10.255.3.12 -L 127.0.0.1:6379:10.255.3.12:6379 -N -f