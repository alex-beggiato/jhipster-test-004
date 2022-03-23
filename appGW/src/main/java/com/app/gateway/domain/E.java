package com.app.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A E.
 */
@Table("e")
public class E implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("uid")
    private UUID uid;

    @Column("ent_cc_cuid")
    private Long entCCCuid;

    @Column("ent_ee_ecod")
    private String entEEEcod;

    @Column("ent_ee_edsc")
    private String entEEEdsc;

    @Transient
    @JsonIgnoreProperties(value = { "uids" }, allowSetters = true)
    private C entCCCuid;

    @Column("entcccuid_id")
    private Long entCCCuidId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public E id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public E uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public Long getEntCCCuid() {
        return this.entCCCuid;
    }

    public E entCCCuid(Long entCCCuid) {
        this.setEntCCCuid(entCCCuid);
        return this;
    }

    public void setEntCCCuid(Long entCCCuid) {
        this.entCCCuid = entCCCuid;
    }

    public String getEntEEEcod() {
        return this.entEEEcod;
    }

    public E entEEEcod(String entEEEcod) {
        this.setEntEEEcod(entEEEcod);
        return this;
    }

    public void setEntEEEcod(String entEEEcod) {
        this.entEEEcod = entEEEcod;
    }

    public String getEntEEEdsc() {
        return this.entEEEdsc;
    }

    public E entEEEdsc(String entEEEdsc) {
        this.setEntEEEdsc(entEEEdsc);
        return this;
    }

    public void setEntEEEdsc(String entEEEdsc) {
        this.entEEEdsc = entEEEdsc;
    }

    public C getEntCCCuid() {
        return this.entCCCuid;
    }

    public void setEntCCCuid(C c) {
        this.entCCCuid = c;
        this.entCCCuidId = c != null ? c.getId() : null;
    }

    public E entCCCuid(C c) {
        this.setEntCCCuid(c);
        return this;
    }

    public Long getEntCCCuidId() {
        return this.entCCCuidId;
    }

    public void setEntCCCuidId(Long c) {
        this.entCCCuidId = c;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof E)) {
            return false;
        }
        return id != null && id.equals(((E) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "E{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", entCCCuid=" + getEntCCCuid() +
            ", entEEEcod='" + getEntEEEcod() + "'" +
            ", entEEEdsc='" + getEntEEEdsc() + "'" +
            "}";
    }
}
