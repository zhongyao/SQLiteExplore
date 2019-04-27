package com.hongri.sqlite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * @author hongri
 * 参考：https://blog.csdn.net/codeeer/article/details/30237597/
 *
 * SQLite的特点：
 * 1、轻量级
 * 2、不需要"安装"
 * 3、单一文件
 * 4、跨平台性
 * 5、弱类型字段
 * 6、开源
 *
 * SQLite常用的五种存储类型：
 * 1、NULL
 * 2、INTEGER 整形
 * 3、REAL 浮点型
 * 4、TEXT 按照文本字符串来存储
 * 5、BLOB 值是BLOB数据块，以输入的数据格式进行存储，即如何输入就如何存储
 * 6、VARCHAR(n) 长度不固定且其最大长度为n的字符串，n不能超过4000
 * 7、CHAR(n)长度为n的字符串，n不能超过254
 *
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn1.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                intent = new Intent();
                intent.setClass(MainActivity.this, BlobTestActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
