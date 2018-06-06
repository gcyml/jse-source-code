/*
 * Copyright (c) 1996, 2013, Oracle and/or its affiliates. All rights reserved.
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

/**
 *
 * {@code Byte} 类将基本类型 {@code byte} 的值包装在一个对象中。
 * 一个 {@code Byte} 的对象只包含一个类型为 {@code byte} 的字段。
 *
 * <p>另外，此类还为 {@code byte} 和 {@code String} 的相互转换提供了几种方法。
 *
 * @author  Nakul Saraiya
 * @author  Joseph D. Darcy
 * @see     java.lang.Number
 * @since   JDK1.1
 */
public final class Byte extends Number implements Comparable<Byte> {

    /**
     * 一个常量，保存 {@code byte} 类型可取的最小值，即-2<sup>7</sup>.
     */
    public static final byte   MIN_VALUE = -128;

    /**
     * 一个常量，保存 {@code byte} 类型可取的最大值，即 2<sup>7</sup>-1.
     */
    public static final byte   MAX_VALUE = 127;

    /**
     * 表示基本类型 {@code byte} 的 {@code Class} 实例。
     */
    @SuppressWarnings("unchecked")
    public static final Class<Byte>     TYPE = (Class<Byte>) Class.getPrimitiveClass("byte");

    /**
     * 返回表达指定 {@code byte} 的一个新 {@code String} 对象。
     * 基数假定为10.
     *
     * @param b 需要转换的 {@code byte}
     * @return 表达指定 {@code byte} 的 String
     * @see java.lang.Integer#toString(int)
     */
    public static String toString(byte b) {
        return Integer.toString((int)b, 10);
    }

    private static class ByteCache {
        private ByteCache(){}

        static final Byte cache[] = new Byte[-(-128) + 127 + 1];

        static {
            for(int i = 0; i < cache.length; i++)
                cache[i] = new Byte((byte)(i - 128));
        }
    }

    /**
     * 返回表达指定 {@code byte} 值的一个{@code Byte} 实例。
     * 如果不需要新的 {@code Byte} 实例， 则通常应优先使用此方法，
     * 而不是构造方法 {@link #Byte(byte)}，
     * 因为该方法有可能通过缓存经常请求的值来显著提高空间和时间性能。
     *
     * @param  b 一个 byte 值。
     * @return 表示 {@code b} 的{@code Byte} 实例。
     * @since  1.5
     */
    public static Byte valueOf(byte b) {
        final int offset = 128;
        return ByteCache.cache[(int)b + offset];
    }

