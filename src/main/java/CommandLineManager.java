
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;

/**
 * @author Samuel Gonzalez - 6/26/18
 * @project BulkFileDownloader
 * @version 0.1
 */

import java.util.List;

public class CommandLineManager {

    public static final char
        SHORT_OPTION_PARAM_IDENTIFIER = '-',
        FILE_SHORT_OPTION = 'f',
        HELP_SHORT_OPTION = 'h',
        LIST_SHORT_OPTION = 'l',
        TARGET_SHORT_OPTION = 't';

    public static final String
        LONG_OPTION_PARAM_IDENTIFIER = "--",
        FILE_LONG_OPTION = "file",
        HELP_LONG_OPTION = "help",
        LIST_LONG_OPTION = "list",
        TARGET_LONG_OPTION = "target",
        CONNECT_TIMEOUT_LONG_OPTION = "connect-timeout",
        READ_TIMEOUT_LONG_OPTION = "read-timeout";

    private static CommandLineManager _self = new CommandLineManager();

    private CommandLineParser commandLineParser;
    private CommandLine commandLine;
    private Options options;

    private String fileOptionValue, targetOptionValue;
    private String[] listOptionValue;
    private List<String> unidentifiedArgs;
    private int connectTimeout, readTimeout;
    private boolean hasList, hasFile, hasHelp, hasTarget, hasConnectTimeout, hasReadTimeout;

    private CommandLineManager() {
        this.commandLineParser = new DefaultParser();

        this.initCliOptions();
    }

    public static CommandLineManager getInstance() {
        return _self != null ? _self : new CommandLineManager();
    }

    public boolean hasList() {
        return hasList;
    }

    public boolean hasFile() {
        return hasFile;
    }

    public boolean hasHelp() {
        return hasHelp;
    }

    public boolean hasTarget() {
        return hasTarget;
    }

    public boolean hasConnectTimeout() {
        return hasConnectTimeout;
    }

    public boolean hasReadTimeout() {
        return hasReadTimeout;
    }

    public String getFileOptionValue() {
        return fileOptionValue;
    }

    public String[] getListOptionValue() {
        return listOptionValue;
    }

    public String getTargetOptionValue() {
        return targetOptionValue;
    }

    public List<String> getUnidentifiedArgs(){
        return this.unidentifiedArgs;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void parse(String[] args) throws ParseException {

        this.commandLine = this.commandLineParser.parse(options, args);

        this.hasFile = this.commandLine.hasOption(FILE_SHORT_OPTION);
        this.hasList = this.commandLine.hasOption(LIST_SHORT_OPTION);
        this.hasHelp = this.commandLine.hasOption(HELP_SHORT_OPTION);
        this.hasTarget = this.commandLine.hasOption(TARGET_SHORT_OPTION);

        if (this.hasFile) {

            this.fileOptionValue = commandLine.getOptionValue(FILE_SHORT_OPTION);
            this.printArgumentList(String.format("%s%s", LONG_OPTION_PARAM_IDENTIFIER, FILE_LONG_OPTION), this.fileOptionValue);
        }

        if (this.hasList) {
            this.listOptionValue = commandLine.getOptionValues(LIST_SHORT_OPTION);
            this.printArgumentList(String.format("%s%s", LONG_OPTION_PARAM_IDENTIFIER, LIST_LONG_OPTION), this.listOptionValue);
        }

        if(this.hasTarget){
            this.targetOptionValue = commandLine.getOptionValue(TARGET_SHORT_OPTION);
            this.printArgumentList(String.format("%s%s", LONG_OPTION_PARAM_IDENTIFIER, TARGET_LONG_OPTION), this.targetOptionValue);
        }

        if(this.hasConnectTimeout){
            this.connectTimeout = Integer.parseInt(commandLine.getOptionValue(CONNECT_TIMEOUT_LONG_OPTION));
            this.printArgumentList(String.format("%s%s", LONG_OPTION_PARAM_IDENTIFIER, CONNECT_TIMEOUT_LONG_OPTION), String.valueOf(this.connectTimeout));
        }

        if(this.hasReadTimeout){
            this.readTimeout = Integer.parseInt(commandLine.getOptionValue(READ_TIMEOUT_LONG_OPTION));
            this.printArgumentList(String.format("%s%s", LONG_OPTION_PARAM_IDENTIFIER, READ_TIMEOUT_LONG_OPTION), String.valueOf(this.readTimeout));
        }

        if (this.hasHelp) {
            printHelp(options);
        }

        // This will set any remaining arguments that were not identified
        this.unidentifiedArgs = commandLine.getArgList();

    }

    private void initCliOptions() {
        this.options = new Options();

        Option list = Option.builder(String.valueOf(LIST_SHORT_OPTION))
                .longOpt(LIST_LONG_OPTION)
                .argName("url")
                .desc("The space separated url list of the files to download. Could be used in conjuntion with -f argument")
                .hasArgs()
                .valueSeparator(' ')
                .build();

        options.addOption(list);

        Option connectTimeout = Option.builder()
                .longOpt(CONNECT_TIMEOUT_LONG_OPTION)
                .type(Long.class)
                .hasArg()
                .numberOfArgs(1)
                .desc("The time the program will wait to establish the connection.")
                .build();

        Option readTimeout = Option.builder()
                .longOpt(READ_TIMEOUT_LONG_OPTION)
                .type(Long.class)
                .hasArg()
                .numberOfArgs(1)
                .desc("The time the program will wait for reading before dropping the connection.")
                .build();

        options.addOption(String.valueOf(FILE_SHORT_OPTION), FILE_LONG_OPTION, true, "The relative or absolute path to file containing the url list of the files to download");
        options.addOption(String.valueOf(LIST_SHORT_OPTION), LIST_LONG_OPTION, true, "The space separated url list of the file sto download.");
        options.addOption(String.valueOf(TARGET_SHORT_OPTION), TARGET_LONG_OPTION, true, "The target directory to where files will be downloaded.");
        options.addOption(connectTimeout);
        options.addOption(readTimeout);
        options.addOption(String.valueOf(HELP_SHORT_OPTION), HELP_LONG_OPTION, false, "Help - how to use the program.");
    }

    private void printArgumentList(String argName, String... args) {
        StringBuilder fileListBuilder = new StringBuilder(String.format("%s: [ ", argName));

        for (String filePath : args) {
            fileListBuilder.append(filePath).append(", ");
        }

        System.out.println(fileListBuilder.delete(fileListBuilder.length() - 2, fileListBuilder.length()).append(" ]").toString());
    }

    private void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("ant", options);
    }
}
