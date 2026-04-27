package app.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;

@ApplicationScoped
public class DataSourceProvider {

    @Produces
    @ApplicationScoped
    public DataSource produceDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/mentari_db?createDatabaseIfNotExist=true");
        ds.setUser("root");
        ds.setPassword("@h.k_rajah8");
        return ds;
    }
}