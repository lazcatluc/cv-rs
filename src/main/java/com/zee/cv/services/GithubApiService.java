package com.zee.cv.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zee.cv.exception.GithubApiException;

@Component
public class GithubApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubApiService.class);

    private static final String[] supportedProjectTypes = { "java", "c++", "c#" };
    @Value("${data.path}")
    private String repoPath;

    private String projectLanguage;

    private URL url;

    private boolean apiSupportedProjectType;

    private String repoURL;

    public void init(String repoURL) throws GithubApiException {
        this.repoURL = repoURL;
        try {
            if(repoURL.endsWith(".git"))
                repoURL = repoURL.replace(".git", "");
            this.repoURL = repoURL;
            url = new URL(repoURL);
            String file = url.getFile();
            file = file.substring(1, file.length());

            // when
            GitHub github = GitHub.connectAnonymously();
            GHRepository ghRepository = github.getRepository(file);
            projectLanguage = ghRepository.getLanguage();

            if (Arrays.asList(supportedProjectTypes).contains(projectLanguage.toLowerCase())) {
                LOGGER.debug("API supports the project language: " + projectLanguage + " of repo: " + repoURL);
                apiSupportedProjectType = true;
            } else {
                LOGGER.debug("API doesn't supports the project language: " + projectLanguage + " of repo: " + repoURL);
                throw new GithubApiException("Api doesn't support the project language");
            }
        } catch (MalformedURLException e) {
            LOGGER.error("MalformedURLException in cloneRepo: " + e.getMessage(), e);
            throw new GithubApiException(e.getMessage());
        } catch (GithubApiException e) {
            LOGGER.error("GithubApiException in cloneRepo: " + e.getMessage(), e);
            throw new GithubApiException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error("IOException in cloneRepo: " + e.getMessage(), e);
            throw new GithubApiException(e.getMessage());
        }
    }

    public String cloneRepo() throws GithubApiException {
        String directoryDir;
        try {
            directoryDir = Paths.get(repoPath + url.getAuthority() + url.getFile()).toString();
            File f = new File(directoryDir);
            if (!f.exists()) {
                Git.cloneRepository().setURI(url.toString()).setDirectory(f).call();
                LOGGER.debug("Repo: " + repoURL + " clonned successfully at path: " + f.getAbsolutePath());
            }
        } catch (InvalidRemoteException e) {
            LOGGER.error("InvalidRemoteException in cloneRepo: " + e.getMessage(), e);
            throw new GithubApiException(e.getMessage());
        } catch (TransportException e) {
            LOGGER.error("TransportException in cloneRepo: " + e.getMessage(), e);
            throw new GithubApiException(e.getMessage());
        } catch (GitAPIException e) {
            LOGGER.error("GithubApiException in cloneRepo: " + e.getMessage(), e);
            throw new GithubApiException(e.getMessage());
        }
        return directoryDir;
    }

    public String getProjectLanguage() {
        return projectLanguage;
    }

    public void setProjectLanguage(String projectLanguage) {
        this.projectLanguage = projectLanguage;
    }

    public boolean isApiSupportedProjectType() {
        return apiSupportedProjectType;
    }

    public void setApiSupportedProjectType(boolean apiSupportedProjectType) {
        this.apiSupportedProjectType = apiSupportedProjectType;
    }
}
