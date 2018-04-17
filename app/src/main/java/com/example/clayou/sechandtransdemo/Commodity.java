package com.example.clayou.sechandtransdemo;

import org.litepal.crud.DataSupport;

/**
 * Created by 10295 on 2018/4/13.
 */

public class Commodity extends DataSupport {

    private String owner;
    private String name;
    private String category;
    private double price;
    private int imageId;
    private String description;

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
