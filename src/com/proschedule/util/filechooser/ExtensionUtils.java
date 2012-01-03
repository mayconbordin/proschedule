package com.proschedule.util.filechooser;

import java.io.File;
import javax.swing.ImageIcon;

/**
 * Extensões de arquivos
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ExtensionUtils {
    /**
     * Imagem JPEG
     */
    public final static String jpeg = "jpeg";
    /**
     * Imagem JPEG
     */
    public final static String jpg = "jpg";
    /**
     * Imagem GIF
     */
    public final static String gif = "gif";
    /**
     * Imagem TIFF
     */
    public final static String tiff = "tiff";
    /**
     * Imagem TIF
     */
    public final static String tif = "tif";
    /**
     * Imagem PNG
     */
    public final static String png = "png";
    /**
     * Formato XLS
     */
    public final static String xls = "xls";
    /**
     * Formato XLSX
     */
    public final static String xlsx = "xlsx";


    /**
     * Recupera a extensão de um arquivo
     * 
     * @param f
     * @return
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /**
     * Retorna o ícone da imagem ou null se o caminho for inválido
     * 
     * @param path
     * @return
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ExtensionUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}