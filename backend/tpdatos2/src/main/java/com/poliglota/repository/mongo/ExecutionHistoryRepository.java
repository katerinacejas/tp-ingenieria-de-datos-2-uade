package com.project.repository.mongodb;

import com.project.model.mongodb.ExecutionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExecutionHistoryRepository extends MongoRepository<ExecutionHistory, String> {
    List<ExecutionHistory> findByRequestId(String requestId);
}
