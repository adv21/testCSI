package ru.crystals;

import java.util.Date;
import java.util.Objects;

public class Price {

    private long id;
    private String productCode;
    private int number;
    private int department;
    private Date begin;
    private Date end;
    private long value;

    public Price() {

    }

    public Price(long id, String productCode, int number, int department, Date begin, Date end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.department = department;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public Price(Price price) {
        this(
            price.getId(),
            price.getProductCode(),
            price.getNumber(),
            price.getDepartment(),
            new Date(price.getBegin().getTime()),
            new Date(price.getEnd().getTime()), price.getValue()
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price = (Price) o;
        return  id == price.id &&
                number == price.number &&
                department == price.department &&
                value == price.value &&
                productCode.equals(price.productCode) &&
                begin.equals(price.begin) &&
                end.equals(price.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, number, department, begin, end, value);
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", department=" + department +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }
}
