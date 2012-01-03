package com.proschedule.util.messages;

import com.proschedule.main.ProSchedule;
import javax.swing.JOptionPane;

/**
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class QuestionDialog {

    /**
     * Diálogo de confirmação
     * 
     * @param message A mensagem a ser exibida para o usuário
     * @return True se o usuário aceitou ou false se recusou
     */
    public static boolean confirm( String message ) {
        int option = JOptionPane.showConfirmDialog(null, message, ProSchedule.getSystemTitle(), JOptionPane.YES_NO_OPTION);

        if ( option == JOptionPane.YES_OPTION ) {
            return true;
        } else {
            return false;
        }
    }
}
