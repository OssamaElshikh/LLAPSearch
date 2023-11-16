package code;
import java.util.*;

public class SearchProblem {
		
	int initialProsperity;
	
	int initialFood;
	int initialMaterials;
	int initialEnergy;
	
	int unitPriceFood;
	int unitPriceMaterials;
	int unitPriceEnergy;
	
	int amountRequestFood;
	int delayRequestFood;
	
	int amountRequestMaterials;
	int delayRequestMaterials;
	
	int amountRequestEnergy;
	int delayRequestEnergy;
	
	int priceBUILD1;
	int foodUseBUILD1;
	int materialsUseBUILD1;
	int energyUseBUILD1;
	int prosperityBUILD1;
	
	int priceBUILD2;
	int foodUseBUILD2; 
	int materialsUseBUILD2;
	int energyUseBUILD2;
	int prosperityBUILD2;
	
	State problemInitialState;	
	int foodMaterialsEnergyWaitCost;
	int build1Cost;
	int build2Cost;
	int maxDelay;
	
	String[] notDelayedOperators = {"RequestFood","RequestMaterials","RequestEnergy","BUILD1","BUILD2"};
	String[] delayedOperators= {"BUILD1","BUILD2","WAIT"};
	int money_spent=0;
	int budget = 100000;
	int maxProsperity=100;	
	int maxResource=50;
	
	int nodesExpanded=0;
	
	HashSet<Long> set = new HashSet<Long>();

	public SearchProblem(String allState) {
		assignValues(allState);
	}
	
	public void assignValues(String initialState) {
		String[] segments = initialState.split(";");
		
		this.initialProsperity = Integer.parseInt(segments[0]);		
		
		this.initialFood = Integer.parseInt(segments[1].split(",")[0]);
		this.initialMaterials = Integer.parseInt(segments[1].split(",")[1]);
		this.initialEnergy = Integer.parseInt(segments[1].split(",")[2]);
		
		this.unitPriceFood = Integer.parseInt(segments[2].split(",")[0]);
		this.unitPriceMaterials = Integer.parseInt(segments[2].split(",")[1]);
		this.unitPriceEnergy = Integer.parseInt(segments[2].split(",")[2]);
			
		this.amountRequestFood = Integer.parseInt(segments[3].split(",")[0]);
		this.delayRequestFood = Integer.parseInt(segments[3].split(",")[1]);
		
		this.amountRequestMaterials = Integer.parseInt(segments[4].split(",")[0]);
		this.delayRequestMaterials = Integer.parseInt(segments[4].split(",")[1]);
		
		this.amountRequestEnergy = Integer.parseInt(segments[5].split(",")[0]);
		this.delayRequestEnergy = Integer.parseInt(segments[5].split(",")[1]);
		
		this.priceBUILD1 = Integer.parseInt(segments[6].split(",")[0]);
		this.foodUseBUILD1 = Integer.parseInt(segments[6].split(",")[1]);
		this.materialsUseBUILD1 = Integer.parseInt(segments[6].split(",")[2]);
		this.energyUseBUILD1 = Integer.parseInt(segments[6].split(",")[3]);
		this.prosperityBUILD1  = Integer.parseInt(segments[6].split(",")[4]);
		
		this.priceBUILD2 = Integer.parseInt(segments[7].split(",")[0]);
		this.foodUseBUILD2 = Integer.parseInt(segments[7].split(",")[1]);
		this.materialsUseBUILD2 = Integer.parseInt(segments[7].split(",")[2]);
		this.energyUseBUILD2 = Integer.parseInt(segments[7].split(",")[3]);
		this.prosperityBUILD2 = Integer.parseInt(segments[7].split(",")[4]);
		
		this.problemInitialState = new State(initialProsperity,initialFood,initialMaterials,initialEnergy,money_spent,0);
		this.foodMaterialsEnergyWaitCost = unitPriceFood + unitPriceMaterials + unitPriceEnergy;
		this.build1Cost = priceBUILD1 + foodUseBUILD1*unitPriceFood + materialsUseBUILD1*unitPriceMaterials + energyUseBUILD1*unitPriceEnergy;
		this.build2Cost = priceBUILD2 + foodUseBUILD2*unitPriceFood + materialsUseBUILD2*unitPriceMaterials + energyUseBUILD2*unitPriceEnergy;
		this.maxDelay= Math.max(Math.max(delayRequestFood,delayRequestMaterials),delayRequestEnergy);		
	}
	
	public String[] getNodeOperators(Node node) {
		int counter =0;	
		Node tmpNode = new Node (node.state,node.parent,node.operator,node.depth,node.pathCost);
		String tmpNodeOperator=tmpNode.operator;
		
		if(tmpNodeOperator==null) 
			return notDelayedOperators;
				
		for(int i=0;i<maxDelay;i++) {
			if(tmpNodeOperator=="RequestFood" & counter<delayRequestFood) 
				return delayedOperators;
			
			if(tmpNodeOperator=="RequestMaterials" & counter<delayRequestMaterials) 
				return delayedOperators;
			
			if(tmpNodeOperator=="RequestEnergy" & counter<delayRequestEnergy ) 
				return delayedOperators;			
			tmpNode = tmpNode.parent;
			if(tmpNode.operator==null) 
				return notDelayedOperators;
			tmpNodeOperator=tmpNode.operator;
			counter++;
		}
		return notDelayedOperators;
	}
	
