package br.com.sw2you.realmeet.report.model;

import java.time.LocalDate;
import java.util.Objects;

public class AllocationReportData extends AbstractReportData {
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    private AllocationReportData(AllocationReportDataBuilder builder) {
        super(builder.email);
        dateFrom = builder.dateFrom;
        dateTo = builder.dateTo;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AllocationReportData that = (AllocationReportData) o;
        return Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateFrom, dateTo);
    }

    @Override
    public String toString() {
        return "AllocationReportData{" + "dateFrom=" + dateFrom + ", dateTo=" + dateTo + '}';
    }

    public static AllocationReportDataBuilder newAllocationDataReportBuilder() {
        return new AllocationReportDataBuilder();
    }

    public static final class AllocationReportDataBuilder {
        private String email;
        private LocalDate dateFrom;
        private LocalDate dateTo;

        private AllocationReportDataBuilder() {}

        public AllocationReportDataBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AllocationReportDataBuilder dateFrom(LocalDate dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public AllocationReportDataBuilder dateTo(LocalDate dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public AllocationReportData build() {
            return new AllocationReportData(this);
        }
    }
}
