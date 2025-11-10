package com.poliglota.repository.mysql;

import com.poliglota.model.mysql.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByGroupbyName(String name);
}
