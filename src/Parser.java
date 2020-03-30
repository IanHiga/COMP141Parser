import java.util.LinkedList;

import jdk.nashorn.internal.runtime.ParserException;

/*
 * COMP 141- Programming Languages
 * Name: Ian Higa
 * Project Phase: 2.2
 */
public class Parser {
	private LinkedList<Token> tokens;
	private Token next;
	
	public String parseList(LinkedList<Token> tokenList) {
		String out = "";
		tokens = (LinkedList<Token>) tokenList.clone();
		next = tokens.getFirst();
		
		Tree root = parseStatement();
		
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
	
	//Statement > Basestatement > Assignment > IfStatement > WhileStatement
	private Tree parseStatement() {
		Tree base = parseBaseStatement();
		if(base.getValue().getType().contentEquals("ERROR")) {
			return base;
		}
		if(next.getValue().contentEquals(";")) {
			Token op = next;
			nextToken();
			return(new Tree(op, base, parseStatement()));
		}
		return base;
	}
	
	private Tree parseBaseStatement() {
		Tree base = null;
		if(next.getValue().contentEquals("if")) {
			//IF STATEMENT
			base = parseIfStatement();
		}
		else if(next.getValue().contentEquals("while")){
			//WHILE STATEMENT
			base = parseWhileStatement();
		}
		else if(next.getType().contentEquals("IDENTIFIER")) {
			//ASSIGNMENT
			base = parseAssignment();
		}
		else if(next.getValue().contentEquals("skip")) {
			base = parseElement();
		}
		else {
			//Error
			Token err = new Token("ERROR", "FATAL ERROR. Expected tokens: 'if', 'while', <IDENTIFIER>, or 'skip' , token was " 
					+ next.getType() + " -> " + next.getValue());
			base = new Tree(err, null, null);
		}
		return base;
	}
	
	private Tree parseAssignment() {
		Tree id = parseElement();
		if(!(next.getValue().contentEquals(":="))) {
			//Error
			Token err = new Token("ERROR", "FATAL ERROR. Expected token ':=', token was " + next.getType() + " -> " + next.getValue());
			return(new Tree(err, null, null));
		}
		nextToken();
		Tree exp = parseExpression();
		Token op = new Token("SYMBOL", ":=");
		return(new Tree(op, id, exp));
	}
	
	private Tree parseIfStatement() {
		nextToken();
		Tree exp = parseExpression();
		if(!(next.getValue().contentEquals("then"))) {
			//Error
			Token err = new Token("ERROR", "FATAL ERROR. Expected token 'then', token was " + next.getType() + " -> " + next.getValue());
			return(new Tree(err, null, null));
		}
		nextToken();
		Tree state1 = parseStatement();
		if(!(next.getValue().contentEquals("else"))) {
			//Error
			Token err = new Token("ERROR", "FATAL ERROR. Expected token 'else', token was " + next.getType() + " -> " + next.getValue());
			return(new Tree(err, null, null));
		}
		nextToken();
		Tree state2 = parseStatement();
		if(!(next.getValue().contentEquals("endif"))) {
			//Error
			Token err = new Token("ERROR", "FATAL ERROR. Expected token 'endif', token was " + next.getType() + " -> " + next.getValue());
			return(new Tree(err, null, null));
		}
		Token op = new Token("KEYWORD", "IF-STATEMENT");
		return(new Tree(op, exp, state1, state2));
		
	}
	
	private Tree parseWhileStatement() {
		nextToken();
		Tree exp = parseExpression();
		if(!(next.getValue().contentEquals("do"))) {
			//Error
			Token err = new Token("ERROR", "FATAL ERROR. Expected token 'do', token was " + next.getType() + "->" + next.getValue());
			return(new Tree(err, null, null));
		}
		nextToken();
		Tree state = parseStatement();
		if(!(next.getValue().contentEquals("endwhile"))){
			//Error
			Token err = new Token("ERROR", "FATAL ERROR. Expected token 'endwhile', token was " + next.getType() + "->" + next.getValue());
			return(new Tree(err, null, null));
		}
		Token op = new Token("KEYWORD", "WHILE-LOOP");
		return(new Tree(op, exp, state));
	}
	
	//Expression > Term > Factor > Piece > Element
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
				Token err = new Token("ERROR", "FATAL ERROR. Expected token ')', token was " + next.getValue() + " -> " + next.getType());
				return(new Tree(err, null, null));
			}
			return node;
		}
		else {
			if(!isElement()) {
				//ERROR
				Token err = new Token("ERROR", "FATAL ERROR. Expected token '(', token was  " + next.getValue() + " -> " + next.getType());
				return(new Tree(err, null, null));
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
