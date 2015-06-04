package controller;

import java.util.ArrayList;
import model.Dfa;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Dfa df=new Dfa();
		df.init();
		Dfa df2=df.minimize();
		System.out.println(df.toString());
		System.out.println(df2.toString());
		System.out.println(df.checkString("c"));
		System.out.println(df2.checkString("c"));
		ArrayList<String> check=df.getStrings(10);
		if(df.isEmpty()){
			System.out.println("DFA is empty");
		}
		else{
			System.out.println("DFA is not empty");
		}
		if(df.isInfinite()){
			System.out.println("Language of DFA is infinite");
		}
		else{
			System.out.println("Language of DFA is finite");
		}
		if(df.acceptsEbsilon()){
			System.out.println("DFA accepts E");
		}
		else{
			System.out.println("DFA does not accept E");
		}
	}

}

