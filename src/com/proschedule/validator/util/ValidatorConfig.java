package com.proschedule.validator.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Devolve a configuração do validador
 * 
 * @author Maycon Bordin
 * @version 1.0
 * @created 04-out-2010 13:24:50
 */
public class ValidatorConfig {
    /**
     *
     * @return
     */
    public BufferedInputStream getConfigStream() {
        return (BufferedInputStream) getClass().getResourceAsStream( "../config/constraints.xml" );
    }

    /**
     * 
     * @return
     */
    public URL getConfigUrl() {
        return getClass().getResource( "/com/proschedule/validator/config/constraints.xml" );
    }

    /**
     *
     * @return
     */
    public File getConfigFile() {
        return new File( "../config/constraints.xml" );
    }

    /**
     *
     * @return
     * @throws ValidatorException
     */
    public static String getConfigPath() throws ValidatorException {
        try {
            String path;
            path = ValidatorConfig.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();

            //Verifica se é ambiente de desenvolvimento ou produção
            if ( path.indexOf("ProSchedule.jar") == -1 ) {
                path = "src/com/proschedule/validator/config/constraints.xml";
            } else {
                path = path.replace("ProSchedule.jar", "") + "config/constraints.xml";
            }

            return path;
        } catch (URISyntaxException ex) {
            throw new ValidatorException("Não foi possível carregar o arquivo de configuração");
        }
    }
}
