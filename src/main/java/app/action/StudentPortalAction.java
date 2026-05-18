package app.action;

import app.framework.annotation.Action;
import app.framework.annotation.ActionGetMethod;
import app.framework.annotation.ProtectedRoute;

import app.framework.response.ActionResponse;

@ProtectedRoute(
        roles = {"STUDENT"}
)

@Action(
        value = "student-portal",
        label = "Student Portal",
        roles = {"STUDENT"},
        showLink = true
)
public class StudentPortalAction {

    @ActionGetMethod("index")
    public ActionResponse index() {

        return new ActionResponse("""
<div class='card'>

<h1 class='page-title'>
Student Portal
</h1>

<p>
Welcome to Mentari LMS Student Portal
</p>

</div>
""");
    }
}