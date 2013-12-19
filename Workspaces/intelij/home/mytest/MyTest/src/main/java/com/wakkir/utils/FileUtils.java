package com.wakkir.utils;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * User: wakkir
 * Date: 03/03/13
 * Time: 15:45
 */
public class FileUtils
{
    private final static SimpleDateFormat dateFormat_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    public static boolean createFolder(String filePath)
    {
        boolean isFolderCreated = false;
        File theDir = new File(filePath);
        // if the directories do not exist, create them
        if (!theDir.exists())
        {
            if (theDir.mkdirs())
            {
                //logger.debug("DIR created : " + filePath);
                isFolderCreated = true;
            }
        }
        return isFolderCreated;
    }

    public static boolean renameFile(String sourceFilePath, String oldFileName, String newFileName, String destinationFilePath)
    {
        boolean isFileRenamed = false;

        File newfile = new File(destinationFilePath, newFileName.trim());
        File oldfile = new File(sourceFilePath, oldFileName.trim());

        if (oldfile.exists())
        {
            // if file doesnt exists, then create it
            createFolder(destinationFilePath);
            int count=0;
            while (newfile.exists())
            {
                count++;
                String newExtFileName=validateFileName(String.valueOf(count),newfile);
                newfile = new File(destinationFilePath, newExtFileName);
            }

            if (oldfile.renameTo(newfile))
            {
                //logger.info("File renamed : from " + oldfile.getAbsoluteFile() + " to " + newfile.getAbsoluteFile());
                isFileRenamed = true;
            }
            else
            {
                //logger.warn("File rename failed : from " + oldfile.getAbsoluteFile() + " to " + newfile.getAbsoluteFile());
            }
        }
        else
        {
            //logger.warn("File " + oldfile.getAbsoluteFile() + " doesn't exist ");
        }
        return isFileRenamed;

    }

    private static String validateFileName(String newExt, File newfile)
    {

        int indexOfDot = newfile.getName().indexOf(".");
        String primaryFileName = newfile.getName().substring(0, 15);
        String fileExtention = newfile.getName().substring(indexOfDot + 1);
        String newExtFileName = primaryFileName + "-" + newExt + "." + fileExtention;
        return  newExtFileName;
    }

    public static boolean renameFile(String sourceFilePath, String oldFileName, String newFileName)
    {
        return renameFile(sourceFilePath, oldFileName, newFileName, sourceFilePath);

    }

    public static String generateNewFileName(long oldFileName)
    {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(oldFileName);
        return formatNewFileName(cal);
    }

    public static String generateNewFileName(String oldFileName)
    {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String temp = String.valueOf(oldFileName).replace(" ", ":");
        String[] str = temp.split(":");
        cal.set(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1, Integer.parseInt(str[2]), Integer.parseInt(str[3]), Integer.parseInt(str[4]), Integer.parseInt(str[5]));
        return formatNewFileName(cal);
    }

    private static String formatNewFileName(Calendar cal)
    {
        String newTempFileName = dateFormat_yyyyMMddHHmmss.format(cal.getTime()).toUpperCase();
        String newFileName = newTempFileName.subSequence(0, 8) + "_" + newTempFileName.subSequence(8, 14);

        return newFileName;
    }


}
