package com.hongri.sqlite.dao;

import com.hongri.sqlite.base.IService;
import com.hongri.sqlite.bean.ArticleInfo;
import com.hongri.sqlite.bean.PersonInfo;

/**
 * @author zhongyao
 * @date 2019/4/16
 */

public interface IDaoService extends IService {
    /**
     * db基本常用类型存储
     *
     * @param personId
     * @param name
     * @param gender
     * @param callback
     */
    void save(String personId, String name, String gender, DataCallback<Boolean> callback);

    /**
     * BLOB类型存储
     * param personId
     *
     * @param blobInfo
     * @param callback
     */
    void saveBlob(String PersonId, ArticleInfo blobInfo, DataCallback<Boolean> callback);

    /**
     * 基本常用类型查询
     *
     * @param personId
     * @param callback
     * @return
     */
    void query(String personId, DataCallback<PersonInfo> callback);

    /**
     * BLOB类型查询
     *
     * @param personId
     * @param callback
     * @return
     */
    void queryBlob(String personId, DataCallback<ArticleInfo> callback);

    /**
     * 数据库更新迁移(新旧表名字不一样)：
     * 1、创建新表
     * 2、将旧表数据迁移至新表
     * 3、删除旧表
     */
    void upgradeDB();
}
