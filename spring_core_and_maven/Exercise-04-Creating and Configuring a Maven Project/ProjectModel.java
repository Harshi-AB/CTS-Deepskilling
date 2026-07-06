import java.util.ArrayList;
import java.util.List;

/**
 * ProjectModel.java
 *
 * Plain Java representation of a Maven project's coordinates and
 * dependency list - i.e. the same information a real pom.xml would
 * hold (groupId, artifactId, version, packaging, dependencies).
 * This lets us "configure a Maven project" conceptually and print
 * a generated pom.xml, without invoking the actual Maven tool.
 */
public class ProjectModel {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String packaging;
    private final List<Dependency> dependencies = new ArrayList<>();

    public ProjectModel(String groupId, String artifactId, String version, String packaging) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    /**
     * Renders this model as a Maven-style pom.xml string.
     */
    public String toPomXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<project>\n");
        sb.append("    <groupId>").append(groupId).append("</groupId>\n");
        sb.append("    <artifactId>").append(artifactId).append("</artifactId>\n");
        sb.append("    <version>").append(version).append("</version>\n");
        sb.append("    <packaging>").append(packaging).append("</packaging>\n");
        sb.append("    <dependencies>\n");
        for (Dependency dependency : dependencies) {
            sb.append(dependency.toXml()).append("\n");
        }
        sb.append("    </dependencies>\n");
        sb.append("</project>");
        return sb.toString();
    }

    public String getArtifactId() {
        return artifactId;
    }
}
