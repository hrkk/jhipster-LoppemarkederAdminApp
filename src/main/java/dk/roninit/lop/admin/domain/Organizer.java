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
 * A Organizer.
 */
@Entity
@Table(name = "organizer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "organizer")
public class Organizer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @NotNull
    @Size(min = 8, max = 8)
    @Column(name = "phone", length = 8, nullable = false)
    private String phone;

    @NotNull
    @Column(name = "enable_booking", nullable = false)
    private Boolean enableBooking;

    @OneToMany(mappedBy = "organizer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MarkedItem> markedItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Organizer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Organizer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Organizer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isEnableBooking() {
        return enableBooking;
    }

    public Organizer enableBooking(Boolean enableBooking) {
        this.enableBooking = enableBooking;
        return this;
    }

    public void setEnableBooking(Boolean enableBooking) {
        this.enableBooking = enableBooking;
    }

    public Set<MarkedItem> getMarkedItems() {
        return markedItems;
    }

    public Organizer markedItems(Set<MarkedItem> markedItems) {
        this.markedItems = markedItems;
        return this;
    }

    public Organizer addMarkedItem(MarkedItem markedItem) {
        this.markedItems.add(markedItem);
        markedItem.setOrganizer(this);
        return this;
    }

    public Organizer removeMarkedItem(MarkedItem markedItem) {
        this.markedItems.remove(markedItem);
        markedItem.setOrganizer(null);
        return this;
    }

    public void setMarkedItems(Set<MarkedItem> markedItems) {
        this.markedItems = markedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organizer organizer = (Organizer) o;
        if (organizer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Organizer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", enableBooking='" + isEnableBooking() + "'" +
            "}";
    }
}
