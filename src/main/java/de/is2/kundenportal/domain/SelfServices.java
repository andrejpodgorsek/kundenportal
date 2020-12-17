package de.is2.kundenportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import de.is2.kundenportal.domain.enumeration.ServicesTyp;

import de.is2.kundenportal.domain.enumeration.ServicesStatus;

/**
 * A SelfServices.
 */
@Entity
@Table(name = "self_services")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "selfservices")
public class SelfServices implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service_typ", nullable = false)
    private ServicesTyp serviceTyp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ServicesStatus status;

    @Column(name = "text")
    private String text;

    @Lob
    @Column(name = "datei")
    private byte[] datei;

    @Column(name = "datei_content_type")
    private String dateiContentType;

    @Column(name = "created")
    private Instant created;

    @ManyToOne
    @JsonIgnoreProperties(value = "selfServices", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicesTyp getServiceTyp() {
        return serviceTyp;
    }

    public SelfServices serviceTyp(ServicesTyp serviceTyp) {
        this.serviceTyp = serviceTyp;
        return this;
    }

    public void setServiceTyp(ServicesTyp serviceTyp) {
        this.serviceTyp = serviceTyp;
    }

    public ServicesStatus getStatus() {
        return status;
    }

    public SelfServices status(ServicesStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ServicesStatus status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public SelfServices text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getDatei() {
        return datei;
    }

    public SelfServices datei(byte[] datei) {
        this.datei = datei;
        return this;
    }

    public void setDatei(byte[] datei) {
        this.datei = datei;
    }

    public String getDateiContentType() {
        return dateiContentType;
    }

    public SelfServices dateiContentType(String dateiContentType) {
        this.dateiContentType = dateiContentType;
        return this;
    }

    public void setDateiContentType(String dateiContentType) {
        this.dateiContentType = dateiContentType;
    }

    public Instant getCreated() {
        return created;
    }

    public SelfServices created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public SelfServices user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SelfServices)) {
            return false;
        }
        return id != null && id.equals(((SelfServices) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SelfServices{" +
            "id=" + getId() +
            ", serviceTyp='" + getServiceTyp() + "'" +
            ", status='" + getStatus() + "'" +
            ", text='" + getText() + "'" +
            ", datei='" + getDatei() + "'" +
            ", dateiContentType='" + getDateiContentType() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
