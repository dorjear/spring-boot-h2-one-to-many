package com.bezkoder.spring.hibernate.onetomany.repository;

import com.bezkoder.spring.hibernate.onetomany.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}