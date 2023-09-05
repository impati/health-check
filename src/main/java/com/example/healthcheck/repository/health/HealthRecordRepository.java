package com.example.healthcheck.repository.health;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

	@Query(" select record " +
		" from HealthRecord  record " +
		" join fetch record.server s " +
		" where s in(:activeServer) " +
		" and record.createdAt > :afterTime " +
		" and record in" +
		"    (( select another " +
		"       from HealthRecord  another " +
		"       join another.server anothers " +
		"       where anothers = s " +
		"       order by another.createdAt desc  " +
		"       limit 1))")
	List<HealthRecord> findLatestRecordOfActiveServer(
		@Param("activeServer") final List<Server> activeServer,
		@Param("afterTime") final LocalDateTime afterTime
	);

	@Query(" select record from HealthRecord record " +
		" where record.server.id = :server " +
		" order by record.createdAt desc " +
		" limit 1")
	HealthRecord findLatestRecordOfActiveServer(@Param("server") final Long serverId);

	Optional<HealthRecord> findTopByServerOrderByCreatedAtDesc(final Server server);
}
