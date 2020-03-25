package com.example.mybang;

public class Card {
    private String name; //中文
    //private String type; //用于给normal判断是什么牌用什么函数（冇用）
    private int key; //牌型编号

    public Card(String name,String type,int key){
        this.name = name;
        //this.type = type;
        this.key = key;
    }

    public String getName(){
        return this.name;
    }
    /*public String getType(){
        return this.type;
    }*/
    public int getKey(){
        return this.key;
    }
}
