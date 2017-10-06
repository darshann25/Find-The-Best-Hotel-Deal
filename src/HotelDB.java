import java.util.ArrayList;
import java.util.HashMap;

public class HotelDB {

    HashMap<String, ArrayList<Hotel>> database;

    public HotelDB() {

        database = new HashMap<>();

    }

    // Adds a hotel to the database
    public void addHotel(Hotel h) {

        ArrayList<Hotel> hotel_list;
        if(database.containsKey(h.hotel_name)) {
            hotel_list = database.get(h.hotel_name);
        } else {
            hotel_list = new ArrayList<>();
            hotel_list.add(h);
            database.put(h.hotel_name, hotel_list);
        }

    }

}
