/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.invisiblefriend.controllers;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.sun.jersey.core.header.FormDataContentDisposition;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author David Andrés Manzano Herrera <damanzano>
 */
public class CloudStoragePlayerPhotoController {

    /**
     * This is where backoff parameters are configured. Here it is aggressively
     * retrying with backoff, up to 10 times but taking no more that 15 seconds
     * total to do so.
     */
    private final GcsService gcsService
            = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
    
    /**
     * This the app identify service
     */
    AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
    
    /**
     * The image services
     */
    ImagesService imagesService = ImagesServiceFactory.getImagesService();

    /**
     * Used below to determine the size of chucks to read in. Should be > 1kb
     * and < 10MB
     */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    public void saveFile(String imageTitle, InputStream uploadedInputStream, FormDataContentDisposition fileDetail) throws IOException, ClassNotFoundException {

        /**
         * Write and read back a map
         */
        GcsFilename filename = new GcsFilename(appIdentityService.getDefaultGcsBucketName(), fileDetail.getFileName());
        Map<String, String> mapContent = new HashMap<>();
        mapContent.put("foo", "bar");

        writeObjectToFile(filename, mapContent);

        System.out.println("Wrote " + mapContent + " read: " + readObjectFromFile(filename));

        /**
         * Write and read back a byteArray
         */
        byte[] byteContent = new byte[]{1, 2, 3, 4, 5};

        writeToFile(filename, byteContent);

        System.out.println("Wrote " + Arrays.toString(byteContent) + " read: "
                + Arrays.toString(readFromFile(filename)));

    }

    /**
     * Writes the provided object to the specified file using Java
     * serialization. One could use this same technique to write many objects,
     * or with another format such as Json or XML or just a DataOutputStream.
     *
     * Notice at the end closing the ObjectOutputStream is not done in a finally
     * block. See below for why.
     */
    private void writeObjectToFile(GcsFilename fileName, Object content) throws IOException {
        GcsOutputChannel outputChannel
                = gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
        @SuppressWarnings("resource")
        ObjectOutputStream oout
                = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
        oout.writeObject(content);
        oout.close();
    }

    /**
     * Writes the provided object to the specified file using Java
     * serialization. One could use this same technique to write many objects,
     * or with another format such as Json or XML or just a DataOutputStream.
     *
     * Notice at the end closing the ObjectOutputStream is not done in a finally
     * block. See below for why.
     */
    private void writeImageToFile(GcsFilename fileName, InputStream content) throws IOException {
        GcsOutputChannel outputChannel
                = gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
        copyStreams(content, Channels.newOutputStream(outputChannel));
     }

    /**
     * Writes the byte array to the specified file. Note that the close at the
     * end is not in a finally.This is intentional. Because the file only exists
     * for reading if close is called, if there is an exception thrown while
     * writing the file won't ever exist. (This way there is no need to worry
     * about cleaning up partly written files)
     */
    private void writeToFile(GcsFilename fileName, byte[] content) throws IOException {
        @SuppressWarnings("resource")
        GcsOutputChannel outputChannel
                = gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
        outputChannel.write(ByteBuffer.wrap(content));
        outputChannel.close();
    }

    /**
     * Reads an object from the specified file using Java serialization. One
     * could use this same technique to read many objects, or with another
     * format such as Json or XML or just a DataInputStream.
     *
     * The final parameter to openPrefetchingReadChannel is a buffer size. It
     * will attempt to buffer the input by at least this many bytes. (This must
     * be at least 1kb and less than 10mb) If buffering is undesirable
     * openReadChannel could be called instead, which is totally unbuffered.
     */
    private Object readObjectFromFile(GcsFilename fileName)
            throws IOException, ClassNotFoundException {
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, 1024 * 1024);
        try (ObjectInputStream oin = new ObjectInputStream(Channels.newInputStream(readChannel))) {
            return oin.readObject();
        }
    }

    /**
     * Reads the contents of an entire file and returns it as a byte array. This
     * works by first requesting the length, and then fetching the whole file in
     * a single call. (Because it calls openReadChannel instead of
     * openPrefetchingReadChannel there is no buffering, and thus there is no
     * need to wrap the read call in a loop)
     *
     * This is really only a good idea for small files. Large files should be
     * streamed out using the prefetchingReadChannel and processed
     * incrementally.
     */
    private byte[] readFromFile(GcsFilename fileName) throws IOException {
        int fileSize = (int) gcsService.getMetadata(fileName).getLength();
        ByteBuffer result = ByteBuffer.allocate(fileSize);
        try (GcsInputChannel readChannel = gcsService.openReadChannel(fileName, 0)) {
            readChannel.read(result);
        }
        return result.array();
    }

    /**
     * Transfer the data from the inputStream to the outputStream. Then close
     * both streams.
     */
    private void copyStreams(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }

}