    /**
     * 将 string 参数解析为一个有符号的 {@code byte}，
     * 其基数由第二个参数指定。
     * 除了第一个字符可以是表示负值的 ASCII 负号 {@code '-'}
     * ({@code '\u005Cu002D'}) 或 表示正值的ASCII 正号
     * {@code '+'} ({@code '\u005Cu002B'})之外，
     * 该 string 中的字符必须都是指定基数的数字
     * （这取决于 {@link java.lang.Character#digit(char, int)} 是否返回非负值）。
     * 返回得到的 {@code byte} 值。
     *
     * <p>如果出现下列任何一种情况，则抛出一个 {@code NumberFormatException}
     * 类型的异常：
     * <ul>
     * <li> 第一个参数为 {@code null} 或者字符串的长度为零。
     *
     * <li> 基数小于 {@link java.lang.Character#MIN_RADIX}
     * 或大于 {@link java.lang.Character#MAX_RADIX}.
     *
     * <li> 字符串的任意字符不是指定基数的数字，
     * 除了第一个字符是表示负值的 ASCII 负号 {@code '-'}
     * ({@code '\u005Cu002D'}) 或 表示正值的ASCII 正号
     * {@code '+'} ({@code '\u005Cu002B'})之外（但此时字符串的长度应超过 1）
     *
     * <li> 字符串所表示的值不是 {@code byte} 类型的值。
     * </ul>
     *
     * @param s         要解析的包含 {@code byte} 表示形式的 {@code String}
     * @param radix     用来解析 {@code s} 的基数
     * @return          以指定基数表示的 string 参数所表示的 {@code byte} 值
     * @throws          NumberFormatException 如果该 string 不包含一个可解析的 {@code byte}。
     */
    public static byte parseByte(String s, int radix)
        throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                "Value out of range. Value:\"" + s + "\" Radix:" + radix);
        return (byte)i;
    }

    /**
     * 将 string 参数解析为有符号的十进制 {@code byte}.
     * 该字符串中的字符必须都是十进制数字，
     * 除了第一个字符是表示负值的 ASCII 负号 {@code '-'}
     * ({@code '\u005Cu002D'}) 或 表示正值的ASCII 正号
     * {@code '+'} ({@code '\u005Cu002B'})之外。
     * 返回得到的 {@code byte} 值与以该 string 参数和基数10为参数的
     * {@link #parseByte(java.lang.String, int)} 方法所返回的值一样。
     *
     * @param s         a 要解析的包含 {@code byte} 表示形式的 {@code String}
     * @return          以十进制的参数表示的 {@code byte} 值
     * @throws          NumberFormatException 如果该 string 不包含一个可解析的 {@code byte}。
     */
    public static byte parseByte(String s) throws NumberFormatException {
        return parseByte(s, 10);
    }

    /**
     * 返回一个 {@code Byte} 对象，该对象保持从指定的{@code String} 中提取的值
     * 该值是在用第二个参数所给定的基数对指定字符串进行解析时提取的。
     * 第一个参数被解释为用第二个参数所指定的基数表示一个有符号的 {@code byte}，
     * 正如将该参数指定给 {@link #parseByte(java.lang.String, int)} 方法一样。
     * 结果是一个表示该 string 所指定的 {@code byte} 值的 {@code Byte} 对象。
     *
     * <p> 换句话说，该方法返回一个等于以下代码的值的 {@code Byte} 对象：
     *
     * <blockquote>
     * {@code new Byte(Byte.parseByte(s, radix))}
     * </blockquote>
     *
     * @param s         要解析的字符串
     * @param radix     在解析 {@code s} 时使用的基数
     * @return          保持用指定基数表示的字符串参数所表示的值的 {@code Byte} 对象。
     * @throws          NumberFormatException  如果 {@code String} 不包含一个可解析的 {@code byte}。                 not contain a parsable {@code byte}.
     */
    public static Byte valueOf(String s, int radix)
        throws NumberFormatException {
        return valueOf(parseByte(s, radix));
    }

    /**
     * 返回一个保持指定 {@code String} 所给出的值的 {@code Byte} 对象。
     * 参数被解释为表示一个有符号的十进制的 {@code byte}，
     * 正如将该参数指定给 {@link #parseByte(java.lang.String)} 方法一样。
     * 结果是一个表示该 string 所指定的 {@code byte} 值的 {@code Byte} 对象。
     *
     * <p> 换句话说，该方法返回一个等于以下代码的值的 {@code Byte} 对象：
     *
     * <blockquote>
     * {@code new Byte(Byte.parseByte(s))}
     * </blockquote>
     *
     * @param s         要解析的字符串
     * @return          保持 string 参数所表示的值的 {@code Byte} 对象
     * @throws          NumberFormatException 如果 {@code String} 不包含一个可解析的 {@code byte}。
     */
    public static Byte valueOf(String s) throws NumberFormatException {
        return valueOf(s, 10);
    }

    /**
     * 将 {@code String} 解码为 {@code Byte}。
     * 接受按下列语法给出的十进制、十六进制和八进制数：
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
     * <cite>The Java&trade; Language Specification</cite> 的 3.10.1节中给出了
     * <i>DecimalNumeral</i>， <i>HexDigits</i>， 和 <i>OctalDigits</i> 的定义。
     * 除了在数字之间不接受下划线的情况。
     *
     * <p>对（可选）负号和/或基数说明符（“{@code 0x}”、“{@code 0X}”、“{@code #}” 或前导零）
     * 后面的字符序列进行解析就如同使用带指定基数（10、16 或 8）的
     * {@code Byte.parseByte} 方法一样。该字符序列必须表示一个正值，
     * 否则将抛出 {@link NumberFormatException}。
     * 如果指定 {@code String} 的第一个字符是负号，
     * 则结果将被求反。该 {@code String} 中不允许出现空白字符。
     *
     * @param     nm 要解码的 {@code String}。
     * @return   保持由 {@code nm} 表示的 {@code byte} 值的 {@code Byte} 对象
     * @throws  NumberFormatException  如果 {@code String}  不包含一个可解析的 {@code byte}。
     * @see java.lang.Byte#parseByte(java.lang.String, int)
     */
    public static Byte decode(String nm) throws NumberFormatException {
        int i = Integer.decode(nm);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                    "Value " + i + " out of range from input " + nm);
        return valueOf((byte)i);
    }

    /**
     * {@code Byte} 的值。
     *
     * @serial
     */
    private final byte value;

    /**
     * 构造一个新分配的 {@code Byte} 对象，以表示指定的 {@code byte} 值。
     *
     * @param value    {@code Byte} 对象所表示的值
     */
    public Byte(byte value) {
        this.value = value;
    }

    /**
     * 构造一个新分配的 {@code Byte} 对象，以表示 {@code String} 参数所指示的 {@code byte} 值。
     * 该字符串以使用基数 10 的 {@code parseByte} 方法所使用的方式被转换成一个 {@code byte} 值。
     *
     * @param s         要转换成 {@code Byte} 的 {@code String}
     * @throws           NumberFormatException 如果 {@code String} 不包含一个可解析的 {@code byte}。
     * @see        java.lang.Byte#parseByte(java.lang.String, int)
     */
    public Byte(String s) throws NumberFormatException {
        this.value = parseByte(s, 10);
    }

    /**
     * 作为一个 {@code byte} 返回此 {@code Byte} 的值。
     */
    public byte byteValue() {
        return value;
    }

    /**
     * 做一个扩宽的原始转换后作为一个 {@code short} 返回此 {@code Byte} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     *  做一个扩宽的原始转换后作为一个 {@code int} 返回此 {@code Byte} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * 做一个扩宽的原始转换后作为一个 {@code long} 返回此 {@code Byte} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public long longValue() {
        return (long)value;
    }

    /**
     * 做一个扩宽的原始转换后作为一个 {@code float} 返回此 {@code Byte} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public float floatValue() {
        return (float)value;
    }

    /**
     * 做一个扩宽的原始转换后作为一个 {@code double} 返回此 {@code Byte} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)value;
    }

    /**
     * 返回表示此 Byte 的值的 {@code String} 对象。
     * 该值被转换成有符号的十进制表示形式，并作为一个 string 返回，
     * 正如将 {@code byte} 值作为一个参数指定给 {@link java.lang.Byte#toString(byte)} 方法所返回的一样。
     *
     * @return  以基数&nbsp;10 表示的此对象值的字符串表示形式。
     */
    public String toString() {
        return Integer.toString((int)value);
    }

    /**
     * 返回此 {@code Byte} 的哈希码。Returns a hash code for this {@code Byte};
     * 和调用了{@code intValue()} 方法的结果相等。
     *
     * @return 此 {@code Byte} 的哈希码。
     */
    @Override
    public int hashCode() {
        return Byte.hashCode(value);
    }

    /**
     * 返回此 {@code byte} 值的哈希码。
     * 和 {@code Byte.hashCode()} 兼容。
     *
     * @param value 需要哈希的值
     * @return 此 {@code byte} 值的哈希码。
     * @since 1.8
     */
    public static int hashCode(byte value) {
        return (int)value;
    }

    /**
     * 将此对象与指定对象比较。当且仅当参数不为 {@code null}，
     * 而是一个与此对象一样包含相同 {@code Byte} 值的 {@code byte} 对象时，结果才为 {@code true} 。
     *
     * @param obj       要进行比较的对象
     * @return          如果这些对象相同，则为 {@code true} ；否则为 {@code false} 。
     */
    public boolean equals(Object obj) {
        if (obj instanceof Byte) {
            return value == ((Byte)obj).byteValue();
        }
        return false;
    }

    /**
     * 在数字上比较两个 {@code Byte} 对象。
     *
     * @param   anotherByte   要比较的 {@code Byte}。
     * @return  如果此 {@code Byte} 等于参数 {@code Byte}，则返回 {@code 0}；
     *           如果此 {@code Byte} 在数字上小于参数 {@code Byte}，则返回小于 {@code 0} 的值；
     *           如果此 {@code Byte} 在数字上大于参数 {@code Byte}，则返回大于 {@code 0} 的值（有符号比较）。
     * @since   1.2
     */
    public int compareTo(Byte anotherByte) {
        return compare(this.value, anotherByte.value);
    }

    /**
     * 在数字上比较两个 {@code byte} 值。
     * 返回的值与以下内容返回的值相同：
     * <pre>
     *    Byte.valueOf(x).compareTo(Byte.valueOf(y))
     * </pre>
     *
     * @param  x 第一个要比较的 {@code byte}
     * @param  y 第二个要比较的 {@code byte}
     * @return 如果 {@code x == y}， 则返回{@code 0};
     *          如果 {@code x < y}， 则返回小于 {@code 0} 的值；
     *          如果 {@code x > y}， 则返回大于 {@code 0} 的值。
     * @since 1.7
     */
    public static int compare(byte x, byte y) {
        return x - y;
    }

    /**
     * 通过无符号转换将参数转换为 {@code int}。
     * 在对 {@code int} 的无符号转换中， {@code int} 的高阶 24 位是 0，
     * 低阶 8 位等于 {@code byte} 参数的值。
     *
     * 因此，零和正 {@code byte} 值被映射到数值相等的 {@code int} 值，
     * 负 {@code byte} 值被映射到 {@code int} 值和 2<sup>8</sup> 加值.
     *
     * @param  x 转换到无符号 {@code int} 的值
     * @return 参数无符号转换后得到的 {@code int}
     * @since 1.8
     */
    public static int toUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }

    /**
     * 通过无符号转换将参数转换为 {@code long}。
     * 在对 {@code long} 的无符号转换中， {@code long} 的高阶 56 位是 0，
     * 低阶 8 位等于 {@code byte} 参数的值。
     *
     *  因此，零和正 {@code byte} 值被映射到数值相等的 {@code long} 值，
     * 负 {@code byte} 值被映射到 {@code long} 值和 2<sup>8</sup> 加值.
     *
     * @param  x 转换到无符号 {@code long} 的值
     * @return 参数无符号转换后得到的 {@code long}
     * @since 1.8
     */
    public static long toUnsignedLong(byte x) {
        return ((long) x) & 0xffL;
    }


    /**
     * 在二进制补码形式中表示 {@code byte} 值的位数。
     *
     * @since 1.5
     */
    public static final int SIZE = 8;

    /**
     * 在二进制补码形式中用来表示 {@code byte} 值 的字节数。
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /** use serialVersionUID from JDK 1.1. for interoperability */
    private static final long serialVersionUID = -7183698231559129828L;
}
