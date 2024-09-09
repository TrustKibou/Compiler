import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.FileInputStream;

public class Driver {

   public static void main(String[] args) throws Exception {
	
	// GRAB INPUT STREAM FROM PROVIDED FILE
	CharStream inStrm = CharStreams.fromStream(System.in);

	// CREATE LEXER OBJECT (Little) FROM STREAM
	LittleLexer lexer = new LittleLexer(inStrm);

	// Create a CommonTokenStream from the lexer
	CommonTokenStream tokens = new CommonTokenStream(lexer);

	// Create a parser object (LittleParser) from the token stream
	LittleParser parser = new LittleParser(tokens);

	// Redirect the error messages to a custom error listener
	parser.removeErrorListeners(); // Remove the default error listeners
	
	// Specify the starting rule (program) and parse the input
	ParseTree tree = parser.program();
	
	// CREATE A GENERIC PARSE TREE WALKER THAT CAN TRIGGER CALLBACKS
	ParseTreeWalker walker = new ParseTreeWalker();
	SimpleTableBuilder stb = new SimpleTableBuilder();
	
	// WALK THE TREE CREATED DURING THE PARSE, TRIGGER CALLBACKS
	walker.walk(stb, tree);
	//stb.prettyPrint();
	
}

}
