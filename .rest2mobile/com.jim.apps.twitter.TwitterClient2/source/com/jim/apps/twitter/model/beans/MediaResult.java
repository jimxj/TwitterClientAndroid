/**
 * File generated by Magnet rest2mobile 1.1 - May 24, 2015 11:23:42 PM
 * @see {@link http://developer.magnet.com}
 */

package com.jim.apps.twitter.model.beans;

import java.util.List;

/**
 * Generated from json example
{
  "id" : 266031293949698048,
  "id_str" : "266031293949698048",
  "indices" : [ 17, 37 ],
  "media_url" : "http://pbs.twimg.com/media/A7EiDWcCYAAZT1D.jpg",
  "media_url_https" : "https://pbs.twimg.com/media/A7EiDWcCYAAZT1D.jpg",
  "url" : "http://t.co/bAJE6Vom",
  "display_url" : "pic.twitter.com/bAJE6Vom",
  "expanded_url" : "http://twitter.com/BarackObama/status/266031293945503744/photo/1",
  "type" : "photo",
  "sizes" : {
    "medium" : {
      "w" : 600,
      "h" : 399,
      "resize" : "fit"
    },
    "thumb" : {
      "w" : 150,
      "h" : 150,
      "resize" : "crop"
    ...

 */

public class MediaResult {

  
  private String display_url;

  
  private String expanded_url;

  
  private Long id;

  
  private String id_str;

  
  private List<java.lang.Integer> indices;

  
  private String media_url;

  
  private String media_url_https;

  
  private Sizes sizes;

  
  private String type;

  
  private String url;

  public String getDisplay_url() {
    return display_url;
  }
  public String getExpanded_url() {
    return expanded_url;
  }
  public Long getId() {
    return id;
  }
  public String getId_str() {
    return id_str;
  }
  public List<java.lang.Integer> getIndices() {
    return indices;
  }
  public String getMedia_url() {
    return media_url;
  }
  public String getMedia_url_https() {
    return media_url_https;
  }
  public Sizes getSizes() {
    return sizes;
  }
  public String getType() {
    return type;
  }
  public String getUrl() {
    return url;
  }

  /**
  * Builder for MediaResult
  **/
  public static class MediaResultBuilder {
    private MediaResult toBuild = new MediaResult();

    public MediaResultBuilder() {
    }

    public MediaResult build() {
      return toBuild;
    }

    public MediaResultBuilder display_url(String value) {
      toBuild.display_url = value;
      return this;
    }
    public MediaResultBuilder expanded_url(String value) {
      toBuild.expanded_url = value;
      return this;
    }
    public MediaResultBuilder id(Long value) {
      toBuild.id = value;
      return this;
    }
    public MediaResultBuilder id_str(String value) {
      toBuild.id_str = value;
      return this;
    }
    public MediaResultBuilder indices(List<java.lang.Integer> value) {
      toBuild.indices = value;
      return this;
    }
    public MediaResultBuilder media_url(String value) {
      toBuild.media_url = value;
      return this;
    }
    public MediaResultBuilder media_url_https(String value) {
      toBuild.media_url_https = value;
      return this;
    }
    public MediaResultBuilder sizes(Sizes value) {
      toBuild.sizes = value;
      return this;
    }
    public MediaResultBuilder type(String value) {
      toBuild.type = value;
      return this;
    }
    public MediaResultBuilder url(String value) {
      toBuild.url = value;
      return this;
    }
  }
}
