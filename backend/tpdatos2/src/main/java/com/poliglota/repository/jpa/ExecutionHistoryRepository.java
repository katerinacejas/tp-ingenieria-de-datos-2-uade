package com.poliglota.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poliglota.model.mysql.ExecutionHistory;

import java.util.List;

@Repository
public interface ExecutionHistoryRepository extends JpaRepository<ExecutionHistory, Long> {
	List<ExecutionHistory> findByProcessRequest_ProcessRequestId(Long processRequestId);
}
