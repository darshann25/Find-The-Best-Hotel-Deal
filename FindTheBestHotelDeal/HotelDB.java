import java.util.ArrayList;
import java.util.HashMap;

/**
 * HotelDB Class holds the Hotel Objects created from the data in the input file
 */
public class HotelDB {

    // stores the hotel objects mapped to hotel name
    HashMap<String, ArrayList<Hotel>> database;

    /**
     * HotelDB Class Constructor initializes the database (hashmap)
     *
     * @param : none
     * @return : none
     */
    public HotelDB() {

        database = new HashMap<>();

    }

    /**
     * addHotel method add a hotel to the database by mapping the hotel name
     * to the list of hotels with different deals and adds the hotel object to
     * the list
     *
     * @param : Hotel to be added
     * @return : none
     * etc
     */

    // Adds a hotel to the database
    public void addHotel(Hotel h) {

        ArrayList<Hotel> hotel_list;
        if(database.containsKey(h.hotel_name)) {
            hotel_list = database.get(h.hotel_name);
        } else {
            hotel_list = new ArrayList<>();
        }
        hotel_list.add(h);
        database.put(h.hotel_name, hotel_list);

    }

}
