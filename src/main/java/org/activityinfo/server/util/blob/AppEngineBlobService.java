package org.activityinfo.server.util.blob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;

public class AppEngineBlobService implements BlobService {

    private DatastoreService datastore;
    private FileService fileService;

    public AppEngineBlobService() {
        datastore = DatastoreServiceFactory.getDatastoreService();
        fileService = FileServiceFactory.getFileService();
    }

    @Override
    public void put(String key, InputSupplier<? extends InputStream> blob) throws IOException {
        AppEngineFile file = fileService.createNewBlobFile("application/octet-stream");
        boolean lock = true;
        FileWriteChannel writeChannel = fileService.openWriteChannel(
            file, lock);
        OutputStream os = Channels.newOutputStream(writeChannel);
        ByteStreams.copy(blob, os);
        os.flush();
        writeChannel.closeFinally();
        
        Entity entity = new Entity("Blob", key);
        entity.setUnindexedProperty("blobKey", fileService.getBlobKey(file));
        datastore.put(entity);
    }

    @Override
    public InputSupplier<InputStream> get(String key) throws BlobNotFoundException {
        Entity entity;
        try {
            entity = datastore.get(KeyFactory.createKey("Blob", key));
        } catch (EntityNotFoundException e) {
            throw new BlobNotFoundException();
        }
        
        final BlobKey blobKey = (BlobKey) entity.getProperty("blobKey");

        return new InputSupplier<InputStream>() {
            @Override
            public InputStream getInput() throws IOException {
                return new BlobstoreInputStream(blobKey);
            }
        };
    }
}