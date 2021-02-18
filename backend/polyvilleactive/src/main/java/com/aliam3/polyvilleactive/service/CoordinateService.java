package com.aliam3.polyvilleactive.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Classe qui gere les coord
 *
 * @author vivian
 * @author clement
 */
@Service
public class CoordinateService {

	private RestTemplate restTemplate = new RestTemplate();


	/**
	 * permet de faire la conversion entre nom adresse et les coordonnes
	 * latitudes/longitudes
	 *
	 * @param locationAddress
	 * @return reponse de la requete
	 */
	public ResponseEntity getPositionFromAddress(String locationAddress) {
		String locationAddres = locationAddress.replace(" ", "+");
		final String URL = "https://nominatim.openstreetmap.org/search?q=" + locationAddres + "&format=json";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(headers);
		String res = "";
		try {

			ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request,
					new ParameterizedTypeReference<String>() {
					});

			res = response.getBody();
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Journey found" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

}
