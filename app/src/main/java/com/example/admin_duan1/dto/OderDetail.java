package com.example.admin_duan1.dto;

public class OderDetail {
    private String Image, ProductId, ProductName, QuanTity, Price, Discount;

    public OderDetail() {
    }

    public OderDetail(String image, String productId, String productName, String quanTity, String price, String discount) {
        Image = image;
        ProductId = productId;
        ProductName = productName;
        QuanTity = quanTity;
        Price = price;
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuanTity() {
        return QuanTity;
    }

    public void setQuanTity(String quanTity) {
        QuanTity = quanTity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
