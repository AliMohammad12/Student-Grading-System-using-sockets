package model;

import java.util.List;
import java.util.Objects;

public class Course {
    private int courseId;
    private String courseName;
    private Department department;

    public Course(String courseName, Department department) {
        this.courseName = courseName;
        this.department = department;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "-Name: " + courseName;
        ret += "  |  Department: " + department.getName();
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseName, course.courseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, department);
    }
}
