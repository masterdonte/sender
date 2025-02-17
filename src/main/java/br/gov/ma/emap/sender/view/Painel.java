package br.gov.ma.emap.sender.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import br.gov.ma.emap.sender.Principal;
import br.gov.ma.emap.sender.modelo.Propriedades;
import br.gov.ma.emap.sender.service.Stabilizer;

public class Painel extends JFrame {

	private static final long serialVersionUID = -1211631691971983889L;
	
	private JPanel contentPane;
	private Timer timerStatus;
	private JLabel weight;
	private JLabel status;		
	
	private Stabilizer stabilizer;
	private JTextArea txaMensagens;

	private Painel() {		
		iniciarComponents();
		iniciarStatusSistema();		
	}
	
	public Painel(Stabilizer stabilizer) {
		this();
		this.stabilizer = stabilizer;
	}

	public void iniciarComponents(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sender.png")));
		setTitle("USUÁRIO: " + Propriedades.USER_ACTIVE);
		setBounds(100, 100, 573, 321);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelTitulo = new JPanel();
		panelTitulo.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelTitulo.setBounds(10, 11, 547, 44);
		contentPane.add(panelTitulo);
		panelTitulo.setLayout(null);
		
		JLabel lblTitulo = new JLabel("ESTABILIZADOR BALANÇA");
		lblTitulo.setForeground(Color.RED);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 0, 547, 41);
		panelTitulo.add(lblTitulo);
		
		JPanel panelCorpo = new JPanel();
		panelCorpo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Dados", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panelCorpo.setBounds(10, 66, 547, 217);
		contentPane.add(panelCorpo);
		panelCorpo.setLayout(null);
		
		JLabel lblStatus = new JLabel("STATUS :");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStatus.setBounds(10, 47, 100, 20);
		panelCorpo.add(lblStatus);
		
		JLabel lblPeso = new JLabel("PESO ATUAL :");
		lblPeso.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPeso.setBounds(10, 17, 100, 20);
		panelCorpo.add(lblPeso);
		
		status = new JLabel("STATUS");
		status.setForeground(new Color(0, 0, 255));
		status.setFont(new Font("Tahoma", Font.BOLD, 14));
		status.setBounds(120, 47, 417, 20);
		panelCorpo.add(status);
		
		weight = new JLabel("PESO");
		weight.setForeground(new Color(0, 0, 255));
		weight.setFont(new Font("Tahoma", Font.BOLD, 14));
		weight.setBounds(120, 17, 284, 20);
		panelCorpo.add(weight);
		
		JButton btnSair = new JButton("FECHAR");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				questaoFecharFrame();				
			}
		});
		btnSair.setBounds(10, 183, 89, 23);
		panelCorpo.add(btnSair);
		
		JLabel lblLog = new JLabel("MENSAGENS:");
		lblLog.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLog.setBounds(10, 76, 100, 20);
		panelCorpo.add(lblLog);
		
		txaMensagens = new JTextArea();
		txaMensagens.setBackground(UIManager.getColor("Panel.background"));
		txaMensagens.setBounds(120, 79, 417, 94);
		txaMensagens.setFont(new Font("Tahoma", Font.BOLD, 14));
		txaMensagens.setForeground(new Color(255, 0, 0));
		txaMensagens.setEnabled(false);
		panelCorpo.add(txaMensagens);
		
		JButton btnReprocessar = new JButton("REPESAR");
		btnReprocessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stabilizer.liberarBalanca();
			}
		});
		btnReprocessar.setBounds(448, 183, 89, 23);
		panelCorpo.add(btnReprocessar);
		addWindowListener(new WindowAdapter() {	
			public void windowClosing(WindowEvent evt) { 
				questaoFecharFrame();
			} 
		});	
		setLocationRelativeTo(null);
	}
	
	private void iniciarStatusSistema(){
		status.setText("AGUARDANDO PESAGEM");
		timerStatus = new Timer();		
		timerStatus.schedule(new TimerTask() {			
			public void run() {
				status.setVisible(!status.isVisible()); 				
			}
		},0, 700);
	}	

	public JLabel getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight.setText(weight);
	}

	public JLabel getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status.setText(status);
	}

	public void addMensagem(String texto) {
		String temp = txaMensagens.getText();
		temp += texto + "\n\n";
		this.txaMensagens.setText(temp);
	}
	
	public void clearMensagens() {
		this.txaMensagens.setText("");
	}
	
	public void setEnableMensagens(boolean flag) {
		this.txaMensagens.setEnabled(flag);
	}
	
	protected void questaoFecharFrame(){
		this.setAlwaysOnTop(false);
		if (JOptionPane.showConfirmDialog(this,"Tem certeza que deseja fechar a aplicação?", "AVISO", 
				JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION){
			Principal.shutdown(0);
		}
		this.setAlwaysOnTop(true);
	}
}
