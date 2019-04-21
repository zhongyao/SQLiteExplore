package com.hongri.sqlite.bean;

import java.io.Serializable;

/**
 * @author zhongyao
 * @date 2019/4/16
 */

public class ArticleInfo implements Serializable{

    private LikeInfo likeInfo;
    private FavInfo favInfo;

    private String secretId;
    private String secretAge;

    public LikeInfo getLikeInfo() {
        return likeInfo;
    }

    public void setLikeInfo(LikeInfo likeInfo) {
        this.likeInfo = likeInfo;
    }

    public FavInfo getFavInfo() {
        return favInfo;
    }

    public void setFavInfo(FavInfo favInfo) {
        this.favInfo = favInfo;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretAge() {
        return secretAge;
    }

    public void setSecretAge(String secretAge) {
        this.secretAge = secretAge;
    }
}
