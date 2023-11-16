package code;
import java.util.*;

public class GenericSearch {
	
	public GenericSearch() {
		
	}
	
	public Node generalSearch(SearchProblem problem,String qingFunc) {
		
		Node root = new Node(problem.problemInitialState,null,null,0,0);
		Deque<Node> nodes = new LinkedList<>();
//		PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingInt(node -> node.pathCost));
		nodes.add(root);
	
		while(true) {		
			if(nodes.isEmpty())
				return null;		
			Node parent = nodes.remove();		
			problem.nodesExpanded=problem.nodesExpanded+1;
			
			if(problem.goalTest(parent.state))
				return parent;			
			
			Queue<Node> children = problem.getChildren(parent);	
			
			if(!children.isEmpty()) { 
				switch(qingFunc) {
					case("BF"):
						for(int i=0;i<children.size();i++) {
							Node child = children.element();	
							nodes.addLast(child);
							children.remove();		
							children.add(child);
						}
						break;	
					case("DF"):
						Deque<Node> tmpNodes = new LinkedList<>();
						for(int i=0;i<children.size();i++) {
							Node child = children.element();	
							tmpNodes.addFirst(child);
							children.remove();		
							children.add(child);
						}
						for(int i=0;i<tmpNodes.size();i++) {
							Node child = tmpNodes.element();	
							nodes.addFirst(child);
							tmpNodes.remove();		
							tmpNodes.add(child);
						}
						break;	
					case("UC"):
						for(int i=0;i<children.size();i++) {
					    	Node child = children.element();	
					    	nodes.add(child);
					    	children.remove();	
					    	children.add(child);
					    }	
						break;
					 case ("ID"):
						 int maxDepth = 0;
		                 while (true) {
		                	 Deque<Node> nodesCopy = new LinkedList<>(nodes);
		                     boolean goalFound = false;
		                     while (!nodesCopy.isEmpty()) {
		                    	 Node parent2 = nodesCopy.remove();
		                         problem.nodesExpanded=problem.nodesExpanded+1;
		                         if (problem.goalTest(parent2.state)) {
		                        	 goalFound = true;
		                        	 return parent2;
		                         }             
		                         if(parent2.depth < maxDepth) {
		                        	 Queue<Node> children2 = problem.getChildren(parent2);
		                        	 nodesCopy.addAll(children2);
		                         }
		                     }
			                 if (goalFound) {
			                	 break;
			                 }
			                 maxDepth++;
			                 nodes.clear();
			                 nodes.addAll(nodesCopy);
			                 nodes.add(root);
			                   
			                 problem.set = new HashSet<Long>();
			                 problem.nodesExpanded=0;
		                 }
		                 break;
				}
			}
		}
	}
	
	public Node bfSearch(SearchProblem problem) {
		return generalSearch(problem,"BF");
	}
	
	public Node dfSearch(SearchProblem problem) {
		return generalSearch(problem,"DF");
	}
	
	public Node idsSearch(SearchProblem problem) {
		return generalSearch(problem, "ID");
	}

	public Node ucSearch(SearchProblem problem) {
		return generalSearch(problem,"UC");

	}
}