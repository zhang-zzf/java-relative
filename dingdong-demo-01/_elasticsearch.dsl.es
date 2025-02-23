GET /_cat/health?v

GET /_cat/indices?v

PUT some-test-index-100/_doc/1
{
  "msg": "some message"
}

GET some-test-index-100/_doc/1

DELETE some-test-index-100

GET /some-test-index-100/_search
{
  "query": {
    "match_all": {}
  }
}

GET /some-test-index-100/_search
{
  "from" : 0,
  "size" : 10,
  "query" : {
    "match" : {
      "msg" : "Message"
    }
  }
}
