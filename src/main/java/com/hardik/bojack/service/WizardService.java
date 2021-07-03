package com.hardik.bojack.service;

import java.util.UUID;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hardik.bojack.dto.WizardCreationRequestDto;
import com.hardik.bojack.entity.Wizard;
import com.hardik.bojack.repository.WizardRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WizardService {

	private final WizardRepository wizardRepository;

	public ResponseEntity<?> create(final WizardCreationRequestDto wizardCreationRequestDto) throws JSONException {

		final var wizard = new Wizard();
		wizard.setFirstName(wizardCreationRequestDto.getFirstName());
		wizard.setMiddleName(wizardCreationRequestDto.getMiddleName());
		wizard.setLastName(wizardCreationRequestDto.getLastName());
		wizard.setGender(wizardCreationRequestDto.getGender());
		wizard.setHouseId(wizardCreationRequestDto.getHouseId());

		final var savedWizard = wizardRepository.save(wizard);

		if (savedWizard == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		final var response = new JSONObject();
		response.put("Wizard-ID", savedWizard.getId().toString());

		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> update(final UUID wizardId, final WizardCreationRequestDto wizardCreationRequestDto)
			throws JSONException {

		final var wizard = wizardRepository.findById(wizardId).get();
		wizard.setFirstName(wizardCreationRequestDto.getFirstName());
		wizard.setMiddleName(wizardCreationRequestDto.getMiddleName());
		wizard.setLastName(wizardCreationRequestDto.getLastName());
		wizard.setGender(wizardCreationRequestDto.getGender());
		wizard.setHouseId(wizardCreationRequestDto.getHouseId());

		final var updatedWizard = wizardRepository.save(wizard);

		if (updatedWizard == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		final var response = new JSONObject();
		response.put("Wizard-ID", updatedWizard.getId().toString());

		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> retreiveById(final UUID wizardId) {

		final var wizard = wizardRepository.findById(wizardId);

		if (wizard.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		return ResponseEntity.ok(wizard);
	}

	public ResponseEntity<?> delete(final UUID wizardId) {
		wizardRepository.deleteById(wizardId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
