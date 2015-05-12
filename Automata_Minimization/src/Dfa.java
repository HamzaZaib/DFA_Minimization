import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
		this.states = states;
		this.starting = starting;
		this.ending = ending;
		this.alphabets=alphabets;
		this.noOfStates=noOfStates;
	}
	
	void init(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
			this.alphabets=reader.readLine().split(",");
			String tokens[]=reader.readLine().split(",");
			State temp;
			for(int i=0;i<tokens.length;i++){
				states.put(tokens[i], new State(tokens[i]));
			}
			noOfStates=tokens.length;
			for(int i=0;i<noOfStates;i++){
				tokens=reader.readLine().split(" ");
				temp=states.get(tokens[0]);
				for(int j=0;j<alphabets.length;j++){
					if(tokens[j+1].equals(".")){
						temp.outflows.put(alphabets[j], null);
					}
					else{
						temp.outflows.put(alphabets[j],states.get(tokens[j+1]));
					}
				}
				switch(tokens[tokens.length-1].charAt(0)){
					case 'b':
						temp.Type=StateType.Both;
						ending.put(tokens[0], temp);
						starting=temp;
						break;
					case 'f':
						temp.Type=StateType.Final;
						ending.put(tokens[0], temp);
						break;
					case 's':
						temp.Type=StateType.Start;
						starting=temp;
						break;
					default:
						temp.Type=StateType.Neither;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
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
	
	void minimize(){
		String[] keys= states.keySet().toString().replace(" ","").replace("[","").replace("]","").split(",");
		for(int i=0;i<keys.length;i++){
			for(int j=i+1;j<keys.length;j++){
				State temp=states.get(keys[i]),temp1=states.get(keys[j]);
				if(temp.equals(temp1,"",alphabets,noOfStates)){
					System.out.println(temp.name+" and "+temp1.name +" are same");
					replaceState(temp,temp1);
					j=keys.length;
				}
			}
		}
	}
	
	private void replaceState(State src, State replacement) {
		states.remove(src.name);
		for(State itr:states.values()){
			for(String itr1:itr.outflows.keySet()){
				State temp=itr.outflows.get(itr1);
				if(temp!=null&&temp.equals(src))
					itr.outflows.put(itr1, replacement);
			}
		}
		ending.remove(src.name);
	}
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
	
}
