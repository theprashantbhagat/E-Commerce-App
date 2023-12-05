package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.validation.ImageNameValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "price required !!")
    private Double price;

    @NotNull
    private Double discountedPrice;

    @NotNull
    private Integer quantity;

    private Date addedDate;

    @NotNull
    private Boolean live;

    @NotNull(message = "Not in stock ")
    private Boolean stock;

    @ImageNameValid
    private String imageName;

}
