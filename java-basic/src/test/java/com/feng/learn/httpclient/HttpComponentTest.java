package com.feng.learn.httpclient;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class HttpComponentTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testFileUploadAndDownloadUseFluentApi() throws IOException {
        String uploadUrl = "http://upload-stage.yonghuivip.com/uploadUnSafe";
        String dir = HttpComponentTest.class.getClassLoader().getResource("").getFile();
        File file2Upload = new File(dir + "test.txt");

        // 文件上传
        String fileUUID = Request.Post(uploadUrl)
                .body(MultipartEntityBuilder.create()
                        .addBinaryBody("file", file2Upload)
                        .addTextBody("userId", "tag-usage-center") // 默认iso-8859-1编码
                        .addTextBody("ownserver", "crm", ContentType.create("text/plain", StandardCharsets.UTF_8))
                        .addTextBody("json", "{\"name\":\"zhanfeng.zhang\"}", ContentType.APPLICATION_JSON)
                        .build())
                .execute()
                .handleResponse(httpResponse -> {
                    String id = JSON.parseObject(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8))
                            .getJSONObject("data")
                            .getString("fileUUID");
                    return new StringBuilder(id).reverse().toString();
                });

        // 文件下载
        String downUrl = "http://upload-stage.yonghuivip.com/downloadUnSafe/";
        File file2Store = new File(dir + "store.txt");
        Request.Get(downUrl + fileUUID)
                .execute()
                .saveContent(file2Store);
    }

    public void uploadFileToCloud(String fileName, InputStream fileInputStream) {
        String ftpUploadUrl = "";
        // 构建请求
        Request request = Request.Post(ftpUploadUrl)
                .body(MultipartEntityBuilder.create()
                        // 下面2个设置解决中文文件名无法编码问题（默认采用iso-8859-1会把中文都编码成 '?'）
                        .setCharset(StandardCharsets.UTF_8)
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .addBinaryBody("file", fileInputStream, ContentType.APPLICATION_OCTET_STREAM, fileName)
                        .addTextBody("userId", "tag-manage-center")
                        .addTextBody("ownserver", "crm")
                        .build()
                );

        // 失败重试机制，视业务场景而定
        String fileUuid = null;
        for (int count = 0; count < 3; count++) {
            try {
                String resultJson = request.execute().returnContent().asString(StandardCharsets.UTF_8);
                //log.info("upload file({}) to cloud finished. result-{}", fileName, resultJson);
                fileUuid = (JSON.parseObject(resultJson).getJSONObject("data").getString("fileuuid"));
                // important 关闭资源
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                break;
            } catch (Exception e) {
                // ignore
            }
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e1) {
                // ignore
            }
        }
        if (fileUuid != null) {
            // 返回结果
            return;
        } else {
            throw new RuntimeException("upload file to cloud failed after tried 3 times");
        }
    }


    @Test
    public void testFluentApi() throws IOException {
        String content = Request.Get("https://www.baidu.com")
                .execute()
                .returnContent()
                .toString();
    }

    @Test
    public void testAsyncClient() {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();

        client.start();
        client.execute(new HttpGet("http://www.baidu.com"), new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                System.out.println(httpResponse);
            }

            @Override
            public void failed(Exception e) {
                System.out.println(e);
            }

            @Override
            public void cancelled() {
                System.out.println();
            }
        });
    }
}
