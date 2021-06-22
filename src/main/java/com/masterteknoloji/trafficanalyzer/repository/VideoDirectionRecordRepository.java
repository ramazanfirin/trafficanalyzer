package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoDirectionRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VideoDirectionRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoDirectionRecordRepository extends JpaRepository<VideoDirectionRecord, Long> {

}
