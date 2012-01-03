package com.proschedule.main;

import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.facade.CalendarFacade;
import com.proschedule.core.calendar.view.NewCalendarDialog;
import com.proschedule.hibernate.util.HibernateUtil;
import com.proschedule.main.view.MainFrame;
import com.proschedule.util.date.DateUtil;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.messages.QuestionDialog;
import com.proschedule.validator.util.HibernateValidatorUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.swing.UIManager;
import javax.validation.Validator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;

/**
 * Classe principal do sistema, carrega arquivos de configuração, apis, etc.
 * Também possui um main que pode ser usado como alternativa ao main da classe Splash
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ProSchedule {
    private static Properties properties;
    private static Properties config;

    /**
     * Inicializa o sistema, carrega configurações, propriedades, hibernate, validator,
     * look and feel.
     * 
     * @throws ProScheduleException
     */
    public static void startUp() throws ProScheduleException {
        loadConfig();
        loadProperties();
        setSystemLookAndFeel();
        startHibernate();
        startHibernateValidator();

        try {
            HibernateUtil.tryConnection();
        } catch(JDBCConnectionException ex) {
            throw new ProScheduleException(ex, "Não foi possível estabelecer conexão com banco de dados.");
        }
    }

    /**
     * Carrega as propriedades do sistema
     * 
     * @throws ProScheduleException
     */
    public static void loadProperties() throws ProScheduleException {
        try {
            //Cria a classe de configurações
            properties = new java.util.Properties();
            properties.load( new FileInputStream( getPropertiesPath() ) );
        } catch (IOException ex) {
            throw new ProScheduleException(ex, "Ocorreu um erro ao carregar o arquivo de configuração.");
        }
    }

    /**
     * Carrega as configurações do sistema
     * 
     * @throws ProScheduleException
     */
    public static void loadConfig() throws ProScheduleException {
        try {
            //Cria a classe de propriedades
            config = new java.util.Properties();
            config.load( new FileInputStream( getConfigPath() ) );
        } catch (IOException ex) {
            throw new ProScheduleException(ex, "Ocorreu um erro ao carregar o arquivo de configuração.");
        }
    }

    /**
     * Seta no sistema o visual da gui
     * 
     * @throws ProScheduleException
     */
    public static void setSystemLookAndFeel() throws ProScheduleException {
        String confLookAndFeel = getProperties().getProperty("system.look_and_feel");
        String lookAndFeel = null;

        if (confLookAndFeel.equals("Metal")) {
            lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        } else if (confLookAndFeel.equals("System")) {
            lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        } else if (confLookAndFeel.equals("Motif")) {
            lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        } else if (confLookAndFeel.equals("GTK")) {
            lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        } else {
            lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        }

        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception ex) {
            throw new ProScheduleException(ex, "Erro ao carregar o visual do sistema.");
        }
    }

    /**
     * Inicia a sessão com o Hibernate
     * @throws HibernateException
     */
    public static void startHibernate() throws HibernateException {
        Session session = HibernateUtil.getSession();
    }

    /**
     * Inicia sessão com o Hibernate Validator
     */
    public static void startHibernateValidator() {
        Validator validator = HibernateValidatorUtil.getValidator();
    }

    /**
     * Checa se o calendário do ano atual existe
     * 
     * @return True se existir ou false caso contrário
     * @throws CalendarPersistenceException
     */
    public static boolean checkCurrentYearCalendar() throws CalendarPersistenceException {
        CalendarFacade facade = new CalendarFacade();

        if ( !facade.checkCurrentYearCalendar() ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return the properties
     */
    public static Properties getProperties() {
        return properties;
    }

    /**
     * @param props
     */
    public static void setProperties(Properties props) {
        properties = props;
    }

    /**
     * Recupera propriedade com chave informada
     * 
     * @param key Chave da propriedade
     * @return valor da propriedade
     */
    public static String getProperty( String key ) {
        return properties.getProperty(key);
    }

    /**
     * Seta um valor a uma propriedade
     * @param key Chave da propriedade
     * @param value valor da propriedade
     */
    public static void setProperty( String key, String value ) {
        properties.setProperty(key, value);
    }

    /**
     * Recupera uma configuração do sistema
     * @param key A chave da configuração
     * @return o valor da configuração
     */
    public static String getConfig( String key ) {
        return config.getProperty(key);
    }

    /**
     * seta um valor de configuração do sistema
     * @param key A chave da configuração
     * @param value O valor da configuração
     */
    public static void setConfig( String key, String value ) {
        config.setProperty(key, value);
    }

    /**
     * Salva as configurações do sistema
     * @throws ProScheduleException
     */
    public static void saveConfig() throws ProScheduleException {
        try {
            config.store(new FileOutputStream( getConfigPath() ), null);
        } catch (FileNotFoundException ex) {
            throw new ProScheduleException(ex, "Erro ao salvar o arquivo de configuração.");
        } catch (IOException ex) {
            throw new ProScheduleException(ex, "Erro ao salvar o arquivo de configuração.");
        }
    }

    /**
     * Devolve o título do sistema usado nas janelas
     * @return
     */
    public static String getSystemTitle() {
        String systemName = ProSchedule.getProperty("system.name") + " " +
                ProSchedule.getProperty("system.version");

        return systemName;
    }

    /**
     * @return Devolve o caminho para as propriedades do sistema
     * @throws ProScheduleException
     */
    public static String getPropertiesPath() throws ProScheduleException {
        try {
            String path;
            path = ProSchedule.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();

            //Verifica se é ambiente de desenvolvimento ou produção
            if ( path.indexOf("ProSchedule.jar") == -1 ) {
                path = "src/com/proschedule/config/system.properties";
            } else {
                path = path.replace("ProSchedule.jar", "") + "config/system.properties";
            }

            return path;
        } catch (URISyntaxException ex) {
            throw new ProScheduleException(ex, "Não foi possível carregar o arquivo de configuração");
        }
    }

    /**
     * @return Devolve o caminho para as configurações do sistema
     * @throws ProScheduleException
     */
    public static String getConfigPath() throws ProScheduleException {
        try {
            String path;
            path = ProSchedule.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();

            //Verifica se é ambiente de desenvolvimento ou produção
            if ( path.indexOf("ProSchedule.jar") == -1 ) {
                path = "src/com/proschedule/config/config.properties";
            } else {
                path = path.replace("ProSchedule.jar", "") + "config/config.properties";
            }

            return path;
        } catch (URISyntaxException ex) {
            throw new ProScheduleException(ex, "Não foi possível carregar o arquivo de configuração");
        }
    }

    /**
     * @return Devolve o caminho para o sistema
     * @throws ProScheduleException
     */
    public static String getSystemPath() throws ProScheduleException {
       try {
            String path;
            path = ProSchedule.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();

            //Verifica se é ambiente de desenvolvimento ou produção
            if ( path.indexOf("ProSchedule.jar") != -1 ) {
                path = path.replace("ProSchedule.jar", "");
            }

            return path;
        } catch (URISyntaxException ex) {
            throw new ProScheduleException(ex, "Não foi possível carregar o arquivo de configuração");
        }
    }

    /**
     * Fecha o sistema
     * @param status
     * @throws ProScheduleException
     */
    public static void exit( int status ) throws ProScheduleException {
        saveConfig();
        System.exit(status);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Faz a inicialização das configurações
            startUp();
            
            //Inicia a tela principal do sistema
            MainFrame view = new MainFrame();
            view.setVisible(true);

            //Verifica o calendário do ano corrente
            if ( !checkCurrentYearCalendar() ) {
                int year = DateUtil.getCurrentYear();

                if ( QuestionDialog.confirm("O calendário deste ano ("
                        + year + ") ainda não foi criado."
                        + "\nDeseja fazer isso agora?") ) {
                    NewCalendarDialog newCalendar = new NewCalendarDialog(view, year);
                    newCalendar.setVisible(true);
                } else {
                    MessageDialog.info("Não se esqueça de criar o calendário antes de adicionar novas ordens.", view);
                }
            }
        } catch (CalendarPersistenceException ex) {
            MessageDialog.error(ex, null);
        } catch (ProScheduleException ex) {
            MessageDialog.error(ex.getMessage(), ex.getDetailMessage(), null);
            System.exit(1);
        } catch (HibernateException ex) {
            MessageDialog.error(ex.getMessage(), null);
            System.exit(1);
        }
    }
}
