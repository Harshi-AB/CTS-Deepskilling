/**
 * Dependency.java
 *
 * Plain Java model representing a single <dependency> entry that
 * would normally live inside a Maven pom.xml. Since Maven itself is
 * not permitted in this exercise, we model the same information as a
 * simple data class and print it out in POM-like XML form.
 */
public class Dependency {

    private final String groupId;
    private final String artifactId;
    private final String version;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String toXml() {
        return "    <dependency>\n" +
               "        <groupId>" + groupId + "</groupId>\n" +
               "        <artifactId>" + artifactId + "</artifactId>\n" +
               "        <version>" + version + "</version>\n" +
               "    </dependency>";
    }
}
