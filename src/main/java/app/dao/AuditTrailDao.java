package app.dao;

import app.model.AuditTrail;
import app.utility.bootstrap.InitBootstrap;
import app.utility.db.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class AuditTrailDao extends GenericDao<AuditTrail, Long> {

    @Inject
    public AuditTrailDao(@InitBootstrap DataSourceHelper ds) {
        super(AuditTrail.class);
        setDs(ds);
    }
}