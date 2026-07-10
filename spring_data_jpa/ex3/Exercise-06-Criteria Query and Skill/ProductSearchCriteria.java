/*
 * ProductSearchCriteria.java
 * ---------------------------
 * Plain value object (Value Object / Criteria pattern) holding the optional
 * filter values a user may have selected on the left-hand filter panel of
 * the retail search page. Every field is nullable/boxed - a null value
 * means "the user did not select a filter for this criteria", so the
 * repository must NOT add a predicate for it.
 *
 * This is exactly the scenario Hands-on 6 describes: the WHERE clause
 * varies dynamically depending on which filters the user picked, which is
 * precisely the problem the JPA Criteria API is designed to solve cleanly
 * (as opposed to string-concatenating an HQL WHERE clause by hand).
 */
public class ProductSearchCriteria {

    private Integer minCustomerReview;
    private Integer minHardDiskSizeGb;
    private Integer minRamSizeGb;
    private Double minCpuSpeedGhz;
    private String operatingSystem;
    private Double maxWeightKg;
    private String cpu;

    public Integer getMinCustomerReview() {
        return minCustomerReview;
    }

    public ProductSearchCriteria setMinCustomerReview(Integer minCustomerReview) {
        this.minCustomerReview = minCustomerReview;
        return this;
    }

    public Integer getMinHardDiskSizeGb() {
        return minHardDiskSizeGb;
    }

    public ProductSearchCriteria setMinHardDiskSizeGb(Integer minHardDiskSizeGb) {
        this.minHardDiskSizeGb = minHardDiskSizeGb;
        return this;
    }

    public Integer getMinRamSizeGb() {
        return minRamSizeGb;
    }

    public ProductSearchCriteria setMinRamSizeGb(Integer minRamSizeGb) {
        this.minRamSizeGb = minRamSizeGb;
        return this;
    }

    public Double getMinCpuSpeedGhz() {
        return minCpuSpeedGhz;
    }

    public ProductSearchCriteria setMinCpuSpeedGhz(Double minCpuSpeedGhz) {
        this.minCpuSpeedGhz = minCpuSpeedGhz;
        return this;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public ProductSearchCriteria setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    public Double getMaxWeightKg() {
        return maxWeightKg;
    }

    public ProductSearchCriteria setMaxWeightKg(Double maxWeightKg) {
        this.maxWeightKg = maxWeightKg;
        return this;
    }

    public String getCpu() {
        return cpu;
    }

    public ProductSearchCriteria setCpu(String cpu) {
        this.cpu = cpu;
        return this;
    }
}
