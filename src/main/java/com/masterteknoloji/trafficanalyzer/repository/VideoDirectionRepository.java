package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoDirection;
import org.springframework.stereotype.Repository;

import java.util.Map;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VideoDirection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoDirectionRepository extends JpaRepository<VideoDirection, Long> {


	 @Query("select v from VideoDirection v where v.video.id = ?1 and v.indexValue = ?2 ")
	 VideoDirection findAllByVideoIdAndRegionId(Long videoId,Long regionId);
}
