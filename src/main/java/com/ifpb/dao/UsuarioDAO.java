package com.ifpb.dao;

import com.ifpb.model.Usuario;
import java.io.File;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

public class UsuarioDAO {

    private final File DB_FILE = new File("redeSocial.db");
    private final Label LABEL = Label.label("user");

    GraphDatabaseService db;

    public void start() {
        db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_FILE);
    }

    public void shutdown() {
        db.shutdown();
    }

    public Node getNode(String email) {
        Node node = db.findNode(LABEL, "email", email);
        return node;
    }

    public void salvar(Usuario u) throws Exception {
        try (Transaction transaction = db.beginTx()) {

            Node busca = getNode(u.getEmail());
            if (busca != null){
                throw new Exception("Já existe um usuário com o email " + u.getEmail());
            }
            
            Node node = db.createNode(LABEL);
            node.setProperty("email", u.getEmail());
            node.setProperty("nome", u.getNome());
            node.setProperty("idade", u.getIdade());

            transaction.success();
        }
    }

    public Usuario buscar(String email) {
        Usuario u = null;

        try (Transaction transaction = db.beginTx()) {
            Node result = getNode(email);
            if (result == null) {
                return u;
            }

            u = new Usuario();
            u.setEmail((String) result.getProperty("email"));
            u.setNome((String) result.getProperty("nome"));
            u.setIdade((Integer) result.getProperty("idade"));

            return u;
        }
    }

    public void atualizar(String email, Usuario u) throws Exception {
        try (Transaction transaction = db.beginTx()) {
            Node result = getNode(email);
            if(result == null){
                throw new Exception("Email "+email+" nao encontrado");
            }
            
            result.setProperty("email", u.getEmail());
            result.setProperty("nome", u.getNome());
            result.setProperty("idade", u.getIdade());

            transaction.success();
        }
    }

    public void deletar(String email) {
        try (Transaction transaction = db.beginTx()) {
            Node node = getNode(email);
            
            if(node != null){                
                node.delete();
            }            

            transaction.success();
        }
    }

}
