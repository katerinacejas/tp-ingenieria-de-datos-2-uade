package com.poliglota.repository.jpa;

import com.poliglota.model.mysql.Account;
import com.poliglota.model.mysql.Rol;
import com.poliglota.model.mysql.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	@Query("select u from User u where u.rolEntity.code = :code")
	List<User> findByRol(@Param("code") Rol code);

	boolean existsByEmail(String email);

	void deleteById(Long id);
}
