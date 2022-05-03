package it.polito.tdp.poweroutages.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		//System.out.println(model.getNercList());
		
		System.out.println("Lista "+model.trovaSequenza(4, 200, model.getNercList().get(0)));
	}

}
