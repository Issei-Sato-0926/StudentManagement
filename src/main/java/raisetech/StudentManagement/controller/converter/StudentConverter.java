package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生、受講生コース情報、および申込状況を相互に変換・統合するコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生リストに対し、各受講生に紐づく受講生コース情報およびそのコースに紐づく申込状況を関連付けて、 {@link StudentDetail}のリストを生成します。
   * <p>
   * 受講生と受講生コース情報の関係は１対多、受講生コース情報と申込状況の関係は１対１です。
   * </p>
   *
   * @param studentList           受講生の一覧
   * @param studentCourseList     受講生コース情報の一覧
   * @param applicationStatusList 申込状況の一覧
   * @return {@link StudentDetail}のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList, List<ApplicationStatus> applicationStatusList) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentCourseList(convertStudentCourseList);

      Set<String> studentCourseIdList = convertStudentCourseList.stream()
          .map(StudentCourse::getId)
          .collect(Collectors.toSet());
      List<ApplicationStatus> convertApplicationStatusList = applicationStatusList.stream()
          .filter(
              applicationStatus -> studentCourseIdList.contains(
                  applicationStatus.getStudentCourseId()))
          .collect(Collectors.toList());
      studentDetail.setApplicationStatusList(convertApplicationStatusList);

      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }
}