
public class Sentence {
	private Predicate conclusion;
	private Predicate[] premise;
	/**
	 * @return the conclusion
	 */
	public Predicate getConclusion() {
		return conclusion;
	}
	/**
	 * @param conclusion the conclusion to set
	 */
	public void setConclusion(Predicate conclusion) {
		this.conclusion = conclusion;
	}
	/**
	 * @return the premise
	 */
	public Predicate[] getPremise() {
		return premise;
	}
	/**
	 * @param premise the premise to set
	 */
	public void setPremise(Predicate[] premise) {
		this.premise = premise;
	}
	
}
