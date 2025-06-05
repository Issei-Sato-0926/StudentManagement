package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.StudentManagement.data.ApplicationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生詳細を表すドメインモデルクラスです。
 * <p>
 * 対象の{@link Student} 、その受講生が受講している {@link StudentCourse} の一覧、 およびそれに紐づく
 * {@link ApplicationStatus}の一覧を保持します。
 * </p>
 */
@Schema(description = "受講生詳細")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid
  private Student student;

  @Valid
  private List<StudentCourse> studentCourseList;

  @Valid
  private List<ApplicationStatus> applicationStatusList;
}