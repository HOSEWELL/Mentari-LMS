package app.model;

import app.framework.annotation.*;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "deferrals")

@MentariTableView(
        title = "Deferrals",
        addAction = "/app/deferrals/student"
)

@MentariForm(
        actionUrl = "/app/deferrals/submit",
        label = "Submit Deferral"
)
public class Deferral extends BaseEntity {

    @MentariTableColumn(
            label = "Student"
    )
    @MentariRelationshipLabel("fullName")
    @MentariSelect(
            entity = Student.class,
            labelField = "fullName"
    )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentId")
    private Student student;

    /*
     * =========================
     * START DATE
     * =========================
     */
    @MentariTableColumn(
            label = "Start Date"
    )
    @MentariFormField(
            label = "Start Date",
            type = "date"
    )
    @Column(name = "startDate")
    private LocalDate startDate;

    /*
     * =========================
     * END DATE
     * =========================
     */
    @MentariTableColumn(
            label = "End Date"
    )
    @MentariFormField(
            label = "End Date",
            type = "date"
    )
    @Column(name = "endDate")
    private LocalDate endDate;

    /*
     * =========================
     * REASON
     * =========================
     */
    @MentariTableColumn(
            label = "Reason"
    )
    @MentariFormField(
            label = "Reason",
            placeholder = "Enter reason"
    )
    @Column(columnDefinition = "TEXT")
    private String reason;

    /*
     * =========================
     * STATUS
     * =========================
     */
    @MentariTableColumn(
            label = "Status"
    )
    @Column(nullable = false)
    private String status;

    /*
     * =========================
     * SUBMITTED AT
     * =========================
     */
    @MentariTableColumn(
            label = "Submitted At"
    )
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submittedAt")
    private Date submittedAt;

    public Deferral() {

        this.status = "PENDING";

        this.submittedAt = new Date();
    }

    /*
     * =========================
     * GETTERS & SETTERS
     * =========================
     */

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(
            LocalDate startDate
    ) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(
            LocalDate endDate
    ) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(
            String reason
    ) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(
            Date submittedAt
    ) {
        this.submittedAt = submittedAt;
    }
}