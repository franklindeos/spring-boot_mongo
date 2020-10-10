package br.com.javamongo.model;

public class Nota {
private Double valor;

public Nota(Double nota) {
	this.valor = nota;
}

public Nota() { }

public Double getValor() {
	return valor;
}

public void setValor(Double valor) {
	this.valor = valor;
}


}
