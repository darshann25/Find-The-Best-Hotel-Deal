import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;



public class BestHotelDeal {

    public enum Error {
        DEFAULT,
        ARGS_LEN,
        FILE_PATH,
        INPUT_FILE,
        USAGE

    }

    public static void main(String[] argsv) {

        String[] args = {"./src/deals.csv", "Hotel Foobar", "201621-021-012", "3"};

        if (args.length == 0) printInstructions(Error.ARGS_LEN);
        if (args[0].equals("-h") || args[0] .equals("--help")) printInstructions(Error.DEFAULT);
        if (args.length != 4 || Integer.parseInt(args[3]) <= 0) printInstructions(Error.ARGS_LEN);

        /* STEP 1 : PARSE 'DEALS.CSV' AND CREATE HOTEL DATABASE */

        HotelDB hotelDB = new HotelDB();
        try {
            hotelDB = createDatabase(args[0]);
        } catch (FileNotFoundException e) {
            printInstructions(Error.FILE_PATH);
        } catch (DateTimeParseException d) {
            printInstructions(Error.INPUT_FILE);
        }


        /* STEP 2 : FIND THE BEST HOTEL DEAL FROM THE PROVIDED INPUT */

        String bestDeal = findBestHotelDeal(hotelDB, args[1], args[2], Integer.parseInt(args[3]));
        //String bestDeal = findBestHotelDeal(hotelDB, "Days Inn", "2016-03-11", 3);

        System.out.println(bestDeal);


    }

    public static HotelDB createDatabase(String path) throws FileNotFoundException, DateTimeParseException{

        HotelDB database = new HotelDB();
        Scanner scanner = new Scanner(new File(path));

        scanner.useDelimiter("[\\n,]");

        while (scanner.hasNext()) {
            if (scanner.hasNextLine()) {

                // using an object array to hold values of different data types
                String[] input = new String[7];

                for (int i = 0; i < 7; i++) {
                    if(scanner.hasNext()) input[i] = scanner.next();
                }

                DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

                Date start_date = java.sql.Date.valueOf(LocalDate.parse(input[5], dateFormatter));
                Date end_date = java.sql.Date.valueOf(LocalDate.parse(input[6], dateFormatter));

                Hotel hotel = new Hotel(input[0], Integer.parseInt(input[1]), input[2], Math.abs(Integer.parseInt(input[3])),
                        input[4], start_date, end_date);

                database.addHotel(hotel);
            }
        }

        return database;
    }

    public static String findBestHotelDeal(HotelDB hotelDB, String hotel_name, String date, int num_nights) {

        double min = Integer.MAX_VALUE;
        Hotel bestDeal = null;

        if (hotelDB.database.containsKey(hotel_name)) {
            ArrayList<Hotel> hotel_deals = hotelDB.database.get(hotel_name);


            loop: for (int i = 0; i < hotel_deals.size(); i++) {
                Hotel currHotel = hotel_deals.get(i);

                // if (currHotel.deal_type.equals("rebate") && num_nights >= 3) continue loop;
                if (currHotel.deal_type.equals("rebate_3plus") && num_nights < 3) continue loop;


                try {
                    if (currHotel.isDateRangeWithinDealRange(date, num_nights)) {
                        double score = currHotel.evaluateDeal(num_nights);
                        if (score < min) {
                            bestDeal = currHotel;
                            min = score;
                        }
                    }
                } catch (ParseException e) {
                    printInstructions(Error.USAGE);
                }
            }
        }

        if (bestDeal == null) {
            return "no deal available";
        } else {
            StringBuilder result = new StringBuilder(bestDeal.promo_txt);
            if (bestDeal.effective_num_nights != num_nights && bestDeal.effective_num_nights != 0) {
                result.append(" (" + bestDeal.effective_num_nights + " nights)");
            }
            return result.toString();
        }
    }

    public static void printInstructions(Error error){

        final String PRODUCT = "\nPRODUCT:\n\tBestHotelDeal";
        final String USAGE = "USAGE:\n\tjava BestHotelDeal [input_path] [hotel_name] [check-in_date] [stay_duration]";
        final String DESCRIPTION = "EXPLANATION:\n\tThis app is used to find the best deal for a Hotel based on Check-in date and Duration of Stay";
        final String ARGS0 = "\tinput_path:\tThis is the path to the file that contains the deals in comma-separated form(CSV)";
        final String ARGS1 = "\thotel_name:\tName of the Hotel";
        final String ARGS2 = "\tcheck-in_date:\tCheck-in date in ISO Date Format [yyyy-MM-dd]";
        final String ARGS3 = "\tstay_duration:\tDuration of the Stay [Greater than zero]\n";
        final String HELP = "HELP:\t\tjava BestHotelDeal --help\n";
        final String CSV_FORMAT = "FORMAT:\nhotel_name,nightly_rate,promo_txt,deal_value,deal_type,start_date,end_date";
        final String CSV_EXAMPLE1 = "Hotel Foobar,250,$50 off your stay 3 nights or more,-50,rebate_3plus,2016-03-01,2016-03-31";
        final String CSV_EXAMPLE2 = "Hotel Foobar,250,5% off your stay,-5,pct,2016-03-01,2016-03-15\n";

        switch (error) {
            case DEFAULT:
                String[] intro = {PRODUCT, USAGE, DESCRIPTION, ARGS0, ARGS1, ARGS2, ARGS3};
                for (String i : intro) {
                    System.out.println(i);
                }
                System.exit(0);
                break;

            case ARGS_LEN:
                System.out.println("INPUT ERROR:\tIncorrect number of arguments");
                System.out.println(HELP);
                break;

            case FILE_PATH:
                System.out.println("INPUT ERROR:\tIncorrect file path provided");
                System.out.println(HELP);
                break;

            case INPUT_FILE:
                System.out.println("INPUT ERROR : Format of input file is incorrect\n");
                final String[] CSV = {CSV_FORMAT, CSV_EXAMPLE1, CSV_EXAMPLE2};
                for (String i : CSV) {
                    System.out.println(i);
                }

            case USAGE:
                System.out.println("INPUT ERROR: Format of input string is incorrect");
                System.out.println(HELP);
                break;
        }

        System.exit(1);

    }
}
