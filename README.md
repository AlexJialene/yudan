# yudan
斗鱼弹幕抓取

[👉golang version](https://github.com/AlexJialene/go-yudan)

### 消息格式
每条你所请求的消息格式为：<br>

4字节消息长度+4字节消息长度+2字节消息类型+1字节加密字段+1字节保留字段+数据部分（结尾为'\0'）<br>

如图:
![](https://github.com/AlexJialene/yudan/blob/master/protocol.png)

详细说明：<br>

1.消息长度:4 字节小端整数，表示整条消息(包括自身)长度(`文本长度`)。 消息长度出现两遍，二者相同 <br>
* 注意到这里是文本长度，开始以为是字节长度数

2.消息类型:2 字节小端整数，表示消息类型。取值如下:
* 689 客户端发送给弹幕服务器的文本格式数据
* 690 弹幕服务器发送给客户端的文本格式数据

3.加密字段:暂时未用，默认为 0 <br>
4.保留字段:暂时未用，默认为 0 <br>
5.数据部分:斗鱼独创序列化文本数据，结尾必须为‘\0’ <br>
