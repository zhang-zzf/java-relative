package com.github.zzf.actuator.property_editor;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.SneakyThrows;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-01-04
 */
public class ABeanEditor extends PropertyEditorSupport {
    @SneakyThrows
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
        }
        else {
            Date createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(text);
            setValue(new ABean().setCreatedAt(createdAt));
        }
    }
}
