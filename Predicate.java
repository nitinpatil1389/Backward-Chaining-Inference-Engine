
public class Predicate {
	private String literal;
	private String[] constants;
	/**
	 * @return the predicates
	 */
	public String getLiteral() {
		return literal;
	}
	/**
	 * @param predicates the predicates to set
	 */
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	/**
	 * @return the constants
	 */
	public String[] getConstants() {
		return constants;
	}
	/**
	 * @return the constant[n]
	 */
	public String getConstants(int n) {
		return constants[n];
	}
	/**
	 * @param constants the constants to set
	 */
	public void setConstants(String[] constants) {
		this.constants = constants;
	}
	public void setConstants(int n, String constant) {
		this.constants[n] = constant;
	}
}
