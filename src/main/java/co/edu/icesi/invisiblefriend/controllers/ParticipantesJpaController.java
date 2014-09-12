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
import co.edu.icesi.invisiblefriend.entities.Personas;
import co.edu.icesi.invisiblefriend.entities.Juego;
import co.edu.icesi.invisiblefriend.entities.EsAmigoDe;
import co.edu.icesi.invisiblefriend.entities.Participantes;
import co.edu.icesi.invisiblefriend.entities.ParticipantesPK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author David Andrés Maznzano Herrera <damanzano>
 */
public class ParticipantesJpaController implements Serializable {

    public ParticipantesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Participantes participantes) throws PreexistingEntityException, Exception {
        if (participantes.getParticipantesPK() == null) {
            participantes.setParticipantesPK(new ParticipantesPK());
            participantes.getParticipantesPK().setNumeroJuego(participantes.getJuego().getNumeroId());
            participantes.getParticipantesPK().setNumeroPersona(participantes.getPersonas().getNumeroId());
        }
        if (participantes.getEsAmigoDeCollection() == null) {
            participantes.setEsAmigoDeCollection(new ArrayList<EsAmigoDe>());
        }
        if (participantes.getEsAmigoDeCollection1() == null) {
            participantes.setEsAmigoDeCollection1(new ArrayList<EsAmigoDe>());
        }
        
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personas personas = participantes.getPersonas();
            if (personas != null) {
                personas = em.getReference(personas.getClass(), personas.getNumeroId());
                participantes.setPersonas(personas);
            }
            Juego juego = participantes.getJuego();
            if (juego != null) {
                juego = em.getReference(juego.getClass(), juego.getNumeroId());
                participantes.setJuego(juego);
            }
            Collection<EsAmigoDe> attachedEsAmigoDeCollection = new ArrayList<EsAmigoDe>();
            for (EsAmigoDe esAmigoDeCollectionEsAmigoDeToAttach : participantes.getEsAmigoDeCollection()) {
                esAmigoDeCollectionEsAmigoDeToAttach = em.getReference(esAmigoDeCollectionEsAmigoDeToAttach.getClass(), esAmigoDeCollectionEsAmigoDeToAttach.getEsAmigoDePK());
                attachedEsAmigoDeCollection.add(esAmigoDeCollectionEsAmigoDeToAttach);
            }
            participantes.setEsAmigoDeCollection(attachedEsAmigoDeCollection);
            Collection<EsAmigoDe> attachedEsAmigoDeCollection1 = new ArrayList<EsAmigoDe>();
            for (EsAmigoDe esAmigoDeCollection1EsAmigoDeToAttach : participantes.getEsAmigoDeCollection1()) {
                esAmigoDeCollection1EsAmigoDeToAttach = em.getReference(esAmigoDeCollection1EsAmigoDeToAttach.getClass(), esAmigoDeCollection1EsAmigoDeToAttach.getEsAmigoDePK());
                attachedEsAmigoDeCollection1.add(esAmigoDeCollection1EsAmigoDeToAttach);
            }
            participantes.setEsAmigoDeCollection1(attachedEsAmigoDeCollection1);
            em.persist(participantes);
            if (personas != null) {
                personas.getParticipantesCollection().add(participantes);
                personas = em.merge(personas);
            }
            if (juego != null) {
                juego.getParticipantesCollection().add(participantes);
                juego = em.merge(juego);
            }
            for (EsAmigoDe esAmigoDeCollectionEsAmigoDe : participantes.getEsAmigoDeCollection()) {
                Participantes oldParticipantesOfEsAmigoDeCollectionEsAmigoDe = esAmigoDeCollectionEsAmigoDe.getParticipantes();
                esAmigoDeCollectionEsAmigoDe.setParticipantes(participantes);
                esAmigoDeCollectionEsAmigoDe = em.merge(esAmigoDeCollectionEsAmigoDe);
                if (oldParticipantesOfEsAmigoDeCollectionEsAmigoDe != null) {
                    oldParticipantesOfEsAmigoDeCollectionEsAmigoDe.getEsAmigoDeCollection().remove(esAmigoDeCollectionEsAmigoDe);
                    oldParticipantesOfEsAmigoDeCollectionEsAmigoDe = em.merge(oldParticipantesOfEsAmigoDeCollectionEsAmigoDe);
                }
            }
            for (EsAmigoDe esAmigoDeCollection1EsAmigoDe : participantes.getEsAmigoDeCollection1()) {
                Participantes oldParticipantes1OfEsAmigoDeCollection1EsAmigoDe = esAmigoDeCollection1EsAmigoDe.getParticipantes1();
                esAmigoDeCollection1EsAmigoDe.setParticipantes1(participantes);
                esAmigoDeCollection1EsAmigoDe = em.merge(esAmigoDeCollection1EsAmigoDe);
                if (oldParticipantes1OfEsAmigoDeCollection1EsAmigoDe != null) {
                    oldParticipantes1OfEsAmigoDeCollection1EsAmigoDe.getEsAmigoDeCollection1().remove(esAmigoDeCollection1EsAmigoDe);
                    oldParticipantes1OfEsAmigoDeCollection1EsAmigoDe = em.merge(oldParticipantes1OfEsAmigoDeCollection1EsAmigoDe);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findParticipantes(participantes.getParticipantesPK()) != null) {
                throw new PreexistingEntityException("Participantes " + participantes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Participantes participantes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        participantes.getParticipantesPK().setNumeroJuego(participantes.getJuego().getNumeroId());
        participantes.getParticipantesPK().setNumeroPersona(participantes.getPersonas().getNumeroId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participantes persistentParticipantes = em.find(Participantes.class, participantes.getParticipantesPK());
            Personas personasOld = persistentParticipantes.getPersonas();
            Personas personasNew = participantes.getPersonas();
            Juego juegoOld = persistentParticipantes.getJuego();
            Juego juegoNew = participantes.getJuego();
            Collection<EsAmigoDe> esAmigoDeCollectionOld = persistentParticipantes.getEsAmigoDeCollection();
            Collection<EsAmigoDe> esAmigoDeCollectionNew = participantes.getEsAmigoDeCollection();
            Collection<EsAmigoDe> esAmigoDeCollection1Old = persistentParticipantes.getEsAmigoDeCollection1();
            Collection<EsAmigoDe> esAmigoDeCollection1New = participantes.getEsAmigoDeCollection1();
            List<String> illegalOrphanMessages = null;
            for (EsAmigoDe esAmigoDeCollectionOldEsAmigoDe : esAmigoDeCollectionOld) {
                if (!esAmigoDeCollectionNew.contains(esAmigoDeCollectionOldEsAmigoDe)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EsAmigoDe " + esAmigoDeCollectionOldEsAmigoDe + " since its participantes field is not nullable.");
                }
            }
            for (EsAmigoDe esAmigoDeCollection1OldEsAmigoDe : esAmigoDeCollection1Old) {
                if (!esAmigoDeCollection1New.contains(esAmigoDeCollection1OldEsAmigoDe)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EsAmigoDe " + esAmigoDeCollection1OldEsAmigoDe + " since its participantes1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personasNew != null) {
                personasNew = em.getReference(personasNew.getClass(), personasNew.getNumeroId());
                participantes.setPersonas(personasNew);
            }
            if (juegoNew != null) {
                juegoNew = em.getReference(juegoNew.getClass(), juegoNew.getNumeroId());
                participantes.setJuego(juegoNew);
            }
            Collection<EsAmigoDe> attachedEsAmigoDeCollectionNew = new ArrayList<EsAmigoDe>();
            for (EsAmigoDe esAmigoDeCollectionNewEsAmigoDeToAttach : esAmigoDeCollectionNew) {
                esAmigoDeCollectionNewEsAmigoDeToAttach = em.getReference(esAmigoDeCollectionNewEsAmigoDeToAttach.getClass(), esAmigoDeCollectionNewEsAmigoDeToAttach.getEsAmigoDePK());
                attachedEsAmigoDeCollectionNew.add(esAmigoDeCollectionNewEsAmigoDeToAttach);
            }
            esAmigoDeCollectionNew = attachedEsAmigoDeCollectionNew;
            participantes.setEsAmigoDeCollection(esAmigoDeCollectionNew);
            Collection<EsAmigoDe> attachedEsAmigoDeCollection1New = new ArrayList<EsAmigoDe>();
            for (EsAmigoDe esAmigoDeCollection1NewEsAmigoDeToAttach : esAmigoDeCollection1New) {
                esAmigoDeCollection1NewEsAmigoDeToAttach = em.getReference(esAmigoDeCollection1NewEsAmigoDeToAttach.getClass(), esAmigoDeCollection1NewEsAmigoDeToAttach.getEsAmigoDePK());
                attachedEsAmigoDeCollection1New.add(esAmigoDeCollection1NewEsAmigoDeToAttach);
            }
            esAmigoDeCollection1New = attachedEsAmigoDeCollection1New;
            participantes.setEsAmigoDeCollection1(esAmigoDeCollection1New);
            participantes = em.merge(participantes);
            if (personasOld != null && !personasOld.equals(personasNew)) {
                personasOld.getParticipantesCollection().remove(participantes);
                personasOld = em.merge(personasOld);
            }
            if (personasNew != null && !personasNew.equals(personasOld)) {
                personasNew.getParticipantesCollection().add(participantes);
                personasNew = em.merge(personasNew);
            }
            if (juegoOld != null && !juegoOld.equals(juegoNew)) {
                juegoOld.getParticipantesCollection().remove(participantes);
                juegoOld = em.merge(juegoOld);
            }
            if (juegoNew != null && !juegoNew.equals(juegoOld)) {
                juegoNew.getParticipantesCollection().add(participantes);
                juegoNew = em.merge(juegoNew);
            }
            for (EsAmigoDe esAmigoDeCollectionNewEsAmigoDe : esAmigoDeCollectionNew) {
                if (!esAmigoDeCollectionOld.contains(esAmigoDeCollectionNewEsAmigoDe)) {
                    Participantes oldParticipantesOfEsAmigoDeCollectionNewEsAmigoDe = esAmigoDeCollectionNewEsAmigoDe.getParticipantes();
                    esAmigoDeCollectionNewEsAmigoDe.setParticipantes(participantes);
                    esAmigoDeCollectionNewEsAmigoDe = em.merge(esAmigoDeCollectionNewEsAmigoDe);
                    if (oldParticipantesOfEsAmigoDeCollectionNewEsAmigoDe != null && !oldParticipantesOfEsAmigoDeCollectionNewEsAmigoDe.equals(participantes)) {
                        oldParticipantesOfEsAmigoDeCollectionNewEsAmigoDe.getEsAmigoDeCollection().remove(esAmigoDeCollectionNewEsAmigoDe);
                        oldParticipantesOfEsAmigoDeCollectionNewEsAmigoDe = em.merge(oldParticipantesOfEsAmigoDeCollectionNewEsAmigoDe);
                    }
                }
            }
            for (EsAmigoDe esAmigoDeCollection1NewEsAmigoDe : esAmigoDeCollection1New) {
                if (!esAmigoDeCollection1Old.contains(esAmigoDeCollection1NewEsAmigoDe)) {
                    Participantes oldParticipantes1OfEsAmigoDeCollection1NewEsAmigoDe = esAmigoDeCollection1NewEsAmigoDe.getParticipantes1();
                    esAmigoDeCollection1NewEsAmigoDe.setParticipantes1(participantes);
                    esAmigoDeCollection1NewEsAmigoDe = em.merge(esAmigoDeCollection1NewEsAmigoDe);
                    if (oldParticipantes1OfEsAmigoDeCollection1NewEsAmigoDe != null && !oldParticipantes1OfEsAmigoDeCollection1NewEsAmigoDe.equals(participantes)) {
                        oldParticipantes1OfEsAmigoDeCollection1NewEsAmigoDe.getEsAmigoDeCollection1().remove(esAmigoDeCollection1NewEsAmigoDe);
                        oldParticipantes1OfEsAmigoDeCollection1NewEsAmigoDe = em.merge(oldParticipantes1OfEsAmigoDeCollection1NewEsAmigoDe);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ParticipantesPK id = participantes.getParticipantesPK();
                if (findParticipantes(id) == null) {
                    throw new NonexistentEntityException("The participantes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ParticipantesPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participantes participantes;
            try {
                participantes = em.getReference(Participantes.class, id);
                participantes.getParticipantesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The participantes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EsAmigoDe> esAmigoDeCollectionOrphanCheck = participantes.getEsAmigoDeCollection();
            for (EsAmigoDe esAmigoDeCollectionOrphanCheckEsAmigoDe : esAmigoDeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Participantes (" + participantes + ") cannot be destroyed since the EsAmigoDe " + esAmigoDeCollectionOrphanCheckEsAmigoDe + " in its esAmigoDeCollection field has a non-nullable participantes field.");
            }
            Collection<EsAmigoDe> esAmigoDeCollection1OrphanCheck = participantes.getEsAmigoDeCollection1();
            for (EsAmigoDe esAmigoDeCollection1OrphanCheckEsAmigoDe : esAmigoDeCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Participantes (" + participantes + ") cannot be destroyed since the EsAmigoDe " + esAmigoDeCollection1OrphanCheckEsAmigoDe + " in its esAmigoDeCollection1 field has a non-nullable participantes1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Personas personas = participantes.getPersonas();
            if (personas != null) {
                personas.getParticipantesCollection().remove(participantes);
                personas = em.merge(personas);
            }
            Juego juego = participantes.getJuego();
            if (juego != null) {
                juego.getParticipantesCollection().remove(participantes);
                juego = em.merge(juego);
            }
            em.remove(participantes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Participantes> findParticipantesEntities() {
        return findParticipantesEntities(true, -1, -1);
    }

    public List<Participantes> findParticipantesEntities(int maxResults, int firstResult) {
        return findParticipantesEntities(false, maxResults, firstResult);
    }

    private List<Participantes> findParticipantesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Participantes.class));
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

    public Participantes findParticipantes(ParticipantesPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Participantes.class, id);
        } finally {
            em.close();
        }
    }

    public int getParticipantesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Participantes> rt = cq.from(Participantes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Participantes findParticipantesByJuego(String juegoId) {
        EntityManager em = getEntityManager();
        //TODO: Implement this functionality
        
        return new Participantes();
    }
    
}
