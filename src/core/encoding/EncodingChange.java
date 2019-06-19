package core.encoding;

import java.io.UnsupportedEncodingException;

public class EncodingChange
{

	
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	
	public static String str2Hex(String str)
	{

		char[] chars = DIGITS_UPPER;
		StringBuilder hexBuilder = new StringBuilder("");
		try
		{
			str = new String(str.getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 指定编码方式
		System.out.println(str);
		byte[] originalBytes = str.getBytes();
		int bit;

		for (int i = 0; i < originalBytes.length; i++)
		{
			// System.out.println("originalBytes:" + originalBytes[i]);
			bit = (originalBytes[i] & 0xf0) >> 4;
			// System.out.println(bit);
			hexBuilder.append(chars[bit]);
			bit = originalBytes[i] & 0x0f;
			hexBuilder.append(chars[bit]);
			hexBuilder.append(' ');
		}
		return hexBuilder.toString().trim();
	}

	
	public static String hex2String(String hex)
	{
		String[] hexs = hex.split(" ");
		byte[] origin = new byte[hexs.length];
		int index = 0;
		for (String temp : hexs)
		{
			origin[index++] = (byte) (indexOf(temp.charAt(0))*16 + indexOf(temp.charAt(1)));
		}
		try
		{
			return new String(origin, "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private static int indexOf(char temp)
	{
		for (int i = 0; i < DIGITS_UPPER.length; i++)
		{
			if (temp == DIGITS_UPPER[i])
				return i;
		}
		return -1;
	}

	public static void main(String[] args)
	{
		System.out.println(EncodingChange.str2Hex("我爱你"));
		System.out.println(EncodingChange.hex2String("E6 88 91 E7 88 B1 E4 BD A0"));
	}

}
