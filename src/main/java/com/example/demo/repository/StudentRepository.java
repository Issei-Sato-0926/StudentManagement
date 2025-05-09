package com.example.demo.repository;

import com.example.demo.data.Student;
import com.example.demo.data.StudentCourses;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM student_courses")
  List<StudentCourses> searchStudentCourses();
}