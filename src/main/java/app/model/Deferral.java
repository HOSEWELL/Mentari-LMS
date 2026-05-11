package app.model;

import app.framework.MentariColumn;
import app.framework.MentariTable;

@MentariTable(name = "deferrals")
public class Deferral {

    @MentariColumn(name = "id", primaryKey = true)
    private Long id;

    @MentariColumn(name = "student_id")
    private Long studentId;

    @MentariColumn(name = "student_name")
    private String studentName;

    @MentariColumn(name = "start_date")
    private String startDate;

    @MentariColumn(name = "end_date")
    private String endDate;

    @MentariColumn(name = "reason")
    private String reason;

    @MentariColumn(name = "status")
    private String status;

    @MentariColumn(name = "submitted_at")
    private String submittedAt;

    public Deferral() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
}