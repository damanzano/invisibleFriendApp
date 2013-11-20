/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.controllers;

import co.edu.icesi.controllers.invisiblefriend.exceptions.IllegalOrphanException;
import co.edu.icesi.controllers.invisiblefriend.exceptions.NonexistentEntityException;
import co.edu.icesi.controllers.invisiblefriend.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.icesi.invisiblefriend.entities.Participantes;
import co.edu.icesi.invisiblefriend.entities.Personas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
public class PersonasJpaController implements Serializable {

    public PersonasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personas personas) throws PreexistingEntityException, Exception {
        if (personas.getParticipantesCollection() == null) {
            personas.setParticipantesCollection(new ArrayList<Participantes>());
        }
        EntityManager em = null;
        try {
            int numeroId = this.getPersonasCount();
            personas.setNumeroId("" + (numeroId + 1));
            System.out.println("Creating persona " + personas.getNumeroId());
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Participantes> attachedParticipantesCollection = new ArrayList<Participantes>();
            for (Participantes participantesCollectionParticipantesToAttach : personas.getParticipantesCollection()) {
                participantesCollectionParticipantesToAttach = em.getReference(participantesCollectionParticipantesToAttach.getClass(), participantesCollectionParticipantesToAttach.getParticipantesPK());
                attachedParticipantesCollection.add(participantesCollectionParticipantesToAttach);
            }
            personas.setParticipantesCollection(attachedParticipantesCollection);
            em.persist(personas);
            for (Participantes participantesCollectionParticipantes : personas.getParticipantesCollection()) {
                Personas oldPersonasOfParticipantesCollectionParticipantes = participantesCollectionParticipantes.getPersonas();
                participantesCollectionParticipantes.setPersonas(personas);
                participantesCollectionParticipantes = em.merge(participantesCollectionParticipantes);
                if (oldPersonasOfParticipantesCollectionParticipantes != null) {
                    oldPersonasOfParticipantesCollectionParticipantes.getParticipantesCollection().remove(participantesCollectionParticipantes);
                    oldPersonasOfParticipantesCollectionParticipantes = em.merge(oldPersonasOfParticipantesCollectionParticipantes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonas(personas.getNumeroId()) != null) {
                throw new PreexistingEntityException("Personas " + personas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personas personas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personas persistentPersonas = em.find(Personas.class, personas.getNumeroId());
            Collection<Participantes> participantesCollectionOld = persistentPersonas.getParticipantesCollection();
            Collection<Participantes> participantesCollectionNew = personas.getParticipantesCollection();
            List<String> illegalOrphanMessages = null;
            if (participantesCollectionNew != null) {
                for (Participantes participantesCollectionOldParticipantes : participantesCollectionOld) {
                    if (!participantesCollectionNew.contains(participantesCollectionOldParticipantes)) {
                        if (illegalOrphanMessages == null) {
                            illegalOrphanMessages = new ArrayList<String>();
                        }
                        illegalOrphanMessages.add("You must retain Participantes " + participantesCollectionOldParticipantes + " since its personas field is not nullable.");
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
                personas.setParticipantesCollection(participantesCollectionNew);
            } else {
                personas.setParticipantesCollection(participantesCollectionOld);
            }
            personas = em.merge(personas);
            if (participantesCollectionNew != null) {
                for (Participantes participantesCollectionNewParticipantes : participantesCollectionNew) {
                    if (!participantesCollectionOld.contains(participantesCollectionNewParticipantes)) {
                        Personas oldPersonasOfParticipantesCollectionNewParticipantes = participantesCollectionNewParticipantes.getPersonas();
                        participantesCollectionNewParticipantes.setPersonas(personas);
                        participantesCollectionNewParticipantes = em.merge(participantesCollectionNewParticipantes);
                        if (oldPersonasOfParticipantesCollectionNewParticipantes != null && !oldPersonasOfParticipantesCollectionNewParticipantes.equals(personas)) {
                            oldPersonasOfParticipantesCollectionNewParticipantes.getParticipantesCollection().remove(participantesCollectionNewParticipantes);
                            oldPersonasOfParticipantesCollectionNewParticipantes = em.merge(oldPersonasOfParticipantesCollectionNewParticipantes);
                        }
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = personas.getNumeroId();
                if (findPersonas(id) == null) {
                    throw new NonexistentEntityException("The personas with id " + id + " no longer exists.");
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
            Personas personas;
            try {
                personas = em.getReference(Personas.class, id);
                personas.getNumeroId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Participantes> participantesCollectionOrphanCheck = personas.getParticipantesCollection();
            for (Participantes participantesCollectionOrphanCheckParticipantes : participantesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Personas (" + personas + ") cannot be destroyed since the Participantes " + participantesCollectionOrphanCheckParticipantes + " in its participantesCollection field has a non-nullable personas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(personas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personas> findPersonasEntities() {
        return findPersonasEntities(true, -1, -1);
    }

    public List<Personas> findPersonasEntities(int maxResults, int firstResult) {
        return findPersonasEntities(false, maxResults, firstResult);
    }

    private List<Personas> findPersonasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personas.class));
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

    public Personas findPersonas(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personas> rt = cq.from(Personas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
