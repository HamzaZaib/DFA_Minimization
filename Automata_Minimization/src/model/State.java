package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class State {
	private String name;
	private HashMap<String,State> outflows;		//for storing transitions 
	private StateType Type;
	
	//setters and getters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, State> getOutflows() {
		return outflows;
	}
	public void setOutflows(HashMap<String, State> outflows) {
		this.outflows = outflows;
	}
	public StateType getType() {
		return Type;
	}
	public void setType(StateType type) {
		Type = type;
	}
	State(){
		this.name=null;
		this.outflows=new HashMap<String,State>();
		this.Type=StateType.Neither;
	}
	State(String name){
		this.name=name;
		this.outflows=new HashMap<String,State>();
		this.Type=StateType.Neither;
	}
	State(String name, HashMap<String,State> outflows){
		this.name=name;
		this.outflows=outflows;
		this.Type=StateType.Neither;
	}
	State(String name, HashMap<String,State> outflows, StateType type){
		this.name=name;
		this.outflows=outflows;
		this.Type=type;
	}
	public State(State state) {
		this.name=state.name;
		this.outflows=new HashMap<String,State>(state.outflows);
		this.Type=state.Type;
	}
	
	//checks whether the 2 states are equal or not
	public boolean equals(State state, String initial, String[] alphabets, int max) {
		if(initial.length()==max)
			return false;
		//checks type of state like final or initial
		if((state.Type==StateType.Both || state.Type==StateType.Final)^(this.Type==StateType.Both || this.Type==StateType.Final))
			return false;
		//iterates each alphabet for 2 states and compares their results to determine if they are the same or not
		for(int i=0;i<alphabets.length;i++){
			State resultState=outflows.get(alphabets[i]),resultState1=state.outflows.get(alphabets[i]);
			if(resultState==null^resultState1==null)
				return false;
			else if(resultState!=null&&resultState1!=null){
				if(!resultState.equals(resultState1)){
					if(!resultState.equals(resultState1,initial+alphabets[i],alphabets,max)){
						return false;
					}
				}
			}
		}
		return true;
	}
	@Override
	public boolean equals(Object obj) {
		if(this.name.equals(((State)obj).name))
			return true;
		return false;
	}
	
}
