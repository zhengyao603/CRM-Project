package com.ssmproject.crm.commons.utils;

import java.util.UUID;

public class UUIDUtils {

    /**
     * 随机生成32位UUID
     * @return 字符串形式的UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
