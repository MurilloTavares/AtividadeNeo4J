package com.ifpb.view;

import com.ifpb.dao.UsuarioDAO;
import com.ifpb.model.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    
    public static void main(String[] args){
        
        Usuario joao = new Usuario("joao@email.com", "Joao", 20);
        Usuario maria = new Usuario("maria@email.com", "Maria", 30);
        
        UsuarioDAO dao = new UsuarioDAO();
        dao.start();
        
        System.out.println("\nSalvando Joao e Maria ...");
        try { dao.salvar(joao); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        try { dao.salvar(maria); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        
        System.out.println("\nAtualizando Maria ...");
        maria.setNome("Maria Lucia");
        try { dao.atualizar(maria.getEmail(), maria); }
        catch (Exception ex) { System.out.println(ex.getMessage()); }
        
        System.out.println("\n---Buscando Joao e Maria---");
        System.out.println(dao.buscar("joao@email.com"));
        System.out.println(dao.buscar("maria@email.com"));
        
        dao.deletar(maria.getEmail());
        maria = dao.buscar(maria.getEmail());
        
        System.out.println("\n---Buscando Maria após deleção---");
        System.out.println(maria);        
        
    }
    
}
