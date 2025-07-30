package org.example.simple_sharding.entity;


import javax.persistence.*;

/**
 * Entity mẫu cho sharded table
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId; // Đây là sharding key

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "country_code")
    private String countryCode;

    // Constructors
    public Order() {}

    public Order(Long tenantId, String orderNumber, String customerName, Double totalAmount, String status, String countryCode) {
        this.tenantId = tenantId;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.countryCode = countryCode;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", tenantId='" + tenantId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}