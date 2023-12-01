package com.behl.cachetropolis.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.behl.cachetropolis.InitializeMysqlContainer;
import com.behl.cachetropolis.entity.Wizard;

import junit.framework.AssertionFailedError;
import net.bytebuddy.utility.RandomString;

@DataJpaTest
@InitializeMysqlContainer
class WizardRepositoryTest {

	@Autowired
	private WizardRepository wizardRepository;

	@Autowired
	private MasterHouseRepository masterHouseRepository;

	@Test
	void shouldReturnWizardListByHouseId() {
		// fetch random house record from datasource
		final var masterHouse = masterHouseRepository.fetchRandom().orElseThrow(AssertionFailedError::new);
		final var houseId = masterHouse.getId();

		// creare wizard record corresponding to fetched house
		final var wizard = new Wizard();
		final var firstName = RandomString.make();
		wizard.setFirstName(firstName);
		wizard.setGender("Male");
		wizard.setHouseId(houseId);
		wizardRepository.save(wizard);

		// invoke method under test
		List<Wizard> wizards = wizardRepository.findByHouseId(houseId);

		// assert datasource response contains saved wizard record
		assertThat(wizards).isNotEmpty().map(Wizard::getFirstName).contains(firstName);
	}

	@Test
	void shouldReturnEmptyWizardListForInvalidHouseId() {
		// generate random house-id
		final var invalidHouseId = UUID.randomUUID();

		// invoke method under test
		List<Wizard> wizards = wizardRepository.findByHouseId(invalidHouseId);

		// assert datasource response is empty list
		assertThat(wizards).isEmpty();
	}

}
