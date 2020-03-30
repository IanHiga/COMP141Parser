import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

/*
 * COMP 141- Programming Languages
 * Name: Ian Higa
 * Project Phase: 2.2
 */

public class MainApplication {
	public static final String DIRECTORY = "files/";
	
	public static void main(String[] args) {
		String in = args[0];
		String out = args[1];
		
		if(in.contentEquals("") || out.contentEquals("")) {
			System.out.println("An error has occured:\nNot enough arguments.\n");
		}
		else {
			File input = new File("../" + DIRECTORY + in);
			File output = new File("../" + DIRECTORY + out);
			readFile(input, output);
		}
	}
	
	private static void readFile(File in, File out) {
		FileWriter tokenOut;

		if(in.exists()){		
			//PROCEED WITH SCAN
			Scanner scan;
			String nextLine = "";
			String nextLineTokens = "Tokens:\n";
			try {
				scan = new Scanner(in);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			try {
				out.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				scan.close();
				return;
			}
			
			try {
				tokenOut = new FileWriter(out);
			} catch (IOException e) {
				e.printStackTrace();
				scan.close();
				return;
			}

			do {
				nextLineTokens += scanInputLine(scan.nextLine());
			} while(scan.hasNext());
			nextLine += parseLine(nextLineTokens);
			try {
				tokenOut.write(nextLine);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				tokenOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			scan.close();
		}
		else {
			System.out.println("An error has occured:\n");
			if(! in.exists()) {
				System.out.println("Input file " + in + " does not exist.\n");
			}
		}
	}
	
	private static String parseLine(String in) {
		String output = in;
		Scanner lineScanner = new Scanner(in);
		LinkedList<Token> tokens = new LinkedList<Token>();
		//Skip Label and newline
		lineScanner.nextLine();
		
		//Identify all tokens and place into list
		while(lineScanner.hasNext() == true) {
			String curToken = lineScanner.nextLine();
			String val = "", type = "";
			Scanner tokenScanner = new Scanner(curToken);
			if(tokenScanner.hasNext()) {				
				val = tokenScanner.next();
				tokenScanner.next();
				type = tokenScanner.next();
				Token nextToken = new Token(type, val);
				tokens.add(nextToken);
			}
			else {
				tokenScanner.close();
				break;
			}
			tokenScanner.close();
		}
		output += "\nAST:\n";
		Parser parse = new Parser();
		output += parse.parseList(tokens);
		return output;
	}
	
	private static String scanInputLine(String in) {
		in += " ";
		char[] tokenChars = in.toCharArray();
		String tokenType = "";
		String temp = "";
		String next = "";
		String tokens = "";
		
		for(int i = 0; i < in.length(); i++) {
			next = "";
			next += tokenChars[i];
			tokenType = "";
			
			if(Pattern.matches("[0-9]+", temp)) {
				if(!Pattern.matches("[0-9]", next)) {
					tokenType = "NUMBER";
				}
			}
			else if(Pattern.matches("[+|\\-|*|/|(|)|;]", temp) || Pattern.matches("[:]|[:][=]", temp)) {
				if(!Pattern.matches("[:]", temp)) {		
					tokenType = "SYMBOL";
				}
			}
			else if(Pattern.matches("[[a-z]|[A-Z]][[a-z]|[A-Z]|[0-9]]*", temp)) {
				if(temp.contentEquals("if") || temp.contentEquals("then") || temp.contentEquals("elseif") || temp.contentEquals("while")
						|| temp.contentEquals("do") || temp.contentEquals("endwhile") || temp.contentEquals("skip")) {
					tokenType = "KEYWORD";
				}
				else if(!(Pattern.matches("[a-z]|[A-Z]|[0-9]", next))) {					
					tokenType = "IDENTIFIER";
				}
			}
			else if(!temp.contentEquals("") && !next.contentEquals("\t")){
				tokenType = "ERROR";
				tokens += "ERROR READING '" + temp + "'!\n\n";
				return(tokens);
			}
			
			if(!tokenType.contentEquals("")) {	
				tokens += temp + " : " + tokenType + "\n";
				temp = "";
			}
			if(!next.contentEquals(" ") && !next.contentEquals("\t")) {		
				temp += next;
			}
		}
		return(tokens);
	}
}
