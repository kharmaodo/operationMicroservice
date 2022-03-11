package sn.free.selfcare.service.dto;

public class CreditValuePurchasedKpiDTO implements Comparable<CreditValuePurchasedKpiDTO> {
    private int monthNumber;
    private String monthName;
    private long creditValuePurchased;

    public CreditValuePurchasedKpiDTO() {
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public long getCreditValuePurchased() {
        return creditValuePurchased;
    }

    public void setCreditValuePurchased(long creditValuePurchased) {
        this.creditValuePurchased = creditValuePurchased;
    }

    @Override
    public int compareTo(CreditValuePurchasedKpiDTO o) {
        return this.monthNumber - o.getMonthNumber();
    }

    @Override
    public String toString() {
        return "CreditValuePurchasedKpiDTO{" +
            "monthNumber=" + monthNumber +
            ", monthName='" + monthName + '\'' +
            ", creditValuePurchased=" + creditValuePurchased +
            '}';
    }
}
