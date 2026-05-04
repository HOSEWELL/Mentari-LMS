package app.model;

import app.framework.MentariColumn;
import app.framework.MentariTable;

@MentariTable(name = "audit_trail")
public class AuditTrail {

    @MentariColumn(name = "id", primaryKey = true)
    private Long id;

    @MentariColumn(name = "activity")
    private String activity;

    @MentariColumn(name = "created_at")
    private String createdAt;

    public AuditTrail() {}

    public AuditTrail(String activity) {
        this.activity = activity;
        this.createdAt = new java.util.Date().toString();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}