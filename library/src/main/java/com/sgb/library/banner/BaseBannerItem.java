package com.sgb.library.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by panda on 16/7/19 下午3:44.
 */
public abstract class BaseBannerItem {

    private Context mContext;
    public LayoutInflater mInflater;

    private String mUrl;
    private int mErrorResId;
    private int mPlaceHolder;
    private Bitmap.Config mConfig;

    public BaseBannerItem(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public abstract View getView();

    public BaseBannerItem image(String url) {
        mUrl = url;
        return this;
    }

    public BaseBannerItem error(int resId) {
        mErrorResId = resId;
        return this;
    }

    public BaseBannerItem placeHolder(int resId) {
        mPlaceHolder = resId;
        return this;
    }

    public BaseBannerItem config(Bitmap.Config config) {
        mConfig = config;
        return this;
    }

    public void bindItemView(View itemView, ImageView imageView) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBannerItemClickListener != null) {
                    mOnBannerItemClickListener.onItemClick();
                }
            }
        });

        if (imageView == null) {
            return;
        }

        RequestCreator creator;

        if (!TextUtils.isEmpty(mUrl)) {
            creator = Picasso.with(mContext).load(mUrl);
        } else {
            return;
        }

        if (mPlaceHolder != 0) {
            creator.placeholder(mPlaceHolder);
        }

        if (mErrorResId != 0) {
            creator.error(mErrorResId);
        }

        if (mConfig != null) {
            creator.config(mConfig);
        }

        creator.fit().into(imageView);
    }

    private OnBannerItemClickListener mOnBannerItemClickListener;

    public void setOnItemClickListener(OnBannerItemClickListener listener) {
        mOnBannerItemClickListener = listener;
    }

    public interface OnBannerItemClickListener {
        void onItemClick();
    }

}
