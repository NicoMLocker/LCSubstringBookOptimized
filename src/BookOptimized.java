import java.awt.print.Book;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import static java.nio.file.Files.readAllBytes;
import java.nio.file.Files;
import java.nio.file.Paths;


public class BookOptimized {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    /* define constants */

    private static String result;
    private final int length;
    static String ResultsFolderPath = "/home/nicolocker/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;


    public static void main(String[] args) throws IOException {

        System.out.println("\n");

        System.out.println("Running first full experiment...");
        runFullExperiment("LCSubstringBookOptimized-Exp1.txt");
    }


    public static void runFullExperiment(String resultsFileName) throws IOException {

        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);
        } catch (Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file " + ResultsFolderPath + resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...
        }

        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial

        resultsWriter.println("#X(input size)         N(result size)            T(avg time)"); // # marks a comment in gnuplot data
        resultsWriter.flush();
        /* for each size of input we want to test: in this case starting small and doubling the size each time */

        long batchElapsedTime = 0;

        /* force garbage collection before each batch of trials run so it is not included in the time */
        System.gc();

        TrialStopwatch.start(); // *** uncomment this line if timing trials individually

        /* run the function we're testing on the trial input */
        BookOptimized.findInBook();

        batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing trials individually
        // }

        // double averageTimePerTrialInBatch = (double) batchElapsedTime / (double) numberOfTrials; // calculate the average time per trial in this batch

        double averageTimePerTrialInBatch = (double) batchElapsedTime;

        /* print data for this size of input */
        resultsWriter.printf("%20d %30.2f\n", result.length(), averageTimePerTrialInBatch); // might as well make the columns look nice
        resultsWriter.flush();
        System.out.println(" ....done.");
    }


    public static BookOptimized search(String str1, String str2){

        int length1 = str1.length();
        int length2 = str2.length();
        String result = "";
        int checker = 0;

        for( int i = 0; i < length1; i++){
            for( int j = 0; j < length2; j++){
                for( int k = 0; i + k < length1 && j + k < length2; k++){
                    if( str1.charAt(i + k) != str2.charAt(j + k)){
                        break;
                    }
                    if( k + 1 > result.length()){
                        result = str1.substring(i, i + k + 1);
                    }
                    checker++;
                }
                //if k = result.length -> break
                if( checker == result.length() && checker != 0 ){
                    break;
                }
            }
            /*
            //if k = result.length -> break
            if( checker == result.length() && checker != 0 ){
                break;
            }
             */
        }
        System.out.println("Common Substring - " + result);
        System.out.println("Length of Common SubString - " + result.length());

        return new BookOptimized(result, result.length());
    }

    // constructor
    public BookOptimized(String result, int length) {
        this.result = result;
        this.length = length;
    }

    public static void findInBook () throws IOException {
        String book1 = new String(Files.readAllBytes(Paths.get("/home/nicolocker/IdeaProjects/LCSubstringBook/src/TimeMachine")));
        String book2 = new String(Files.readAllBytes(Paths.get("/home/nicolocker/IdeaProjects/LCSubstringBook/src/WarOfTheWorlds")));

        BookOptimized.search(book1, book2);
    }
}

