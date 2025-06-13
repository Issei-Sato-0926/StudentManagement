package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.model.StudentSearchCondition;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生の検索が行えること() {
    Student actual = sut.searchStudent("1");
    assertThat(actual.getName()).isEqualTo("田中 太郎");
  }

  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(13);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 申込状況の全件検索が行えること() {
    List<ApplicationStatus> actual = sut.searchApplicationStatusList();
    assertThat(actual.size()).isEqualTo(13);
  }

  @Test
  void 受講生コース情報IDに紐づく申込状況の検索が行えること() {
    Set<String> studentCourseIds = Set.of("1", "2");
    List<ApplicationStatus> actual = sut.searchApplicationStatus(studentCourseIds);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 条件に合致する受講生の検索が行えること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setName("佐藤");
    List<Student> actual = sut.searchByCondition(condition);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("佐藤一誠");
    student.setKanaName("サトウイッセイ");
    student.setNickname("イッセイ");
    student.setEmail("test@example.com");
    student.setArea("奈良");
    student.setAge(27);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(11);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("AWSコース");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    sut.registerStudentCourse(studentCourse);
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(14);
  }

  @Test
  void 申込状況の登録が行えること() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStudentCourseId("1");
    applicationStatus.setStatus("仮申込");

    sut.registerApplicationStatus(applicationStatus);
    List<ApplicationStatus> actual = sut.searchApplicationStatusList();
    assertThat(actual.size()).isEqualTo(14);
  }

  @Test
  void 受講生の更新が行えること() {
    Student student = sut.searchStudent("1");

    student.setNickname("焼肉");
    sut.updateStudent(student);

    Student actual = sut.searchStudent("1");
    assertThat(actual.getNickname()).isEqualTo("焼肉");
  }

  @Test
  void 受講生コース情報の更新が行えること() {
    List<StudentCourse> studentCourseList = sut.searchStudentCourse("1");
    StudentCourse studentCourse = studentCourseList.get(0);

    studentCourse.setCourseName("AWSコース");
    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse("1");
    assertThat(actual.get(0).getCourseName()).isEqualTo("AWSコース");
  }

  @Test
  void 申込状況の更新が行えること() {
    List<StudentCourse> studentCourseList = sut.searchStudentCourse("1");
    StudentCourse studentCourse = studentCourseList.get(0);
    Set<String> studentCourseIds = Set.of(studentCourse.getId());
    List<ApplicationStatus> applicationStatusList = sut.searchApplicationStatus(studentCourseIds);
    ApplicationStatus applicationStatus = applicationStatusList.get(0);

    applicationStatus.setStatus("本申込");
    sut.updateApplicationStatus(applicationStatus);

    List<ApplicationStatus> actual = sut.searchApplicationStatus(studentCourseIds);
    assertThat(actual.get(0).getStatus()).isEqualTo("本申込");
  }
}