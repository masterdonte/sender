package br.gov.ma.emap.sender.modelo;

import java.util.Date;

public class Sender {
	private String uuid;
	private String device;
	private Type type; // 0 - entrada, 1 - pesagem, 2 - saida, 3 - repesagem 
	private Date occurrence;
	private String user;
	private Integer weight;
	private String message;
	private String url;
	
	public Sender(String uuid, Type type, Integer weight, String url) {
		this.url = url;
		this.type = type;
		this.uuid = uuid;
		this.weight = weight;
		this.occurrence = new Date();
		this.user = Propriedades.USER_ACTIVE;
		this.device = Propriedades.getDevice();
	}
	
	public Sender(String uuid, Type type, String url) {
		this.url = url;
		this.type = type;
		this.uuid = uuid;
		this.weight = 0;
		this.occurrence = new Date();
		this.user = Propriedades.USER_ACTIVE;
		this.device = Propriedades.getDevice();
	}

	public String getUuid() {
		return uuid;
	}

	public String getDevice() {
		return device;
	}

	public Type getType() {
		return type;
	}

	public Date getOccurrence() {
		return occurrence;
	}

	public String getUser() {
		return user;
	}

	public Integer getWeight() {
		return weight;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getJson() {
		return String.format("{\"uuid\":\"%s\",\"dispositivo\":\"%s\",\"peso\":\"%s\"}", uuid, device, weight);
	}

	@Override
	public String toString() {
		return "Sender [uuid=" + uuid + ", device=" + device + ", type=" + type + ", occurr=" + occurrence
				+ ", user=" + user + ", weight=" + weight + ", message=" + message + ", url=" + url + "]";
	}

}
