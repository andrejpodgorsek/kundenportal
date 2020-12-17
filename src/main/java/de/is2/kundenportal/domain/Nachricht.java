package de.is2.kundenportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import de.is2.kundenportal.domain.enumeration.NachrichtStatus;

/**
 * A Nachricht.
 */
@Entity
@Table(name = "nachricht")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "nachricht")
public class Nachricht implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "an", nullable = false)
    private String an;

    @NotNull
    @Column(name = "betreff", nullable = false)
    private String betreff;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @Lob
    @Column(name = "anhang")
    private byte[] anhang;

    @Column(name = "anhang_content_type")
    private String anhangContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NachrichtStatus status;

    @Column(name = "created")
    private Instant created;

    @ManyToOne
    @JsonIgnoreProperties(value = "nachrichts", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAn() {
        return an;
    }

    public Nachricht an(String an) {
        this.an = an;
        return this;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getBetreff() {
        return betreff;
    }

    public Nachricht betreff(String betreff) {
        this.betreff = betreff;
        return this;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getText() {
        return text;
    }

    public Nachricht text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getAnhang() {
        return anhang;
    }

    public Nachricht anhang(byte[] anhang) {
        this.anhang = anhang;
        return this;
    }

    public void setAnhang(byte[] anhang) {
        this.anhang = anhang;
    }

    public String getAnhangContentType() {
        return anhangContentType;
    }

    public Nachricht anhangContentType(String anhangContentType) {
        this.anhangContentType = anhangContentType;
        return this;
    }

    public void setAnhangContentType(String anhangContentType) {
        this.anhangContentType = anhangContentType;
    }

    public NachrichtStatus getStatus() {
        return status;
    }

    public Nachricht status(NachrichtStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(NachrichtStatus status) {
        this.status = status;
    }

    public Instant getCreated() {
        return created;
    }

    public Nachricht created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public Nachricht user(User user) {
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
        if (!(o instanceof Nachricht)) {
            return false;
        }
        return id != null && id.equals(((Nachricht) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nachricht{" +
            "id=" + getId() +
            ", an='" + getAn() + "'" +
            ", betreff='" + getBetreff() + "'" +
            ", text='" + getText() + "'" +
            ", anhang='" + getAnhang() + "'" +
            ", anhangContentType='" + getAnhangContentType() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
