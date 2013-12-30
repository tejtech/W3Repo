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

    public static boolean renameFile(String sourceFilePath, String oldFileName, String newFileName, String destinationFilePath,int primaryFileNameStartIndex,int primaryFileNameEndIndex)
    {
        boolean isFileRenamed = false;

        File newFile = new File(destinationFilePath, newFileName.trim());
        File oldFile = new File(sourceFilePath, oldFileName.trim());

        if (oldFile.exists())
        {
            // if file doesnt exists, then create it
            createFolder(destinationFilePath);
            int count=0;
            while (newFile.exists())
            {
                count++;
                String newExtFileName=validateFileName(String.valueOf(count),newFile,primaryFileNameStartIndex,primaryFileNameEndIndex);
                newFile = new File(destinationFilePath, newExtFileName);
            }

            if (oldFile.renameTo(newFile))
            {
                //logger.info("File renamed : from " + oldFile.getAbsoluteFile() + " to " + newFile.getAbsoluteFile());
                isFileRenamed = true;
            }
            else
            {
                //logger.warn("File rename failed : from " + oldFile.getAbsoluteFile() + " to " + newFile.getAbsoluteFile());
            }
        }
        else
        {
            //logger.warn("File " + oldFile.getAbsoluteFile() + " doesn't exist ");
        }
        return isFileRenamed;

    }

    private static String validateFileName(String newExt, File newFile,int primaryFileNameStartIndex,int primaryFileNameEndIndex)
    {

        int indexOfDot = newFile.getName().indexOf(".");
        String primaryFileName = newFile.getName().substring(primaryFileNameStartIndex, primaryFileNameEndIndex);
        String fileExtension = newFile.getName().substring(indexOfDot + 1);
        String newExtFileName = primaryFileName + "-" + newExt + "." + fileExtension;
        return  newExtFileName;
    }

    public static boolean renameFile(String sourceFilePath, String oldFileName, String newFileName,int primaryFileNameStartIndex,int primaryFileNameEndIndex)
    {
        return renameFile(sourceFilePath, oldFileName, newFileName, sourceFilePath,primaryFileNameStartIndex,primaryFileNameEndIndex);

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

    public static String generateNewFileNameWithOffsetDateTime(String oldFileName,int years,int months,int days,int hours,int minutes)
    {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String temp = String.valueOf(oldFileName).replace(" ", ":");
        String[] str = temp.split(":");
        cal.set(Integer.parseInt(str[0])+years, Integer.parseInt(str[1]) - 1+months, Integer.parseInt(str[2])+days, Integer.parseInt(str[3])+hours, Integer.parseInt(str[4])+minutes, Integer.parseInt(str[5]));
        return formatNewFileName(cal);
    }

    private static String formatNewFileName(Calendar cal)
    {
        String newTempFileName = dateFormat_yyyyMMddHHmmss.format(cal.getTime()).toUpperCase();
        String newFileName = newTempFileName.subSequence(0, 8) + "_" + newTempFileName.subSequence(8, 14);

        return newFileName;
    }


}
