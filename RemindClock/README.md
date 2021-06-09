
# 贝可提醒机

## 需求

个人的一些事件记录，并允许定时提醒或做一些处理，常见场景：

- 生日提醒，每年都要提醒，并可提前5天、3天、1天各提醒一次；
- 临时事件，如10天后去医院复查，只提醒一次；

提醒方案：

- 短信、邮件、钉钉群通知等；
- 时间到，自动调用某个URL。

其它：

- SaaS机制，事件按用户ID隔离；
- 事件可以通知多人，或调用多个URLL。

### 原型地址

产品的原型链接

### 系统上下文图

_【编写说明】用于描述待建系统与所在环境之间的关系，它定义了待建系统与系统外部之间的边界和接口_

### 用例图

_【编写说明】用例图应该区别于功能清单，主要体现出Actor对系统的交互。通过用例图可以帮助设计人员理解及整理需求，建议用例图可以从原型中提取，并使用功能清单进行核对。_

```plantuml
@startuml
left to right direction
actor 用户 as user
usecase "短信/邮件等系统" as sms
package System {
  usecase "登录" as UC_Login
  usecase "个人信息" as UC_Profile
  usecase "注册" as UC_Register
  usecase "编辑" as UC_Modify
  usecase "编辑事件" as UC_Edit
  usecase "提醒方式" as UC_Notify
  usecase "短信提醒" as UC_Sms
  usecase "邮件提醒" as UC_Email
  usecase "钉钉提醒" as UC_Dingding
  usecase "URL调用" as UC_Url
  usecase 计时器 as UC_Timer
  usecase 触发事件 as UC_Event
}
user --> UC_Register
user --> UC_Login
user --> UC_Profile
UC_Profile .> UC_Modify: include
user --> UC_Edit
UC_Edit .> UC_Notify: include
UC_Notify <|-- UC_Sms
UC_Notify <|-- UC_Email
UC_Notify <|-- UC_Dingding
UC_Notify <|-- UC_Url
UC_Edit .> UC_Timer: extends
UC_Timer --> UC_Event
UC_Event --> sms
@enduml
```

### 关键流程

```plantuml
@startuml
title 计时器通知
start
:每秒检查;
repeat
  if(符合触发条件)then(yes)
  	:启动线程;
    :触发提醒;
    note right
    可以有多种提醒方式，循环触发
    窗体、钉钉、短信等
    end note
  else(no)
  endif
repeat while(循环下一个记事) is (yes) not (no)
stop
@enduml
```

```plantuml
@startuml
title 客户端同步流程
start

:读取远程服务端版本号:verServerNow;
note right
首次部署返回0
end note
:读取本地版本号:verClient;
note right
首次无数据时，返回0
end note
:读取前次同步的服务端版本号:verServerLast;
note right
上次与服务端交互时，得到的服务端版本号.
未同步过时，返回0
end note

if(verClient < verServerLast)then(yes)
    :不可能，本地版本只会大于等于本地前次同步信息,有bug;
    stop
endif
if(verServerNow < verServerLast)then(yes)
    :不可能，服务端被重新初始化了或有bug;
    stop
endif

if(verServerNow == verServerLast)then(yes)
    if(verClient == verServerLast)then(yes)
        :服务端与客户端都没有变更，无需同步;
    else (no)
        :POST本地数据给服务端;
        :完成 客户端=>服务端的数据同步;
    endif
else(no)
    if(verClient == verServerLast)then(yes)
        :GET服务端数据;
        :完成 服务端=>客户端的数据同步;
    else (no)
        :本地和服务端同时有变化;
        :提示数据冲突，人工同步;
    endif
endif


stop
@enduml
```

### 其他需求

_【编写说明】来自产品负责人和高级设计人员的非功能性需求_

## 概要设计

### 关键架构决策


### 领域模型或类图

```plantuml
@startuml
title 贝可提醒机服务端模型
class Users<<用户>>{
    Id<int>: 用户唯一ID
    Account<string>: 登录账号
    Token<string>: 数据同步用
    Version<int>: 服务端数据版本号
    CreationTime<time>: 注册时间
    LastModifyTime<time>: 最后编辑时间
}

class Notes<<记事>>{
    Id<int>: 唯一ID
    UserId<int>: 所属用户
    ClientId<int>: 客户端ID
    Title<string>: 标题
    Content<string>: 内容
    DingDingToken<string>: 钉钉群Token，不为空时要发钉钉通知
    CreationTime<time>: 新增时间
    LastModifyTime<time>: 最后编辑时间
    Details<List<NoteDetail>>: 触发时间配置
}

class NoteDetail<<记事触发明细配置信息>>{
    Id<int>: 唯一ID
    NoticeId<int>: 所属记事ID
    EventType<string>: 提醒类型, "单次","每分钟","每小时","每天","周一~周五每天","周六~周日每天","每周","每月","每年"
    EventTime<time>: 提醒时间，根据EventType处理
    CreationTime<time>: 新增时间
    LastModifyTime<time>: 最后编辑时间
}

interface Notice<<提醒>> {
    string call(NoteDetail note);
}

class SmsNotice<<短信提醒>> {
    string call(NoteDetail note);
}
class EmailNotice<<邮件提醒>> {
    string call(NoteDetail note);
}
class DingdingNotice<<钉钉提醒>> {
    string call(NoteDetail note);
}
class UrlNotice<<URL调用>> {
    string call(NoteDetail note);
}

Users o-- Notes
Notes *-- NoteDetail
SmsNotice --|> Notice
EmailNotice --|> Notice
DingdingNotice --|> Notice
UrlNotice --|> Notice
NoteDetail --> Notice
@enduml
```

## 详细设计

### 接口


### 事件

_【编写说明】描述对外公开的事件的详细信息_

### 可复用组件（UI）清单

_【编写说明】按端描述对外公开的可复用界面或组件的清单信息_

## 专项设计

_【编写说明】针对重要或负责的业务进行更细粒度的设计，一般采用状态图、时序图、活动图、数据流图等体现较为复杂的业务场景_

## 技术债务

_【编写说明】由于时间、排期等因素，每个版本的设计或开发都会包含若干含有减法的架构决策，因此需要对该类的决策中未解决的业务及技术问题进行记录，便于下一步进行技术债务的解决_
