import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class TinyGenerator {

	
	public static void generate(LinkedList<String> IR) {

		LinkedList<String> code = new LinkedList<>();
		
		// INITIAL LINE
		code.add(";=========");
		code.add(";tiny code");
		code.add(";=========");
		
		
		// ITERATE THROUGH EACH LINE OF 3AC CODE AND CONVERT TO TINY ============================================
		for (int i=0; i<IR.size(); i++) {

			String line = IR.get(i);
			
			// LINES TO SKIP
			if (line.equals(";IR Code") || line.equals(";LABEL main") || line.equals(";LINK") || line.equals(";======="))
				continue;
			
			// PRELOOP OPERATIONS
			// split line into parts
			String[] parts = line.split(" ");
			
			// loop through and replace register with r
			for (int j=0; j<parts.length; j++) {
				parts[j] = parts[j].replace("$T", "r");
			}
			
			// GLOBAL VARIABLE DECLARATIONS ---------------------------------------------------------------------
			if (parts[0].equals(";VARDCL"))
				code.add("var " + parts[1]);
			else if (parts[0].equals(";STRDCL")) {
				String tempLine = "str " + parts[1] + " ";
				for (int j=2; j<parts.length; j++) {
					tempLine += parts[j] + " ";
				}
				code.add(tempLine);
			}
			
			
			
			// ASSIGNMENT STATEMENTS ----------------------------------------------------------------------------
			else if (parts[0].equals(";STOREI") || parts[0].equals(";STOREF")) {
				code.add("move " + parts[1] + " " + parts[2]);
			}
			
			
			
			// INPUT AND OUTPUT ---------------------------------------------------------------------------------
			// READ
			else if (parts[0].equals(";READI"))
				code.add("sys readi " + parts[1]);
			else if (parts[0].equals(";READF"))
				code.add("sys readr " + parts[1]);
			
			// WRITE
			else if (parts[0].equals(";WRITEI"))
				code.add("sys writei " + parts[1]);
			else if (parts[0].equals(";WRITEF"))
				code.add("sys writer " + parts[1]);
			else if (parts[0].equals(";WRITES"))
				code.add("sys writes " + parts[1]);
			
			
			
			// EXPRESSIONS --------------------------------------------------------------------------------------
			// ADD
			else if (parts[0].equals(";ADDI")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("addi " + parts[2] + " " + parts[3]);
			}
			else if (parts[0].equals(";ADDF")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("addr " + parts[2] + " " + parts[3]);
			}
				
			// SUB
			else if (parts[0].equals(";SUBI")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("subi " + parts[2] + " " + parts[3]);
			}
			else if (parts[0].equals(";SUBF")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("subr " + parts[2] + " " + parts[3]);
			}
			
			// MULT
			else if (parts[0].equals(";MULTI")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("muli " + parts[2] + " " + parts[3]);
			}
			else if (parts[0].equals(";MULTF")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("mulr " + parts[2] + " " + parts[3]);
			}
			
				
			// DIV
			else if (parts[0].equals(";DIVI")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("divi " + parts[2] + " " + parts[3]);
			}
			else if (parts[0].equals(";DIVF")) {
				code.add("move " + parts[1] + " " + parts[3]);
				code.add("divr " + parts[2] + " " + parts[3]);
			}
			
		}
		
		// FOOTER LINE
		code.add("sys halt");

		File file = new File("new_output.out");
		
		try (
				PrintWriter writer = new PrintWriter(file);
			) {
				for (String lineOfIR: IR) {
//					writer.println(lineOfIR);
					System.out.println(lineOfIR);
				}
				for (String lineOfTiny: code) {
//					writer.println(lineOfTiny);
					System.out.println(lineOfTiny);
				}
			}	
		catch (FileNotFoundException ex) {}
		
	}
	
	
}
