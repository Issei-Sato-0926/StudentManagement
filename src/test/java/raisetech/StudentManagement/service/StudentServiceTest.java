package raisetech.StudentManagement.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.StudentNotFoundException;
import raisetech.StudentManagement.model.StudentSearchCondition;
import raisetech.StudentManagement.repository.StudentRepository;

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
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<ApplicationStatus> applicationStatusList = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(repository.searchApplicationStatusList()).thenReturn(applicationStatusList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchApplicationStatusList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);
  }

  @Test
  void 受講生詳細の検索_リポジトリの処理が適切に呼び出せていること() {
    String id = "999";
    Student student = new Student();
    student.setId(id);

    String courseId = "999";
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(courseId);
    studentCourse.setStudentId(student.getId());
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    Set<String> studentCourseIds = Set.of(studentCourse.getId());
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStudentCourseId(studentCourse.getId());
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(id)).thenReturn(studentCourseList);
    when(repository.searchApplicationStatus(studentCourseIds)).thenReturn(applicationStatusList);

    StudentDetail expected = new StudentDetail(student, studentCourseList, applicationStatusList);
    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(student.getId());
    verify(repository, times(1)).searchApplicationStatus(studentCourseIds);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 受講生詳細の検索_受講生が存在しない場合に例外がスローされること() {
    String id = "999";
    when(repository.searchStudent(id)).thenReturn(null);

    StudentNotFoundException thrown = assertThrows(StudentNotFoundException.class, () -> {
      sut.searchStudent(id);
    });

    assertThat(thrown.getMessage()).contains("(ID：" + id + ")");
  }

  @Test
  void 受講生詳細の条件指定検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    List<Student> students = List.of(new Student());
    List<StudentCourse> studentCourses = new ArrayList<>();
    List<ApplicationStatus> applicationStatuses = new ArrayList<>();

    when(repository.searchByCondition(condition)).thenReturn(students);
    when(repository.searchStudentCourseList()).thenReturn(studentCourses);
    when(repository.searchApplicationStatusList()).thenReturn(applicationStatuses);

    sut.searchByCondition(condition);

    verify(repository, times(1)).searchByCondition(condition);
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchApplicationStatusList();
    verify(converter, times(1)).convertStudentDetails(students, studentCourses,
        applicationStatuses);
  }

  @Test
  void 受講生詳細の条件指定検索_条件に合致する受講生がいない場合に例外がスローされること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    when(repository.searchByCondition(condition)).thenReturn(new ArrayList<>());

    StudentNotFoundException thrown = assertThrows(StudentNotFoundException.class, () -> {
      sut.searchByCondition(condition);
    });

    assertThat(thrown.getMessage()).contains("(検索条件：" + condition + ")");
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList, List.of());

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
    ArgumentCaptor<ApplicationStatus> captor = ArgumentCaptor.forClass(ApplicationStatus.class);
    verify(repository, times(1)).registerApplicationStatus(captor.capture());
  }

  @Test
  void 受講生詳細の登録_受講生コース情報の初期化処理が行われること() {
    String id = "999";
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertThat(studentCourse.getStudentId()).isEqualTo(id);
    assertThat(studentCourse.getCourseStartAt().getHour()).isEqualTo(LocalDateTime.now().getHour());
    assertThat(studentCourse.getCourseEndAt().getYear()).isEqualTo(
        LocalDateTime.now().plusYears(1).getYear());
  }

  @Test
  void 受講生詳細の登録_申込状況の初期化処理が行われること() {
    String id = "999";
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(id);
    ApplicationStatus applicationStatus = new ApplicationStatus();

    sut.initApplicationStatus(applicationStatus, studentCourse.getId());

    assertThat(applicationStatus.getStudentCourseId()).isEqualTo(id);
    assertThat(applicationStatus.getStatus()).isEqualTo("仮申込");
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    ApplicationStatus applicationStatus = new ApplicationStatus();
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList,
        applicationStatusList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
    verify(repository, times(1)).updateApplicationStatus(applicationStatus);
  }

}