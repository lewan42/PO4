package grails;

import model.Attr;
import model.Table;
import model.Utils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Project {
    private final static String GRAILS_PATH = "/Users/lewan/Downloads/grails-4.0.3/bin/grails";
    private final static String PATH = "/Users/lewan/Desktop/PO4";
    private final static String INNER_PATH = "/grails-app/domain/";
    private final static String APP = "app";
    private final static String SITE_APP = "http://localhost:8080/";
    private int appCount = 1;

    public Project() {

    }


    public void create() {
        Runtime rt = Runtime.getRuntime();

        while (fileExist(APP + appCount))
            appCount++;


        try {
//            Process proc1 = rt.exec("cd ..");
//            int exitVal1 = proc1.waitFor();
//            System.out.println("Process exitValue: " + proc1);
//
//            Process proc2 = rt.exec("cd /grails");
//            int exitVal2 = proc2.waitFor();
//            System.out.println("Process exitValue: " + proc2);
            //String[] cmd = {"cmd", "-c", GRAILS_PATH + " create-app app" + projectName + "; cd app" + projectName + "; ls -la"};

            String domain = GRAILS_PATH + " create-domain-class ";
            StringBuilder commands = new StringBuilder();

            for (Table t : Utils.listTables) {
                commands.append(domain).append(t.getName().getText()).append("; ");
            }

            String[] cmd = {"/bin/sh", "-c", GRAILS_PATH + " create-app " + getNameApp() + "; cd " + getNameApp() + "; " + commands.toString()};
            Process p = rt.exec(cmd);
            // while (p.isAlive()) ;


            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String createContentFile(Table table) {

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        Template t = velocityEngine.getTemplate("src/main/resources/index.vm");

        VelocityContext context = new VelocityContext();
        context.put("packageName", getNameApp());
        context.put("className", table.getName().getText());

        ArrayList<Attr> list = table.getAttributes();

        context.put("list", list);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        return writer.toString();
    }

    public void generateAll() {

        Runtime rt = Runtime.getRuntime();
        //String[] cmd = {"cmd", "-c", "cd " + getNameApp() + ";" + GRAILS_PATH + " generate-all '*'; " + GRAILS_PATH + " run-app"};
        String[] cmd = {"/bin/sh", "-c", "cd " + getNameApp() + ";" + GRAILS_PATH + " generate-all '*'; " + GRAILS_PATH + " run-app"};
        Process p = null;
        BufferedReader in = null;
        try {
            p = rt.exec(cmd);

            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }


            // while (p.isAlive());
            //rt.exec("open " + SITE_APP);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void startApp(){
//        Runtime rt = Runtime.getRuntime();
//        String[] cmd = {"/bin/sh", "-c", "cd " + getNameApp() + ";" + GRAILS_PATH + " generate-all '*'" };
//        Process p = null;
//        try {
//            p = rt.exec(cmd);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        while (p.isAlive()) ;
//    }

    public void replaceFile(String fileName, String content) {

        try {
            System.out.println("PATH=" + PATH + "/" + getNameApp() + INNER_PATH + getNameApp() + "/" + fileName);
            System.err.println("///");
            Files.write(Paths.get(PATH + "/" + getNameApp() + INNER_PATH + getNameApp() + "/" + fileName + ".groovy"), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean fileExist(String fileName) {
        return new File(PATH + "/" + fileName).exists();
    }

    private String getNameApp() {
        return APP + appCount;
    }
}
