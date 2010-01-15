package org.activityinfo.server.report;

import org.activityinfo.server.auth.SecureTokenGenerator;
import org.activityinfo.server.report.renderer.html.ImageStorage;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;

import java.io.FileOutputStream;
import java.io.IOException;

public class ServletImageStorageProvider implements ImageStorageProvider {

    private String urlBase;
    private String tempPath;

    public ServletImageStorageProvider(String urlBase, String tempPath) {
        this.urlBase = urlBase;
        this.tempPath = tempPath + "/";
    }

    @Override
    public ImageStorage getImageUrl(String suffix) throws IOException {

        String filename = SecureTokenGenerator.generate() + suffix;

        String path = tempPath + filename;
        String url = urlBase + filename;

        FileOutputStream os = new FileOutputStream(path);

        return new ImageStorage(url, os);
    }
}
