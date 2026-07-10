/*
 * Product.java
 * ------------
 * Entity mapped to the 'product' table - represents the online-retail
 * "laptop" scenario described in Hands-on 6: a user searches for laptops
 * and can filter the result set by any combination of customer review,
 * hard disk size, RAM size, CPU speed, operating system, weight and CPU.
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "customer_review")
    private int customerReview; // 1-5 star rating

    @Column(name = "hard_disk_size_gb")
    private int hardDiskSizeGb;

    @Column(name = "ram_size_gb")
    private int ramSizeGb;

    @Column(name = "cpu_speed_ghz")
    private double cpuSpeedGhz;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "weight_kg")
    private double weightKg;

    @Column(name = "cpu")
    private String cpu;

    @Column(name = "price")
    private double price;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCustomerReview() {
        return customerReview;
    }

    public void setCustomerReview(int customerReview) {
        this.customerReview = customerReview;
    }

    public int getHardDiskSizeGb() {
        return hardDiskSizeGb;
    }

    public void setHardDiskSizeGb(int hardDiskSizeGb) {
        this.hardDiskSizeGb = hardDiskSizeGb;
    }

    public int getRamSizeGb() {
        return ramSizeGb;
    }

    public void setRamSizeGb(int ramSizeGb) {
        this.ramSizeGb = ramSizeGb;
    }

    public double getCpuSpeedGhz() {
        return cpuSpeedGhz;
    }

    public void setCpuSpeedGhz(double cpuSpeedGhz) {
        this.cpuSpeedGhz = cpuSpeedGhz;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', review=" + customerReview
                + ", hdd=" + hardDiskSizeGb + "GB, ram=" + ramSizeGb + "GB, cpuSpeed=" + cpuSpeedGhz
                + "GHz, os='" + operatingSystem + "', weight=" + weightKg + "kg, cpu='" + cpu
                + "', price=" + price + "}";
    }
}
