package com.masterteknoloji.trafficanalyzer.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoDirectionRecord;


/**
 * Spring Data JPA repository for the VideoDirectionRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoDirectionRecordRepository extends JpaRepository<VideoDirectionRecord, Long> {
	 @Query("select v from VideoDirectionRecord v where v.videoDirection.video.id = ?1 and v.videoDirection.indexValue = ?2 ")
	 List<VideoDirectionRecord> findIntersectionData(Long videoId,Long index);
	 
	 @Query(value="Select \n"
	 		+ "    		COUNT(*) as counts,\n"
	 		+ "            video.name as videoname,\n"
	 		+ "            video_direction.name as directionname\n"
	 		+ "    		FROM video_direction_record i\n"
	 		+ "         INNER JOIN video_direction as video_direction ON i.video_direction_id=video_direction.id\n"
	 		+ "    		INNER JOIN video ON video_direction.video_id=video.id\n"
	 		+ "            where video.id=:videoId\n"
	 		+ "            group by videoname,directionname order by directionname",nativeQuery=true)
	public  Iterable<Map<String,Object>> getSummaryReport(@Param("videoId") Long videoId);
}
