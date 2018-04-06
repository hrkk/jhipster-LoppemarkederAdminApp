package dk.roninit.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class MarkedItemView {
    private Long id;
    private String name;
    private String additionalOpenTimePeriod;
    private String entreInfo;
    private String markedRules;
    private String markedInformation;

    private String address;
    private LocalDate fromDate;
    private LocalDate toDate;

    private Double latitude;
    private Double longitude;

    // organizer
    private String organizerName;
    private String organizerEmail;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionalOpenTimePeriod() {
        return additionalOpenTimePeriod;
    }

    public void setAdditionalOpenTimePeriod(String additionalOpenTimePeriod) {
        this.additionalOpenTimePeriod = additionalOpenTimePeriod;
    }

    public String getEntreInfo() {
        return entreInfo;
    }

    public void setEntreInfo(String entreInfo) {
        this.entreInfo = entreInfo;
    }

    public String getMarkedRules() {
        return markedRules;
    }

    public void setMarkedRules(String markedRules) {
        this.markedRules = markedRules;
    }

    public String getMarkedInformation() {
        return markedInformation;
    }

    public void setMarkedInformation(String markedInformation) {
        this.markedInformation = markedInformation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }
}
