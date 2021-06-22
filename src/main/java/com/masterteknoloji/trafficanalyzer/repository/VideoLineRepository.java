package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoLine;
import org.springframework.stereotype.Repository;

import java.util.Map;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VideoLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoLineRepository extends JpaRepository<VideoLine, Long> {

	 @Query("select v.id as id,"
	    		+ "    v.vehicleType as vehicleType,"
	    		+ "    v.insertDate as insertDate,"
	    		+ "    v.videoLine.id as lineId,"
	    		+ "    v.duration as duration, "
	    		+ "    v.speed as speed "
	    		+ "    from VideoRecord v where v.videoLine.id = ?1 order by v.insertDate")
	 Iterable<Map<String,Object>> findAllByLineId(Long id);
}
