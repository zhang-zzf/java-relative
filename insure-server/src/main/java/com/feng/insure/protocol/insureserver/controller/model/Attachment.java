package com.feng.insure.protocol.insureserver.controller.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>文件附件</p>
 *
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/10/15
 */
@Data
@Accessors(chain = true)
public class Attachment {

    private String type;
    private List<File> fileList;
    private LocalDateTime createdAt;

    @Data
    @Accessors(chain = true)
    public static class File {

        private String name;
        private String ossFileUrl;

    }


}
