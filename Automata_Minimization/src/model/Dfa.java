package model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Dfa {
	private HashMap<String,State> states;
	private int noOfStates;
	private State starting;
	private HashMap<String,State> ending;
	private String[] alphabets;
	
	public Dfa() {
		this.states = new HashMap<String,State>();
		this.starting = new State();
		this.ending = new HashMap<String,State>();
		this.alphabets=null;
		this.noOfStates=0;
	}

	public Dfa(HashMap<String,State> states, State starting, HashMap<String,State> ending, String[] alphabets,int noOfStates) {
		this.states = new HashMap<String,State>(states);
		this.starting = new State(starting);
		this.ending = new HashMap<String,State>(ending);
		this.alphabets=alphabets.clone();
		this.noOfStates=noOfStates;
	}
	
	public void init(){
		try {
			//Reading the DFA from file
			BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
			this.alphabets=reader.readLine().split(",");	//reading alphabets from the first line
			String tokens[]=reader.readLine().split(",");	//reading the states from the second line
			State temp;
			for(int i=0;i<tokens.length;i++){
				states.put(tokens[i], new State(tokens[i]));	//saving states in the hashmap	
			}
			noOfStates=tokens.length;						//calculates total number of states
			//saves outflows for each state
			for(int i=0;i<noOfStates;i++){
				tokens=reader.readLine().split(" ");
				temp=states.get(tokens[0]);
				for(int j=0;j<alphabets.length;j++){
					if(tokens[j+1].equals(".")){
						temp.getOutflows().put(alphabets[j], null);
					}
					else{
						temp.getOutflows().put(alphabets[j],states.get(tokens[j+1]));
					}
				}
				
				//type of state such as final, initial or intermediary
				switch(tokens[tokens.length-1].charAt(0)){
					case 'b':
						temp.setType(StateType.Both);
						ending.put(tokens[0], temp);
						starting=temp;
						break;
					case 'f':
						temp.setType(StateType.Final);
						ending.put(tokens[0], temp);
						break;
					case 's':
						temp.setType(StateType.Start);
						starting=temp;
						break;
					default:
						temp.setType(StateType.Neither);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		// OPTIONAL: Taking input from user on command line to create a DFA
		
		/*this.states = new HashMap<String,State>();
		this.starting = new State();
		this.ending = new HashMap<String,State>();
		this.alphabets=null;
		
		ArrayList<String> uninitialized=new ArrayList<String>();
		
		Scanner in= new Scanner(System.in);
		String input="";
		
		System.out.println("Enter all the alphabets separated with a comma");
		input=in.next();
		this.alphabets=input.split(",");
		
		System.out.println("Enter the name of starting state");
		this.starting.name=in.next();
		this.starting.Type=StateType.Start;
		this.states.put(this.starting.name,this.starting);
		uninitialized.add(this.starting.name);
		
		System.out.println("Enter the names of resulting states.If there is no state then just press enter\n");
		
		while(uninitialized.size()!=0){
			String name=uninitialized.get(0);
			System.out.println("/***  Initializing state "+name+" *** /\n");
			State state=this.states.get(name);
		
			for(int i=0; i<alphabets.length;i++){
				System.out.println("Resulting state for input alphabet '"+alphabets[i]+"'");
				input=null;
				input=in.next();
				if(input.equals(".")){
					state.outflows.put(alphabets[i], null);
				}
				else if(this.states.containsKey(input)){
						state.outflows.put(alphabets[i],this.states.get(input));
				}
				else{
					State tempState=new State(input);
					tempState.Type=StateType.Neither;
					this.states.put(input,tempState);
					state.outflows.put(alphabets[i],this.states.get(input));
					uninitialized.add(input);
				}
			}
			System.out.println(name+" is a final state Enter\n1 for FINAL\n2 for NOT FINAL");
			if(in.nextInt()==1){
				this.ending.put(state.name, state);
				if(state.Type==StateType.Start){
					state.Type=StateType.Both;
				}
				else{
					state.Type=StateType.Final;
				}
			}
		
			uninitialized.remove(0);
		}*/
	}
	
	//Checks if String is part of the language or not
	public boolean checkString(String input){
		State current=starting;
		for(int i=0;i<input.length();i++){
			 if(isAlphabet(input.charAt(i))){
				 current=current.getOutflows().get(input.charAt(i));
				 if(current==null)
					 return false;
			 }
		}
		if(current.getType()==StateType.Both|| current.getType()==StateType.Final)
			return true;
		else
			return false;
	}
	
	//Checks if alphabet is part of the DFA
	public boolean isAlphabet(char alphabet){
		for(int i=0;i<alphabets.length;i++){
			if(alphabets[i].equals(alphabet+""))
				return true;
		}
		return false;
	}

	//Returns language of the DFA where length of accepted string is less than limit which is specified by the user
	public ArrayList<String> getStrings(int limit){
		if(this.ending.size()==0)
			return new ArrayList<String>();
		else
		return this.getStrings(this.starting, limit, 0, "");
	}
	
	//Actual implementation of the above function, this is called recursively to determine the length of accepted string
	private ArrayList<String> getStrings(State state,int limit, int current, String start ){
		ArrayList<String> accepted=new ArrayList<String>();
		if(state.getType()==StateType.Both|| state.getType()==StateType.Final)
			accepted.add(start);
		if(current>limit){
			return null;
		}
		for(int i=0;i<alphabets.length;i++){
			if(state.getOutflows().get(alphabets[i])!=null){
				ArrayList<String> temp;
				temp=this.getStrings(state.getOutflows().get(alphabets[i]),limit, current+1,start+alphabets[i]);
				if(temp!=null)
					accepted.addAll(temp);
			}
		}
		return accepted;
	}
	
	//Checks whether the DFA accepts epsilon or not
	public boolean acceptsEbsilon(){
		if(this.starting.getType()==StateType.Both)
			return true;
		else
			return false;
	}
	
	//Checks whether language of DFA is empty or not
	public boolean isEmpty(){
		if(this.ending.size()==0)
			return false;
		else
			return this.isEmpty(this.starting, 0, "");
	}

	//Actual implementation of the above function, this is called recursively as well
	private boolean isEmpty(State state, int current, String start ){
		if(state.getType()==StateType.Both|| state.getType()==StateType.Final)
			return false;
		if(current>noOfStates){
			return true;
		}
		for(int i=0;i<alphabets.length;i++){
			if(state.getOutflows().get(alphabets[i])!=null){
				ArrayList<String> temp;
				if(!this.isEmpty(state.getOutflows().get(alphabets[i]), current+1,start+alphabets[i])){
					return false;
				}
			}
		}
		return true;
	}
	
	//Checks whether the language is infinite
	public boolean isInfinite(){
		if(this.ending.size()==0)
			return false;
		else
			return isInfinite(this.starting,0,"");
	}
	
	//Actual implementation of the above function, this is called recursively as well
	private boolean isInfinite(State state, int current, String start){
		if((state.getType()==StateType.Both|| state.getType()==StateType.Final) && current<=2*noOfStates && current>noOfStates )
			return true;
		if(current>2*noOfStates){
			return false;
		}
		for(int i=0;i<alphabets.length;i++){
			if(state.getOutflows().get(alphabets[i])!=null){
				ArrayList<String> temp;
				if(!this.isEmpty(state.getOutflows().get(alphabets[i]), current+1,start+alphabets[i])){
					return false;
				}
			}
		}
		return true;
	}
	
	//MINIMIZATION
	//Iterates through the states and then compares 2 states at a time to determine whether they can be classified as the same or not
	public Dfa minimize(){
		Dfa dfa=new Dfa(states,starting,ending,alphabets,noOfStates);
		String[] keys= dfa.states.keySet().toString().replace(" ","").replace("[","").replace("]","").split(",");
		for(int i=0;i<keys.length;i++){
			for(int j=i+1;j<keys.length;j++){
				State temp=dfa.states.get(keys[i]),temp1=dfa.states.get(keys[j]);
				if(temp.equals(temp1,"",dfa.alphabets,dfa.noOfStates)){
					System.out.println(temp.getName()+" and "+temp1.getName() +" are same");
					replaceState(dfa,temp,temp1);
					j=keys.length;
				}
			}
		}
		return dfa;
	}
	
	//If 2 states are equal/similar then it removes 1 state(the source), then all the outflows are directed through the replaced state
	private void replaceState(Dfa dfa,State src, State replacement) {
		dfa.noOfStates--;
		dfa.states.remove(src.getName());
		for(State itr:dfa.states.values()){
			for(String itr1:itr.getOutflows().keySet()){
				State temp=itr.getOutflows().get(itr1);
				if(temp!=null&&temp.equals(src))
					itr.getOutflows().put(itr1, replacement);
			}
		}
		dfa.ending.remove(src.getName());
	}
	
	//setters and getters
	public HashMap<String,State> getStates() {
		return states;
	}
	public void setStates(HashMap<String,State> states) {
		this.states = states;
	}
	public State getStarting() {
		return starting;
	}
	public void setStarting(State starting) {
		this.starting = starting;
	}
	public HashMap<String,State> getEnding() {
		return ending;
	}
	public void setEnding(HashMap<String,State> ending) {
		this.ending = ending;
	}
	public String[] getAlphabets() {
		return alphabets;
	}
	public void setAlphabets(String[] alphabets) {
		this.alphabets = alphabets;
	}

	@Override
	public String toString() {
		String temp="";
		for(State itr:states.values()){
			temp+=itr.getName()+" ";
		}
		return temp;
	}
	
}
