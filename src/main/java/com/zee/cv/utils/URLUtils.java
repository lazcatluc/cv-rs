package com.zee.cv.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zee.cv.exception.GithubApiException;

public class URLUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLUtils.class);

    private static final String INVALID_GITHUB_REPOSITORY_URL = "Invalid Github repository URL.";

    public static boolean isValidGithubRepoURL(String githubRepoURL) throws GithubApiException {
        // check 1 - using apache common-validator
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(githubRepoURL))
            throw new GithubApiException(INVALID_GITHUB_REPOSITORY_URL);

        // check 2 - init a java.net URL
        URL u = null;

        try {
            u = new URL(githubRepoURL);
        } catch (MalformedURLException e) {
            throw new GithubApiException(INVALID_GITHUB_REPOSITORY_URL);
        }

        // check 3 - check github.com should exist in URL
        if (!u.getAuthority().contains("github.com"))
            throw new GithubApiException(INVALID_GITHUB_REPOSITORY_URL);

        // check 4 - only 3 slashes allowed after github.com (e.g
        // /zeeshanasghar/test)
        if (u.getFile().split("/").length != 3)
            throw new GithubApiException(INVALID_GITHUB_REPOSITORY_URL);

        LOGGER.debug(String.format("Provided URL "+githubRepoURL+" is valid github repository url."));
        return true;
    }
}
