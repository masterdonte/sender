package br.gov.ma.emap.sender.modelo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.gov.ma.emap.sender.dao.SenderDao;

public class Propriedades {
	
	public  final static String USER_ACTIVE = System.getenv("username");
	//FIXME REMOVER ESTAS VARIAVEIS
	//public  final static String QRY_USER_ACTIVE = "cmd /c \"quser \"" + USER_ACTIVE + "\" | findstr \"" + USER_ACTIVE + "\"\"";
	//private final static String JSON_MODELO = "{\"dispositivo\":\"%s\",\"peso\":\"%s\"}";
	
	private static Configuracao configuracao;
	
	static{
		configuracao = SenderDao.getInstancia().getConfiguracao();
		System.out.println(configuracao);
	}
	
	public static String getUrlLiberar(){
		return configuracao.getTargetUrl() + "/liberar";
	}
	
	public static String getUrlProcessar(){
		return configuracao.getTargetUrl() + "/processar";
	}

	public static String getUrlPesar(){
		return configuracao.getTargetUrl();
	}
	
	public static int getTimes(){
		return configuracao.getTimes();
	}
	
	public static int getMargem(){
		return configuracao.getMargem();
	}
	
	public static int getMinimo(){
		return configuracao.getMinimo();
	}
	
	public static String getSocketServer(){
		return configuracao.getSocketServer();
	}
	
	public static int getSocketPorta(){
		return configuracao.getSocketPorta();
	}
	//FIXME REMOVER ESTE METODO
	/*
	public static String getJsonFormatado(String peso){
		return String.format(JSON_MODELO, configuracao.getDeviceFis(), peso);
	}*/
	
	public static String getDevice(){
		return configuracao.getDeviceFis();
	}
	
	public static String getMacHost(){
		String result = null;
		try {         
			InetAddress address = InetAddress.getLocalHost();  
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);  
			byte[] mac = ni.getHardwareAddress();
			String macAddress = "";
			for (int i = 0; i < mac.length; i++) {             
				macAddress += (String.format("%02X-", mac[i]));  
			}		
			result =  macAddress.substring(0, macAddress.length()-1);
		} catch (UnknownHostException | SocketException e) {  
			e.printStackTrace();
		}
		return result;
	}
	
	public static String config(){
		return configuracao.toString();
	}
}
