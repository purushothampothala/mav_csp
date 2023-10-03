package com.maveric.csp.repository;

import com.maveric.csp.entity.SessionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionHistoryRepository extends JpaRepository<SessionHistory,String> {
    public SessionHistory findBySessionId(String id);
}
