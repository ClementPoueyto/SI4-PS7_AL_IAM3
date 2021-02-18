package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.dsl.Prohibition;
import com.aliam3.polyvilleactive.model.deserializer.JourneyDeserializer;
import com.aliam3.polyvilleactive.model.gamification.Demands;
import com.aliam3.polyvilleactive.model.gamification.ResponseDemands;
import com.aliam3.polyvilleactive.model.location.Position;
import com.aliam3.polyvilleactive.model.location.StepState;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.transport.Section;
import com.aliam3.polyvilleactive.model.transport.Transport;
import com.aliam3.polyvilleactive.model.user.Form;
import com.aliam3.polyvilleactive.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe qui gere l'integralite des trajets actuels et en cours
 *
 * @author vivian
 * @author clement
 */
@Service
public class JourneyService {

	@Autowired
	NavitiaService navitiaService;

	@Autowired
	IncidentService incidentService;

	@Autowired
	UserService userService;

	ObjectMapper objectMapper = new ObjectMapper();

	public List<Journey> journeyList = new ArrayList<>();

	public List<Demands> demandsList;

	List<Demands> handledDemands;

	public JourneyService() {
		demandsList = new ArrayList<>();
		Transport mockTransport = new Transport();
		mockTransport.setLine("");
		// demandsList.add(new Demands(1,2,mockTransport));
		handledDemands = new ArrayList<>();
	}

	/**
	 * renvoie un trajet selon tous les options donnes soit par l'utilisateur, soit
	 * par les regles du DSL
	 *
	 * @param from - Position de depart
	 * @param to   - Position de depart
	 * @param id   - id utilistaeur
	 * @param form - preference de l'utilisateur
	 * @param mock
	 * @return le trajet a renvoyer a l'utilisateur
	 */
	public Journey getJourney(Position from, Position to, long id, Form form, boolean mock) {

		List<Journey> myJourneys = new ArrayList<>(); // new journeys from all Navitia sources
		for (NavitiaDatasources value : NavitiaDatasources.values()) {
			myJourneys.addAll(callNavitia(from, to, value, mock));
		}
		if (!myJourneys.isEmpty()) {
			Journey newJourney = engine(myJourneys, id, extractProhibitions(form), form.isGreen());
			if (newJourney == null) {
				return null;
			}
			updateList(newJourney);
			System.out.println(newJourney);
			return newJourney;
		} else {
			return null;
		}
	}

	/**
	 * recupere les moyens de transport que l'utilisateur ne veut pas prendre
	 *
	 * @param form - preference utilisateur
	 * @return liste des moyens de transport a ne pas prendre
	 */
	private List<Prohibition> extractProhibitions(Form form) {
		List<Prohibition> prohibitions = new ArrayList<>();
		for (ModeTransport transport : form.getFilters()) {
			prohibitions.add(new Prohibition(transport));
		}
		return prohibitions;
	}

