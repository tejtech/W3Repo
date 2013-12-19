package com.wakkir.photo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.wakkir.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;


/**
 * User: wakkir
 * Date: 22/10/13
 * Time: 00:50
 */
public class AccessFileMetaData
{

    public void readDirectory(String inputPath, String extension, String outputPath, boolean needRename) throws ImageProcessingException, IOException
    {
        File inputFilePath = new File(inputPath);
        File[] files = inputFilePath.listFiles();
        int count = 0;
        assert files != null;
        for (File file : files)
        {
            if (file.isDirectory())
            {
                System.out.println(file.getAbsolutePath());
                readDirectory(file.getPath(), extension, outputPath,needRename);
            }
            else
            {
                //System.out.println(file.getAbsolutePath());
                if (readMetaData(file, extension, outputPath,needRename))
                {
                    count++;
                }
            }
        }
        if(needRename)
        {
            System.out.println("Total Renamed : " + count);
        }
        else
        {
            System.out.println("Total Retrived : " + count);
        }
    }

    public boolean readMetaData(File inputFilePath, String extension, String newFilePath,boolean needRename) throws ImageProcessingException, IOException
    {
        boolean doneIt = false;
        try
        {
            String oldFileName = inputFilePath.getName();
            String oldFilePath = inputFilePath.getParent();

            //if(oldFileName.endsWith(".MTS"))
            if (".jpg".equalsIgnoreCase(extension) && oldFileName.toLowerCase().endsWith(extension))
            {
                Metadata metadata = ImageMetadataReader.readMetadata(inputFilePath);
                for (Directory directory : metadata.getDirectories())
                {
                    //System.out.println(directory.getClass().getName());
                    if ("com.drew.metadata.exif.ExifSubIFDDirectory".equalsIgnoreCase(directory.getClass().getName()))
                    {
                        for (Tag tag : directory.getTags())
                        {
                            if (36867 == tag.getTagType())
                            {
                                String newFileName=FileUtils.generateNewFileName(tag.getDescription());
                                System.out.println("NewFileName :" + newFileName);
                                if(needRename)
                                {
                                    doneIt = FileUtils.renameFile(oldFilePath, oldFileName, newFileName + extension, newFilePath);
                                }
                                else
                                {
                                    doneIt=true;
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            else if (".mts".equalsIgnoreCase(extension) && oldFileName.toLowerCase().endsWith(extension))
            {
                //System.out.println(name);
                Path path = Paths.get(oldFilePath, oldFileName);
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                FileTime fileTime = attrs.lastModifiedTime();
                String newFileName=FileUtils.generateNewFileName(fileTime.toMillis());
                System.out.println("NewFileName :" + newFileName);
                if(needRename)
                {
                    doneIt = FileUtils.renameFile(oldFilePath, oldFileName, newFileName + extension, newFilePath);
                }
                else
                {
                    doneIt=true;
                }
            }
            return doneIt;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return doneIt;
        }

    }



}