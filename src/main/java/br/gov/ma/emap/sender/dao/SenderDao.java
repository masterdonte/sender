package br.gov.ma.emap.sender.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.gov.ma.emap.sender.modelo.Configuracao;
import br.gov.ma.emap.sender.modelo.Propriedades;
import br.gov.ma.emap.sender.modelo.Sender;

public class SenderDao {
	private static SenderDao instancia;	

	public static SenderDao getInstancia(){
		if(instancia == null){
			instancia = new SenderDao();
		}
		return instancia;
	}

	public Configuracao getConfiguracao() {
		Configuracao conf = null;
		try(Connection conn = Conexao.getConnection()){
			String sql = "SELECT * FROM CONFIGBALANCA C WHERE C.MACHOST = ? OR C.ID = 5 ORDER BY ID";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {	    	
				ps.setString(1, Propriedades.getMacHost());
				ResultSet rs = ps.executeQuery();	           
				if (rs.next()) {	                 
					conf = new Configuracao(rs);
				}
				rs.close();	           
			}			    
		}catch (Exception e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ocorreu uma excecao", JOptionPane.ERROR_MESSAGE);
		}
		return conf;    	        
	}

	public void salvarEvento(Sender sender) throws SQLException{
		try(Connection conn = Conexao.getConnection()){
			String sql = "INSERT INTO SENDER (UUID, DEVICE, TYPE, DESCRIPTION, WEIGHT, OCCURR, USERNAME ) VALUES (?, ?, ?, ?, ?, ?, ?)";      
			try (PreparedStatement ps = conn.prepareStatement(sql)) {	    	
				ps.setString(1, sender.getUuid());
				ps.setString(2, sender.getDevice());
				ps.setInt(3, sender.getType().ordinal());
				ps.setString(4, sender.getType().name());
				ps.setInt(5, sender.getWeight());
				ps.setDate(6, new java.sql.Date(sender.getOccurrence().getTime()));
				ps.setString(7, Propriedades.USER_ACTIVE);
				ps.execute();          	           	          
			}
		}
	}
}
