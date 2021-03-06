/**
 * File generated by Magnet rest2mobile 1.1 - May 22, 2015 10:30:30 PM
 * @see {@link http://developer.magnet.com}
 */

package com.jim.apps.twitter.models;

import java.util.List;

/**
 * Generated from json example
{
  "coordinates" : [ [ [ -122.367781, 37.835727 ], [ -122.234185, 37.835727 ], [ -122.234185, 37.905824 ], [ -122.367781, 37.905824 ] ] ],
  "type" : "Polygon"
}

 */

public class Bounding_box {

  
  private List<Coordinates> coordinates;

  
  private String type;

  public List<Coordinates> getCoordinates() {
    return coordinates;
  }
  public String getType() {
    return type;
  }

  /**
  * Builder for Bounding_box
  **/
  public static class Bounding_boxBuilder {
    private Bounding_box toBuild = new Bounding_box();

    public Bounding_boxBuilder() {
    }

    public Bounding_box build() {
      return toBuild;
    }

    public Bounding_boxBuilder coordinates(List<Coordinates> value) {
      toBuild.coordinates = value;
      return this;
    }
    public Bounding_boxBuilder type(String value) {
      toBuild.type = value;
      return this;
    }
  }
}
