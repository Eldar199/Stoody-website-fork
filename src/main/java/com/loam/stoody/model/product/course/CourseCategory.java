/*
@fileName:  Category

@aka:       Category Model

@purpose:   Contains the data (that's either transient or non-transient) of a category.

@author:    OrkhanGG

@created:   01.12.2022
*/

package com.loam.stoody.model.product.course;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private CourseCategory parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_categories_category_id")
    private List<CourseCategory> subCategories;
}