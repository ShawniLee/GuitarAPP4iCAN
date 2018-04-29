package ex.guitartest.util;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Pattern;

/**
 * author zaaach on 2016/1/28.
 */
public class PinyinUtils {
    /**
     * 获取拼音的首字母（大写）
     *
     * @param pinyin
     * @return
     */
    public static String getFirstLetter(final String pinyin) {
        if (TextUtils.isEmpty(pinyin)) return "定位";
        String c = pinyin.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else if ("0".equals(c)) {
            return "定位";
        } else if ("1".equals(c)) {
            return "热门";
        }
        return "定位";
    }

        // 将汉字转换为全拼
        public static String getPingYin(String src) {
            char[] t1 = null;
            t1 = src.toCharArray();
            String[] t2 = new String[t1.length];
            HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

            t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            t3.setVCharType(HanyuPinyinVCharType.WITH_V);
            String t4 = "";
            int t0 = t1.length;
            try {
                for (int i = 0; i < t0; i++) {
                    // 判断是否为汉字字符
                    if (Character.toString(t1[i]).matches(
                            "[\\u4E00-\\u9FA5]+")) {
                        t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                        t4 += t2[0];
                    } else
                        t4 += Character.toString(t1[i]);
                }
                // System.out.println(t4);
                return t4;
            } catch (BadHanyuPinyinOutputFormatCombination e1) {
                e1.printStackTrace();
            }
            return t4;
        }

}
