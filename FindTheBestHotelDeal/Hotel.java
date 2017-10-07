import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Hotel Class holds hotel deal data within a single object
 */
public class Hotel {

    // Internal Variables
    String hotel_name;
    int nightly_rate;
    String promo_txt;
    int deal_value;
    String deal_type;
    Date start_date;
    Date end_date;
    int effective_num_nights;   // stores the range of the stay falling within the deal range


    /**
     * Object Hotel Constructor takes in values from functions and stores them in internal public
     * variables
     *
     * @param : hotel_name, nightly_rate, promo_txt, deal_value, deal_type, start_date, end_date
     * @return : none
     */
    public Hotel(String hotel_name, int nightly_rate, String promo_txt, int deal_value, String deal_type,
                 Date start_date, Date end_date) {

        // set the Internal Variables
        this.hotel_name = hotel_name;
        this.nightly_rate = nightly_rate;
        this.promo_txt = promo_txt;
        this.deal_value = deal_value;
        this.deal_type = deal_type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.effective_num_nights = 0;  // initially set to zero

    }


    /**
     * Method to check if a date falls within a range of the start and end date. If it falls completely or partially within
     * range then it returns true, otherwise false. If part of the stay_range (date + num_nights) lies within the range,
     * then the difference is stored in effective_num_nights
     *
     * @param : date to be checked within range, num_nights to calculate the stay range
     * @return : true if date falls within range, and false otherwise
     * @throws : ParseException if the date is not in ISO format
     */
    public boolean isDateRangeWithinDealRange(String date, int num_nights) throws ParseException {

        // booleans for start and end of stay range falling within range
        boolean start_within_range = false;
        boolean end_within_range = false;

        // set the date format for ISO
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // check if the input date is valid
        Date tDate = sdf.parse(date);
        if (!date.equals(sdf.format(tDate))) {
            throw new ParseException("",0);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        Date range_start = c.getTime();    // set range start date
        c.add(Calendar.DATE, num_nights);  // number of days to add
        Date range_end = c.getTime();   // set range end date

        // check if start date and end date fall within range
        if (range_start.after(start_date) && range_start.before(end_date)) start_within_range = true;
        if (range_end.after(start_date) && range_end.before(end_date)) end_within_range = true;

        if(range_start.equals(start_date) || range_start.equals(end_date)) start_within_range = true;
        if(range_end.equals(start_date) || range_end.equals(end_date)) end_within_range = true;

        // Calculate effective number of nights for the deal to be valid
        if (logicalXOR(start_within_range, end_within_range)) {

            long diff = 0;
            if (!start_within_range && end_within_range) {
            //if (range_start.before(start_date) && (range_end.before(end_date) || range_end.equals(end_date))) {
                diff = Math.abs(range_end.getTime() - start_date.getTime());
            }

            if (start_within_range && !end_within_range) {
            //if ((range_start.after(start_date) || range_start.equals(start_date)) && range_end.after(end_date)) {
                diff = Math.abs(end_date.getTime() - range_start.getTime());
            }

            if (diff == 0) diff++;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            effective_num_nights = (int) diffDays;
        }

        return (start_within_range || end_within_range);

    }

    /**
     * Evaluates the Score/Value for a Deal, which is essentially the final cost for the customer's stay
     *
     * @param : num_nights of the stay range
     * @return : score -> final cost of the customer's stay
     * etc
     */
    public double evaluateDeal(int num_nights) {

        // using double, since percentages are involved
        double value1 = nightly_rate * effective_num_nights;    // deal applicable on value1
        double value2 = nightly_rate * (num_nights - effective_num_nights);     // deal not applicable on value2

        switch(deal_type) {

            // rebate -> subtract deal_value from total cost
            case "rebate" :
                value1 -= deal_value;
                break;

            // rebate_3plus -> if stay within range is greater than 3, then rebate
            case "rebate_3plus" :
                if (effective_num_nights >= 3) value1 -= deal_value;
                break;

            // pct -> subtract a percent of the cost as discount for the deal
            case "pct" :
                double discount = (deal_value / 100.0) * value1;
                value1 -= discount;
                break;

            default:
                break;
        }

        return value1 + value2;

    }

    /**
     * logicalXOR calculates the Exclusive OR value of two booleans
     * source : Stack Overflow
     *
     * @param : two boolean values x, y
     * @return : logical exclusive or of the boolean values
     *
     */
    public static boolean logicalXOR(boolean x, boolean y) {

        return ( ( x || y ) && ! ( x && y ) );

    }
}
