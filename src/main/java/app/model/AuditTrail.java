package app.model;

import app.framework.annotation.MentariTableColumn;
import app.framework.annotation.MentariTableView;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "audit_trail")

@MentariTableView(
        title = "Audit Trail"
)
public class AuditTrail extends BaseEntity {

    @MentariTableColumn(
            label = "Activity"
    )
    @Column(columnDefinition = "TEXT")
    private String activity;

    @MentariTableColumn(
            label = "Created At"
    )
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    public AuditTrail() {
        this.createdAt = new Date();
    }

    public AuditTrail(String activity) {
        this.activity = activity;
        this.createdAt = new Date();
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}