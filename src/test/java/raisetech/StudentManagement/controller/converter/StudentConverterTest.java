package raisetech.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生のリストと受講生コース情報のリストと申込状況のリストを渡して_受講生詳細のリストが作成できること() {
    Student student = createStudent();
    StudentCourse studentCourse = createStudentCourse("1");
    ApplicationStatus applicationStatus = createApplicationStatus("1");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentCourseList);
    assertThat(actual.get(0).getApplicationStatusList()).isEqualTo(applicationStatusList);
  }

  @Test
  void 受講生のリストと受講生コース情報のリストと申込状況のリストを渡した時に_紐づかない受講生コース情報と申込状況は除外されること() {
    Student student = createStudent();
    StudentCourse studentCourse = createStudentCourse("2");
    ApplicationStatus applicationStatus = createApplicationStatus("2");

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
    assertThat(actual.get(0).getApplicationStatusList()).isEmpty();
  }

  private static Student createStudent() {
    Student student = new Student();
    student.setId("1");
    student.setName("佐藤太郎");
    student.setKanaName("サトウタロウ");
    student.setNickname("タロウ");
    student.setEmail("test@example.com");
    student.setArea("東京都");
    student.setAge(25);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }

  private static StudentCourse createStudentCourse(String studentId) {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName("Javaコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));
    return studentCourse;
  }

  private static ApplicationStatus createApplicationStatus(String studentCourseId) {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setId("1");
    applicationStatus.setStudentCourseId(studentCourseId);
    applicationStatus.setStatus("仮申込");
    return applicationStatus;
  }
}