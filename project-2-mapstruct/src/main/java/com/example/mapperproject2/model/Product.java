package com.example.mapperproject2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name; private BigDecimal price; private Integer quantity;
    @Column(length = 1000) private String description; private String imageUrl;
    public Product() { }
    public Product(String name, BigDecimal price, Integer quantity, String description, String imageUrl) { this.name=name; this.price=price; this.quantity=quantity; this.description=description; this.imageUrl=imageUrl; }
    public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getName(){return name;} public void setName(String name){this.name=name;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal price){this.price=price;} public Integer getQuantity(){return quantity;} public void setQuantity(Integer quantity){this.quantity=quantity;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;} public String getImageUrl(){return imageUrl;} public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
}
