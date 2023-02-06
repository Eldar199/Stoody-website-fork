package com.loam.stoody.model.product.course.approved;

import com.loam.stoody.model.product.course.core.LectureCore;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(indexes = {@Index(name = "lectureIndex_id", columnList = "id", unique = true)})
public class ApprovedCourseLecture extends LectureCore {
}