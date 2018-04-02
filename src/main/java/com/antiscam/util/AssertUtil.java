package com.antiscam.util;

/**
 * 断言工具
 *
 * @author wuming
 */
public class AssertUtil {

    /**
     * 校验
     *
     * @param flag bool flag
     */
    public static void check(boolean flag) {
        if (!flag) {
            throw new RuntimeException("Assert failed");
        }
    }

    /**
     * 校验
     *
     * @param flag bool flag
     * @param msg  失败后提示信息
     */
    public static void check(boolean flag, String msg) {
        if (!flag) {
            throw new RuntimeException(msg);
        }
    }

    private AssertUtil() {
    }
}
