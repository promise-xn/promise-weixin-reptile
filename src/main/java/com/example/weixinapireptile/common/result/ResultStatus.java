package com.example.weixinapireptile.common.result;

/**
 * 请求返回响应码
 *
 * @author zhanglincheng
 * @date 2022/10/1
 */
public enum ResultStatus {
    /***/
    SUCCESS("00000", "success"),
    FAILURE("99999", "failure"),
    BIZ_EXCEPTION("50000", "业务异常"),
    SYS_INNER_EXCEPTION("99999", "系统内部异常"),
    UNKNOWN_EXCEPTION("-1", "未知异常"),

    /** **/
    USER_NOT_EXIST("A1001", "用户不存在"),
    USER_PASSWORD_ERROR("A1002", "用户密码输入错误"),
    USER_ACCOUNT_LOCKED("A1004", "当前账户已被锁定,请联系管理员"),
    USER_LOGIN_EXPIRED("A1010", "登录状态已过期"),
    USER_LOGIN_ERROR("A1011", "用户登录异常"),
    USER_OTHER_DEVICE_LOGIN("A1012", "您的账户已在其它设备登录"),
    USER_NOT_LOGIN("A1013", "用户未登录"),
    TOKEN_LOGIN_EXPIRED("A1019", "访问令牌已过期"),
    TOKEN_ACCESS_FORBIDDEN("A1020", "访问令牌无效"),


    /**
     * 参数校验
     */
    PARAM_ERROR("A0400", "请求参数错误"),
    ILLEGAL_REQUEST("A0405", "非法请求"),
    PARAM_IS_NULL("A0410", "请求必填参数为空"),

    /**
     * 系统级别
     */
    SYSTEM_EXECUTION_ERROR("B0001", "系统执行出错"),
    SYSTEM_EXECUTION_TIMEOUT("B0100", "系统执行超时"),
    SYSTEM_ORDER_PROCESSING_TIMEOUT("B0100", "系统订单处理超时"),
    SYSTEM_DISASTER_RECOVERY_TRIGGER("B0200", "系统容灾功能被出发"),
    FLOW_LIMITING("B0210", "系统限流"),
    DEGRADATION("B0220", "系统功能降级"),
    SYSTEM_RESOURCE_ERROR("B0300", "系统资源异常"),
    SYSTEM_RESOURCE_EXHAUSTION("B0310", "系统资源耗尽"),
    SYSTEM_RESOURCE_ACCESS_ERROR("B0320", "系统资源访问异常"),
    SYSTEM_READ_DISK_FILE_ERROR("B0321", "系统读取磁盘文件失败"),

    /**
     * 服务
     */
    SERVICE_NOT_FOUND("C0000", "服务未找到"),
    SERVICE_INTERNAL_ERROR("C0001", "服务内部错误"),
    CALL_THIRD_PARTY_SERVICE_ERROR("C0002", "调用第三方服务出错"),
    REQUEST_SERVICE_TIMEOUT("C0003", "请求服务超时"),
    RESOURCE_NOT_FOUND("C0004", "请求资源不存在"),
    MIDDLEWARE_SERVICE_ERROR("C0010", "中间件服务出错"),
    SERVICE_STOPPED("C9999", "服务已停用"),

    /**
     * DB
     */
    DATABASE_ERROR("DB001", "数据库服务出错"),
    DATABASE_TABLE_NOT_EXIST("DB002", "表不存在"),
    DATABASE_COLUMN_NOT_EXIST("DB003", "列不存在"),
    DATABASE_DUPLICATE_COLUMN_NAME("DB004", "多表关联中存在多个相同名称的列"),
    DATABASE_DEADLOCK("DB005", "数据库死锁"),
    DATABASE_PRIMARY_KEY_CONFLICT("DB006", "主键冲突");


    //DATABASE_PRIMARY_KEY_CONFLICT("Z0001", "异常");

    private final String status;

    private final String message;

    ResultStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
