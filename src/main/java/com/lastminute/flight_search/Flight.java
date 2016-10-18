package com.lastminute.flight_search;

@SuppressWarnings("ClassWithoutLogger")
public class Flight {
    private String origin;
    private String destination;
    private String airline;
    private float basePrice;

    public Flight(String origin, String destination, String airline, float basePrice)
            throws IllegalArgumentException {
        if(basePrice > 0 && argsOk(origin, destination, airline)) {
            this.origin = origin.trim();
            this.destination = destination.trim();
            this.airline = airline.trim();
            this.basePrice = basePrice;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getAirline() {
        return airline;
    }

    public float getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return "Flight{" + "origin=" + origin + ", destination=" + destination +
                ", airline=" + airline + ", basePrice=" + basePrice + '}';
    }

    public static final boolean argsOk(String ... args) {
        for(String arg : args) {
            if(arg == null || arg.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}