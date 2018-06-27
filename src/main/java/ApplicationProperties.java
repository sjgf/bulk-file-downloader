import java.util.Locale;
import java.util.PropertyResourceBundle;

/**
 * @author Samuel Gonzalez - 6/26/18
 * @project BulkFileDownloader
 * @version 0.1
 */

public class ApplicationProperties {

    private static final ApplicationProperties _self = new ApplicationProperties();

    public static final String APPLICATION_PROPERTIES_BUNDLE = "application";

    private class ApplicationProperty {
        public static final String
            EXCEPTION_MESSAGE_HEADER_TEMPLATE = "EXCEPTION_MESSAGE_HEADER_TEMPLATE",
            FILE_DOWNLOADER_BYTES_CHUNK_SIZE = "FILE_DOWNLOADER_BYTES_CHUNK_SIZE",
            URL_PARSER_NULL_OR_EMPTY_LIST_MESSAGE = "URL_PARSER_NULL_OR_EMPTY_LIST_MESSAGE";
    }

    private PropertyResourceBundle bundle;

    private String exceptionMessageHeaderTemplate, urlParserNullOrEmptyListMessage;
    private int fileDownloaderBytesChunkSize;

    private ApplicationProperties(){
        this.bundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle(APPLICATION_PROPERTIES_BUNDLE, Locale.getDefault());

        this.load();
    }

    public static ApplicationProperties getInstance(){
        return _self;
    }

    public String getExceptionMessageHeaderTemplate() {
        return this.exceptionMessageHeaderTemplate;
    }

    public String getUrlParserNullOrEmptyListMessage() {
        return this.urlParserNullOrEmptyListMessage;
    }

    public int getFileDownloaderBytesChunkSize() {
        return this.fileDownloaderBytesChunkSize;
    }

    private void load(){

        this.exceptionMessageHeaderTemplate = bundle.getString(ApplicationProperty.EXCEPTION_MESSAGE_HEADER_TEMPLATE);
        this.urlParserNullOrEmptyListMessage = bundle.getString(ApplicationProperty.URL_PARSER_NULL_OR_EMPTY_LIST_MESSAGE);
        this.fileDownloaderBytesChunkSize = Integer.parseInt(bundle.getString(ApplicationProperty.FILE_DOWNLOADER_BYTES_CHUNK_SIZE));

    }
}
