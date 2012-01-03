package com.proschedule.core.calendar.view;

import com.proschedule.core.calendar.model.Day;
import com.proschedule.util.date.DateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel que monta os dias de um mês de acordo com o valores repassados.
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:49
 */
public class MonthPanel extends javax.swing.JPanel {

    private List<javax.swing.JPanel> panels = new ArrayList();
    private List<javax.swing.JTextField> fields = new ArrayList();
    private List<javax.swing.JCheckBox> checks = new ArrayList();
    private List<Day> days;

    /**
     * Construtor da Classe - Inicia os componentes
     *
     * @param days Lista dos dias de um mês
     */
    public MonthPanel( List<Day> days ) {
        this.days = days;
        initComponents();
    }

    /**
     * Cria o título do dia com base no dia do mês e no nome do dia da semana.
     *
     * @param i O dia do mês
     * @return O título criado para o dia
     */
    private String createPanelTitle( int i ) {
        String dayOfWeek = DateUtil.getDayOfWeekString( days.get(i-1).getDate() );

        if ( i < 10 ) {
            return "0" + i + " - " + dayOfWeek;
        } else {
            return "" + i + " - " + dayOfWeek;
        }
    }

    /**
     * Passa os valores da view (o painel) para o model (lista de dias).
     */
    public void viewToModel() {
        for ( int i=0; i < days.size(); i++ ) {
            days.get(i).setWorkingHours( Double.parseDouble( fields.get(i).getText() ) );
            days.get(i).setWorkingDay( checks.get(i).isSelected() );
        }
    }

    /**
     * Passa os valores do model (lista de dias) para a view (o painel).
     */
    public void modelToView() {
         for ( int i=0; i < days.size(); i++ ) {
             fields.get(i).setText( String.valueOf( days.get(i).getWorkingHours() ) );

             if ( days.get(i).isWorkingDay() ) {
                checks.get(i).setSelected(true);
            } else {
                checks.get(i).setSelected(false);
            }
         }
    }

    /**
     * Aplica um valor para todos os text fields que armazenam
     * as horas trabalhadas/dia.
     *
     * @param value Valor a ser aplicado em todos text fields
     */
    public void applyToAllFields( String value ) {
        for ( javax.swing.JTextField field : fields ) {
            field.setText(value);
        }
    }

    /**
     * Seleciona todos os check boxes.
     */
    public void markAllChecks() {
        for ( int i=0; i < days.size(); i++ ) {
            checks.get(i).setSelected(true);
        }
    }

    /**
     * Desceleciona todos os check boxes.
     */
    public void unmarkAllChecks() {
        for ( int i=0; i < days.size(); i++ ) {
            checks.get(i).setSelected(false);
        }
    }

    /**
     * Inicia o painel dos dias do mês
     */
    private void initComponents() {
        //Seta layou como null
        setLayout(null);

        //Direções
        int x = 0;
        int y = 0;

        //Borda
        int xBorder = 15;
        int yBorder = 35;

        //Label
        javax.swing.JLabel label = new javax.swing.JLabel();
        label.setText("Horas   -   Trabalhado");
        label.setSize(120, 25);
        label.setBounds(30, 8, 120, 25);
        add(label);

        //Gera os componentes
        for ( int i=0; i < days.size(); i++,x++ ) {
            //Cria o painel
            javax.swing.JPanel panel = new javax.swing.JPanel();
            panel.setBorder(
                    javax.swing.BorderFactory.createTitledBorder(createPanelTitle(i+1))
            );
            panel.setSize(120, 50);
            panel.setLayout(null);

            //Quebra de linha
            if ( i%6 == 0 && i != 0 ) {
                x = 0;
                y += 50;
            }

            //Posicionamento do painel
            panel.setBounds( (x*130) + xBorder, y + yBorder, 120, 50);

            //Campo de texto
            javax.swing.JTextField field = new javax.swing.JTextField();
            field.setSize(50, 20);
            field.setBounds(15, 20, 50, 20);

            //Adiciona campo ao painel
            panel.add(field);

            //Adiciona campo a lista
            fields.add(field);

            //Checkbox
            javax.swing.JCheckBox check = new javax.swing.JCheckBox();
            check.setBounds(80, 20, 18, 18);

            //Adiciona checkbox ao painel
            panel.add(check);

            //Adiciona checkbox a lista
            checks.add(check);

            //Adiciona painel a lista
            panels.add(panel);

            //Adiciona painel ao painel pai
            add(panel);
        }

        //Carrega os valores para os componentes
        modelToView();
        
    }

    /**
     * @param days the days to set
     */
    public void setDays(List<Day> days) {
        this.days = days;
    }
}
