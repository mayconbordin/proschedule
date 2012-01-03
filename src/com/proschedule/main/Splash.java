package com.proschedule.main;

import com.proschedule.core.calendar.exceptions.CalendarPersistenceException;
import com.proschedule.core.calendar.view.NewCalendarDialog;
import com.proschedule.hibernate.util.HibernateUtil;
import com.proschedule.main.view.MainFrame;
import com.proschedule.util.date.DateUtil;
import com.proschedule.util.messages.MessageDialog;
import com.proschedule.util.messages.QuestionDialog;
import java.awt.*;
import org.hibernate.HibernateException;
import org.hibernate.exception.JDBCConnectionException;

/**
 * Classe que cria o splash screen de abertura
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class Splash {

    private final SplashScreen splash = SplashScreen.getSplashScreen();
    private Graphics2D g;
    private static Color orange = new Color(233, 116, 0);
    private static Color grey = new Color(57, 57, 57);

    /**
     * Construtor da Classe - inicializa o splash e todos componentes e configurações
     * do sistema.
     */
    public Splash() {
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }

        g = (Graphics2D)splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }

        try {
            //Faz a inicialização das configurações
            setProgress(0, "Carregando configurações...");
            ProSchedule.loadConfig();
            setProgress(20, "Carregando propriedades...");
            ProSchedule.loadProperties();
            setProgress(40, "Carregando componentes visuais...");
            ProSchedule.setSystemLookAndFeel();
            setProgress(60, "Carregando Hibernate...");
            ProSchedule.startHibernate();
            setProgress(80, "Carregando Hibernate Validator...");
            ProSchedule.startHibernateValidator();
            setProgress(90, "Verificando conexão com BD...");
            try {
                HibernateUtil.tryConnection();
            } catch(JDBCConnectionException ex) {
                throw new ProScheduleException(ex, "Não foi possível estabelecer conexão com banco de dados.");
            }
            setProgress(100, "Concluído...");

            //Inicia a tela principal do sistema
            MainFrame view = new MainFrame();
            view.setVisible(true);

            //Verifica o calendário do ano corrente
            if ( !ProSchedule.checkCurrentYearCalendar() ) {
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

    /**
     * Renderiza a barra de progresso
     * 
     * @param g
     * @param frame
     * @param text
     */
    static void renderSplashFrame(Graphics2D g, int frame, String text) {
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(0,320,517,40);
            g.setPaintMode();
            g.setColor( grey );
            g.drawString(text, 10, 330);
            g.setColor( orange );
            g.fillRect(0,340, (int) (frame*5.17),5);
    }

    /**
     * Seta o progresso atual do carregamento do sistema
     * @param progress
     * @param text
     */
    public void setProgress(int progress, String text) {
        renderSplashFrame(g, progress, text);
        splash.update();
    }

    /**
     * @param args
     */
    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Splash test = new Splash();
            }
        });
    }
}
