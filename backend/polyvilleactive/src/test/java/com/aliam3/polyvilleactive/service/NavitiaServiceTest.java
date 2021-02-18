package com.aliam3.polyvilleactive.service;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.aliam3.polyvilleactive.model.location.Position;

@SpringBootTest
public class NavitiaServiceTest {
	
	@Autowired
	NavitiaService navitiaService;
	
	@Autowired
	JourneyService journeyService;
	
	@Test
	public void getJourneyInfoTest() {
		ResponseEntity response = navitiaService.getJourneyInfo(new Position(48.85614465, 2.297820393322227), new Position(43.6595612, 1.3605827), NavitiaDatasources.ILE_DE_FRANCE, true);

		if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != "") {
			assertTrue(journeyService.jsonJourneyToObject(response.getBody().toString()).size()!=0);
		}

	}

}
