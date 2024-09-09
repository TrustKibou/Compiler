import java.util.ArrayList;
import java.util.List;

public class Node {
	private String type; // TYPE OF STATEMENT
	private String LHS; // LEFT HAND SIDE (+ READ) LHS name
	private String RHS; // RIGHT HAND SIDE (+ WRITE) RHS value
	private List<Node> children;
	
	Node() {
		children = new ArrayList<>();
	}
	
	Node(String type, String LHS, String RHS) {
		this();
		this.type=type;
		this.LHS=LHS;
		this.RHS=RHS;
	}
	
	public void addChild(Node child) {
		children.add(child);
	}
	public Node getChild(int i) {
		return children.get(i);
	}
	public int size() {
		return children.size();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLHS() {
		return LHS;
	}
	public void setLHS(String LHS) {
		this.LHS = LHS;
	}
	public String getRHS() {
		return RHS;
	}
	public void setRHS(String RHS) {
		this.RHS = RHS;
	}
}
