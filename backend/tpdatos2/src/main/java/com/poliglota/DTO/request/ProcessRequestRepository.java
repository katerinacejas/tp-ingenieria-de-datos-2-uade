package com.project.repository.mongodb;

import com.project.model.mongodb.ProcessRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessRequestRepository extends MongoRepository<ProcessRequest, String> {
    List<ProcessRequest> findByUserId(String userId);
}
