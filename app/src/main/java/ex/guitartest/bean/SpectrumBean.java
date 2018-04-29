package ex.guitartest.bean;

import java.io.Serializable;

/**
 * Created by qyxlx on 2018/3/15 0006.
 */

public class SpectrumBean implements Serializable {
    private String name;
    private String pinyin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public SpectrumBean(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
    }
}
