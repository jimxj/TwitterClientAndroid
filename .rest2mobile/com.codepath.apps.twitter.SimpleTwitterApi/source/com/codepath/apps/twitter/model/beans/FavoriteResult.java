/**
 * File generated by Magnet rest2mobile 1.1 - May 22, 2015 10:30:30 PM
 * @see {@link http://developer.magnet.com}
 */

package com.codepath.apps.twitter.model.beans;


/**
 * Generated from json example
{
  "contributors" : null,
  "coordinates" : null,
  "created_at" : "WedSep0500:07:01+00002012",
  "entities" : {
    "hashtags" : [ ],
    "urls" : [ ],
    "user_mentions" : [ ]
  },
  "favorited" : false,
  "geo" : null,
  "id" : 243138128959913986,
  "id_str" : "243138128959913986",
  "in_reply_to_screen_name" : null,
  "in_reply_to_status_id" : null,
  "in_reply_to_status_id_str" : null,
  "in_reply_to_user_id" : null,
  "in_reply_to_user_id_str" : null,
  "place" : null,
  "retweet_count" : 0,
    ...

TODO (generated by Magnet r2m) : Property in_reply_to_status_id is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property in_reply_to_screen_name is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property in_reply_to_user_id is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property place is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property in_reply_to_user_id_str is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property in_reply_to_status_id_str is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property geo is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property coordinates is ignored because the value is null in the json example.
TODO (generated by Magnet r2m) : Property contributors is ignored because the value is null in the json example.

 */

public class FavoriteResult {

  
  private String created_at;

  
  private Entities entities;

  
  private Boolean favorited;

  
  private Long id;

  
  private String id_str;

  
  private Integer retweet_count;

  
  private Boolean retweeted;

  
  private String source;

  
  private String text;

  
  private Boolean truncated;

  
  private User user;

  public String getCreated_at() {
    return created_at;
  }
  public Entities getEntities() {
    return entities;
  }
  public Boolean getFavorited() {
    return favorited;
  }
  public Long getId() {
    return id;
  }
  public String getId_str() {
    return id_str;
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

  /**
  * Builder for FavoriteResult
  **/
  public static class FavoriteResultBuilder {
    private FavoriteResult toBuild = new FavoriteResult();

    public FavoriteResultBuilder() {
    }

    public FavoriteResult build() {
      return toBuild;
    }

    public FavoriteResultBuilder created_at(String value) {
      toBuild.created_at = value;
      return this;
    }
    public FavoriteResultBuilder entities(Entities value) {
      toBuild.entities = value;
      return this;
    }
    public FavoriteResultBuilder favorited(Boolean value) {
      toBuild.favorited = value;
      return this;
    }
    public FavoriteResultBuilder id(Long value) {
      toBuild.id = value;
      return this;
    }
    public FavoriteResultBuilder id_str(String value) {
      toBuild.id_str = value;
      return this;
    }
    public FavoriteResultBuilder retweet_count(Integer value) {
      toBuild.retweet_count = value;
      return this;
    }
    public FavoriteResultBuilder retweeted(Boolean value) {
      toBuild.retweeted = value;
      return this;
    }
    public FavoriteResultBuilder source(String value) {
      toBuild.source = value;
      return this;
    }
    public FavoriteResultBuilder text(String value) {
      toBuild.text = value;
      return this;
    }
    public FavoriteResultBuilder truncated(Boolean value) {
      toBuild.truncated = value;
      return this;
    }
    public FavoriteResultBuilder user(User value) {
      toBuild.user = value;
      return this;
    }
  }
}