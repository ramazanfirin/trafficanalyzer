package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoDirection;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VideoDirection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoDirectionRepository extends JpaRepository<VideoDirection, Long> {

}
