package com.zee.cv.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;
import com.zee.cv.exception.UnderstandApiException;
import com.zee.cv.model.CallGraph;
import com.zee.cv.model.GraphCallReply;
import com.zee.cv.model.GraphCallResponse;

public class UnderstandApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnderstandApiService.class);

    private static final String appPath = System.getProperty("user.dir") + File.separator;

    public List<CallGraph> getCallGraph(final String undPath) throws MalformedURLException, UnderstandException {
        List<CallGraph> reply = new ArrayList<CallGraph>();

        // open the understand database
        System.out.println("opening");
        
        Database db = Understand.open(undPath);

        System.out.println("opened");
        // get a list of all functions and methods
        Entity[] funcs = db.ents("method");
        for (Entity en : funcs) {
            
            Reference[] parameterRefs = en.refs("Call", null, true);
            if (parameterRefs.length > 0) {
                for (Reference param : parameterRefs) {
                    Entity pEnd = param.ent();

                    CallGraph cg = new CallGraph(en.name(), en.name(), pEnd.name());
                    reply.add(cg);
                }
            } else {
                String longname = en.longname(true);
//                if (!longname.startsWith("java.") && !longname.startsWith("sun.")
//                        && !longname.startsWith("javax.security") && !longname.startsWith("javax.swing")
//                        && !longname.startsWith("org.w3c") && !longname.startsWith("javax.print")
//                        && !longname.startsWith("javax.imageio") && !longname.startsWith("javax.accessibility")
//                        && !longname.startsWith("com.sun.beans")) {
                if(en.library()==null || en.library().isEmpty()) {
                    System.out.println(en.library() + " " + longname);
                    CallGraph cg = new CallGraph(en.name(), en.name(), "");
                    reply.add(cg);
                }
            }
        }
        db.close();
        System.out.println("closed");
        return reply;
    }

    public String createAddAnalyzeUDB0(final String repoPath, String projectLanguage) throws MalformedURLException {

        String dataPath = Paths.get(repoPath).toString();
        File dataFile = new File(dataPath + ".udb");

        String singleLineCommand[] = { "und", "create", "-languages", projectLanguage.toLowerCase(),
                dataPath + ".udb" };
        ProcessBuilder pb = new ProcessBuilder(singleLineCommand);
        System.out.println(pb.command());
        pb.redirectErrorStream(true); // equivalent of 2>&1
        try {
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
            in.close();
        } catch (IOException e) {
            LOGGER.error("IOException in createAddAnalyzeUDB. " + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException in createAddAnalyzeUDB. " + e.getMessage(), e);
        }
        LOGGER.debug(
                "createAddAnalyzeUDB completed successfully of Repo: " + repoPath + " at path: " + dataPath + ".udb");
        return dataPath + ".udb";
    }

    public String createAddAnalyzeUDB1(final String repoPath, String projectLanguage) throws MalformedURLException {

        String dataPath = Paths.get(repoPath).toString();
        File dataFile = new File(dataPath + ".udb");

        String singleLineCommand[] = { "und", "-db", dataPath + ".udb", "add", "@" + dataPath + ".txt" };
        ProcessBuilder pb = new ProcessBuilder(singleLineCommand);
        System.out.println(pb.command());
        pb.redirectErrorStream(true); // equivalent of 2>&1
        try {
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
            in.close();
        } catch (IOException e) {
            LOGGER.error("IOException in createAddAnalyzeUDB. " + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException in createAddAnalyzeUDB. " + e.getMessage(), e);
        }
        LOGGER.debug(
                "createAddAnalyzeUDB completed successfully of Repo: " + repoPath + " at path: " + dataPath + ".udb");
        return dataPath + ".udb";
    }

    public String createAddAnalyzeUDB2(final String repoPath, String projectLanguage) throws MalformedURLException {

        String dataPath = Paths.get(repoPath).toString();
        File dataFile = new File(dataPath + ".udb");

        String singleLineCommand[] = { "und", "-db", dataPath + ".udb", "analyze", "-files", "@" + dataPath + ".txt" };
        ProcessBuilder pb = new ProcessBuilder(singleLineCommand);
        System.out.println(pb.command());
        pb.redirectErrorStream(true); // equivalent of 2>&1
        try {
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                // System.out.println(line);
            }
            p.waitFor();
            in.close();
        } catch (IOException e) {
            LOGGER.error("IOException in createAddAnalyzeUDB. " + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException in createAddAnalyzeUDB. " + e.getMessage(), e);
        }
        LOGGER.debug(
                "createAddAnalyzeUDB completed successfully of Repo: " + repoPath + " at path: " + dataPath + ".udb");
        return dataPath + ".udb";
    }

    public static boolean isUndLicenseWorking() throws UnderstandApiException {
        ProcessBuilder pb = new ProcessBuilder("und", "version");
        pb.redirectErrorStream(true); // equivalent of 2>&1
        try {
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String lines = "";
            String line;
            while ((line = in.readLine()) != null) {
                lines += line;
            }
            if (lines.contains("No valid Understand license found"))
                throw new UnderstandApiException("No valid Understand license found.");
            p.waitFor();
            in.close();
        } catch (IOException e) {
            LOGGER.error("IOException in isUndLicenseWorking: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException in isUndLicenseWorking: " + e.getMessage(), e);
        }
        return true;
    }

    public static String getUndVersion() throws UnderstandApiException {
        String lines = "";
        ProcessBuilder pb = new ProcessBuilder("und", "version");
        pb.redirectErrorStream(true); // equivalent of 2>&1
        try {
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                lines += line;
            }

            p.waitFor();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
