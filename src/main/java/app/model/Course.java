package app.model;

import app.framework.MentariColumn;
import app.framework.MentariTable;

@MentariTable(name = "courses")
public class Course {

    @MentariColumn(name = "id", primaryKey = true)
    private Long id;

    @MentariColumn(name = "course_name")
    private String name;

    @MentariColumn(name = "course_code")
    private String code;

    @MentariColumn(name = "description")
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}