package com.behl.cachetropolis.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.behl.cachetropolis.entity.Wizard;

@Repository
public interface WizardRepository extends JpaRepository<Wizard, UUID> {

	List<Wizard> findByHouseId(UUID houseId);

}
