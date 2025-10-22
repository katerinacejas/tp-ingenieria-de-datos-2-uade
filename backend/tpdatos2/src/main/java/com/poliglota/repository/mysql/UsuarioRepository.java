package com.poliglota.repository.mongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.poliglota.model.mongo.User;
import java.util.Optional;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    
	// Método para verificar si un usuario existe por su email
	boolean existsByEmail(String email);
	
	// Método para eliminar un usuario por su id
	void deleteById(Long id);
}

