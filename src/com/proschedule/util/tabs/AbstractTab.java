/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.proschedule.util.tabs;

import java.awt.Component;
import javax.swing.JTabbedPane;

/**
 *
 * @author mb0066559
 */
public class AbstractTab {
    private ButtonTabComponent tab;
    private JTabbedPane tabPanel;
    private Component contentPanel;
    private boolean activated = false;
    private String title = "";
    private int index = -1;

    public AbstractTab( JTabbedPane tabPanel , String title , Component contentPanel ) {
        this.tabPanel = tabPanel;
        this.contentPanel = contentPanel;

        //Seta o título da aba
        setTitle(title);

        //Carrega a aba
        loadTab();
    }

    private void loadTab() {
        //Cria o painel da aba
        tab = new ButtonTabComponent( this );
        
        //Adiciona o painel de conteúdo ao painel de abas
        tabPanel.addTab("", contentPanel);

        //Adiciona o painel da aba ao painel de abas
        tabPanel.setTabComponentAt( tabPanel.getTabCount() - 1 , tab);

        //Destaca a aba
        tabPanel.setSelectedIndex( tabPanel.getTabCount() - 1 );

        //Seta a aba como ativa
        this.setActivated(true);

        //Adiciona a posição da aba no índice
        this.setIndex( tabPanel.getTabCount() - 1 );
    }

    public void setAsSelected() {
        tabPanel.setSelectedIndex( this.getIndex() );
    }

    public void remove() {
        activated = false;
        tabPanel.remove(index);
    }

    /**
     * @return the activated
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * @param activated the activated to set
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the tab
     */
    public ButtonTabComponent getTab() {
        return tab;
    }

    /**
     * @param tab the tab to set
     */
    public void setTab(ButtonTabComponent tab) {
        this.tab = tab;
    }

    /**
     * @return the tabPanel
     */
    public JTabbedPane getTabPanel() {
        return tabPanel;
    }

    /**
     * @param tabPanel the tabPanel to set
     */
    public void setTabPanel(JTabbedPane tabPanel) {
        this.tabPanel = tabPanel;
    }

    /**
     * @return the contentPanel
     */
    public Component getContentPanel() {
        return contentPanel;
    }

    /**
     * @param contentPanel the contentPanel to set
     */
    public void setContentPanel(Component contentPanel) {
        this.contentPanel = contentPanel;
    }
}
