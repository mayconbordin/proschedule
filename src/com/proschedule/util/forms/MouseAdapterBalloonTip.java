/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.util.forms;

import javax.swing.JComponent;
import net.java.balloontip.BalloonTip;

/**
 * Adaptador para evento de mouse para balões de dicas
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class MouseAdapterBalloonTip extends java.awt.event.MouseAdapter {
    private JComponent component;
    private BalloonTip balloon;

    /**
     * Construtor da Classe
     * @param component
     * @param message
     */
    public MouseAdapterBalloonTip(JComponent component, String message) {
        balloon = new BalloonTip(component, message);
        balloon.setVisible(false);
        balloon.setCloseButton(null);
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        balloon.setVisible(true);
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        balloon.setVisible(false);
    }
}
