package model;

public class CourseEnrollment {
    private int courseEnrollmentId;
    private int studentId;
    private int instructorId;
    private Course course;
    private String grade;

    public CourseEnrollment(int studentId, int instructorId, Course course, String grade, int courseEnrollmentId) {
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.course = course;
        this.grade = grade;
        this.courseEnrollmentId = courseEnrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getCourseEnrollmentId() {
        return courseEnrollmentId;
    }

    public void setCourseEnrollmentId(int courseEnrollmentId) {
        this.courseEnrollmentId = courseEnrollmentId;
    }
}
