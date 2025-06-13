package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private StudentService service;

  @TestConfiguration
  static class TestConfig {

    @Bean
    public StudentService studentService() {
      return mock(StudentService.class);
    }

  }

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索が実行できて空で返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 条件に合致する受講生の検索が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(
            post("/students/search").contentType(MediaType.APPLICATION_JSON).content(
                """
                    {
                        "name" : "佐藤太郎",
                        "kanaName" : "サトウタロウ",
                        "nickname" : "タロウ",
                        "email" : "test@example.com",
                        "area" : "東京都",
                        "minAge" : "20",
                        "maxAge" : "30",
                        "gender" : "男性",
                        "remark" : "",
                        "courseName" : "Javaコース"
                    }
                    """
            ))
        .andExpect(status().isOk());

    verify(service, times(1)).searchByCondition(any());
  }

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                    "student": {
                        "name" : "佐藤太郎",
                        "kanaName" : "サトウタロウ",
                        "nickname" : "タロウ",
                        "email" : "test@example.com",
                        "area" : "東京都",
                        "age" : "25",
                        "gender" : "男性",
                        "remark" : ""
                    },
                    "studentCourseList" : [
                        {
                            "courseName" : "Javaコース"
                        }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                    "student": {
                        "id": "1",
                        "name": "佐藤太郎",
                        "kanaName": "サトウタロウ",
                        "nickname": "タロウ",
                        "email": "test@example.com",
                        "area": "東京都",
                        "age": 25,
                        "gender": "男性",
                        "remark": ""
                    },
                    "studentCourseList": [
                        {
                            "id": "1",
                            "studentId": "1",
                            "courseName": "AWSコース",
                            "courseStartAt": "2025-05-12T13:03:00",
                            "courseEndAt": "2026-05-12T13:03:00"
                        }
                    ],
                    "applicationStatusList": [
                        {
                            "id": "1",
                            "studentCourseId": "1",
                            "status": "本申込"
                        }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("佐藤太郎");
    student.setKanaName("サトウタロウ");
    student.setNickname("タロウ");
    student.setEmail("test@example.com");
    student.setArea("東京都");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生で不正な形式がある場合に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("test");
    student.setName("佐藤太郎");
    student.setKanaName("サトウタロウ");
    student.setNickname("タロウ");
    student.setEmail("test");
    student.setArea("東京都");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(2);
    assertThat(violations).extracting(
            v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
        .containsExactlyInAnyOrder(
            tuple("id", "数字のみ入力するようにしてください。"),
            tuple("email", "正しい形式のメールアドレスを入力してください。")
        );
  }

  @Test
  void 受講生詳細の受講生で必須項目が空の場合に入力チェックに掛かること() {
    Student student = new Student();
    student.setName("");
    student.setKanaName("");
    student.setNickname("");
    student.setEmail("");
    student.setArea("");
    student.setGender("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(6);
    assertThat(violations).extracting(
            v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
        .containsExactlyInAnyOrder(
            tuple("name", "名前を入力してください。"),
            tuple("kanaName", "カナ名を入力してください。"),
            tuple("nickname", "ニックネームを入力してください。"),
            tuple("email", "メールアドレスを入力してください。"),
            tuple("area", "居住地域（例：東京都、神奈川県など）を入力してください。"),
            tuple("gender", "性別（男性、女性、その他）を入力してください。")
        );
  }

  @Test
  void 受講生詳細の受講生コース情報で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("Javaコース");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生コース情報で不正な形式や必須項目に空がある場合に入力チェックに掛かること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("test");
    studentCourse.setStudentId("test");
    studentCourse.setCourseName("");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(3);
    assertThat(violations).extracting(
            v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
        .containsExactlyInAnyOrder(
            tuple("id", "数字のみ入力するようにしてください。"),
            tuple("studentId", "数字のみ入力するようにしてください。"),
            tuple("courseName", "コース名を入力してください。")
        );
  }

  @Test
  void 受講生詳細の申込状況で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setStatus("本申込");

    Set<ConstraintViolation<ApplicationStatus>> violations = validator.validate(applicationStatus);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の申込状況で不正な形式や必須項目に空がある場合に入力チェックに掛かること() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setId("test");
    applicationStatus.setStudentCourseId("test");
    applicationStatus.setStatus("");

    Set<ConstraintViolation<ApplicationStatus>> violations = validator.validate(applicationStatus);

    assertThat(violations.size()).isEqualTo(3);
    assertThat(violations).extracting(
            v -> v.getPropertyPath().toString(), ConstraintViolation::getMessage)
        .containsExactlyInAnyOrder(
            tuple("id", "数字のみ入力するようにしてください。"),
            tuple("studentCourseId", "数字のみ入力するようにしてください。"),
            tuple("status", "申込ステータスを入力してください。")
        );
  }

}