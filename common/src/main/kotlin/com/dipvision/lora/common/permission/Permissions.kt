package com.dipvision.lora.common.permission

enum class Permissions(val permission: Permission) {
    NOTHING(Permission(0)),

    CONTROL_CONNECT(Permission(1L shl 0)),
    INSTALL_PROGRAM(Permission(1L shl 1)),

    READ_FACILITY(Permission(1L shl 2)),
    CREATE_FACILITY(Permission(1L shl 3)),
    UPDATE_FACILITY(Permission(1L shl 4)),
    DELETE_FACILITY(Permission(1L shl 5)),
    TEST_FACILITY(Permission(1L shl 6)),

    READ_COMPLAIN(Permission(1L shl 7)),
    CREATE_COMPLAIN(Permission(1L shl 8)),
    UPDATE_COMPLAIN(Permission(1L shl 9)),
    DELETE_COMPLAIN(Permission(1L shl 10)),

    READ_MAINTENANCE(Permission(1L shl 11)),
    CREATE_MAINTENANCE(Permission(1L shl 12)),
    UPDATE_MAINTENANCE(Permission(1L shl 13)),
    DELETE_MAINTENANCE(Permission(1L shl 14)),
    ISSUE_MAINTENANCE(Permission(1L shl 15)),

    CREATE_NEW_MATERIALS(Permission(1L shl 16)),
    ALLOW_REPORT(Permission(1L shl 17)),
    USER_SETTING(Permission(1L shl 18)),
    MANAGE_CONTROL(Permission(1L shl 19)),

    CREATE_GROUP(Permission(1L shl 20)),
    UPDATE_GROUP(Permission(1L shl 21)),
    DELETE_GROUP(Permission(1L shl 22)),

    SEOUL(Permission(1L shl 23)),
    BUSAN(Permission(1L shl 24)),
    DAEGU(Permission(1L shl 25)),
    INCHEON(Permission(1L shl 26)),
    GWANGJU(Permission(1L shl 27)),
    DAEJEON(Permission(1L shl 28)),
    ULSAN(Permission(1L shl 29)),
    SEJONG(Permission(1L shl 30)),
    ;
}
