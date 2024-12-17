package com.maya.kliksoftapp1;

import java.util.Arrays;
import java.util.Dictionary;

public class Product {
    public String productName;
    public String productDesc;
    public int productPrice;
    public int id;

    Product(String name, String desc, int price, int id){
        super();
        this.productName = name;
        this.productDesc = desc;
        this.productPrice = price;
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getProductName() {
        return productName;
    }
    public String getProductDesc() {
        return productDesc;
    }
    public int getProductPrice() {
        return productPrice;
    }

}
