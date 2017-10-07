import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


/**
 * BestHotelDeal is the main class for the program for running the program
 */
public class BestHotelDeal {

    // holds the enums used for error handling
    public enum Error {
        DEFAULT,
        ARGS_LEN,
        FILE_PATH,
        INPUT_FILE,
        USAGE

    }

    /**
     * main method runs the program in 3 steps
     * Step 1 : Check for input errors (cover major errors, minor errors handled as they are encountered further)
     * Step 2 : Create the database from the input file
     * Step 3 : Search for the Optimal Deal from the database based on input
     *
     * @param : inputfile_path, hotel_name, check-in_date, duration_of_stay
     * @return : none
     * etc
     */
    public static void main(String[] args) {

        /* STEP 1 : INPUT ERROR CHECKING */

        if (args.length == 0) printInstructions(Error.ARGS_LEN);    // check if no arguments are passed
        if (args[0].equals("-h") || args[0] .equals("--help")) printInstructions(Error.DEFAULT);    // check if help argument is passed
        if (args.length != 4) printInstructions(Error.ARGS_LEN);  // check if arguments size is not 4
        if (Integer.parseInt(args[3]) == 0) printInstructions(Error.USAGE);     // check if duration of stay is not zero

        /* STEP 2 : PARSE 'DEALS.CSV' AND CREATE HOTEL DATABASE */

        HotelDB hotel_database = new HotelDB();    // initialize the database
        try {
            hotel_database = createDatabase(args[0]);      // creates the database from the input file
        } catch (FileNotFoundException e) {
            printInstructions(Error.FILE_PATH);     // handle incorrect filepath error
        } catch (DateTimeParseException d) {
            printInstructions(Error.INPUT_FILE);    // handle incorrect input file format error
        }


        /* STEP 3 : FIND THE BEST HOTEL DEAL FROM THE PROVIDED INPUT */

        String bestDeal = findBestHotelDeal(hotel_database, args[1], args[2], Integer.parseInt(args[3]));      // finds the best deal from the input and database
        System.out.println(bestDeal);   // prints the solution to the commandline


    }

    /**
     * createDatabase method creates the database from the input file
     *
     * @param : inputfile_path
     * @return : HotelDB object that holds the database
     * @throws : FileNotFoundException for file path error handling
     *           DateTimeParseException for incorrect date error handling
     */
    public static HotelDB createDatabase(String path) throws FileNotFoundException, DateTimeParseException{

        HotelDB database = new HotelDB();   // holds the database
        Scanner scanner = new Scanner(new File(path));  // scans the input file

        scanner.useDelimiter("[\\n,]");

        while (scanner.hasNext()) {
            if (scanner.hasNextLine()) {

                String[] input = new String[7];     // holds each line of the input file at one time

                for (int i = 0; i < 7; i++) {
                    if(scanner.hasNext()) input[i] = scanner.next();
                }

                DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;     // set the ISO format for the parser

                Date start_date = java.sql.Date.valueOf(LocalDate.parse(input[5], dateFormatter));  // parse the start date
                Date end_date = java.sql.Date.valueOf(LocalDate.parse(input[6], dateFormatter));    // parse the end date

                // create a Hotel object
                Hotel hotel = new Hotel(input[0], Integer.parseInt(input[1]), input[2], Math.abs(Integer.parseInt(input[3])),
                        input[4], start_date, end_date);

                database.addHotel(hotel);   // add the hotel to the database
            }
        }

        return database;
    }

