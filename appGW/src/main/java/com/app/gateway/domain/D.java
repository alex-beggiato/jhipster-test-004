package com.app.gateway.domain;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A D.
 */
@Table("d")
public class D implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("uid")
    private UUID uid;

    @Column("ent_dd_dcod")
    private String entDDDcod;

    @Column("ent_dd_ddsc")
    private String entDDDdsc;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public D id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public D uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getEntDDDcod() {
        return this.entDDDcod;
    }

    public D entDDDcod(String entDDDcod) {
        this.setEntDDDcod(entDDDcod);
        return this;
    }

    public void setEntDDDcod(String entDDDcod) {
        this.entDDDcod = entDDDcod;
    }

    public String getEntDDDdsc() {
        return this.entDDDdsc;
    }

    public D entDDDdsc(String entDDDdsc) {
        this.setEntDDDdsc(entDDDdsc);
        return this;
    }

    public void setEntDDDdsc(String entDDDdsc) {
        this.entDDDdsc = entDDDdsc;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof D)) {
            return false;
        }
        return id != null && id.equals(((D) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "D{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", entDDDcod='" + getEntDDDcod() + "'" +
            ", entDDDdsc='" + getEntDDDdsc() + "'" +
            "}";
    }
}
