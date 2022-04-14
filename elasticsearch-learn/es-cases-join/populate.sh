#!/usr/bin/env bash

ADDRESS=$1

if [ -z "$ADDRESS" ]; then
  ADDRESS="localhost:9200"
fi

# Check that Elasticsearch is running
curl -H 'Content-Type: application/json' -s "http://$ADDRESS" &>/dev/null
if [ $? != 0 ]; then
  echo "Unable to contact Elasticsearch at $ADDRESS"
  echo "Please ensure Elasticsearch is running and can be reached at http://$ADDRESS/"
  exit 1
fi

index_name="weibo_msg_and_comment"

echo "WARNING, this script will delete the '${index_name}' indices and re-index all data!"
echo "Press Control-C to cancel this operation."
echo
echo "Press [Enter] to continue."
read -r

# Delete the old index, swallow failures if it doesn't exist
curl -H 'Content-Type: application/json' -s -XDELETE "$ADDRESS/${index_name}"
echo

# Create the next index using mapping_group.json
echo "Creating '${index_name}' index..."
curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}" -d@$(dirname $0)/${index_name}.json

# Wait for index to become yellow
curl -H 'Content-Type: application/json' -s "$ADDRESS/${index_name}/_health?wait_for_status=yellow&timeout=10s" >/dev/null
echo
echo "Done creating '${index_name}' index."

echo
echo "Indexing data..."

echo "Indexing data into ${index_name}..."

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/1" -d'{
  "who": "zhang.zzf",
  "when": "2012-06-15 15:36:25",
  "what": "This is my first weibo message.",
  "join_field": "msg"
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/2" -d'{
  "who": "zhang.zzf",
  "when": "2012-06-15 17:36:25",
  "what": "This is my second weibo message.",
  "join_field": "msg"
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/11" -d'{
  "who": "huang.hhl",
  "when": "2012-07-15 15:36:25",
  "what": "This is my first weibo message.",
  "join_field": "msg"
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/12" -d'{
  "who": "huang.hhl",
  "when": "2012-07-15 17:36:25",
  "what": "This is my second weibo message.",
  "join_field": "msg"
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/1001?routing=1" -d'{
  "who": "huang.hhl",
  "when": "2012-07-15 17:37:25",
  "what": "Amazing!",
  "join_field": {
    "name": "comment",
    "parent": "1"
  }
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/1002?routing=1" -d'{
  "who": "huang.hhl",
  "when": "2012-07-15 17:38:25",
  "what": "Awesome!",
  "join_field": {
    "name": "comment",
    "parent": "1"
  }
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/1003?routing=11" -d'{
  "who": "zhang.zzf",
  "when": "2012-07-16 17:38:25",
  "what": "What a Awesome idea!",
  "join_field": {
    "name": "comment",
    "parent": "11"
  }
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/1004?routing=11" -d'{
  "who": "zhang.zzf",
  "when": "2012-07-16 17:39:25",
  "what": "What a Awesome idea!",
  "join_field": {
    "name": "comment",
    "parent": "11"
  }
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/1013?routing=12" -d'{
  "who": "zhang.zzf",
  "when": "2012-07-18 17:38:25",
  "what": "What a Awesome idea!",
  "join_field": {
    "name": "comment",
    "parent": "12"
  }
}'
echo

curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/1005?routing=11" -d'{
  "who": "huang.hhl",
  "when": "2012-07-15 18:38:25",
  "what": "Awesome!",
  "join_field": {
    "name": "comment",
    "parent": "11"
  }
}'
echo

echo "Done indexing groups."

echo
echo "Done indexing data."
echo
