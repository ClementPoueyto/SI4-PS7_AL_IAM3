package com.aliam3.polyvilleactive.controller;

import com.aliam3.polyvilleactive.dsl.Interpreter;
import com.aliam3.polyvilleactive.exception.SemanticException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class DSLController {
	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 *  route pour envoyer le programme fait a partir de l'application Web. Regle transport
	 * @param body
	 * @return reponse hhtp
	 * @throws IOException
	 */
	@PostMapping("/program")
	public ResponseEntity postProgram(@RequestBody String body) throws IOException {
		JsonNode jsonNode;
		Boolean verify;
		try {
			jsonNode = objectMapper.readTree(body);
			String program = objectMapper.readValue(jsonNode.get("program").toString(), String.class);
			verify = objectMapper.readValue(jsonNode.get("verifyOnly").toString(), Boolean.class);
			new Interpreter().read(program, verify);

		} catch (ParseCancellationException | SemanticException e) {
			
			return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(e.getMessage()));
		}
		if(Boolean.TRUE.equals(verify))
			return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString("Votre programme est OK, vous pouvez l'envoyer"));
		else
			return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString("Votre programme est OK, les modifications ont étés prises en compte"));
	}
}
