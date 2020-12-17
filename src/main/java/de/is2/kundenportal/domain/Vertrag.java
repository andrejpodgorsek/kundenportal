package de.is2.kundenportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import de.is2.kundenportal.domain.enumeration.Sparte;

import de.is2.kundenportal.domain.enumeration.Rhytmus;

/**
 * A Vertrag.
 */
@Entity
@Table(name = "vertrag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "vertrag")
public class Vertrag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "vsnr", nullable = false)
    private String vsnr;

    @Enumerated(EnumType.STRING)
    @Column(name = "sparte")
    private Sparte sparte;

    @Enumerated(EnumType.STRING)
    @Column(name = "zahlenrhytmus")
    private Rhytmus zahlenrhytmus;

    @Column(name = "antragsdatum")
    private Instant antragsdatum;

    @Column(name = "versicherungsbeginn")
    private Instant versicherungsbeginn;

    @Column(name = "iban")
    private String iban;

    @Column(name = "created")
    private Instant created;

    @ManyToOne
    @JsonIgnoreProperties(value = "vertrags", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVsnr() {
        return vsnr;
    }

    public Vertrag vsnr(String vsnr) {
        this.vsnr = vsnr;
        return this;
    }

    public void setVsnr(String vsnr) {
        this.vsnr = vsnr;
    }

    public Sparte getSparte() {
        return sparte;
    }

    public Vertrag sparte(Sparte sparte) {
        this.sparte = sparte;
        return this;
    }

    public void setSparte(Sparte sparte) {
        this.sparte = sparte;
    }

    public Rhytmus getZahlenrhytmus() {
        return zahlenrhytmus;
    }

    public Vertrag zahlenrhytmus(Rhytmus zahlenrhytmus) {
        this.zahlenrhytmus = zahlenrhytmus;
        return this;
    }

    public void setZahlenrhytmus(Rhytmus zahlenrhytmus) {
        this.zahlenrhytmus = zahlenrhytmus;
    }

    public Instant getAntragsdatum() {
        return antragsdatum;
    }

    public Vertrag antragsdatum(Instant antragsdatum) {
        this.antragsdatum = antragsdatum;
        return this;
    }

    public void setAntragsdatum(Instant antragsdatum) {
        this.antragsdatum = antragsdatum;
    }

    public Instant getVersicherungsbeginn() {
        return versicherungsbeginn;
    }

    public Vertrag versicherungsbeginn(Instant versicherungsbeginn) {
        this.versicherungsbeginn = versicherungsbeginn;
        return this;
    }

    public void setVersicherungsbeginn(Instant versicherungsbeginn) {
        this.versicherungsbeginn = versicherungsbeginn;
    }

    public String getIban() {
        return iban;
    }

    public Vertrag iban(String iban) {
        this.iban = iban;
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Instant getCreated() {
        return created;
    }

    public Vertrag created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public Vertrag user(User user) {
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
        if (!(o instanceof Vertrag)) {
            return false;
        }
        return id != null && id.equals(((Vertrag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vertrag{" +
            "id=" + getId() +
            ", vsnr='" + getVsnr() + "'" +
            ", sparte='" + getSparte() + "'" +
            ", zahlenrhytmus='" + getZahlenrhytmus() + "'" +
            ", antragsdatum='" + getAntragsdatum() + "'" +
            ", versicherungsbeginn='" + getVersicherungsbeginn() + "'" +
            ", iban='" + getIban() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
