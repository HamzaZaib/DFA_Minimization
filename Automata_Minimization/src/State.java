import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class State {
	String name;
	HashMap<String,State> outflows;
	StateType Type;
	
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
	public boolean equals(State state, String initial, String[] alphabets, int max) {
		if(initial.length()==max)
			return false;
		if((state.Type==StateType.Both || state.Type==StateType.Final)^(this.Type==StateType.Both || this.Type==StateType.Final))
			return false;
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
