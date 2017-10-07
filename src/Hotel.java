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
    int effective_num_nights;

    public Hotel(String hotel_name, int nightly_rate, String promo_txt, int deal_value, String deal_type,
                 Date start_date, Date end_date) {

        this.hotel_name = hotel_name;
        this.nightly_rate = nightly_rate;
        this.promo_txt = promo_txt;
        this.deal_value = deal_value;
        this.deal_type = deal_type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.effective_num_nights = 0;

    }

    // Function to check if date falls within [start_date, end_date]
    public boolean isDateRangeWithinDealRange(String date, int num_nights) throws ParseException {

        boolean start_within_range = false;
        boolean end_within_range = false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date tDate = sdf.parse(date);
        if (!date.equals(sdf.format(tDate))) {
            throw new ParseException("",0);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        Date range_start = c.getTime();
        c.add(Calendar.DATE, num_nights);  // number of days to add
        Date range_end = c.getTime();

        // if (range_start.before(start_date) || range_start.after(end_date)) withinRange = false;
        // if (range_end.before(start_date) || range_end.after(end_date)) withinRange = false;

        if (range_start.after(start_date) && range_start.before(end_date)) start_within_range = true;
        if (range_end.after(start_date) && range_end.before(end_date)) end_within_range = true;

        // Calculate effective number of nights for the deal to be valid
        if (logicalXOR(start_within_range, end_within_range)) {
            if (range_start.before(start_date) && range_end.before(end_date)) {
                long diff = Math.abs(range_end.getTime() - start_date.getTime());
                if (diff == 0) diff++;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                effective_num_nights = (int) diffDays;
            }

            if (range_start.after(start_date) && range_end.after(end_date)) {
                long diff = Math.abs(end_date.getTime() - range_start.getTime());
                if (diff == 0) diff++;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                effective_num_nights = (int) diffDays;
            }
        }

        return (start_within_range || end_within_range);

    }

    // Function to evaluate the value of a deal
    public double evaluateDeal(int num_nights) {

        // using double, since percentages are involved
        double value1 = nightly_rate * effective_num_nights;    // deal applicable on value1
        double value2 = nightly_rate * (num_nights - effective_num_nights);     // deal not applicable on value2

        switch(deal_type) {

            // Although case rebate and rebate_3plus perform
            // the same function, I am leaving them as different cases
            // for any changes in the future
            case "rebate" :
                value1 -= deal_value;
                break;

            case "rebate_3plus" :
                if (effective_num_nights >= 3) value1 -= deal_value;
                break;

            case "pct" :
                double discount = (deal_value / 100.0) * value1;
                value1 -= discount;
                break;

            default:
                break;
        }

        return value1 + value2;

    }

    public static boolean logicalXOR(boolean x, boolean y) {
        return ( ( x || y ) && ! ( x && y ) );
    }
}
