package br.gov.ma.emap.sender;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import br.gov.ma.emap.sender.service.SenderService;
import br.gov.ma.emap.sender.service.Stabilizer; 

public class Principal {
	
	private final static File lockFile = new File("C:/ProgramData/Balanca/sender.lock");
	private static boolean shutdownHookExecuted = false;
	
	public static void main(String[] args) {
		
		try {
			
			File diretorio = lockFile.getParentFile();

	        if (diretorio != null && !diretorio.exists()) {
	            if (!diretorio.mkdirs())
	                throw new IOException();
	        }
	        
			if (lockFile.exists()) {
				JOptionPane.showMessageDialog(null, "O Sender já está em execução.\nFeche a aplicação que já está rodando", "ERRO", JOptionPane.ERROR_MESSAGE);
				shutdown(0);
			}
			
			lockFile.createNewFile();
			new Thread(new Stabilizer()).start(); //new Thread(() -> new Stabilizer().run()).start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Não foi possível criar os arquivos necessários.", "ERRO", JOptionPane.ERROR_MESSAGE);
			shutdown(1);
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {//Metodo a se chamado com saidas abruptas ou finalizacao de processo
			shutdownHookExecuted = true;
			shutdown(0);
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
