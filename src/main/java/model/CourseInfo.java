package model;

public class CourseInfo {
    private int studentCourseId;
    private Course course;
    private String grade;

    public CourseInfo(int studentCourseId, String grade, Course course) {
        this.studentCourseId = studentCourseId;
        this.grade = grade;
        this.course = course;
    }

    public int getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(int studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
