package sn.free.selfcare.service.dto;

public class AmountPaidBillKpiDTO implements Comparable<AmountPaidBillKpiDTO> {
    private int monthNumber;
    private String monthName;
    private long amountOfPaidBills;

    public AmountPaidBillKpiDTO() {
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

    public long getAmountOfPaidBills() {
        return amountOfPaidBills;
    }

    public void setAmountOfPaidBills(long amountOfPaidBills) {
        this.amountOfPaidBills = amountOfPaidBills;
    }

    @Override
    public int compareTo(AmountPaidBillKpiDTO o) {
        return this.monthNumber - o.getMonthNumber();
    }

    @Override
    public String toString() {
        return "AmountPaidBillKpiDTO{" +
            "monthNumber=" + monthNumber +
            ", monthName='" + monthName + '\'' +
            ", amountOfPaidBills=" + amountOfPaidBills +
            '}';
    }
}
