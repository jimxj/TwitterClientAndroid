/**
 * File generated by Magnet rest2mobile 1.1 - May 22, 2015 10:30:30 PM
 * @see {@link http://developer.magnet.com}
 */

package com.codepath.apps.twitter.model.beans;


/**
 * Generated from json example
{
  "name" : "Berkeley",
  "country_code" : "US",
  "country" : "UnitedStates",
  "attributes" : {
  },
  "url" : "http://api.twitter.com/1/geo/id/5ef5b7f391e30aff.json",
  "id" : "5ef5b7f391e30aff",
  "bounding_box" : {
    "coordinates" : [ [ [ -122.367781, 37.835727 ], [ -122.234185, 37.835727 ], [ -122.234185, 37.905824 ], [ -122.367781, 37.905824 ] ] ],
    "type" : "Polygon"
  },
  "full_name" : "Berkeley,CA",
  "place_type" : "city"
}

TODO (generated by Magnet r2m) : Property attributes is ignored because the value is empty object({}) in the json example.

 */

public class Place {

  
  private Bounding_box bounding_box;

  
  private String country;

  
  private String country_code;

  
  private String full_name;

  
  private String id;

  
  private String name;

  
  private String place_type;

  
  private String url;

  public Bounding_box getBounding_box() {
    return bounding_box;
  }
  public String getCountry() {
    return country;
  }
  public String getCountry_code() {
    return country_code;
  }
  public String getFull_name() {
    return full_name;
  }
  public String getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public String getPlace_type() {
    return place_type;
  }
  public String getUrl() {
    return url;
  }

  /**
  * Builder for Place
  **/
  public static class PlaceBuilder {
    private Place toBuild = new Place();

    public PlaceBuilder() {
    }

    public Place build() {
      return toBuild;
    }

    public PlaceBuilder bounding_box(Bounding_box value) {
      toBuild.bounding_box = value;
      return this;
    }
    public PlaceBuilder country(String value) {
      toBuild.country = value;
      return this;
    }
    public PlaceBuilder country_code(String value) {
      toBuild.country_code = value;
      return this;
    }
    public PlaceBuilder full_name(String value) {
      toBuild.full_name = value;
      return this;
    }
    public PlaceBuilder id(String value) {
      toBuild.id = value;
      return this;
    }
    public PlaceBuilder name(String value) {
      toBuild.name = value;
      return this;
    }
    public PlaceBuilder place_type(String value) {
      toBuild.place_type = value;
      return this;
    }
    public PlaceBuilder url(String value) {
      toBuild.url = value;
      return this;
    }
  }
}
