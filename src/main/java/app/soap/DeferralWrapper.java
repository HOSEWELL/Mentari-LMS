package app.soap;

import app.model.Deferral;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "deferrals")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeferralWrapper implements Serializable {

    private List<Deferral> deferral;

    public DeferralWrapper() {}

    public DeferralWrapper(List<Deferral> deferral) {

        this.deferral = deferral;
    }

    public List<Deferral> getDeferral() {

        return deferral;
    }

    public void setDeferral(List<Deferral> deferral) {

        this.deferral = deferral;
    }
}