    /**
     * findtheBestHotelDeal method gets the hotel list from the database and iterates through it to find the
     * best valid deal
     *
     * @param : hotel_database, check-in_date, duration of stay -> num_nights
     * @return : promo_txt of the best deal
     */
    public static String findBestHotelDeal(HotelDB hotel_database, String hotel_name, String date, int num_nights) {

        double min = Integer.MAX_VALUE;     // holds the score of the optimal solution
        Hotel bestDeal = null;      // holds the Hotel Object for the optimal solution

        if (hotel_database.database.containsKey(hotel_name)) {  // look into the hashmap for the hotel_name

            ArrayList<Hotel> hotel_deals = hotel_database.database.get(hotel_name);     // get the list of hotels

            // iterate through the list to find the most optimal deal
            loop:
            for (int i = 0; i < hotel_deals.size(); i++) {
                Hotel currHotel = hotel_deals.get(i);

                // if (currHotel.deal_type.equals("rebate") && num_nights >= 3) continue loop;
                if (currHotel.deal_type.equals("rebate_3plus") && num_nights < 3) continue loop;

                try {
                    if (currHotel.isDateRangeWithinDealRange(date, num_nights)) {   // checks if date range overlaps with stay range
                        double score = currHotel.evaluateDeal(num_nights);      // hold the score of the current deal
                        if (score < min) {      // compare the score with the current minimal deal
                            bestDeal = currHotel;
                            min = score;
                        }
                    }
                } catch (ParseException e) {
                    printInstructions(Error.USAGE);     // date error handling
                }
            }
        }

        if (bestDeal == null) {
            return "no deal available";     // if no optimal deal is found
        } else {
            StringBuilder result = new StringBuilder(bestDeal.promo_txt);   // find the promo code and the number of nights it was applicable for
            if (bestDeal.effective_num_nights != num_nights && bestDeal.effective_num_nights != 0) {
                result.append(" (" + bestDeal.effective_num_nights + " nights)");
            }
            return result.toString();
        }
    }

    /**
     * printInstructions method is used to print usage instructions and error handling messages
     *
     * @param : Error enum for indicating the type of error
     * @return : prints the usage / error message
     */
    public static void printInstructions(Error error){

        final String PRODUCT = "\nPRODUCT:\n\tBestHotelDeal";
        final String USAGE = "USAGE:\n\tjava BestHotelDeal [input_path] [hotel_name] [check-in_date] [stay_duration]";
        final String DESCRIPTION = "EXPLANATION:\n\tThis app is used to find the best deal for a Hotel based on Check-in date and Duration of Stay";
        final String ARGS0 = "\tinput_path:\tThis is the path to the file that contains the deals in comma-separated form(CSV)";
        final String ARGS1 = "\thotel_name:\tName of the Hotel";
        final String ARGS2 = "\tcheck-in_date:\tCheck-in date in ISO Date Format [yyyy-MM-dd]";
        final String ARGS3 = "\tstay_duration:\tDuration of the Stay [Greater than zero]\n";
        final String HELP = "HELP:\tjava BestHotelDeal --help\n";
        final String CSV_FORMAT = "FORMAT:\nhotel_name,nightly_rate,promo_txt,deal_value,deal_type,start_date,end_date";
        final String CSV_EXAMPLE1 = "Hotel Foobar,250,$50 off your stay 3 nights or more,-50,rebate_3plus,2016-03-01,2016-03-31";
        final String CSV_EXAMPLE2 = "Hotel Foobar,250,5% off your stay,-5,pct,2016-03-01,2016-03-15\n";

        switch (error) {
            case DEFAULT:   // default instructions
                String[] intro = {PRODUCT, USAGE, DESCRIPTION, ARGS0, ARGS1, ARGS2, ARGS3};
                for (String i : intro) {
                    System.out.println(i);
                }
                System.exit(0);
                break;

            case ARGS_LEN:  // invalid length of input arguments
                System.out.println("INPUT ERROR:\tIncorrect number of arguments");
                System.out.println(HELP);
                break;

            case FILE_PATH:     // incorrect file path
                System.out.println("INPUT ERROR:\tIncorrect file path provided");
                System.out.println(HELP);
                break;

            case INPUT_FILE:    // incorrect input file format
                System.out.println("INPUT ERROR : Format of input file is incorrect\n");
                final String[] CSV = {CSV_FORMAT, CSV_EXAMPLE1, CSV_EXAMPLE2};
                for (String i : CSV) {
                    System.out.println(i);
                }

            case USAGE:     // incorrect input arguments
                System.out.println("INPUT ERROR: Format of input is incorrect");
                System.out.println(HELP);
                break;
        }

        System.exit(1);

    }
}
