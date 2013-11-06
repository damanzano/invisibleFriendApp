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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
@Entity
@Table(name = "PARTICIPANTES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Participantes.findAll", query = "SELECT p FROM Participantes p"),
    @NamedQuery(name = "Participantes.findByNumeroJuego", query = "SELECT p FROM Participantes p WHERE p.participantesPK.numeroJuego = :numeroJuego"),
    @NamedQuery(name = "Participantes.findByNumeroPersona", query = "SELECT p FROM Participantes p WHERE p.participantesPK.numeroPersona = :numeroPersona"),
    @NamedQuery(name = "Participantes.findByFechaInscripcion", query = "SELECT p FROM Participantes p WHERE p.fechaInscripcion = :fechaInscripcion")})
public class Participantes implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ParticipantesPK participantesPK;
    @Basic(optional = false)
    @Column(name = "FECHA_INSCRIPCION")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    @JoinColumn(name = "NUMERO_PERSONA", referencedColumnName = "NUMERO_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personas personas;
    @JoinColumn(name = "NUMERO_JUEGO", referencedColumnName = "NUMERO_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Juego juego;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participantes")
    private Collection<EsAmigoDe> esAmigoDeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participantes1")
    private Collection<EsAmigoDe> esAmigoDeCollection1;

    public Participantes() {
    }

    public Participantes(ParticipantesPK participantesPK) {
        this.participantesPK = participantesPK;
    }

    public Participantes(ParticipantesPK participantesPK, Date fechaInscripcion) {
        this.participantesPK = participantesPK;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Participantes(String numeroJuego, String numeroPersona) {
        this.participantesPK = new ParticipantesPK(numeroJuego, numeroPersona);
    }

    public ParticipantesPK getParticipantesPK() {
        return participantesPK;
    }

    public void setParticipantesPK(ParticipantesPK participantesPK) {
        this.participantesPK = participantesPK;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Personas getPersonas() {
        return personas;
    }

    public void setPersonas(Personas personas) {
        this.personas = personas;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    @XmlTransient
    public Collection<EsAmigoDe> getEsAmigoDeCollection() {
        return esAmigoDeCollection;
    }

    public void setEsAmigoDeCollection(Collection<EsAmigoDe> esAmigoDeCollection) {
        this.esAmigoDeCollection = esAmigoDeCollection;
    }

    @XmlTransient
    public Collection<EsAmigoDe> getEsAmigoDeCollection1() {
        return esAmigoDeCollection1;
    }

    public void setEsAmigoDeCollection1(Collection<EsAmigoDe> esAmigoDeCollection1) {
        this.esAmigoDeCollection1 = esAmigoDeCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (participantesPK != null ? participantesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Participantes)) {
            return false;
        }
        Participantes other = (Participantes) object;
        if ((this.participantesPK == null && other.participantesPK != null) || (this.participantesPK != null && !this.participantesPK.equals(other.participantesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.icesi.entities.Participantes[ participantesPK=" + participantesPK + " ]";
    }
    
}
