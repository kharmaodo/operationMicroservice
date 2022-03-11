package sn.free.selfcare.service.dto;

public class NumberPaidBillKpiDTO implements Comparable<NumberPaidBillKpiDTO> {
    private int monthNumber;
    private String monthName;
    private long numberOfPaidBills;

    public NumberPaidBillKpiDTO() {
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

    public long getNumberOfPaidBills() {
        return numberOfPaidBills;
    }

    public void setNumberOfPaidBills(long numberOfPaidBills) {
        this.numberOfPaidBills = numberOfPaidBills;
    }

    @Override
    public int compareTo(NumberPaidBillKpiDTO o) {
        return this.monthNumber - o.getMonthNumber();
    }

    @Override
    public String toString() {
        return "NumberPaidBillKpiDTO{" +
            "monthNumber=" + monthNumber +
            ", monthName='" + monthName + '\'' +
            ", numberOfPaidBills=" + numberOfPaidBills +
            '}';
    }
}
