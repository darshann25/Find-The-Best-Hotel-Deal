# Find The Best Hotel Deal
<h2>Problem Statement - Find the Best Hotel Deal</h2>
<p>
Imagine you’re given a data file containing hotel deals, aggregated from participating hotels. Write an app that will find the best deal given a hotel name, check-in date, and duration of stay. Print the promo_txt value for the best deal, or “no deals available” if none exists. Consider the type and value of the deals, as well as whether they apply. Your solution will be evaluated based on correctness, performance, code design, and maintainability.
</p>

<h2>Design</h2>
<ul>
    <li>
        <b>Hotel Object : </b>Created a Hotel Object that stores all the information 
        pertaining the information of each hotel. A different Hotel instance is created for the same
        hotel with a different deal. Hence, the Hotel object is deal-focused.
    </li>
    <li>
        <b>HotelDB : </b>HotelDB holds the information extracted from the input file (./deals.csv). 
        Storing the information from the input file in a HashMap with the (key, value) pair being 
        (String hotel_name, ArrayList of Hotel objects with different deals). Thus, each hotel is mapped 
        with all of its different deals.
    </li>
    <li>
        <b>Search for Miminal Cost: </b>Our goal was to find the deal that was valid and would give us
        the minimize the cost of stay for the customer. <b>The stay range can be within or beyond the deal
        range. However, the deal is applied for only the applicable days (printed with the promo text). </b> Using the arguments provided by
        the user, the program looks for the Hotel Objects in the HashMap. Once it gets the ArrayList of 
        all the objects, it iterates through them, and checks if the stay range is within and deal range.
        If the deal is valid for the whole or part of the stay, then the effective value is calculated
        and compared to the minimal cost value held from previous calculations.
    </li>  
    <li>
        <b>NOTE: Since only one search is made for every time the input file is processed, this problem
        can be solved by simply iterating through input file and looking for the minimal cost deal. It would
        give the fastest solution in this case (O(N) time). However, this would not be good design, since it 
        would not be reusable. If the input calls were changed to process the input file once, and then make
        several queries, the design that I have proposed would save a tremenduous amount
        of computer time.</b>
    </li>
    
</ul>

<h2>Efficiency / Run-Time Complexity</h2>
<h4>Time</h4>
<ul>
    <li>Data Storage : O(N), where N is the number of items in the input file.</li>
    <li>Hotel Search : O(1)</li>
    <li>Minimal Deal : O(n), where n is the number of deals with same hotel name being searched.</li>
    <li><b>TOTAL RUN TIME : </b> O(N) + O(n)</li>
    <li><i>Optimization : </i> If for one occurence of data storage, multiple searches are made,
    store the deals associated with one hotel name in an <b>Interval Tree</b> instead of a list.</li>
    <li> Interval Tree : 
        <ul>
            <li>Creation : O(n log(n))</li>
            <li>Deletion : O(log(n))</li>
            <li>Search : O(log(n) + m)</li>
        </ul>
    </li>    
</ul>
<h4>Space</h4>
<ul>
    <li>Since we are storing the entire input file, our space complexity will be O(N), where N is the number of 
    items in the input file.</li>
</ul>

<h2>Usage</h2>
<ul>
    <li><i>Makefile:</i> Run the Makefile using the 'make' command on the commandline.</li>
    <li><i>Usage:</i> java BestHotelDeal [input_path] [hotel_name] [check-in_date] [stay_duration]</li>
</ul>