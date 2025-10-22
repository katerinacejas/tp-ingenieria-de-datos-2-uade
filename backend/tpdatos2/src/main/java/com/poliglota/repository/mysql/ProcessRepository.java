package com.project.repository.mongodb;

import com.project.model.mongodb.Process;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends MongoRepository<Process, String> {
}
