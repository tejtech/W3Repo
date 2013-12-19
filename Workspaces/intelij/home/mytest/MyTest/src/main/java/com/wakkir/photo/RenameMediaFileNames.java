package com.wakkir.photo;

import com.drew.imaging.ImageProcessingException;

import java.io.IOException;

/**
 * User: wakkir
 * Date: 27/11/13
 * Time: 20:47
 */
public class RenameMediaFileNames
{
    AccessFileMetaData fmd = new AccessFileMetaData();

    public void renameVideoFiles(boolean needRename)  throws ImageProcessingException, IOException
    {
        String inputPath = "E:\\MyMedia\\MyVideos\\Test_20130302";
        String extention = ".mts";
        String outputPath = "E:\\MyMedia\\MyVideos\\Test_20130302";

        fmd.readDirectory(inputPath, extention, outputPath,needRename);
    }

    public void renamePictureFiles(boolean needRename) throws ImageProcessingException, IOException
    {
        String inputPath = "E:\\MyMedia\\MyPictures\\xxx\\Waseem_Birthday-01";
        String extention = ".jpg";
        String outputPath = "E:\\MyMedia\\MyPictures\\xxx";

        fmd.readDirectory(inputPath, extention, outputPath,needRename);
    }

    public static void main(String[] args)
    {

        try
        {
            RenameMediaFileNames rename=new RenameMediaFileNames();
            boolean needRename=true;
            rename.renamePictureFiles(needRename);
            //rename.renameVideoFiles(needRename);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
