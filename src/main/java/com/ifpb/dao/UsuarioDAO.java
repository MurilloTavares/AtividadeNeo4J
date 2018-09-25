package com.ifpb.dao;

import com.ifpb.model.Usuario;
import java.io.File;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

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

    public void salvar(Usuario u) throws Exception {
        try (Transaction transaction = db.beginTx()) {

            Node busca = getNode(u.getEmail());
            if (busca != null) {
                throw new Exception("Já existe um usuário com o email " + u.getEmail());
            }

            userToNode(u);

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

            u = nodeToUser(result);

            return u;
        }
    }

    public void atualizar(String email, Usuario u) throws Exception {
        try (Transaction transaction = db.beginTx()) {
            Node result = getNode(email);
            if (result == null) {
                throw new Exception("Email " + email + " nao encontrado");
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

            if (node != null) {
                node.delete();
            }

            transaction.success();
        }
    }

}