	public int checkNodeSupply(Node node,String operatorChecking) {			
		if(node.parent==null)
			return 0;		
		Node tmpNode = new Node (node.state,node.parent,node.operator,node.depth,node.pathCost);			

		switch(operatorChecking) {
			case("RequestFood"):		
				for(int i=0;i<delayRequestFood-1;i++) 
					tmpNode = tmpNode.parent;
				if(tmpNode.operator=="RequestFood") 
					return amountRequestFood;	
				break;
				
			case("RequestMaterials"):
				for(int i=0;i<delayRequestMaterials-1;i++)		
					tmpNode = tmpNode.parent;		
				if(tmpNode.operator=="RequestMaterials") 
					return amountRequestMaterials;	
				break;
				
			case("RequestEnergy"):
				for(int i=0;i<delayRequestEnergy-1;i++) 			
					tmpNode = tmpNode.parent;
				if(tmpNode.operator=="RequestEnergy") 
					return amountRequestEnergy;		
				break;
		}
		return 0;
	}
	
	public State adjustUnitSupply(Node parent, State newState){
		int requestedFood = checkNodeSupply(parent,"RequestFood");
		int requestedMaterials = checkNodeSupply(parent,"RequestMaterials");
		int requestedEnergy = checkNodeSupply(parent,"RequestEnergy");
		
		newState.food=Math.min(newState.food+requestedFood,50);
		newState.materials=Math.min(newState.materials+requestedMaterials,50);
		newState.energy=Math.min(newState.energy+requestedEnergy,50);
		return newState;
	}
	
	public Queue<Node> getChildren(Node parent){		
			
		int currentStateProsperity = parent.state.prosperity; 
		int currentStateFood = parent.state.food; 
		int currentStateMaterials = parent.state.materials; 
		int currentStateEnergy =   parent.state.energy;
		int currentState_money_spent =  parent.state.money_spent;
		
		String [] parentOperators = getNodeOperators(parent);	
		State newState= new State(0,0,0,0,0,0);
		Queue<Node> expandedNodes = new LinkedList<>();
		
		for(int i=0;i<parentOperators.length;i++) {
			switch(parentOperators[i]) {
				case("RequestFood"):
					newState= new State(currentStateProsperity,currentStateFood-1,currentStateMaterials-1,currentStateEnergy-1,currentState_money_spent+foodMaterialsEnergyWaitCost,0);
					break;
				case("RequestMaterials"):
					newState= new State(currentStateProsperity,currentStateFood-1,currentStateMaterials-1,currentStateEnergy-1,currentState_money_spent+foodMaterialsEnergyWaitCost,0);
					break;
				case("RequestEnergy"):
					newState= new State(currentStateProsperity,currentStateFood-1,currentStateMaterials-1,currentStateEnergy-1,currentState_money_spent+foodMaterialsEnergyWaitCost,0);
					break;
				case("BUILD1"):
					newState= new State(Math.min(currentStateProsperity+prosperityBUILD1,maxProsperity),currentStateFood-foodUseBUILD1,currentStateMaterials-materialsUseBUILD1,currentStateEnergy-energyUseBUILD1,currentState_money_spent+build1Cost,0);
					break;
				case("BUILD2"):
					newState= new State(Math.min(currentStateProsperity+prosperityBUILD2,maxProsperity),currentStateFood-foodUseBUILD2,currentStateMaterials-materialsUseBUILD2,currentStateEnergy-energyUseBUILD2,currentState_money_spent+build2Cost,0);
					break;
				case("WAIT"):
					newState= new State(currentStateProsperity,currentStateFood-1,currentStateMaterials-1,currentStateEnergy-1,currentState_money_spent+foodMaterialsEnergyWaitCost,0);
					break;
			}		
			newState = adjustUnitSupply(parent,newState);
			
			if(failTest(newState) == false) {
				
				int number=getNumber(parent,parentOperators[i]);
				newState.number=number;
				String word = newState.prosperity+""+newState.food+""+newState.materials+""+newState.energy+""+newState.money_spent+""+newState.number;
				
				long word1 = Long.parseLong(word);
				if(set.contains(word1))
					continue;
				
				Node newNode = new Node(newState,parent,parentOperators[i],parent.depth+1,1);
				expandedNodes.add(newNode);		
	
				set.add(word1);
			}				
		}
		return expandedNodes;
	}
	
	public boolean goalTest(State state) {
		int currentProsperity = state.prosperity;
		if(currentProsperity==maxProsperity) 
			return true;
		return false;
	}
	
	public boolean failTest(State state) {
		int currentStateFood = state.food;
		int currentStateMaterials = state.materials;
		int currentStateEnergy = state.energy;
		int currentState_money_spent = state.money_spent;	
		if(currentStateFood<0 | currentStateMaterials<0 | currentStateEnergy<0  ) 
			return true;
		if(currentState_money_spent>budget) 
			return true;
		return false;
	}
	
	public int getNumber(Node node,String operator) {
		if(node.operator==null)
			return 0;
		if(operator.equals("RequestFood"))
			return 1;
		else if(operator.equals("RequestMaterials"))
			return 3;
		else if(operator.equals("RequestEnergy"))
			return 5;
		else {
			if(node.operator.equals("RequestFood"))
				return 2;
			if(node.operator.equals("RequestMaterials"))
				return 4;
			if(node.operator.equals("RequestEnergy"))
				return 6;	
		}		
		return 0;
	}
}