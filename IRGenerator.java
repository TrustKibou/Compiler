import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IRGenerator {
	static LinkedHashMap<String, String> globalSymbolTable;
	static LinkedList<String> code = new LinkedList<>();
	
    public static void generate(Node root){

        // HEADER LINES
        code.add(";=======");
        code.add(";IR CODE");
        code.add(";=======");
        
        // CONVERT TO 3AC CODE
        convertStatements(root, code, 0);
        
        // FOOTER LINE
        code.add(";RET\n\n");
    }
    

    // PARSE TIME
    public static void convertStatements(Node root, LinkedList<String> code, int temporary) {
    	
    	Node current = root;


        // CHECK FOR MAIN FUNCTION
        if(current.getLHS() != null && current.getLHS().equals("main")){
            code.add(";LABEL main");    
            code.add(";LINK");
        }
        
        
        
        // CHECK FOR VARIABLE DECLARATIONS (I know we don't need it for IR, but I'm doing it anyway)
        else if (current.getType() != null && (current.getType().equals("INT") || current.getType().equals("FLOAT")))
        		code.add(";VARDCL " + current.getLHS());
        else if (current.getType() != null && current.getType().equals("STRING"))
    		code.add(";STRDCL " + current.getLHS() + " " + current.getRHS());
    
        
        
        // CHECK FOR READ (NO STRINGS)
        else if (current.getType() != null && current.getType().equals("READ")) {
        		String[] variables = current.getLHS().split(",");
        		int i=0;
        		
        		for (String var: variables) {
        			if (globalSymbolTable.get("name " + var).contains("type INT"))
        				code.add(";READI " + var);
        			else if (globalSymbolTable.get("name " + var).contains("type FLOAT"))
        				code.add(";READF " + var);
        			i++;
        		}
        }
        
        
        
        // CHECK FOR WRITE
        else if (current.getType() != null && current.getType().equals("WRITE")) {
    		String[] variables = current.getRHS().split(",");
    		int i=0;
    		
    		for (String var: variables) {
    			if (globalSymbolTable.get("name " + var).contains("type INT"))
    				code.add(";WRITEI " + var);
    			else if (globalSymbolTable.get("name " + var).contains("type FLOAT"))
    				code.add(";WRITEF " + var);
    			else if (globalSymbolTable.get("name " + var).contains("type STRING"))
    				code.add(";WRITES " + var);
    			i++;
    		}
        }
        
        
        
        // CHECK FOR ASSIGNMENT STATEMENT (INT)
        else if (current.getType() != null && current.getType().contains("ASGN-I")) {
			code.add(";STOREI " + current.getRHS() + " $T" + temporary);
			code.add(";STOREI $T" + (temporary++) + " " + current.getLHS());
        }
        
    	// CHECK FOR ASSIGNMENT STATEMENT (FLOAT)
        else if (current.getType() != null && current.getType().contains("ASGN-F")) {
			code.add(";STOREF " + current.getRHS() + " $T" + temporary);
			code.add(";STOREF $T" + (temporary++) + " " + current.getLHS());
        }
        
        
        
        
        // CHECK FOR EXPRESSIONS
        else if (current.getType() != null && current.getType().contains("EXPR")) {
        	char symbol = ' ';
        	String curValue = current.getRHS();
        	String[] ops = new String[2];
        	boolean activeFloat = false;
        	
        	
        	// REMOVE SPACES AND PARENTHESIS
        	curValue = curValue.replace("(", "");
        	curValue = curValue.replace(")", "");
        	curValue = curValue.replace(" ", "");
        	
        	
        	// FIND OPERATOR
        	if (curValue.contains("+")) {
        		ops = curValue.split("\\+");
        		symbol = '+';
        	} else if (curValue.contains("-")) {
        		ops = curValue.split("\\-");
        		symbol = '-';
        	} else if (curValue.contains("*")) {
        		ops = curValue.split("\\*");
        		symbol = '*';
        	} else if (curValue.contains("/")) {
        		ops = curValue.split("\\/");
        		symbol = '/';
        	}

        	
        	// CHECK IF EITHER SIDE OF OPERATOR IS A VALUE
        	// IF IT IS, SAVE IT INTO A REGISTER AND REPLACE IT IN THE CURVALUE STRING
        	int op1val = (int)(ops[0].charAt(0));
    		int op2val = (int)(ops[1].charAt(0));

    		
        	if (op1val < 65) {
        		if (ops[0].contains(".")) {
        			activeFloat = true;
        			code.add(";STOREF " + ops[0] + " $T" + temporary);
        		}
        		else
        			code.add(";STOREI " + ops[0] + " $T" + temporary);
        		
        		ops[0] = "$T" + temporary++;
        	}
        	if (op2val < 65) {
        		if (ops[1].contains(".")) {
        			activeFloat = true;
        			code.add(";STOREF " + ops[1] + " $T" + temporary);
        		}
        		else
        			code.add(";STOREI " + ops[1] + " $T" + temporary);

        		ops[1] = "$T" + temporary++;
        	}
        	
        	
        	// NOW CHECK WHETHER EITHER OPERAND IS INT OR FLOAT
	        if (!activeFloat) {
	        	for (String var: ops) {
	    			if (globalSymbolTable.get("name " + var).contains("type INT"))
	    				activeFloat = false;
	    			else if (globalSymbolTable.get("name " + var).contains("type FLOAT"))
	    				activeFloat = true;
	    		}
	        }
	        
        	
	        // HANDLE INTEGER EXPRESSIONS
        	if (!activeFloat) {
        		if (symbol == '+') {
        			code.add(";ADDI " + ops[0] + " " + ops[1] + " $T" + temporary);
        		} else if (symbol == '-') {
        			code.add(";SUBI " + ops[0] + " " + ops[1] + " $T" + temporary);        			
        		} else if (symbol == '*') {
        			code.add(";MULTI " + ops[0] + " " + ops[1] + " $T" + temporary);        			
        		} else if (symbol == '/') {
        			code.add(";DIVI " + ops[0] + " " + ops[1] + " $T" + temporary);        			
        		}
        		
        		code.add(";STOREI $T" + (temporary++) + " " + current.getLHS());
        	}
        	// HANDLE FLOAT EXPRESSIONS
        	else {
        		if (symbol == '+') {
        			code.add(";ADDF " + ops[0] + " " + ops[1] + " $T" + temporary);
        		} else if (symbol == '-') {
        			code.add(";SUBF " + ops[0] + " " + ops[1] + " $T" + temporary);        			
        		} else if (symbol == '*') {
        			code.add(";MULTF " + ops[0] + " " + ops[1] + " $T" + temporary);        			
        		} else if (symbol == '/') {
        			code.add(";DIVF " + ops[0] + " " + ops[1] + " $T" + temporary);        			
        		}
        		
        		code.add(";STOREF $T" + (temporary++) + " " + current.getLHS());
        	}
        }
        
        
        // REPEAT FOR NEXT STATEMENT
        for (int i = 0; i < current.size(); i++)
        	convertStatements(current.getChild(i), code, temporary);
    }
}
