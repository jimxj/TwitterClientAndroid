/**
 * File generated by Magnet rest2mobile 1.1 - May 24, 2015 11:23:42 PM
 * @see {@link http://developer.magnet.com}
 */

package com.jim.apps.twitter.models;


/**
 * Generated from json example
{
  "medium" : {
    "w" : 600,
    "h" : 399,
    "resize" : "fit"
  },
  "thumb" : {
    "w" : 150,
    "h" : 150,
    "resize" : "crop"
  },
  "small" : {
    "w" : 340,
    "h" : 226,
    "resize" : "fit"
  },
  "large" : {
    "w" : 800,
    "h" : 532,
    "resize" : "fit"
  ...

 */

public class Sizes {

  
  private Large large;

  
  private Medium medium;

  
  private Small small;

  
  private Thumb thumb;

  public Large getLarge() {
    return large;
  }
  public Medium getMedium() {
    return medium;
  }
  public Small getSmall() {
    return small;
  }
  public Thumb getThumb() {
    return thumb;
  }

  /**
  * Builder for Sizes
  **/
  public static class SizesBuilder {
    private Sizes toBuild = new Sizes();

    public SizesBuilder() {
    }

    public Sizes build() {
      return toBuild;
    }

    public SizesBuilder large(Large value) {
      toBuild.large = value;
      return this;
    }
    public SizesBuilder medium(Medium value) {
      toBuild.medium = value;
      return this;
    }
    public SizesBuilder small(Small value) {
      toBuild.small = value;
      return this;
    }
    public SizesBuilder thumb(Thumb value) {
      toBuild.thumb = value;
      return this;
    }
  }
}
