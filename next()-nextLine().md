## Java中next()与nextLine()的区别：

>```java
>Scanner input = new Scanner(System.in);
>
>System.out.println("请输入字符串（next）：");
>String str = input.next();
>System.out.println(str);
>
>System.out.println("请输入字符串（nextLine）：");//曹老板很有钱
>String str1 = input.nextLine();
>System.out.println(str1);
>```

#### 输出：

>请输入字符串（next）：
>老板很有钱
>老板很有钱
>请输入字符串（nextLine）：
>
>/*
>next()方法读取到空白符就结束l；
>nextLine()读取到回车结束也就是“\r”；
>所以没还顺序前测试的时候next()再检测的空格的时候就结束输出了。
>修改顺序后遇到的问题就是因为next()读取到空白符前的数据时结束了，然后把回车“\r”留给了nextLine();
>所以上面nextLine()没有输出,不输出不代表没数据，是接到了空（回车“/r”）的数据。
>*/



this is *text*









