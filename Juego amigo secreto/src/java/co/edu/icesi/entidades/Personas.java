/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 14620701
 */
@Entity
@Table(name = "PERSONAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personas.findAll", query = "SELECT p FROM Personas p"),
    @NamedQuery(name = "Personas.findByNumeroId", query = "SELECT p FROM Personas p WHERE p.numeroId = :numeroId"),
    @NamedQuery(name = "Personas.findByNombre", query = "SELECT p FROM Personas p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Personas.findByApellidos", query = "SELECT p FROM Personas p WHERE p.apellidos = :apellidos"),
    @NamedQuery(name = "Personas.findByCorreoElectronico", query = "SELECT p FROM Personas p WHERE p.correoElectronico = :correoElectronico"),
    @NamedQuery(name = "Personas.findByFoto", query = "SELECT p FROM Personas p WHERE p.foto = :foto"),
    @NamedQuery(name = "Personas.findByUbicacion", query = "SELECT p FROM Personas p WHERE p.ubicacion = :ubicacion"),
    @NamedQuery(name = "Personas.findBySexo", query = "SELECT p FROM Personas p WHERE p.sexo = :sexo")})
public class Personas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NUMERO_ID")
    private String numeroId;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;
    @Column(name = "FOTO")
    private String foto;
    @Basic(optional = false)
    @Column(name = "UBICACION")
    private String ubicacion;
    @Basic(optional = false)
    @Column(name = "SEXO")
    private String sexo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personas")
    private Collection<Participantes> participantesCollection;

    public Personas() {
    }

    public Personas(String numeroId) {
        this.numeroId = numeroId;
    }

    public Personas(String numeroId, String nombre, String apellidos, String correoElectronico, String ubicacion, String sexo) {
        this.numeroId = numeroId;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.ubicacion = ubicacion;
        this.sexo = sexo;
    }

    public String getNumeroId() {
        return numeroId;
    }

    public void setNumeroId(String numeroId) {
        this.numeroId = numeroId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @XmlTransient
    public Collection<Participantes> getParticipantesCollection() {
        return participantesCollection;
    }

    public void setParticipantesCollection(Collection<Participantes> participantesCollection) {
        this.participantesCollection = participantesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroId != null ? numeroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personas)) {
            return false;
        }
        Personas other = (Personas) object;
        if ((this.numeroId == null && other.numeroId != null) || (this.numeroId != null && !this.numeroId.equals(other.numeroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.icesi.entitys.Personas[ numeroId=" + numeroId + " ]";
    }
    
}
