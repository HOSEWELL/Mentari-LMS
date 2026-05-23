package app.framework.rendering;

import app.framework.annotation.*;
import app.framework.response.DashboardCard;
import app.framework.response.DashboardResponse;
import app.model.Deferral;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import java.lang.reflect.Field;
import java.util.List;

@ApplicationScoped
public class MentariFramework {

    public String dashboard(DashboardResponse response) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='container'>").append("<h1 class='page-title'>").append(response.getTitle()).append("</h1>").append("<div class='dashboard-grid'>");

        for (DashboardCard card : response.getCards()) {
            html.append("<div class='dashboard-card'>").append("<div class='dashboard-card-top'>").append("<div>")
                    .append("<div class='dashboard-title'>").append(card.getTitle()).append("</div>")
                    .append("<div class='dashboard-value'>").append(card.getValue()).append("</div>")
                    .append("</div>")
                    .append("<div class='dashboard-icon' style='background:").append(card.getColor()).append("'>")
                    .append("<i class='").append(card.getIcon()).append("'></i>")
                    .append("</div>").append("</div>").append("</div>");
        }

        html.append("</div></div>");
        return html.toString();
    }

    public String table(Class<?> clazz, List<?> data, String role) {
        StringBuilder html = new StringBuilder();
        MentariTableView tableView = clazz.getAnnotation(MentariTableView.class);
        boolean showAddButton = !(clazz.equals(Deferral.class) && !"STUDENT".equals(role));

        html.append("<div class='container'>").append("<div class='table-container'>");
        html.append("<div class='table-header'>").append("<h2 class='page-title'>").append(tableView.title()).append("</h2>");

        if (!tableView.addAction().isBlank() && showAddButton) {
            html.append("<a class='floating-btn' href='javascript:void(0)' onclick='toggleMentariForm()'><i class='fas fa-plus'></i></a>");
        }

        html.append("</div>");

        if (showAddButton) {
            html.append("<div id='mentari-form' style='display:none; margin-bottom:25px;'>").append(form(clazz)).append("</div>");
        }

        html.append("<table>").append("<thead>").append("<tr>");

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(MentariTableColumn.class)) {
                MentariTableColumn column = field.getAnnotation(MentariTableColumn.class);
                if (!column.hidden()) html.append("<th>").append(column.label()).append("</th>");
            }
        }

        if (clazz.equals(Deferral.class) && "ADMIN".equals(role)) html.append("<th>Actions</th>");

        html.append("</tr>").append("</thead>").append("<tbody>");

        for (Object row : data) {
            html.append("<tr>");

            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(MentariTableColumn.class)) continue;
                MentariTableColumn column = field.getAnnotation(MentariTableColumn.class);
                if (column.hidden()) continue;
                field.setAccessible(true);

                try {
                    Object value = field.get(row);

                    if (field.isAnnotationPresent(MentariRelationshipLabel.class) && value != null) {
                        String labelFieldName = field.getAnnotation(MentariRelationshipLabel.class).value();
                        Field labelField = value.getClass().getDeclaredField(labelFieldName);
                        labelField.setAccessible(true);
                        value = labelField.get(value);
                    }

                    html.append("<td>").append(value != null ? value : "-").append("</td>");
                } catch (Exception e) {
                    html.append("<td>Error</td>");
                }
            }

            if (clazz.equals(Deferral.class) && "ADMIN".equals(role)) {
                try {
                    Long id = (Long) row.getClass().getMethod("getId").invoke(row);
                    String status = (String) row.getClass().getMethod("getStatus").invoke(row);

                    html.append("<td>");
                    if ("PENDING".equalsIgnoreCase(status)) {
                        html.append("<div style='display:flex; gap:10px;'>")
                                .append("<a class='approve-btn' href='/Mentari/app/deferrals/approve/").append(id).append("'>Approve</a>")
                                .append("<a class='reject-btn' href='/Mentari/app/deferrals/reject/").append(id).append("'>Reject</a>")
                                .append("</div>");
                    } else {
                        html.append("-");
                    }
                    html.append("</td>");
                } catch (Exception e) {
                    html.append("<td>-</td>");
                }
            }

            html.append("</tr>");
        }

        html.append("</tbody>").append("</table>").append("</div>");
        html.append("<script>function toggleMentariForm(){let form=document.getElementById('mentari-form');form.style.display=form.style.display==='none'?'block':'none';}</script>");
        html.append("</div>");

        return html.toString();
    }

    public String form(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(MentariForm.class)) return "";

        MentariForm form = clazz.getAnnotation(MentariForm.class);
        StringBuilder html = new StringBuilder();

        html.append("<form method='").append(form.method()).append("' action='").append(form.actionUrl()).append("'>");

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(MentariFormField.class)) continue;
            MentariFormField formField = field.getAnnotation(MentariFormField.class);
            if (formField.hidden()) continue;

            html.append("<div class='form-group'>").append("<label>").append(formField.label()).append("</label>");

            if (field.isAnnotationPresent(MentariSelect.class)) {
                MentariSelect select = field.getAnnotation(MentariSelect.class);
                try {
                    Class<?> daoClass = Class.forName("app.dao." + select.entity().getSimpleName() + "Dao");
                    Object dao = CDI.current().select(daoClass).get();
                    List<?> items = (List<?>) daoClass.getMethod("findAll").invoke(dao);

                    html.append("<select name='").append(field.getName()).append(".id'>");

                    for (Object item : items) {
                        Field idField = item.getClass().getSuperclass().getDeclaredField("id");
                        idField.setAccessible(true);
                        Object id = idField.get(item);
                        Field labelField = item.getClass().getDeclaredField(select.labelField());
                        labelField.setAccessible(true);
                        Object label = labelField.get(item);
                        html.append("<option value='").append(id).append("'>").append(label).append("</option>");
                    }

                    html.append("</select>");
                } catch (Exception e) {
                    html.append("Select Error");
                }
            } else {
                html.append("<input type='").append(formField.type()).append("' name='").append(field.getName())
                        .append("' placeholder='").append(formField.placeholder()).append("' ")
                        .append(formField.required() ? "required" : "").append("/>");
            }

            html.append("</div>");
        }

        html.append("<button class='btn'>").append(form.label()).append("</button>").append("</form>");
        return html.toString();
    }
}