package com.lastminute.flight_search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings({"ClassWithoutLogger", "UtilityClassWithoutPrivateConstructor"})
public final class DatabaseHelper {
    // Reading properties file into a map
    public static Map<String, String> getProperties(String filename) throws IOException {
        Predicate<Map.Entry<Object, Object>> propertyOk = p ->
            !p.getKey().toString().isEmpty() && !p.getValue().toString().isEmpty();

        Properties props = new Properties();
        try (InputStream input = new FileInputStream(filename)) {
            props.load(input);
            return props.entrySet().stream()
                .filter(propertyOk)
                .collect(Collectors.toMap(
                    e -> e.getKey().toString(),
                    e -> e.getValue().toString()
            ));
        }
    }

    // Processing flights CSV file into a list of Flight
    public static List<Flight> getFlights(Map<String, String> ds) throws IOException {
        // Function to convert csv line into Flight
        Function<String, Optional<Flight>> mapToFlight = (line) -> {
            String[] param = line.split(",");
            try {
                // Worthy double checking...
                return Optional.ofNullable(Flight.argsOk(param[0],param[1],param[2]) ?
                        new Flight(
                            param[0],
                            param[1],
                            param[2],
                            Float.parseFloat(param[3])
                        ) : null);
            } catch(ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
                // Some logging profiling ex...
                return Optional.empty();
            }
        };

        try (InputStream is = new FileInputStream(new File(ds.get("flights.datasource")));
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            List<Flight> flights = br.lines()
                .skip(1)
                .map(mapToFlight)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
            return flights;
        }
    }

    private static boolean argOk(String arg) {
        return arg != null && !arg.trim().isEmpty();
    }

    // Processing airports CSV file into a map<iata,city>
    public static Map<String, String> getAirports(Map<String, String> ds) throws IOException {
        // Function to convert csv line into Airport entry
        Function<String, Optional<Map.Entry<String, String>>> mapToAirport = (line) -> {
            String[] param = line.split(",");
            try {
                return Optional.ofNullable(argOk(param[0]) && argOk(param[1]) ?
                        new AbstractMap.SimpleEntry<>(
                            param[0].trim(),
                            param[1].trim()
                        ) : null);
            } catch(ArrayIndexOutOfBoundsException ex) {
                // Some logging profiling ex...
                return Optional.empty();
            }
        };

        try (InputStream is = new FileInputStream(new File(ds.get("airports.datasource")));
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            Map<String, String> airports = br.lines()
                    .skip(1)
                    .map(mapToAirport)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()
                    ));
            return airports;
        }
    }

    // Processing airlines' infant prices map<iata,<name,price>>
    public static Map<String, InfantPrice> getInfantPrices(Map<String, String> ds) throws IOException {
        // Function to convert csv line into InfantPrice entry
        Function<String, Optional<Map.Entry<String, InfantPrice>>> mapToInfantPrice = (line) -> {
            String[] param = line.split(",");
            try {
                return Optional.ofNullable(InfantPrice.argOk(param[0]) ?
                        new AbstractMap.SimpleEntry<>(
                            param[0].trim(),
                            new InfantPrice(param[1], Float.parseFloat(param[2]))
                        ) : null);
            } catch(ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
                // Some logging profiling ex...
                return Optional.empty();
            }
        };

        try (InputStream is = new FileInputStream(new File(ds.get("infant.prices.datasource")));
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            Map<String,InfantPrice> infantPrices = br.lines()
                    .skip(1)
                    .map(mapToInfantPrice)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()
                    ));
            return infantPrices;
        }
    }

    // Searches processing from XML input file (modify for new searches)
    public static List<SearchData> getSearches(Map<String, String> ds) throws
            ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(ds.get("search.input")));
            List<SearchData> searches = new ArrayList<>();
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
	               Node n = nodeList.item(i);
                   if (n.getNodeType() == Node.ELEMENT_NODE) {
	                    Element elem = (Element) n;
                        int ID = Integer.parseUnsignedInt(
                            n.getAttributes().getNamedItem("id").getNodeValue()
                        );
                        String airport_origin = elem.getElementsByTagName("airport_origin")
                                .item(0).getChildNodes().item(0).getNodeValue();
                        String airport_dest = elem.getElementsByTagName("airport_dest")
                                .item(0).getChildNodes().item(0).getNodeValue();
                        String str_departure_date = elem.getElementsByTagName("departure_date")
                                .item(0).getChildNodes().item(0).getNodeValue();
                        Date departure_date = null;
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate localDate = LocalDate.parse(str_departure_date, formatter);
                            departure_date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        } catch(DateTimeParseException ex) {
                            // Some logging profiling ex
                            continue;
                        }
                        int adults = Integer.parseUnsignedInt(
                            elem.getElementsByTagName("adults")
                            .item(0).getChildNodes().item(0).getNodeValue()
                        );
                        int childs = Integer.parseUnsignedInt(
                            elem.getElementsByTagName("childs")
                            .item(0).getChildNodes().item(0).getNodeValue()
                        );
                        int infants = Integer.parseUnsignedInt(
                            elem.getElementsByTagName("infants")
                            .item(0).getChildNodes().item(0).getNodeValue()
                        );
                        searches.add(new SearchData(
                            ID, airport_origin, airport_dest,
                            departure_date, adults, childs, infants
                        ));
                   }
            }
            return searches;
    }
}
