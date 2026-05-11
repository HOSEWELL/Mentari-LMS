package app.dao;

import app.model.Deferral;
import app.utility.bootstrap.InitBootstrap;
import app.utility.db.DataSourceHelper;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class DeferralDao extends GenericDao<Deferral, Long> {

    @Inject
    public DeferralDao(@InitBootstrap DataSourceHelper ds) {
        super(Deferral.class);
        setDs(ds);
    }

}