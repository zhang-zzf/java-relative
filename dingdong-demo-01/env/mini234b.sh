#!/usr/bin/env bash

# MySQL
ssh -L :3306:192.168.56.20:8306 -N -f mini243b
# Redis
ssh -L :6379:192.168.56.20:6379 -N -f mini243b