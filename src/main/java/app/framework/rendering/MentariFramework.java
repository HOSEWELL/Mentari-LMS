package app.framework.rendering;

import app.framework.annotation.*;
import app.framework.response.DashboardCard;
import app.framework.response.DashboardResponse;
import app.model.Deferral;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@ApplicationScoped
public class MentariFramework {

    /*
     * =========================================
     * DASHBOARD
     * =========================================
     */
    public String dashboard(
            DashboardResponse response
    ) {

        StringBuilder html = new StringBuilder();

        html.append("""
<div class='container'>

<h1 class='page-title'>
""");

        html.append(response.getTitle());

        html.append("""
</h1>

<div class='dashboard-grid'>
""");

        for (DashboardCard card : response.getCards()) {

            html.append("""
<div class='dashboard-card'>

<div class='dashboard-card-top'>

<div>

<div class='dashboard-title'>
""");

            html.append(card.getTitle());

            html.append("""
</div>

<div class='dashboard-value'>
""");

            html.append(card.getValue());

            html.append("""
</div>

</div>

<div class='dashboard-icon'
style='background:
""");

            html.append(card.getColor());

            html.append("""
'>

<i class='
""");

            html.append(card.getIcon());

            html.append("""
'></i>

</div>

</div>

</div>
""");
        }

        html.append("""
</div>

</div>
""");

        return html.toString();
    }

    /*
     * =========================================
     * TABLE GENERATOR
     * =========================================
     */
    public String table(
            Class<?> clazz,
            List<?> data
    ) {

        StringBuilder html =
                new StringBuilder();

        MentariTableView tableView =
                clazz.getAnnotation(
                        MentariTableView.class
                );

        html.append("""
<div class='container'>

<div class='table-container'>
""");

        /*
         * HEADER
         */
        html.append("""
<div class='table-header'>
""");

        html.append("""
<h2 class='page-title'>
""");

        html.append(tableView.title());

        html.append("""
</h2>
""");

        /*
         * FLOAT BUTTON
         */
        if (!tableView.addAction().isBlank()) {

            html.append("""
<a class='floating-btn'
href='javascript:void(0)'
onclick='toggleMentariForm()'>

<i class='fas fa-plus'></i>

</a>
""");
        }

        html.append("""
</div>
""");

        /*
         * FORM
         */
        html.append("""
<div id='mentari-form'
style='display:none; margin-bottom:25px;'>
""");

        html.append(form(clazz));

        html.append("""
</div>
""");

        /*
         * TABLE
         */
        html.append("""
<table>

<thead>

<tr>
""");

        for (Field field :
                clazz.getDeclaredFields()) {

            if (
                    field.isAnnotationPresent(
                            MentariTableColumn.class
                    )
            ) {

                MentariTableColumn column =
                        field.getAnnotation(
                                MentariTableColumn.class
                        );

                if (!column.hidden()) {

                    html.append("""
<th>
""");

                    html.append(column.label());

                    html.append("""
</th>
""");
                }
            }
        }

        /*
         * DEFERRAL ACTIONS
         */
        if (clazz.equals(Deferral.class)) {

            html.append("""
<th>Actions</th>
""");
        }

        html.append("""
</tr>

</thead>

<tbody>
""");

        /*
         * ROWS
         */
        for (Object row : data) {

            html.append("""
<tr>
""");

            for (Field field :
                    clazz.getDeclaredFields()) {

                if (
                        field.isAnnotationPresent(
                                MentariTableColumn.class
                        )
                ) {

                    MentariTableColumn column =
                            field.getAnnotation(
                                    MentariTableColumn.class
                            );

                    if (column.hidden()) {
                        continue;
                    }

                    field.setAccessible(true);

                    try {

                        Object value =
                                field.get(row);

                        /*
                         * RELATIONSHIP LABEL
                         */
                        if (
                                field.isAnnotationPresent(
                                        MentariRelationshipLabel.class
                                )
                        ) {

                            if (value != null) {

                                String labelFieldName =
                                        field.getAnnotation(
                                                MentariRelationshipLabel.class
                                        ).value();

                                Field labelField =
                                        value.getClass()
                                                .getDeclaredField(
                                                        labelFieldName
                                                );

                                labelField.setAccessible(true);

                                value =
                                        labelField.get(value);
                            }
                        }

                        html.append("""
<td>
""");

                        html.append(
                                value != null
                                        ? value
                                        : "-"
                        );

                        html.append("""
</td>
""");

                    } catch (Exception e) {

                        html.append("""
<td>Error</td>
""");
                    }
                }
            }

            /*
             * DEFERRAL ACTIONS
             */
            if (clazz.equals(Deferral.class)) {

                try {

                    Method method =
                            row.getClass()
                                    .getMethod("getId");

                    Long id =
                            (Long) method.invoke(row);

                    html.append("""
<td>

<div style='display:flex; gap:10px;'>

<a class='approve-btn'
href='/Mentari/app/deferrals/approve/
""");

                    html.append(id);

                    html.append("""
'>

Approve

</a>

<a class='reject-btn'
href='/Mentari/app/deferrals/reject/
""");

                    html.append(id);

                    html.append("""
'>

Reject

</a>

</div>

</td>
""");

                } catch (Exception e) {

                    html.append("""
<td>-</td>
""");
                }
            }

            html.append("""
</tr>
""");
        }

        html.append("""
</tbody>

</table>

</div>

<script>

function toggleMentariForm(){

    let form =
        document.getElementById(
            'mentari-form'
        );

    if(form.style.display === 'none'){

        form.style.display = 'block';

    }else{

        form.style.display = 'none';
    }
}

</script>

</div>
""");

        return html.toString();
    }

