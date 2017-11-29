# Changelog

## Version 0.0.3 *(2017-11-29)*

* `onMessage(WSConnection connection, String text)` 回调方法签名改为 `onMessage(WSConnection connection, WSMessage<String> message)`
* 新增登录失败回调方法 `onLoginFailure()`
* `onConnectionFailed()` 回调方法签名改为 `onConnectionFailure()`

## Version 0.0.2 *(2017-11-29)*

* 将 `LMLongConnManager.release(String url)` 方法改为 `LMLongConnManager.releaseConnection(WSConnection connection)`

## Version 0.0.1 *(2017-11-28)*
Initial release.