package com.behl.cachetropolis.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.behl.cachetropolis.entity.MasterHouse;

@Repository
public interface MasterHouseRepository extends JpaRepository<MasterHouse, UUID> {

	@Query(value = "SELECT * FROM master_houses ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Optional<MasterHouse> fetchRandom();

}
