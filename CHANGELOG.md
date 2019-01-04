# Change Log

## 0.1 (2018-08-29)

**集成motan服务化框架**
- 在oa模块中，编写spring boot启动motan，发布服务

## 0.1 (2018-08-17)

**集成组织权限和门户平台以及定时任务**
- 将原来的组织权限系统集成进来，抽取service服务，通过motan独立发布
- 将原来的建站平台和定时任务集成进来，抽取service服务，通过motan独立发布

## 0.1 (2018-06-06)

**完成spring容器之间的继承关系改造**
- 实现编码方式spring容器和spring-mvc容器之间的继承关系，保证bean可见
- 优化资源加载方式，包括jar包中的资源加载，各个模块负责注册自己的配置到注册中心

## 0.1 (2018-05-22)

**完成配置中心后台功能**
- 实现默认类路径下所有配置文件的加载和缓存，在系统启动时
- 实现了MySQL数据源的插件化改造

## 0.1 (2018-04-30)

**完成核心扩展管理逻辑功能**
- ExtensionLoader功能

## 0.1 (2018-04-10)

**抽取所有web组件到venus-authority工程，去除struts跳转逻辑**
- save struts的国际化资源功能，启动时继续初始化struts
- replace springMVC作为控制层

## 0.1 (2018-03-18)

**初始化venus工程结构，增加venus-common、venus-core工程结构**
- add venus-common 工程结构
- add venus-core 工程结构


