package com.poliglota.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poliglota.model.mysql.ProcessRequest;

@Repository
public interface ProcessRequestRepository extends JpaRepository<ProcessRequest, Long> {
	ProcessRequest findByName(String name);
	List<ProcessRequest> findAllByUser_UserId(Long userId);
}
