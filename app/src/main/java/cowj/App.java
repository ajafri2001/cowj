/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cowj;

import zoomba.lang.core.types.ZTypes;

/**
 * Entry point for the Cowj Application
 */
public class App {

    private static boolean PROD_MODE = true;

    /**
     * Is the Cowj system running in the prod mode ?
     * If the 2nd command line is specified as 'true'
     * then it would be running in non prod mode, i.e. dev mode.
     * @return true if prod, i.e. non dev mode, false if dev mode
     */
    public static boolean isProdMode(){ return PROD_MODE ; }

    /**
     * Runs the Cowj App
     * @param args must be at least 1, pointing to a yaml config file
     */
    public static void main(String[] args) {
        if ( args.length < 1 ){
            System.err.println("Usage : java -jar cowj.jar <config_file_path> [true|false(default)]");
            System.err.println("If the 2nd arg is 'true', then automatically reloads script,json schema resources on save.");
            System.err.println("Default is 'false'. Do not run with 'true' in production");
            return;
        }
        if ( args.length > 1 ){
            PROD_MODE = !ZTypes.bool(args[1],false);
            System.out.printf("Casted 2nd arg '%s' as DEV_MODE=%s %n", args[1], !PROD_MODE);
        }
        final String path = args[0];
        ModelRunner mr = ModelRunner.fromModel(path) ;
        mr.run();
    }
}
