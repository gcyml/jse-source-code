/*
 * Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang;

import java.lang.annotation.Native;
import java.math.*;


/**
 * {@code Long} 类在对象中包装了基本类型 {@code Long} 的值。
 * 每个 {@code Long} 类型的对象都包含一个 {@code Long} 类型的字段。
 *
 * <p> 此外，该类提供了多个方法，可以将 {@code long} 转换为 {@code String}，
 * 将 {@code String} 转换为 {@code long}，
 * 除此之外，还提供了其他一些处理 {@code long} 时有用的常量和方法。
 *
 * <p>实现注意事项："bit twiddling" 方法（如 {@link #highestOneBit(long) highestOneBit} 和
 * {@link #numberOfTrailingZeros(long) numberOfTrailingZeros} ）的
 * 实现基于 Henry S. Warren 和 Jr. 撰写的 <i>Hacker's Delight</i> (Addison Wesley, 2002) 一书中的资料。
 *
 * @author  Lee Boynton
 * @author  Arthur van Hoff
 * @author  Josh Bloch
 * @author  Joseph D. Darcy
 * @since   JDK1.0
 */
public final class Long extends Number implements Comparable<Long> {
    /**
     * 保持 {@code long} 类型的最小值的常量，该值为 -2<sup>63</sup>。
     */
    @Native public static final long MIN_VALUE = 0x8000000000000000L;

    /**
     * 保持 {@code long} 类型的最大值的常量，该值为 2<sup>63</sup>-1。
     */
    @Native public static final long MAX_VALUE = 0x7fffffffffffffffL;

    /**
     * 表示基本类型 {@code long} 的 {@code Class} 实例。
     *
     * @since   JDK1.1
     */
    @SuppressWarnings("unchecked")
    public static final Class<Long>     TYPE = (Class<Long>) Class.getPrimitiveClass("long");

