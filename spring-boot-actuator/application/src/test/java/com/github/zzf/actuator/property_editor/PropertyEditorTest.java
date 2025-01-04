package com.github.zzf.actuator.property_editor;

import static org.assertj.core.api.BDDAssertions.then;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import org.junit.jupiter.api.Test;

/**
 * @author : zhanfeng.zhang@icloud.com
 * @date : 2025-01-04
 */
public class PropertyEditorTest {

    @Test
    void givenPropertyEditor_when_then() {
        PropertyEditor editor = PropertyEditorManager.findEditor(ABean.class);
        then(editor).isNotNull();
        editor.setAsText("2024-01-04T12:34:56+08:00");
        Object value = editor.getValue();
        then(value).isInstanceOf(ABean.class);
        PropertyEditor bEditor = PropertyEditorManager.findEditor(BBean.class);
        then(bEditor).isNull();
    }
}
