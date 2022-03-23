package com.app.sql1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A B.
 */
@Entity
@Table(name = "b")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class B implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "uid", length = 36)
    private UUID uid;

    @Column(name = "ent_aa_auid")
    private Long entAAAuid;

    @Column(name = "ent_bb_bcod")
    private String entBBBcod;

    @Column(name = "ent_bb_bdsc")
    private String entBBBdsc;

    @ManyToOne
    @JsonIgnoreProperties(value = { "uids" }, allowSetters = true)
    private A entAAAuid;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public B id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public B uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public Long getEntAAAuid() {
        return this.entAAAuid;
    }

    public B entAAAuid(Long entAAAuid) {
        this.setEntAAAuid(entAAAuid);
        return this;
    }

    public void setEntAAAuid(Long entAAAuid) {
        this.entAAAuid = entAAAuid;
    }

    public String getEntBBBcod() {
        return this.entBBBcod;
    }

    public B entBBBcod(String entBBBcod) {
        this.setEntBBBcod(entBBBcod);
        return this;
    }

    public void setEntBBBcod(String entBBBcod) {
        this.entBBBcod = entBBBcod;
    }

    public String getEntBBBdsc() {
        return this.entBBBdsc;
    }

    public B entBBBdsc(String entBBBdsc) {
        this.setEntBBBdsc(entBBBdsc);
        return this;
    }

    public void setEntBBBdsc(String entBBBdsc) {
        this.entBBBdsc = entBBBdsc;
    }

    public A getEntAAAuid() {
        return this.entAAAuid;
    }

    public void setEntAAAuid(A a) {
        this.entAAAuid = a;
    }

    public B entAAAuid(A a) {
        this.setEntAAAuid(a);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof B)) {
            return false;
        }
        return id != null && id.equals(((B) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "B{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", entAAAuid=" + getEntAAAuid() +
            ", entBBBcod='" + getEntBBBcod() + "'" +
            ", entBBBdsc='" + getEntBBBdsc() + "'" +
            "}";
    }
}
