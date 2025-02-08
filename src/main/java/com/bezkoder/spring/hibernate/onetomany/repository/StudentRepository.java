package com.bezkoder.spring.hibernate.onetomany.repository;

import com.bezkoder.spring.hibernate.onetomany.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
