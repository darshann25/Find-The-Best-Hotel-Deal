import java.util.Date;

public class Hotel {
    String hotel_name;
    int nightly_rate;
    String promo_txt;
    int deal_value;
    String deal_type;
    Date start_date;
    Date end_date;

    public Hotel(String hotel_name, int nightly_rate, String promo_txt, int deal_value, String deal_type,
                 Date start_date, Date end_date) {

        this.hotel_name = hotel_name;
        this.nightly_rate = nightly_rate;
        this.promo_txt = promo_txt;
        this.deal_value = deal_value;
        this.deal_type = deal_type;
        this.start_date = start_date;
        this.end_date = end_date;

    }

    // Function to check if date falls within [start_date, end_date]
    public boolean isDateWithinRange(Date date) {

        boolean withinRange = false;

        if (date.equals(start_date) || date.equals(end_date)) withinRange = true;
        else if (date.after(start_date) && date.before(end_date)) withinRange = true;

        return withinRange;

    }

}
