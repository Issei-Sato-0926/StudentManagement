package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.controller.converter.StudentConverter;
import com.example.demo.data.Student;
import com.example.demo.data.StudentCourse;
import com.example.demo.domain.StudentDetail;
import com.example.demo.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void searchStudentList_がリポジトリとコンバーターを正しく呼び出すこと() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseLIst()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseLIst();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void searchStudent_がIDに基づき_リポジトリを呼び出して_正しい受講生詳細を返すこと() {
    Student student = new Student();
    String testId = "999";
    student.setId(testId);
    List<StudentCourse> studentCourse = new ArrayList<>();
    when(repository.searchStudent(testId)).thenReturn(student);
    when(repository.searchStudentCourse(testId)).thenReturn(studentCourse);

    StudentDetail result = sut.searchStudent(testId);

    verify(repository, times(1)).searchStudent(testId);
    verify(repository, times(1)).searchStudentCourse(testId);
    assertNotNull(result);
    assertEquals(student, result.getStudent());
    assertEquals(studentCourse, result.getStudentCourseList());
  }

  @Test
  void registerStudent_が受講生と受講生コース情報を登録して_同じ受講生詳細を返すこと() {
    Student student = new Student();
    String testId = "999";
    student.setId(testId);
    StudentCourse studentCourse1 = new StudentCourse();
    StudentCourse studentCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);

    StudentDetail result = sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse1);
    verify(repository, times(1)).registerStudentCourse(studentCourse2);

    assertEquals(student, result.getStudent());
    assertEquals(studentDetail, result);
  }

  @Test
  void initStudentsCourse_が受講生IDと受講開始日及び受講終了日を正しく設定すること() {
    Student student = new Student();
    String testId = "999";
    student.setId(testId);
    StudentCourse studentCourse = new StudentCourse();
    LocalDateTime before = LocalDateTime.now();

    sut.initStudentsCourse(studentCourse, student);
    LocalDateTime after = LocalDateTime.now();

    assertEquals(testId, studentCourse.getStudentId());
    assertTrue(!studentCourse.getCourseStartAt().isBefore(before));
    assertTrue(!studentCourse.getCourseStartAt().isAfter(after));
    assertEquals(studentCourse.getCourseStartAt().plusYears(1), studentCourse.getCourseEndAt());
  }

  @Test
  void updateStudent_が受講生と受講生コース情報を更新すること() {
    Student student = new Student();
    String testId = "999";
    student.setId(testId);
    StudentCourse studentCourse1 = new StudentCourse();
    StudentCourse studentCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse1);
    verify(repository, times(1)).updateStudentCourse(studentCourse2);
  }
}