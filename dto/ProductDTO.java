package com.wsystems.residentstore.dto;


import com.wsystems.residentstore.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {

    private Long id;

    @Size(min=3, max=80, message = "Nome com 3 a 80 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String name;

    @Size(min = 10, message = "Descrição deve ter no mínimo 10 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String description;

    @Positive(message = "O preço deve ser positivo")
    private Double price;
    private String imgUrl;


    public ProductDTO(Long id, String description, String name, Double price, String imgUrl) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO() {
    }

    public ProductDTO(Product product) {
        id = product.getId();
        description = product.getDescription();
        name = product.getName();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
