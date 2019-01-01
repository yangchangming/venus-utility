/*
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd.
 */

package venus.portal.hotwords.util;

import venus.portal.hotwords.model.HotWords;
import venus.portal.util.PinYinStringUtil;

import java.util.Comparator;

/**
 * 对HotWords类排序用的比较器
 * User: chengliang
 * Date: 13-7-31
 * Time: 下午4:28
 */
public class HotWordsComparator implements Comparator<HotWords> {

    public int compare (HotWords h1, HotWords h2) {
        String h1Letter = PinYinStringUtil.getAllFirstLetter(h1.getName());
        String h2Letter = PinYinStringUtil.getAllFirstLetter(h2.getName());

        return h1Letter.compareTo(h2Letter);
    }
}
