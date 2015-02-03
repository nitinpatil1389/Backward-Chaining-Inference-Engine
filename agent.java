import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;


public class agent {

	public static void main(String[] args) {
		HashMap<String, PredicateHashMap> KB = new HashMap<String, PredicateHashMap>();
		PrintWriter writer = null;
		
		try{
			String query;
			String sentence, literal, constants;
			String[] splitConstants;
			int noOfClauses;
			Sentence sentenceObj;
			
			BufferedReader reader = new BufferedReader(new FileReader(new File("input.txt")));
			
			query=reader.readLine();
			//process the query
			Predicate queryObj = new Predicate(); //positive literal
			
			literal = query.substring(0, query.indexOf('(')); //"Diagnosis"
			queryObj.setLiteral(literal); //add predicate's literal symbol
			
			constants = query.substring(query.indexOf('(')+1, query.indexOf(')')); //"x,Infected"
			splitConstants = constants.split(","); //"x", "Infected"
			queryObj.setConstants(splitConstants); //add predicate's constants
			
			noOfClauses = Integer.parseInt(reader.readLine());
			for (int i = 0; i < noOfClauses; i++){
				sentence = reader.readLine();
				String variable = "x"+i;
				if(sentence.contains("=>")){
					String premise, conclusion;
					String[] splitSentence, splitPremise;
					
					splitSentence = sentence.split("=>");//split to get left premises and right conclusion
					
					premise = splitSentence[0];//"LostWeight(x)&Diagnosis(x,LikelyInfected)"
					conclusion = splitSentence[1];//"Diagnosis(x,Infected)"
					
					//process conclusion - has one literal and utmost 2 constants
					Predicate conclusionPredicate = new Predicate(); //conclusion predicate
					
					literal = conclusion.substring(0, conclusion.indexOf('(')); //"Diagnosis"
					conclusionPredicate.setLiteral(literal); //add predicate's literal symbol
					
					constants = conclusion.substring(conclusion.indexOf('(')+1, conclusion.indexOf(')')); //"x,Infected"
					splitConstants = constants.split(","); //"x", "Infected"
					for(int k=0; k<splitConstants.length; k++)
						if(splitConstants[k].equals("x"))
							splitConstants[k] = variable;
					conclusionPredicate.setConstants(splitConstants); //add predicate's constants
					//conclusion processed
					
					//process premise - one or more literals and utmost 2 constants per literal
					splitPremise = premise.split("&");//"LostWeight(x)", "Diagnosis(x,LikelyInfected)"
					Predicate[] premisePredicates = new Predicate[splitPremise.length]; //premise predicates list array
					
					for(int j = 0; j<splitPremise.length; j++){
						Predicate eachPremisePredicate = new Predicate();//current premise object
						
						literal = splitPremise[j].substring(0, splitPremise[j].indexOf('('));//"LostWeight" or "Diagnosis"
						eachPremisePredicate.setLiteral(literal);//add predicate's literal symbol
						
						constants = splitPremise[j].substring(splitPremise[j].indexOf('(')+1, splitPremise[j].indexOf(')'));//"x" or "x,LikelyInfected"
						splitConstants = constants.split(",");//"x" or "x", "LikelyInfected"
						for(int k=0; k<splitConstants.length; k++)
							if(splitConstants[k].equals("x"))
								splitConstants[k] = variable;
						eachPremisePredicate.setConstants(splitConstants);//add predicate's constants
						
						premisePredicates[j] = eachPremisePredicate;//add to array
					}
					
					sentenceObj = new Sentence(); //create sentence
					sentenceObj.setConclusion(conclusionPredicate); //set conclusion of the sentence
					sentenceObj.setPremise(premisePredicates);//set premise of sentence
					
					literal = sentenceObj.getConclusion().getLiteral();
					if(!KB.containsKey(literal))//check if hash value exists in hash map
						KB.put(literal, new PredicateHashMap());//if not, add it
					KB.get(literal).addConclusionSentence(sentenceObj);
					
					for(int j=0; j<premisePredicates.length; j++){
						literal = premisePredicates[j].getLiteral();
						if(!KB.containsKey(literal))//check if hash value exists in hash map
							KB.put(literal, new PredicateHashMap());//if not, add it
						KB.get(literal).addPremiseSentence(sentenceObj);
					}
						
				}
				else{
						Predicate conclusionPredicate = new Predicate(); //positive literal
						
						literal = sentence.substring(0, sentence.indexOf('(')); //"Diagnosis"
						conclusionPredicate.setLiteral(literal); //add predicate's literal symbol
						
						constants = sentence.substring(sentence.indexOf('(')+1, sentence.indexOf(')')); //"x,Infected"
						splitConstants = constants.split(","); //"x", "Infected"
						for(int k=0; k<splitConstants.length; k++)
							if(splitConstants[k].equals("x"))
								splitConstants[k] = variable;
						conclusionPredicate.setConstants(splitConstants); //add predicate's constants
						
						Predicate[] premisePredicates = new Predicate[0];
						
						sentenceObj = new Sentence(); //create sentence
						sentenceObj.setConclusion(conclusionPredicate); //set conclusion of the sentence
						sentenceObj.setPremise(premisePredicates);//set premise of sentence
						
						literal = sentenceObj.getConclusion().getLiteral();
						if(!KB.containsKey(literal))//check if hash value exists in hash map
							KB.put(literal, new PredicateHashMap());//if not, add it
						KB.get(literal).addConclusionSentence(sentenceObj);
				}
			}
		
			reader.close();
			
			writer = new PrintWriter("output.txt");
			if(FOL_BC_ASK(KB, queryObj))
				writer.print("TRUE");
			else
				writer.print("FALSE");	
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			writer.print("FALSE");
			return;
		}finally{
			if(writer != null)
				writer.close();
		}
	}

