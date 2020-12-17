package de.is2.kundenportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Schade.
 */
@Entity
@Table(name = "schade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "schade")
public class Schade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "vsnr", nullable = false)
    private String vsnr;

    @Column(name = "text")
    private String text;

    @Lob
    @Column(name = "anhang")
    private byte[] anhang;

    @Column(name = "anhang_content_type")
    private String anhangContentType;

    @Column(name = "created")
    private Instant created;

    @ManyToOne
    @JsonIgnoreProperties(value = "schades", allowSetters = true)
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

    public Schade vsnr(String vsnr) {
        this.vsnr = vsnr;
        return this;
    }

    public void setVsnr(String vsnr) {
        this.vsnr = vsnr;
    }

    public String getText() {
        return text;
    }

    public Schade text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getAnhang() {
        return anhang;
    }

    public Schade anhang(byte[] anhang) {
        this.anhang = anhang;
        return this;
    }

    public void setAnhang(byte[] anhang) {
        this.anhang = anhang;
    }

    public String getAnhangContentType() {
        return anhangContentType;
    }

    public Schade anhangContentType(String anhangContentType) {
        this.anhangContentType = anhangContentType;
        return this;
    }

    public void setAnhangContentType(String anhangContentType) {
        this.anhangContentType = anhangContentType;
    }

    public Instant getCreated() {
        return created;
    }

    public Schade created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public Schade user(User user) {
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
        if (!(o instanceof Schade)) {
            return false;
        }
        return id != null && id.equals(((Schade) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Schade{" +
            "id=" + getId() +
            ", vsnr='" + getVsnr() + "'" +
            ", text='" + getText() + "'" +
            ", anhang='" + getAnhang() + "'" +
            ", anhangContentType='" + getAnhangContentType() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
