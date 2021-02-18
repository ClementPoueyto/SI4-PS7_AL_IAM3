package com.aliam3.polyvilleactive.dsl.listeners;

import com.aliam3.polyvilleactive.MemoryRule;
import com.aliam3.polyvilleactive.dsl.Action;
import com.aliam3.polyvilleactive.dsl.DSLListener;
import com.aliam3.polyvilleactive.dsl.DSLParser;
import com.aliam3.polyvilleactive.dsl.DSLParser.ActionContext;
import com.aliam3.polyvilleactive.dsl.DSLParser.AleaContext;
import com.aliam3.polyvilleactive.dsl.DSLParser.InterdictionContext;
import com.aliam3.polyvilleactive.dsl.events.*;
import com.aliam3.polyvilleactive.dsl.ClauseVerificator;
import com.aliam3.polyvilleactive.dsl.Regle;
import com.aliam3.polyvilleactive.dsl.events.transportation.Avance;
import com.aliam3.polyvilleactive.dsl.events.transportation.Panne;
import com.aliam3.polyvilleactive.dsl.events.transportation.Rempli;
import com.aliam3.polyvilleactive.dsl.events.transportation.Retard;
import com.aliam3.polyvilleactive.dsl.events.weather.RainEvent;
import com.aliam3.polyvilleactive.dsl.events.weather.SnowEvent;
import com.aliam3.polyvilleactive.dsl.events.weather.SunEvent;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CustomListener implements DSLListener {
	ClauseVerificator clauseVerificator;
	/**
	 * boolean qui permet de regarder si l'état de l'expression( correct ou incorrect)
	 * si oui, alors il ne fait que regarder l'expression
	 * si non, il ajoute les regles dans le moteur
	 */
	boolean verifyOnly;

	Evenement event;
	Action action;

	public CustomListener(boolean verifyOnly) {
		super();
		this.verifyOnly = verifyOnly;
	}

	// --------- SENTENCES ----------

	@Override
	public void enterPriorite(DSLParser.PrioriteContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitPriorite(DSLParser.PrioriteContext ctx) {
		ModeTransport mode = parseTransportMode(ctx.type);
		clauseVerificator.priorite(ctx.instruction, mode, ctx.rank);
	}

	@Override
	public void enterMeteo(DSLParser.MeteoContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitMeteo(DSLParser.MeteoContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void enterGlobal(DSLParser.GlobalContext ctx) {
		clauseVerificator = new ClauseVerificator();
	}

	@Override
	public void exitGlobal(DSLParser.GlobalContext ctx) {
		clauseVerificator.verify(); // throws SemanticException
		if(!verifyOnly)
			MemoryRule.changeGlobalPriorities(clauseVerificator.createPriorities());
	}

	@Override
	public void enterWhenBlock(DSLParser.WhenBlockContext ctx) {
		clauseVerificator.resetList();
	}

	@Override
	public void exitWhenBlock(DSLParser.WhenBlockContext ctx) {
		if(!verifyOnly)
			MemoryRule.addLocalRules(new Regle(event, action));
	}

	@Override
	public void enterTransport(DSLParser.TransportContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitTransport(DSLParser.TransportContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void enterProg(DSLParser.ProgContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitProg(DSLParser.ProgContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void enterLocal(DSLParser.LocalContext ctx) {
		if(!verifyOnly)
			MemoryRule.resetLocalRules();

	}

	@Override
	public void exitLocal(DSLParser.LocalContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void enterEvenement(DSLParser.EvenementContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitEvenement(DSLParser.EvenementContext ctx) {
		if (ctx.meteo==null) {
			ModeTransport mode = parseTransportMode(ctx.type);
			event = createEvent(mode, parseAlea(ctx.aleas));
		} else {
			event = createMeteoEvent(parseMeteoAlea(ctx.aleas));
		}
	}

	@Override
	public void enterAction(ActionContext ctx) {
		action = new Action();

	}

	@Override
	public void exitAction(ActionContext ctx) {
		clauseVerificator.verify();
		action.setPriorities(clauseVerificator.createPriorities());
		action.setProhibitions(clauseVerificator.createProhibitions());
		

	}
	

	@Override
	public void enterAlea(AleaContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitAlea(AleaContext ctx) {
		// Not used because not necessary, but kept just in case
	}
	
	@Override
	public void enterInterdiction(InterdictionContext ctx) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitInterdiction(InterdictionContext ctx) {
		ModeTransport mode = parseTransportMode(ctx.type);
		clauseVerificator.interdire(mode);
		
	}

	// --------- SPECIAL ----------

	@Override
	public void visitTerminal(TerminalNode terminalNode) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void visitErrorNode(ErrorNode errorNode) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void enterEveryRule(ParserRuleContext parserRuleContext) {
		// Not used because not necessary, but kept just in case
	}

	@Override
	public void exitEveryRule(ParserRuleContext parserRuleContext) {
		// Not used because not necessary, but kept just in case
	}

	// -------- CUSTOM parsing ---------

	private ModeTransport parseTransportMode(String modeAsString) {
		switch (modeAsString.toLowerCase()) {
			case "bus": return ModeTransport.BUS;
			case "metro": return ModeTransport.METRO;
			case "train": return ModeTransport.TRAIN;
			case "tramway": return ModeTransport.TRAMWAY;
			case "velo": return ModeTransport.BIKE;
			default: throw new IllegalArgumentException("Given transport is not recognized");
		}
	}
	
	private Alea parseAlea(String aleaAsString) {
		switch (aleaAsString.toLowerCase()) {
			case "retard": return Alea.RETARD;
			case "avance": return Alea.AVANCE;
			case "rempli": return Alea.REMPLI;
			case "panne": return Alea.PANNE;
			default: throw new IllegalArgumentException("Given Alea is not recognized");
		}
	}

	private Alea parseMeteoAlea(String aleaAsString) {
		switch (aleaAsString.toUpperCase()) {
			case "SOLEIL": return Alea.SOLEIL;
			case "PLUIE": return Alea.PLUIE;
			case "NEIGE": return Alea.NEIGE;
			default: throw new IllegalArgumentException("Given Alea is not recognized");
		}
	}

	private Evenement createEvent(ModeTransport transport, Alea alea) {
		switch (alea) {
			case PANNE: return new Panne(transport);
			case AVANCE: return new Avance(transport);
			case REMPLI: return new Rempli(transport);
			case RETARD: return new Retard(transport);
			default: return null;
		}
	}

	private Evenement createMeteoEvent(Alea meteoAlea) {
		switch (meteoAlea) {
			case SOLEIL: return new SunEvent();
			case PLUIE : return new RainEvent();
			case NEIGE : return new SnowEvent();
			default: return null;
		}
	}




}
