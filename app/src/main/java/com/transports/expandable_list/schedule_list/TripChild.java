package com.transports.expandable_list.schedule_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.transports.data.Stop;

import java.util.Objects;

public class TripChild implements Parcelable {

    private String companyName;
    private String departureDate;
    private String departureHour;
    private Stop origin;
    private Stop destination;
    private String arrivingHour;
    private double price;

    public TripChild(String companyName, String departureDate, String departureHour, String arrivingHour,
                     Stop origin, Stop arrivingLocation, double price) {
        this.companyName = companyName;
        this.departureDate = departureDate;
        this.departureHour = departureHour;
        this.origin = origin;
        this.destination = arrivingLocation;
        this.arrivingHour = arrivingHour;
        this.price = price;
    }

    //temporary...
    public TripChild(String companyName, Stop destination) {
        this.companyName = companyName;
        this.destination = destination;
    }

    

    public static final Creator<TripChild> CREATOR = new Creator<TripChild>() {
        @Override
        public TripChild createFromParcel(Parcel in) {
            return new TripChild(in);
        }

        @Override
        public TripChild[] newArray(int size) {
            return new TripChild[size];
        }
    };

    protected TripChild(Parcel in) {
        companyName = in.readString();
        departureDate = in.readString();
        departureHour = in.readString();
        arrivingHour = in.readString();
        price = in.readDouble();
        origin = in.readParcelable(Stop.class.getClassLoader());
        destination = in.readParcelable(Stop.class.getClassLoader());
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureHour() {
        return departureHour;
    }

    public void setDepartureHour(String departureHour) {
        this.departureHour = departureHour;
    }

    public Stop getOrigin() {
        return origin;
    }

    public void setOrigin(Stop origin) {
        this.origin = origin;
    }

    public Stop getDestination() {
        return destination;
    }

    public void setDestination(Stop destination) {
        this.destination = destination;
    }

    public String getArrivingHour() {
        return arrivingHour;
    }

    public void setArrivingHour(String arrivingHour) {
        this.arrivingHour = arrivingHour;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSchedule(){
        return this.departureHour+"-"+this.arrivingHour;
    }

    public String getTrip(){
        return this.origin.getStopName()+"->"+this.destination.getStopName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companyName);
        dest.writeString(departureDate);
        dest.writeString(departureHour);
        dest.writeParcelable(origin, flags);
        dest.writeParcelable(destination, flags);
        dest.writeString(arrivingHour);
        dest.writeDouble(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripChild tripChild = (TripChild) o;
        return Double.compare(tripChild.price, price) == 0 &&
                Objects.equals(companyName, tripChild.companyName) &&
                Objects.equals(departureDate, tripChild.departureDate) &&
                Objects.equals(departureHour, tripChild.departureHour) &&
                Objects.equals(origin, tripChild.origin) &&
                Objects.equals(destination, tripChild.destination) &&
                Objects.equals(arrivingHour, tripChild.arrivingHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, departureDate, departureHour, origin, destination, arrivingHour, price);
    }

    @Override
    public String toString() {
        return "TripChild{" +
                "companyName='" + companyName + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", departureHour='" + departureHour + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", arrivingHour='" + arrivingHour + '\'' +
                ", price=" + price +
                '}';
    }
}
