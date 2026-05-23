package app.soap;

import app.model.AuditTrail;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "auditTrails")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditTrailWrapper implements Serializable {

    private List<AuditTrail> auditTrail;

    public AuditTrailWrapper() {}

    public AuditTrailWrapper(List<AuditTrail> auditTrail) {

        this.auditTrail = auditTrail;
    }

    public List<AuditTrail> getAuditTrail() {

        return auditTrail;
    }

    public void setAuditTrail(List<AuditTrail> auditTrail) {

        this.auditTrail = auditTrail;
    }
}