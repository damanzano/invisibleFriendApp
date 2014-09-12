/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.invisiblefriend.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
@Entity
@Table(name = "ES_AMIGO_DE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EsAmigoDe.findAll", query = "SELECT e FROM EsAmigoDe e"),
    @NamedQuery(name = "EsAmigoDe.findByNumeroEsAmigo", query = "SELECT e FROM EsAmigoDe e WHERE e.esAmigoDePK.numeroEsAmigo = :numeroEsAmigo"),
    @NamedQuery(name = "EsAmigoDe.findByNumJuegoEsAmigo", query = "SELECT e FROM EsAmigoDe e WHERE e.esAmigoDePK.numJuegoEsAmigo = :numJuegoEsAmigo"),
    @NamedQuery(name = "EsAmigoDe.findByNumeroAmigoDe", query = "SELECT e FROM EsAmigoDe e WHERE e.esAmigoDePK.numeroAmigoDe = :numeroAmigoDe"),
    @NamedQuery(name = "EsAmigoDe.findByNumJuegoAmigoDe", query = "SELECT e FROM EsAmigoDe e WHERE e.esAmigoDePK.numJuegoAmigoDe = :numJuegoAmigoDe"),
    @NamedQuery(name = "EsAmigoDe.findByEnviado", query = "SELECT e FROM EsAmigoDe e WHERE e.enviado = :enviado")})
public class EsAmigoDe implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EsAmigoDePK esAmigoDePK;
    @Basic(optional = false)
    @Column(name = "ENVIADO")
    private String enviado;
    @JoinColumns({
        @JoinColumn(name = "NUMERO_ES_AMIGO", referencedColumnName = "NUMERO_JUEGO", insertable = false, updatable = false),
        @JoinColumn(name = "NUM_JUEGO_ES_AMIGO", referencedColumnName = "NUMERO_PERSONA", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Participantes participantes;
    @JoinColumns({
        @JoinColumn(name = "NUMERO_AMIGO_DE", referencedColumnName = "NUMERO_JUEGO", insertable = false, updatable = false),
        @JoinColumn(name = "NUM_JUEGO_AMIGO_DE", referencedColumnName = "NUMERO_PERSONA", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Participantes participantes1;

    public EsAmigoDe() {
    }

    public EsAmigoDe(EsAmigoDePK esAmigoDePK) {
        this.esAmigoDePK = esAmigoDePK;
    }

    public EsAmigoDe(EsAmigoDePK esAmigoDePK, String enviado) {
        this.esAmigoDePK = esAmigoDePK;
        this.enviado = enviado;
    }

    public EsAmigoDe(String numeroEsAmigo, String numJuegoEsAmigo, String numeroAmigoDe, String numJuegoAmigoDe) {
        this.esAmigoDePK = new EsAmigoDePK(numeroEsAmigo, numJuegoEsAmigo, numeroAmigoDe, numJuegoAmigoDe);
    }

    public EsAmigoDePK getEsAmigoDePK() {
        return esAmigoDePK;
    }

    public void setEsAmigoDePK(EsAmigoDePK esAmigoDePK) {
        this.esAmigoDePK = esAmigoDePK;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }

    public Participantes getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Participantes participantes) {
        this.participantes = participantes;
    }

    public Participantes getParticipantes1() {
        return participantes1;
    }

    public void setParticipantes1(Participantes participantes1) {
        this.participantes1 = participantes1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (esAmigoDePK != null ? esAmigoDePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EsAmigoDe)) {
            return false;
        }
        EsAmigoDe other = (EsAmigoDe) object;
        if ((this.esAmigoDePK == null && other.esAmigoDePK != null) || (this.esAmigoDePK != null && !this.esAmigoDePK.equals(other.esAmigoDePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.icesi.entities.EsAmigoDe[ esAmigoDePK=" + esAmigoDePK + " ]";
    }
    
}
