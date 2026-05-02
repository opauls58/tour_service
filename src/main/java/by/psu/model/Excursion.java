package by.psu.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class Excursion extends TourService {
    private String guideName;
    private String excursionType;
    private boolean lunchIncluded;

    public Excursion() {
        super();
    }

    public Excursion(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                     String guideName, String excursionType, boolean lunchIncluded) {
        super(id, name, price, from, to);
        this.guideName = guideName;
        this.excursionType = excursionType;
        this.lunchIncluded = lunchIncluded;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getExcursionType() {
        return excursionType;
    }

    public void setExcursionType(String excursionType) {
        this.excursionType = excursionType;
    }

    public boolean isLunchIncluded() {
        return lunchIncluded;
    }

    public void setLunchIncluded(boolean lunchIncluded) {
        this.lunchIncluded = lunchIncluded;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal totalPrice = getPrice().multiply(BigDecimal.valueOf(participants));

        // Скидка 10% при количестве участников больше 10
        if (participants > 10) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.9"));
        }

        // Если включен обед, добавляем 15%
        if (lunchIncluded) {
            totalPrice = totalPrice.multiply(new BigDecimal("1.15"));
        }

        return totalPrice;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return "Excursion{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", price=" + (getPrice() != null ? df.format(getPrice()) : "null") +
                ", from=" + getFrom() +
                ", to=" + getTo() +
                ", guideName='" + guideName + '\'' +
                ", excursionType='" + excursionType + '\'' +
                ", lunchIncluded=" + lunchIncluded +
                '}';
    }
}