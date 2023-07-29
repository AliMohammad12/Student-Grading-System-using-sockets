package model;

import java.util.List;
import java.util.Objects;

public class Course {
    private int courseId;
    private String courseName;
    private String departmentName;

    public Course(String courseName, String departmentName) {
        this.courseName = courseName;
        this.departmentName = departmentName;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
        ret += "  |  Department: " + departmentName;
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
        return Objects.hash(courseName, departmentName);
    }
}
