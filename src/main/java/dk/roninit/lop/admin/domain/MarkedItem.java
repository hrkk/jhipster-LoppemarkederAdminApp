package dk.roninit.lop.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MarkedItem.
 */
@Entity
@Table(name = "marked_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "markeditem")
public class MarkedItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "marked_information", length = 256, nullable = false)
    private String markedInformation;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "marked_rules", length = 256, nullable = false)
    private String markedRules;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "entre_info", length = 256, nullable = false)
    private String entreInfo;

    @Size(max = 256)
    @Column(name = "date_extra_info", length = 256)
    private String dateExtraInfo;

    @NotNull
    @Size(min = 2)
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Column(name = "enable_booking", nullable = false)
    private Boolean enableBooking;

    @OneToMany(mappedBy = "markedItem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DateInterval> dateIntervals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MarkedItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarkedInformation() {
        return markedInformation;
    }

    public MarkedItem markedInformation(String markedInformation) {
        this.markedInformation = markedInformation;
        return this;
    }

    public void setMarkedInformation(String markedInformation) {
        this.markedInformation = markedInformation;
    }

    public String getMarkedRules() {
        return markedRules;
    }

    public MarkedItem markedRules(String markedRules) {
        this.markedRules = markedRules;
        return this;
    }

    public void setMarkedRules(String markedRules) {
        this.markedRules = markedRules;
    }

    public String getEntreInfo() {
        return entreInfo;
    }

    public MarkedItem entreInfo(String entreInfo) {
        this.entreInfo = entreInfo;
        return this;
    }

    public void setEntreInfo(String entreInfo) {
        this.entreInfo = entreInfo;
    }

    public String getDateExtraInfo() {
        return dateExtraInfo;
    }

    public MarkedItem dateExtraInfo(String dateExtraInfo) {
        this.dateExtraInfo = dateExtraInfo;
        return this;
    }

    public void setDateExtraInfo(String dateExtraInfo) {
        this.dateExtraInfo = dateExtraInfo;
    }

    public String getAddress() {
        return address;
    }

    public MarkedItem address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public MarkedItem latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public MarkedItem longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean isEnableBooking() {
        return enableBooking;
    }

    public MarkedItem enableBooking(Boolean enableBooking) {
        this.enableBooking = enableBooking;
        return this;
    }

    public void setEnableBooking(Boolean enableBooking) {
        this.enableBooking = enableBooking;
    }

    public Set<DateInterval> getDateIntervals() {
        return dateIntervals;
    }

    public MarkedItem dateIntervals(Set<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
        return this;
    }

    public MarkedItem addDateInterval(DateInterval dateInterval) {
        this.dateIntervals.add(dateInterval);
        dateInterval.setMarkedItem(this);
        return this;
    }

    public MarkedItem removeDateInterval(DateInterval dateInterval) {
        this.dateIntervals.remove(dateInterval);
        dateInterval.setMarkedItem(null);
        return this;
    }

    public void setDateIntervals(Set<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarkedItem markedItem = (MarkedItem) o;
        if (markedItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), markedItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MarkedItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", markedInformation='" + getMarkedInformation() + "'" +
            ", markedRules='" + getMarkedRules() + "'" +
            ", entreInfo='" + getEntreInfo() + "'" +
            ", dateExtraInfo='" + getDateExtraInfo() + "'" +
            ", address='" + getAddress() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", enableBooking='" + isEnableBooking() + "'" +
            "}";
    }
}
