package com.project.entity.enums;

public enum RoleType {
    ADMIN("Admin"), //"Admin" string karsiligi
    TEACHER("Teacher"),
    STUDENT("Student"),
    MANAGER("Dean"),
    ASSISTANT_MANAGER("ViceDean");

    public final String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
