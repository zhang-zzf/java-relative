package com.github.learn.infra.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HttpMethodsResp {

  @JsonProperty("args")
  private Args args;
  @JsonProperty("data")
  private String data;
  @JsonProperty("files")
  private Files files;
  @JsonProperty("form")
  private Form form;
  @JsonProperty("headers")
  private Map<String, String> headers;
  @JsonProperty("json")
  private Object json;
  @JsonProperty("origin")
  private String origin;
  @JsonProperty("url")
  private String url;

  @NoArgsConstructor
  @Data
  public static class Args extends HashMap<String, String> {
  }

  @NoArgsConstructor
  @Data
  public static class Files {
  }

  @NoArgsConstructor
  @Data
  public static class Form {
  }

}
