package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 受講生コース情報を表すデータクラスです。
 */
@Schema(description = "受講生コース情報")
@Data
public class StudentCourse {

  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String id;

  /**
   * 紐づく受講生のID（数値のみ）。
   */
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String studentId;

  @NotBlank(message = "コース名を入力してください。")
  private String courseName;

  /**
   * 受講開始日。
   */
  private LocalDateTime courseStartAt;

  /**
   * 受講終了予定日。
   */
  private LocalDateTime courseEndAt;
}