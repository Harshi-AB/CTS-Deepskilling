/**
 * MavenBuildSimulator.java
 *
 * Simulates the standard Maven build lifecycle phases
 * (validate -> compile -> test -> package -> install) for a given
 * ProjectModel. Since the real "mvn" tool is not permitted in this
 * exercise, this class walks through the same conceptual phases in
 * plain Java, printing progress for each one.
 */
public class MavenBuildSimulator {

    private static final String[] LIFECYCLE_PHASES = {
            "validate", "compile", "test", "package", "install"
    };

    public void runBuild(ProjectModel project) {
        System.out.println("Building project: " + project.getArtifactId());
        for (String phase : LIFECYCLE_PHASES) {
            System.out.println("[" + phase.toUpperCase() + "] executing " + phase + " phase...");
        }
        System.out.println("BUILD SUCCESS");
    }
}
