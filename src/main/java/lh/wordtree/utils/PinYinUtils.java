package lh.wordtree.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public interface PinYinUtils {
    Log log = LogFactory.get();

    /**
     * 转换为每个汉字对应拼音首字母字符串
     *
     * @param pinYinStr 需转换的汉字
     * @return 拼音字符串
     */
    static String getPinYinPrefix(String pinYinStr) {
        return getPinYinPrefix(pinYinStr, false);
    }

    /**
     * 转换为每个汉字对应拼音首字母字符串
     *
     * @param pinYinStr 需转换的汉字
     * @param uppercase true 大写，false 默认
     * @return 拼音字符串
     */
    static String getPinYinPrefix(String pinYinStr, boolean uppercase) {
        if (StrUtil.isEmpty(pinYinStr)) {
            return pinYinStr;
        }
        try {
            String tempStr = PinyinHelper.getShortPinyin(pinYinStr);
            if (uppercase) {
                return tempStr.toUpperCase();
            } else {
                return tempStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("汉字转拼音首字母异常：" + e);
            return pinYinStr;
        }
    }

    /**
     * 获取汉字全品音
     *
     * @param pinYinStr 要转换的字符串
     * @return 汉语拼音
     */
    static String getPinYin(String pinYinStr) {
        return getPinYin(pinYinStr, StrUtil.EMPTY, PinyinFormat.WITHOUT_TONE, false);
    }

    /**
     * 获取汉字全品音
     *
     * @param pinYinStr 要转换的字符串
     * @param uppercase true 大写，false 默认
     * @return 汉语拼音
     */
    static String getPinYin(String pinYinStr, boolean uppercase) {
        return getPinYin(pinYinStr, StrUtil.EMPTY, PinyinFormat.WITHOUT_TONE, uppercase);
    }

    /**
     * 获取汉字全品音
     *
     * @param pinYinStr 要转换的字符串
     * @param separator 分隔符
     * @param uppercase true 大写，false 默认
     * @return 汉语拼音
     */
    static String getPinYin(String pinYinStr, String separator, boolean uppercase) {
        return getPinYin(pinYinStr, separator, PinyinFormat.WITHOUT_TONE, uppercase);
    }

    /**
     * 获取汉字全品音
     *
     * @param pinYinStr    要转换的字符串
     * @param separator    分隔符
     * @param pinyinFormat 拼音格式 WITH_TONE_MARK（带声调）,WITHOUT_TONE（不带声调） WITH_TONE_NUMBER（带声调数字）
     * @param uppercase    true 大写，false 默认
     * @return 汉语拼音
     */
    static String getPinYin(String pinYinStr, String separator, PinyinFormat pinyinFormat, boolean uppercase) {
        if (StrUtil.isEmpty(pinYinStr)) {
            return pinYinStr;
        }
        try {
            String tempStr = PinyinHelper.convertToPinyinString(pinYinStr, separator, pinyinFormat);
            if (uppercase) {
                return tempStr.toUpperCase();
            } else {
                return tempStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("汉字转拼音首字母异常：" + e);
            return pinYinStr;
        }
    }

    /**
     * 判断一个汉字是否为多音字
     *
     * @param c 汉字
     * @return 判断结果，是汉字返回true，否则返回false
     */
    static boolean hasMultiPinyin(char c) {
        return PinyinHelper.hasMultiPinyin(c);
    }

    /**
     * 将单个汉字转换成带声调格式的拼音
     *
     * @param c 需要转换成拼音的汉字
     * @return 字符串的拼音
     */
    static String[] convertToPinyinArray(char c) {
        return PinyinHelper.convertToPinyinArray(c);
    }

    /**
     * 将单个汉字转换为相应格式的拼音
     *
     * @param c            需要转换成拼音的汉字
     * @param pinyinFormat 拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，WITH_TONE_MARK--带声调
     * @return 汉字的拼音
     */
    static String[] convertToPinyinArray(char c, PinyinFormat pinyinFormat) {
        return PinyinHelper.convertToPinyinArray(c, pinyinFormat);
    }

    /**
     * 繁体字转为简体字
     *
     * @param wordStr 字符串
     * @return 转换后的简体字符串
     */
    static String convertToSimplifiedChinese(String wordStr) {
        if (StrUtil.isEmpty(wordStr)) {
            return wordStr;
        }
        return ChineseHelper.convertToSimplifiedChinese(wordStr);
    }

    /**
     * 简体字转换为繁体字
     *
     * @param wordStr 字符串
     * @return 转换后的简体字符串
     */
    static String convertToTraditionalChinese(String wordStr) {
        if (StrUtil.isEmpty(wordStr)) {
            return wordStr;
        }
        return ChineseHelper.convertToTraditionalChinese(wordStr);
    }

    /**
     * 判断word是否为简体中文
     *
     * @param word
     * @return true 是，false 否
     */
    static boolean isChinese(char word) {
        return ChineseHelper.isChinese(word);
    }

    /**
     * 判断word是否为繁体字
     *
     * @param word 字符
     * @return true 是，false 否
     */
    static boolean isTraditionalChinese(char word) {
        return ChineseHelper.isTraditionalChinese(word);
    }

    /**
     * 判断字符串是否包含中文
     *
     * @param wordStr 要判断的字符串
     * @return true 包含，false 不包含
     */
    static boolean containsChinese(String wordStr) {
        if (StrUtil.isEmpty(wordStr)) {
            return false;
        }
        return ChineseHelper.containsChinese(wordStr);
    }
}
