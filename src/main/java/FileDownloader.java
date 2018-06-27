import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuel Gonzalez - 6/26/18
 * @project BulkFileDownloader
 * @version 0.1
 */

public class FileDownloader {

    public static final String
            CONTENT_DISPOSITION = "Content-Disposition",
            CONTENT_DISPOSITION_FILENAME = "filename",
            CONTENT_DISPOSITION_ATTACHMENT = "attachment",
            CONTENT_DISPOSITION_FILENAME_SEPARATOR = "=",
            PDF_FILE_EXTENSION = "pdf";

    private List<String> completedDownloads;
    private List<String> failedDownloads;

    private String targetDirectory;

    private int connectTimeoutInSeconds, readTimeoutInSeconds;

    public FileDownloader(String targetDirectory) {
        this.completedDownloads = new ArrayList<>();
        this.failedDownloads = new ArrayList<>();

        this.targetDirectory = targetDirectory;
    }

    public int getConnectTimeoutInSeconds() {
        return connectTimeoutInSeconds;
    }

    public void setConnectTimeoutInSeconds(int connectTimeoutInSeconds) {
        this.connectTimeoutInSeconds = connectTimeoutInSeconds;
    }

    public int getReadTimeoutInSeconds() {
        return readTimeoutInSeconds;
    }

    public void setReadTimeoutInSeconds(int readTimeoutInSeconds) {
        this.readTimeoutInSeconds = readTimeoutInSeconds;
    }

    public int download(URL url) throws IOException {

        URLConnection conn = url.openConnection();

        this.changeConnectTimeoutInConnection(conn);
        this.changeReadTimeoutInConnection(conn);

        String contentDisposition = conn.getHeaderField(CONTENT_DISPOSITION);

        if(!isLinkAFile(contentDisposition)){
            Console.writeLine(String.format("Couldn't find a file at %s", url.toString()));
            return 2; // File not found error code
        }

        String filename = this.getFilenameFromContentDisposition(contentDisposition);

        long fileSize = conn.getContentLengthLong();
        long bytesRead = 0;

        Path file = this.joinTargetDirectoryAndFilenameString(filename);

        int ret = 0;
        boolean somethingRead = false;

        Console.writeLine(String.format("Downloading %s, %s", url.toString(), filename));

        try (InputStream is = conn.getInputStream()) {
            try (BufferedInputStream in = new BufferedInputStream(is); OutputStream fout = Files.newOutputStream(file)) {
                final byte data[] = new byte[ApplicationProperties.getInstance().getFileDownloaderBytesChunkSize()];
                int count;
                while ((count = in.read(data)) > 0) {
                    somethingRead = true;
                    fout.write(data, 0, count);

                    bytesRead += count;
                }
            }
        } catch (java.io.IOException e) {
            int httpCode = 999;

            try {

                httpCode = ((HttpURLConnection) conn).getResponseCode();

            } catch (Exception ee) {

                Console.writeError(ApplicationProperties.getInstance().getExceptionMessageHeaderTemplate(), "FileDownloader.java.", ee.getMessage());

            }

            if (somethingRead && e instanceof java.net.SocketTimeoutException) {
                ret = 1;
            } else if (e instanceof FileNotFoundException && httpCode >= 400 && httpCode < 500) {
                ret = 2;
            } else if (httpCode >= 400 && httpCode < 600) {
                ret = 3;
            } else if (e instanceof java.net.SocketTimeoutException) {
                ret = 4;
            } else if (e instanceof java.net.ConnectException) {
                ret = 5;
            } else if (e instanceof java.net.UnknownHostException) {
                ret = 6;
            } else {
                throw e;
            }
        }

        return ret;
    }

    private boolean isLinkAFile(String contentDisposition) {
        return contentDisposition.contains(CONTENT_DISPOSITION_ATTACHMENT);
    }

    private String getFilenameFromContentDisposition(String contentDisposition) {

        // Set default filename
        String filename = String.format("%d.%s", System.currentTimeMillis(), PDF_FILE_EXTENSION);

        if (contentDisposition != null) {
            if (contentDisposition.contains(CONTENT_DISPOSITION_FILENAME)) {
                int index = contentDisposition.indexOf(CONTENT_DISPOSITION_FILENAME);
                filename = contentDisposition
                        .substring(index + 1)
                        .split(CONTENT_DISPOSITION_FILENAME_SEPARATOR)[1]
                        .replace("\"", "");
            }
        }

        return filename;
    }

    private Path joinTargetDirectoryAndFilenameString(String filename) {
        return Paths.get(targetDirectory, filename);
    }

    private void changeReadTimeoutInConnection(URLConnection conn) {
        if (this.readTimeoutInSeconds > 0) {
            conn.setReadTimeout(this.readTimeoutInSeconds * 1000);
        }
    }

    private void changeConnectTimeoutInConnection(URLConnection conn) {
        if (this.connectTimeoutInSeconds > 0) {
            conn.setConnectTimeout(connectTimeoutInSeconds * 1000);
        }
    }

}
