import java.util.LinkedHashMap;
import java.util.Map;

/**
 * EntityMapping
 * -------------
 * Plain holder for the object-relational mapping information parsed out of
 * an Employee.hbm.xml style file: which table a class maps to, which
 * property is the @Id-equivalent, and the property-name -> column-name
 * pairs for every other mapped field.
 */
public class EntityMapping {

    private String className;
    private String tableName;
    private String idPropertyName;
    private String idColumnName;
    private String generatorClass;
    private final Map<String, String> propertyToColumn = new LinkedHashMap<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdPropertyName() {
        return idPropertyName;
    }

    public void setIdPropertyName(String idPropertyName) {
        this.idPropertyName = idPropertyName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public String getGeneratorClass() {
        return generatorClass;
    }

    public void setGeneratorClass(String generatorClass) {
        this.generatorClass = generatorClass;
    }

    public Map<String, String> getPropertyToColumn() {
        return propertyToColumn;
    }

    public void addProperty(String propertyName, String columnName) {
        propertyToColumn.put(propertyName, columnName);
    }
}
