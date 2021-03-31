## for循环字符串拼接（大量）

> ```java
> package com.itheima.test;
> 
> /**
>  * @Author: YaFeiLiu
>  * @Descciption:
>  * @Date:Create in 14:48 2021/3/28
>  * @Modefied By:
>  */
> public class Integer_AutoSetMax {
>     public static void main(String[] args) {
>         /*Integer a = 1000;         有问题
>         Integer b = 1000;
>         System.out.println(a==b);*/
>         //开始时间戳
>         long starttime = System.currentTimeMillis();
> 
> //        String str = "";      //输出时间为18440ms
> //        for (int i = 0; i < 100000; i++) {
> //            str = str + i + " ";
> //
> //        }
> //        System.out.println(str);
> 
>         StringBuilder str = new StringBuilder();    //输出时间为20ms
>         for (int i = 0; i < 100000; i++) {
>             str.append(i);
>             str.append(" ");
>         }
>         System.out.println(str);
> 
>         //结束时间戳
>         long endttime = System.currentTimeMillis();
>         System.out.println("时间为：" + (endttime-starttime) + "毫秒！");
>     }
> }
> ```

