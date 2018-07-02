package com.antiscam.enums;

/**
 * 算法枚举
 *
 * @author wuming
 */
public enum Algorithm {

    SHA256("SHA-256", "SHA256"), RIPEMD160("RipeMD160", "RipeMD160"), ECDSA("ECDSA", "ECDSA"),
    SHA256WITHECDSA("SHA256withECDSA", "SHA256withECDSA");

    private String name;
    private String desc;

    Algorithm(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for property 'desc'.
     *
     * @return Value for property 'desc'.
     */
    public String getDesc() {
        return desc;
    }
}
