# Changelog

## Version 0.0.5 *(2017-12-12)*

* 去除 library 中对 appcompat-v7 的依赖

## Version 0.0.4 *(2017-12-04)*

* 业务码长度改为 6 位
* 新增业务类型常量类 [WSBusinessType](https://github.com/UamaHZ/long-conn-manager/blob/master/library/src/main/java/cn/com/uama/longconnmanager/WSBusinessType.java)
* `WSMessageCode` 新增创建客户端消息方法 [WSMessageCode.createClient(int businessType, int businessCode)](https://github.com/UamaHZ/long-conn-manager/blob/e5dd47bdc355db3694fba74ce627b0eef54c5afb/library/src/main/java/cn/com/uama/longconnmanager/WSMessageCode.java#L48)


## Version 0.0.3 *(2017-11-29)*

* `onMessage(WSConnection connection, String text)` 回调方法签名改为 `onMessage(WSConnection connection, WSMessage<String> message)`
* 新增登录失败回调方法 `onLoginFailure()`
* `onConnectionFailed()` 回调方法签名改为 `onConnectionFailure()`

## Version 0.0.2 *(2017-11-29)*

* 将 `LMLongConnManager.release(String url)` 方法改为 `LMLongConnManager.releaseConnection(WSConnection connection)`

## Version 0.0.1 *(2017-11-28)*
Initial release.