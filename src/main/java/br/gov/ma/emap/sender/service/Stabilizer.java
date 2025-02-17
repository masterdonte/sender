package br.gov.ma.emap.sender.service;

import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import br.gov.ma.emap.sender.Principal;
import br.gov.ma.emap.sender.modelo.Propriedades;
import br.gov.ma.emap.sender.modelo.Sender;
import br.gov.ma.emap.sender.modelo.Type;
import br.gov.ma.emap.sender.view.Painel;

public class Stabilizer implements Runnable {
	
	private Socket socket;
	private InputStream servidor;
	private Scanner leitor;
	private Notificador notifier;
	private Gerenciador manager;
	private Painel frame;

	public Stabilizer() {
		iniciar();
	}
	
	private void iniciar(){			
		try {
			socket = new Socket(Propriedades.getSocketServer(), Propriedades.getSocketPorta());
			servidor = socket.getInputStream();
			notifier = new Notificador();
			manager = new Gerenciador(Propriedades.getTimes(), Propriedades.getMargem(), Propriedades.getMinimo() );
			frame = new Painel(this);
			frame.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			Principal.shutdown(0);
		}		
	}

	public void run() {
		leitor = new Scanner(this.servidor);
		while (leitor.hasNextLine()) {
			manager.setPrevWeight(manager.getCurrWeight());			
			manager.setCurrWeight(Integer.parseInt(leitor.nextLine()));
			
			frame.setWeight(manager.getCurrWeight() + "");
			
			if(manager.podeNotificarProcessamento()){		
				processarInicioPesagem();
			}
			
			if (manager.isNaMargemLeituraAnterior()) {
				if (manager.podeNotificarPesagem()) {
					processarPesagem(Type.PESAGEM);
				}
			} else {
				manager.clearCount();
				if (manager.isZeroed() && manager.isInProcess()) {
					processarFimPesagem();
				}
			}
		}
	}
	
	private void processarInicioPesagem(){
		Sender sender = new Sender(manager.getUuid(), Type.ENTRADA, Propriedades.getUrlProcessar());
		if(notifier.notificar(sender)){
			manager.setInProcess(true);
			frame.clearMensagens();
			frame.setEnableMensagens(true);
			frame.addMensagem("MENSAGEM PROCESSANDO ENVIADA");
			frame.setStatus("PROCESSANDO PESAGEM - ESTABILIZANDO");
		}			
	}
	
	private void processarPesagem(Type type){
		Sender sender = new Sender(manager.getUuid(), type, manager.getAverageWeight(), Propriedades.getUrlPesar());
		if(notifier.notificar(sender)){
			manager.setReading(true);
			frame.addMensagem("MENSAGEM PESAGEM DE " + manager.getAverageWeight()  + "KG ENVIADA");	
			frame.setStatus("PESAGEM PROCESSADA, AGUARDANDO LIBERACAO");
		}
	}

	private void processarFimPesagem(){
		Sender sender = new Sender(manager.getUuid(), Type.SAIDA, Propriedades.getUrlLiberar());
		if(notifier.notificar(sender)){
			manager.newReading();						
			frame.setEnableMensagens(false);
			frame.addMensagem("MENSAGEM LIBERAR BALANÇA ENVIADA");			
			frame.setStatus("AGUARDANDO NOVA PESAGEM");
		}
	}
	
	public void liberarBalanca(){
		frame.setAlwaysOnTop(false);
		if (JOptionPane.showConfirmDialog(frame,"OPERAÇÃO CRÍTICA NO SISTEMA\n"
				+ "Toda esta operação está sendo gravada.\nProsseguir com a liberação?",
				"AVISO", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION){
			
			if(!manager.podeRepesar()){
				JOptionPane.showMessageDialog(frame, "Situação atual não permite repesagem", "ERRO", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			processarPesagem(Type.REPESAGEM);
		} 
		frame.setAlwaysOnTop(true);		
	}

}
