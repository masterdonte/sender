package br.gov.ma.emap.sender.service;

import java.util.UUID;

public class Gerenciador {
	
	private final int TIMES;
	private final int MARGEM;
	private final int MINPESO;

	private boolean reading;
	private int count;
	private int currWeight;
	private int prevWeight;
	private boolean inProcess;
	private String uuid;
	
	public Gerenciador(int times, int margem, int pesoMin) {
		TIMES   = times;
		MARGEM  = margem;
		MINPESO = pesoMin;
		newReading();
	}
	
	public boolean isNaMargemLeituraAnterior() {
		boolean margemMenor = (prevWeight >= (currWeight - MARGEM));
		boolean margemMaior = (prevWeight <= (currWeight + MARGEM));
		boolean resultado = margemMenor && margemMaior && !isZeroed();
		return resultado;
	}

	public boolean isReading() {
		return reading;
	}

	public void setReading(boolean reading) {
		this.reading = reading;
	}

	public int getCurrWeight() {
		return currWeight;
	}

	public void setCurrWeight(int currWeight) {
		this.currWeight = currWeight;
	}

	public int getPrevWeight() {
		return prevWeight;
	}

	public void setPrevWeight(int prevWeight) {
		this.prevWeight = prevWeight;
	}

	public boolean isInProcess() {
		return inProcess;
	}

	public void setInProcess(boolean inProcess) {
		this.inProcess = inProcess;
	}

	public String getUuid() {
		return uuid;
	}

	private void increaseCount(){
		++count;
	}

	public boolean podeNotificarProcessamento(){		
		return !isZeroed() && inProcess == false;		
	}
	
	public boolean podeNotificarPesagem(){
		increaseCount();
		return count > TIMES && !isReading() && !isZeroed() && inProcess;
	}	
	
	public boolean podeRepesar(){
		return count > TIMES && isReading() && isNaMargemLeituraAnterior() && inProcess;
	}	
	
	public void clearCount() {
		count = 0;
	}

	public boolean isZeroed() {
		return currWeight < MINPESO;
	}
	
	public void newReading() {
		count = 0;
		prevWeight = 0;
		currWeight = 0;
		reading = false;
		inProcess = false;
		uuid = UUID.randomUUID().toString();
	}

	public int getAverageWeight() {
		return currWeight;//Não está fazendo a média... Modificar se solicitado
	}
	

}
