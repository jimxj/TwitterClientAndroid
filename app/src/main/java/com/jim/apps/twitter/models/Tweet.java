/**
 * File generated by Magnet rest2mobile 1.1 - May 22, 2015 10:30:30 PM
 * @see {@link http://developer.magnet.com}
 */

package com.jim.apps.twitter.models;


/**
 * Generated from json example
{
  "coordinates" : null,
  "truncated" : false,
  "created_at" : "TueAug2821:16:23+00002012",
  "favorited" : false,
  "id_str" : "240558470661799936",
  "in_reply_to_user_id_str" : "123",
  "entities" : {
    "urls" : [ ],
    "hashtags" : [ "a" ],
    "user_mentions" : [ ]
  },
  "text" : "justanothertest",
  "contributors" : null,
  "id" : 240558470661799936,
  "retweet_count" : 0,
  "in_reply_to_status_id_str" : "123",
  "geo" : null,
  "retweeted" : false,
  "in_reply_to_user_id" : null,
    ...

TODO (generated by Magnet r2m) : Property in_reply_to_status_id is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property in_reply_to_screen_name is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property in_reply_to_user_id is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property contributors is ignored because the value is null in the json example.

 */

public class Tweet {

  
  private Coordinates coordinates;

  
  private String created_at;

  
  private Entities entities;

  
  private Boolean favorited;

  
  private Geo geo;

  
  private Long id;

  
  private String id_str;

  
  private String in_reply_to_status_id_str;

  
  private String in_reply_to_user_id_str;

  
  //private Place place;

  
  private Boolean possibly_sensitive;

  
  private Integer retweet_count;

  private Integer favorite_count;

  
  private Boolean retweeted;

  
  private String source;

  
  private String text;

  
  private Boolean truncated;

  
  private User user;

  public Coordinates getCoordinates() {
    return coordinates;
  }
  public String getCreated_at() {
    return created_at;
  }
  public Entities getEntities() {
    return entities;
  }
  public Boolean getFavorited() {
    return favorited;
  }

  public Integer getFavorite_count() {
    return favorite_count;
  }

  public Geo getGeo() {
    return geo;
  }
  public Long getId() {
    return id;
  }
  public String getId_str() {
    return id_str;
  }
  public String getIn_reply_to_status_id_str() {
    return in_reply_to_status_id_str;
  }
  public String getIn_reply_to_user_id_str() {
    return in_reply_to_user_id_str;
  }
//  public Place getPlace() {
//    return place;
//  }
  public Boolean getPossibly_sensitive() {
    return possibly_sensitive;
  }
  public Integer getRetweet_count() {
    return retweet_count;
  }
  public Boolean getRetweeted() {
    return retweeted;
  }
  public String getSource() {
    return source;
  }
  public String getText() {
    return text;
  }
  public Boolean getTruncated() {
    return truncated;
  }
  public User getUser() {
    return user;
  }

  @Override
  public boolean equals(Object o) {
    if(null == o) {
      return false;
    }

    Tweet theOther = (Tweet) o;
    return theOther.getId().equals(id);
  }

  /**
  * Builder for TimelineResult
  **/
  public static class TimelineResultBuilder {
    private Tweet toBuild = new Tweet();

    public TimelineResultBuilder() {
    }

    public Tweet build() {
      return toBuild;
    }

    public TimelineResultBuilder coordinates(Coordinates value) {
      toBuild.coordinates = value;
      return this;
    }
    public TimelineResultBuilder created_at(String value) {
      toBuild.created_at = value;
      return this;
    }
//    public TimelineResultBuilder entities(Entities value) {
//      toBuild.entities = value;
//      return this;
//    }
    public TimelineResultBuilder favorited(Boolean value) {
      toBuild.favorited = value;
      return this;
    }
    public TimelineResultBuilder setFavorite_count(Integer favourites_count) {
      toBuild.favorite_count = favourites_count;
      return this;
    }
    public TimelineResultBuilder geo(Geo value) {
      toBuild.geo = value;
      return this;
    }
    public TimelineResultBuilder id(Long value) {
      toBuild.id = value;
      return this;
    }
    public TimelineResultBuilder id_str(String value) {
      toBuild.id_str = value;
      return this;
    }
    public TimelineResultBuilder in_reply_to_status_id_str(String value) {
      toBuild.in_reply_to_status_id_str = value;
      return this;
    }
    public TimelineResultBuilder in_reply_to_user_id_str(String value) {
      toBuild.in_reply_to_user_id_str = value;
      return this;
    }
//    public TimelineResultBuilder place(Place value) {
//      toBuild.place = value;
//      return this;
//    }
    public TimelineResultBuilder possibly_sensitive(Boolean value) {
      toBuild.possibly_sensitive = value;
      return this;
    }
    public TimelineResultBuilder retweet_count(Integer value) {
      toBuild.retweet_count = value;
      return this;
    }
    public TimelineResultBuilder retweeted(Boolean value) {
      toBuild.retweeted = value;
      return this;
    }
    public TimelineResultBuilder source(String value) {
      toBuild.source = value;
      return this;
    }
    public TimelineResultBuilder text(String value) {
      toBuild.text = value;
      return this;
    }
    public TimelineResultBuilder truncated(Boolean value) {
      toBuild.truncated = value;
      return this;
    }
    public TimelineResultBuilder user(User value) {
      toBuild.user = value;
      return this;
    }
  }
}
