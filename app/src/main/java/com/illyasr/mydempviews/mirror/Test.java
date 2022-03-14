package com.illyasr.mydempviews.mirror;

public   class Test {
    private int age = 10;
    private String name = "Von";
    private int testint = 2;

    public Test(int age) {
        this.age = age;
    }

    public Test(int age, String name) {
        this.age = age;
        this.name = name;
        System.out.println("hello "+name + " i am "+age);
    }

    private Test(String name) {
        this.name = name;
        System.out.println("My name is "+name + "");
    }

    public Test() {
    }

    @Override
    public String toString() {
        return "Test{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", testint=" + testint +
                '}';
    }
}
