package com.lastminute.flight_search;

import java.util.Date;

@SuppressWarnings("ClassWithoutLogger")
public class SearchData {
    private int id;
    private String airport_origin;
    private String airport_dest;
    private Date departure_date;
    private int adults;
    private int childs;
    private int infants;

    public SearchData(int id, String airport_origin, String airport_dest, Date departure_date, int adults, int childs, int infants) {
        this.id = id;
        this.airport_origin = airport_origin;
        this.airport_dest = airport_dest;
        this.departure_date = departure_date;
        this.adults = adults;
        this.childs = childs;
        this.infants = infants;
    }
    public SearchData() {}
    public String getAirport_origin() {
        return airport_origin;
    }
    public void setAirport_origin(String airport_origin) {
        this.airport_origin = airport_origin;
    }
    public String getAirport_dest() {
        return airport_dest;
    }
    public void setAirport_dest(String airport_dest) {
        this.airport_dest = airport_dest;
    }
    public Date getDeparture_date() {
        return departure_date;
    }
    public void setDeparture_date(Date departure_date) {
        this.departure_date = departure_date;
    }
    public int getAdults() {
        return adults;
    }
    public void setAdults(int adults) {
        this.adults = adults;
    }
    public int getChilds() {
        return childs;
    }
    public void setChilds(int childs) {
        this.childs = childs;
    }
    public int getInfants() {
        return infants;
    }
    public void setInfants(int infants) {
        this.infants = infants;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "SearchData{" + "id=" + id + ", airport_origin=" + airport_origin +
            ", airport_dest=" + airport_dest + ", departure_date=" + departure_date +
            ", adults=" + adults + ", childs=" + childs + ", infants=" + infants + "}\n";
    }

}