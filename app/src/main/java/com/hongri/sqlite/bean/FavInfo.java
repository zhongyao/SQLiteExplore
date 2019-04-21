package com.hongri.sqlite.bean;

import java.io.Serializable;

/**
 *
 * @author zhongyao
 * @date 2019/4/16
 */

public class FavInfo implements Serializable{
    public String favStatus;

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }
}
