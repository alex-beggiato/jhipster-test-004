package com.app.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A A.
 */
@Table("a")
public class A implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("uid")
    private UUID uid;

    @Column("ent_aa_acod")
    private String entAAAcod;

    @Column("ent_aa_adsc")
    private String entAAAdsc;

    @Transient
    @JsonIgnoreProperties(value = { "entAAAuid" }, allowSetters = true)
    private Set<B> uids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public A id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public A uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getEntAAAcod() {
        return this.entAAAcod;
    }

    public A entAAAcod(String entAAAcod) {
        this.setEntAAAcod(entAAAcod);
        return this;
    }

    public void setEntAAAcod(String entAAAcod) {
        this.entAAAcod = entAAAcod;
    }

    public String getEntAAAdsc() {
        return this.entAAAdsc;
    }

    public A entAAAdsc(String entAAAdsc) {
        this.setEntAAAdsc(entAAAdsc);
        return this;
    }

    public void setEntAAAdsc(String entAAAdsc) {
        this.entAAAdsc = entAAAdsc;
    }

    public Set<B> getUids() {
        return this.uids;
    }

    public void setUids(Set<B> bs) {
        if (this.uids != null) {
            this.uids.forEach(i -> i.setEntAAAuid(null));
        }
        if (bs != null) {
            bs.forEach(i -> i.setEntAAAuid(this));
        }
        this.uids = bs;
    }

    public A uids(Set<B> bs) {
        this.setUids(bs);
        return this;
    }

    public A addUid(B b) {
        this.uids.add(b);
        b.setEntAAAuid(this);
        return this;
    }

    public A removeUid(B b) {
        this.uids.remove(b);
        b.setEntAAAuid(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof A)) {
            return false;
        }
        return id != null && id.equals(((A) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "A{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", entAAAcod='" + getEntAAAcod() + "'" +
            ", entAAAdsc='" + getEntAAAdsc() + "'" +
            "}";
    }
}
