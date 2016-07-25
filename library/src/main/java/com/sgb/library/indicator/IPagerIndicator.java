package com.sgb.library.indicator;

/**
 * Created by panda on 16/7/20 上午9:29.
 */
public interface IPagerIndicator {

    void setTotalNum(int totalNum);

    int getTotalNum();

    void setCurIndex(int index);

    int getCurIndex();
}
