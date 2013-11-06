/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.invisiblefriend.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
@Embeddable
public class EsAmigoDePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "NUMERO_ES_AMIGO")
    private String numeroEsAmigo;
    @Basic(optional = false)
    @Column(name = "NUM_JUEGO_ES_AMIGO")
    private String numJuegoEsAmigo;
    @Basic(optional = false)
    @Column(name = "NUMERO_AMIGO_DE")
    private String numeroAmigoDe;
    @Basic(optional = false)
    @Column(name = "NUM_JUEGO_AMIGO_DE")
    private String numJuegoAmigoDe;

    public EsAmigoDePK() {
    }

    public EsAmigoDePK(String numeroEsAmigo, String numJuegoEsAmigo, String numeroAmigoDe, String numJuegoAmigoDe) {
        this.numeroEsAmigo = numeroEsAmigo;
        this.numJuegoEsAmigo = numJuegoEsAmigo;
        this.numeroAmigoDe = numeroAmigoDe;
        this.numJuegoAmigoDe = numJuegoAmigoDe;
    }

    public String getNumeroEsAmigo() {
        return numeroEsAmigo;
    }

    public void setNumeroEsAmigo(String numeroEsAmigo) {
        this.numeroEsAmigo = numeroEsAmigo;
    }

    public String getNumJuegoEsAmigo() {
        return numJuegoEsAmigo;
    }

    public void setNumJuegoEsAmigo(String numJuegoEsAmigo) {
        this.numJuegoEsAmigo = numJuegoEsAmigo;
    }

    public String getNumeroAmigoDe() {
        return numeroAmigoDe;
    }

    public void setNumeroAmigoDe(String numeroAmigoDe) {
        this.numeroAmigoDe = numeroAmigoDe;
    }

    public String getNumJuegoAmigoDe() {
        return numJuegoAmigoDe;
    }

    public void setNumJuegoAmigoDe(String numJuegoAmigoDe) {
        this.numJuegoAmigoDe = numJuegoAmigoDe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroEsAmigo != null ? numeroEsAmigo.hashCode() : 0);
        hash += (numJuegoEsAmigo != null ? numJuegoEsAmigo.hashCode() : 0);
        hash += (numeroAmigoDe != null ? numeroAmigoDe.hashCode() : 0);
        hash += (numJuegoAmigoDe != null ? numJuegoAmigoDe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EsAmigoDePK)) {
            return false;
        }
        EsAmigoDePK other = (EsAmigoDePK) object;
        if ((this.numeroEsAmigo == null && other.numeroEsAmigo != null) || (this.numeroEsAmigo != null && !this.numeroEsAmigo.equals(other.numeroEsAmigo))) {
            return false;
        }
        if ((this.numJuegoEsAmigo == null && other.numJuegoEsAmigo != null) || (this.numJuegoEsAmigo != null && !this.numJuegoEsAmigo.equals(other.numJuegoEsAmigo))) {
            return false;
        }
        if ((this.numeroAmigoDe == null && other.numeroAmigoDe != null) || (this.numeroAmigoDe != null && !this.numeroAmigoDe.equals(other.numeroAmigoDe))) {
            return false;
        }
        if ((this.numJuegoAmigoDe == null && other.numJuegoAmigoDe != null) || (this.numJuegoAmigoDe != null && !this.numJuegoAmigoDe.equals(other.numJuegoAmigoDe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.icesi.entities.EsAmigoDePK[ numeroEsAmigo=" + numeroEsAmigo + ", numJuegoEsAmigo=" + numJuegoEsAmigo + ", numeroAmigoDe=" + numeroAmigoDe + ", numJuegoAmigoDe=" + numJuegoAmigoDe + " ]";
    }
    
}
