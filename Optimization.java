import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

// TEST 1 - 49 ... (1 2) = 1 4 5
// TEST 2 - 83 ... ()    = -10 920 920
// TEST 3 - 59 ...()     = 1 3.14159 2 1.5708 "" 3.14159

public class Optimization {
	static LinkedHashMap<String, String> registers = new LinkedHashMap<>();
	
//	public static void main(String[] args) {
//		LinkedList<String> IR = new LinkedList<>();
//		
//		IR.add(";STOREI 1 $T1\r\n");
//		IR.add(";STOREI $T1 a\r\n");
//		IR.add(";STOREI 2 $T2\r\n");
//		IR.add(";STOREI $T2 b");
//		
//		optimize(IR);
//	}
	public static void optimize(LinkedList<String> IR) {
		
		LinkedHashMap<String, String> registers = new LinkedHashMap<>();
		boolean skipIteration = false;
		int constantNum = 0;
		
//		System.out.println("\n\n\n----------OPTIMIZATION----------");
		
		for (int i=0; i<IR.size(); i++) {
			
			skipIteration = false;
			
			// GET PARTS OF IR CODE STATEMENT
			String line = IR.get(i);
			String[] parts = line.split(" ");
			
			
			// CHECK FOR STORE ASSIGNMENTS
			if ((parts[0].equals(";STOREI") || parts[0].equals(";STOREF")) && parts[2].contains("$T") && i != IR.size()-1) {
				// HANDLE CONSTANTS USED IN EXPRESSIONS
				if (IR.get(i+1).startsWith(";STOREF") || IR.get(i+1).startsWith(";STOREI")) {
					registers.put(IR.get(i+1).split(" ")[2], parts[2]);
					IR.remove(i+1);
				}
				else {
					registers.put(constantNum++ + "C", parts[2]);
				}
			}
			
			
			
			// CHECK FOR READ ASSIGNMENTS
			// IS THIS NECESSARY? 
//			else if ((parts[0].equals(";READI") || parts[0].equals(";READF")) && !parts[2].contains("$T") && i != IR.size()-1) {
//				registers.put(parts[1], )
//				
//			}
			// HANDLE EXPRESSIONS
			else if (parts[0].equals(";ADDI") || parts[0].equals(";ADDF") ||
					 parts[0].equals(";SUBI") || parts[0].equals(";SUBF") ||
					 parts[0].equals(";MULTI") || parts[0].equals(";MULTF") ||
				     parts[0].equals(";DIVI") || parts[0].equals(";DIVF")) {
				if (!registers.containsKey(IR.get(i+1).split(" ")[2])) {
					registers.put(IR.get(i+1).split(" ")[2], parts[3]);
				}
				else{
					line = line.replace(parts[3], IR.get(i+1).split(" ")[2]);
//					parts[3] = registers.get(IR.get(i+1).split(" ")[2]);
//					line = String.join(" ", parts);
					IR.set(i, line);
				}
				
				// HANDLING EXPRESSIONS THAT UTILIZE THE ASSIGNED VAR IN THE EXPRESSION
				if (parts[2].equals(IR.get(i+1).split(" ")[2]) && !parts[2].equals(parts[1])) {
					for (int j=0; j<100; j++) {
						if (!registers.containsValue("$T" + j)) {
							parts[3] = "$T" + j;
							line = String.join(" ", parts);
//							line = line.replace(parts[3], "$T" + j);
							
							IR.set(i, line);
							
							IR.set(i+1, ";STOREI $T" + j + " " + registers.get(parts[2]));
							skipIteration = true;
							break;
						}
					}
				}
				else {
					IR.remove(i+1);
				}
			}


			
				
			// FINAL CHECK - REPLACE INSTANCES OF MEMORY IDS WITH REGISTERS
			
			// LOOP THROUGH EACH REGISTER IN MAP AND REPLACE IN IR
			for (Map.Entry<String, String> entry : registers.entrySet()) { 
				
				parts = IR.get(i).split(" ");
				
				for (int k=0; k<parts.length; k++) {
					if (parts[k].equals(entry.getKey())) {
						parts[k] = entry.getValue();
					}
				}
				
				line = String.join(" ", parts);
				IR.set(i, line); 
				
//				if (line.contains(" " + entry.getKey() + "\n")) {
//					line = line.replace(entry.getKey(), entry.getValue());
//					IR.set(i, line);
//				}
					
			}
			
			if (skipIteration) i++;
		}
//		
//		System.out.println("REGISTERS LIST");
//		for (var entry : registers.entrySet())
//			System.out.println(entry.getKey() + " - " + entry.getValue());
//
//		for (String lineOfIR: IR)
//			System.out.println(lineOfIR);
//		
	}
}
