package com.hardik.bojack.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class HouseDto implements Serializable {

	private static final long serialVersionUID = 7373567029129031276L;
	private final UUID id;
	private final String name;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

}
