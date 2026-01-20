package pl.lab6;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.sys.JenaSystem;

import pl.lab6.browse.GraphBrowser;
import pl.lab6.io.RdfIO;
import pl.lab6.query.SparqlRunner;
import pl.lab6.reason.ReasonerRunner;

public final class App {
    private static final String DEFAULT_INPUT = "data/wardrobe.ttl";
    private static final String DEFAULT_FORMAT = "TTL";

    private static final String CMD_IO = "io";
    private static final String CMD_BROWSE = "browse";
    private static final String CMD_SPARQL = "sparql";
    private static final String CMD_REASON = "reason";

    private static final String OPT_INPUT = "--input";
    private static final String OPT_FORMAT = "--format";
    private static final String OPT_QUERY = "--query";
    private static final String OPT_RULES = "--rules";
    private static final String OPT_HELP = "--help";
    private static final String OPT_HELP_SHORT = "-h";
    private static final String OPT_INPUT_SHORT = "-i";
    private static final String OPT_FORMAT_SHORT = "-f";

    private App() {
    }

    public static void main(String[] args) {
        JenaSystem.init();

        Options options = Options.parse(args);
        if (options.showHelp) {
            printUsage();
            return;
        }

        if (options.command == null) {
            fail("Missing command.");
        }

        if (!Files.exists(Path.of(options.inputPath))) {
            fail("Input file not found: " + options.inputPath);
        }

        try {
            switch (options.command) {
                case CMD_IO:
                    runIo(options);
                    break;
                case CMD_BROWSE:
                    runBrowse(options);
                    break;
                case CMD_SPARQL:
                    runSparql(options);
                    break;
                case CMD_REASON:
                    runReason(options);
                    break;
                default:
                    fail("Unknown command: " + options.command);
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static void runIo(Options options) throws Exception {
        Model model = RdfIO.loadModel(options.inputPath);
        Lang lang = RdfIO.parseLang(options.format);

        Path outDir = Path.of("out");
        Files.createDirectories(outDir);
        Path outFile = outDir.resolve("out.ttl");

        RdfIO.writeModel(model, outFile.toString(), lang);
        System.out.println("Wrote model to: " + outFile);
    }

    private static void runBrowse(Options options) {
        Model model = RdfIO.loadModel(options.inputPath);
        GraphBrowser browser = new GraphBrowser();
        browser.printOverview(model);
    }

    private static void runSparql(Options options) {
        if (options.queryPath == null) {
            fail("Missing " + OPT_QUERY + " for sparql command.");
        }
        if (!Files.exists(Path.of(options.queryPath))) {
            fail("Query file not found: " + options.queryPath);
        }

        Model model = RdfIO.loadModel(options.inputPath);
        SparqlRunner.runQuery(model, options.queryPath);
    }

    private static void runReason(Options options) {
        if (options.rulesPath == null) {
            fail("Missing " + OPT_RULES + " for reason command.");
        }
        if (!Files.exists(Path.of(options.rulesPath))) {
            fail("Rules file not found: " + options.rulesPath);
        }

        Model model = RdfIO.loadModel(options.inputPath);
        ReasonerRunner.runAndPrint(model, options.rulesPath);
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar target/jena-lab6.jar [options] <command>");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  io       Load RDF and write to out/out.ttl");
        System.out.println("  browse   Print basic graph stats and sample triples");
        System.out.println("  sparql   Run SPARQL query (requires --query)");
        System.out.println("  reason   Run rule-based reasoning (requires --rules)");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --input, -i   Input file (default: " + DEFAULT_INPUT + ")");
        System.out.println("  --format, -f  Output format for io: TTL|RDFXML|JSONLD (default: " + DEFAULT_FORMAT + ")");
        System.out.println("  --query       SPARQL query file path");
        System.out.println("  --rules       Jena rules file path");
        System.out.println("  --help, -h    Show this help");
    }

    private static void fail(String message) {
        System.err.println(message);
        printUsage();
        System.exit(1);
    }

    private static final class Options {
        private String inputPath = DEFAULT_INPUT;
        private String format = DEFAULT_FORMAT;
        private String command;
        private String queryPath;
        private String rulesPath;
        private boolean showHelp;

        private static Options parse(String[] args) {
            Options options = new Options();

            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                if (OPT_HELP.equals(arg) || OPT_HELP_SHORT.equals(arg)) {
                    options.showHelp = true;
                    return options;
                }

                if (OPT_INPUT.equals(arg) || OPT_INPUT_SHORT.equals(arg)) {
                    options.inputPath = requireValue(arg, args, ++i);
                    continue;
                }

                if (OPT_FORMAT.equals(arg) || OPT_FORMAT_SHORT.equals(arg)) {
                    options.format = requireValue(arg, args, ++i);
                    continue;
                }

                if (OPT_QUERY.equals(arg)) {
                    options.queryPath = requireValue(arg, args, ++i);
                    continue;
                }

                if (OPT_RULES.equals(arg)) {
                    options.rulesPath = requireValue(arg, args, ++i);
                    continue;
                }

                if (arg.startsWith("-")) {
                    fail("Unknown option: " + arg);
                }

                if (options.command == null) {
                    options.command = arg;
                } else {
                    fail("Unexpected argument: " + arg);
                }
            }

            return options;
        }

        private static String requireValue(String opt, String[] args, int index) {
            if (index >= args.length) {
                fail("Missing value for " + opt);
            }
            return args[index];
        }
    }
}
