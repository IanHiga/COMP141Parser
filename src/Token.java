
/*
 * COMP 141- Programming Languages
 * Name: Ian Higa
 * Project Phase: 2.1
 */
public class Token {
	private String value;
	private String type;
	
	public Token(String tokenType, String tokenValue) {
		type = tokenType;
		value = tokenValue;
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return(type + " : " + value + "\n");
	}
}
