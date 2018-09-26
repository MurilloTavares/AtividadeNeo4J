package com.ifpb.dao;

import com.ifpb.model.Publicacao;
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

public class PublicacaoDAO {
    private final File DB_FILE = new File("redeSocial.db");
    private final Label USER_LABEL = Label.label("user");    
    private final Label PUB_LABEL = Label.label("publicacao");

    GraphDatabaseService db;

    public void start() {
        db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_FILE);
    }

    public void shutdown() {
        db.shutdown();
    }

    private Node getUser(String email) {
        Node node = db.findNode(USER_LABEL, "email", email);
        return node;
    }

    private Node getPub(String titulo){
        Node node = db.findNode(PUB_LABEL, "titulo", titulo);
        return node;
    }
    
    private Node userToNode(Usuario user) {
        Node node = db.createNode(USER_LABEL);
        node.setProperty("email", user.getEmail());
        node.setProperty("nome", user.getNome());
        node.setProperty("idade", user.getIdade());

        return node;
    }
    
    private Node pubToNode(Publicacao pub) {
        Node node = db.createNode(PUB_LABEL);
        node.setProperty("titulo", pub.getTitulo());
        node.setProperty("conteudo", pub.getConteudo());

        return node;
    }

    private Usuario nodeToUser(Node node) {
        Usuario user = new Usuario();
        user.setEmail((String) node.getProperty("email"));
        user.setNome((String) node.getProperty("nome"));
        user.setIdade((Integer) node.getProperty("idade"));

        return user;
    }
    
    private Publicacao nodeToPub(Node node) {
        Publicacao pub = new Publicacao();
        pub.setTitulo((String) node.getProperty("titulo"));
        pub.setConteudo((String) node.getProperty("conteudo"));

        return pub;
    }
    
    public void criarPublicacao(Usuario user, Publicacao pub){
        try (Transaction transaction = db.beginTx()){
            Node publicacao = pubToNode(pub);
            Node usuario = getUser(user.getEmail());
            
            Relationship relationship = usuario
                    .createRelationshipTo(publicacao, Relacionamentos.PUBLICA);            
            relationship.setProperty("desde", LocalDate.now());
            
            transaction.success();
        }
    }
      
    public ArrayList<Publicacao> buscarPublicacoes(Usuario user){        
        ArrayList<Publicacao> pubs = new ArrayList<>();
        
        try (Transaction transaction = db.beginTx()) {
        
            Node node = getUser(user.getEmail());            
            Iterable<Relationship> relacionamentos = node
                    .getRelationships(Relacionamentos.PUBLICA, Direction.OUTGOING);
            
            for(Relationship rel : relacionamentos){
                Publicacao pub = nodeToPub(rel.getEndNode());
                pubs.add(pub);
            }
            
            transaction.success();
        }
        return pubs;
    }
}
