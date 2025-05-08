package com.example.demo;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentCourseRepository {

  @Select("SELECT * FROM student_courses")
  List<StudentCourse> search();
}
