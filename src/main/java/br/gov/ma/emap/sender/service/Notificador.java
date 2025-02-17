package br.gov.ma.emap.sender.service;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import br.gov.ma.emap.sender.modelo.Sender;

public class Notificador {
	
	private final static int timeout = 15000;

	public boolean notificar(Sender sender) {
		boolean resultado = true;
		SenderService.salvarEvento(sender);
		
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
		
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
			HttpPost post = new HttpPost(sender.getUrl());
			post.setHeader("Content-type", "application/json");
			StringEntity postingString = new StringEntity(sender.getJson(), "UTF-8");
			post.setEntity(postingString);
			
			try (CloseableHttpResponse response = httpClient.execute(post)) {
				if(response.getStatusLine().getStatusCode() != 200) {
					String responseBody = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "Falha ao se comunicar com o Integrador";
					throw new Exception(responseBody);				    
				}  
			}
		} catch (Exception e) {
			sender.setMessage(e.getMessage());
			resultado = false;
		} finally {
			SenderService.salvarLog(sender);
		}
		return resultado;
	}
	
	/*
	//FIXME REMOVER ESTE METODO
	public boolean notificarLiberacao(){			
		boolean resultado = notificar(Propriedades.getUrlLiberacao(), Propriedades.getJsonFormatado("0"));		
		return resultado;    	       
	}
	//FIXME REMOVER ESTE METODO
	public boolean notificarProcessamento(){
		boolean resultado = notificar(Propriedades.getUrlProcessamento(), Propriedades.getJsonFormatado("0"));
		return resultado;    	       
	}
	//FIXME REMOVER ESTE METODO
	public boolean notificarPesagem(int peso){
		boolean resultado = notificar(Propriedades.getUrlPesagem(), Propriedades.getJsonFormatado(peso +""));		
		return resultado;    	       
	}
	//FIXME REMOVER ESTE METODO
	public void registrar(String mensagem){
		if(mensagem != null)
			logger.info("[" + mensagem.toUpperCase() + "]");
	}
	//FIXME REMOVER ESTE METODO
	private boolean notificar(String url, String json) {
		if(!checkUserActive()) {
			logger.warn("Ignorando envio de usuário não ativo: " + Propriedades.USER_ACTIVE);
			return true;//Se não for o usuario ativo, retorna aqui sem notificar
		}
	
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5 * 1000).build();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		HttpPost post = new HttpPost(url);
		boolean resultado = true;
		try {
			StringEntity postingString = new StringEntity(json);
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			httpClient.execute(post);			
			httpClient.close();
			logger.info("[JSON: " + json + ", URL: "+ url +", USER: " + Propriedades.USER_ACTIVE +"]"); 
		} catch (Exception e) {
			resultado = false;
			logger.error(e.getMessage() + " [JSON:"+ json+ ", URL: "+ url +", USER: " + Propriedades.USER_ACTIVE +"]");       
		}        
		return resultado;
	}

	private boolean checkUserActive() {				
		String token;
		try {
			Process process = Runtime.getRuntime().exec(Propriedades.QRY_USER_ACTIVE);
			int waitFor = process.waitFor();
			if(waitFor == 1) return true;// Qualquer erro, vai seguir com a execução
			BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
			String readLine = inStreamReader.readLine();
			token = readLine.split("\\s+")[3];
		}catch(NullPointerException | IOException | InterruptedException | ArrayIndexOutOfBoundsException ex) {
			logger.error(ex.getMessage());
			return true;
		}
		return token != null && (token.equals("Ativo") || token.equals("Active"));
	}
	*/
}
