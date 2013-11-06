/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.icesi.invisiblefriend.controllers;

import co.edu.icesi.controllers.invisiblefriend.exceptions.IllegalOrphanException;
import co.edu.icesi.controllers.invisiblefriend.exceptions.NonexistentEntityException;
import co.edu.icesi.controllers.invisiblefriend.exceptions.PreexistingEntityException;
import co.edu.icesi.invisiblefriend.entities.Juego;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.icesi.invisiblefriend.entities.Participantes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
public class JuegoJpaController implements Serializable {

    public JuegoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Juego juego) throws PreexistingEntityException, Exception {
        if (juego.getParticipantesCollection() == null) {
            juego.setParticipantesCollection(new ArrayList<Participantes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Participantes> attachedParticipantesCollection = new ArrayList<Participantes>();
            for (Participantes participantesCollectionParticipantesToAttach : juego.getParticipantesCollection()) {
                participantesCollectionParticipantesToAttach = em.getReference(participantesCollectionParticipantesToAttach.getClass(), participantesCollectionParticipantesToAttach.getParticipantesPK());
                attachedParticipantesCollection.add(participantesCollectionParticipantesToAttach);
            }
            juego.setParticipantesCollection(attachedParticipantesCollection);
            em.persist(juego);
            for (Participantes participantesCollectionParticipantes : juego.getParticipantesCollection()) {
                Juego oldJuegoOfParticipantesCollectionParticipantes = participantesCollectionParticipantes.getJuego();
                participantesCollectionParticipantes.setJuego(juego);
                participantesCollectionParticipantes = em.merge(participantesCollectionParticipantes);
                if (oldJuegoOfParticipantesCollectionParticipantes != null) {
                    oldJuegoOfParticipantesCollectionParticipantes.getParticipantesCollection().remove(participantesCollectionParticipantes);
                    oldJuegoOfParticipantesCollectionParticipantes = em.merge(oldJuegoOfParticipantesCollectionParticipantes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJuego(juego.getNumeroId()) != null) {
                throw new PreexistingEntityException("Juego " + juego + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Juego juego) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juego persistentJuego = em.find(Juego.class, juego.getNumeroId());
            Collection<Participantes> participantesCollectionOld = persistentJuego.getParticipantesCollection();
            Collection<Participantes> participantesCollectionNew = juego.getParticipantesCollection();
            List<String> illegalOrphanMessages = null;
            for (Participantes participantesCollectionOldParticipantes : participantesCollectionOld) {
                if (!participantesCollectionNew.contains(participantesCollectionOldParticipantes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Participantes " + participantesCollectionOldParticipantes + " since its juego field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Participantes> attachedParticipantesCollectionNew = new ArrayList<Participantes>();
            for (Participantes participantesCollectionNewParticipantesToAttach : participantesCollectionNew) {
                participantesCollectionNewParticipantesToAttach = em.getReference(participantesCollectionNewParticipantesToAttach.getClass(), participantesCollectionNewParticipantesToAttach.getParticipantesPK());
                attachedParticipantesCollectionNew.add(participantesCollectionNewParticipantesToAttach);
            }
            participantesCollectionNew = attachedParticipantesCollectionNew;
            juego.setParticipantesCollection(participantesCollectionNew);
            juego = em.merge(juego);
            for (Participantes participantesCollectionNewParticipantes : participantesCollectionNew) {
                if (!participantesCollectionOld.contains(participantesCollectionNewParticipantes)) {
                    Juego oldJuegoOfParticipantesCollectionNewParticipantes = participantesCollectionNewParticipantes.getJuego();
                    participantesCollectionNewParticipantes.setJuego(juego);
                    participantesCollectionNewParticipantes = em.merge(participantesCollectionNewParticipantes);
                    if (oldJuegoOfParticipantesCollectionNewParticipantes != null && !oldJuegoOfParticipantesCollectionNewParticipantes.equals(juego)) {
                        oldJuegoOfParticipantesCollectionNewParticipantes.getParticipantesCollection().remove(participantesCollectionNewParticipantes);
                        oldJuegoOfParticipantesCollectionNewParticipantes = em.merge(oldJuegoOfParticipantesCollectionNewParticipantes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = juego.getNumeroId();
                if (findJuego(id) == null) {
                    throw new NonexistentEntityException("The juego with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juego juego;
            try {
                juego = em.getReference(Juego.class, id);
                juego.getNumeroId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juego with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Participantes> participantesCollectionOrphanCheck = juego.getParticipantesCollection();
            for (Participantes participantesCollectionOrphanCheckParticipantes : participantesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Juego (" + juego + ") cannot be destroyed since the Participantes " + participantesCollectionOrphanCheckParticipantes + " in its participantesCollection field has a non-nullable juego field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(juego);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juego> findJuegoEntities() {
        return findJuegoEntities(true, -1, -1);
    }

    public List<Juego> findJuegoEntities(int maxResults, int firstResult) {
        return findJuegoEntities(false, maxResults, firstResult);
    }

    private List<Juego> findJuegoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Juego.class));
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

    public Juego findJuego(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juego.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuegoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Juego> rt = cq.from(Juego.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
