package com.hongri.sqlite.bean;

import java.io.Serializable;

/**
 * @author zhongyao
 * @date 2019/4/16
 */

public class LikeInfo implements Serializable{

    public String likeStatus;
    public String liekCount;

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

    public String getLiekCount() {
        return liekCount;
    }

    public void setLiekCount(String liekCount) {
        this.liekCount = liekCount;
    }

}
