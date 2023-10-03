package com.maveric.csp.repository;

import com.maveric.csp.CspApplication;

import com.maveric.csp.entity.Session;


import java.util.Date;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CspApplication.class)
@ActiveProfiles("test")
@Transactional
class SessionRepositoryTest {
    @Autowired
    SessionRepository repository;

    Session session;

    @BeforeEach
    void setUp() {
        session = new Session("SESSION_1", "sessionName", "purushotham", new Date(), new Date(), 'A', "No remarks", null);
        repository.save(session);
    }

    @AfterEach
    void tearDown() {
        session = null;
        repository.deleteAll();
    }


    @Test
    void testFindByStatus_Successful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Session> sessions = repository.findByStatus('A', pageable);
        assertEquals(1, sessions.getTotalElements());
        assertEquals(session.getCreatedBy(), sessions.getContent().get(0).getCreatedBy());
    }

    @Test
    void testFindByStatus_NotFound() {
        Page<Session> sessions = repository.findByStatus('B', PageRequest.of(0, 10));
        assertTrue(sessions.isEmpty());
    }


}