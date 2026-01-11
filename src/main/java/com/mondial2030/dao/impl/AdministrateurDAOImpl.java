package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.AdministrateurDAO;
import com.mondial2030.entity.Administrateur;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Administrateur.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class AdministrateurDAOImpl extends GenericDAOImpl<Administrateur, Long> implements AdministrateurDAO {
    
    @Override
    public Optional<Administrateur> findByEmail(String email) {
        String hql = "FROM Administrateur a WHERE a.email = :email";
        return executeSingleResultQuery(hql, "email", email);
    }
    
    @Override
    public List<Administrateur> findByDepartement(String departement) {
        String hql = "FROM Administrateur a WHERE a.departement = :departement";
        return executeQuery(hql, "departement", departement);
    }
    
    @Override
    public List<Administrateur> findByNiveauAccesMinimum(int niveauAcces) {
        String hql = "FROM Administrateur a WHERE a.niveauAcces >= :niveau ORDER BY a.niveauAcces DESC";
        return executeQuery(hql, "niveau", niveauAcces);
    }
    
    @Override
    public List<Administrateur> findWithAccesComplet() {
        String hql = "FROM Administrateur a WHERE a.peutGererUtilisateurs = true " +
                    "AND a.peutGererMatchs = true AND a.peutGenererRapports = true " +
                    "AND a.peutVoirAlertes = true";
        return executeQuery(hql);
    }
    
    @Override
    public Optional<Administrateur> authentifier(String email, String motDePasse) {
        Optional<Administrateur> admin = findByEmail(email);
        if (admin.isPresent()) {
            Administrateur a = admin.get();
            System.out.println("=== DEBUG AUTH ADMIN ===");
            System.out.println("Email: " + email);
            System.out.println("Password entré: " + motDePasse);
            System.out.println("Hash stocké: " + a.getMotDePasse());
            boolean match = BCrypt.checkpw(motDePasse, a.getMotDePasse());
            System.out.println("BCrypt match: " + match);
            System.out.println("========================");
            if (match) {
                return admin;
            }
        }
        return Optional.empty();
    }
    
    /**
     * Sauvegarde un administrateur en hachant son mot de passe
     */
    @Override
    public void save(Administrateur entity) {
        // Hacher le mot de passe avant sauvegarde (seulement si pas déjà hashé)
        String password = entity.getMotDePasse();
        if (password != null && !password.startsWith("$2a$") && !password.startsWith("$2b$")) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            entity.setMotDePasse(hashedPassword);
        }
        super.save(entity);
    }
}
