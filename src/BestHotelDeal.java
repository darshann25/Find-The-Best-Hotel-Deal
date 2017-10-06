import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class BestHotelDeal {

    public static void main(String[] args) {

        /* STEP 1 : PARSE 'DEALS.CSV' AND CREATE HOTEL DATABASE */

        HotelDB hotelDB = new HotelDB();
        try {
            hotelDB = createDatabase(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // print Instructions
        }


        /* STEP 2 : FIND THE BEST HOTEL DEAL FROM THE PROVIDED INPUT */

        String bestDeal = findBestHotelDeal(hotelDB, args[1], args[2], Integer.parseInt(args[3]));
        System.out.println(bestDeal);


    }

    public static HotelDB createDatabase(String path) throws FileNotFoundException{

        HotelDB database = new HotelDB();
        Scanner scanner = new Scanner(new File("./deals.csv"));

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

                if (currHotel.deal_type.equals("rebate") && num_nights >= 3) continue loop;
                if (currHotel.deal_type.equals("rebate_3plus") && num_nights < 3) continue loop;


                if (currHotel.isDateRangeWithinDealRange(date, num_nights)) {
                    double score = currHotel.evaluateDeal(num_nights);
                    if (score < min) {
                        bestDeal = currHotel;
                        min = score;
                    }
                }
            }
        }

        if (bestDeal == null) {
            return "no deal available";
        } else {
            return bestDeal.promo_txt;
        }
    }
}
