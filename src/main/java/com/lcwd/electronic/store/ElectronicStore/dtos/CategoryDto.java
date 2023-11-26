package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    private String title;

    private String description;

    private String coverImage;


}
