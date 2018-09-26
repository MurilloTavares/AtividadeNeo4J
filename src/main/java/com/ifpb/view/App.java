package com.ifpb.view;

import com.ifpb.dao.PublicacaoDAO;
import com.ifpb.dao.RelacionamentoDAO;
import com.ifpb.dao.UsuarioDAO;
import com.ifpb.model.Publicacao;
import com.ifpb.model.Usuario;

public class App {
    
    public static void main(String[] args){
                
        // --- Usuario CRUD ---
        UsuarioDAO uDao = new UsuarioDAO();
        uDao.start();
        
        System.out.println("\nSalvando Usuarios ...");   
        
        Usuario joao = new Usuario("joao@email.com", "Joao", 20);
        try { uDao.salvar(joao); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }        
        
        Usuario maria = new Usuario("maria@email.com", "Maria", 30);
        try { uDao.salvar(maria); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        Usuario ana = new Usuario("ana@email.com", "Ana", 30);
        try { uDao.salvar(ana); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        Usuario joaquim = new Usuario("joaquim@email.com", "Joaquim", 25);
        try { uDao.salvar(joaquim); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        Usuario carlos = new Usuario("carlos@email.com", "Carlos", 20);
        try { uDao.salvar(carlos); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        
        System.out.println("\nAtualizando Maria ...");
        maria.setNome("Maria Lucia");
        try { uDao.atualizar(maria.getEmail(), maria); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        System.out.println("\n---Buscando Joao e Maria---");
        System.out.println(uDao.buscar("joao@email.com"));
        System.out.println(uDao.buscar("maria@email.com"));
        
        uDao.deletar(maria.getEmail());
        maria = uDao.buscar(maria.getEmail());
        
        System.out.println("\n---Buscando Maria após deleção---");
        System.out.println(maria);
        
        uDao.shutdown();

        // --- Criando e Buscando Amizades ---        
        RelacionamentoDAO rDao = new RelacionamentoDAO();
        rDao.start();
        System.out.println("\nCriando amizades ...");
        
        rDao.criarAmizade(joao, ana);
        rDao.criarAmizade(joao, carlos);
        rDao.criarAmizade(ana, joaquim);
        
        System.out.println("\n---Buscando amigos de Joao---");
        for(Usuario amigo: rDao.buscarAmigos(joao)){
            System.out.println(amigo.getNome());
        }
        
        System.out.println("\n---Buscando amigos de amigos de Joao---");
        for(Usuario amigo: rDao.buscarAmigos(joao)){
            for(Usuario amigoDeAmigo: rDao.buscarAmigos(amigo)){
                System.out.println(amigoDeAmigo.getNome());
            }
        }
        
        rDao.shutdown();
        
        //--- Criando e buscando publicacoes ---
        PublicacaoDAO pDao = new PublicacaoDAO();
        pDao.start();
        System.out.println("\nCriando publicacoes ...");
        Publicacao pubA = new Publicacao("Publicacao A", "Conteudo da publicação A.");
        Publicacao pubB = new Publicacao("Publicacao B", "Conteudo da publicacao B.");
        
        pDao.criarPublicacao(joao, pubA);
        pDao.criarPublicacao(joao, pubB);
        
        System.out.println("\n---Buscando publicacoes de Joao---");
        for(Publicacao pub: pDao.buscarPublicacoes(joao)){
            System.out.println(pub);
        }
        
        pDao.shutdown();
        
    }
    
}
