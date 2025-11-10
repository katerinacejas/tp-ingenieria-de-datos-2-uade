package com.poliglota.repository.mongo;

import com.poliglota.model.mongo.ExecutionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExecutionHistoryRepository extends MongoRepository<ExecutionHistory, String> {
	List<ExecutionHistory> findByRequestId(String requestId);
}
