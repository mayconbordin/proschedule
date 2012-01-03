package com.proschedule.core.scheduling.exportation.excel;

import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Set;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class SheetModel {
    private Sheet sheet;
    private String type;
    private int week;
    private int startWeek;
    private Set currentSet;
    private int startSet;
    private Operation operation;

    /**
     * Construtor da Classe
     */
    public SheetModel() {

    }

    /**
     * Construtor da Classe
     * 
     * @param sheet
     * @param type
     * @param week
     * @param startWeek
     */
    public SheetModel(Sheet sheet, String type, int week, int startWeek) {
        this.sheet = sheet;
        this.type = type;
        this.week = week;
        this.startWeek = startWeek;
    }

    /**
     * Construtor da Classe
     *
     * @param sheet
     * @param type
     * @param week
     * @param startWeek
     * @param operation
     */
    public SheetModel(Sheet sheet, String type, int week, int startWeek, Operation operation) {
        this.sheet = sheet;
        this.type = type;
        this.week = week;
        this.startWeek = startWeek;
        this.operation = operation;
    }

    /**
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * @param sheet the sheet to set
     */
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the week
     */
    public int getWeek() {
        return week;
    }

    /**
     * @param week the week to set
     */
    public void setWeek(int week) {
        this.week = week;
    }

    /**
     * @return the startWeek
     */
    public int getStartWeek() {
        return startWeek;
    }

    /**
     * @param startWeek the startWeek to set
     */
    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    /**
     * @return the currentSet
     */
    public Set getCurrentSet() {
        return currentSet;
    }

    /**
     * @param currentSet the currentSet to set
     */
    public void setCurrentSet(Set currentSet) {
        this.currentSet = currentSet;
    }

    /**
     * @return the startSet
     */
    public int getStartSet() {
        return startSet;
    }

    /**
     * @param startSet the startSet to set
     */
    public void setStartSet(int startSet) {
        this.startSet = startSet;
    }

    /**
     * @return the operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

}
