# modbus

工控网络的大作业

最初的设想是使用java来实现这个功能

## 1.modbus的协议说明

最基本的格式是下文所述的RTU格式，我们抽取其中最关键的来说一下，总共有5个部分组成，其中源地址和crc校验在后文会解释。

```html
+-----------+-------------+---------+----+---------+
|device addr|function code|data addr|data|crc check|
+-----------+-------------+---------+----+---------+

源地址 | 功能码 | 数据地址 | 数据 | crc校验
```

### 功能码的请求与应答

1. 读取线圈`function code 0x01`

    - 上位机请求
        功能码|数据地址|数据
        :-:|:-:|:-:
        0x01|0x0000->0xFFFF(2 char)|0x0001->0x7D00(2 char)|

    - 下位机应答
        功能码|字节数|线圈状态
        :-:|:-:|:-:
        0x01|N(1 char)|(N char)|

    ps. N = n % 8 如果N == 0，那么 N = 1

    功能码很简单是0x01，数据地址是也不难，没什么好说的。重点是数据的解释，我们**请求**的时候是请求下位机发送一个线圈的（包括这个线圈）之后的n个线圈的状态。

    而下位机应答的时候，他需要发送字节数以及线圈的状态，其实这一点并不好理解，但是由于这个作业并不涉及太多modbus协议栈所以我不仔细解释。

    下面我们来模拟一次功能码01的执行过程：

    1. 首先是请求(hex)：

        01 00 13 00 13

    2. 其次是应答(hex)：

        01 03 CD 6B 05

    请求的时候，首先使用是功能码01，那么显然是使用读取线圈的功能。线圈地址是0x0013也就是对应着十进制的19，说明我们的线圈的起始地址是19号线圈。请求的数据也是0x0013，意味着读取19号寄存器之后的19个值。

    应答的时候，使用的也是功能码01，对，就也是使用读取线圈的功能。在从机(client)收到主机(server)的命令的时候，它明白了要发送19个值给主机，19个值需要2个8bit的数据以及1个3bit的数据去表示，我们把CD,6B,05转化为二进制就是`1100 1101 0110 1011`，注意最后一个05转化为`0000 0101`，实际我们取MSB(最高有效位)。
