package app.framework.response;

import java.io.Serializable;
import java.util.List;

public class ActionResponse implements Serializable {

    private Class<?> responseClazz;

    private List<?> responseDataList;

    private String responseText;

    private DashboardResponse dashboardResponse;

    private RedirectResponse redirectResponse;

    public ActionResponse() {
    }

    /*
     * TABLE RESPONSE
     */
    public ActionResponse(
            Class<?> responseClazz,
            List<?> responseDataList
    ) {

        this.responseClazz = responseClazz;
        this.responseDataList = responseDataList;
    }

    /*
     * TEXT RESPONSE
     */
    public ActionResponse(
            String responseText
    ) {

        this.responseText = responseText;
    }

    /*
     * DASHBOARD RESPONSE
     */
    public ActionResponse(
            DashboardResponse dashboardResponse
    ) {

        this.dashboardResponse = dashboardResponse;
    }

    /*
     * REDIRECT RESPONSE
     */
    public ActionResponse(
            RedirectResponse redirectResponse
    ) {

        this.redirectResponse = redirectResponse;
    }

    public Class<?> getResponseClazz() {
        return responseClazz;
    }

    public void setResponseClazz(
            Class<?> responseClazz
    ) {

        this.responseClazz = responseClazz;
    }

    public List<?> getResponseDataList() {
        return responseDataList;
    }

    public void setResponseDataList(
            List<?> responseDataList
    ) {

        this.responseDataList = responseDataList;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(
            String responseText
    ) {

        this.responseText = responseText;
    }

    public DashboardResponse getDashboardResponse() {
        return dashboardResponse;
    }

    public void setDashboardResponse(
            DashboardResponse dashboardResponse
    ) {

        this.dashboardResponse = dashboardResponse;
    }

    public RedirectResponse getRedirectResponse() {
        return redirectResponse;
    }

    public void setRedirectResponse(
            RedirectResponse redirectResponse
    ) {

        this.redirectResponse = redirectResponse;
    }
}