package com.behl.cachetropolis.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "wizards")
public class Wizard {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private UUID id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "house_id", nullable = false)
	private UUID houseId;

	@Column(name = "wand_type")
	private String wandType;
	
	@Column(name = "quidditch_position")
	private String quidditchPosition;
	
	@Column(name = "blood_status")
	private String bloodStatus;
	
	@Column(name = "patronus")
	private String patronus;
	
	@Setter(AccessLevel.NONE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "house_id", nullable = false, insertable = false, updatable = false)
	private MasterHouse masterHouse;

	@Setter(AccessLevel.NONE)
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Setter(AccessLevel.NONE)
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
