package com.feng.learn.fastjson;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author zhanfeng.zhang
 * @date 2020/08/13
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Person {

    @NonNull
    private Long id;
    private String name;
    private Address address;

    @Data
    public static class Address {

        private String street;
        private String home;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            if (street != null) {
                sb.append("\"street\":\"").append(street).append('\"').append(',');
            }
            if (home != null) {
                sb.append("\"home\":\"").append(home).append('\"').append(',');
            }
            return sb.replace(sb.length() - 1, sb.length(), "}").toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        if (id != null) {
            sb.append("\"id\":");
            String objectStr = id.toString().trim();
            if (objectStr.startsWith("{") && objectStr.endsWith("}")) {
                sb.append(objectStr);
            }
            else if (objectStr.startsWith("[") && objectStr.endsWith("]")) {
                sb.append(objectStr);
            }
            else {
                sb.append("\"").append(objectStr).append("\"");
            }
            sb.append(',');
        }
        if (name != null) {
            sb.append("\"name\":\"").append(name).append('\"').append(',');
        }
        if (address != null) {
            sb.append("\"address\":");
            String objectStr = address.toString().trim();
            if (objectStr.startsWith("{") && objectStr.endsWith("}")) {
                sb.append(objectStr);
            }
            else if (objectStr.startsWith("[") && objectStr.endsWith("]")) {
                sb.append(objectStr);
            }
            else {
                sb.append("\"").append(objectStr).append("\"");
            }
            sb.append(',');
        }
        return sb.replace(sb.length() - 1, sb.length(), "}").toString();
    }
}
