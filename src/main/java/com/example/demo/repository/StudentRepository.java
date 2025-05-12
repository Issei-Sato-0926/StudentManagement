package com.example.demo.repository;

import com.example.demo.data.Student;
import com.example.demo.data.StudentCourses;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(String id);

  @Select("SELECT * FROM student_courses")
  List<StudentCourses> searchStudentCoursesLIst();

  @Select("SELECT * FROM student_courses WHERE student_id = #{studentId}")
  List<StudentCourses> searchStudentCourses(String studentId);

  @Insert(
      "INSERT INTO students(name, kana_name, nickname, email, area, age, gender, remark, is_deleted)"
          + "VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Insert("INSERT INTO student_courses(student_id, course_name, course_start_at, course_end_at)"
      + "VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourses(StudentCourses studentCourses);

  @Update(
      "UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, email = #{email},"
          + " area = #{area}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} WHERE id = #{id}")
  void updateStudent(Student student);

  @Update(
      "UPDATE student_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentCourses(StudentCourses studentCourses);
}