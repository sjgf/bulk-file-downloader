import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuel Gonzalez - 6/26/18
 * @project BulkFileDownloader
 * @version 0.1
 */

public class UrlParser {

    private List<URL> valid;
    private List<String> invalid;

    public UrlParser(){
        this.valid = new ArrayList<>();
        this.invalid = new ArrayList<>();
    }

    public List<URL> getValid() {
        return this.valid;
    }

    public List<String> getInvalid() {
        return this.invalid;
    }

    public void parse(List<String> urls) throws UrlParserException {

        if(urls == null || urls.size() == 0){
            throw new UrlParserException(ApplicationProperties.getInstance().getUrlParserNullOrEmptyListMessage());
        }

        for(String url : urls){
            try {

                this.valid.add(new URL(url));

            } catch (MalformedURLException e) {

                this.invalid.add(url);

            }
        }


    }


}
