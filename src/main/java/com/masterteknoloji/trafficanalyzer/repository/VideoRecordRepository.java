package com.masterteknoloji.trafficanalyzer.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;


/**
 * Spring Data JPA repository for the VideoRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRecordRepository extends JpaRepository<VideoRecord, Long> {
    @Query("select v.id as id,v.vehicleType as vehicleType,v.insertDate as insertDate,v.videoLine.id as lineId from VideoRecord v where v.videoLine.video.id = ?1 order by v.insertDate desc")
    Iterable<Map<String,Object>> findAllByVideoId(Long id);
}
