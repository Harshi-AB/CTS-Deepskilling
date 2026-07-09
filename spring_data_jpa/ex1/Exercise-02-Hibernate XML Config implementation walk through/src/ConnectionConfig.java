/**
 * ConnectionConfig
 * ----------------
 * Plain holder for the &lt;session-factory&gt; properties parsed out of
 * hibernate.cfg.xml (driver class, JDBC URL, credentials, dialect, etc).
 */
public class ConnectionConfig {

    private String driverClass;
    private String url;
    private String username;
    private String password;
    private String dialect;
    private boolean showSql;
    private String ddlAuto;
    private String mappingResource;

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public String getMappingResource() {
        return mappingResource;
    }

    public void setMappingResource(String mappingResource) {
        this.mappingResource = mappingResource;
    }
}
