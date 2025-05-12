package com.example.demo.service;

import com.example.demo.data.Student;
import com.example.demo.data.StudentCourses;
import com.example.demo.domain.StudentDetail;
import com.example.demo.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourses> studentCourses = repository.searchStudentCourses(student.getId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourses);
    return studentDetail;
  }

  public List<Student> searchThirtiesStudentList() {
    List<Student> allStudents = repository.search();
    return allStudents.stream()
        .filter(s -> s.getAge() >= 30 && s.getAge() < 40)
        .toList();
  }

  public List<StudentCourses> getStudentCourseList() {
    return repository.searchStudentCoursesLIst();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      studentCourses.setStudentId(studentDetail.getStudent().getId());
      studentCourses.setCourseStartAt(LocalDateTime.now());
      studentCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourses(studentCourses);
    }
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      repository.updateStudentCourses(studentCourses);
    }
  }
}