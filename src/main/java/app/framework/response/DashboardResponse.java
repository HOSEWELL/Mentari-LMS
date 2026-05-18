package app.framework.response;

import java.io.Serializable;
import java.util.List;

public class DashboardResponse implements Serializable {

    private String title;

    private List<DashboardCard> cards;

    public DashboardResponse() {
    }

    public DashboardResponse(
            String title,
            List<DashboardCard> cards
    ) {

        this.title = title;
        this.cards = cards;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(
            String title
    ) {

        this.title = title;
    }

    public List<DashboardCard> getCards() {
        return cards;
    }

    public void setCards(
            List<DashboardCard> cards
    ) {

        this.cards = cards;
    }

    /*
     * RENDER HTML
     */
    public String render() {

        StringBuilder html =
                new StringBuilder();

        html.append("""
<div class="dashboard-page">

<h1 class="dashboard-title">
""");

        html.append(title);

        html.append("""
</h1>

<div class="dashboard-grid">
""");

        if (cards != null) {

            for (DashboardCard card : cards) {

                html.append("""
<div class="dashboard-card">

<div class="dashboard-card-title">
""");

                html.append(card.getTitle());

                html.append("""
</div>

<div class="dashboard-card-value">
""");

                html.append(card.getValue());

                html.append("""
</div>

</div>
""");
            }
        }

        html.append("""
</div>

</div>

<style>

.dashboard-page{
padding:20px;
}

.dashboard-title{
font-size:32px;
font-weight:700;
margin-bottom:30px;
color:#111827;
}

.dashboard-grid{
display:grid;
grid-template-columns:repeat(auto-fit,minmax(250px,1fr));
gap:20px;
}

.dashboard-card{
background:white;
padding:25px;
border-radius:16px;
box-shadow:0 4px 12px rgba(0,0,0,0.08);
border:1px solid #e5e7eb;
}

.dashboard-card-title{
font-size:16px;
color:#6b7280;
margin-bottom:12px;
}

.dashboard-card-value{
font-size:32px;
font-weight:700;
color:#2563eb;
}

</style>
""");

        return html.toString();
    }
}