	private static boolean FOL_BC_ASK(HashMap<String, PredicateHashMap> KB, Predicate query) {
		LinkedList<HashMap<String, String>> substitutions = FOL_BC_OR(KB, query, new HashMap<String, String>());
		if(substitutions.size() > 0)
			return true;
		return false;
	}

	private static LinkedList<HashMap<String, String>> FOL_BC_OR(HashMap<String, PredicateHashMap> KB, Predicate goal, HashMap<String, String> theta) {
		LinkedList<Sentence> sentences = FETCH_RULES_FOR_GOAL(KB, goal);
		LinkedList<HashMap<String, String>> theta1List = new LinkedList<HashMap<String,String>>();
		//for each rule (lhs => rhs) in FETCH-RULES-FOR-GOAL(KB, goal ) do
		for(int i=0; i<sentences.size(); i++){
			Predicate rhs = sentences.get(i).getConclusion();
			Predicate[] lhs = sentences.get(i).getPremise();
			//for each theta1 in FOL-BC-AND(KB, lhs, UNIFY(rhs, goal , theta)) do
			HashMap<String, String> thetaCopy = new HashMap<String, String>(theta);
			LinkedList<HashMap<String, String>> theta1 = FOL_BC_AND(KB, lhs, UNIFY(rhs, goal, thetaCopy));//if UNIFY returns null then failure - for example UNIFY(A(A,B) and A(A))
			theta1List.addAll(theta1);
		}
		return theta1List;
	}

	private static LinkedList<Sentence> FETCH_RULES_FOR_GOAL(HashMap<String, PredicateHashMap> KB, Predicate goal) {
		LinkedList<Sentence> sentences = KB.get(goal.getLiteral()).getListOfSentencesInConclusion();
		LinkedList<Sentence> outputSentences = new LinkedList<Sentence>();
		String[] currentSentenceConclusionConstants;
		String[] goalConstants = goal.getConstants();
		for(int i=0; i< sentences.size(); i++){
			currentSentenceConclusionConstants = sentences.get(i).getConclusion().getConstants();
			if(currentSentenceConclusionConstants.length != goalConstants.length)
				continue;
			
			outputSentences.add(sentences.get(i));
		}
		return outputSentences;
	}

