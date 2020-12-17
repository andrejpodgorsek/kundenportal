package de.is2.kundenportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import de.is2.kundenportal.domain.enumeration.DokumentStatus;

/**
 * A Dokument.
 */
@Entity
@Table(name = "dokument")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dokument")
public class Dokument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nummer", nullable = false)
    private String nummer;

    @Enumerated(EnumType.STRING)
    @Column(name = "dokument")
    private DokumentStatus dokument;

    @Column(name = "text")
    private String text;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    @Column(name = "created")
    private Instant created;

    @ManyToOne
    @JsonIgnoreProperties(value = "dokuments", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNummer() {
        return nummer;
    }

    public Dokument nummer(String nummer) {
        this.nummer = nummer;
        return this;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public DokumentStatus getDokument() {
        return dokument;
    }

    public Dokument dokument(DokumentStatus dokument) {
        this.dokument = dokument;
        return this;
    }

    public void setDokument(DokumentStatus dokument) {
        this.dokument = dokument;
    }

    public String getText() {
        return text;
    }

    public Dokument text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getData() {
        return data;
    }

    public Dokument data(byte[] data) {
        this.data = data;
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public Dokument dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public Instant getCreated() {
        return created;
    }

    public Dokument created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public Dokument user(User user) {
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
        if (!(o instanceof Dokument)) {
            return false;
        }
        return id != null && id.equals(((Dokument) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dokument{" +
            "id=" + getId() +
            ", nummer='" + getNummer() + "'" +
            ", dokument='" + getDokument() + "'" +
            ", text='" + getText() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
