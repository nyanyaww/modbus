# modbus

工控网络的大作业

最初的设想是使用java来实现这个功能

## 1. modbus的协议说明

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

        功能码|起始地址|线圈数量
        :-:|:-:|:-:
        0x01|0x0000->0xFFFF(2 char)|0x0001->0x7D00(2 char)|

    - 下位机应答

        功能码|字节数|线圈状态
        :-:|:-:|:-:
        0x01|N(1 char)|(N char)|

    ps. N = n % 8 如果N == 0，那么 N = N + 1

    功能码很简单是0x01，数据地址是也不难，没什么好说的。重点是数据的解释，我们**请求**的时候是请求下位机发送一个线圈的（包括这个线圈）之后的n个线圈的状态。

    而下位机应答的时候，他需要发送字节数以及线圈的状态，其实这一点并不好理解，但是由于这个作业并不涉及太多modbus协议栈所以我不仔细解释。

    下面我们来模拟一次功能码01的执行过程：

    1. 首先是请求(hex)：

        01 00 13 00 13

        ```html
           +--+-----+-----+
           |01|00 13|00 13|
           +--+-----+-----+
        ```

    2. 其次是应答(hex)：

        01 03 CD 6B 05

        ```html
           +--+--+--------+
           |01|03|CD 6B 05|
           +--+--+--------+
        ```

    请求的时候，首先使用是功能码01，那么显然是使用读取线圈的功能。线圈地址是0x0013也就是对应着十进制的19，说明我们的线圈的起始地址是19号线圈。请求的数据也是0x0013，意味着读取19号寄存器之后的19个值。

    应答的时候，使用的也是功能码01，对，就也是使用读取线圈的功能。在从机(client)收到主机(server)的命令的时候，它明白了要发送19个值给主机，19个值需要2个8bit的数据以及1个3bit的数据去表示，我们把CD,6B,05转化为二进制就是`1100 1101 0110 1011`，注意最后一个05转化为`0000 0101`，实际我们取MSB(最高有效位)。

2. 读取离散量输入`function code 0x02`

    与第1条几乎一模一样，除了功能码的不同，其余一致。

3. 读保持寄存器`function code 0x03`

    - 上位机请求

        功能码|起始地址|寄存器数量
        :-:|:-:|:-:
        0x03|0x0000->0xFFFF(2 char)|0x0001->0x007D(2 char)|

    - 下位机应答

        功能码|字节数|寄存器值
        :-:|:-:|:-:
        0x03|N * 2(1 char)|(2 * N char)|

    ps. N = 寄存器数量

    这里的请求与功能码01与02的区别仅仅在于01和02的线圈数量的范围是(1-2000，最大7D00)，而03的寄存器数量是(1-125，最大007D)。

    而应答的不同就在于寄存器值得分为高4位低4位两个char值去封装。

    模拟一次功能码03的运行：

    1. 首先是请求(hex)：

        03 00 6B 00 03

        ```html
           +--+-----+-----+
           |03|00 6B|00 03|
           +--+-----+-----+
        ```

    2. 其次是应答(hex)：

        03 06 02 2B 00 00 00 64

        ```html
           +--+--+-----------------+
           |03|06|02 2B|00 00|00 64|
           +--+--+-----------------+
        ```

    其实也不难理解，上位机发送了我要读保持寄存器，从006B这个地址开始读起，也就是读取107号寄存器组的值，往后读3个。

    下位机做了什么呢，下位机说ok，我返回给你3个值，但是实际上是6个字节，`0x022B 0x0000 0x0064`，这个值一般来说是十进制的，也就是说我得到的那三个寄存器的值是`557 0 100`，这样就ok了。

4. 读输入寄存器`function code 0x04`

    这个与功能码03一致，不予解释。

5. 写单个线圈`function code 0x05`

    - 上位机请求

        功能码|输出地址|输出值
        :-:|:-:|:-:
        0x05|0x0000->0xFFFF(2 char)|0x0001->0xFF00(2 char)|

    - 下位机应答

        功能码|输出地址|输出值
        :-:|:-:|:-:
        0x05|0x0000->0xFFFF(2 char)|0x0001->0xFF00(2 char)|

    这边需要注意的是请求与应答是一致的，还有请注意输出值是只有0xFF00(ON)和0x0000(OFF)是合法值，其余值均不合法，收到舍弃。

    仍然模拟运行一次：

    1. 首先是请求(hex)：

        05 00 AC FF 00

        ```html
           +--+-----+-----+
           |05|00 AC|FF 00|
           +--+-----+-----+
        ```

    2. 其次是应答(hex)：

        05 00 AC FF 00

        ```html
           +--+-----+-----+
           |05|00 AC|FF 00|
           +--+-----+-----+
        ```

    上位机请求说，我需要把0x00AC也就是172号线圈写上0xFF00(ON)的命令。

    下位机执行发现语句合法，于是执行完置172线圈的值为ON之后，将请求语句作为应答语句返回。

6. 写单个寄存器`function code 0x06`

    - 上位机请求

        功能码|寄存器地址|输出值
        :-:|:-:|:-:
        0x06|0x0000->0xFFFF(2 char)|0x0001->0xFFFF(2 char)|

    - 下位机应答

        功能码|寄存器地址|输出值
        :-:|:-:|:-:
        0x06|0x0000->0xFFFF(2 char)|0x0001->0xFFFF(2 char)|

    这里没有什么值得注意的部分，寄存器的值可以是任意的(0000->FFFF)也就是(0->65535)的任意值。

    仍然模拟运行一次：

    1. 首先是请求(hex)：

        06 00 04 10 01

        ```html
           +--+-----+-----+
           |06|00 04|10 01|
           +--+-----+-----+
        ```

    2. 其次是应答(hex)：

        06 00 04 10 01

        ```html
           +--+-----+-----+
           |06|00 04|10 01|
           +--+-----+-----+
        ```

    请求部分，上位机说我需要给4号寄存器写入`1001`。

    下位机收到于是将请求作为应答返回。

## 2. 如何设计mobus的每一帧数据

```html
+-----------+-------------+---------+----+---------+
|device addr|function code|data addr|data|crc check|
+-----------+-------------+---------+----+---------+
```

需要注意的是，我们每次发送与接收数据都得遵守这个格式，device addr是设备地址，但是请注意，他并非ip地址，这个设备地址是与我们约定的，比如说叫0x01，而不是`192.168.0.1`这样的地址。

至于这些如何使用我们的常见数据类型来表示，这个就随意了。

我们来分析一下，`0x00->0xFF`是`0->255`，在java中就用char来表示吧，实际上应该是`byte & 0xff`这样来表示的。但是这个是作业，我想了想还是不要那么麻烦，在cs部分可以自己处理的。我就以char作为底层，封装成String型发送请求，应答也是封装成String型。

## 3. crc校验