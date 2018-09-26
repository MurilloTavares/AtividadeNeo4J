package com.ifpb.dao;

import com.ifpb.model.Relacionamentos;
import com.ifpb.model.Usuario;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class RelacionamentoDAO {
    
    private final File DB_FILE = new File("redeSocial.db");
    private final Label LABEL = Label.label("user");

    GraphDatabaseService db;

    public void start() {
        db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_FILE);
    }

    public void shutdown() {
        db.shutdown();
    }

    private Node getNode(String email) {
        Node node = db.findNode(LABEL, "email", email);
        return node;
    }

    private Node userToNode(Usuario user) {
        Node node = db.createNode(LABEL);
        node.setProperty("email", user.getEmail());
        node.setProperty("nome", user.getNome());
        node.setProperty("idade", user.getIdade());

        return node;
    }

    private Usuario nodeToUser(Node node) {
        Usuario user = new Usuario();
        user.setEmail((String) node.getProperty("email"));
        user.setNome((String) node.getProperty("nome"));
        user.setIdade((Integer) node.getProperty("idade"));

        return user;
    }
    
    private void criarRelacionamento(Usuario user, Relacionamentos rel, Usuario destino){
        try (Transaction transaction = db.beginTx()) {
            Node nodeUser = getNode(user.getEmail());
            Node nodeDestino = getNode(destino.getEmail());
            
            Relationship relationship = nodeUser.createRelationshipTo(nodeDestino, rel);
            relationship.setProperty("desde", LocalDate.now());

            transaction.success();
        }
    }
    
    public void criarAmizade(Usuario user, Usuario amigo){
        criarRelacionamento(user, Relacionamentos.AMIGO, amigo);
    }
    
    public void criarSeguir(Usuario user, Usuario seguido){
        criarRelacionamento(user, Relacionamentos.SEGUE, seguido);
    }
    
    public ArrayList<Usuario> buscarAmigos(Usuario user){        
        ArrayList<Usuario> amigos = new ArrayList<>();
        
        try (Transaction transaction = db.beginTx()) {
        
            Node node = getNode(user.getEmail());            
            Iterable<Relationship> relacionamentos = node
                    .getRelationships(Relacionamentos.AMIGO, Direction.OUTGOING);
            
            for(Relationship rel : relacionamentos){
                Usuario amigo = nodeToUser(rel.getEndNode());
                amigos.add(amigo);
            }
            
            transaction.success();
        }
        return amigos;
    }
    
}
