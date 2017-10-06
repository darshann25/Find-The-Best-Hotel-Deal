import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Scanner;

public class BestHotelDeal {

    public static void main(String[] args) {

        /* STEP 1 : PARSE 'DEALS.CSV' AND CREATE HOTEL DATABASE */

        HotelDB database;
        try {
            database = createDatabase("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // print Instructions
        }




    }

    public static HotelDB createDatabase(String path) throws FileNotFoundException{

        HotelDB database = new HotelDB();
        Scanner scanner = new Scanner(new File("./src/deals.csv"));

        scanner.useDelimiter("[\\n,]");

        while (scanner.hasNext()) {
            if (scanner.hasNextLine()) {

                // using an object array to hold values of different data types
                String[] input = new String[7];

                for(int i = 0; i < 7; i++) {
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
}
