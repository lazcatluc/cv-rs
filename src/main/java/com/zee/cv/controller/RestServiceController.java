package com.zee.cv.controller;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zee.cv.model.CallGraph;
import com.zee.cv.model.GraphCallReply;
import com.zee.cv.model.Reply;
import com.zee.cv.model.ReplyPackageAssociation;
import com.zee.cv.services.GithubApiService;
import com.zee.cv.services.QDoxApiService2;
import com.zee.cv.services.UnderstandApiService;
import com.zee.cv.utils.FileUtils;
import com.zee.cv.utils.URLUtils;

@Component
@RestController
public class RestServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceController.class);

    @Autowired
    private GithubApiService githubService;
    @Autowired
    private QDoxApiService2 qdoxService;

    @RequestMapping("/")
    @ResponseBody
    public String root() {

        return "Ok!";
    }

    @RequestMapping("/hierarchy")
    @ResponseBody
    public Reply getClassesInfoHierarchy(@RequestParam("url") String repoURL) {
        Reply reply = null;

        try {
            githubService.init(repoURL);

            if (URLUtils.isValidGithubRepoURL(repoURL) && githubService.isApiSupportedProjectType()) {
                String projectLanguage = "java";// githubService.getProjectLanguage();
                String cloneRepoPath = githubService.cloneRepo();
                reply = qdoxService.getClassesInfoHierarchy(cloneRepoPath);
            }
        } catch (Exception e) {
            LOGGER.error("Exception in finding graph hierarchy: " + e.getMessage(), e);
            // if (reply == null)
            // reply = new GraphHierarchyReply();
            // reply.setError(true);
            // reply.setResponseMsg(e.getMessage());
        }
        return reply;
    }
    
    @RequestMapping("/aggregation")
    @ResponseBody
    public Reply getClassesInfoAggregation(@RequestParam("url") String repoURL) {
        Reply reply = null;

        try {
            githubService.init(repoURL);

            if (URLUtils.isValidGithubRepoURL(repoURL) && githubService.isApiSupportedProjectType()) {
                String projectLanguage = "java";// githubService.getProjectLanguage();
                String cloneRepoPath = githubService.cloneRepo();
                reply = qdoxService.getClassesInfoAggregation(cloneRepoPath);
            }
        } catch (Exception e) {
            LOGGER.error("Exception in finding graph hierarchy: " + e.getMessage(), e);
            // if (reply == null)
            // reply = new GraphHierarchyReply();
            // reply.setError(true);
            // reply.setResponseMsg(e.getMessage());
        }
        return reply;
    }

    @RequestMapping("/packagedata")
    @ResponseBody
    public ReplyPackageAssociation getPackageAssociations(@RequestParam("url") String repoURL) {
        ReplyPackageAssociation reply = null;

        try {
            githubService.init(repoURL);

            if (URLUtils.isValidGithubRepoURL(repoURL) && githubService.isApiSupportedProjectType()) {
                String projectLanguage = "java";// githubService.getProjectLanguage();
                String cloneRepoPath = githubService.cloneRepo();
                reply = qdoxService.getPackageAssociations(cloneRepoPath);
            }
        } catch (Exception e) {
            LOGGER.error("Exception in finding graph hierarchy: " + e.getMessage(), e);
            // if (reply == null)
            // reply = new GraphHierarchyReply();
            // reply.setError(true);
            // reply.setResponseMsg(e.getMessage());
        }
        return reply;
    }

    @RequestMapping("/graph/call")
    @ResponseBody
    public List<CallGraph> graphCall(@RequestParam("url") String repoURL) {
        List<CallGraph> reply = null;
        try {
            githubService.init(repoURL);
            if (URLUtils.isValidGithubRepoURL(repoURL) && githubService.isApiSupportedProjectType()
                    && UnderstandApiService.isUndLicenseWorking()) {

                String projectLanguage = githubService.getProjectLanguage();
                String cloneRepoPath = githubService.cloneRepo();
                List<File> sourceFileListing = FileUtils.getSourceFileListing(cloneRepoPath, "java");
                PrintWriter writer = new PrintWriter(cloneRepoPath + ".txt", "UTF-8");
                for (File file : sourceFileListing) {
                    writer.println(file.toString());
                }
                writer.close();
                UnderstandApiService und = new UnderstandApiService();
                Thread.sleep(600);
                String undPath = und.createAddAnalyzeUDB0(cloneRepoPath, projectLanguage);
                Thread.sleep(600);
                undPath = und.createAddAnalyzeUDB1(cloneRepoPath, projectLanguage);
                Thread.sleep(600);
                undPath = und.createAddAnalyzeUDB2(cloneRepoPath, projectLanguage);
                Thread.sleep(600);
                //reply = und.getCallGraph(Paths.get("repos/github.com/unitsofmeasurement/unit-api.udb").toString());
                reply = und.getCallGraph(undPath);
                Thread.sleep(600);
            }
        } catch (Exception e) {
            LOGGER.error("Exception in finding graph call: " + e.getMessage(), e);
            if (reply == null)
                reply = new ArrayList<>();
        }
        return reply;
    }
}