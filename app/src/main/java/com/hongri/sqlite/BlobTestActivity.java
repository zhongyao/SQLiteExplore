package com.hongri.sqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.hongri.sqlite.base.Services;
import com.hongri.sqlite.bean.ArticleInfo;
import com.hongri.sqlite.bean.FavInfo;
import com.hongri.sqlite.bean.LikeInfo;
import com.hongri.sqlite.bean.PersonInfo;
import com.hongri.sqlite.dao.DataCallback;
import com.hongri.sqlite.dao.IDaoService;
import com.hongri.sqlite.util.Logger;

/**
 * @author hongri
 */
public class BlobTestActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText personIdEdit, nameEdit, genderEdit;
    private Button btnSave, btnGet, btnBlobGet, btnBlobSave, btnDBUpdate;

    private String secretId = "888";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blob_test);

        personIdEdit = findViewById(R.id.personIdEdit);
        nameEdit = findViewById(R.id.nameEdit);
        genderEdit = findViewById(R.id.genderEdit);
        btnSave = findViewById(R.id.btnSave);
        btnGet = findViewById(R.id.btnGet);
        btnBlobSave = findViewById(R.id.btnBlobSave);
        btnBlobGet = findViewById(R.id.btnBlobGet);
        btnDBUpdate = findViewById(R.id.btnDBUpdate);

        btnSave.setOnClickListener(this);
        btnGet.setOnClickListener(this);
        btnBlobSave.setOnClickListener(this);
        btnBlobGet.setOnClickListener(this);
        btnDBUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String personId = personIdEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String gender = genderEdit.getText().toString();
        switch (v.getId()) {
            case R.id.btnSave:
                Services.get(IDaoService.class).save(personId, name, gender, new DataCallback<Boolean>() {
                    @Override
                    public void onDataCallback(Boolean result) {
                        Logger.d("save--:" + result);
                    }
                });
                break;
            case R.id.btnGet:
                Services.get(IDaoService.class).query(personId, new DataCallback<PersonInfo>() {
                    @Override
                    public void onDataCallback(PersonInfo data) {
                        Logger.d("query--:" + data.toString());
                    }
                });
                break;
            case R.id.btnBlobSave:
                ArticleInfo articleInfo = new ArticleInfo();
                FavInfo favInfo = new FavInfo();
                LikeInfo likeInfo = new LikeInfo();

                articleInfo.setSecretId(secretId);
                articleInfo.setSecretAge("18");

                favInfo.setFavStatus("1");

                likeInfo.setLikeStatus("0");
                likeInfo.setLiekCount("200");

                articleInfo.setFavInfo(favInfo);
                articleInfo.setLikeInfo(likeInfo);

                Services.get(IDaoService.class).saveBlob(personId, articleInfo, new DataCallback<Boolean>() {
                    @Override
                    public void onDataCallback(Boolean result) {
                        Logger.d("save--Blob:" + result);
                    }
                });
                break;
            case R.id.btnBlobGet:
                Services.get(IDaoService.class).queryBlob(personId, new DataCallback<ArticleInfo>() {
                    @Override
                    public void onDataCallback(ArticleInfo data) {
                        if (data == null) {
                            Logger.d("query--Blob: null");
                            return;
                        }
                        Logger.d("query--Blob:" + data.toString());
                    }

                });
                break;

            case R.id.btnDBUpdate:
                //数据库迁移

                Services.get(IDaoService.class).upgradeDB();
                break;
            default:
                break;
        }
    }
}
