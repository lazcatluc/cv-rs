package com.zee.cv.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    
    public static List<File> getSourceFileListing(String softwareAddress, String fileExtension) throws FileNotFoundException {
            File aStartingDir = new File(softwareAddress);
            List<File> result = new ArrayList<>();
            File[] filesAndDirs = aStartingDir.listFiles();
            List<File> filesDirs = Arrays.asList(filesAndDirs);
            for (File file : filesDirs) {
    //            result.add(file); //always add, even if directory
                if (!file.isFile()) {
                    //must be a directory
                    //recursive call!
                    List<File> deeperList = getSourceFileListing(file.getPath(), fileExtension);
                    result.addAll(deeperList);
                } else {
                    boolean isJavaFile = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).equals(fileExtension);
                    boolean isInSrcFolder = file.getPath().contains(File.separator+"src"+File.separator);
                    if (isJavaFile && isInSrcFolder) {
                        result.add(file);
                    }
                }
            }
            return result;
        }
}

