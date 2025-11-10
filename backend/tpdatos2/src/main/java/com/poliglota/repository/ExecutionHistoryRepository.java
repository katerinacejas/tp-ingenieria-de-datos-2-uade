package com.poliglota.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.poliglota.model.mysql.ExecutionHistory;

import java.util.List;

@Repository
public interface ExecutionHistoryRepository extends MongoRepository<ExecutionHistory, String> {
	List<ExecutionHistory> findByRequestId(String requestId);
}
