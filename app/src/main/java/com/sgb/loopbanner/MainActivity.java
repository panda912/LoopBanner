package com.sgb.loopbanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sgb.library.banner.BannerLayout;
import com.sgb.library.banner.BaseBannerItem;
import com.sgb.library.banner.DefaultBannerItem;
import com.sgb.loopbanner.entity.BannerDataListEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BannerLayout mBannerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBannerLayout = (BannerLayout) findViewById(R.id.banner);
        bindData();
    }

    private void bindData() {

        String jsonStr = null;

        try {
            InputStream is = getAssets().open("gankio.json");
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            is.close();
            jsonStr = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BannerDataListEntity dataEntity = new Gson().fromJson(jsonStr, new TypeToken<BannerDataListEntity>() {
        }.getType());

        ArrayList<BaseBannerItem> list = new ArrayList<>();
        for (int i = 0; i < dataEntity.results.size(); i++) {
            DefaultBannerItem item = new DefaultBannerItem(this);
            item.image(dataEntity.results.get(i).url)
                    .config(Bitmap.Config.RGB_565)
                    .placeHolder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher);
            list.add(item);
        }

        mBannerLayout.addItemList(list);
        mBannerLayout.hideIndicator(list.size() == 1);
        mBannerLayout.startLoop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBannerLayout != null) {
            mBannerLayout.startLoop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBannerLayout != null) {
            mBannerLayout.stopLoop();
        }
    }
}
