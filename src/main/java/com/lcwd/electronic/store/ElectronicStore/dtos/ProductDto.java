package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String productId;

    @NotEmpty
    @Size(min = 4,message = "Title is required")
    private String title;

    @NotEmpty
    @Size(min=10,message = "Description is required")
    private String description;

    @NotEmpty
    private Double price;

    @NotEmpty
    private Integer quantity;

    @NotEmpty
    private Date addedDate;

    @NotEmpty
    private Boolean live;

    @NotEmpty
    private Boolean stock;



}
