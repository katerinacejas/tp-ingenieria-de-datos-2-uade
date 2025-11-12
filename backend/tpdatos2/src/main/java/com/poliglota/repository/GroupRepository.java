package com.poliglota.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.poliglota.model.mongo.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    Optional<Group> findByName(String name);
    boolean existsByName(String name);

    // Busca todos los grupos donde el usuario (id MySQL) participa
    List<Group> findByMemberIds(Long memberId);
}
