/**
 * CreatingAndConfiguringAMavenProject.java
 *
 * Demonstrates "creating and configuring a Maven project" without
 * using the actual Maven tool (not permitted in this exercise). We
 * model the project's coordinates and dependencies using ProjectModel,
 * generate the equivalent pom.xml content, and simulate running the
 * Maven build lifecycle using MavenBuildSimulator.
 */
public class CreatingAndConfiguringAMavenProject {

    public static void main(String[] args) {
        // 1. Configure the project coordinates (like the top of a pom.xml)
        ProjectModel project = new ProjectModel(
                "com.cognizant.deepskilling",
                "spring-core-exercises",
                "1.0.0",
                "jar"
        );

        // 2. Add dependencies (like <dependencies> entries in pom.xml)
        project.addDependency(new Dependency("org.springframework", "spring-context", "5.3.20"));
        project.addDependency(new Dependency("junit", "junit", "4.13.2"));

        // 3. Print the generated pom.xml
        System.out.println("Generated pom.xml:\n");
        System.out.println(project.toPomXml());

        // 4. Simulate running "mvn install"
        System.out.println("\nRunning Maven build lifecycle:\n");
        MavenBuildSimulator buildSimulator = new MavenBuildSimulator();
        buildSimulator.runBuild(project);
    }
}
