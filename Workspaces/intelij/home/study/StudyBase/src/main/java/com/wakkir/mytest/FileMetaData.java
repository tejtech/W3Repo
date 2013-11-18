package com.wakkir.mytest;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * User: wakkir
 * Date: 22/10/13
 * Time: 00:50
 */
public class FileMetaData
{
    public void readDirectory(String inputPath, String extension, String outputPath) throws ImageProcessingException, IOException
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
                readDirectory(file.getPath(), extension, outputPath);
            }
            else
            {
                //System.out.println(file.getAbsolutePath());
                if (readMetaData(file, extension, outputPath))
                {
                    count++;
                }
            }
        }
        System.out.println("Total Renamed : " + count);
    }

    public boolean readMetaData(File inputFilePath, String extension, String newFilePath) throws ImageProcessingException, IOException
    {
        boolean doneIt = false;
        try
        {
            String oldFileName = inputFilePath.getName();
            String oldFilePath = inputFilePath.getParent();

            //if(oldFileName.endsWith(".MTS"))
            if (oldFileName.toLowerCase().endsWith(extension))
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
                                String temp = tag.getDescription().replace(" ", ":");
                                //System.out.println(tag.getTagName() + " > " + temp);
                                String[] str = temp.split(":");
                                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                                cal.set(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1, Integer.parseInt(str[2]), Integer.parseInt(str[3]), Integer.parseInt(str[4]), Integer.parseInt(str[5]));

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                String newTempFileName = dateFormat.format(cal.getTime()).toUpperCase();
                                String newFileName = newTempFileName.subSequence(0, 8) + "_" + newTempFileName.subSequence(8, 14);

                                System.out.println("NewFileName :" + newFileName);

                                doneIt = FileUtils.renameFile(oldFilePath, oldFileName, newFileName + extension, newFilePath);

                                break;
                            }
                        }
                        break;
                    }
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


    public void readMetaDatax(File inputFilePath, String extention) throws ImageProcessingException, IOException
    {

        int count = 0;
        try
        {
            String[] names = inputFilePath.list();
            // At this point Java listed all files in c:/tmp and loaded their names into an array of Strings
            for (String oldFileName : names)
            {
                //if(oldFileName.endsWith(".MTS"))
                if (oldFileName.toLowerCase().endsWith(extention))
                {
                    Metadata metadata = ImageMetadataReader.readMetadata(new File(inputFilePath.getPath() + "\\" + oldFileName));

                    for (Directory directory : metadata.getDirectories())
                    {
                        //System.out.println(directory.getClass().getName());
                        if ("com.drew.metadata.exif.ExifSubIFDDirectory".equalsIgnoreCase(directory.getClass().getName()))
                        {

                            for (Tag tag : directory.getTags())
                            {
                                if (36867 == tag.getTagType())
                                {
                                    String temp = tag.getDescription().replace(" ", ":");
                                    //System.out.println(tag.getTagName() + " > " + temp);
                                    String[] str = temp.split(":");
                                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                                    cal.set(Integer.parseInt(str[0]), Integer.parseInt(str[1]) - 1, Integer.parseInt(str[2]), Integer.parseInt(str[3]), Integer.parseInt(str[4]), Integer.parseInt(str[5]));

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String newTempFileName = dateFormat.format(cal.getTime()).toUpperCase();
                                    String newFileName = newTempFileName.subSequence(0, 8) + "_" + newTempFileName.subSequence(8, 14);

                                    System.out.println("NewFileName :" + newFileName);

                                    //FileUtils.renameFile(inputFilePath.getAbsolutePath(), oldFileName, newFileName+extention);
                                    count++;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Total Renamed : " + count);
        }
    }

    public static void main(String[] args)
    {

        FileMetaData fmd = new FileMetaData();
        try
        {
            String inputPath = "\\\\Fs01\\Wakkir\\Documents\\01.Personal\\04.Naadira\\x";

            String extention = ".jpg";

            String outputPath = "\\\\Fs01\\Wakkir\\Documents\\01.Personal\\04.Naadira\\x";

            fmd.readDirectory(inputPath, extention, outputPath);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}