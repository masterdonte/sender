package br.gov.ma.emap.sender.dao;

import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import br.gov.ma.emap.sender.Principal;

public class Conexao {

    private static final String ARQUIVO_PROPS = "database.properties";
    private static HikariDataSource dataSource;
    
    static {	// Inicializa o pool de conexões uma única vez
        try {
            carregarConfiguracao();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao configurar conexão: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            Principal.shutdown(0);
        }
    }

    private static void carregarConfiguracao() throws Exception {
        Properties props = new Properties();
        try (InputStream in = Conexao.class.getClassLoader().getResourceAsStream(ARQUIVO_PROPS)) {
            if (in == null) {
                throw new Exception("Arquivo de configuração não encontrado: " + ARQUIVO_PROPS);
            }
            props.load(in);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("oracle.url"));
        config.setUsername(props.getProperty("oracle.user"));
        config.setPassword(props.getProperty("oracle.pass"));
        config.setDriverClassName(props.getProperty("oracle.driver"));

        // Configurações recomendadas do HikariCP        
        config.setMinimumIdle(2);       // Número mínimo de conexões inativas
        config.setIdleTimeout(30000);   // Tempo para liberar conexões inativas (30s)
        config.setMaximumPoolSize(10);  // Número máximo de conexões no pool
        config.setConnectionTimeout(4000); // Tempo máximo para pegar uma conexão (30s)
        config.setValidationTimeout(3000); // Tempo máximo para pegar uma conexão (30s)
        config.setLeakDetectionThreshold(15000); // Detecta conexões presas por mais de 15s
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        config.setPoolName("SENDERPOOL");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void fecharPool() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