	/**
	 * fonction pour recuperer les informations des trajets par Navitia
	 *
	 * @param from        - Position de l'utilisateur
	 * @param to          - Position de l'utilisateur
	 * @param datasources
	 * @param mock
	 * @return liste des trajets renvoye par Navitia
	 */
	public List<Journey> callNavitia(Position from, Position to, NavitiaDatasources datasources, boolean mock) {
		ResponseEntity response = navitiaService.getJourneyInfo(from, to, datasources, mock);

		if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != "") {
			return jsonJourneyToObject(response.getBody().toString());
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * deserializer de Journey
	 *
	 * @param json
	 * @return liste des trajets sous forme d'objets
	 */
	public List<Journey> jsonJourneyToObject(String json) {
		SimpleModule module = new SimpleModule("CustomDeserializer", new Version(1, 0, 0, null, null, null));
		module.addDeserializer(Journey.class, new JourneyDeserializer());
		objectMapper.registerModule(module);
		JsonNode jsonNode = null;
		List<Journey> journeys = new ArrayList<>();
		try {
			jsonNode = objectMapper.readTree(json);

			journeys = objectMapper.readValue(jsonNode.get("journeys").toPrettyString(),
					new TypeReference<List<Journey>>() {
					});

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return journeys;
	}

	public void updateList(Journey journey) {
		journeyList.removeIf(e -> e.getIdUser() == journey.getIdUser());
		journeyList.add(journey);

	}

	/**
	 * selectionne un trajet a l'utilisateur parmi une liste de trajets fournis
	 *
	 * @param journeys
	 * @param id
	 * @param prohibitions
	 * @param greenMode
	 * @return le trajet choisi
	 */
	public Journey engine(List<Journey> journeys, long id, List<Prohibition> prohibitions, boolean greenMode) {
		Journey lastJourney = journeyList.stream().filter((Journey j) -> j.getIdUser() == id).findFirst().orElse(null);
		return new Engine().selectJourney(id, lastJourney, journeys, incidentService.incidents,
				incidentService.incidentsWeather, prohibitions, greenMode);
	}

	/**
	 * Add new demands.
	 *
	 * @param demands Demands.
	 */
	public void addDemands(Demands demands) {
		demandsList.add(demands);
	}

	/**
	 * Returns the corresponding demand if it exists already
	 *
	 * @param demands Demands.
	 * @return the matching demand if it exists already, null if not
	 */
	public Demands getDemandsIfExists(Demands demands) {
		return demandsList.stream().filter(d -> demands.getTransport().equals(d.getTransport())
				&& demands.getIdUserAsking() == d.getIdUserAsking()).findFirst().orElse(null);
	}

	/**
	 * Retourne la demande faite à un utilisateur.
	 *
	 * @param idUserProviding L'identifiant de l'utilisateur qui veut répondre à une
	 *                        demande.
	 * @return Une demande faite à l'utilisateur, null si aucune
	 */
	public Demands lookForDemands(long idUserProviding) {
		Optional<Journey> myJourney = journeyList.stream().filter((Journey j) -> j.getIdUser() == idUserProviding)
				.findFirst();
		if (myJourney.isEmpty())
			return null;
		Section currentStep = myJourney.get().getCurrentStep();

		return demandsList.stream()
				.filter((Demands d) -> d.getTransport().getLine().equals(currentStep.getTransport().getLine())
						&& !d.wasThrowedTo(idUserProviding))
				.findFirst().orElse(null);
	}

	/**
	 * Annuler les demandes d'un utilisateur donné
	 *
	 * @param idUserAsking l'utilisiteur pour lequel on veut annuler les demandes
	 */
	public void cancelDemandsFrom(long idUserAsking) {
		demandsList.stream().filter(demands -> demands.getIdUserAsking() == idUserAsking)
				.forEach(Demands::end);
	}

	/**
	 * Supprime les demandes d'un utilisateur donné, comme si elle n'avait jamais
	 * été créées
	 *
	 * @param idUser utilisateur à l'origine des demandes
	 */
	private void deleteDemandsFrom(long idUser) {
		demandsList.removeIf(demands -> demands.getIdUserAsking() == idUser);
	}

	/**
	 * Sauvegarde la réponse d'un utilisateur à une demande.
	 *
	 * @param responseDemands La réponse à une demande.
	 */
	public int saveResponseDemands(ResponseDemands responseDemands, long idUser) {
		Demands demands = demandsList.stream().filter((Demands d) -> d.getId() == responseDemands.getIdDemands())
				.findFirst().orElse(null);
		if (demands == null)
			return 0;
		if (demands.hasEnded())
			return 3;
		boolean alreadyHandled = handledDemands.contains(demands);
		if (responseDemands.wasAccepted() && !alreadyHandled) {
			demands.accept();
			Optional<User> user = userService.searchForUser(idUser);
			if (user.isPresent()) user.get().addScore(50);
			return 1; // Accepted and not handled yet
		} else if (!alreadyHandled) {
			return 2; // Refused and not handled yet
		} else {
			
			return 0; // Demands has expired
		}
	}

	/**
	 * Marks as reached the section which ends at endPosition
	 *
	 * @param userId      user's id
	 * @param endPosition end position for targeted journey's section
	 * @param form        form filled by user
	 */
	public StepState validateJourneyStep(long userId, Position endPosition, Form form) {
		Optional<Journey> targetJourney = journeyList.stream().filter(journey -> journey.getIdUser() == userId)
				.findFirst();
		if (targetJourney.isEmpty()) {
			throw new IllegalArgumentException("No journey assigned to user of id " + userId);
		} else {
			Journey journey = targetJourney.get();
			Optional<Section> targetSection = journey.getSections().stream()
					.filter(s -> !s.getTransport().getModeTransport().equals(ModeTransport.WAITING)) // filter out
																										// waiting
					.filter(s -> s.getTo().getPos().equals(endPosition)).findFirst();
			if (targetSection.isPresent()) {
				targetSection.get().setReached(true);
				if (isJourneyOver(journey, targetSection.get())) {
					terminateJourney(journey, form);
					return StepState.OVER;
				} else {
					return StepState.VALIDATED;
				}

			} else {
				return StepState.ERROR;
			}

		}
	}

	/**
	 * regarde si le trajet en cours est fini
	 *
	 * @param journey
	 * @param section
	 * @return true si le trajet est fini
	 */
	boolean isJourneyOver(Journey journey, Section section) {
		return section.getTo().getPos()
				.equals(journey.getSections().get(journey.getSections().size() - 1).getTo().getPos());
	}

	/**
	 * fait terminer un trajet et assigne des points à l'utilisateur en question.
	 *
	 * @param journey
	 * @param form    - preference utilistaeur, utilise pour calculer points
	 */
	void terminateJourney(Journey journey, Form form) {
		Optional<User> userOptional = userService.searchForUser(journey.getIdUser());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.addScore(userService.calculScore(form));
			user.setBadges(userService.getUnlockedBadges(user.getScore()));
			userService.updateUser(user);
			journeyList.removeIf(e -> e.getIdUser() == journey.getIdUser());
			this.deleteDemandsFrom(user.getId());
		}
	}
}
