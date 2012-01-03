package com.proschedule.util.filechooser;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author Maycon Bordin
 */
public class XLSFilter extends FileFilter {
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = ExtensionUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(ExtensionUtils.xls) ||
                extension.equals(ExtensionUtils.xlsx)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return ".xls, .xlsx";
    }
}