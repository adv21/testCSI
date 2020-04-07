package ru.crystals;

import java.util.Objects;

public class PriceKey {
    private String productCode;
    private int number;
    private int department;

    public PriceKey(Price price) {
        this.productCode = price.getProductCode();
        this.number = price.getNumber();
        this.department = price.getDepartment();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceKey)) return false;
        PriceKey priceKey = (PriceKey) o;
        return number == priceKey.number &&
                department == priceKey.department &&
                productCode.equals(priceKey.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, number, department);
    }

    @Override
    public String toString() {
        return "PriceKey{" +
                "productCode='" + productCode + '\'' +
                ", number=" + number +
                ", department=" + department +
                '}';
    }
}
