package com.sgb.library.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by panda on 16/7/21 上午10:43.
 */
public class BannerPagerAdapter extends PagerAdapter {

    private ArrayList<BaseBannerItem> mList;

    public BannerPagerAdapter() {
        mList = new ArrayList<>();
    }

    public void addItemList(ArrayList<BaseBannerItem> list) {
        mList.clear();

        if (list.size() == 1) {
            mList.addAll(list);
        } else {
            mList.add(list.get(list.size() - 1));
            mList.addAll(list);
            mList.add(list.get(0));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseBannerItem item = mList.get(position);
        View view = item.getView();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}