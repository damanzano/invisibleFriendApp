/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
@Entity
@Table(name = "JUEGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Juego.findAll", query = "SELECT j FROM Juego j"),
    @NamedQuery(name = "Juego.findByNumeroId", query = "SELECT j FROM Juego j WHERE j.numeroId = :numeroId"),
    @NamedQuery(name = "Juego.findByDescripcion", query = "SELECT j FROM Juego j WHERE j.descripcion = :descripcion"),
    @NamedQuery(name = "Juego.findByFechaCreacion", query = "SELECT j FROM Juego j WHERE j.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Juego.findByFechaInicial", query = "SELECT j FROM Juego j WHERE j.fechaInicial = :fechaInicial"),
    @NamedQuery(name = "Juego.findByFechaFinal", query = "SELECT j FROM Juego j WHERE j.fechaFinal = :fechaFinal"),
    @NamedQuery(name = "Juego.count", query = "SELECT count (j.numeroId) FROM Juego j ")
})
public class Juego implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NUMERO_ID")
    private String numeroId;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "FECHA_INICIAL")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Basic(optional = false)
    @Column(name = "FECHA_FINAL")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "juego")
    /*@JoinTable(
            name = "PARTICIPANTES",
            joinColumns = {
                @JoinColumn(name = "NUMERO_JUEGO", referencedColumnName = "NUMEROID")}
    )*/
    private Collection<Participantes> participantesCollection;

    public Juego() {
    }

    public Juego(String numeroId) {
        this.numeroId = numeroId;
    }

    public Juego(String numeroId, String descripcion, Date fechaCreacion, Date fechaInicial, Date fechaFinal) {
        this.numeroId = numeroId;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    public String getNumeroId() {
        return numeroId;
    }

    public void setNumeroId(String numeroId) {
        this.numeroId = numeroId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    //@XmlTransient
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
        if (!(object instanceof Juego)) {
            return false;
        }
        Juego other = (Juego) object;
        if ((this.numeroId == null && other.numeroId != null) || (this.numeroId != null && !this.numeroId.equals(other.numeroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.icesi.entities.Juego[ numeroId=" + numeroId + " ]";
    }

}
