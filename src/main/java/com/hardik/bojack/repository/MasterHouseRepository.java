package com.hardik.bojack.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hardik.bojack.entity.MasterHouse;

@Repository
public interface MasterHouseRepository extends JpaRepository<MasterHouse, UUID> {

}
