package dk.roninit.lopadminapp.domain;

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
 * A Marked.
 */
@Entity
@Table(name = "marked")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "marked")
public class Marked implements Serializable {

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
    @Size(min = 2, max = 256)
    @Column(name = "address", length = 256, nullable = false)
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

    @OneToMany(mappedBy = "marked")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DateInterval> dateIntervals = new HashSet<>();

    @ManyToOne
    private Organizer organizer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Marked name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarkedInformation() {
        return markedInformation;
    }

    public Marked markedInformation(String markedInformation) {
        this.markedInformation = markedInformation;
        return this;
    }

    public void setMarkedInformation(String markedInformation) {
        this.markedInformation = markedInformation;
    }

    public String getMarkedRules() {
        return markedRules;
    }

    public Marked markedRules(String markedRules) {
        this.markedRules = markedRules;
        return this;
    }

    public void setMarkedRules(String markedRules) {
        this.markedRules = markedRules;
    }

    public String getEntreInfo() {
        return entreInfo;
    }

    public Marked entreInfo(String entreInfo) {
        this.entreInfo = entreInfo;
        return this;
    }

    public void setEntreInfo(String entreInfo) {
        this.entreInfo = entreInfo;
    }

    public String getDateExtraInfo() {
        return dateExtraInfo;
    }

    public Marked dateExtraInfo(String dateExtraInfo) {
        this.dateExtraInfo = dateExtraInfo;
        return this;
    }

    public void setDateExtraInfo(String dateExtraInfo) {
        this.dateExtraInfo = dateExtraInfo;
    }

    public String getAddress() {
        return address;
    }

    public Marked address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Marked latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Marked longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean isEnableBooking() {
        return enableBooking;
    }

    public Marked enableBooking(Boolean enableBooking) {
        this.enableBooking = enableBooking;
        return this;
    }

    public void setEnableBooking(Boolean enableBooking) {
        this.enableBooking = enableBooking;
    }

    public Set<DateInterval> getDateIntervals() {
        return dateIntervals;
    }

    public Marked dateIntervals(Set<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
        return this;
    }

    public Marked addDateInterval(DateInterval dateInterval) {
        this.dateIntervals.add(dateInterval);
        dateInterval.setMarked(this);
        return this;
    }

    public Marked removeDateInterval(DateInterval dateInterval) {
        this.dateIntervals.remove(dateInterval);
        dateInterval.setMarked(null);
        return this;
    }

    public void setDateIntervals(Set<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public Marked organizer(Organizer organizer) {
        this.organizer = organizer;
        return this;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Marked marked = (Marked) o;
        if (marked.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), marked.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Marked{" +
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
