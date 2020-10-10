package br.com.javamongo.model;

import java.util.ArrayList;
import java.util.List;

public class Contato {
	
	  private String endereco;

	  public String getEndereco() {
	    return endereco;
	  }
	  public void setEndereco(String endereco) {
	    this.endereco = endereco;
	  }
	  
	  public Contato() { }

	  public Contato(String endereco) {
	    this.endereco = endereco;
	  }

	}
