package com.wakkir.mytest;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;


/**
 * User: wakkir
 * Date: 22/10/13
 * Time: 00:50
 */
public class MetaData
{

    public static void main(String[] args) throws ImageProcessingException, IOException
    {

        String extention = ".jpg";
        int count = 0;
        try
        {
            File f = new File("\\\\Fs01\\mymedia\\MyPhones\\xNokia\\pic");
            String[] names = f.list();

            // At this point Java listed all files in c:/tmp and loaded their names into an array of Strings
            for (String oldFileName : names)
            {
                //if(oldFileName.endsWith(".MTS"))
                if (oldFileName.toLowerCase().endsWith(extention))
                {
                    Metadata metadata = ImageMetadataReader.readMetadata(new File(f.getPath() + "\\" + oldFileName));

                    Iterator<Directory> directories = metadata.getDirectories().iterator();

                    while (directories.hasNext())
                    {
                        Directory directory = directories.next();
                        //System.out.println(directory.getClass().getName());
                        if ("com.drew.metadata.exif.ExifSubIFDDirectory".equalsIgnoreCase(directory.getClass().getName()))
                        {
                            Iterator<Tag> tags = directory.getTags().iterator();

                            while (tags.hasNext())
                            {
                                Tag tag = tags.next();
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

                                    FileUtils.renameFile(f.getAbsolutePath(), oldFileName, newFileName + extention);
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally
        {
            System.out.println("Total Renamed : " + count);
        }
    }

    void readAndDisplayMetadata(String fileName)
    {
        try
        {

            File file = new File(fileName);
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

            if (readers.hasNext())
            {

                // pick the first available ImageReader
                ImageReader reader = readers.next();

                // attach source to the reader
                reader.setInput(iis, true);

                // read metadata of first image
                IIOMetadata metadata = reader.getImageMetadata(0);

                String[] names = metadata.getMetadataFormatNames();
                int length = names.length;
                for (int i = 0; i < length; i++)
                {
                    System.out.println("Format name: " + names[i]);
                    displayMetadata(metadata.getAsTree(names[i]));
                }
            }
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }

    void displayMetadata(Node root)
    {
        displayMetadata(root, 0);
    }

    void indent(int level)
    {
        for (int i = 0; i < level; i++)
        {
            System.out.print("    ");
        }
    }

    void displayMetadata(Node node, int level)
    {
        // print open tag of element
        indent(level);
        System.out.print("<" + node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null)
        {

            // print attribute values
            int length = map.getLength();
            for (int i = 0; i < length; i++)
            {
                Node attr = map.item(i);
                System.out.print(" " + attr.getNodeName() +
                        "=\"" + attr.getNodeValue() + "\"");
            }
        }

        Node child = node.getFirstChild();
        if (child == null)
        {
            // no children, so close element and return
            System.out.println("/>");
            return;
        }

        // children, so close current tag
        System.out.println(">");
        while (child != null)
        {
            // print children recursively
            displayMetadata(child, level + 1);
            child = child.getNextSibling();
        }

        // print close tag of element
        indent(level);
        System.out.println("</" + node.getNodeName() + ">");
    }
}
