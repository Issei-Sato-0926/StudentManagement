package raisetech.StudentManagement.repository;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.model.StudentSearchCondition;

/**
 * 受講生・受講生コース情報・申込状況に関するデータアクセスを担当する Repository インターフェースです。 MyBatis を使用して、データベースとの連携を行います。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生コース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentId);

  /**
   * 申込状況の全件検索を行います。
   *
   * @return 申込状況（全件）
   */
  List<ApplicationStatus> searchApplicationStatusList();

  /**
   * 受講生コース情報IDに紐づく申込状況を検索します。
   *
   * @param studentCourseIds 受講生コース情報ID
   * @return 受講生コース情報IDに紐づく申込状況
   */
  List<ApplicationStatus> searchApplicationStatus(Set<String> studentCourseIds);

  /**
   * 指定された条件に合致する受講生を検索します。
   *
   * @param condition 検索条件
   * @return 条件に合致する受講生
   */
  List<Student> searchByCondition(StudentSearchCondition condition);

  /**
   * 受講生を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 申込状況を新規登録します。 IDに関しては自動採番を行う。
   *
   * @param applicationStatus 申込状況
   */
  void registerApplicationStatus(ApplicationStatus applicationStatus);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 申込状況を更新します。
   *
   * @param applicationStatus 申込状況
   */
  void updateApplicationStatus(ApplicationStatus applicationStatus);

}