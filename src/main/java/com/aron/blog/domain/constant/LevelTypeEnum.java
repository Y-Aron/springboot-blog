package com.aron.blog.domain.constant;

public enum LevelTypeEnum {
    SuperAdmin(0),
    SeniorAdmin(1),
    SystemAdmin(2),
    CommonUser(3),
    Visitor(4);

    private final int value;

    LevelTypeEnum(int value) {
        this.value = value;
    }
    public static boolean isDefine(int value){
        for (LevelTypeEnum levelTypeEnum: LevelTypeEnum.values()){
            if (levelTypeEnum.value == value){
                return true;
            }
        }
        return false;
    }

    public int getValue() {
        return value;
    }
}
