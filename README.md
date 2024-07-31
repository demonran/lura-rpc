# Lura RPC 框架

### 特点
>手写RPC框架, 支持多种注册中心，多种容器,多种协议， 多种注册方式

#### 插件化支持多种注册中心
- [x] local-file
- [x] etcd
- [ ] nacos
- [ ] zookeeper


#### 多容器支持
- [x] vertx
- [x] tomcat
- [x] netty
- [ ] jetty
- [x] undertow

#### 多种协议支持
- [x] http
- [ ] grpc
- [ ] tcp


#### 多种服务注册方式
- [x] code-base
- [ ] annotation
- [ ] config-file

#### 多种负载均衡策略支持
- [x] random
- [ ] round-robin
- [ ] consistent-hash

#### 插件化支持
- [ ] 通过SPI方式实现多插件的组合
