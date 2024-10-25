package com.nacer.examejb1.Service;

import com.nacer.examejb1.Entity.Cd;
import com.nacer.examejb1.Entity.Emprunt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class CDServiceBeanImpl implements CdServiceBean {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Cd> getAllCDsDisponibles() {
        return em.createQuery("SELECT cd FROM CD cd WHERE cd.disponible = true", Cd.class).getResultList();
    }

    @Override
    public void emprunterCD(Long cdId, Long utilisateurId) {
        Cd cd = em.find(Cd.class, cdId);
        if (cd != null && cd.isDisponible()) { // Vérifiez si le CD est disponible
            cd.setDisponible( false);
            Emprunt emprunt = new Emprunt();
            emprunt.setUtilisateurId(utilisateurId);
            emprunt.setCdId(cdId);
            emprunt.setDateEmprunt(LocalDate.now());
            em.persist(emprunt);
        }
    }

    @Override
    public void retournerCD(Long cdId) {
        Cd cd = em.find(Cd.class, cdId);
        if (cd != null) { // Vérifiez que le CD existe
            cd.setDisponible(true);
            Emprunt emprunt = em.createQuery("SELECT e FROM Emprunt e WHERE e.cdId = :cdId AND e.dateRetour IS NULL", Emprunt.class)
                    .setParameter("cdId", cdId)
                    .getSingleResult();
            emprunt.setDateRetour(LocalDate.now());
            em.merge(emprunt);
        }
    }
}
