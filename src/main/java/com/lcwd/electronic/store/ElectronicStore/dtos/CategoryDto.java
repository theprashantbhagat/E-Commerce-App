package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.validation.ImageNameValid;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank
    @Size(min =4 ,message = "Title must contain atleast 4 characters")
    private String title;

    @NotBlank
    @Size(min = 10,message = "Description should contain minimum of 10 characters")
    private String description;

    @ImageNameValid
    private String coverImage;


}
