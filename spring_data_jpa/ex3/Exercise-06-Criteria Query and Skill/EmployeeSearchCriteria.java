/*
 * EmployeeSearchCriteria.java
 * ----------------------------
 * Value object holding the optional filters for the "search employees by
 * department / employment type / salary / skill" scenario. Fulfils the
 * "...and Skill" part of this exercise's title by adding a Skill-based
 * filter on top of the classic Criteria Query pattern.
 */
public class EmployeeSearchCriteria {

    private String departmentName;
    private Boolean permanent;
    private Double minSalary;
    private String skillName;

    public String getDepartmentName() {
        return departmentName;
    }

    public EmployeeSearchCriteria setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public EmployeeSearchCriteria setPermanent(Boolean permanent) {
        this.permanent = permanent;
        return this;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public EmployeeSearchCriteria setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
        return this;
    }

    public String getSkillName() {
        return skillName;
    }

    public EmployeeSearchCriteria setSkillName(String skillName) {
        this.skillName = skillName;
        return this;
    }
}
