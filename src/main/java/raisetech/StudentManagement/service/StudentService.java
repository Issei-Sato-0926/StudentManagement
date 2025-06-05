package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.StudentNotFoundException;
import raisetech.StudentManagement.model.StudentSearchCondition;
import raisetech.StudentManagement.repository.StudentRepository;

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
   * 受講生詳細の一覧検索を行います。 全件検索を行うので、条件指定は行わいません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<ApplicationStatus> applicationStatusList = repository.searchApplicationStatusList();
    return converter.convertStudentDetails(studentList, studentCourseList, applicationStatusList);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(String id) {

    Student student = repository.searchStudent(id);

    if (student == null) {
      throw new StudentNotFoundException("(ID：" + id + ")");
    }

    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());

    Set<String> studentCourseIds = studentCourse.stream()
        .map(StudentCourse::getId)
        .collect(Collectors.toSet());
    List<ApplicationStatus> applicationStatus = repository.searchApplicationStatus(
        studentCourseIds);

    return new StudentDetail(student, studentCourse, applicationStatus);
  }

  /**
   * 指定された検索条件に合致する受講生詳細を取得します。
   *
   * @param condition 検索条件
   * @return 検索条件に合致する受講生詳細のリスト
   */
  public List<StudentDetail> searchByCondition(StudentSearchCondition condition) {
    List<Student> students = repository.searchByCondition(condition);
    List<StudentCourse> studentCourses = repository.searchStudentCourseList();
    List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusList();

    if (students.isEmpty()) {
      throw new StudentNotFoundException("(検索条件：" + condition + ")");
    }

    return converter.convertStudentDetails(students, studentCourses, applicationStatuses);
  }

  /**
   * 受講生詳細の登録を行います。
   * <p>
   * 受講生・受講生コース情報・申込状況を個別に登録します。 受講生コース情報には、受講生情報を紐づける値・コース開始日・コース終了日を設定します。
   * 申込状況には、受講生コース情報を紐づける値・申込ステータスの初期値を設定します。
   * </p>
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    List<ApplicationStatus> applicationStatuses = new ArrayList<>();

    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      initStudentsCourse(studentCourse, student.getId());
      repository.registerStudentCourse(studentCourse);

      ApplicationStatus applicationStatus = new ApplicationStatus();
      initApplicationStatus(applicationStatus, studentCourse.getId());
      repository.registerApplicationStatus(applicationStatus);
      applicationStatuses.add(applicationStatus);
    });
    studentDetail.setApplicationStatusList(applicationStatuses);

    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定します。
   *
   * @param studentCourse 受講生コース情報
   * @param id            受講生ID
   */
  void initStudentsCourse(StudentCourse studentCourse, String id) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(id);
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusYears(1));
  }

  /**
   * 申込状況を登録する際の初期情報を設定します。
   *
   * @param applicationStatus 申込状況
   * @param id                受講生コースID
   */
  void initApplicationStatus(ApplicationStatus applicationStatus, String id) {
    applicationStatus.setStudentCourseId(id);
    applicationStatus.setStatus("仮申込");
  }

  /**
   * 受講生詳細の更新を行います。 対象は受講生・受講生コース情報・申込状況の各情報です。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
    studentDetail.getApplicationStatusList()
        .forEach(applicationStatus -> repository.updateApplicationStatus(applicationStatus));
  }

}