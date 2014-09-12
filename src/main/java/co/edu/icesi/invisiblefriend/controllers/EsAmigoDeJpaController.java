/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.invisiblefriend.controllers;

import co.edu.icesi.controllers.invisiblefriend.exceptions.NonexistentEntityException;
import co.edu.icesi.controllers.invisiblefriend.exceptions.PreexistingEntityException;
import co.edu.icesi.invisiblefriend.entities.EsAmigoDe;
import co.edu.icesi.invisiblefriend.entities.EsAmigoDePK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.icesi.invisiblefriend.entities.Participantes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
public class EsAmigoDeJpaController implements Serializable {

    public EsAmigoDeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EsAmigoDe esAmigoDe) throws PreexistingEntityException, Exception {
        if (esAmigoDe.getEsAmigoDePK() == null) {
            esAmigoDe.setEsAmigoDePK(new EsAmigoDePK());
        }
        esAmigoDe.getEsAmigoDePK().setNumJuegoAmigoDe(esAmigoDe.getParticipantes1().getParticipantesPK().getNumeroPersona());
        esAmigoDe.getEsAmigoDePK().setNumJuegoEsAmigo(esAmigoDe.getParticipantes().getParticipantesPK().getNumeroPersona());
        esAmigoDe.getEsAmigoDePK().setNumeroEsAmigo(esAmigoDe.getParticipantes().getParticipantesPK().getNumeroJuego());
        esAmigoDe.getEsAmigoDePK().setNumeroAmigoDe(esAmigoDe.getParticipantes1().getParticipantesPK().getNumeroJuego());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participantes participantes = esAmigoDe.getParticipantes();
            if (participantes != null) {
                participantes = em.getReference(participantes.getClass(), participantes.getParticipantesPK());
                esAmigoDe.setParticipantes(participantes);
            }
            Participantes participantes1 = esAmigoDe.getParticipantes1();
            if (participantes1 != null) {
                participantes1 = em.getReference(participantes1.getClass(), participantes1.getParticipantesPK());
                esAmigoDe.setParticipantes1(participantes1);
            }
            em.persist(esAmigoDe);
            if (participantes != null) {
                participantes.getEsAmigoDeCollection().add(esAmigoDe);
                participantes = em.merge(participantes);
            }
            if (participantes1 != null) {
                participantes1.getEsAmigoDeCollection().add(esAmigoDe);
                participantes1 = em.merge(participantes1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEsAmigoDe(esAmigoDe.getEsAmigoDePK()) != null) {
                throw new PreexistingEntityException("EsAmigoDe " + esAmigoDe + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EsAmigoDe esAmigoDe) throws NonexistentEntityException, Exception {
        esAmigoDe.getEsAmigoDePK().setNumJuegoAmigoDe(esAmigoDe.getParticipantes1().getParticipantesPK().getNumeroPersona());
        esAmigoDe.getEsAmigoDePK().setNumJuegoEsAmigo(esAmigoDe.getParticipantes().getParticipantesPK().getNumeroPersona());
        esAmigoDe.getEsAmigoDePK().setNumeroEsAmigo(esAmigoDe.getParticipantes().getParticipantesPK().getNumeroJuego());
        esAmigoDe.getEsAmigoDePK().setNumeroAmigoDe(esAmigoDe.getParticipantes1().getParticipantesPK().getNumeroJuego());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EsAmigoDe persistentEsAmigoDe = em.find(EsAmigoDe.class, esAmigoDe.getEsAmigoDePK());
            Participantes participantesOld = persistentEsAmigoDe.getParticipantes();
            Participantes participantesNew = esAmigoDe.getParticipantes();
            Participantes participantes1Old = persistentEsAmigoDe.getParticipantes1();
            Participantes participantes1New = esAmigoDe.getParticipantes1();
            if (participantesNew != null) {
                participantesNew = em.getReference(participantesNew.getClass(), participantesNew.getParticipantesPK());
                esAmigoDe.setParticipantes(participantesNew);
            }
            if (participantes1New != null) {
                participantes1New = em.getReference(participantes1New.getClass(), participantes1New.getParticipantesPK());
                esAmigoDe.setParticipantes1(participantes1New);
            }
            esAmigoDe = em.merge(esAmigoDe);
            if (participantesOld != null && !participantesOld.equals(participantesNew)) {
                participantesOld.getEsAmigoDeCollection().remove(esAmigoDe);
                participantesOld = em.merge(participantesOld);
            }
            if (participantesNew != null && !participantesNew.equals(participantesOld)) {
                participantesNew.getEsAmigoDeCollection().add(esAmigoDe);
                participantesNew = em.merge(participantesNew);
            }
            if (participantes1Old != null && !participantes1Old.equals(participantes1New)) {
                participantes1Old.getEsAmigoDeCollection().remove(esAmigoDe);
                participantes1Old = em.merge(participantes1Old);
            }
            if (participantes1New != null && !participantes1New.equals(participantes1Old)) {
                participantes1New.getEsAmigoDeCollection().add(esAmigoDe);
                participantes1New = em.merge(participantes1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EsAmigoDePK id = esAmigoDe.getEsAmigoDePK();
                if (findEsAmigoDe(id) == null) {
                    throw new NonexistentEntityException("The esAmigoDe with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EsAmigoDePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EsAmigoDe esAmigoDe;
            try {
                esAmigoDe = em.getReference(EsAmigoDe.class, id);
                esAmigoDe.getEsAmigoDePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The esAmigoDe with id " + id + " no longer exists.", enfe);
            }
            Participantes participantes = esAmigoDe.getParticipantes();
            if (participantes != null) {
                participantes.getEsAmigoDeCollection().remove(esAmigoDe);
                participantes = em.merge(participantes);
            }
            Participantes participantes1 = esAmigoDe.getParticipantes1();
            if (participantes1 != null) {
                participantes1.getEsAmigoDeCollection().remove(esAmigoDe);
                participantes1 = em.merge(participantes1);
            }
            em.remove(esAmigoDe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EsAmigoDe> findEsAmigoDeEntities() {
        return findEsAmigoDeEntities(true, -1, -1);
    }

    public List<EsAmigoDe> findEsAmigoDeEntities(int maxResults, int firstResult) {
        return findEsAmigoDeEntities(false, maxResults, firstResult);
    }

    private List<EsAmigoDe> findEsAmigoDeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EsAmigoDe.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public EsAmigoDe findEsAmigoDe(EsAmigoDePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EsAmigoDe.class, id);
        } finally {
            em.close();
        }
    }

    public int getEsAmigoDeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EsAmigoDe> rt = cq.from(EsAmigoDe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