    /*
     * =========================================
     * FORM GENERATOR
     * =========================================
     */
    public String form(
            Class<?> clazz
    ) {

        if (
                !clazz.isAnnotationPresent(
                        MentariForm.class
                )
        ) {

            return "";
        }

        MentariForm form =
                clazz.getAnnotation(
                        MentariForm.class
                );

        StringBuilder html =
                new StringBuilder();

        html.append("""
<form method='
""");

        html.append(form.method());

        html.append("""
' action='
""");

        html.append(form.actionUrl());

        html.append("""
'>
""");

        for (Field field :
                clazz.getDeclaredFields()) {

            if (
                    field.isAnnotationPresent(
                            MentariFormField.class
                    )
            ) {

                MentariFormField formField =
                        field.getAnnotation(
                                MentariFormField.class
                        );

                if (formField.hidden()) {
                    continue;
                }

                html.append("""
<div class='form-group'>
""");

                html.append("""
<label>
""");

                html.append(formField.label());

                html.append("""
</label>
""");

                /*
                 * SELECT
                 */
                if (
                        field.isAnnotationPresent(
                                MentariSelect.class
                        )
                ) {

                    MentariSelect select =
                            field.getAnnotation(
                                    MentariSelect.class
                            );

                    try {

                        Class<?> daoClass =
                                Class.forName(
                                        "app.dao."
                                                + select.entity()
                                                .getSimpleName()
                                                + "Dao"
                                );

                        Object dao =
                                CDI.current()
                                        .select(daoClass)
                                        .get();

                        Method method =
                                daoClass.getMethod(
                                        "findAll"
                                );

                        List<?> items =
                                (List<?>)
                                        method.invoke(dao);

                        html.append("""
<select name='
""");

                        html.append(field.getName());

                        html.append("""
.id'>
""");

                        for (Object item : items) {

                            Field idField =
                                    item.getClass()
                                            .getSuperclass()
                                            .getDeclaredField("id");

                            idField.setAccessible(true);

                            Object id =
                                    idField.get(item);

                            Field labelField =
                                    item.getClass()
                                            .getDeclaredField(
                                                    select.labelField()
                                            );

                            labelField.setAccessible(true);

                            Object label =
                                    labelField.get(item);

                            html.append("""
<option value='
""");

                            html.append(id);

                            html.append("""
'>
""");

                            html.append(label);

                            html.append("""
</option>
""");
                        }

                        html.append("""
</select>
""");

                    } catch (Exception e) {

                        html.append("""
Select Error
""");
                    }

                } else {

                    html.append("""
<input type='
""");

                    html.append(formField.type());

                    html.append("""
' name='
""");

                    html.append(field.getName());

                    html.append("""
' placeholder='
""");

                    html.append(formField.placeholder());

                    html.append("""
' 
""");

                    if (formField.required()) {
                        html.append("required");
                    }

                    html.append("""
/>
""");
                }

                html.append("""
</div>
""");
            }
        }

        html.append("""
<button class='btn'>
""");

        html.append(form.label());

        html.append("""
</button>

</form>
""");

        return html.toString();
    }
}