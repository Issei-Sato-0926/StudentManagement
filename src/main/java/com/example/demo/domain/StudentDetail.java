package com.example.demo.domain;

import com.example.demo.data.Student;
import com.example.demo.data.StudentCourses;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentCourses> studentCourses;

}
