CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    kana_name VARCHAR(100) NOT NULL,
    nickname VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    area VARCHAR(100) NOT NULL,
    age INT,
    gender VARCHAR(10),
    remark TEXT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE student_courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE application_statuses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_course_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (student_course_id) REFERENCES student_courses(id)
);