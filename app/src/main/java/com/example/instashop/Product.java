package com.example.instashop;

public class Product
{
    public boolean selected;
    private int ID;
    private String Discount, Name, Image, Tax, Cost, Item_id, Quantity, ProductID;

    public Product()
    {

    }

    public Product(int id, String item_id, String name, String tax, String cost, String image, String discount, String quantity)
    {
        this.ID = id;
        Item_id = item_id;
        Name = name;
        Tax = tax;
        Cost = cost;
        Image = image;
        Discount = discount;
        Quantity = quantity;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getImage()
    {
        return Image;
    }

    public void setImage(String image)
    {
        Image = image;
    }

    public String getTax()
    {
        return Tax;
    }

    public void setTax(String tax)
    {
        Tax = tax;
    }

    public String getCost()
    {
        return Cost;
    }

    public void setCost(String cost)
    {
        Cost = cost;
    }

    public String getItem_id()
    {
        return Item_id;
    }

    public void setItem_id(String item_id)
    {
        Item_id = item_id;
    }

    public String getDiscount()
    {
        return Discount;
    }

    public void setDiscount(String discount)
    {
        Discount = discount;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }
}
