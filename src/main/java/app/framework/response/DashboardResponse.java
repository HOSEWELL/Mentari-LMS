package app.framework.response;

import java.io.Serializable;
import java.util.List;

public class DashboardResponse implements Serializable {

    private String title;
    private List<DashboardCard> cards;

    public DashboardResponse() {}

    public DashboardResponse(String title, List<DashboardCard> cards) {
        this.title = title;
        this.cards = cards;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<DashboardCard> getCards() { return cards; }
    public void setCards(List<DashboardCard> cards) { this.cards = cards; }

    public String render() {
        StringBuilder html = new StringBuilder();

        html.append("<div class=\"dashboard-page\">")
                .append("<h1 class=\"dashboard-title\">").append(title).append("</h1>")
                .append("<div class=\"dashboard-grid\">");

        if (cards != null) {
            for (DashboardCard card : cards) {
                html.append("<div class=\"dashboard-card\">")
                        .append("<div class=\"dashboard-card-title\">").append(card.getTitle()).append("</div>")
                        .append("<div class=\"dashboard-card-value\">").append(card.getValue()).append("</div>")
                        .append("</div>");
            }
        }

        html.append("</div></div>");
        html.append("""
<style>
.dashboard-page{ padding:20px; }
.dashboard-title{ font-size:32px; font-weight:700; margin-bottom:30px; color:#111827; }
.dashboard-grid{ display:grid; grid-template-columns:repeat(auto-fit,minmax(250px,1fr)); gap:20px; }
.dashboard-card{ background:white; padding:25px; border-radius:16px; box-shadow:0 4px 12px rgba(0,0,0,0.08); border:1px solid #e5e7eb; }
.dashboard-card-title{ font-size:16px; color:#6b7280; margin-bottom:12px; }
.dashboard-card-value{ font-size:32px; font-weight:700; color:#2563eb; }
</style>
""");

        return html.toString();
    }
}