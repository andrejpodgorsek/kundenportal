package de.is2.kundenportal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import de.is2.kundenportal.domain.enumeration.Anrede;

/**
 * A Kunde.
 */
@Entity
@Table(name = "kunde")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "kunde")
public class Kunde implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "anrede", nullable = false)
    private Anrede anrede;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "strasse")
    private String strasse;

    @Column(name = "plzort")
    private String plzort;

    @Column(name = "telefonnr")
    private String telefonnr;

    @Column(name = "iban")
    private String iban;

    @Column(name = "created")
    private Instant created;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Anrede getAnrede() {
        return anrede;
    }

    public Kunde anrede(Anrede anrede) {
        this.anrede = anrede;
        return this;
    }

    public void setAnrede(Anrede anrede) {
        this.anrede = anrede;
    }

    public String getName() {
        return name;
    }

    public Kunde name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public Kunde vorname(String vorname) {
        this.vorname = vorname;
        return this;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getEmail() {
        return email;
    }

    public Kunde email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStrasse() {
        return strasse;
    }

    public Kunde strasse(String strasse) {
        this.strasse = strasse;
        return this;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPlzort() {
        return plzort;
    }

    public Kunde plzort(String plzort) {
        this.plzort = plzort;
        return this;
    }

    public void setPlzort(String plzort) {
        this.plzort = plzort;
    }

    public String getTelefonnr() {
        return telefonnr;
    }

    public Kunde telefonnr(String telefonnr) {
        this.telefonnr = telefonnr;
        return this;
    }

    public void setTelefonnr(String telefonnr) {
        this.telefonnr = telefonnr;
    }

    public String getIban() {
        return iban;
    }

    public Kunde iban(String iban) {
        this.iban = iban;
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Instant getCreated() {
        return created;
    }

    public Kunde created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public Kunde user(User user) {
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
        if (!(o instanceof Kunde)) {
            return false;
        }
        return id != null && id.equals(((Kunde) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Kunde{" +
            "id=" + getId() +
            ", anrede='" + getAnrede() + "'" +
            ", name='" + getName() + "'" +
            ", vorname='" + getVorname() + "'" +
            ", email='" + getEmail() + "'" +
            ", strasse='" + getStrasse() + "'" +
            ", plzort='" + getPlzort() + "'" +
            ", telefonnr='" + getTelefonnr() + "'" +
            ", iban='" + getIban() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
