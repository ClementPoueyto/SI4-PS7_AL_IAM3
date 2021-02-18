package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.model.location.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Classe qui gere les reponses avec l'API Navitia
 * 
 * @author vivian
 * @author clement
 */
@Service
@PropertySource(value = { "classpath:application.properties" })
public class NavitiaService {

	@Value("${api.url}")
	private String url;

	@Value("${api.key}")
	private String apiKey;

	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * permet d'avoir la liste des trajets en fonction des positions soumises par
	 * l'utilisateur
	 * 
	 * @param from        - Position de l'utilisateur
	 * @param to          - Position de l'utilisateur
	 * @param dataSources - data de navitia
	 * @param mock
	 * @return reponse de la requete
	 */
	public ResponseEntity getJourneyInfo(Position from, Position to, NavitiaDatasources dataSources, boolean mock) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(headers);
		String res = "";
		String URL = url + dataSources + "journeys?from=" + from.getNavitiaPosition() + "&to="
				+ to.getNavitiaPosition();
		try {
			if (!mock) {
				ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, request,
						new ParameterizedTypeReference<String>() {
						});

				res = response.getBody();
			} else {
				/* WARNING : do not remove (needed for demo) */
				// res=mockNavitia();
				res = demoNavitia(dataSources);
			}
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Journey found" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	public String mockNavitia() {
		MockAPI mockAPI = new MockAPI();

		return mockAPI.loadResource("mock/test1.json");

	}

	public String demoNavitia(NavitiaDatasources dataSource) {
		MockAPI mockAPI = new MockAPI();
		switch (dataSource) {
		case ILE_DE_FRANCE:
			return mockAPI.loadResource("demo1/fridf.json");
		case NONE:
			return mockAPI.loadResource("demo1/none.json");
		case NORTH_WEST:
			return mockAPI.loadResource("demo1/frnw.json");
		case NORTH_EAST:
			return mockAPI.loadResource("demo1/frne.json");
		case NORMANDIE:
			return mockAPI.loadResource("demo1/fnor.json");
		default:
			return "";
		}

	}

}
