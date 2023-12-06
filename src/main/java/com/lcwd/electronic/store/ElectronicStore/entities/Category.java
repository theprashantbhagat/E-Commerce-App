package com.lcwd.electronic.store.ElectronicStore.entities;

import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Product> products=new ArrayList<>();

}
