package com.nacer.examejb1.Service;

import java.util.List;
import com.nacer.examejb1.Entity.Cd;
import jakarta.ejb.Remote;
public interface CdServiceBean {
    List<Cd> getAllCDsDisponibles();
    void emprunterCD(Long cdId, Long utilisateurId);
}
