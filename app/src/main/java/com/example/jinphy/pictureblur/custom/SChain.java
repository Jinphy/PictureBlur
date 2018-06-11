package com.example.jinphy.pictureblur.custom;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.example.jinphy.pictureblur.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DESC: 该类专治各种TextView特殊效果的不服
 * 使用入口: {@link SChain#with(Object)}
 *
 *
 * <h1>使用说明：</h1><p>{@code
 *              SChain.with("你好！")
 *                      .colorForText(0x333333)
 *                      .append("这里是")
 *                      .colorForText(0x000000,0,3)
 *                      .append("道乐科技有限公司")
 *                      .underLine(0,4)
 *                      .colorForText(0x343434,"道乐科技",2,1,5)
 *                      .colorForBackground(0xffffff,0,4)
 *                      .sizeRelative(1.5f)
 *                      .append(",")
 *                      .append("点我有惊喜哦！")
 *                      .colorForBackground(0x677876)
 *                      .onClick(value -> {
 *                          // “点我有惊喜哦！” 这段文本的点击事件
 *                      })
 *                      .into(textView);// 或者你可以调用make()来获取上面设置好的结果
 *
 * }</p>
 *              注：更多用法请查看源码！
 *
 *
 *
 *
 *
 * Created by Jinphy on 2017/12/8.
 */

public class SChain {

    private SpannableStringBuilder builder;

    private SpannableString current;

    private int highlightColor=-1;

    private SChain(String text) {
        builder = new SpannableStringBuilder();
        if (text != null) {
            current = new SpannableString(text);
        }
    }

    /**
     * DESC: 用指定的创建一个SpannableBuilder对象
     * <p>
     * <p>
     * Created by Jinphy, on 2017/12/8, at 9:25
     */
    public static SChain with(@Nullable Object text) {
        if (text == null) {
            return new SChain(null);
        }
        return new SChain(text.toString());
    }

    /**
     * DESC: 拼接SpannableString
     *  拼接后的后续操作都只对当前新拼接的text有效，即只在新拼接的text中操作
     *
     *
     *  注意：如果{@code text} ==null 则将忽略该text，并且在下次调用该函数传入非null的{@code text}之前，
     *      所有接下来的操作都将会被忽略而不会影响之前的字符串的设置，总之传入的参数如果是null，则忽略该字符串即
     *      对该字符串接下来的设置
     *  
     *  @param text 要拼接的字符串
     * Created by Jinphy, on 2017/12/8, at 14:55
     */
    public SChain append(@Nullable Object text) {
        if (current != null) {
            builder.append(current);
        }
        if (text != null) {
            current = new SpannableString(text.toString());
        } else {
            current = null;
        }
        return this;
    }

    /**
     * DESC: 把所有的SpannableString设置到目标TextView中
     * 
     * Created by Jinphy, on 2017/12/8, at 15:00
     */
    public TextView into(TextView target) {
        if (target == null) {
            return target;
        }
        if (current != null) {
            builder.append(current);
        }
        target.setText(builder);
        // 不是设置这个没有点击事件响应
        target.setMovementMethod(LinkMovementMethod.getInstance());
        if (highlightColor < 0) {
            return target;
        }
        target.setHighlightColor(highlightColor);
        return target;
    }


    /**
     * DESC: 返回设置结果，可以用来设置TextView
     *
     * Created by Jinphy, on 2017/12/11, at 11:24
     */
    public CharSequence make() {
        if (current != null) {
            builder.append(current);
        }
        return builder;
    }


    //----------------------------------------------------------------------------------------------

    /**
     * DESC: 设置点击时的高亮颜色
     * Created by Jinphy, on 2017/12/8, at 16:38
     */
    public SChain highlightColor(int color) {
        this.highlightColor = color;
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString添加字体颜色
     * @param color 要设置的颜色
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForText(int color){
        return colorForText(color, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString添加字体颜色
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * @param color 要设置的颜色
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForText(int color, int start, int end){
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString添加指定文本的字体颜色
     *
     * @param color 要设置的颜色
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForText(int color, String where, int... which) {
        this.call(where,which,entry -> colorForText(color, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString添加指定文本的字体颜色
     *
     * @param color 要设置的颜色
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForText(int color, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> colorForText(color, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString添加背景颜色
     * @param color 要设置的颜色
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForBackground(int color){
        return colorForBackground(color, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString添加背景颜色
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * @param color 要设置的颜色
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForBackground(int color, int start, int end){
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        BackgroundColorSpan span = new BackgroundColorSpan(color);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString添加指定文本的背景颜色
     *
     * @param color 要设置的颜色
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForBackground(int color, String where, int... which) {
        this.call(where,which,entry -> colorForBackground(color, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString添加指定文本的背景颜色
     *
     * @param color 要设置的颜色
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain colorForBackground(int color, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> colorForBackground(color, entry.start, entry.end));
        }
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 设置字体大小
     * @param rate 目标字体大小与原TextView字体大小的比例，rate = 1 时大小不变
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain sizeRelative(float rate) {
        return sizeRelative(rate, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置字体大小
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * @param rate 目标字体大小与原TextView字体大小的比例，rate = 1 时大小不变
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain sizeRelative(float rate, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        RelativeSizeSpan span = new RelativeSizeSpan(rate);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的字体大小
     *
     * @param rate 目标字体大小与原TextView字体大小的比例，rate = 1 时大小不变
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain sizeRelative(float rate, String where, int... which) {
        this.call(where,which,entry -> sizeRelative(rate, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的字体大小
     *
     * @param rate 目标字体大小与原TextView字体大小的比例，rate = 1 时大小不变
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain sizeRelative(float rate, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> sizeRelative(rate, entry.start, entry.end));
        }
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 设置字体大小
     * @param sp 目标字体大小,单位是sp
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain sizeSp(int sp) {
        return sizeSp(sp, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置字体大小
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * @param sp 目标字体大小,单位是sp
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain sizeSp(int sp, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(sp,true);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的字体大小
     *
     * @param sp 目标字体大小,单位是sp
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain sizeSp(int sp, String where, int... which) {
        this.call(where,which,entry -> sizeSp(sp, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的字体大小
     *
     * @param sp 目标字体大小,单位是sp
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain sizeSp(int sp, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> sizeSp(sp, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置删除线
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain deleteLine() {
        return deleteLine(0,current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置删除线
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain deleteLine(int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        StrikethroughSpan span = new StrikethroughSpan();
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的删除线
     *
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain deleteLine(String where, int... which) {
        this.call(where,which,entry -> deleteLine(entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的删除线
     *
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain deleteLine(String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> deleteLine(entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置下划线
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain underLine() {
        return underLine(0,current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置下划线
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain underLine(int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        UnderlineSpan span = new UnderlineSpan();
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的下划线
     *
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain underLine(String where, int... which) {
        this.call(where,which,entry -> underLine(entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的下划线
     *
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain underLine(String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> underLine(entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置上标
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain superscript() {
        return superscript(1f,0,current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置上标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain superscript(float sizeRate) {
        return superscript(sizeRate,0,current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置上标
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain superscript(int start, int end) {
        superscript(1f, start, end);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置上标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain superscript(float sizeRate, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        SuperscriptSpan superscriptSpan = new SuperscriptSpan ();
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(sizeRate);
        current.setSpan(superscriptSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        current.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的上标
     *
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain superscript(String where, int... which) {
        this.call(where,which,entry -> superscript(entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的上标
     *
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain superscript(String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> superscript(entry.start, entry.end));
        }
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的上标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain superscript(float sizeRate, String where, int... which) {
        this.call(where,which,entry -> superscript(sizeRate, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的上标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain superscript(float sizeRate, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> superscript(sizeRate, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置下标
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain subscript() {
        return subscript(1f,0,current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置下标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain subscript(float sizeRate) {
        return subscript(sizeRate,0,current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置下标
     *
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain subscript(int start, int end) {
        subscript(1f, start, end);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置下标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain subscript(float sizeRate, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        SubscriptSpan subscriptSpan = new SubscriptSpan ();
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(sizeRate);
        current.setSpan(subscriptSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        current.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的下标
     *
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain subscript(String where, int... which) {
        this.call(where,which,entry -> subscript(1f, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的下标
     *
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain subscript(String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> subscript(1f, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的下标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain subscript(float sizeRate, String where, int... which) {
        this.call(where,which,entry -> subscript(sizeRate, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的下标
     *
     * @param sizeRate 上标的字体大小，与原TextView的比值，{@code sizeRate = 1} 时，大小不变
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain subscript(float sizeRate, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> subscript(sizeRate, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置粗体、斜体等风格
     *
     * @param style 字体风格{@link Typeface#BOLD}（粗体） etc.
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain style(int... style) {
        return style(0, current.length(), style);
    }

    /**
     * DESC: 为当前的current SpannableString 设置粗体、斜体等风格
     *
     * @param style 字体风格{@link Typeface#ITALIC}（斜体） etc.
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain style(int start, int end, int... style) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        if (style.length == 0) {
            ObjectUtils.throwRuntime("you must set at least one style value");
        }
        for (int i : style) {
            StyleSpan span = new StyleSpan(i);
            current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的粗体、斜体等风格
     *
     * @param style 字体风格{@link Typeface#ITALIC}（斜体） etc.
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain style(int style, String where, int... which) {
        this.call(where,which,entry -> style(entry.start, entry.end, new int[]{style}));
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的粗体、斜体等风格
     *
     * @param style 字体风格{@link Typeface#ITALIC}（斜体） etc.
     * @param wheres 指定的字符串数组
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain style(int style, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where,which,entry -> style(entry.start, entry.end, new int[]{style}));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置字体
     *
     * @param family 字体 ,例如："monospace", "serif", and "sans-serif",或者：{@link Typeface#SANS_SERIF}等。
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain typeface(String family) {
        return typeface(family, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置字体
     *
     * @param family 字体 ,例如："monospace", "serif", and "sans-serif".或者：{@link Typeface#SANS_SERIF}等。
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain typeface(String family, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        TypefaceSpan span = new TypefaceSpan(family);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的字体
     *
     * @param family 字体 ,例如："monospace", "serif", and "sans-serif".或者：{@link Typeface#SANS_SERIF}等。
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain typeface(String family, String where, int... which) {
        this.call(where, which, entry -> typeface(family, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的字体
     *
     * @param family 字体 ,例如："monospace", "serif", and "sans-serif".或者：{@link Typeface#SANS_SERIF}等。
     * @param wheres 指定的字符串数组
     * @param which  指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     *               Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain typeface(String family, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where, which, entry -> typeface(family, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置图片
     *
     * @param drawable
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain image(Drawable drawable) {
        return image(drawable, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置图片
     *
     * @param drawable
     * @param start 开始下标，包含该下标的字符
     * @param end 结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain image(Drawable drawable, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        ImageSpan span = new ImageSpan(drawable);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的图片
     *
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain image(Drawable drawable, String where, int... which) {
        this.call(where, which, entry -> image(drawable, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的图片
     *
     * @param wheres 指定的字符串数组
     * @param which  指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     *               Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain image(Drawable drawable, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where, which, entry -> image(drawable, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置图片
     *
     * @param resId 图片资源Id
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain image(@DrawableRes int resId) {
        return image(resId, null, 0, current.length());
    }



    /**
     * DESC: 为当前的current SpannableString 添加指定文本的图片
     *
     * @param resId  图片id
     * @param wheres 指定的字符串数组
     * @param which  指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     *               Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain image(@DrawableRes int resId, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where, which, entry -> image(resId, null, entry.start, entry.end));
        }
        return this;
    }



    /**
     * DESC: 为当前的current SpannableString 设置点击事件
     *
     * @param click 点击事件的回调
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain click(@NonNull Consumer<String> click) {
        ObjectUtils.requireNonNull(click, "callback cannot be null!");
        return click(click, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置点击事件
     *
     * @param click 点击事件的回调
     * @param start 开始下标，包含该下标的字符
     * @param end   结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain click(@NonNull Consumer<String>click, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        ObjectUtils.requireNonNull(click,"callback cannot be null!");
        String text = current.toString().substring(start, end);
        ClickSpan span = new ClickSpan(text, click);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的点击事件
     *
     * @param click 点击事件的回调
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain click(@NonNull Consumer<String>click, String where, int... which) {
        this.call(where, which, entry -> click(click, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的点击事件
     *
     * @param click  点击事件的回调
     * @param wheres 指定的字符串数组
     * @param which  指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     *               Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain click(@NonNull Consumer<String>click, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where, which, entry -> click(click, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置点击跳转url
     *
     * @param url
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain url(@NonNull String url) {
        ObjectUtils.requireNonNull(url, "url cannot be null!");
        return url(url, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置点击跳转url
     *
     * @param url
     * @param start 开始下标，包含该下标的字符
     * @param end   结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain url(@NonNull String url, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        ObjectUtils.requireNonNull(url,"url cannot be null!");
        URLSpan  span = new URLSpan(url);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的点击跳转url
     *
     * @param url
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain url(@NonNull String url, String where, int... which) {
        this.call(where, which, entry -> url(url, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的点击跳转url
     *
     * @param url
     * @param wheres 指定的字符串数组
     * @param which  指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     *               Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain url(@NonNull String url, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where, which, entry -> url(url, entry.start, entry.end));
        }
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 设置缩放X轴方向的大小
     *
     * @param rate 放大比例
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain scaleX(float rate) {
        return scaleX(rate, 0, current.length());
    }

    /**
     * DESC: 为当前的current SpannableString 设置缩放x轴方向的大小
     *
     * @param rate 放大比例
     * @param start 开始下标，包含该下标的字符
     * @param end   结束下标，不包含该下标的字符
     * Created by Jinphy, on 2017/12/8, at 15:12
     */
    public SChain scaleX(float rate, int start, int end) {
        if (start >= end || current == null) {
            return this;
        }
        start = ensureLowIndex(start);
        end = ensureHighIndex(end);
        ScaleXSpan span = new ScaleXSpan(rate);
        current.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * DESC: 为当前的current SpannableString 添加指定文本的缩放x轴方向的大小
     *
     * @param rate 放大比例
     * @param where 指定的字符串
     * @param which 指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     * Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain scaleX(float rate, String where, int... which) {
        this.call(where, which, entry -> scaleX(rate, entry.start, entry.end));
        return this;
    }


    /**
     * DESC: 为当前的current SpannableString 添加指定文本的缩放x轴方向的大小
     *
     * @param rate   放大比例
     * @param wheres 指定的字符串数组
     * @param which  指定第几个匹配的字符串中生效,不指定则所有匹配的字符串都生效,which 从0 开始表示第一个，以此类推
     *               Created by Jinphy, on 2017/12/8, at 15:04
     */
    public SChain scaleX(float rate, String[] wheres, int... which) {
        if (wheres == null || wheres.length == 0) {
            return this;
        }
        for (String where : wheres) {
            this.call(where, which, entry -> scaleX(rate, entry.start, entry.end));
        }
        return this;
    }

    //==============================================================================================
    /*
     * DESC: 确保下标不越界
     * Created by Jinphy, on 2017/12/8, at 17:47
     */
    private int ensureLowIndex(int low) {
        if (low < 0) {
            low = 0;
        }
        return low;
    }

    /*
     * DESC: 确保上标不越界
     * Created by Jinphy, on 2017/12/8, at 17:47
     */
    private int ensureHighIndex(int high) {
        if (high > current.toString().length()) {
            high = current.toString().length();
        }
        return high;
    }

    private boolean in(int from, int to, int value) {
        return value >= from && value < to;
    }

    /*
     * DESC: 该函数是所有通过字符串匹配设置各种TextView属性的入口函数
     * Created by Jinphy, on 2017/12/8, at 19:14
     */
    private void call(String where,int[] which, Consumer<Entry> callback) {
        // 查找所有与where匹配的current中的子串
        Entry[] entries = Entry.findAll(current.toString(),where);
        if (entries == null) {
            return;// 找不到则直接返回
        }
        if (which == null || which.length == 0) {
            // 没有指定要在哪个匹配的字符串中生效则默认所有的匹配结果都生效
            for (Entry entry : entries) {
                callback.accept(entry);
            }
        } else {
            // 对指定的匹配结果生效
            for (int i : which) {
                // 判断指定的匹配结果的索引是否合法
                if (in(0, entries.length, i)) {
                    // 合法则设置
                    callback.accept(entries[i]);
                }
            }
        }
    }

    //===============内部类 =========================================================================

    /**
     * DESC: 回调接口，
     *
     * 1、用于所有通过字符串匹配来设置TextView 属性的方法的回调
     * {@link SChain#call(String, int[], Consumer)}
     *
     * 2、点击事件的回调
     * {@link SChain#click(Consumer)}
     * {@link SChain#click(Consumer, int, int)}
     * {@link SChain#click(Consumer, String, int...)}
     * {@link SChain#click(Consumer, String[], int...)}
     *
     * Created by Jinphy, on 2017/12/8, at 19:21
     */
    public interface Consumer<T>{


        /**
         * DESC: 回调函数
         *
         * @param value 当作为点击事件的回调接口是，value是点击事件中的那段文本
         * Created by Jinphy, on 2017/12/8, at 17:49
         */
        void accept(T value);
    }

    /**
     * DESC: 辅助实体类，保存某个需要设置属性的子串的位置信息
     * Created by Jinphy, on 2017/12/8, at 19:50
     */
    private static class Entry{
        String text;
        int start = -1;
        int end = -1;

        public Entry() {

        }

        public Entry(String text, int start, int end) {
            this.text = text;
            this.start = start;
            this.end = end;
        }


        /**
         * DESC: 找出当前字符串{@code current} 中所有与 where 匹配的子串
         * Created by Jinphy, on 2017/12/8, at 17:59
         */
        public static Entry[] findAll(String source, String where) {
            if (TextUtils.isEmpty(where) ||  !source.contains(where)) {
                return null;
            }
            int len = where.length();
            List<Entry> entries = new ArrayList<>();
            Entry entry;

            int start = 0;
            while ((start = source.indexOf(where,start)) >= 0) {
                entry = new Entry();
                entry.start = start;
                entry.end = start + len;
                entry.text = source.substring(entry.start, entry.end);
                entries.add(entry);
                start = entry.end;
                if (start >= source.length()) {
                    break;
                }
            }
            return entries.toArray(new Entry[entries.size()]);

        }

    }

    /**
     * DESC: 可点击的Span
     * Created by Jinphy, on 2017/12/8, at 17:49
     */
    private static class ClickSpan extends ClickableSpan{
        private String text;
        private Consumer<String> click;

        ClickSpan(String text, Consumer<String> onClick) {
            this.text = text;
            this.click = onClick;
        }

        @Override
        public void onClick(View widget) {
            if (this.click != null) {
                this.click.accept(text);
            }
        }

        /**
         * Makes the text underlined and in the link color.
         */
        @Override
        public void updateDrawState(TextPaint ds) {}
    }

}





















