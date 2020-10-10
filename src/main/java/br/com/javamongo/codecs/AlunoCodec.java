package br.com.javamongo.codecs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import br.com.javamongo.model.Aluno;
import br.com.javamongo.model.Contato;
import br.com.javamongo.model.Curso;
import br.com.javamongo.model.Habilidade;
import br.com.javamongo.model.Nota;

public class AlunoCodec implements CollectibleCodec<Aluno> {
	
	private Codec<Document> codec;

	  public AlunoCodec(Codec<Document> codec) {
	    this.codec = codec;
	  }

	@Override
	public void encode(BsonWriter writer, Aluno aluno, EncoderContext encoderContext) {
		ObjectId id = aluno.getId();
		String nome = aluno.getNome();
		Date dataNascimento = aluno.getDataNascimento();
		Curso curso = aluno.getCurso();
		
		Document documento = new Document();
		documento.put("_id", id);
		documento.put("nome", nome);
		documento.put("data_nascimento", dataNascimento);
		documento.put("curso", new Document("nome", curso.getNome()));
		
		List<Document> habilidadesDocument = new ArrayList<Document>();
		aluno.getHabilidades().forEach(habilidade ->
		      habilidadesDocument.add(new Document("nome", habilidade.getNome())
		          .append("nível", habilidade.getNivel())));
		documento.put("habilidades", habilidadesDocument);
		
		List<Double> notasParaSalvar = new ArrayList<Double>();
		aluno.getNotas().forEach(nota ->  notasParaSalvar.add(nota.getValor()));
		documento.put("notas", notasParaSalvar);
		
		Contato contato = aluno.getContato();


		  documento.put("contato", new Document()
		    .append("endereco" , contato.getEndereco()));
		
		codec.encode(writer, documento, encoderContext);
		
	}

	@Override
	public Class<Aluno> getEncoderClass() {
		return Aluno.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Aluno decode(BsonReader reader, DecoderContext decoderContext) {
		
		Document documento = codec.decode(reader, decoderContext);
		Aluno aluno = new Aluno();
		aluno.setId(documento.getObjectId("_id"));
		aluno.setNome(documento.getString("nome"));
		aluno.setDataNascimento(documento.getDate("data_nascimento"));
		Document curso = (Document) documento.get("curso");
		
		aluno.setCurso(new Curso());
		aluno.getCurso().setNome(curso.getString("nome"));
		
		List<Document> habilidades = (ArrayList<Document>) documento.get("habilidades");
		if(habilidades!=null)
			habilidades
				.forEach(habilidade -> aluno.getHabilidades()
						.add(new Habilidade(habilidade.getString("nome"),habilidade.getString("nível"))));
		
		List<Double> notas = (ArrayList<Double>) documento.get("notas");
		if(notas!=null) 
			notas.forEach(nota -> aluno.getNotas()
					.add(new Nota(nota)));
		
		Document contato = (Document) documento.get("contato");
		  if (contato != null) {
		    String endereco = contato.getString("contato");
		    aluno.setContato(new Contato(endereco));
		  }
		
		return aluno;
	}

	@Override
	public Aluno generateIdIfAbsentFromDocument(Aluno aluno) {
		return documentHasId(aluno) ?  aluno : aluno.criaId();
	}

	@Override
	public boolean documentHasId(Aluno aluno) {
		return aluno.getId() != null;
	}

	@Override
	public BsonValue getDocumentId(Aluno aluno) {
		if(!documentHasId(aluno)){
		    throw new IllegalStateException("Esse Document não tem id");
		  }
		  return new BsonString(aluno.getId().toHexString());
	}

}
