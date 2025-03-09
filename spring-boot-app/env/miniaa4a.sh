#!/usr/bin/env bash

# MySQL
ssh -L :3306:192.168.56.20:8306 -N -f -o StrictHostKeyChecking=no 127.0.0.1
# Redis
ssh -L :6379:192.168.56.20:6379 -N -f -o StrictHostKeyChecking=no 127.0.0.1
# start consul in background
bash ~/workspace/consul/start_agent_in_mac.sh