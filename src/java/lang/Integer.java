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

/**
 * {@code Integer} 类在对象中包装了一个基本类型 {@code int} 的值。
 * {@code Integer} 类型的对象包含一个 {@code int} 类型的字段。
 *
 * <p>此外，该类提供了多个方法，
 * 能在 {@code int} 类型和 {@code String} 类型之间互相转换，
 * 还提供了处理 {@code int} 类型时非常有用的其他一些常量和方法。
 *
 * <p>实现注意事项：“bit twiddling”方法
 * （如 {@link #highestOneBit(int) highestOneBit} 和
 * {@link #numberOfTrailingZeros(int) numberOfTrailingZeros}）的实现基于
 * Henry S. Warren, Jr.撰写的 <i>Hacker's Delight</i>（Addison Wesley, 2002）中的一些有关材料。
 *
 * @author  Lee Boynton
 * @author  Arthur van Hoff
 * @author  Josh Bloch
 * @author  Joseph D. Darcy
 * @since JDK1.0
 */
public final class Integer extends Number implements Comparable<Integer> {
    /**
     * 值为 -2<sup>31</sup> 的常量，它表示 {@code int} 类型能够表示的最小值。
     */
    @Native public static final int   MIN_VALUE = 0x80000000;

    /**
     * 值为 2<sup>31</sup>-1 的常量，它表示 {@code int} 类型能够表示的最大值。
     */
    @Native public static final int   MAX_VALUE = 0x7fffffff;

    /**
     * 表示基本类型 {@code int} 的 {@code Class} 实例。
     *
     * @since   JDK1.1
     */
    @SuppressWarnings("unchecked")
    public static final Class<Integer>  TYPE = (Class<Integer>) Class.getPrimitiveClass("int");

    /**
     * 所有可能的代表数字的字符
     */
    final static char[] digits = {
        '0' , '1' , '2' , '3' , '4' , '5' ,
        '6' , '7' , '8' , '9' , 'a' , 'b' ,
        'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
        'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
        'o' , 'p' , 'q' , 'r' , 's' , 't' ,
        'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    };

    /**
     * 返回用第二个参数指定基数表示的第一个参数的字符串表示形式。
     *
     * <p>如果基数小于 {@code Character.MIN_RADIX} 或者
     * 大于 {@code Character.MAX_RADIX}，则改用基数 {@code 10}。
     *
     * <p>如果第一个参数为负，
     * 则结果中的第一个元素为 ASCII 的减号 {@code '-'} ({@code '\u005Cu002D'})。
     * 如果第一个参数为非负，则没有符号字符出现在结果中。
     *
     * <p>结果中的剩余字符表示第一个参数的大小。
     * 如果大小为零，则用一个零字符 {@code '0'} ({@code '\u005Cu0030'}) 表示；
     * 否则，大小的表示形式中的第一个字符将不是零字符。
     * 用以下 ASCII 字符作为数字：
     *
     * <blockquote>
     *   {@code 0123456789abcdefghijklmnopqrstuvwxyz}
     * </blockquote>
     *
     * 其范围是从 {@code '\u005Cu0030'} 到 {@code '\u005Cu0039'} 和
     * 从 {@code '\u005Cu0061'} 到 {@code '\u005Cu007A'}。
     * 如果 {@code radix} 为 <var>N</var>, 则按照所示顺序，使用这些字符中的前 <var>N</var> 个作为其数字。
     * 因此，十六进制（基数为 16）的数字是 {@code 0123456789abcdef}。
     * 如果希望得到大写字母，则可以在结果上调用 {@link java.lang.String#toUpperCase()} 方法：
     *
     * <blockquote>
     *  {@code Integer.toString(n, 16).toUpperCase()}
     * </blockquote>
     *
     * @param   i       要转换成字符串的整数。
     * @param   radix   用于字符串表示形式的基数。
     * @return  使用指定基数的参数的字符串表示形式。
     * @see     java.lang.Character#MAX_RADIX
     * @see     java.lang.Character#MIN_RADIX
     */
    public static String toString(int i, int radix) {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            radix = 10;

        /* Use the faster version */
        if (radix == 10) {
            return toString(i);
        }

        char buf[] = new char[33];
        boolean negative = (i < 0);
        int charPos = 32;

        if (!negative) {
            i = -i;
        }

        while (i <= -radix) {
            buf[charPos--] = digits[-(i % radix)];
            i = i / radix;
        }
        buf[charPos] = digits[-i];

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (33 - charPos));
    }

    /**
     * 以第二个参数指定进制的无符号整数形式返回第一个整数参数的字符串表示形式。
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
     * <p>基数和作为数字使用的字符的行为与当中{@link #toString(int, int) toString} 相同。
     *
     * @param   i       要转换成无符号字符串的整数。
     * @param   radix   字符串表达形式的基数。
     * @return  参数在指定基数中的无符号字符串表示形式。
     * @see     #toString(int, int)
     * @since 1.8
     */
    public static String toUnsignedString(int i, int radix) {
        return Long.toUnsignedString(toUnsignedLong(i), radix);
    }

