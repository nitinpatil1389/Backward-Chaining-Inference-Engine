import java.util.LinkedList;


public class PredicateHashMap {
	//private LinkedList<Predicate> listOfPositiveLiterals = new LinkedList<Predicate>();
	private LinkedList<Sentence> listOfSentencesInPremise = new LinkedList<Sentence>();
	private LinkedList<Sentence> listOfSentencesInConclusion = new LinkedList<Sentence>();
	
	/*public void addPositiveLiteral(Predicate positiveLiteral){
		listOfPositiveLiterals.add(positiveLiteral);
	}*/
	
	public void addPremiseSentence(Sentence premiseSentence){
		listOfSentencesInPremise.add(premiseSentence);
	}
	
	public void addConclusionSentence(Sentence conclusionSentense){
		listOfSentencesInConclusion.add(conclusionSentense);
	}

	/**
	 * @return the listOfPositiveLiterals
	 */
	/*public LinkedList<Predicate> getListOfPositiveLiterals() {
		return listOfPositiveLiterals;
	}*/

	/**
	 * @return the listOfSentencesInPremise
	 */
	public LinkedList<Sentence> getListOfSentencesInPremise() {
		return listOfSentencesInPremise;
	}

	/**
	 * @return the listOfSentencesInConclusion
	 */
	public LinkedList<Sentence> getListOfSentencesInConclusion() {
		return listOfSentencesInConclusion;
	}
}
