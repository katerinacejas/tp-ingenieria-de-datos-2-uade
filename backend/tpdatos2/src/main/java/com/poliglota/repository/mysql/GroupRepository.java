package com.project.repository.mongodb;

import com.project.model.mongodb.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
     Group findByGroupbyName(String name);
}
