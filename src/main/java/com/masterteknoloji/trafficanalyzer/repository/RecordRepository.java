package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Record;
import com.masterteknoloji.trafficanalyzer.domain.User;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Record entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);
}
