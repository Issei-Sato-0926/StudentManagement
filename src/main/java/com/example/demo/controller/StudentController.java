package com.example.demo.controller;

import com.example.demo.controller.converter.StudentConverter;
import com.example.demo.data.Student;
import com.example.demo.data.StudentCourses;
import com.example.demo.domain.StudentDetail;
import com.example.demo.service.StudentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.getStudentCourseList();

    return converter.convertStudentDetails(students, studentCourses);
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourses> getStudentCourseList() {
    return service.getStudentCourseList();
  }

  @GetMapping("/thirtiesStudentList")
  public List<Student> getThirtiesStudentList() {
    return service.searchThirtiesStudentList();
  }
}