package com.sgb.library.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.sgb.library.R;


/**
 * Created by panda on 16/7/19 下午4:30.
 */
public class DefaultBannerItem extends BaseBannerItem {

    public DefaultBannerItem(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View item = mInflater.inflate(R.layout.item_default_banner, null);
        ImageView imageView = (ImageView) item.findViewById(R.id.iv_banner);
        bindItemView(item, imageView);
        return item;
    }
}
