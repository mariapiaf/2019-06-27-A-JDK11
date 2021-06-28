package it.polito.tdp.crimes.model;

public class Arco {

	private String id1;
	private String id2;
	private int peso;
	
	public Arco(String id1, String id2, int peso) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso = peso;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return id1+" " + id2+" " + " - " + peso;
	}
	
	
}
