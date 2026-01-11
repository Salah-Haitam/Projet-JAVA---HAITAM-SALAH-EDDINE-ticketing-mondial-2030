package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.SpectateurDAO;
import com.mondial2030.entity.Spectateur;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Spectateur.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class SpectateurDAOImpl extends GenericDAOImpl<Spectateur, Long> implements SpectateurDAO {
    
    @Override
    public Optional<Spectateur> findByEmail(String email) {
        String hql = "FROM Spectateur s WHERE s.email = :email";
        return executeSingleResultQuery(hql, "email", email);
    }
    
    @Override
    public List<Spectateur> findByNationalite(String nationalite) {
        String hql = "FROM Spectateur s WHERE s.nationalite = :nationalite";
        return executeQuery(hql, "nationalite", nationalite);
    }
    
    @Override
    public Optional<Spectateur> findByNumeroPasseport(String numeroPasseport) {
        String hql = "FROM Spectateur s WHERE s.numeroPasseport = :passeport";
        return executeSingleResultQuery(hql, "passeport", numeroPasseport);
    }
    
    @Override
    public List<Spectateur> findByNombreTicketsMinimum(int nombreMinimum) {
        String hql = "FROM Spectateur s WHERE s.nombreTicketsAchetes >= :minimum";
        return executeQuery(hql, "minimum", nombreMinimum);
    }
    
    @Override
    public Optional<Spectateur> authentifier(String email, String motDePasse) {
        Optional<Spectateur> spectateur = findByEmail(email);
        if (spectateur.isPresent()) {
            Spectateur s = spectateur.get();
            System.out.println("=== DEBUG AUTH SPECTATEUR ===");
            System.out.println("Email: " + email);
            System.out.println("Password entré: " + motDePasse);
            System.out.println("Hash stocké: " + s.getMotDePasse());
            boolean match = BCrypt.checkpw(motDePasse, s.getMotDePasse());
            System.out.println("BCrypt match: " + match);
            System.out.println("=============================");
            if (match) {
                return spectateur;
            }
        }
        return Optional.empty();
    }
    
    @Override
    public List<Spectateur> findByVille(String ville) {
        String hql = "FROM Spectateur s WHERE LOWER(s.ville) LIKE LOWER(:ville)";
        return executeQuery(hql, "ville", "%" + ville + "%");
    }
    
    @Override
    public void incrementerNombreTickets(Long id) {
        String hql = "UPDATE Spectateur s SET s.nombreTicketsAchetes = s.nombreTicketsAchetes + 1 WHERE s.id = :id";
        executeUpdate(hql, "id", id);
    }
    
    /**
     * Sauvegarde un spectateur en hachant son mot de passe
     */
    @Override
    public void save(Spectateur entity) {
        // Hacher le mot de passe avant sauvegarde (seulement si pas déjà hashé)
        String password = entity.getMotDePasse();
        if (password != null && !password.startsWith("$2a$") && !password.startsWith("$2b$")) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            entity.setMotDePasse(hashedPassword);
        }
        super.save(entity);
    }
}
