# Wrapper

## 需求
+ 原有框架扩展
    > 根据功能明确模块功能并抽离，统一异常编码和bean类命名
+ mvp方案兼容
    > 从原有方案中解耦，统一底层，合理抽层
+ mvvm方案实现
    > 简化vm和m层结合，和mvp公用底层
+ data binding
    > binding方案实现，可自主选择是否使用
+ 网络
    > retrofit、okhttp
+ 数据解析分发
    > gson、rxjava、callback
+ 多语言
    > 提供全局注入方案
+ 状态栏适配
    > 全局统一，采用统一一套方案，基类预埋
+ 多状态布局 
    > 根据当前网络/请求状态/异常显示不同状态
+ 异步框架
    > rxJava
+ 数据存储选型
    > 临时存储方案/缓存存储方案/数据库存储方案
+ 通用标题栏
    > IWrapper相关扩展
+ loading效果自己开启关闭
    > mvp和mvvm两种跟随网络请求方案实现
+ 提供合适的注释和提示
    > 关键环节的关联上，如何使用不合理提供提示说明     
+ 提供混淆
    > 各library单独混淆                       


## 实现
+ wrapper-core      
    > 通用基类实现，提供上层使用         
    > 提供通用回调和扩展工具类       
    > 提供简单配置的能力         

+ wrapper-http      
    > 网络请求封装        
    > 提示通用bean类     
    > 提供兼容解析方案      
    > 和其他库解耦        
                                  
+ wrapper-mvp
    > 提供mvp方案实现，整体分成3层 view、presenter、model     
    > 提供状态方案实现       
    > 简化http请求流程        
    > 简化m层数据分发      
    > 最小改动兼容原有方案        
                  
+ wrapper-mvvm
    > 提供mvvm方案实现，整体分成4层 view、view model、model、service，service层是model层的扩展，用于为model层提供能力支持        
    > 提供状态方案实现        
    > 简化m层和vm层的关联       
    > 简化m层和service层的关联，提供自动注入功能     
    > 提供m层和vm层生命周期支持        
    > 提供通用数据分发方案        
