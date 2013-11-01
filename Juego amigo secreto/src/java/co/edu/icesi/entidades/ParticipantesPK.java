/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author 14620701
 */
@Embeddable
public class ParticipantesPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "NUMERO_JUEGO")
    private String numeroJuego;
    @Basic(optional = false)
    @Column(name = "NUMERO_PERSONA")
    private String numeroPersona;

    public ParticipantesPK() {
    }

    public ParticipantesPK(String numeroJuego, String numeroPersona) {
        this.numeroJuego = numeroJuego;
        this.numeroPersona = numeroPersona;
    }

    public String getNumeroJuego() {
        return numeroJuego;
    }

    public void setNumeroJuego(String numeroJuego) {
        this.numeroJuego = numeroJuego;
    }

    public String getNumeroPersona() {
        return numeroPersona;
    }

    public void setNumeroPersona(String numeroPersona) {
        this.numeroPersona = numeroPersona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroJuego != null ? numeroJuego.hashCode() : 0);
        hash += (numeroPersona != null ? numeroPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParticipantesPK)) {
            return false;
        }
        ParticipantesPK other = (ParticipantesPK) object;
        if ((this.numeroJuego == null && other.numeroJuego != null) || (this.numeroJuego != null && !this.numeroJuego.equals(other.numeroJuego))) {
            return false;
        }
        if ((this.numeroPersona == null && other.numeroPersona != null) || (this.numeroPersona != null && !this.numeroPersona.equals(other.numeroPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.icesi.entitys.ParticipantesPK[ numeroJuego=" + numeroJuego + ", numeroPersona=" + numeroPersona + " ]";
    }
    
}
