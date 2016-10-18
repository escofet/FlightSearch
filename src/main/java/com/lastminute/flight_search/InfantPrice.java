
package com.lastminute.flight_search;

@SuppressWarnings("ClassWithoutLogger")
public class InfantPrice {
    private String name;
    private float price;

    public InfantPrice(String name, float price) throws IllegalArgumentException {
        if(price > 0 && argOk(name)) {
            this.name = name.trim();
            this.price = price;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "InfantPrice{" + "name=" + getName() + ", price=" + getPrice() + '}';
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public static final boolean argOk(String arg) {
        return arg != null && !arg.trim().isEmpty();
    }
}