    /**
     * 返回在使用第二个参数指定的基数时第一个参数的字符串表示形式。
     *
     * <p>如果该基数小于 {@code Character.MIN_RADIX}，
     * 或大于 {@code Character.MAX_RADIX}，则使用基数 {@code 10}。
     *
     * <p>如果第一个参数是负数，则结果的第一个元素是 ASCII 字符的减号 {@code '-'} ({@code '\u005Cu002d'})。
     * 如果第一个参数非负，则结果中不会出现符号字符。
     *
     * <p>结果的其余字符表示第一个参数的大小。
     * 如果大小为零，则用单个零字符 {@code '0'} 表示它 ({@code '\u005Cu0030'})；
     * 否则大小表示形式中的第一个字符将不是零字符。以下 ASCII 字符均被用作数字：
     *
     * <blockquote>
     *   {@code 0123456789abcdefghijklmnopqrstuvwxyz}
     * </blockquote>
     *
     * 这些是从 {@code '\u005Cu0030'} 到 {@code '\u005Cu0039'} 和
     * 从 {@code '\u005Cu0061'} 到 {@code '\u005Cu007a'} 的字符。
     * 如果 {@code radix} 是 <var>N</var>，
     * 则这些字符的第一个 <var>N</var> 用作显示顺序中基数 <var>N</var> 的数字。
     * 因此，该数字的十六进制（基数 16）表示形式为 {@code 0123456789abcdef}。
     * 如果需要使用大写字母，则可以在结果上调用 {@link java.lang.String#toUpperCase()} 方法：
     *
     * <blockquote>
     *  {@code Long.toString(n, 16).toUpperCase()}
     * </blockquote>
     *
     * @param   i       要转换为字符串的 {@code long}。
     * @param   radix   将在字符串表示形式中使用的基数。
     * @return  指定基数中参数的字符串表示形式。
     * @see     java.lang.Character#MAX_RADIX
     * @see     java.lang.Character#MIN_RADIX
     */
    public static String toString(long i, int radix) {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            radix = 10;
        if (radix == 10)
            return toString(i);
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (i < 0);

        if (!negative) {
            i = -i;
        }

        while (i <= -radix) {
            buf[charPos--] = Integer.digits[(int)(-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = Integer.digits[(int)(-i)];

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (65 - charPos));
    }

    /**
     *  以第二个参数指定进制的无符号整数形式返回第一个整数参数的字符串表示形式。
     *
     * <p>如果基数小于 {@code Character.MIN_RADIX} 或
     * 大于 {@code Character.MAX_RADIX}，则基数为 {@code 10}。
     *
     * <p>注意，由于第一个参数被视为无符号值，所以不打印前导符号字符。
     *
     * <p>如果无符号数的大小值为零，
     * 则用一个零字符 {@code '0'} ({@code '\u005Cu0030'}) 表示它；
     * 否则，无符号数大小的表示形式中的第一个字符将不是零字符。
     *
     * <p>基数和作为数字使用的字符的行为与当中 {@link #toString(long, int) toString} 相同。
     *
     * @param   i       要转换成无符号字符串的整数。
     * @param   radix   字符串表达形式的基数。
     * @return  参数在指定基数中的无符号字符串表示形式。
     * @see     #toString(long, int)
     * @since 1.8
     */
    public static String toUnsignedString(long i, int radix) {
        if (i >= 0)
            return toString(i, radix);
        else {
            switch (radix) {
            case 2:
                return toBinaryString(i);

            case 4:
                return toUnsignedString0(i, 2);

            case 8:
                return toOctalString(i);

            case 10:
                /*
                 * We can get the effect of an unsigned division by 10
                 * on a long value by first shifting right, yielding a
                 * positive value, and then dividing by 5.  This
                 * allows the last digit and preceding digits to be
                 * isolated more quickly than by an initial conversion
                 * to BigInteger.
                 */
                long quot = (i >>> 1) / 5;
                long rem = i - quot * 10;
                return toString(quot) + rem;

            case 16:
                return toHexString(i);

            case 32:
                return toUnsignedString0(i, 5);

            default:
                return toUnsignedBigInteger(i).toString(radix);
            }
        }
    }

    /**
     * 返回一个BigInteger，它等于参数的无符号值。
     */
    private static BigInteger toUnsignedBigInteger(long i) {
        if (i >= 0L)
            return BigInteger.valueOf(i);
        else {
            int upper = (int) (i >>> 32);
            int lower = (int) i;

            // return (upper << 32) + lower
            return (BigInteger.valueOf(Integer.toUnsignedLong(upper))).shiftLeft(32).
                add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
        }
    }

    /**
     * 以十六进制无符号整数形式返回 {@code long} 参数的字符串表示形式。
     *
     * <p>如果参数为负，则无符号 {@code long} 值为该参数加上 2<sup>64</sup>；
     * 否则，它等于该参数。此值将被转换为不带附加前导 {@code 0} 的十六进制（基数&nbsp;16）ASCII 数字字符串。
     *
     * <p>参数的值可以从字符串 {@code s} 中通过调用 {@link
     * Long#parseUnsignedLong(String, int) Long.parseUnsignedLong(s,
     * 16)} 得到。
     *
     * <p>如果无符号大小为零，则该数字由单个零字符 {@code '0'} 表示 ({@code '\u005Cu0030'})；
     * 否则，无符号大小表示形式中的第一个字符将不是零字符。
     * 下列字符都被用作十六进制数字：
     *
     * <blockquote>
     *  {@code 0123456789abcdef}
     * </blockquote>
     *
     * 这些是从 {@code '\u005Cu0030'} 到 {@code '\u005Cu0039'} 和
     * 从 {@code '\u005Cu0061'} 到 {@code '\u005Cu0066'} 的字符。
     * 如果需要使用大写字母，则可以在结果上调用 {@link java.lang.String#toUpperCase()} 方法：
     *
     * <blockquote>
     *  {@code Long.toHexString(n).toUpperCase()}
     * </blockquote>
     *
     * @param   i    要转换为字符串的 {@code long}。
     * @return  十六进制（基数&nbsp;16）参数表示的无符号 {@code long} 值的字符串表示形式。
     * @see #parseUnsignedLong(String, int)
     * @see #toUnsignedString(long, int)
     * @since   JDK 1.0.2
     */
    public static String toHexString(long i) {
        return toUnsignedString0(i, 4);
    }

    /**
     * 以八进制无符号整数形式返回 {@code long} 参数的字符串表示形式。
     *
     * <p>如果参数为负，该无符号 {@code long} 值为参数加上 2<sup>64</sup>；否则它等于该参数。
     * 该值被转换成八进制（基数&nbsp;8）ASCII 数字的字符串，且没有附加前导 {@code 0}。
     *
     * <p>>参数的值可以从字符串 {@code s} 中通过调用{@link
     * Integer#parseUnsignedInt(String, int)
     * Integer.parseUnsignedInt(s, 8)} 得到。
     *
     * <p>如果无符号数大小为零，则用一个零字符 {@code '0'} ({@code '\u005Cu0030'}) 表示它；
     * 否则，无符号数大小的表示形式中的第一个字符将不是零字符。
     * 用以下字符作为八进制数字：
     *
     * <blockquote>
     *  {@code 01234567}
     * </blockquote>
     *
     * 它们是从 {@code '\u005Cu0030'} 到 {@code '\u005Cu0037'} 的字符。
     *
     * @param   i   要转换为字符串的 {@code long}。
     * @return  八进制（基数&nbsp;8）参数表示的无符号 {@code long} 值的字符串表示形式。
     * @see #parseUnsignedLong(String, int)
     * @see #toUnsignedString(long, int)
     * @since   JDK 1.0.2
     */
    public static String toOctalString(long i) {
        return toUnsignedString0(i, 3);
    }

    /**
     * 以二进制（基数&nbsp;2）无符号整数形式返回一个 {@code long} 参数的字符串表示形式。
     *
     * <p>如果参数为负，该无符号整数值为参数加上 2<sup>64</sup>；否则等于该参数。
     * 将该值转换为二进制（基数&nbsp;2）形式的无前导 {@code 0} 的 ASCII 数字字符串。
     *
     * <p>参数的值可以从字符串 {@code s} 中通过调用{@link
     * Integer#parseUnsignedInt(String, int)
     * Integer.parseUnsignedInt(s, 2)} 得到。
     *
     * <p>如果无符号数的大小为零，则用一个零字符 {@code '0'} ({@code '\u005Cu0030'}) 表示它；
     * 否则，无符号数大小的表示形式中的第一个字符将不是零字符。
     * 字符  {@code '0'} ({@code '\u005Cu0030'}) 和
     * {@code '1'} ({@code '\u005Cu0031'}) 被用作二进制数字。
     *
     * @param   i   要转换为字符串的 {@code long}。
     * @return  用二进制参数（基数 &nbsp;2）表示的无符号 {@code long} 值的字符串表示形式。
     * @see #parseUnsignedLong(String, int)
     * @see #toUnsignedString(long, int)
     * @since   JDK 1.0.2
     */
    public static String toBinaryString(long i) {
        return toUnsignedString0(i, 1);
    }

    /**
     * 将long （看作无符号）格式化成字符串。
     * @param val 要格式化的值
     * @param shift 要格式化的 log2 的基数 (4 代表十六进制，3 代表八进制，1 代表二进制)
     */
    static String toUnsignedString0(long val, int shift) {
        // assert shift > 0 && shift <=5 : "Illegal shift value";
        int mag = Long.SIZE - Long.numberOfLeadingZeros(val);
        int chars = Math.max(((mag + (shift - 1)) / shift), 1);
        char[] buf = new char[chars];

        formatUnsignedLong(val, shift, buf, 0, chars);
        return new String(buf, true);
    }

    /**
     * 将 long（视为无符号）格式化为字符 buffer。
     * @param val 要格式化的无符号 long
     * @param shift 要格式化的 log2 的基数 (4 代表十六进制，3 代表八进制，1 代表二进制)
     * @param buf 要写入的字符 buffer
     * @param offset 从目标缓冲区开始的偏移量
     * @param len 要写入的字符数
     * @return  使用的最低字符位置
     */
     static int formatUnsignedLong(long val, int shift, char[] buf, int offset, int len) {
        int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] = Integer.digits[((int) val) & mask];
            val >>>= shift;
        } while (val != 0 && charPos > 0);

        return charPos;
    }

