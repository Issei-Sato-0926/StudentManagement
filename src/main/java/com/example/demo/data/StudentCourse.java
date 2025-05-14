package com.example.demo.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  @Pattern(regexp = "^\\d+$")
  private String id;

  @NotBlank
  private String studentId;

  @NotBlank
  private String courseName;

  private LocalDateTime courseStartAt;

  private LocalDateTime courseEndAt;
}