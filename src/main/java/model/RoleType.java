package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class RoleType implements Serializable 
{    
    @Id
    @SequenceGenerator(name = "roleTypeIdSeq", sequenceName = "ROLETYPE_ID_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleTypeIdSeq")
    @Column(name = "id")
    private Integer id;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roletype")
    private Collection<Person> persons;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roletype")
    private Collection<RoleType_Localized> names;

    public RoleType() {
    }

    public Integer getId() {
        return id;
    }
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoleType)) {
            return false;
        }
        RoleType other = (RoleType) object;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "model.RoleType[ id=" + id + " ]";
    }
}