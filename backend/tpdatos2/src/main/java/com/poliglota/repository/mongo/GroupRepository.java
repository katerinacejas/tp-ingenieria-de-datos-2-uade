package com.poliglota.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.poliglota.model.mongo.Group;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> findByName(String name);
    boolean existsByName(String name);

    List<Group> findByMemberIds(Long memberId);
}
