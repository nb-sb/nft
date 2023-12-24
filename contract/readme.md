若无特殊原因，mysql中数据应和区块链中的表有一份对应的

数字藏品可以有多个拥有者，但是只能有一个作者

统一交互规则：

    1.合约将所有返回的地址信息需要用string类型转换
    2.接收地址一律用address类型接受，存贮用string类型存贮，不用string类型接受地址的原因是用address类型可以自动判断传入的是否是地址
    3.更新信息等需要保证业务原子性

合约部署顺序

```
1.部署SellStroage -- 发售合约
2.部署OwnershipStorage合约 -- 存贮当前用户的藏品所属权合约
3.部署DetailStorage合约 -- 流水记录合约
4.部署UserStorage合约 -- 用户区块链地址和实体mysql中信息对应合约
```

合约调用流程

注：以下仅为合约快读使用流程，可以快速了解合约运行流程

 1.发售合约SellStroage

    调用createSell方法：
    调用者：0x738e8a6024d68cd6193a44cc29e56e622f017754
    参数：hash123
    参数：10

2.购买合约OwnershipStorage

```
调用addOwnership
参数：0x738e8a6024d68cd6193a44cc29e56e622f017754
参数：hash123
```

调用查询方法selectByUserAddr、selectByHash查询插入情况

```
调用转增
调用者：0x738e8a6024d68cd6193a44cc29e56e622f017754
参数：0x2091c7b11e8b0985772e016acd6eea731ae49bc6
参数：hash123
```

​	调用查询方法selectByUserAddr、selectByHash查询转增情况

3.调用流水合约DetailStorage

​	调用查询方法selectByUserAddr、selectByUserAccept、selectByHash可以查询到上面的交易信息包括自己的接受订单、转移订单、使用数字藏品hash可以查询到对应的所有的交易的订单