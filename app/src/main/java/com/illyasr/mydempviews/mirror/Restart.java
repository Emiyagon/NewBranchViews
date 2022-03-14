package com.illyasr.mydempviews.mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 *  反射学习类
 */
public class Restart {

    public void main(String[] args) {
//        Person p1 = new Person();
        Test p1 = new Test();
        Class c4 = p1.getClass();
        Constructor[] constructors ;
        Constructor  constructor = null;

       /* constructors = c4.getDeclaredConstructors();// 获得所有构造函数包括private
        for (int i = 0; i < constructors.length; i++) {
            System.out.print(Modifier.toString(constructors[i].getModifiers()) + "参数-->");
            Class[] parametertypes = constructors[i].getParameterTypes();
            for (int j = 0; j < parametertypes.length; j++) {
                System.out.print(parametertypes[j].getName() + "/");
            }
            System.out.println("");
        }
        */
        try {
            // 调用无参构造方法,必须用try/catch包住,因为你不确定这个方法是不是一定有
//            System.out.print(Modifier.toString(constructor.getModifiers()) + "-->");
            // 调用有参数的构造方法,这里需要知道这个方法传入的是哪些参数
           /* Class[] p = {String.class};
            constructor = c4.getDeclaredConstructor(p);
            constructor.setAccessible(true);//设置这个之后就可以调用无参构造方法了
            constructor.newInstance( "Enizibern");*/
            Class[] p = {int.class,String.class };
            constructor = c4.getDeclaredConstructor(p);
            constructor.setAccessible(true);//设置这个之后就可以调用无参构造方法了
//            Person o =(Person)constructor.newInstance( "ASDF111212","01010101010","160");//这个方法返回的是object
            Test o =(Test)constructor.newInstance( 10,"Aliye");//这个方法返回的是object,可以强转为当前修改的

            Field field = c4.getDeclaredField("name");
            field.setAccessible(true);
            field.set(o,"假人");
            Field field1 = c4.getDeclaredField("age");
            field1.setAccessible(true);
            field1.set(o,18);
            System.out.println(field.get(o).toString());
            System.out.println(o.toString());

            /*
            //反射得到lintenerInfo对象
             Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getListenerInfo.setAccessible(true);
            Object listenerInfo = getListenerInfo.invoke(mDrawerLayout);
            //得到原始的listener
            Class listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field mOnClickListener = listenerInfoClz.getDeclaredField("mOnClickListener");
            View.OnClickListener orangeOnClickListener = (View.OnClickListener) mOnClickListener.get(listenerInfo);
            //用代理替换掉原始的,这里就是用自定义的去替换掉原始的,这样达到了无缝插入
             //************
            */

        } catch ( Exception e) {
            e.printStackTrace();
            System.out.print("小兄弟,出错了哦~");
        }


    }



}
