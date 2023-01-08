package com.loam.stoody.model.product.course;

import com.loam.stoody.model.communication.video.Video;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(indexes = {
        @Index(name = "lectureIndex_id", columnList = "id", unique = true)
})
public class CourseLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String article;
    private String description;
    @OneToMany(
            mappedBy = "courseCurriculumDetails",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Video> videos;
    private String imageUrl;

    @ManyToOne(targetEntity = CourseSection.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", referencedColumnName = "id")
    CourseSection courseSection;


}
