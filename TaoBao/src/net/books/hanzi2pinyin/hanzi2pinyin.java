/**
 * 
 */
package net.books.hanzi2pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author ZHAO
 * 汉字转化为拼音
 *
 */
public class hanzi2pinyin
{
	HanyuPinyinOutputFormat PINYIN_FORMAT = new HanyuPinyinOutputFormat();
	public hanzi2pinyin()
	{
		PINYIN_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		PINYIN_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
	}
	public  String toPinyin(String input)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++)
		{
			char c = input.charAt(i);
			if (c <= 255)
			{
				sb.append(c);
			} else
			{
				String pinyin = null;
				try
				{
					String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, PINYIN_FORMAT);
					pinyin = pinyinArray[0];
				} catch (BadHanyuPinyinOutputFormatCombination e)
				{
//					logger.error(e.getMessage(), e);
					e.printStackTrace();
				} catch (NullPointerException e)
				{
					// 如果是日文，可能抛出该异常
				}
				if (pinyin != null)
				{
					sb.append(pinyin);
				}
			}
		}
		return new String(sb.toString().replace(" ",""));
	}
}
