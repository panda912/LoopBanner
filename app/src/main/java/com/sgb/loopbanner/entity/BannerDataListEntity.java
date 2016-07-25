package com.sgb.loopbanner.entity;

import java.util.List;

/**
 * Created by panda on 16/7/22 上午9:13.
 */
public class BannerDataListEntity {

    public int count;
    public boolean error;
    public List<ResultsObj> results;

    public static class ResultsObj {
        public String desc;
        public String ganhuo_id;
        public String publishedAt;
        public String readability;
        public String type;
        public String url;
        public String who;
    }
}