    /**
     * 返回表示指定 {@code long} 的 {@code String} 对象。
     * 该参数被转换为有符号的十进制表示形式，并作为字符串返回，
     * 该字符串与用该参数和基数 10 作为参数的 {@link #toString(long, int)} 方法所得到的值非常相似。
     *
     * @param   i    要转换的 {@code long}。
     * @return  十进制参数的字符串表示形式。
     */
    public static String toString(long i) {
        if (i == Long.MIN_VALUE)
            return "-9223372036854775808";
        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);
        char[] buf = new char[size];
        getChars(i, size, buf);
        return new String(buf, true);
    }

    /**
     * 返回参数作为无符号小数值的字符串表示形式。
     *
     * 该参数被转换为无符号的小数表示，并以字符串形式返回，
     *就像参数和基数 10 被作为参数赋给 {@link #toUnsignedString(long, int)} 方法一样。
     *
     * @param   i  要转换成无符号字符串的整数。.
     * @return  参数的无符号字符串表示形式。
     * @see     #toUnsignedString(long, int)
     * @since 1.8
     */
    public static String toUnsignedString(long i) {
        return toUnsignedString(i, 10);
    }

    /**
     * 将表示整数i的字符放在字符数组buf中。
     * 字符从指定索引(独占)的最不重要的数字开始向后放置到缓冲区中，
     * 并从那里向后工作。
     *
     * 如果 i == Long.MIN_VALUE 则失败
     */
    static void getChars(long i, int index, char[] buf) {
        long q;
        int r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > Integer.MAX_VALUE) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int)(i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = Integer.DigitOnes[r];
            buf[--charPos] = Integer.DigitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int)i;
        while (i2 >= 65536) {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = Integer.DigitOnes[r];
            buf[--charPos] = Integer.DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (;;) {
            q2 = (i2 * 52429) >>> (16+3);
            r = i2 - ((q2 << 3) + (q2 << 1));  // r = i2-(q2*10) ...
            buf[--charPos] = Integer.digits[r];
            i2 = q2;
            if (i2 == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    // Requires positive x
    static int stringSize(long x) {
        long p = 10;
        for (int i=1; i<19; i++) {
            if (x < p)
                return i;
            p = 10*p;
        }
        return 19;
    }

    /**
     * 将字符串参数解析为有符号十进制 {@code long}。
     * 字符串中的字符必须都是十进制数字，
     * 除非第一个字符是用来表示负值的 ASCII 减号 {@code '-'} ({@code '\u005Cu002D'}) 或
     * ASCII 加号 {@code '+'} ({@code '\u005Cu002B'}) 。
     * 返回得到的 {@code long} 值，
     * 该值与用该参数和基数 10 作为参数的 {@link java.lang.Character#digit(char, int)} 方法得到的值非常相似。
     *
     * <p>注意，不允许将字符 {@code L} ({@code '\u005Cu004C'}) 和
     * {@code l} ({@code '\u005Cu006C'}) 作为类型指示符出现在字符串的结尾处，
     * 而这一点在 Java 编程语言源代码中是允许的——除非 {@code L} 或 {@code l} 以大于 22 的基数形式出现。
     *
     * <p>如果发生以下任意一种情况，则抛出一个 {@code NumberFormatException} 类型的异常：
     * <ul>
     *
     * <li>第一个参数为 {@code null} 或一个长度为零的字符串。
     *
     * <li>基数小于 {@link java.lang.Character#MIN_RADIX} 或者
     * 大于 {@link java.lang.Character#MAX_RADIX}。
     *
     * <li>假如字符串的长度超过 1，
     * 那么除了第一个字符可以是减号 {@code '-'} ({@code '\u005Cu002D'}) 或
     * 加号 {@code '+'} ({@code '\u005Cu002B'})外，
     * 字符串中存在任意不是由指定基数的数字表示的字符。
     *
     * <li>字符串表示的值不是 {@code long} 类型的值。
     * </ul>
     *
     * <p>示例：
     * <blockquote><pre>
     * parseLong("0", 10) returns 0L
     * parseLong("473", 10) returns 473L
     * parseLong("+42", 10) returns 42L
     * parseLong("-0", 10) returns 0L
     * parseLong("-FF", 16) returns -255L
     * parseLong("1100110", 2) returns 102L
     * parseLong("99", 8) throws a NumberFormatException
     * parseLong("Hazelnut", 10) throws a NumberFormatException
     * parseLong("Hazelnut", 36) returns 1356099454469L
     * </pre></blockquote>
     *
     * @param      s       包含要解析的 {@code long} 表示形式的 {@code String}。
     * @param      radix   将在解析 {@code s} 时使用的基数。
     * @return     由指定基数中的字符串参数表示的 {@code long}。
     * @throws     NumberFormatException  如果字符串不包含可解析的 {@code long}。
     */
    public static long parseLong(String s, int radix)
              throws NumberFormatException
    {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix +
                                            " less than Character.MIN_RADIX");
        }
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix +
                                            " greater than Character.MAX_RADIX");
        }

        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+')
                    throw NumberFormatException.forInputString(s);

                if (len == 1) // Cannot have lone "+" or "-"
                    throw NumberFormatException.forInputString(s);
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++),radix);
                if (digit < 0) {
                    throw NumberFormatException.forInputString(s);
                }
                if (result < multmin) {
                    throw NumberFormatException.forInputString(s);
                }
                result *= radix;
                if (result < limit + digit) {
                    throw NumberFormatException.forInputString(s);
                }
                result -= digit;
            }
        } else {
            throw NumberFormatException.forInputString(s);
        }
        return negative ? result : -result;
    }

    /**
     * 将字符串参数解析为有符号十进制 {@code long}。
     * 字符串中的字符必须都是十进制数字，
     * 除非第一个字符是用来表示负值的 ASCII 减号 {@code '-'} ({@code '\u005Cu002D'}) 或
     * ASCII 加号 {@code '+'} ({@code '\u005Cu002B'}) 。
     * 返回得到的 {@code long} 值，
     * 该值与用该参数和基数 10 作为参数的 {@link java.lang.Character#digit(char, int)} 方法得到的值非常相似。
     *
     * <p>注意，不允许将字符 {@code L} ({@code '\u005Cu004C'}) 和
     * {@code l} ({@code '\u005Cu006C'}) 作为类型指示符出现在字符串的结尾处，
     * 而这一点在 Java 编程语言源代码中是允许的。
     *
     * @param      s   包含要解析的 {@code long} 表示形式的 {@code String}
     * @return     十进制参数表示的 {@code long}。
     * @throws     NumberFormatException  如果字符串不包含可解析的 {@code long}。
     */
    public static long parseLong(String s) throws NumberFormatException {
        return parseLong(s, 10);
    }

    /**
     * 将字符串参数解析为第二个参数指定的基数中的无符号 {@code long}。
     * 无符号整数将通常与负数相关的值映射到大于 {@code MAX_VALUE} 的正数。
     *
     * 除了第一个字符可能是ASCII 加号 {@code '+'} ({@code '\u005Cu002B'}) 外，
     * 字符串中的字符必须都是指定基数的数字（通过 {@link java.lang.Character#digit(char, int)} 是否返回一个负值确定）。
     * 除了第一个字符可能是ASCII 加号 {@code '+'} ({@code '\u005Cu002B'}) 。
     * 返回结果的整数值。
     *
     * <p>如果发生以下任意一种情况，则抛出一个 {@code NumberFormatException} 类型的异常：
     * <ul>
     * <li>第一个参数为 {@code null} 或一个长度为零的字符串。
     *
     * <li>基数小于 {@link java.lang.Character#MIN_RADIX} 或者
     * 大于 {@link java.lang.Character#MAX_RADIX}。
     *
     * <li>假如字符串的长度超过 1，
     * 那么除了第一个字符可以是 加号 {@code '+'} ({@code '\u005Cu002B'})外，
     * 字符串中存在任意不是由指定基数的数字表示的字符。
     *
     * <li>字符串表示的值大于无符号 {@code int} 的最大值 2<sup>64</sup>-1。
     *
     * </ul>
     *
     *
     * @param      s   包含要解析的无符号整数表示的字符串
     * @param      radix   解析 {@code s} 时要使用的基数。
     * @return     用指定基数的字符串参数表示的 {@code long}。
     * @throws     NumberFormatException 如果字符串不包含可解析的 {@code long}。
     * @since 1.8
     */
    public static long parseUnsignedLong(String s, int radix)
                throws NumberFormatException {
        if (s == null)  {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new
                    NumberFormatException(String.format("Illegal leading minus sign " +
                                                       "on unsigned string %s.", s));
            } else {
                if (len <= 12 || // Long.MAX_VALUE in Character.MAX_RADIX is 13 digits
                    (radix == 10 && len <= 18) ) { // Long.MAX_VALUE in base 10 is 19 digits
                    return parseLong(s, radix);
                }

                // No need for range checks on len due to testing above.
                long first = parseLong(s.substring(0, len - 1), radix);
                int second = Character.digit(s.charAt(len - 1), radix);
                if (second < 0) {
                    throw new NumberFormatException("Bad digit at end of " + s);
                }
                long result = first * radix + second;
                if (compareUnsigned(result, first) < 0) {
                    /*
                     * The maximum unsigned value, (2^64)-1, takes at
                     * most one more digit to represent than the
                     * maximum signed value, (2^63)-1.  Therefore,
                     * parsing (len - 1) digits will be appropriately
                     * in-range of the signed parsing.  In other
                     * words, if parsing (len -1) digits overflows
                     * signed parsing, parsing len digits will
                     * certainly overflow unsigned parsing.
                     *
                     * The compareUnsigned check above catches
                     * situations where an unsigned overflow occurs
                     * incorporating the contribution of the final
                     * digit.
                     */
                    throw new NumberFormatException(String.format("String value %s exceeds " +
                                                                  "range of unsigned long.", s));
                }
                return result;
            }
        } else {
            throw NumberFormatException.forInputString(s);
        }
    }

    /**
     * 将字符串参数解析为无符号十进制整数。
     * 除了第一个字符可能是ASCII 加号{@code '+'} ({@code '\u005Cu002B'})外，
     * 字符串中的字符必须是十进制的。
     * 返回结果的整数值，就像将参数和基数10作为 {@link #parseUnsignedLong(java.lang.String, int)} 方法的参数一样。
     *
     * @param s   包含要解析的无符号 {@code long} 表示的字符串
     * @return     十进制参数表示的无符号 {@code long} 值。
     * @throws    NumberFormatException  如果字符串不包含可解析的整数。
     * @since 1.8
     */
    public static long parseUnsignedLong(String s) throws NumberFormatException {
        return parseUnsignedLong(s, 10);
    }

    /**
     * 当使用第二个参数给出的基数进行解析时，
     * 返回保持从指定 {@code String} 中提取的值的 {@code Long} 对象。
     * 第一个参数被解释为有符号的 {@code long}，
     * 基数由第二个参数指定，
     * 该值与用该参数作为参数的 {@link #parseLong(java.lang.String, int)} 方法得到的值非常类似。
     * 结果是表示字符串指定的 long 值的 {@code Long} 对象。
     *
     * <p>>换句话说，此方法返回一个 {@code Long} 对象，它的值等于：
     *
     * <blockquote>
     *  {@code new Long(Long.parseLong(s, radix))}
     * </blockquote>
     *
     * @param      s       要解析的字符串
     * @param      radix   解释 {@code s} 时使用的基数
     * @return     一个 {@code Long} 对象，它含有字符串参数（以指定的基数）所表示的数值。
     * @throws     NumberFormatException  如果不能将字符串解析为 {@code long}。
     */
    public static Long valueOf(String s, int radix) throws NumberFormatException {
        return Long.valueOf(parseLong(s, radix));
    }

    /**
     * 返回保持指定 {@code String} 的值的 {@code Long} 对象。
     * 该参数被解释为表示一个有符号的十进制 {@code long}，
     * 该值与用该参数作为参数的 {@link #parseLong(java.lang.String)} 方法得到的值非常相似。
     * 结果是表示由字符串指定的整数值的 {@code Long}对象。
     *
     * <p>换句话说，此方法返回一个 {@code Long} 对象，它的值等于：
     *
     * <blockquote>
     *  {@code new Long(Long.parseLong(s))}
     * </blockquote>
     *
     * @param      s   要解析的字符串。
     * @return     包含由字符串参数表示的值的 {@code Long} 对象。
     * @throws     NumberFormatException  如果不能将字符串解析为 {@code long}。
     */
    public static Long valueOf(String s) throws NumberFormatException
    {
        return Long.valueOf(parseLong(s, 10));
    }

    private static class LongCache {
        private LongCache(){}

        static final Long cache[] = new Long[-(-128) + 127 + 1];

        static {
            for(int i = 0; i < cache.length; i++)
                cache[i] = new Long(i - 128);
        }
    }

    /**
     * 返回表示指定 {@code long} 值的 {@code Long} 实例。
     * 如果不需要新的 {@code Long} 实例，则通常优先使用此方法，
     * 而不是使用构造方法 {@link #Long(long)}，
     * 因为此方法通过缓存频繁请求的值，可以显著提高时间和空间性能。
     *
     * 注意，与 {@code Integer} 类中的 {@linkplain Integer#valueOf(int) corresponding method}
     * 不同，该方法 <em>不</em> 需要在特定范围内缓存值。
     * range.
     *
     * @param  l 一个 long 值。
     * @return 表示 {@code l} 的 {@code Long} 实例。
     * @since  1.5
     */
    public static Long valueOf(long l) {
        final int offset = 128;
        if (l >= -128 && l <= 127) { // will cache
            return LongCache.cache[(int)l + offset];
        }
        return new Long(l);
    }

    /**
     * 将 {@code String} 解码成 {@code Long}。接受通过以下语法给出的十进制、十六进制和八进制数：
     *
     * <blockquote>
     * <dl>
     * <dt><i>DecodableString:</i>
     * <dd><i>Sign<sub>opt</sub> DecimalNumeral</i>
     * <dd><i>Sign<sub>opt</sub></i> {@code 0x} <i>HexDigits</i>
     * <dd><i>Sign<sub>opt</sub></i> {@code 0X} <i>HexDigits</i>
     * <dd><i>Sign<sub>opt</sub></i> {@code #} <i>HexDigits</i>
     * <dd><i>Sign<sub>opt</sub></i> {@code 0} <i>OctalDigits</i>
     *
     * <dt><i>Sign:</i>
     * <dd>{@code -}
     * <dd>{@code +}
     * </dl>
     * </blockquote>
     *
     * <cite>The Java&trade; Language Specification</cite> 的第 3.10.1 节中有
     * <i>DecimalNumeral</i>, <i>HexDigits</i>, 和 <i>OctalDigits</i> 的定义。
     *
     * <p>跟在（可选）负号和/或基数说明符（“{@code 0x}”、“{@code 0X}”、“{@code #}”或前导零）后面的字符序列是
     * 使用指示的基数（10、16 或 8）通过 {@code Integer.parseInt} 方法解析的。
     * 字符序列必须表示一个正值，否则会抛出 {@link NumberFormatException}。
     * 如果指定的 {@code String} 的第一个字符是减号，则对结果求反。
     * {@code String} 中不允许出现空白字符。
     *
     * @param     nm 要解码的 {@code String}。
     * @return    保存 {@code nm} 所表示的 {@code long} 值的 {@code Long} 对象。
     * @throws    NumberFormatException  如果 {@code String} 不包含可解析整数。
     * @see java.lang.Long#parseLong(String, int)
     * @since 1.2
     */
    public static Long decode(String nm) throws NumberFormatException {
        int radix = 10;
        int index = 0;
        boolean negative = false;
        Long result;

        if (nm.length() == 0)
            throw new NumberFormatException("Zero length string");
        char firstChar = nm.charAt(0);
        // Handle sign, if present
        if (firstChar == '-') {
            negative = true;
            index++;
        } else if (firstChar == '+')
            index++;

        // Handle radix specifier, if present
        if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        }
        else if (nm.startsWith("#", index)) {
            index ++;
            radix = 16;
        }
        else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
            index ++;
            radix = 8;
        }

        if (nm.startsWith("-", index) || nm.startsWith("+", index))
            throw new NumberFormatException("Sign character in wrong position");

        try {
            result = Long.valueOf(nm.substring(index), radix);
            result = negative ? Long.valueOf(-result.longValue()) : result;
        } catch (NumberFormatException e) {
            // If number is Long.MIN_VALUE, we'll end up here. The next line
            // handles this case, and causes any genuine format error to be
            // rethrown.
            String constant = negative ? ("-" + nm.substring(index))
                                       : nm.substring(index);
            result = Long.valueOf(constant, radix);
        }
        return result;
    }

    /**
     * {@code Long} 的值。
     *
     * @serial
     */
    private final long value;

    /**
     * 构造新分配的 {@code Long} 对象，表示指定的 {@code long} 参数。
     *
     * @param   value   {@code Long} 对象表示的值。
     */
    public Long(long value) {
        this.value = value;
    }

    /**
     * 构造新分配的 {@code Long} 对象，表示由 {@code String} 参数指示的 {@code long} 值。
     * 该字符串被转换为 {@code long} 值，
     * 其方式与 radix 参数为 10 的 {@code parseLong} 方法所使用的方式一致。
     *
     * @param      s   要转换为 {@code Long} 的 {@code String}。
     * @throws     NumberFormatException  如果 {@code String} 不包含可解析的 {@code long}。
     * @see        java.lang.Long#parseLong(java.lang.String, int)
     */
    public Long(String s) throws NumberFormatException {
        this.value = parseLong(s, 10);
    }

    /**
     * 以 {@code byte} 形式返回此 {@code Long} 的值。
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * 以 {@code short} 形式返回此 {@code Long} 的值。
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     * 以 {@code int} 形式返回此 {@code Long} 的值。
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * 以 {@code long} 形式返回此 {@code Long} 的值。
     */
    public long longValue() {
        return value;
    }

    /**
     * 以 {@code float} 形式返回此 {@code Long} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public float floatValue() {
        return (float)value;
    }

    /**
     * 以 {@code double} 形式返回此 {@code Long} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)value;
    }

    /**
     * 返回表示 {@code Long} 值的 {@code String} 对象。
     * 该值被转换为有符号十进制表示形式，并作为字符串返回，
     * 该字符串与用 {@code long} 值作为参数的 {@link java.lang.Long#toString(long)} 方法得到的字符串非常相似。
     *
     * @return  十进制对象值的字符串表示形式。
     */
    public String toString() {
        return toString(value);
    }

    /**
     * 返回 {@code Long} 的哈希码。
     * 结果是此 {@code Long} 对象保持的基本 {@code long} 值的两个部分的异或 (XOR)。
     * 也就是说，哈希码就是表达式的值：
     *
     * <blockquote>
     *  {@code (int)(this.longValue()^(this.longValue()>>>32))}
     * </blockquote>
     *
     * @return  此对象的哈希码值。
     */
    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    /**
     * 返回 {@code long} 值的哈希值；和 {@code Long.hashCode()} 兼容。
     *
     * @param value 要哈希的值
     * @return {@code long} 值的哈希值。
     * @since 1.8
     */
    public static int hashCode(long value) {
        return (int)(value ^ (value >>> 32));
    }

    /**
     * 将此对象与指定对象进行比较。
     * 当且仅当该参数不是 {@code null}，
     * 且 {@code Long} 对象与此对象包含相同的 {@code long} 值时，结果才为 {@code true}。
     *
     * @param   obj   要与之进行比较的对象。
     * @return  如果对象相同，则返回 {@code true}；否则，返回 {@code false}。
     */
    public boolean equals(Object obj) {
        if (obj instanceof Long) {
            return value == ((Long)obj).longValue();
        }
        return false;
    }

    /**
     * 确定具有指定名称的系统属性的 {@code long}  值。
     *
     * <p>第一个参数被视为系统属性的名称。
     * 通过 {@link java.lang.System#getProperty(java.lang.String)} 方法可以访问该系统属性。
     * 然后，根据每个 {@link Integer#decode decode} 方法，
     * 以 {@code long} 值的形式解释此属性的字符串值，并返回表示此值的 {@code Long} 对象。
     *
     * <p>如果没有具有指定名称的属性，或者指定名称为空或 {@code null}，
     * 或者属性的数字格式不正确，则返回 {@code null}。
     *
     * <p>换句话说，该方法返回一个等于以下值的 {@code Long} 对象：
     *
     * <blockquote>
     *  {@code getLong(nm, null)}
     * </blockquote>
     *
     * @param   nm   属性名。
     * @return  属性的 {@code Long} 值。
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static Long getLong(String nm) {
        return getLong(nm, null);
    }

    /**
     * 使用指定名称确定系统属性的 {@code long} 值。
     *
     * <p>第一个参数被视为系统属性的名称。
     * 通过 {@link java.lang.System#getProperty(java.lang.String)} 方法可以访问该系统属性。
     * 然后，根据每个 {@link Integer#decode decode} 方法，
     * 以 {@code long} 值的形式解释此属性的字符串值，并返回表示此值的 {@code Long} 对象。
     *
     * <p>第二个参数是默认值。如果指定的名称没有属性，
     * 或者该属性不具备正确的数字格式，抑或指定名称为空或 null，
     * 则返回表示第二个参数的值的 {@code Long} 对象。
     *
     * <p>换句话说，此方法返回一个 {@code Long} 对象，它的值等于：
     *
     * <blockquote>
     *  {@code getLong(nm, new Long(val))}
     * </blockquote>
     *
     * 但是实际上，它可能通过以下方式实现：
     *
     * <blockquote><pre>
     * Long result = getLong(nm, null);
     * return (result == null) ? new Long(val) : result;
     * </pre></blockquote>
     *
     * 这样可以避免不需要默认值时进行的不必要的 {@code Long} 对象分配。
     *
     * @param   nm   属性名。
     * @param   val   默认值。
     * @return  属性的 {@code Long} 值。
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static Long getLong(String nm, long val) {
        Long result = Long.getLong(nm, null);
        return (result == null) ? Long.valueOf(val) : result;
    }

    /**
     * 使用指定名称返回系统属性的 {@code long} 值。
     * 第一个参数被视为系统属性的名称。
     * 通过 {@link java.lang.System#getProperty(java.lang.String)} 方法可以访问该系统属性。
     * 然后，以 {@code long} 值的形式解释此属性的字符串值，
     * 并且按照 {@link Long#decode decode} 方法返回表示此值的 {@code Long} 对象。
     * 总之：
     *
     * <ul>
     * <li>如果属性值以两个 ASCII 字符 {@code 0x} 或者 ASCII 字符 {@code #} 开始，
     *     并且后面没有减号，则将它的剩余部分解析为十六进制整数，
     *     就好像以 16 为基数调用 {@link #valueOf(java.lang.String, int)} 方法一样。
     * <li>如果属性值以 ASCII 字符 {@code 0} 开始，后面还有其他字符，
     *     则将它解析为八进制整数，
     *     就好像以 8 为基数调用 {@link #valueOf(java.lang.String, int)} 方法一样。
     * <li>否则，将属性值解析为十进制整数，
     *     就好像以 10 为基数调用 {@link #valueOf(java.lang.String, int)} 方法一样。
     * </ul>
     *
     * <p>注意，在所有情况下，
     * 都不允许将 {@code L} ('{@code '\u005Cu004C'}) 和
     * {@code l} ({@code '\u005Cu006C'}) 作为类型指示符出现在属性值的结尾处，
     * 这一点在 Java 编程语言源代码中是允许的。
     *
     * <p>第二个参数是默认值。如果指定的名称没有属性，
     * 或者属性不具有正确的数字格式，抑或指定名称为空或 {@code null}，则返回默认值。
     *
     * @param   nm   属性名。
     * @param   val   默认值。
     * @return  属性的 {@code Long}  值。
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     System#getProperty(java.lang.String)
     * @see     System#getProperty(java.lang.String, java.lang.String)
     */
    public static Long getLong(String nm, Long val) {
        String v = null;
        try {
            v = System.getProperty(nm);
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        if (v != null) {
            try {
                return Long.decode(v);
            } catch (NumberFormatException e) {
            }
        }
        return val;
    }

    /**
     * 在数字上比较两个 {@code Long} 对象。
     *
     * @param   anotherLong   要比较的 {@code Long}。
     * @return  如果 {@code Long} 等于参数 {@code Long}，则返回 {@code 0} 值；
     *          如果 {@code Long} 在数字上小于参数 {@code Long}，则返回小于 {@code 0} 的值；
     *          如果 {@code Long} 在数字上大于参数 {@code Long}，则返回大于 {@code 0} 的值（有符号比较）。
     * @since   1.2
     */
    public int compareTo(Long anotherLong) {
        return compare(this.value, anotherLong.value);
    }

    /**
     * 在数字上比较两个 {@code long} 值。
     * 返回的值和下面返回的值相同：
     * <pre>
     *    Long.valueOf(x).compareTo(Long.valueOf(y))
     * </pre>
     *
     * @param  x 第一个要比较的 {@code long}
     * @param  y 第二个要比较的 {@code long}
     * @return 如果 {@code x == y} 则返回 {@code 0}；
     *         如果 {@code x < y} 则返回 小于 {@code 0} 的值；
     *         如果 {@code x > y} 则返回 大于 {@code 0} 的值；
     * @since 1.7
     */
    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * 数值上比较两个 被看作无符号的 {@code long} 值。
     *
     * @param  x 第一个要比较的 {@code long}
     * @param  y 第二个要比较的 {@code long}
     * @return 如果 {@code x == y} 则返回 {@code 0}；
     *         如果看作无符号整数 {@code x < y} 则返回 小于 {@code 0} 的值；
     *         如果看作无符号整数 {@code x > y} 则返回 大于 {@code 0} 的值；
     * @since 1.8
     */
    public static int compareUnsigned(long x, long y) {
        return compare(x + MIN_VALUE, y + MIN_VALUE);
    }


    /**
     * 返回将第一个参数除以第二个参数的无符号商，其中每个参数和结果都被解释为无符号值。
     *
     * <p>请注意，在在二进制补码运算中，如果两个操作数都被认为是有符号的或两者都是无符号的，
     * 则加、减和乘的其他三个基本算术运算是按位相同的。
     * 因此，不提供单独的 {@code addUnsigned} 等方法。
     *
     * @param dividend 被除数
     * @param divisor 除数
     * @return 第一个参数的除以第二个参数的无符号商
     * @see #remainderUnsigned
     * @since 1.8
     */
    public static long divideUnsigned(long dividend, long divisor) {
        if (divisor < 0L) { // signed comparison
            // Answer must be 0 or 1 depending on relative magnitude
            // of dividend and divisor.
            return (compareUnsigned(dividend, divisor)) < 0 ? 0L :1L;
        }

        if (dividend > 0) //  Both inputs non-negative
            return dividend/divisor;
        else {
            /*
             * For simple code, leveraging BigInteger.  Longer and faster
             * code written directly in terms of operations on longs is
             * possible; see "Hacker's Delight" for divide and remainder
             * algorithms.
             */
            return toUnsignedBigInteger(dividend).
                divide(toUnsignedBigInteger(divisor)).longValue();
        }
    }

    /**
     * 返回第一个参数除以第二个参数的无符号余数，其中每个参数和结果都被解释为无符号值。
     *
     * @param dividend 被除数
     * @param divisor 除数
     * @return 第一个参数除以第二个参数的无符号余数
     * @see #divideUnsigned
     * @since 1.8
     */
    public static long remainderUnsigned(long dividend, long divisor) {
        if (dividend > 0 && divisor > 0) { // signed comparisons
            return dividend % divisor;
        } else {
            if (compareUnsigned(dividend, divisor) < 0) // Avoid explicit check for 0 divisor
                return dividend;
            else
                return toUnsignedBigInteger(dividend).
                    remainder(toUnsignedBigInteger(divisor)).longValue();
        }
    }

    // Bit Twiddling

    /**
     * 在二进制补码形式中用来表示 {@code long} 值的位数。
     *
     * @since 1.5
     */
    @Native public static final int SIZE = 64;

    /**
     * 在二进制补码形式中用来表示 {@code long} 值的字节数。
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * 返回至多有一个 1 位的 {@code long} 值，
     * 在指定的 {@code long} 值中最高位（最左边）的 1 位的位置。
     * 如果指定值在其二进制补码表示形式中没有 1 位，即等于零，则返回零。
     *
     * @param i 要计算最高位的值
     * @return 返回具有单个 1 位的 {@code long} 值，在指定值中最高位的 1 位的位置；
     *         否则，如果指定值本身等于零，则返回零。
     * @since 1.5
     */
    public static long highestOneBit(long i) {
        // HD, Figure 3-1
        i |= (i >>  1);
        i |= (i >>  2);
        i |= (i >>  4);
        i |= (i >>  8);
        i |= (i >> 16);
        i |= (i >> 32);
        return i - (i >>> 1);
    }

    /**
     * 返回至多有一个 1 位的 {@code long} 值，
     * 在指定的 {@code long} 值中最低位（最右边）的 1 位的位置。
     * 如果指定值在其二进制补码表示形式中没有 1 位，即等于零，则返回零。
     *
     * @param i 要计算最低位的值
     * @return 返回具有单个 1 位的 {@code long} 值，在指定值中最低位的 1 位的位置；
     *         否则，如果指定值本身等于零，则返回零。
     * @since 1.5
     */
    public static long lowestOneBit(long i) {
        // HD, Section 2-1
        return i & -i;
    }

    /**
     * 在指定 {@code long} 值的二进制补码表示形式中最高位（最左边）的 1 位之前，
     * 返回零位的数量。
     * 如果指定值在其二进制补码表示形式中不存在 1 位，
     * 换句话说，如果它等于零，则返回 64。
     *
     * <p>注意，此方法与二进制对数密切相关。
     * 对于所有的正 {@code long} 值 x：
     * <ul>
     * <li>floor(log<sub>2</sub>(x)) = {@code 63 - numberOfLeadingZeros(x)}
     * <li>ceil(log<sub>2</sub>(x)) = {@code 64 - numberOfLeadingZeros(x - 1)}
     * </ul>
     *
     * @param i 要计算头部 0 的个数的值
     * @return 返回在指定 {@code long} 值的二进制补码表示形式中最高位（最左边）的 1 位之前的零位的数量；
     *         否则，如果该值等于零，则返回 64。
     * @since 1.5
     */
    public static int numberOfLeadingZeros(long i) {
        // HD, Figure 5-6
         if (i == 0)
            return 64;
        int n = 1;
        int x = (int)(i >>> 32);
        if (x == 0) { n += 32; x = (int)i; }
        if (x >>> 16 == 0) { n += 16; x <<= 16; }
        if (x >>> 24 == 0) { n +=  8; x <<=  8; }
        if (x >>> 28 == 0) { n +=  4; x <<=  4; }
        if (x >>> 30 == 0) { n +=  2; x <<=  2; }
        n -= x >>> 31;
        return n;
    }

    /**
     * 返回在指定 {@code long} 值的二进制补码表示形式中最低位（最右边）的 1 位之后的零位的数量。
     * 如果指定值在其二进制补码表示形式中不存在 1 位，
     * 换句话说，如果它等于零，则返回 64。
     *
     * @param i 要计算尾部 0 个数的值
     * @return 返回在指定 {@code long} 值的二进制补码表示形式中最低位（最右边）的 1 位之后零位的数量；
     *         否则，如果该值等于零，则返回 64。
     * @since 1.5
     */
    public static int numberOfTrailingZeros(long i) {
        // HD, Figure 5-14
        int x, y;
        if (i == 0) return 64;
        int n = 63;
        y = (int)i; if (y != 0) { n = n -32; x = y; } else x = (int)(i>>>32);
        y = x <<16; if (y != 0) { n = n -16; x = y; }
        y = x << 8; if (y != 0) { n = n - 8; x = y; }
        y = x << 4; if (y != 0) { n = n - 4; x = y; }
        y = x << 2; if (y != 0) { n = n - 2; x = y; }
        return n - ((x << 1) >>> 31);
    }

    /**
     * 返回指定 {@code long} 值的二进制补码表示形式中的 1 位的数量。
     * 此功能有时被称为<i>填充计算</i>。
     *
     * @param i 要计算位数的值
     * @return 返回指定 {@code long} 值的二进制补码表示形式的 1 位的数量。
     * @since 1.5
     */
     public static int bitCount(long i) {
        // HD, Figure 5-14
        i = i - ((i >>> 1) & 0x5555555555555555L);
        i = (i & 0x3333333333333333L) + ((i >>> 2) & 0x3333333333333333L);
        i = (i + (i >>> 4)) & 0x0f0f0f0f0f0f0f0fL;
        i = i + (i >>> 8);
        i = i + (i >>> 16);
        i = i + (i >>> 32);
        return (int)i & 0x7f;
     }

    /**
     * 返回根据指定的位数循环左移指定的 {@code long} 值的二进制补码表示形式而得到的值。
     * （位是从左边（即高位）移出，从右边（即低位）再进入）
     *
     * <p>注意，使用负距离的左循环等同于右循环：
     * {@code rotateLeft(val, -distance) == rotateRight(val, distance)}。
     * 另请注意，使用 64 的倍数循环无效，
     * 因此除了最后六位，所有循环距离都可以忽略，即使距离是负值：
     * {@code rotateLeft(val, distance) == rotateLeft(val, distance & 0x3F)}。
     *
     * @param i 需要循环左移的值
     * @param distance 循环左移的位数
     * @return 返回根据指定的位数循环左移指定的 {@code long} 值的二进制补码表示形式而得到的值。
     * @since 1.5
     */
    public static long rotateLeft(long i, int distance) {
        return (i << distance) | (i >>> -distance);
    }

    /**
     * 返回根据指定的位数循环右移指定的 {@code long} 值的二进制补码表示形式而得到的值。
     * （位是从右边（即低位）移出，从左边（即高位）再进入）
     *
     * <p>注意，使用负距离右循环等于左循环：
     * {@code rotateRight(val, -distance) == rotateLeft(val, distance)}。
     * 另请注意，使用 64 的倍数循环无效，
     * 因此除了最后六位，所有循环距离都可以忽略，即使距离是负值：
     * {@code rotateRight(val, distance) == rotateRight(val, distance & 0x3F)}。
     *
     * @param i 需要循环右移的值
     * @param distance 循环右移的位数
     * @return 返回根据指定的位数循环右移指定的 {@code long} 值的二进制补码表示形式而得到的值。
     * @since 1.5
     */
    public static long rotateRight(long i, int distance) {
        return (i >>> distance) | (i << -distance);
    }

    /**
     * 返回通过反转指定 {@code long} 值的二进制补码表示形式中位的顺序而获得的值。
     *
     * @param i 需要反转的值
     * @return 返回通过反转指定 {@code long} 值中位的顺序而获得的值。
     * @since 1.5
     */
    public static long reverse(long i) {
        // HD, Figure 7-1
        i = (i & 0x5555555555555555L) << 1 | (i >>> 1) & 0x5555555555555555L;
        i = (i & 0x3333333333333333L) << 2 | (i >>> 2) & 0x3333333333333333L;
        i = (i & 0x0f0f0f0f0f0f0f0fL) << 4 | (i >>> 4) & 0x0f0f0f0f0f0f0f0fL;
        i = (i & 0x00ff00ff00ff00ffL) << 8 | (i >>> 8) & 0x00ff00ff00ff00ffL;
        i = (i << 48) | ((i & 0xffff0000L) << 16) |
            ((i >>> 16) & 0xffff0000L) | (i >>> 48);
        return i;
    }

    /**
     * 返回指定 {@code long} 值的符号函数。
     * （如果指定值为负，则返回值 -1；如果指定值为零，则返回 0；如果指定值为正，则返回 1。）
     *
     * @param i 需要计算符号函数的值
     * @return 返回指定 {@code long} 值的符号函数。
     * @since 1.5
     */
    public static int signum(long i) {
        // HD, Section 2-7
        return (int) ((i >> 63) | (-i >>> 63));
    }

    /**
     * 返回通过反转指定 {@code long} 值的二进制补码表示形式中字节的顺序而获得的值。
     *
     * @param i 要反转字节的值
     * @return 返回通过反转指定 {@code long} 值的字节而获得的值。
     * @since 1.5
     */
    public static long reverseBytes(long i) {
        i = (i & 0x00ff00ff00ff00ffL) << 8 | (i >>> 8) & 0x00ff00ff00ff00ffL;
        return (i << 48) | ((i & 0xffff0000L) << 16) |
            ((i >>> 16) & 0xffff0000L) | (i >>> 48);
    }

    /**
     * 按 + 运算符将两个整数相加。
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的和
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static long sum(long a, long b) {
        return a + b;
    }

    /**
     * 返回两个 {@code int} 值的最大值，
     * 通过调用 {@link Math#max(int, int) Math.max} 得到。
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的最大值
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static long max(long a, long b) {
        return Math.max(a, b);
    }

    /**
     * 返回两个 {@code int} 值的最小值，
     * 通过调用 {@link Math#min(int, int) Math.min} 得到。
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的最小值
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static long min(long a, long b) {
        return Math.min(a, b);
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    @Native private static final long serialVersionUID = 4290774380558885855L;
}
