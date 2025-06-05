package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 申込状況を表すデータクラスです。
 */
@Schema(description = "申込状況")
@Data
public class ApplicationStatus {

  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String id;

  /**
   * 紐づく受講生コースID。
   */
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String studentCourseId;

  /**
   * 申込ステータス（仮申込、本申込、受講中、受講終了）。
   */
  @NotBlank(message = "申込ステータスを入力してください。")
  private String status;
}