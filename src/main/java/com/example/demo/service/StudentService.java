package com.example.demo.service;

import com.example.demo.controller.converter.StudentConverter;
import com.example.demo.data.Student;
import com.example.demo.data.StudentCourses;
import com.example.demo.domain.StudentDetail;
import com.example.demo.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;


  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生の覧検索を行います。 全件検索を行うので、条件指定は行わいません。
   *
   * @return 受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourses> studentCoursesList = repository.searchStudentCoursesLIst();
    return converter.convertStudentDetails(studentList, studentCoursesList);
  }

  /**
   * 受講生検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourses> studentCourses = repository.searchStudentCourses(student.getId());
    return new StudentDetail(student, studentCourses);
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      studentCourses.setStudentId(studentDetail.getStudent().getId());
      studentCourses.setCourseStartAt(LocalDateTime.now());
      studentCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourses(studentCourses);
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      repository.updateStudentCourses(studentCourses);
    }
  }
}