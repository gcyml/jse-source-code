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
     * Decodes a {@code String} into a {@code Long}.
     * Accepts decimal, hexadecimal, and octal numbers given by the
     * following grammar:
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
     * <i>DecimalNumeral</i>, <i>HexDigits</i>, and <i>OctalDigits</i>
     * are as defined in section 3.10.1 of
     * <cite>The Java&trade; Language Specification</cite>,
     * except that underscores are not accepted between digits.
     *
     * <p>The sequence of characters following an optional
     * sign and/or radix specifier ("{@code 0x}", "{@code 0X}",
     * "{@code #}", or leading zero) is parsed as by the {@code
     * Long.parseLong} method with the indicated radix (10, 16, or 8).
     * This sequence of characters must represent a positive value or
     * a {@link NumberFormatException} will be thrown.  The result is
     * negated if first character of the specified {@code String} is
     * the minus sign.  No whitespace characters are permitted in the
     * {@code String}.
     *
     * @param     nm the {@code String} to decode.
     * @return    a {@code Long} object holding the {@code long}
     *            value represented by {@code nm}
     * @throws    NumberFormatException  if the {@code String} does not
     *            contain a parsable {@code long}.
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
     * The value of the {@code Long}.
     *
     * @serial
     */
    private final long value;

    /**
     * Constructs a newly allocated {@code Long} object that
     * represents the specified {@code long} argument.
     *
     * @param   value   the value to be represented by the
     *          {@code Long} object.
     */
    public Long(long value) {
        this.value = value;
    }

    /**
     * Constructs a newly allocated {@code Long} object that
     * represents the {@code long} value indicated by the
     * {@code String} parameter. The string is converted to a
     * {@code long} value in exactly the manner used by the
     * {@code parseLong} method for radix 10.
     *
     * @param      s   the {@code String} to be converted to a
     *             {@code Long}.
     * @throws     NumberFormatException  if the {@code String} does not
     *             contain a parsable {@code long}.
     * @see        java.lang.Long#parseLong(java.lang.String, int)
     */
    public Long(String s) throws NumberFormatException {
        this.value = parseLong(s, 10);
    }

    /**
     * Returns the value of this {@code Long} as a {@code byte} after
     * a narrowing primitive conversion.
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * Returns the value of this {@code Long} as a {@code short} after
     * a narrowing primitive conversion.
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     * Returns the value of this {@code Long} as an {@code int} after
     * a narrowing primitive conversion.
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * Returns the value of this {@code Long} as a
     * {@code long} value.
     */
    public long longValue() {
        return value;
    }

    /**
     * Returns the value of this {@code Long} as a {@code float} after
     * a widening primitive conversion.
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public float floatValue() {
        return (float)value;
    }

    /**
     * Returns the value of this {@code Long} as a {@code double}
     * after a widening primitive conversion.
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)value;
    }

    /**
     * Returns a {@code String} object representing this
     * {@code Long}'s value.  The value is converted to signed
     * decimal representation and returned as a string, exactly as if
     * the {@code long} value were given as an argument to the
     * {@link java.lang.Long#toString(long)} method.
     *
     * @return  a string representation of the value of this object in
     *          base&nbsp;10.
     */
    public String toString() {
        return toString(value);
    }

    /**
     * Returns a hash code for this {@code Long}. The result is
     * the exclusive OR of the two halves of the primitive
     * {@code long} value held by this {@code Long}
     * object. That is, the hashcode is the value of the expression:
     *
     * <blockquote>
     *  {@code (int)(this.longValue()^(this.longValue()>>>32))}
     * </blockquote>
     *
     * @return  a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    /**
     * Returns a hash code for a {@code long} value; compatible with
     * {@code Long.hashCode()}.
     *
     * @param value the value to hash
     * @return a hash code value for a {@code long} value.
     * @since 1.8
     */
    public static int hashCode(long value) {
        return (int)(value ^ (value >>> 32));
    }

    /**
     * Compares this object to the specified object.  The result is
     * {@code true} if and only if the argument is not
     * {@code null} and is a {@code Long} object that
     * contains the same {@code long} value as this object.
     *
     * @param   obj   the object to compare with.
     * @return  {@code true} if the objects are the same;
     *          {@code false} otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Long) {
            return value == ((Long)obj).longValue();
        }
        return false;
    }

    /**
     * Determines the {@code long} value of the system property
     * with the specified name.
     *
     * <p>The first argument is treated as the name of a system
     * property.  System properties are accessible through the {@link
     * java.lang.System#getProperty(java.lang.String)} method. The
     * string value of this property is then interpreted as a {@code
     * long} value using the grammar supported by {@link Long#decode decode}
     * and a {@code Long} object representing this value is returned.
     *
     * <p>If there is no property with the specified name, if the
     * specified name is empty or {@code null}, or if the property
     * does not have the correct numeric format, then {@code null} is
     * returned.
     *
     * <p>In other words, this method returns a {@code Long} object
     * equal to the value of:
     *
     * <blockquote>
     *  {@code getLong(nm, null)}
     * </blockquote>
     *
     * @param   nm   property name.
     * @return  the {@code Long} value of the property.
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static Long getLong(String nm) {
        return getLong(nm, null);
    }

    /**
     * Determines the {@code long} value of the system property
     * with the specified name.
     *
     * <p>The first argument is treated as the name of a system
     * property.  System properties are accessible through the {@link
     * java.lang.System#getProperty(java.lang.String)} method. The
     * string value of this property is then interpreted as a {@code
     * long} value using the grammar supported by {@link Long#decode decode}
     * and a {@code Long} object representing this value is returned.
     *
     * <p>The second argument is the default value. A {@code Long} object
     * that represents the value of the second argument is returned if there
     * is no property of the specified name, if the property does not have
     * the correct numeric format, or if the specified name is empty or null.
     *
     * <p>In other words, this method returns a {@code Long} object equal
     * to the value of:
     *
     * <blockquote>
     *  {@code getLong(nm, new Long(val))}
     * </blockquote>
     *
     * but in practice it may be implemented in a manner such as:
     *
     * <blockquote><pre>
     * Long result = getLong(nm, null);
     * return (result == null) ? new Long(val) : result;
     * </pre></blockquote>
     *
     * to avoid the unnecessary allocation of a {@code Long} object when
     * the default value is not needed.
     *
     * @param   nm    property name.
     * @param   val   default value.
     * @return  the {@code Long} value of the property.
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
     * Returns the {@code long} value of the system property with
     * the specified name.  The first argument is treated as the name
     * of a system property.  System properties are accessible through
     * the {@link java.lang.System#getProperty(java.lang.String)}
     * method. The string value of this property is then interpreted
     * as a {@code long} value, as per the
     * {@link Long#decode decode} method, and a {@code Long} object
     * representing this value is returned; in summary:
     *
     * <ul>
     * <li>If the property value begins with the two ASCII characters
     * {@code 0x} or the ASCII character {@code #}, not followed by
     * a minus sign, then the rest of it is parsed as a hexadecimal integer
     * exactly as for the method {@link #valueOf(java.lang.String, int)}
     * with radix 16.
     * <li>If the property value begins with the ASCII character
     * {@code 0} followed by another character, it is parsed as
     * an octal integer exactly as by the method {@link
     * #valueOf(java.lang.String, int)} with radix 8.
     * <li>Otherwise the property value is parsed as a decimal
     * integer exactly as by the method
     * {@link #valueOf(java.lang.String, int)} with radix 10.
     * </ul>
     *
     * <p>Note that, in every case, neither {@code L}
     * ({@code '\u005Cu004C'}) nor {@code l}
     * ({@code '\u005Cu006C'}) is permitted to appear at the end
     * of the property value as a type indicator, as would be
     * permitted in Java programming language source code.
     *
     * <p>The second argument is the default value. The default value is
     * returned if there is no property of the specified name, if the
     * property does not have the correct numeric format, or if the
     * specified name is empty or {@code null}.
     *
     * @param   nm   property name.
     * @param   val   default value.
     * @return  the {@code Long} value of the property.
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
     * Compares two {@code Long} objects numerically.
     *
     * @param   anotherLong   the {@code Long} to be compared.
     * @return  the value {@code 0} if this {@code Long} is
     *          equal to the argument {@code Long}; a value less than
     *          {@code 0} if this {@code Long} is numerically less
     *          than the argument {@code Long}; and a value greater
     *          than {@code 0} if this {@code Long} is numerically
     *           greater than the argument {@code Long} (signed
     *           comparison).
     * @since   1.2
     */
    public int compareTo(Long anotherLong) {
        return compare(this.value, anotherLong.value);
    }

    /**
     * Compares two {@code long} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Long.valueOf(x).compareTo(Long.valueOf(y))
     * </pre>
     *
     * @param  x the first {@code long} to compare
     * @param  y the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     * @since 1.7
     */
    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * Compares two {@code long} values numerically treating the values
     * as unsigned.
     *
     * @param  x the first {@code long} to compare
     * @param  y the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y}; a value less
     *         than {@code 0} if {@code x < y} as unsigned values; and
     *         a value greater than {@code 0} if {@code x > y} as
     *         unsigned values
     * @since 1.8
     */
    public static int compareUnsigned(long x, long y) {
        return compare(x + MIN_VALUE, y + MIN_VALUE);
    }


    /**
     * Returns the unsigned quotient of dividing the first argument by
     * the second where each argument and the result is interpreted as
     * an unsigned value.
     *
     * <p>Note that in two's complement arithmetic, the three other
     * basic arithmetic operations of add, subtract, and multiply are
     * bit-wise identical if the two operands are regarded as both
     * being signed or both being unsigned.  Therefore separate {@code
     * addUnsigned}, etc. methods are not provided.
     *
     * @param dividend the value to be divided
     * @param divisor the value doing the dividing
     * @return the unsigned quotient of the first argument divided by
     * the second argument
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
     * Returns the unsigned remainder from dividing the first argument
     * by the second where each argument and the result is interpreted
     * as an unsigned value.
     *
     * @param dividend the value to be divided
     * @param divisor the value doing the dividing
     * @return the unsigned remainder of the first argument divided by
     * the second argument
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
     * The number of bits used to represent a {@code long} value in two's
     * complement binary form.
     *
     * @since 1.5
     */
    @Native public static final int SIZE = 64;

    /**
     * The number of bytes used to represent a {@code long} value in two's
     * complement binary form.
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * Returns a {@code long} value with at most a single one-bit, in the
     * position of the highest-order ("leftmost") one-bit in the specified
     * {@code long} value.  Returns zero if the specified value has no
     * one-bits in its two's complement binary representation, that is, if it
     * is equal to zero.
     *
     * @param i the value whose highest one bit is to be computed
     * @return a {@code long} value with a single one-bit, in the position
     *     of the highest-order one-bit in the specified value, or zero if
     *     the specified value is itself equal to zero.
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
     * Returns a {@code long} value with at most a single one-bit, in the
     * position of the lowest-order ("rightmost") one-bit in the specified
     * {@code long} value.  Returns zero if the specified value has no
     * one-bits in its two's complement binary representation, that is, if it
     * is equal to zero.
     *
     * @param i the value whose lowest one bit is to be computed
     * @return a {@code long} value with a single one-bit, in the position
     *     of the lowest-order one-bit in the specified value, or zero if
     *     the specified value is itself equal to zero.
     * @since 1.5
     */
    public static long lowestOneBit(long i) {
        // HD, Section 2-1
        return i & -i;
    }

    /**
     * Returns the number of zero bits preceding the highest-order
     * ("leftmost") one-bit in the two's complement binary representation
     * of the specified {@code long} value.  Returns 64 if the
     * specified value has no one-bits in its two's complement representation,
     * in other words if it is equal to zero.
     *
     * <p>Note that this method is closely related to the logarithm base 2.
     * For all positive {@code long} values x:
     * <ul>
     * <li>floor(log<sub>2</sub>(x)) = {@code 63 - numberOfLeadingZeros(x)}
     * <li>ceil(log<sub>2</sub>(x)) = {@code 64 - numberOfLeadingZeros(x - 1)}
     * </ul>
     *
     * @param i the value whose number of leading zeros is to be computed
     * @return the number of zero bits preceding the highest-order
     *     ("leftmost") one-bit in the two's complement binary representation
     *     of the specified {@code long} value, or 64 if the value
     *     is equal to zero.
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
     * Returns the number of zero bits following the lowest-order ("rightmost")
     * one-bit in the two's complement binary representation of the specified
     * {@code long} value.  Returns 64 if the specified value has no
     * one-bits in its two's complement representation, in other words if it is
     * equal to zero.
     *
     * @param i the value whose number of trailing zeros is to be computed
     * @return the number of zero bits following the lowest-order ("rightmost")
     *     one-bit in the two's complement binary representation of the
     *     specified {@code long} value, or 64 if the value is equal
     *     to zero.
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
     * Returns the number of one-bits in the two's complement binary
     * representation of the specified {@code long} value.  This function is
     * sometimes referred to as the <i>population count</i>.
     *
     * @param i the value whose bits are to be counted
     * @return the number of one-bits in the two's complement binary
     *     representation of the specified {@code long} value.
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
     * Returns the value obtained by rotating the two's complement binary
     * representation of the specified {@code long} value left by the
     * specified number of bits.  (Bits shifted out of the left hand, or
     * high-order, side reenter on the right, or low-order.)
     *
     * <p>Note that left rotation with a negative distance is equivalent to
     * right rotation: {@code rotateLeft(val, -distance) == rotateRight(val,
     * distance)}.  Note also that rotation by any multiple of 64 is a
     * no-op, so all but the last six bits of the rotation distance can be
     * ignored, even if the distance is negative: {@code rotateLeft(val,
     * distance) == rotateLeft(val, distance & 0x3F)}.
     *
     * @param i the value whose bits are to be rotated left
     * @param distance the number of bit positions to rotate left
     * @return the value obtained by rotating the two's complement binary
     *     representation of the specified {@code long} value left by the
     *     specified number of bits.
     * @since 1.5
     */
    public static long rotateLeft(long i, int distance) {
        return (i << distance) | (i >>> -distance);
    }

    /**
     * Returns the value obtained by rotating the two's complement binary
     * representation of the specified {@code long} value right by the
     * specified number of bits.  (Bits shifted out of the right hand, or
     * low-order, side reenter on the left, or high-order.)
     *
     * <p>Note that right rotation with a negative distance is equivalent to
     * left rotation: {@code rotateRight(val, -distance) == rotateLeft(val,
     * distance)}.  Note also that rotation by any multiple of 64 is a
     * no-op, so all but the last six bits of the rotation distance can be
     * ignored, even if the distance is negative: {@code rotateRight(val,
     * distance) == rotateRight(val, distance & 0x3F)}.
     *
     * @param i the value whose bits are to be rotated right
     * @param distance the number of bit positions to rotate right
     * @return the value obtained by rotating the two's complement binary
     *     representation of the specified {@code long} value right by the
     *     specified number of bits.
     * @since 1.5
     */
    public static long rotateRight(long i, int distance) {
        return (i >>> distance) | (i << -distance);
    }

    /**
     * Returns the value obtained by reversing the order of the bits in the
     * two's complement binary representation of the specified {@code long}
     * value.
     *
     * @param i the value to be reversed
     * @return the value obtained by reversing order of the bits in the
     *     specified {@code long} value.
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
     * Returns the signum function of the specified {@code long} value.  (The
     * return value is -1 if the specified value is negative; 0 if the
     * specified value is zero; and 1 if the specified value is positive.)
     *
     * @param i the value whose signum is to be computed
     * @return the signum function of the specified {@code long} value.
     * @since 1.5
     */
    public static int signum(long i) {
        // HD, Section 2-7
        return (int) ((i >> 63) | (-i >>> 63));
    }

    /**
     * Returns the value obtained by reversing the order of the bytes in the
     * two's complement representation of the specified {@code long} value.
     *
     * @param i the value whose bytes are to be reversed
     * @return the value obtained by reversing the bytes in the specified
     *     {@code long} value.
     * @since 1.5
     */
    public static long reverseBytes(long i) {
        i = (i & 0x00ff00ff00ff00ffL) << 8 | (i >>> 8) & 0x00ff00ff00ff00ffL;
        return (i << 48) | ((i & 0xffff0000L) << 16) |
            ((i >>> 16) & 0xffff0000L) | (i >>> 48);
    }

    /**
     * Adds two {@code long} values together as per the + operator.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the sum of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static long sum(long a, long b) {
        return a + b;
    }

    /**
     * Returns the greater of two {@code long} values
     * as if by calling {@link Math#max(long, long) Math.max}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the greater of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static long max(long a, long b) {
        return Math.max(a, b);
    }

    /**
     * Returns the smaller of two {@code long} values
     * as if by calling {@link Math#min(long, long) Math.min}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the smaller of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static long min(long a, long b) {
        return Math.min(a, b);
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    @Native private static final long serialVersionUID = 4290774380558885855L;
}