    /**
     * 以十六进制（基数&nbsp;16）无符号整数形式返回一个整数参数的字符串表示形式。
     *
     * <p>如果参数为负，那么无符号整数值为参数加上  2<sup>32</sup>；
     * 否则等于该参数。将该值转换为十六进制（基数&nbsp;16）的无前导 {@code 0} 的 ASCII 数字字符串。
     *
     * <p>参数的值可以从字符串 {@code s} 中通过调用{@link
     * Integer#parseUnsignedInt(String, int)
     * Integer.parseUnsignedInt(s, 16)} 得到。
     *
     * <p>如果无符号数的大小值为零，
     * 则用一个零字符 {@code '0'} ({@code '\u005Cu0030'}) 表示它；
     * 否则，无符号数大小的表示形式中的第一个字符将不是零字符。
     * 用以下字符作为十六进制数字：
     *
     * <blockquote>
     *  {@code 0123456789abcdef}
     * </blockquote>
     *
     * 这些字符的范围是从 {@code '\u005Cu0030'} 到 {@code '\u005Cu0039'} 和
     * 从 {@code '\u005Cu0061'} 到 {@code '\u005Cu0066'}。
     * 如果希望得到大写字母，可以在结果上调用  {@link java.lang.String#toUpperCase()} 方法：
     *
     * <blockquote>
     *  {@code Integer.toHexString(n).toUpperCase()}
     * </blockquote>
     *
     * @param   i   要转换成字符串的整数。
     * @return  参数的十六进制（基数&nbsp;16)无符号整数值的字符串表示形式。
     * @see #parseUnsignedInt(String, int)
     * @see #toUnsignedString(int, int)
     * @since   JDK1.0.2
     */
    public static String toHexString(int i) {
        return toUnsignedString0(i, 4);
    }

    /**
     * 以八进制（基数&nbsp;8）无符号整数形式返回一个整数参数的字符串表示形式。
     *
     * <p>如果参数为负，该无符号整数值为参数加上 2<sup>32</sup>；否则等于该参数。
     * 该值被转换成八进制（基数&nbsp;8）ASCII 数字的字符串，且没有附加前导 {@code 0}。
     *
     * <p>参数的值可以从字符串 {@code s} 中通过调用{@link
     * Integer#parseUnsignedInt(String, int)
     * Integer.parseUnsignedInt(s, 8)} 得到。
     *
     * <p>如果无符号数大小为零，则用一个零字符 {@code '0'} ({@code '\u005Cu0030'}) 表示它；
     * 否则，无符号数大小的表示形式中的第一个字符将不是零字符。
     * 用以下字符作为八进制数字：
     *
     * <blockquote>
     * {@code 01234567}
     * </blockquote>
     *
     * 它们是从 {@code '\u005Cu0030'} 到 {@code '\u005Cu0037'} 的字符。
     *
     * @param   i   要转换成字符串的整数。
     * @return  用八进制参数（基数 &nbsp;8）表示的无符号整数值的字符串表示形式。
     * @see #parseUnsignedInt(String, int)
     * @see #toUnsignedString(int, int)
     * @since   JDK1.0.2
     */
    public static String toOctalString(int i) {
        return toUnsignedString0(i, 3);
    }

    /**
     * 以二进制（基数&nbsp;2）无符号整数形式返回一个整数参数的字符串表示形式。
     *
     * <p>如果参数为负，该无符号整数值为参数加上 2<sup>32</sup>；否则等于该参数。
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
     * @param   i    要转换为字符串的整数。
     * @return  用二进制参数（基数 &nbsp;2）表示的无符号整数值的字符串表示形式。
     * @see #parseUnsignedInt(String, int)
     * @see #toUnsignedString(int, int)
     * @since   JDK1.0.2
     */
    public static String toBinaryString(int i) {
        return toUnsignedString0(i, 1);
    }

    /**
     * Convert the integer to an unsigned number.
     */
    private static String toUnsignedString0(int val, int shift) {
        // assert shift > 0 && shift <=5 : "Illegal shift value";
        int mag = Integer.SIZE - Integer.numberOfLeadingZeros(val);
        int chars = Math.max(((mag + (shift - 1)) / shift), 1);
        char[] buf = new char[chars];

        formatUnsignedInt(val, shift, buf, 0, chars);

        // Use special constructor which takes over "buf".
        return new String(buf, true);
    }

