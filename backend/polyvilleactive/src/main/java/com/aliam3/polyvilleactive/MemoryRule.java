package com.aliam3.polyvilleactive;

import java.util.ArrayList;
import java.util.List;

import com.aliam3.polyvilleactive.dsl.Priorite;
import com.aliam3.polyvilleactive.dsl.Regle;

public class MemoryRule {

    private MemoryRule() {/* Default Constructor */}
	
	public static List<Priorite> GLOBALPRIORITIZE= new ArrayList<>();
    public static List<Regle> RULES= new ArrayList<>();

    public static void changeGlobalPriorities(List<Priorite> prio) {
        GLOBALPRIORITIZE.clear();
    	GLOBALPRIORITIZE.addAll(prio);
    }
    
    
    public static void addLocalRules(Regle regles) {
    	RULES.add(regles);
    }


	public static void resetLocalRules() {
		RULES.clear();
		
	}


}
