package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoLine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VideoLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoLineRepository extends JpaRepository<VideoLine, Long> {

}
