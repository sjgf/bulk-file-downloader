import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Samuel Gonzalez - 6/26/18
 * @project BulkFileDownloader
 * @version 0.1
 */

public class FileParser {

    private String filePath;

    public FileParser(){
        this.filePath = null;
    }

    public FileParser(String filePath){
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> parse() throws FileNotFoundException {

        File file = this.isFile(this.filePath);

        if(file == null){
            throw new FileNotFoundException(String.format("The file specified in path %s does not exists", this.filePath));
        }

        return this.readFileLines(file);
    }

    private File isFile(String filePath){
        File file = new File(filePath);

        if(file.exists() && file.isFile()){
            return file;
        }

        return null;
    }

    private List<String> readFileLines(File file) throws FileNotFoundException {
        ArrayList<String> urlList = new ArrayList<>();

        Scanner fileScanner = new Scanner(file);

        while(fileScanner.hasNextLine()){
            urlList.add(fileScanner.nextLine());
        }

        fileScanner.close();

        return urlList;
    }

}
