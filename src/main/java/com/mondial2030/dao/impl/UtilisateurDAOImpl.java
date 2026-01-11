package com.mondial2030.dao.impl;

import com.mondial2030.dao.interfaces.UtilisateurDAO;
import com.mondial2030.entity.Utilisateur;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation Hibernate du DAO Utilisateur.
 * 
 * @author Mondial 2030 Team
 * @version 1.0
 */
public class UtilisateurDAOImpl extends GenericDAOImpl<Utilisateur, Long> implements UtilisateurDAO {
    
    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        String hql = "FROM Utilisateur u WHERE u.email = :email";
        return executeSingleResultQuery(hql, "email", email);
    }
    
    @Override
    public Optional<Utilisateur> authentifier(String email, String motDePasse) {
        Optional<Utilisateur> utilisateur = findByEmail(email);
        if (utilisateur.isPresent()) {
            Utilisateur u = utilisateur.get();
            // Vérifier le mot de passe avec BCrypt
            if (BCrypt.checkpw(motDePasse, u.getMotDePasse())) {
                updateDerniereConnexion(u.getId());
                return utilisateur;
            }
        }
        return Optional.empty();
    }
    
    @Override
    public boolean emailExists(String email) {
        String hql = "SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :email";
        long count = executeCountQuery(hql, "email", email);
        return count > 0;
    }
    
    @Override
    public List<Utilisateur> findByNom(String nom) {
        String hql = "FROM Utilisateur u WHERE LOWER(u.nom) LIKE LOWER(:nom)";
        return executeQuery(hql, "nom", "%" + nom + "%");
    }
    
    @Override
    public List<Utilisateur> findAllActifs() {
        String hql = "FROM Utilisateur u WHERE u.actif = true";
        return executeQuery(hql);
    }
    
    @Override
    public void updateDerniereConnexion(Long id) {
        String hql = "UPDATE Utilisateur u SET u.derniereConnexion = :date WHERE u.id = :id";
        executeUpdate(hql, "date", LocalDateTime.now(), "id", id);
    }
    
    @Override
    public void setActif(Long id, boolean actif) {
        String hql = "UPDATE Utilisateur u SET u.actif = :actif WHERE u.id = :id";
        executeUpdate(hql, "actif", actif, "id", id);
    }
    
    /**
     * Sauvegarde un utilisateur en hachant son mot de passe
     */
    @Override
    public void save(Utilisateur entity) {
        // Hacher le mot de passe avant sauvegarde
        String hashedPassword = BCrypt.hashpw(entity.getMotDePasse(), BCrypt.gensalt());
        entity.setMotDePasse(hashedPassword);
        super.save(entity);
    }
}
