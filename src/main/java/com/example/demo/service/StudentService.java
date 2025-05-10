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

  public List<Student> searchThirtiesStudentList() {
    List<Student> allStudents = repository.search();
    return allStudents.stream()
        .filter(s -> s.getAge() >= 30 && s.getAge() < 40)
        .toList();
  }

  public List<StudentCourses> getStudentCourseList() {
    return repository.searchStudentCourses();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    // TODO: コース情報登録も行う。
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      studentCourses.setStudentId(studentDetail.getStudent().getId());
      studentCourses.setCourseStartAt(LocalDateTime.now());
      studentCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourses(studentCourses);
    }
  }
}