	private static HashMap<String, String> UNIFY(Predicate rhs, Predicate goal, HashMap<String, String> theta) {
		if(theta==null)
			return null;
		String[] rhsConstants = rhs.getConstants();
		String[] goalConstants = goal.getConstants();
		if(rhsConstants.length==1)
			return UNIFY(rhsConstants[0], goalConstants[0], theta);
		else
			return UNIFY(rhsConstants, goalConstants, theta);
	}

	private static HashMap<String, String> UNIFY(String[] rhsConstants, String[] goalConstants, HashMap<String, String> theta) {
		if(theta==null)
			return null;
		return UNIFY(rhsConstants[1], goalConstants[1], UNIFY(rhsConstants[0], goalConstants[0], theta));
	}

	private static HashMap<String, String> UNIFY(String rhsConstant, String goalConstant,	HashMap<String, String> theta) {
		if(theta==null)
			return null;
		else if(rhsConstant.equals(goalConstant))
			return theta;
		else if(rhsConstant.startsWith("x"))
			return UNIFY_VAR(rhsConstant, goalConstant, theta);
		else if(goalConstant.startsWith("x"))
			return UNIFY_VAR(goalConstant, rhsConstant, theta);
		else
			return null;
	}

	private static HashMap<String, String> UNIFY_VAR(String var, String x, HashMap<String, String> theta) {
		if(theta.containsKey(var))
			return UNIFY(theta.get(var), x, theta);
		else if(theta.containsKey(x))
			return UNIFY(var, theta.get(x), theta);
		/*else if(OCCUR_CHECK(var, x))
			return null;*/
		else{
			theta.put(var, x);
			return theta;
		}
	}

	private static LinkedList<HashMap<String, String>> FOL_BC_AND(HashMap<String, PredicateHashMap> KB, Predicate[] goals, HashMap<String, String> theta) {
		LinkedList<HashMap<String,String>>thetaList = new LinkedList<HashMap<String,String>>();
		//if theta = failure then return
		if(theta==null)
			return thetaList;
		//else if LENGTH(goals) = 0 then yield theta
		else if(goals.length==0){
			thetaList.add(theta);
			return thetaList;
		}
		//else do
		else{
			//first,rest <- FIRST(goals), REST(goals)
			Predicate first = goals[0];
			Predicate[] rest = new Predicate[goals.length-1];
			for(int i=1; i<goals.length; i++)
				rest[i-1]=goals[i];
			//for each theta1 in FOL-BC-OR(KB, SUBST(theta, first), theta) do
			LinkedList<HashMap<String, String>> theta1 = FOL_BC_OR(KB, SUBST(theta, first), theta);
			//for each theta2 in FOL-BC-AND(KB, rest , theta1) do
			for(int i=0; i<theta1.size(); i++){
				LinkedList<HashMap<String, String>> theta2 = FOL_BC_AND(KB, rest, theta1.get(i));
				thetaList.addAll(theta2);
			}
			//yield theta2
			return thetaList;
		}
	}

	private static Predicate SUBST(HashMap<String, String> theta, Predicate first) {
		Predicate substitutedPredicate = new Predicate();
		substitutedPredicate.setLiteral(first.getLiteral());
		String[] constants = new String[first.getConstants().length];
		for(int i=0; i<constants.length; i++){
			String constant = first.getConstants()[i];
			if(constant.startsWith("x") && theta.containsKey(constant)){
				String cur = theta.get(constant);
				//loop for substitutions like {x0|x1, x1|x2, x2|ABC}
				while(cur.startsWith("x") && theta.containsKey(cur))
					cur=theta.get(cur);
				constants[i] = cur;
			}
			else
				constants[i] = constant;
		}
		substitutedPredicate.setConstants(constants);
			
		return substitutedPredicate;
	}

}
