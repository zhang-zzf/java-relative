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

for ((i = 0; i < 1000000; i++)); do
  curl -H 'Content-Type: application/json' -s -XPUT "$ADDRESS/${index_name}/_doc/${i}" -d"{
    \"who\": \"System\",
    \"when\": \"2012-06-15 15:36:25\",
    \"what\": \"This is a weibo message （你好，世界！） published at ${i}.\",
    \"join_field\": \"msg\"
  }"
  printf "\n"
  sleep 1s
done
echo -n

echo
echo "Done indexing data."
echo
