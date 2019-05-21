package com.swpu.analyse.enums;

import lombok.Getter;

/**
 * 异常枚举类
 *
 * @author cyg
 * @date 18-3-7 下午7:56
 **/
@Getter
public enum ResultEnum {

    /****/
    UNKNOWN_ERROR(-1, "未知错误"),
    LOGIN_ERROR(2, "登录失败,用户密码错误"),
    WITHOUT_THIS_USER(3, "没有此用户"),
    USER_REGISTRATION_FAILURE(4, "用户注册失败"),
    INSERT_ERROR(5, "插入失败"),
    UPDATE_ERROR(6, "更新失败"),
    DELETE_ERROR(7, "删除失败"),
    FILE_IS_EMPTY(9, "文件为空"),
    UPLOAD_FILE_FAILURE(10, "文件为空"),
    AN_OBJECT_THAT_IS_NOT_SATISFIED(11, "无满足条件的对象"),
    TOKEN_NON_ERROR(13, "token信息必传"),
    TOKEN_ERROR(14, "token验证失败"),
    NEW_AND_OLD_CIPHER_INCONSISTENCIES(15, "新旧密码不一致"),
    PASSWORD_ERROR(16, "原密码错误"),
    PARAMETERS_ARE_EMPTY(17, "空指针异常"),
    LACK_OF_AUTHORITY(18, "权限不足"),
    VERIFICATION_CODE_FAILURE(19, "验证码失效"),
    PHONE_NOT_RIGHT_FORMAT(20, "手机号格式不对"),
    EMAIL_NOT_RIGHT_FORMAT(21, "邮箱格式不对"),
    ID_CARD_NOT_RIGHT_FORMAT(21, "身份证号格式不对"),
    INSERT_ERROR_RESUME(23, "上传简历失败"),
    INSERT_ERROR_STUDENT(24, "预报名失败"),
    INSERT_ERROR_NUMBER(25, "申请编号失败"),
    THE_ID_CARD_NOT__REGISTRATION(26, "该身份证号没有申请报名编号"),
    RESET_PASSWORD_FAILED(27, "重置密码失败"),
    THE_ID_CARD_HAVE_REGISTRATION(28, "该身份证号已经申请过报名编号"),
    SEND_ERROR(29, "该身份证号已经申请过报名编号"),
    WITHOUT_THIS_STUDENT(30, "没有此学生"),
    WITHOUT_THIS_UNIVERSITY(30, "没有此高校"),
    WITHOUT_THIS_DEPARTMENT(31, "没有此学院"),
    WITHOUT_THIS_MAJOR(32, "没有此专业"),
    NO_CROSS_YARD_VIEW(33, "不得跨院查看信息"),
    OP_ERROR(34, "操作失败,更新数据库出错,请联系开发人员"),
    INSERT_NOTICE_ERROR(34, "发布通知失败"),
    UPDATE_NOTICE_ERROR(35, "更新通知失败"),
    CHOICE_UNIVERSITY(36, "请选择学校"),
    TITLE_CONTENT_NOT_EMPTY(37, "标题或内容不能为空");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
