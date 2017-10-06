import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public boolean isDateRangeWithinDealRange(String date, int num_nights) {

        boolean withinRange = true;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date range_start = c.getTime();
        c.add(Calendar.DATE, num_nights);  // number of days to add
        Date range_end = c.getTime();

        if (range_start.before(start_date) || range_start.after(end_date)) withinRange = false;
        if (range_end.before(start_date) || range_end.after(end_date)) withinRange = false;

        return withinRange;

    }

    // Function to evaluate the value of a deal
    public double evaluateDeal(int num_nights) {

        // using double, since percentages are involved
        double value = nightly_rate * num_nights;

        switch(deal_type) {

            // Although case rebate and rebate_3plus perform
            // the same function, I am leaving them as different cases
            // for any changes in the future
            case "rebate" :
                value -= deal_value;
                break;

            case "rebate_3plus" :
                value -= deal_value;
                break;

            case "pct" :
                double discount = (deal_value / 100.0) * value;
                value -= discount;
                break;

            default:
                break;
        }

        return value;

    }
}
