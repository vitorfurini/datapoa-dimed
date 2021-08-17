package com.datapoamobilidade.enums;

public enum OperacaoEnum {
    UPDATE(1, "UPDATE"),
    CREATE(2, "CREATE");

    private int code;
    private String description;

    OperacaoEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return this.description;
    }
}
