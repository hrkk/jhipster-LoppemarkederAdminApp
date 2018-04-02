package dk.roninit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
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
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @NotNull
    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Column(name = "enable_booking", nullable = false)
    private Boolean enableBooking;

    @ManyToOne
    private Organizer organizer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public Marked fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public Marked toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", enableBooking='" + isEnableBooking() + "'" +
            "}";
    }
}
