package com.bezkoder.spring.hibernate.onetomany.controller;

import com.bezkoder.spring.hibernate.onetomany.model.Student;
import com.bezkoder.spring.hibernate.onetomany.model.Teacher;
import com.bezkoder.spring.hibernate.onetomany.repository.StudentRepository;
import com.bezkoder.spring.hibernate.onetomany.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.Optional;

@RestController
@RequestMapping("/school")
public class SchoolController {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public SchoolController(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    //create teacher
    @PostMapping("/teacher")
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher _teacher = teacherRepository.save(teacher);
        return new ResponseEntity<>(_teacher, HttpStatus.CREATED);
    }

    //create student
    @PostMapping("/student")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student _student = studentRepository.save(student);
        return new ResponseEntity<>(_student, HttpStatus.CREATED);
    }

    //add student to a teacher
    @PostMapping("/teacher/{teacherId}/addStudent")
    public ResponseEntity<Teacher> addStudent(@PathVariable(value = "teacherId") Long teacherId, @RequestBody Student addStudent) {
        //TODO
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
        if (!teacherOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Teacher teacher = teacherOpt.get();

        // Check if the student already exists in the teacher's list
        boolean isAlreadyAssociated = teacher.getStudents().stream()
                .anyMatch(student -> student.getId() == (addStudent.getId()));

        if (!isAlreadyAssociated) {
            // Ensure the student exists in the database

            Optional<Student> existingStudentOpt = studentRepository.findById(addStudent.getId());
            Student student = existingStudentOpt.orElseGet(() -> studentRepository.save(addStudent));

            // Add the relationship
            teacher.getStudents().add(student);
            student.getTeachers().add(teacher);

            // Save both entities
            teacherRepository.save(teacher);
        }

        return new ResponseEntity<>(teacher, HttpStatus.CREATED);
    }

    //get students of a teacher
    @GetMapping("/teacher/{teacherId}/students")
    public ResponseEntity<Set<Student>> getStudentsOfATeacher(@PathVariable(value = "teacherId") Long teacherId) {
        //TODO
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
        if (!teacherOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Teacher teacher = teacherOpt.get();
        return new ResponseEntity<>(teacher.getStudents(), HttpStatus.OK);
    }

    // get teachers of a student
    @GetMapping("/student/{studentId}/teachers")
    public ResponseEntity<Set<Teacher>> getTeachersOfAStudent(@PathVariable(value = "studentId") Long studentId) {
        //TODO
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Student student = studentOpt.get();
        return new ResponseEntity<>(student.getTeachers(), HttpStatus.OK);
    }
}
