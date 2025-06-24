package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.model.StudentSearchCondition;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わいません。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(summary = "受講生詳細一覧検索", description = "受講生詳細の一覧を検索します。")
  @ApiResponse(responseCode = "200", description = "正常に取得")
  @GetMapping("/students")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @Operation(summary = "受講生詳細検索", description = "受講生詳細を検索します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "正常に取得"),
      @ApiResponse(responseCode = "400", description = "不正なID"),
      @ApiResponse(responseCode = "404", description = "受講生が存在しない")
  })
  @GetMapping("/students/{id}")
  public StudentDetail getStudent(@PathVariable long id) {
    return service.searchStudent(String.valueOf(id));
  }

  /**
   * 検索条件に合致する受講生詳細を取得します。
   *
   * @param condition 検索条件
   * @return 検索条件に合致する受講生詳細のリスト
   */
  @Operation(summary = "受講生詳細条件検索", description = "条件に合致する受講生詳細を検索します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "正常に取得"),
      @ApiResponse(responseCode = "404", description = "受講生が存在しない")
  })
  @PostMapping("/students/search")
  public List<StudentDetail> searchStudents(@RequestBody @Valid StudentSearchCondition condition) {
    return service.searchByCondition(condition);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "登録成功"),
      @ApiResponse(responseCode = "400", description = "バリデーションエラー")
  })
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail1 = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail1);
  }

  /**
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生詳細更新", description = "受講生詳細の更新をします。")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新成功"),
      @ApiResponse(responseCode = "400", description = "バリデーションエラー")
  })
  @PutMapping("/students")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

}