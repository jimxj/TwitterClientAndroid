/**
 * File generated by Magnet rest2mobile 1.1 - May 24, 2015 11:23:42 PM
 * @see {@link http://developer.magnet.com}
 */

package com.jim.apps.twitter.model.beans;


/**
 * Generated from json example
{
  "w" : 150,
  "h" : 150,
  "resize" : "crop"
}

 */

public class Thumb {

  
  private Integer h;

  
  private String resize;

  
  private Integer w;

  public Integer getH() {
    return h;
  }
  public String getResize() {
    return resize;
  }
  public Integer getW() {
    return w;
  }

  /**
  * Builder for Thumb
  **/
  public static class ThumbBuilder {
    private Thumb toBuild = new Thumb();

    public ThumbBuilder() {
    }

    public Thumb build() {
      return toBuild;
    }

    public ThumbBuilder h(Integer value) {
      toBuild.h = value;
      return this;
    }
    public ThumbBuilder resize(String value) {
      toBuild.resize = value;
      return this;
    }
    public ThumbBuilder w(Integer value) {
      toBuild.w = value;
      return this;
    }
  }
}
