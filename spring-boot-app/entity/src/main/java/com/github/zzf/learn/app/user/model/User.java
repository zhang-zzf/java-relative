package com.github.zzf.learn.app.user.model;


import static com.github.zzf.learn.app.common.Common.MOBILE_REGEXP_PATTERN;
import static com.github.zzf.learn.app.common.Common._MOBILE_REGEXP;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {

    public static final String USER_TYPE_PATTERN = "^card$|^mobile$";

    public static final String USER_NO_PATTERN = "^card/.*$|^mobile/" + _MOBILE_REGEXP + "$";
    public static final String TYPE_CARD = "card";
    public static final String TYPE_MOBILE = "mobile";
    private String userNo;
    private String type;
    /**
     * 必须唯一
     */
    private String username;
    private String password;
    private LocalDateTime createdAt;

    public static User fromMobile(String mobile) {
        if (!MOBILE_REGEXP_PATTERN.matcher(mobile).matches()) {
            throw new IllegalArgumentException("ILLEGAL_MOBILE");
        }
        User user = new User().setType(TYPE_MOBILE).setUsername(mobile);
        user.setUserNo(user.queryUserNo());
        return user;
    }

    public static User fromCard(String cardNo) {
        User user = new User().setType(TYPE_CARD).setUsername(cardNo);
        user.setUserNo(user.queryUserNo());
        return user;
    }

    public static boolean cardUser(String type) {
        return TYPE_CARD.equals(type);
    }

    public static boolean mobileUser(String type) {
        return TYPE_MOBILE.equals(type);
    }

    public static String queryUsername(String userNo) {
        if (userNo.startsWith(TYPE_MOBILE)) {
            return userNo.substring(TYPE_MOBILE.length() + 1);
        }
        else if (userNo.startsWith(TYPE_CARD)) {
            return userNo.substring(TYPE_CARD.length() + 1);
        }
        throw new IllegalArgumentException();
    }

    public static String queryType(String userNo) {
        return userNo.substring(0, userNo.indexOf('/'));
    }

    public static User from(String userNo) {
        String[] split = userNo.split("/");
        return from(split[0], split[1]);
    }

    public static User from(String type, String username) {
        return switch (type) {
            case TYPE_CARD -> fromCard(username);
            case TYPE_MOBILE -> fromMobile(username);
            default -> throw new IllegalArgumentException();
        };
    }

    public boolean mobileUser() {
        return mobileUser(type);
    }

    public String queryUserNo() {
        if (userNo != null) {
            return userNo;
        }
        return type + "/" + username;
    }

    public boolean cardUser() {
        return cardUser(type);
    }
}
