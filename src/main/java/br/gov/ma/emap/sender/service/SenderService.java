package br.gov.ma.emap.sender.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.gov.ma.emap.sender.dao.Conexao;
import br.gov.ma.emap.sender.dao.SenderDao;
import br.gov.ma.emap.sender.modelo.Configuracao;
import br.gov.ma.emap.sender.modelo.Sender;

public class SenderService {
	private final static Logger logger = Logger.getLogger(SenderService.class);
	private static final ExecutorService executor = Executors.newFixedThreadPool(6);

	public static Configuracao getConfig() {
		return SenderDao.getInstancia().getConfiguracao();
	}

	public static void salvarEvento(Sender sender) {
		executor.submit(() -> {
			try {
				SenderDao.getInstancia().salvarEvento(sender);
			} catch (Exception e) {
				sender.setMessage(e.getMessage());
				logger.error(sender);
			}
		});
	}

	public static void salvarLog(Sender sender) {
		executor.submit(() -> {
			try {
				//SenderDao.getInstancia().salvarLog(sender);
				logger.info(sender);
			} catch (Exception e) {
				logger.fatal(sender);
			}
		});
	}

	public static void finalizar() {
		executor.shutdown();
		Conexao.fecharPool();
	}
}
