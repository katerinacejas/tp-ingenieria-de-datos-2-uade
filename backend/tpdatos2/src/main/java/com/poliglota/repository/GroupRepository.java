package com.poliglota.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poliglota.model.mongo.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    //  Buscar Grupos  por Nombre
    Group findByName(String name);
}