    /**
     * 将 int（视为无符号）格式化为字符 buffer。
     * @param val 要格式化的无符号 int
     * @param shift 要格式化的 log2 的基数 (4 代表十六进制，3 代表八进制，1 代表二进制)
     * @param buf 要写入的字符 buffer
     * @param offset 从目标缓冲区开始的偏移量
     * @param len 要写入的字符数
     * @return  使用的最低字符位置
     */
     static int formatUnsignedInt(int val, int shift, char[] buf, int offset, int len) {
        int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] = Integer.digits[val & mask];
            val >>>= shift;
        } while (val != 0 && charPos > 0);

        return charPos;
    }

    final static char [] DigitTens = {
        '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
        '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
        '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
        '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
        '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
        '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
        '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
        '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
        '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
        '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
        } ;

    final static char [] DigitOnes = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        } ;

        // I use the "invariant division by multiplication" trick to
        // accelerate Integer.toString.  In particular we want to
        // avoid division by 10.
        //
        // The "trick" has roughly the same performance characteristics
        // as the "classic" Integer.toString code on a non-JIT VM.
        // The trick avoids .rem and .div calls but has a longer code
        // path and is thus dominated by dispatch overhead.  In the
        // JIT case the dispatch overhead doesn't exist and the
        // "trick" is considerably faster than the classic code.
        //
        // TODO-FIXME: convert (x * 52429) into the equiv shift-add
        // sequence.
        //
        // RE:  Division by Invariant Integers using Multiplication
        //      T Gralund, P Montgomery
        //      ACM PLDI 1994
        //

    /**
     * 返回一个表示指定整数的 {@code String} 对象。
     * 将该参数转换为有符号的十进制表示形式，以字符串形式返回它，
     * 就好像将参数和基数 10 作为参数赋予 {@link * #toString(int, int)}  方法。
     *
     * @param   i   要转换的整数。
     * @return  十进制（基数&nbsp;10）参数的字符串表示形式。
     */
    public static String toString(int i) {
        if (i == Integer.MIN_VALUE)
            return "-2147483648";
        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);
        char[] buf = new char[size];
        getChars(i, size, buf);
        return new String(buf, true);
    }

    /**
     * 返回参数作为无符号小数值的字符串表示形式。
     *
     * 该参数被转换为无符号的小数表示，并以字符串形式返回，
     * 就像参数和基数 10 被作为参数赋给 {@link #toUnsignedString(int, int)} 方法一样。
     *
     * @param   i  要转换成无符号字符串的整数。
     * @return  参数的无符号字符串表示形式。
     * @see     #toUnsignedString(int, int)
     * @since 1.8
     */
    public static String toUnsignedString(int i) {
        return Long.toString(toUnsignedLong(i));
    }

    /**
     * 将表示整数i的字符放在字符数组buf中。
     * 字符从指定索引(独占)的最不重要的数字开始向后放置到缓冲区中，
     * 并从那里向后工作。
     *
     * 如果i = Integer.MIN_VALUE会失败
     */
    static void getChars(int i, int index, char[] buf) {
        int q, r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Generate two digits per iteration
        while (i >= 65536) {
            q = i / 100;
        // really: r = i - (q * 100);
            r = i - ((q << 6) + (q << 5) + (q << 2));
            i = q;
            buf [--charPos] = DigitOnes[r];
            buf [--charPos] = DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (;;) {
            q = (i * 52429) >>> (16+3);
            r = i - ((q << 3) + (q << 1));  // r = i-(q*10) ...
            buf [--charPos] = digits [r];
            i = q;
            if (i == 0) break;
        }
        if (sign != 0) {
            buf [--charPos] = sign;
        }
    }

    final static int [] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
                                      99999999, 999999999, Integer.MAX_VALUE };

    // Requires positive x
    static int stringSize(int x) {
        for (int i=0; ; i++)
            if (x <= sizeTable[i])
                return i+1;
    }

    /**
     * 使用第二个参数指定的基数，将字符串参数解析为有符号的整数。
     * 除了第一个字符可以是用来表示负值的 ASCII 减号 {@code '-'} ({@code '\u005Cu002D'}) 或
     * ASCII 加号 {@code '+'} ({@code '\u005Cu002B'}) 外，
     * 字符串中的字符必须都是指定基数的数字（通过 {@link java.lang.Character#digit(char, int)} 是否返回一个负值确定）。
     * 返回得到的整数值。
     *
     * <p>如果发生以下任意一种情况，则抛出一个 {@code NumberFormatException} 类型的异常：
     * <ul>
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
     * <li>字符串表示的值不是 {@code int} 类型的值。
     * </ul>
     *
     * <p>示例：
     * <blockquote><pre>
     * parseInt("0", 10) returns 0
     * parseInt("473", 10) returns 473
     * parseInt("+42", 10) returns 42
     * parseInt("-0", 10) returns 0
     * parseInt("-FF", 16) returns -255
     * parseInt("1100110", 2) returns 102
     * parseInt("2147483647", 10) returns 2147483647
     * parseInt("-2147483648", 10) returns -2147483648
     * parseInt("2147483648", 10) throws a NumberFormatException
     * parseInt("99", 8) throws a NumberFormatException
     * parseInt("Kona", 10) throws a NumberFormatException
     * parseInt("Kona", 27) returns 411787
     * </pre></blockquote>
     *
     * @param      s   包含要解析的整数表示形式的 {@code String}
     * @param      radix   解析 {@code s} 时使用的基数。
     * @return     使用指定基数的字符串参数表示的整数。
     * @exception  NumberFormatException 如果 {@code String} 不包含可解析的 {@code int}。
     */
    public static int parseInt(String s, int radix)
                throws NumberFormatException
    {
        /*
         * WARNING: This method may be invoked early during VM initialization
         * before IntegerCache is initialized. Care must be taken to not use
         * the valueOf method.
         */

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

        int result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;
        int multmin;
        int digit;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
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
     * 将字符串参数作为有符号的十进制整数进行解析。
     * 除了第一个字符可以是用来表示负值的 ASCII 减号 '-' ( '\u002D') 或
     * ASCII 加号 {@code '+'} ({@code '\u005Cu002B'}) 外，
     * 字符串中的字符都必须是十进制数字。
     * 返回得到的整数值，就好像将该参数和基数 10 作为参数赋予 {@link #parseInt(java.lang.String, int)} 方法一样。
     *
     * @param s    包含要解析的 {@code int} 表示形式的 {@code String}。
     * @return     用十进制参数表示的整数值。
     * @exception  NumberFormatException  如果字符串不包含可解析的整数。
     */
    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s,10);
    }

    /**
     * 将字符串参数解析为第二个参数指定的基数中的无符号整数。
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
     * <li>字符串表示的值大于无符号 {@code int} 的最大值 2<sup>32</sup>-1。
     *
     * </ul>
     *
     * @param      s   包含要解析的无符号整数表示的字符串
     * @param      radix   解析 {@code s} 时要使用的基数。
     * @return     用指定基数的字符串参数表示的整数。
     * @throws     NumberFormatException 如果字符串不包含可解析的整数。
     * @since 1.8
     */
    public static int parseUnsignedInt(String s, int radix)
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
                if (len <= 5 || // Integer.MAX_VALUE in Character.MAX_RADIX is 6 digits
                    (radix == 10 && len <= 9) ) { // Integer.MAX_VALUE in base 10 is 10 digits
                    return parseInt(s, radix);
                } else {
                    long ell = Long.parseLong(s, radix);
                    if ((ell & 0xffff_ffff_0000_0000L) == 0) {
                        return (int) ell;
                    } else {
                        throw new
                            NumberFormatException(String.format("String value %s exceeds " +
                                                                "range of unsigned int.", s));
                    }
                }
            }
        } else {
            throw NumberFormatException.forInputString(s);
        }
    }

    /**
     * 将字符串参数解析为无符号十进制整数。
     * 除了第一个字符可能是ASCII 加号{@code '+'} ({@code '\u005Cu002B'})外，
     * 字符串中的字符必须是十进制的。
     * 返回结果的整数值，就像将参数和基数10作为 {@link #parseUnsignedInt(java.lang.String, int)} 方法的参数一样。
     *
     * @param s   包含要解析的无符号整数表示的字符串
     * @return    十进制参数表示的无符号整数值。
     * @throws    NumberFormatException  如果字符串不包含可解析的整数。
     * @since 1.8
     */
    public static int parseUnsignedInt(String s) throws NumberFormatException {
        return parseUnsignedInt(s, 10);
    }

    /**
     * 返回一个 {@code Integer} 对象，
     * 该对象中保存了用第二个参数提供的基数进行解析时从指定的 {@code String} 中提取的值。
     * 将第一个参数解释为用第二个参数指定的基数表示的有符号整数，
     * 就好像将这些参数赋予 {@link #parseInt(java.lang.String, int)} 方法一样。
     * 结果是一个表示字符串指定的整数值的 Integer 对象。
     *
     * <p>换句话说，该方法返回一个等于以下值的 {@code Integer} 对象：
     *
     * <blockquote>
     *  {@code new Integer(Integer.parseInt(s, radix))}
     * </blockquote>
     *
     * @param      s   要解析的字符串。
     * @param      radix 解释 {@code s} 时使用的基数。
     * @return     一个 {@code Integer} 对象，它含有字符串参数（以指定的基数）所表示的数值。
     * @exception NumberFormatException 如果 {@code String} 不包含可解析的 {@code int}。
     */
    public static Integer valueOf(String s, int radix) throws NumberFormatException {
        return Integer.valueOf(parseInt(s,radix));
    }

    /**
     * 返回保存指定的 {@code String} 的值的 {@code Integer} 对象。
     * 将该参数解释为表示一个有符号的十进制整数,
     * 就好像将该参数赋予 {@link #parseInt(java.lang.String)} 方法一样。
     * 结果是一个表示字符串指定的整数值的 {@code Integer} 对象。
     *
     * <p>换句话说，该方法返回一个等于以下值的 {@code Integer} 对象：
     *
     * <blockquote>
     *  {@code new Integer(Integer.parseInt(s))}
     * </blockquote>
     *
     * @param      s   要解析的字符串。
     * @return     保存字符串参数表示的值的 {@code Integer} 对象。
     * @exception  NumberFormatException  如果字符串不能解析为一个整数。
     */
    public static Integer valueOf(String s) throws NumberFormatException {
        return Integer.valueOf(parseInt(s, 10));
    }

    /**
     * Cache to support the object identity semantics of autoboxing for values between
     * -128 and 127 (inclusive) as required by JLS.
     *
     * The cache is initialized on first usage.  The size of the cache
     * may be controlled by the {@code -XX:AutoBoxCacheMax=<size>} option.
     * During VM initialization, java.lang.Integer.IntegerCache.high property
     * may be set and saved in the private system properties in the
     * sun.misc.VM class.
     */

    private static class IntegerCache {
        static final int low = -128;
        static final int high;
        static final Integer cache[];

        static {
            // high value may be configured by property
            int h = 127;
            String integerCacheHighPropValue =
                sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            if (integerCacheHighPropValue != null) {
                try {
                    int i = parseInt(integerCacheHighPropValue);
                    i = Math.max(i, 127);
                    // Maximum array size is Integer.MAX_VALUE
                    h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
                } catch( NumberFormatException nfe) {
                    // If the property cannot be parsed into an int, ignore it.
                }
            }
            high = h;

            cache = new Integer[(high - low) + 1];
            int j = low;
            for(int k = 0; k < cache.length; k++)
                cache[k] = new Integer(j++);

            // range [-128, 127] must be interned (JLS7 5.1.7)
            assert IntegerCache.high >= 127;
        }

        private IntegerCache() {}
    }

    /**
     * 返回一个表示指定的 {@code int} 值的 {@code Integer} 实例。
     * 如果不需要新的 {@code Integer} 实例，
     * 则通常应优先使用该方法，而不是构造方法 {@link #Integer(int)}，
     * 因为该方法有可能通过缓存经常请求的值而显著提高空间和时间性能。
     *
     * 该方法将始终缓存值在范围-128到127之间，并可能缓存此范围之外的其他值。
     *
     * @param  i 一个 {@code int} 值。
     * @return 表示 {@code i} 的 {@code Integer} 实例。
     * @since  1.5
     */
    public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }

    /**
     * {@code Integer} 的值。
     *
     * @serial
     */
    private final int value;

    /**
     * 构造一个新分配的 {@code Integer} 对象，它表示指定的 {@code int} 值。
     *
     * @param   value   {@code Integer} 对象表示的值。
     */
    public Integer(int value) {
        this.value = value;
    }

    /**
     * 构造一个新分配的 {@code Integer} 对象，它表示 {@code String} 参数所指示的 {@code int} 值。
     * 使用与 {@code parseInt} 方法（对基数为 10 的值）相同的方式将该字符串转换成 {@code int} 值。
     *
     * @param      s   要转换为 {@code Integer} 的 {@code String}。
     * @exception  NumberFormatException   如果 {@code String} 不包含可解析的整数。
     * @see        java.lang.Integer#parseInt(java.lang.String, int)
     */
    public Integer(String s) throws NumberFormatException {
        this.value = parseInt(s, 10);
    }

    /**
     * 以 {@code byte} 类型返回该 {@code Integer} 的值。
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * 以 {@code short} 类型返回该 {@code Integer} 的值。
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     * 以 {@code int} 类型返回该 {@code Integer} 的值。
     */
    public int intValue() {
        return value;
    }

    /**
     * 以 {@code long} 类型返回该 {@code Integer} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     * @see Integer#toUnsignedLong(int)
     */
    public long longValue() {
        return (long)value;
    }

    /**
     * 以 {@code float} 类型返回该 {@code Integer} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public float floatValue() {
        return (float)value;
    }

    /**
     * 以 {@code double} 类型返回该 {@code Integer} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)value;
    }

    /**
     * 返回一个表示该 {@code Integer} 值的 {@code String} 对象。
     * 将该参数转换为有符号的十进制表示形式，
     * 并以字符串的形式返回它，
     * 就好像将该整数值作为参数赋予 {@link java.lang.Integer#toString(int)} 方法一样。
     *
     * @return  该对象的值（基数&nbsp;10）的字符串表示形式。
     */
    public String toString() {
        return toString(value);
    }

    /**
     * 返回此 {@code Integer} 的哈希码。
     *
     * @return  该对象的哈希码值，它的值即为该 {@code Integer} 对象表示的基本 {@code int} 类型的数值。
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    /**
     * 返回 {@code int} 值的哈希值；和 {@code Integer.hashCode()} 兼容。
     *
     * @param value 要哈希的值
     * @since 1.8
     *
     * @return {@code int} 值的哈希值。
     */
    public static int hashCode(int value) {
        return value;
    }

    /**
     * 比较此对象与指定对象。当且仅当参数不为 {@code null}，
     * 并且是一个与该对象包含相同 {@code int} 值的 {@code Integer} 对象时，结果为 {@code true}。
     *
     * @param   obj   要比较的对象。
     * @return  如果对象相同，则返回 {@code true}，否则返回 {@code false}。
     */
    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return value == ((Integer)obj).intValue();
        }
        return false;
    }

    /**
     * 确定具有指定名称的系统属性的整数值。
     *
     * <p>第一个参数被视为系统属性的名称。
     * 通过 {@link java.lang.System#getProperty(java.lang.String)} 方法可以访问系统属性。
     * 然后，根据每个 {@link Integer#decode decode} 方法，
     * 将该属性的字符串值解释为一个整数值，并返回一个表示该值的 Integer 对象。
     *
     * <p>如果没有具有指定名称的属性，或者指定名称为空或 {@code null}，
     * 或者属性的数字格式不正确，则返回 {@code null}。
     *
     * <p>换句话说，该方法返回一个等于以下值的 {@code Integer} 对象：
     *
     * <blockquote>
     *  {@code getInteger(nm, null)}
     * </blockquote>
     *
     * @param   nm   属性名。
     * @return  属性的 {@code Integer} 值。
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static Integer getInteger(String nm) {
        return getInteger(nm, null);
    }

    /**
     * 确定具有指定名称的系统属性的整数值。
     *
     * <p>第一个参数被视为系统属性的名称。
     * 通过 {@link java.lang.System#getProperty(java.lang.String)} 方法可以访问系统属性。
     * 然后，根据每个 {@link Integer#decode decode} 方法，
     * 将该属性的字符串值解释为一个整数值，并返回一个表示该值的 Integer 对象。
     *
     * <p>第二个参数是默认值。如果未具有指定名称的属性，
     * 或者属性的数字格式不正确，或者指定名称为空或 {@code null}，
     * 则返回一个表示第二个参数的值的 {@code Integer} 对象。
     *
     * <p>换句话说，该方法返回一个等于以下值的 {@code Integer} 对象：
     *
     * <blockquote>
     *  {@code getInteger(nm, new Integer(val))}
     * </blockquote>
     *
     * 但在实践中可能会用以下类似方式实现它：
     *
     * <blockquote><pre>
     * Integer result = getInteger(nm, null);
     * return (result == null) ? new Integer(val) : result;
     * </pre></blockquote>
     *
     * 从而避免在无需默认值时分配不必要的 {@code Integer} 对象。
     *
     * @param   nm   属性名。
     * @param   val   默认值。
     * @return  属性的 {@code Integer} 值。
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static Integer getInteger(String nm, int val) {
        Integer result = getInteger(nm, null);
        return (result == null) ? Integer.valueOf(val) : result;
    }

    /**
     * 返回具有指定名称的系统属性的整数值。
     * 第一个参数被视为系统属性的名称。
     * 通过 {@link java.lang.System#getProperty(java.lang.String)} 方法可以访问系统属性。
     * 然后，根据每个 {@link Integer#decode decode} 方法，
     * 将该属性的字符串值解释为一个整数值，并返回一个表示该值的 {@code Integer} 对象。
     * 总之：
     *
     * <ul><li>如果属性值以两个 ASCII 字符 {@code 0x} 或者 ASCII 字符 {@code #} 开始，
     *         并且后面没有减号，则将它的剩余部分解析为十六进制整数，
     *         就好像以 16 为基数调用 {@link #valueOf(java.lang.String, int)} 方法一样。
     * <li>如果属性值以 ASCII 字符 {@code 0} 开始，后面还有其他字符，
     *     则将它解析为八进制整数，
     *     就好像以 8 为基数调用 {@link #valueOf(java.lang.String, int)} 方法一样。
     * <li>否则，将属性值解析为十进制整数，
     *     就好像以 10 为基数调用 {@link #valueOf(java.lang.String, int)} 方法一样。
     * </ul>
     *
     * <p>第二个参数是默认值。如果未具有指定名称的属性，
     * 或者属性的数字格式不正确，或者指定名称为空或 {@code null}，则返回默认值。
     *
     * @param   nm   属性名。
     * @param   val   默认值。
     * @return  属性的 {@code Integer} 值。 
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     System#getProperty(java.lang.String)
     * @see     System#getProperty(java.lang.String, java.lang.String)
     */
    public static Integer getInteger(String nm, Integer val) {
        String v = null;
        try {
            v = System.getProperty(nm);
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        if (v != null) {
            try {
                return Integer.decode(v);
            } catch (NumberFormatException e) {
            }
        }
        return val;
    }

    /**
     * 将 {@code String} 解码为 {@code Integer}。
     * 接受通过以下语法给出的十进制、十六进制和八进制数字：
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
     * @return    保存 {@code nm} 所表示的 {@code int} 值的 {@code Integer} 对象。
     * @exception NumberFormatException  如果 {@code String} 不包含可解析整数。
     * @see java.lang.Integer#parseInt(java.lang.String, int)
     */
    public static Integer decode(String nm) throws NumberFormatException {
        int radix = 10;
        int index = 0;
        boolean negative = false;
        Integer result;

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
            result = Integer.valueOf(nm.substring(index), radix);
            result = negative ? Integer.valueOf(-result.intValue()) : result;
        } catch (NumberFormatException e) {
            // If number is Integer.MIN_VALUE, we'll end up here. The next line
            // handles this case, and causes any genuine format error to be
            // rethrown.
            String constant = negative ? ("-" + nm.substring(index))
                                       : nm.substring(index);
            result = Integer.valueOf(constant, radix);
        }
        return result;
    }

    /**
     * 在数字上比较两个{@code Integer} 对象。
     *
     * @param   anotherInteger   要比较的 {@code Integer}。
     * @return  如果该 {@code Integer} 等于 {@code Integer} 参数，则返回 {@code 0} 值；
     *           如果该 {@code Integer} 在数字上小于 {@code Integer} 参数，则返回小于 {@code 0} 的值；
     *           如果 {@code Integer} 在数字上大于 {@code Integer} 参数，则返回大于 {@code 0} 的值（有符号的比较）。
     * @since   1.2
     */
    public int compareTo(Integer anotherInteger) {
        return compare(this.value, anotherInteger.value);
    }

    /**
     * 在数字上比较两个 {@code int} 值。
     * 返回的值和下面返回的值相同：
     * <pre>
     *    Integer.valueOf(x).compareTo(Integer.valueOf(y))
     * </pre>
     *
     * @param  x 第一个要比较的 {@code int}
     * @param  y 第二个要比较的 {@code int}
     * @return 如果 {@code x == y} 则返回 {@code 0}；
     *          如果 {@code x < y} 则返回 小于 {@code 0} 的值；
     *          如果 {@code x > y} 则返回 大于 {@code 0} 的值；
     * @since 1.7
     */
    public static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * 数值上比较两个 被看作无符号的 {@code int} 值。
     *
     * @param  x 第一个要比较的 {@code int}
     * @param  y 第二个要比较的 {@code int}
     * @return 如果 {@code x == y} 则返回 {@code 0}；
     *          如果看作无符号整数 {@code x < y} 则返回 小于 {@code 0} 的值；
     *          如果看作无符号整数 {@code x > y} 则返回 大于 {@code 0} 的值；
     * @since 1.8
     */
    public static int compareUnsigned(int x, int y) {
        return compare(x + MIN_VALUE, y + MIN_VALUE);
    }

    /**
     * 通过无符号转换将参数转换为 {@code long}。
     * 在无符号转换为 {@code long} 时，{@code long} 的高阶32位为零，低阶32位等于整数参数的位。
     *
     * 因此，零和正的 {@code int} 值被映射到数值上相等的 {@code long} 值，
     * 而负的{@code int} 值被映射到等于输入加上 2<sup>32</sup> 的 {@code long}值。
     *
     * @param  x 要转换到无符号 {@code long} 型的值。
     * @return 通过无符号转换将参数转换为 {@code long}
     * @since 1.8
     */
    public static long toUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
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
    public static int divideUnsigned(int dividend, int divisor) {
        // In lieu of tricky code, for now just use long arithmetic.
        return (int)(toUnsignedLong(dividend) / toUnsignedLong(divisor));
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
    public static int remainderUnsigned(int dividend, int divisor) {
        // In lieu of tricky code, for now just use long arithmetic.
        return (int)(toUnsignedLong(dividend) % toUnsignedLong(divisor));
    }


    // Bit twiddling

    /**
     * 在二进制补码形式中用来表示 {@code int} 值的位数。
     *
     * @since 1.5
     */
    @Native public static final int SIZE = 32;

    /**
     * 在二进制补码形式中用来表示 {@code int} 值的字节数。
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * 返回具有至多单个 1 位的 {@code int} 值，
     * 在指定的 {@code int} 值中最高位（最左边）的 1 位的位置。
     * 如果指定的值在其二进制补码表示形式中不具有 1 位，即它等于零，则返回零。
     *
     * @param i 要计算最高位的值
     * @return 返回具有单个 1 位的 {@code int} 值, 在指定值中最高位的 1 位的位置，
     *          否则，如果指定值本身等于零，则返回零。
     * @since 1.5
     */
    public static int highestOneBit(int i) {
        // HD, Figure 3-1
        i |= (i >>  1);
        i |= (i >>  2);
        i |= (i >>  4);
        i |= (i >>  8);
        i |= (i >> 16);
        return i - (i >>> 1);
    }

    /**
     * 返回具有至多单个 1 位的 {@code int} 值，
     * 在指定的 {@code int} 值中最低位（最右边）的 1 位的位置。
     * 如果指定的值在其二进制补码表示形式中不具有 1 位，即它等于零，则返回零。
     *
     * @param i 要计算最低位的值
     * @return 返回具有单个 1 位的 {@code int} 值, 在指定值中最低位的 1 位的位置，
     *          否则，如果指定值本身等于零，则返回零。
     * @since 1.5
     */
    public static int lowestOneBit(int i) {
        // HD, Section 2-1
        return i & -i;
    }

    /**
     * 在指定 {@code int} 值的二进制补码表示形式中最高位（最左边）的 1 位之前，返回零位的数量。
     * 如果指定值在其二进制补码表示形式中不存在 1 位，换句话说，如果它等于零，则返回 32。
     *
     * <p>注意，此方法与基数为 2 的对数密切相关。对于所有的正 {@code int} 值 x：
     * <ul>
     * <li>floor(log<sub>2</sub>(x)) = {@code 31 - numberOfLeadingZeros(x)}
     * <li>ceil(log<sub>2</sub>(x)) = {@code 32 - numberOfLeadingZeros(x - 1)}
     * </ul>
     *
     * @param i 要计算头部 0 的个数的值
     * @return 返回在指定 {@code int} 值的二进制补码表示形式中最高位（最左边）的 1 位之前的零位的数量；
     *          否则，如果该值等于零，则返回 32。
     * @since 1.5
     */
    public static int numberOfLeadingZeros(int i) {
        // HD, Figure 5-6
        if (i == 0)
            return 32;
        int n = 1;
        if (i >>> 16 == 0) { n += 16; i <<= 16; }
        if (i >>> 24 == 0) { n +=  8; i <<=  8; }
        if (i >>> 28 == 0) { n +=  4; i <<=  4; }
        if (i >>> 30 == 0) { n +=  2; i <<=  2; }
        n -= i >>> 31;
        return n;
    }

    /**
     * 返回指定的 {@code int} 值的二进制补码表示形式中最低（“最右边”）的为 1 的位后面的零位个数。
     * 如果指定值在它的二进制补码表示形式中没有为 1 的位，即它的值为零，则返回 32。
     *
     * @param i 要计算尾部 0 个数的值
     * @return 返回在指定 {@code int} 值的二进制补码表示形式中最低位（最右边）的 1 位之后零位的数量；
     *          否则，如果该值等于零，则返回 32。
     * @since 1.5
     */
    public static int numberOfTrailingZeros(int i) {
        // HD, Figure 5-14
        int y;
        if (i == 0) return 32;
        int n = 31;
        y = i <<16; if (y != 0) { n = n -16; i = y; }
        y = i << 8; if (y != 0) { n = n - 8; i = y; }
        y = i << 4; if (y != 0) { n = n - 4; i = y; }
        y = i << 2; if (y != 0) { n = n - 2; i = y; }
        return n - ((i << 1) >>> 31);
    }

    /**
     * 返回指定 {@code int} 值的二进制补码表示形式的 1 位的数量。
     * 此函数有时用于<i>人口普查</i>。
     *
     * @param i 要计算位数的值
     * @return 返回指定 {@code int} 值的二进制补码表示形式的 1 位的数量。
     * @since 1.5
     */
    public static int bitCount(int i) {
        // HD, Figure 5-2
        i = i - ((i >>> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        i = (i + (i >>> 4)) & 0x0f0f0f0f;
        i = i + (i >>> 8);
        i = i + (i >>> 16);
        return i & 0x3f;
    }

    /**
     * 返回根据指定的位数循环左移指定的 {@code int} 值的二进制补码表示形式而得到的值。
     * （位是从左边（即高位）移出，从右边（即低位）再进入）
     *
     * <p>注意，使用负距离的左循环等同于右循环：
     * {@code rotateLeft(val, -distance) == rotateRight(val, distance)}。
     * 还要注意的是，以 32 的任何倍数进行的循环都是无操作指令，
     * 因此，即使距离为负，除了最后五位外，其余所有循环距离都可以忽略：
     * {@code rotateLeft(val, distance) == rotateLeft(val, distance & 0x1F)}。
     *
     * @param i 需要循环左移的值
     * @param distance 循环左移的位数
     * @return 返回根据指定的位数循环左移指定的 {@code int} 值的二进制补码表示形式而得到的值。
     * @since 1.5
     */
    public static int rotateLeft(int i, int distance) {
        return (i << distance) | (i >>> -distance);
    }

    /**
     * 返回根据指定的位数循环右移指定的 {@code int} 值的二进制补码表示形式而得到的值。
     * （位是从右边（即低位）移出，从左边（即高位）再进入）
     *
     * <p>注意，使用负距离的右循环等同于左循环：
     * {@code rotateRight(val, -distance) == rotateLeft(val, distance)}。
     * 还要注意的是，以 32 的任何倍数进行的循环都是无操作指令，
     * 因此，即使距离为负，除了最后五位外，其余所有循环距离都可以忽略：
     * {@code rotateRight(val, distance) == rotateRight(val, distance & 0x1F)}。
     *
     * @param i 需要循环右移的值
     * @param distance 循环右移的位数
     * @return 返回根据指定的位数循环右移指定的 {@code int} 值的二进制补码表示形式而得到的值。
     * @since 1.5
     */
    public static int rotateRight(int i, int distance) {
        return (i >>> distance) | (i << -distance);
    }

    /**
     * 返回通过反转指定 {@code int} 值的二进制补码表示形式中位的顺序而获得的值。
     *
     * @param i 需要反转的值
     * @return 返回通过反转指定 {@code int} 值中位的顺序而获得的值。
     * @since 1.5
     */
    public static int reverse(int i) {
        // HD, Figure 7-1
        i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;
        i = (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333;
        i = (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f;
        i = (i << 24) | ((i & 0xff00) << 8) |
            ((i >>> 8) & 0xff00) | (i >>> 24);
        return i;
    }

    /**
     * 返回指定 {@code int} 值的符号函数。
     * （如果指定值为负，则返回 －1；如果指定值为零，则返回 0；如果指定的值为正，则返回 1。）
     *
     * @param i 需要计算符号函数的值
     * @return 返回指定 {@code int} 值的符号函数。
     * @since 1.5
     */
    public static int signum(int i) {
        // HD, Section 2-7
        return (i >> 31) | (-i >>> 31);
    }

    /**
     * 返回通过反转指定 {@code int} 值的二进制补码表示形式中字节的顺序而获得的值。
     *
     * @param i 要反转字节的值
     * @return 返回通过反转指定 {@code int} 值的字节而获得的值。
     * @since 1.5
     */
    public static int reverseBytes(int i) {
        return ((i >>> 24)           ) |
               ((i >>   8) &   0xFF00) |
               ((i <<   8) & 0xFF0000) |
               ((i << 24));
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
    public static int sum(int a, int b) {
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
    public static int max(int a, int b) {
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
    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    @Native private static final long serialVersionUID = 1360826667806852920L;
}
