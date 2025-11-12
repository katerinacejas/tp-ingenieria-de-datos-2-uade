package com.poliglota.repository;

import com.poliglota.model.mysql.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	List<User> findByRol(String rol);

	// Método para verificar si un usuario existe por su email
	boolean existsByEmail(String email);

	// Método para eliminar un usuario por su id
	void deleteById(Long id);
}
