package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    private String categoryId;

    @Column(name = "category_title")
    private String title;

    @Column(name = "category_description")
    private String description;

    @Column(name = "cover_image")
    private String coverImage;

}
