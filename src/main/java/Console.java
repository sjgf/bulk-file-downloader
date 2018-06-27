import java.util.List;

/**
 * @author Samuel Gonzalez - 6/26/18
 * @project BulkFileDownloader
 * @version 0.1
 */

public class Console {

    public static void writeLine(String message){
        System.out.println(message);
    }

    public static void write(String message){
        System.out.println(message);
    }

    public static void newLine(){
        System.out.println();
    }

    public static void writeLine(List<String> list){
        if(list != null){
            for(String elem : list){
                System.out.println(elem);
            }
        }
    }

    public static void writeError(String staticHeader, String description, String exceptionMessage){
        Console.writeLine(String.format("%s %s\n%s", staticHeader, description, exceptionMessage));
    }

}
