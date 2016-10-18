package com.lastminute.flight_search;
/*
    Simple flight search application:

    No frameworks, except for testing purposes
    CSV comma-separated files without internal fuzz (embedded ", diff separators, etc.)
    Input in XML file (modify for new searches). Good for loop testing
    No loggins (Logback)
    Coded for Java 8, straightforward with few comments
*/
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

@SuppressWarnings("ClassWithoutLogger")
public class FlightSearch {
    public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException {
        try {
            Map<String, String> ds = DatabaseHelper.getProperties("lastminute.properties");
            //System.out.println(ds);
            List<Flight> flights = DatabaseHelper.getFlights(ds);
            //flights.stream().forEach(System.out::println);
            Map<String, String> airports = DatabaseHelper.getAirports(ds);
            //airports.entrySet().stream().forEach(System.out::println);
            Map<String, InfantPrice> infantPrices = DatabaseHelper.getInfantPrices(ds);
            //infantPrices.entrySet().stream().forEach(System.out::println);
            List<SearchData> searches = DatabaseHelper.getSearches(ds);
            System.out.println(searches);
        } catch(IOException | NullPointerException ex) {
            // Some logging profiling ex...
            System.out.println("Error accessing datasources ... So long");
            System.exit(-1);
        }
    }
}
