package dk.roninit.lopadminapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
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

    @Column(name = "name")
    private String name;

    @Column(name = "markedInformation")
    private String markedInformation;

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

    public String getMarkedInformation() {
        return markedInformation;
    }

    public void setMarkedInformation(String markedInformation) {
        this.markedInformation = markedInformation;
    }

    public void setName(String name) {
        this.name = name;
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
            "}";
    }
}
