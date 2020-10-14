
## 框架入口
+ com.sayweee.wrapper.http.RetrofitIml
> 对外暴漏对象，提供初始化相关方法和生成API Service接口

## 特殊介绍
+ com.sayweee.wrapper.bean.BaseBean
> 若继承此类，将仅解析data内部的对象，不对外层结构进行返回，且保证返回体是有效的

+ com.sayweee.wrapper.bean.N
> 若继承此类，将不对返回体就行解析，且不考虑返回result是否正确，需要二次解析

+ com.sayweee.wrapper.bean.FailureBean
> 通用请求失败返回类

+ com.sayweee.wrapper.http.ExceptionHandler
> 异常处理类，并提供异常code的封装

+ com.sayweee.wrapper.http.ResponseException
> 异常类，用于上层定位原因和提供提示