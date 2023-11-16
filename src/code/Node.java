package code;

public class Node {
	public State state;
	public Node parent;
	public String operator;
	public int depth;
	public int pathCost;
	
	public Node(State state , Node parent, String operator, int depth, int pathCost) {
		this.state=state;
		this.parent=parent;
		this.operator=operator;
		this.depth=depth;
		this.pathCost=pathCost;
	}
	
}