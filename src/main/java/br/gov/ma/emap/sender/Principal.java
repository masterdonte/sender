package br.gov.ma.emap.sender;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import br.gov.ma.emap.sender.service.SenderService;
import br.gov.ma.emap.sender.service.Stabilizer; 

public class Principal {
	private final static File lockFile = new File("C:/BALANÇA/sender.lock");
	private static boolean shutdownHookExecuted = false;
	
	public static void main(String[] args) {
		if (lockFile.exists()) {
			JOptionPane.showMessageDialog(null, "O Sender já está em execução.\nFeche a aplicação que já está rodando", "ERRO", JOptionPane.ERROR_MESSAGE);
			shutdown(0);
		}
		try {
			lockFile.createNewFile();
			new Thread(new Stabilizer()).start(); //new Thread(() -> new Stabilizer().run()).start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de trava.", "ERRO", JOptionPane.ERROR_MESSAGE);
			shutdown(1);
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			shutdownHookExecuted = true;
			shutdown(0);//Metodo que será executado quando houver algum erro- uma saída abrupta por exemplo
		}));
	}
	
	public static void shutdown(int codigo) {
		if (lockFile != null && lockFile.exists())
            lockFile.delete();
		
		SenderService.finalizar();
		
		if (!shutdownHookExecuted)
            System.exit(codigo);
	}
}
