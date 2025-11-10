package com.poliglota.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.poliglota.model.mysql.RolEntity;
import com.poliglota.model.mysql.Rol;

public interface RolRepository extends JpaRepository<RolEntity, Long> {
    Optional<RolEntity> findByCode(Rol code);
}