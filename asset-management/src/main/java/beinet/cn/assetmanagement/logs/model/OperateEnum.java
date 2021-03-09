package beinet.cn.assetmanagement.logs.model;

public enum OperateEnum {
    Login("员工登录"),
    Register("员工注册"),
    Modify("个人信息修改"),
    ChangePwd("个人密码修改"),

    AddAsset("添加资产"),
    ;

    private String description;

    OperateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}