package br.com.javamongo.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import br.com.javamongo.codecs.AlunoCodec;
import br.com.javamongo.model.Aluno;

@Repository
public class AlunoRepository {

	private MongoClient mongoClient;
	private final String collectionName = "alunos";

	public void salvar(Aluno aluno) {
		if(aluno.getId()!=null) {
			atualizar(aluno);
		} else {
			getCollection().insertOne(aluno);
		}
		closeConnection();
	}
	
	private void atualizar(Aluno aluno) {
		getCollection()
			.updateOne(Filters.eq("_id",aluno.getId()), new Document("$set",aluno));
	}

	private MongoCollection<Aluno> getCollection() {
		MongoClient cliente = getClientInstance();
		MongoDatabase bancoDeDados = cliente.getDatabase("teste");
		MongoCollection<Aluno> alunos = bancoDeDados.getCollection(collectionName, Aluno.class);
		return alunos;
	}

	private MongoClient getClientInstance() {
		MongoCredential credential = MongoCredential.createCredential("admin", "teste", "admin".toCharArray());
		mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential),
				getClientOptions());
		return mongoClient;
	}

	private MongoClientOptions getClientOptions() {
		MongoClientOptions options = MongoClientOptions.builder().codecRegistry(codecAluno()).build();
		return options;
	}

	private CodecRegistry codecAluno() {
		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
		AlunoCodec alunoCodec = new AlunoCodec(codec);

		CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromCodecs(alunoCodec));
		return registro;
	}

	private void closeConnection() {
		mongoClient.close();
	}

	public List<Aluno> listar() {
		List<Aluno> alunosList = new ArrayList<Aluno>();
		getCollection().find().forEach((Aluno aluno) -> alunosList.add(aluno));
		/*
		 * alunos.find().forEach(new Consumer<Aluno>() {
		 * 
		 * @Override public void accept(Aluno t) { alunosList.add((Aluno) t);
		 * 
		 * } });
		 */
		closeConnection();
		return alunosList;

	}
	
	public Aluno obterAlunoPor(String id) {
		return getCollection().find(Filters.eq("_id", new ObjectId(id))).first();
	}
	
	public List<Aluno> obterAlunosPor(String nome) {
		List<Aluno> alunos = new ArrayList<Aluno>();
		getCollection().find(Filters.eq("nome", nome)).forEach((Aluno aluno) -> alunos.add(aluno));
		return alunos;
	}

}
