package beinet.cn.assetmanagement.logs.model;

public enum OperateEnum {
    Login("员工登录"),
    Register("员工注册"),
    Modify("个人信息修改"),
    ChangePwd("个人密码修改"),
    AdminAdd("管理员添加员工"),
    AdminModify("管理员编辑员工"),
    AdminResetPwd("管理员重置员工密码"),
    AdminChgState("管理员改员工状态"),

    AddDepartment("添加部门"),
    EditDepartment("编辑部门"),

    AddAssetClass("添加分类"),
    EditAssetClass("编辑分类"),

    AddConfig("添加配置"),
    EditConfig("编辑配置"),

    AddAsset("添加资产"),
    EditAsset("编辑资产"),
    ;

    private String description;

    OperateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}