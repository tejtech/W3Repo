package com.wakkir.mytest;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
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

    public static boolean renameFile(String sourceFilePath, String oldFileName, String newFileName)
    {
        return renameFile(sourceFilePath, oldFileName, newFileName, sourceFilePath);

    }

    public static void main(String[] args)
    {
        String extention = ".JPG";
        try
        {
            File f = new File("E:\\MyMedia\\MyVideos\\Test_20130302");
            String[] names = f.list();

            // At this point Java listed all files in c:/tmp and loaded their names into an array of Strings
            for (String oldFileName : names)
            {
                //if(oldFileName.endsWith(".MTS"))
                if (oldFileName.endsWith(extention))
                {
                    //System.out.println(name);
                    Path path = Paths.get(f.getAbsolutePath(), oldFileName);

                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

                    FileTime fileTime = attrs.lastModifiedTime();


                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    cal.setTimeInMillis(fileTime.toMillis());


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String newFileName = dateFormat.format(cal.getTime()).toUpperCase();

                    System.out.println(fileTime + " - " + newFileName);

                    //System.out.println(oldFileName+": Created: " + newFileName+extention);

                    //renameFile(f.getAbsolutePath(), oldFileName, newFileName+extention);

                }
            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        /*
        FileUtils test = new FileUtils();

        BufferedReader br = null;

        try
        {

            //File fl = new File("E:\\MyMedia\\MyVideos\\converted\\00001.MTS");



            String sCurrentLine;

            br = new BufferedReader(new FileReader("E:\\MyMedia\\MyVideos\\converted\\00001.MTS"));

            while ((sCurrentLine = br.readLine()) != null)
            {
                System.out.println(sCurrentLine);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (br != null) br.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

         */
         /*
        javaxt.io.File file = new javaxt.io.File("E:\\MyMedia\\MyVideos\\converted\\00001.MTS");
        System.out.println("Created: " + file.getCreationTime());
        System.out.println("Accessed: " + file.getLastAccessTime());
        System.out.println("Modified: " + file.getLastModifiedTime());
        */


    }
}
