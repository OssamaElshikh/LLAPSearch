package code;
public class LLAPSearch extends GenericSearch{	
	

	
	public LLAPSearch() {
	}	
	
	public static String solve(String initialState,String strategy, boolean visualize) {
						
		LLAPSearch problem = new LLAPSearch();
		SearchProblem searchProblem = new SearchProblem(initialState);		
		
		String plan="";
		int monetaryCost=0;
		int nodesExpanded=0;
		String finalString = "";
		
		switch(strategy) {
			case("BF"):
				Node bf = problem.bfSearch(searchProblem);
				if(bf==null) {
					return "NOSOLUTION";
				}
				
				Node tmp2 = new Node(bf.state,bf.parent,bf.operator,bf.depth,bf.pathCost);				
				while(tmp2.parent!=null) {
					plan=tmp2.operator+","+plan;
					tmp2 = tmp2.parent;
				}				
				plan = plan.substring(0,plan.length()-1);
				
				nodesExpanded = searchProblem.nodesExpanded;
				monetaryCost = bf.state.money_spent;
	
				finalString=plan+";"+monetaryCost+";"+nodesExpanded;
				
				break;
				
			case("DF"):
				Node df = problem.dfSearch(searchProblem);
				if(df==null) {
					return "NOSOLUTION";
				}
				
				Node tmp = new Node(df.state,df.parent,df.operator,df.depth,df.pathCost);
				
				while(tmp.parent!=null) {
					plan=tmp.operator+","+plan;
					tmp = tmp.parent;
				}
				
				plan = plan.substring(0,plan.length()-1);
				
				nodesExpanded = searchProblem.nodesExpanded;
				monetaryCost =  df.state.money_spent;
				
				finalString=plan+";"+monetaryCost+";"+nodesExpanded;
				break;
			case ("ID"):
				Node id = problem.idsSearch(searchProblem);
				if (id == null) {
					return "NOSOLUTION";
				}

				Node tp3 = new Node(id.state, id.parent, id.operator, id.depth, id.pathCost);

				while (tp3.parent != null) {
					plan = tp3.operator + "," + plan;
					tp3 = tp3.parent;
				}

				plan = plan.substring(0, plan.length()-1);

				nodesExpanded = searchProblem.nodesExpanded;
				monetaryCost = (int) id.state.money_spent;

				finalString = plan + ";" + monetaryCost + ";" + nodesExpanded;
				break;
			case("UC"):			
				Node uc = problem.ucSearch(searchProblem);
		    	if(uc==null) {
	    			return "NOSOLUTION";
	    		}
			
		    	Node tmp4 = new Node(uc.state,uc.parent,uc.operator,uc.depth,uc.pathCost);
			
	    		while(tmp4.parent!=null) {
		    		plan=tmp4.operator+","+plan;
		    		tmp4 = tmp4.parent;
		    	}
			
		    	plan = plan.substring(0,plan.length()-1);
			
		    	nodesExpanded = searchProblem.nodesExpanded;
		    	monetaryCost = (int) uc.state.money_spent;

		    	finalString=plan+";"+monetaryCost+";"+nodesExpanded;
				
				break;
		}
		return finalString;
	}

	
	public static void main(String[] args) {
		
		String initialState3 = "0;" +
                "19,35,40;" +
                "27,84,200;" +
                "15,2;37,1;19,2;" +
                "569,11,20,3,50;"+
                "115,5,8,21,38;" ;

		
		String strategy = "ID";
		boolean visualize = false;	
		
		System.out.println(solve(initialState3, strategy, visualize));
	}
}