package com.masterteknoloji.trafficanalyzer.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;


/**
 * Spring Data JPA repository for the VideoRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRecordRepository extends JpaRepository<VideoRecord, Long> {
    @Query("select v.id as id,"
    		+ "    v.vehicleType as vehicleType,"
    		+ "    v.insertDate as insertDate,"
    		+ "    v.videoLine.id as lineId,"
    		+ "    v.duration as duration, "
    		+ "    v.speed as speed "
    		+ "    from VideoRecord v where v.videoLine.video.id = ?1 order by v.insertDate")
    Iterable<Map<String,Object>> findAllByVideoId(Long id);

    @Query(value="Select vehicle_type as type, from_unixtime(FLOOR(UNIX_TIMESTAMP(insert_date)/(5*60))*(5*60)) GroupTime,\n"
    		+ "COUNT(*) as counts\n"
    		+ "FROM video_record i\n"
    		+ "INNER JOIN video_line as line ON i.video_line_id=line.id\n"
    		+ "INNER JOIN video ON line.video_id=video.id\n"
    		+ "where video_id=:videoId\n"
    		+ "GROUP BY GroupTime,vehicle_type",nativeQuery=true)
	public  Iterable<Map<String,Object>> getSummaryReport(@Param("videoId") Long videoId);
}
