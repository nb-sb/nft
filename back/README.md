代码待完善中


作者友情提示初学者:
1. 开发大忌 空指针 , 使用optional进行处理
2. 用户不需要知道那么多，返回结果只需要返回片面的就可以了,比如传入参数需要等于1，但是传入了0或者其他字母非数字的，只需要提示参数错误即可，其他的比如：”此处由于...限制，只能...“。 这样的信息只需要打印日志即可，别全都给用户提示，等哪天被脚本小子揍了你才知道吗？

错误码

1：正常信息，并且可以直接将后端返回的内容直接弹窗提示给前端

0 ：错误信息，并且可以直接将后端返回的内容直接弹窗提示给前端

401： 一般是用户token错误，前端进行重新登录就可以了

1、toString()，可能会抛空指针异常

在这种使用方法中，因为java.lang.Object类里已有public方法.toString()，所以java对象都可以调用此方法。但在使用时要注意，必须保证object不是null值，否则将抛出NullPointerException异常。采用这种方法时，通常派生类会覆盖Object里的toString()方法。

2、String.valueOf()，推荐使用，返回字符串“null”

String.valueOf()方法是小编推荐使用的，因为它不会出现空指针异常，而且是静态的方法，直接通过String调用即可，只是有一点需要注意，就是上面提到的，如果为null，String.valueOf()返回结果是字符串“null”。而不是null。

3、(String)强转，不推荐使用

（String）是标准的类型转换，将Object类型转为String类型，使用(String)强转时，最好使用instanceof做一个类型检查，以判断是否可以进行强转，否则容易抛出ClassCastException异常。需要注意的是编写的时候，编译器并不会提示有语法错误，所以这个方法要谨慎的使用。
![img.png](img.png)

待完成功能：
用户需要进行实名认证并通过才能进行购买藏品


Copyright：网站： https://nb.sb/ || git开源地址：https://gitee.com/nb-sb/nft
