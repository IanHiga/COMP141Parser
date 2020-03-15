import java.util.LinkedList;

import jdk.nashorn.internal.runtime.ParserException;

/*
 * COMP 141- Programming Languages
 * Name: Ian Higa
 * Project Phase: 2.1
 */
public class Parser {
	private LinkedList<Token> tokens;
	private Token next;
	
	public String parseList(LinkedList<Token> tokenList) {
		String out = "";
		tokens = (LinkedList<Token>) tokenList.clone();
		next = tokens.getFirst();
		
		Tree root = parseExpression();
		
		if(next == null) {
			throw new ParserException("Empty List!");
		}		
		out += root.toString();
		return out;
	}

	private void nextToken() {
		tokens.pop();
		if(tokens.isEmpty()) {
			next = new Token("END", "");
		}
		else {
			next = tokens.getFirst();
		}
	}
	
	private Tree parseExpression() {
		Tree term = parseTerm();
		if(next.getValue().contentEquals("+")) {
			Token op = next;
			nextToken();
			return(new Tree(op, term, parseExpression()));
		}
		return term;
	}
	
	private Tree parseTerm() {
		Tree factor = parseFactor();
		if(next.getValue().contentEquals("-")) {
			Token op = next;
			nextToken();
			return(new Tree(op, factor, parseTerm()));
		}
		return factor;
	}
	
	private Tree parseFactor() {
		Tree piece = parsePiece();
		if(next.getValue().contentEquals("/")) {
			Token op = next;
			nextToken();
			return(new Tree(op, piece, parseFactor()));
		}
		return piece;
	}
	
	private Tree parsePiece() {
		Tree element = parseElement();
		if(next.getType().contentEquals("END")) {
			return element;
		}
		if(next.getValue().contentEquals("*")) {
			Token op = next;
			nextToken();
			return(new Tree(op, element, parsePiece()));
		}
		return element;
	}
	
	private Tree parseElement() {
		Tree node;
		if(next.getValue().contentEquals("(")) {
			nextToken();
			node = parseExpression();
			if(next.getValue().contentEquals(")")) {
				nextToken();
			}
			else {
				//ERROR
				System.out.println("FATAL ERROR 1. Token was " + next.getValue() + " " + next.getType());
			}
			return node;
		}
		else {
			if(!isElement()) {
				//ERROR
				System.out.println("FATAL ERROR 2. Token was " + next.getValue() + " " + next.getType());
				return null;
			}
			Token op = next;
			nextToken();
			return (new Tree(op, null, null));
		}
		
	}
	
	private boolean isElement() {
		if(next.getType().contentEquals("NUMBER") || next.getType().contentEquals("IDENTIFIER")) {
			return true;
		}
		return false;
	}
}
