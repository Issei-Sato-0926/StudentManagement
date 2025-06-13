package raisetech.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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
  void 受講生リストと受講生コース情報リストと申込状況リストを渡して_受講生詳細リストが作成できること() {
    Student student = createStudent("1", "佐藤太郎");
    StudentCourse studentCourse = createStudentCourse("1", "1", "Javaコース");
    ApplicationStatus applicationStatus = createApplicationStatus("1", "1", "仮申込");

    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse),
        List.of(applicationStatus));
    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    List<StudentDetail> expected = List.of(studentDetail);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual).isEqualTo(expected);

    StudentDetail actualStudentDetail = actual.get(0);
    assertThat(actualStudentDetail.getStudent().getName()).isEqualTo("佐藤太郎");
    assertThat(actualStudentDetail.getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "Javaコース");
    assertThat(actualStudentDetail.getApplicationStatusList().get(0).getStatus()).isEqualTo(
        "仮申込");
  }

  @Test
  void 受講生リストと受講生コース情報リストと申込状況リストを渡した場合_紐づかない受講生コース情報と申込状況は除外されること() {
    Student student = createStudent("1", "佐藤太郎");
    StudentCourse studentCourse = createStudentCourse("1", "2", "Javaコース");
    ApplicationStatus applicationStatus = createApplicationStatus("1", "2", "仮申込");

    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>(), new ArrayList<>());
    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    List<StudentDetail> expected = List.of(studentDetail);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual).isEqualTo(expected);

    StudentDetail actualStudentDetail = actual.get(0);
    assertThat(actualStudentDetail.getStudent().getName()).isEqualTo("佐藤太郎");
    assertThat(actualStudentDetail.getStudentCourseList()).isEmpty();
    assertThat(actualStudentDetail.getApplicationStatusList()).isEmpty();
  }

  @Test
  void 複数の受講生が存在する場合_それぞれの受講生詳細リストが作成されること() {
    Student student1 = createStudent("1", "佐藤太郎");
    StudentCourse studentCourse1 = createStudentCourse("1", "1", "Javaコース");
    ApplicationStatus applicationStatus1 = createApplicationStatus("1", "1", "仮申込");

    Student student2 = createStudent("2", "鈴木大輔");
    StudentCourse studentCourse2 = createStudentCourse("2", "2", "AWSコース");
    ApplicationStatus applicationStatus2 = createApplicationStatus("2", "2", "本申込");

    StudentDetail studentDetail1 = new StudentDetail(student1, List.of(studentCourse1),
        List.of(applicationStatus1));
    StudentDetail studentDetail2 = new StudentDetail(student2, List.of(studentCourse2),
        List.of(applicationStatus2));
    List<Student> studentList = List.of(student1, student2);
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus1, applicationStatus2);

    List<StudentDetail> expected = List.of(studentDetail1, studentDetail2);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).isEqualTo(expected);

    StudentDetail actualStudentDetail1 = actual.get(0);
    assertThat(actualStudentDetail1.getStudent().getName()).isEqualTo("佐藤太郎");
    assertThat(actualStudentDetail1.getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "Javaコース");
    assertThat(actualStudentDetail1.getApplicationStatusList().get(0).getStatus()).isEqualTo(
        "仮申込");

    StudentDetail actualStudentDetail2 = actual.get(1);
    assertThat(actualStudentDetail2.getStudent().getName()).isEqualTo("鈴木大輔");
    assertThat(actualStudentDetail2.getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "AWSコース");
    assertThat(actualStudentDetail2.getApplicationStatusList().get(0).getStatus()).isEqualTo(
        "本申込");
  }

  @Test
  void 受講生が複数のコースを受講している場合_すべての受講生コース情報および申込状況が紐づけられること() {
    Student student = createStudent("1", "佐藤太郎");
    StudentCourse studentCourse1 = createStudentCourse("1", "1", "Javaコース");
    StudentCourse studentCourse2 = createStudentCourse("2", "1", "AWSコース");
    ApplicationStatus applicationStatus1 = createApplicationStatus("1", "1", "仮申込");
    ApplicationStatus applicationStatus2 = createApplicationStatus("2", "2", "本申込");

    StudentDetail studentDetail = new StudentDetail(student,
        List.of(studentCourse1, studentCourse2), List.of(applicationStatus1, applicationStatus2));
    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus1, applicationStatus2);

    List<StudentDetail> expected = List.of(studentDetail);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual).isEqualTo(expected);

    StudentDetail actualStudentDetail = actual.get(0);
    assertThat(actualStudentDetail.getStudent()).isEqualTo(student);
    assertThat(actualStudentDetail.getStudent().getName()).isEqualTo("佐藤太郎");
    assertThat(actualStudentDetail.getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "Javaコース");
    assertThat(actualStudentDetail.getStudentCourseList().get(1).getCourseName()).isEqualTo(
        "AWSコース");
    assertThat(actualStudentDetail.getApplicationStatusList().get(0).getStatus()).isEqualTo(
        "仮申込");
    assertThat(actualStudentDetail.getApplicationStatusList().get(1).getStatus()).isEqualTo(
        "本申込");
  }

  @Test
  void 受講生リストが空の場合_空のリストが返されること() {
    StudentCourse studentCourse = createStudentCourse("1", "1", "Javaコース");
    ApplicationStatus applicationStatus = createApplicationStatus("1", "1", "仮申込");

    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生コース情報リストが空の場合_受講生コース情報および申込状況が空のリストが返されること() {
    Student student = createStudent("1", "佐藤太郎");
    ApplicationStatus applicationStatus = createApplicationStatus("1", "1", "仮申込");
    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>(), new ArrayList<>());

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<ApplicationStatus> applicationStatusList = List.of(applicationStatus);

    List<StudentDetail> expected = List.of(studentDetail);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual).isEqualTo(expected);

    StudentDetail actualStudentDetail = actual.get(0);
    assertThat(actualStudentDetail.getStudent().getName()).isEqualTo("佐藤太郎");
    assertThat(actualStudentDetail.getStudentCourseList()).isEmpty();
    assertThat(actualStudentDetail.getApplicationStatusList()).isEmpty();
  }

  @Test
  void 申込状況リストが空の場合は受講生コース情報の紐付けのみ行われ_申込状況が空のリストが返されること() {
    Student student = createStudent("1", "佐藤太郎");
    StudentCourse studentCourse = createStudentCourse("1", "1", "Javaコース");
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse),
        new ArrayList<>());

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<ApplicationStatus> applicationStatusList = new ArrayList<>();

    List<StudentDetail> expected = List.of(studentDetail);
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList,
        applicationStatusList);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual).isEqualTo(expected);

    StudentDetail actualStudentDetail = actual.get(0);
    assertThat(actualStudentDetail.getStudent().getName()).isEqualTo("佐藤太郎");
    assertThat(actualStudentDetail.getStudentCourseList().get(0).getCourseName()).isEqualTo(
        "Javaコース");
    assertThat(actualStudentDetail.getApplicationStatusList()).isEmpty();
  }

  private static Student createStudent(String id, String name) {
    Student student = new Student();
    student.setId(id);
    student.setName(name);
    return student;
  }

  private static StudentCourse createStudentCourse(String id, String studentId, String courseName) {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(id);
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName(courseName);
    return studentCourse;
  }

  private static ApplicationStatus createApplicationStatus(String id, String studentCourseId,
      String status) {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setId(id);
    applicationStatus.setStudentCourseId(studentCourseId);
    applicationStatus.setStatus(status);
    return applicationStatus;
  }
}