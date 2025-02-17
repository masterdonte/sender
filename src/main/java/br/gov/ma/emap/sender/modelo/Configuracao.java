package br.gov.ma.emap.sender.modelo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Configuracao {
	
	public enum Tipo {LIDER, FILIZOLA}
	
	private Integer id;
	private Integer baudrate;	
	private Integer copias;
	private Integer times;
	private Integer margem;
	private Integer minimo;
	private String  balanca;				
	private String  porta;		
	private String  macHost;
	private String  targetUrl;
	private String  deviceFis;
	private Tipo    modelo;
	private String  socketServer;
	private Integer socketPorta;
	
	public Configuracao(ResultSet rs) throws SQLException {
		id = rs.getInt("id");
		baudrate = rs.getInt("baudrate");
		copias = rs.getInt("copias");
		times = rs.getInt("times");
		margem = rs.getInt("margem");
		minimo = rs.getInt("pesomin");		
		balanca = rs.getString("balanca");		
		porta = rs.getString("porta");							
		macHost = rs.getString("machost");
		targetUrl = rs.getString("targeturl");
		deviceFis = rs.getString("devicefis");
		modelo = Tipo.valueOf(rs.getString("modelo"));
		socketServer = rs.getString("serversocket");
		socketPorta  = rs.getInt("serverport");			
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(Integer baudrate) {
		this.baudrate = baudrate;
	}

	public Integer getCopias() {
		return copias;
	}

	public void setCopias(Integer copias) {
		this.copias = copias;
	}	

	public String getBalanca() {
		return balanca;
	}

	public void setBalanca(String balanca) {
		this.balanca = balanca;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getMacHost() {
		return macHost;
	}

	public void setMacHost(String macHost) {
		this.macHost = macHost;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Tipo getModelo() {
		return modelo;
	}

	public void setModelo(Tipo modelo) {
		this.modelo = modelo;
	}

	public String getDeviceFis() {
		return deviceFis;
	}

	public void setDeviceFis(String deviceFis) {
		this.deviceFis = deviceFis;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getMargem() {
		return margem;
	}

	public void setMargem(Integer margem) {
		this.margem = margem;
	}

	public Integer getMinimo() {
		return minimo;
	}

	public void setMinimo(Integer minimo) {
		this.minimo = minimo;
	}

	public String getSocketServer() {
		return socketServer;
	}

	public void setSocketServer(String socketServer) {
		this.socketServer = socketServer;
	}

	public Integer getSocketPorta() {
		return socketPorta;
	}

	public void setSocketPorta(Integer socketPorta) {
		this.socketPorta = socketPorta;
	}

	@Override
	public String toString() {
		return "Configuracao [balanca=" + balanca + ", macHost=" + macHost + ", targetUrl=" + targetUrl 
				+ ", deviceFis=" + deviceFis + ", times=" + times + ", margem=" + margem + ", pesoMinimo=" + minimo
				+ ", socketServer=" + socketServer + ", socketPorta=" + socketPorta + "]";
	}

}