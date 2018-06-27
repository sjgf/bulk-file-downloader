import org.apache.commons.cli.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Arrays;

/**
 * @author Samuel Gonzalez - 6/26/18
 * @project BulkFileDownloader
 * @version 0.1
 */

public class BulkFileDownloaderDriver {

    private static final BulkFileDownloaderDriver _self = new BulkFileDownloaderDriver();

    public static BulkFileDownloaderDriver getInstance() {
        return _self;
    }

    public void main(String[] args) {
        try {
            CommandLineManager commandLineManager = CommandLineManager.getInstance();

            commandLineManager.parse(args);

            UrlParser urlParser = new UrlParser();

            this.parseUrlListInFile(commandLineManager, urlParser);
            this.parseUrlList(commandLineManager, urlParser);
            this.parseUnidentifiedRemainingArgs(commandLineManager, urlParser);

            FileDownloader fileDownloader = new FileDownloader(commandLineManager.getTargetOptionValue());

            this.setConnectTimeoutInFileDownloader(commandLineManager, fileDownloader);
            this.setReadTimeoutInFileDownloader(commandLineManager, fileDownloader);

            this.startDownloadOfFiles(urlParser.getValid(), fileDownloader);

        } catch (ParseException e) {

            Console.writeError(ApplicationProperties.getInstance().getExceptionMessageHeaderTemplate(), "CommandLineManager.java.", e.getMessage());

        } catch (FileNotFoundException e) {

            Console.writeError(ApplicationProperties.getInstance().getExceptionMessageHeaderTemplate(), "FileParser.java.", e.getMessage());

        } catch (UrlParserException e) {

            Console.writeError(ApplicationProperties.getInstance().getExceptionMessageHeaderTemplate(), "UrlParser.java.", e.getMessage());
        }
    }

    private void startDownloadOfFiles(List<URL> validUrls, FileDownloader fileDownloader) {
        for (URL url : validUrls) {
            try {
                fileDownloader.download(url);
            } catch (IOException e) {

                Console.writeError(ApplicationProperties.getInstance().getExceptionMessageHeaderTemplate(), String.format("while downloading %s", url), e.getMessage());

            }
        }
    }

    private void setReadTimeoutInFileDownloader(CommandLineManager commandLineManager, FileDownloader fileDownloader) {
        if (commandLineManager.hasReadTimeout()) {
            fileDownloader.setReadTimeoutInSeconds(commandLineManager.getReadTimeout());
        }
    }

    private void setConnectTimeoutInFileDownloader(CommandLineManager commandLineManager, FileDownloader fileDownloader) {
        if (commandLineManager.hasConnectTimeout()) {
            fileDownloader.setConnectTimeoutInSeconds(commandLineManager.getConnectTimeout());
        }
    }

    private void parseUrlList(CommandLineManager commandLineManager, UrlParser urlParser) throws UrlParserException {
        if (commandLineManager.hasList()) {
            urlParser.parse(Arrays.asList(commandLineManager.getListOptionValue()));
        }
    }

    private void parseUrlListInFile(CommandLineManager commandLineManager, UrlParser urlParser) throws FileNotFoundException, UrlParserException {
        if (commandLineManager.hasFile()) {
            List<String> urls = new FileParser(commandLineManager.getFileOptionValue()).parse();
            urlParser.parse(urls);
        }
    }

    private void parseUnidentifiedRemainingArgs(CommandLineManager commandLineManager, UrlParser urlParser) throws UrlParserException {
        if(commandLineManager.getUnidentifiedArgs().size() > 0){
            urlParser.parse(commandLineManager.getUnidentifiedArgs());
        }
    }
}
