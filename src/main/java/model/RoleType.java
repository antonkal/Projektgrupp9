package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
    
    @Basic(optional = false)
    @Column(name="name", nullable = false, unique = true)
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roletype")
    private Collection<Person> persons;

    public RoleType() {
    }

    public Integer getId() {
        return id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
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
