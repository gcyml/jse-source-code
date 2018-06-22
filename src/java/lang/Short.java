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
 * {@code Short} 类在对象中包装基本类型 {@code short} 的值。
 * 一个 {@code Short} 类型的对象只包含一个 short 类型的字段。
 *
 * <p>另外，该类提供了多个方法，可以将 {@code short} 转换为 {@code String}，
 * 将 {@code String} 转换为 {@code short}，同时还提供了
 * 其他一些处理 {@code short} 时有用的常量和方法。
 *
 * @author  Nakul Saraiya
 * @author  Joseph D. Darcy
 * @see     java.lang.Number
 * @since   JDK1.1
 */
public final class Short extends Number implements Comparable<Short> {

    /**
     *  保存 {@code short} 可取的最小值的常量，最小值为 -2<sup>15</sup>。
     */
    public static final short   MIN_VALUE = -32768;

    /**
     * 保存 {@code short} 可取的最大值的常量，最大值为 2<sup>15</sup>。
     */
    public static final short   MAX_VALUE = 32767;

    /**
     * 表示基本类型 {@code short} 的 {@code Class} 实例。
     */
    @SuppressWarnings("unchecked")
    public static final Class<Short>    TYPE = (Class<Short>) Class.getPrimitiveClass("short");

    /**
     * 返回表示指定 {@code short} 的一个新 {@code String} 对象。假定用十进制表示。
     *
     * @param s 所要转换的 {@code short}
     * @return 表示指定 {@code short} 的字符串
     * @see java.lang.Integer#toString(int)
     */
    public static String toString(short s) {
        return Integer.toString((int)s, 10);
    }

    /**
     * 将字符串参数解析为由第二个参数指定的基数中的有符号的 {@code short}。
     * 该字符串中的字符必须都是指定基数（这取决于 {@link java.lang.Character#digit(char,int)}
     * 是否返回非负值）的数字，除非第一个字符是表示负值的 ASCII 符号中的负号 {@code '-'}
     * （{@code '\u005Cu002D'}） 或表示正值的ASCII 符号中的
     * 正号{@code '+'}（{@code '\u005Cu002B'}）。返回得到的 byte 值。
     *
     * <p>如果出现以下情形之一，则抛出 {@code NumberFormatException} 类型的异常：
     * <ul>
     * <li> 第一个参数是 {@code null} 或零长度的字符串。
     *
     * <li> 基数小于 {@link java.lang.Character#MIN_RADIX}
     * 或大于 {@link java.lang.Character#MAX_RADIX}。
     *
     * <li> 除了在字符串长度超过 1 的情况下第一个字符可能是负号
     * {@code '-'} ({@code '\u005Cu002D'}) 或者正号 {@code '+'} ({@code '\u005Cu002B'})
     * 之外，字符串的任何字符都不是指定基数的数字。
     *
     * <li> 字符串所表示的值不是 {@code short} 类型的值。
     * </ul>
     *
     * @param s         包含要解析的 {@code short} 表示形式的 {@code String}
     * @param radix    将在解析 {@code s} 时使用的基数
     * @return          由指定基数中的字符串参数表示的 {@code short} 。
     * @throws          NumberFormatException 如果 {@code String} 不包含可解析的 {@code short}。
     */
    public static short parseShort(String s, int radix)
        throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                "Value out of range. Value:\"" + s + "\" Radix:" + radix);
        return (short)i;
    }

    /**
     * 将字符串参数解析为有符号的十进制 short。
     * 该字符串中的字符必须都是十进制数字，除非第一个字符是表示负值的 ASCII 符号中的负号 {@code '-'}
     * （{@code '\u005Cu002D'}） 或表示正值的ASCII 符号中的
     * 正号{@code '+'}（{@code '\u005Cu002B'}）。
     * 此值与用该参数和基数 10 作为参数的 {@link #parseShort(java.lang.String, int)}
     * 方法得到的值相同。
     *
     * @param s 包含要解析的 {@code short} 表示形式的 {@code String}
     * @return  参数（十进制）表示的 {@code short} 值。
     * @throws  NumberFormatException 如果该字符串不包含可解析的 {@code short}。
     */
    public static short parseShort(String s) throws NumberFormatException {
        return parseShort(s, 10);
    }

    /**
     * 返回一个 {@code Short} 对象，该对象保持从指定的 {@code String} 中提取的值，
     * 该值是在使用第二个参数给出的基数对指定字符串进行解析时提取的。
     * 第一个参数被解释为表示在使用第二个参数所指定基数时的一个有符号的 {@code short}，
     * 此值与用该参数作为参数的 {@link #parseShort(java.lang.String, int)} 方法得到的值相同。
     * 结果是一个表示该字符串所指定的 {@code short} 值的 {@code Short} 对象。
     *
     * <p>换句话说，此方法返回一个 {@code Short} 对象，它的值等于：
     *
     * <blockquote>
     *  {@code new Short(Short.parseShort(s, radix))}
     * </blockquote>
     *
     * @param s         要解析的字符串
     * @param radix     将在解释 {@code s} 时使用的基数
     * @return          保持指定基数中的字符串参数所表示的值的 {@code Short}  对象。
     * @throws          NumberFormatException 如果 {@code String} 不包含可解析的 {@code short}。
     */
    public static Short valueOf(String s, int radix)
        throws NumberFormatException {
        return valueOf(parseShort(s, radix));
    }

    /**
     * 返回一个保持指定 {@code String} 所给出的值的 {@code Short} 对象。
     * 该参数被解释为表示一个有符号的十进制 {@code short}，
     * 此值与用该参数作为参数的 {@link #parseShort(java.lang.String)} 方法得到的值相同。
     * 结果是一个表示该字符串所指定的 {@code short} 值的 {@code Short} 对象。
     *
     * <p>换句话说，此方法返回一个 {@code Short} 对象，它的值等于：
     *
     * <blockquote>
     *  {@code new Short(Short.parseShort(s))}
     * </blockquote>
     *
     * @param s 要解析的字符串
     * @return  保持字符串参数所表示的值的 {@code Short} 对象
     * @throws  NumberFormatException 如果 {@code String} 不包含可解析的 {@code short}。
     */
    public static Short valueOf(String s) throws NumberFormatException {
        return valueOf(s, 10);
    }

    private static class ShortCache {
        private ShortCache(){}

        static final Short cache[] = new Short[-(-128) + 127 + 1];

        static {
            for(int i = 0; i < cache.length; i++)
                cache[i] = new Short((short)(i - 128));
        }
    }

    /**
     * 返回表示指定 {@code short} 值的 {@code Short} 实例。如果不需要新的 {@code Short} 实例，
     * 则通常应该优先采用此方法，而不是构造方法 {@link #Short(short)}，
     * 因为此方法很可能通过缓存经常请求的值来显著提高空间和时间性能。
     *
     * 此方法缓存值范围总在 -128 到 127 之间，并且可以缓存该范围外的其他值。
     *
     * @param  s 一个 short 值。
     * @return 表示 s 的 {@code Short} 实例。
     * @since  1.5
     */
    public static Short valueOf(short s) {
        final int offset = 128;
        int sAsInt = s;
        if (sAsInt >= -128 && sAsInt <= 127) { // must cache
            return ShortCache.cache[sAsInt + offset];
        }
        return new Short(s);
    }

    /**
     * 将 {@code String} 解码为 {@code Short}。接受通过以下语法给出的十进制、十六进制和八进制数：
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
     * <i>DecimalNumeral</i>, <i>HexDigits</i>, 和 <i>OctalDigits</i>
     * 在 <cite>The Java&trade; Language Specification</cite> 的 3.10.1 已经定义，
     * 除了数字之间不能有下划线的情况之外。
     *
     * <p>在可选符号和/或基数说明符（"{@code 0x}", "{@code 0X}",
     * "{@code #}", 或前导零）后面的字符序列进行解析就如同用 {@code Short.parseShort}
     * 方法来解析指定的基数（10、16 或 8）一样。
     * 该字符序列必须表示为一个正值，否则将抛出 {@link NumberFormatException}。
     * 如果指定 {@code String} 的第一个字符是减号，则结果无效。
     * {@code String} 中不允许出现空白字符。
     *
     * @param     nm 要解码的 {@code String}。
     * @return    保持 {@code nm} 所表示的 {@code short} 值的 {@code Short} 对象
     * @throws    NumberFormatException  如果 {@code String} 不包含可解析的 {@code short}。            contain a parsable {@code short}.
     * @see java.lang.Short#parseShort(java.lang.String, int)
     */
    public static Short decode(String nm) throws NumberFormatException {
        int i = Integer.decode(nm);
        if (i < MIN_VALUE || i > MAX_VALUE)
            throw new NumberFormatException(
                    "Value " + i + " out of range from input " + nm);
        return valueOf((short)i);
    }

    /**
     * {@code Short} 的 值。
     *
     * @serial
     */
    private final short value;

    /**
     * 构造一个新分配的 {@code Short} 对象，用来表示指定的 {@code short} 值。
     *
     * @param value    {@code Short}所表示的值
     */
    public Short(short value) {
        this.value = value;
    }

    /**
     * 构造一个新分配的 {@code Short} 对象，用来表示 {@code String} 参数所
     * 指示的 {@code short} 值。
     * 将字符串转换为 {@code short} 值，转换方式与基数为 10 的 {@code parseShort} 方法
     * 所用的方式完全相同。
     *
     * @param s 要转换为 {@code Short} 的 {@code String}
     * @throws  NumberFormatException 如果 {@code String} 不包含可解析的 {@code short}。
     * @see     java.lang.Short#parseShort(java.lang.String, int)
     */
    public Short(String s) throws NumberFormatException {
        this.value = parseShort(s, 10);
    }

    /**
     * 经过缩小的原始转换后以 {@code byte} 形式返回此 {@code Short}  的值。
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * 以 {@code short} 形式返回此 {@code Short} 的值。
     */
    public short shortValue() {
        return value;
    }

    /**
     * 经过拓宽的原始转换后以 {@code int} 形式返回此 {@code Short} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * 经过拓宽的原始转换后以 {@code long} 形式返回此 {@code Short} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public long longValue() {
        return (long)value;
    }

    /**
     * 经过拓宽的原始转换后以 {@code float} 形式返回此 {@code Short} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public float floatValue() {
        return (float)value;
    }

    /**
     * 经过拓宽的原始转换后以 {@code double} 形式返回此 {@code Short} 的值。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)value;
    }

    /**
     * 返回表示此 {@code Short} 的值的 {@code String} 对象。
     * 该值被转换成有符号的十进制表示形式，并作为一个字符串返回，
     * 正如将 {@code short} 值作为一个参数指定给
     * {@link java.lang.Short#toString(short)} 方法所得到的值那样。
     *
     * @return  a string representation of the value of this object in
     *          base&nbsp;10.
     */
    public String toString() {
        return Integer.toString((int)value);
    }

    /**
     * 返回此 {@code Short} 的哈希码；
     * 和调用 {@code intValue()} 的结果相等。
     *
     * @return a hash code value for this {@code Short}
     */
    @Override
    public int hashCode() {
        return Short.hashCode(value);
    }

    /**
     * 返回此 {@code short} 的哈希码；和 {@code Short.hashCode()} 兼容。
     *
     * @param value the value to hash
     * @return a hash code value for a {@code short} value.
     * @since 1.8
     */
    public static int hashCode(short value) {
        return (int)value;
    }

    /**
     * 将此对象与指定对象比较。
     * 当且仅当参数不是 {@code null}，而是一个与该对象一样包含相同
     * {@code short} 值的 {@code Short} 对象时，结果才为 {@code true}。
     *
     * @param obj       将与之进行比较的对象。
     * @return          如果这些对象相同，则返回 {@code true}；否则返回 {@code false}。;
     */
    public boolean equals(Object obj) {
        if (obj instanceof Short) {
            return value == ((Short)obj).shortValue();
        }
        return false;
    }

    /**
     * 比较两个 {@code Short} 对象所表示的数值。.
     *
     * @param   anotherShort   要比较的 {@code Short}。
     * @return  如果该 {@code Short} 等于参数 {@code Short}，则返回 {@code 0}；
     *           如果该 {@code Short} 数值小于参数 {@code Short}，则返回小于 {@code 0} 的值；
     *           如果该 {@code Short} 数值大于参数 {@code Short}，
     *           则返回大于 {@code 0} 的值（有符号比较）。
     * @since   1.2
     */
    public int compareTo(Short anotherShort) {
        return compare(this.value, anotherShort.value);
    }

    /**
     * 数值上比较两个 {@code short} 值。
     * 返回的值与以下内容返回的值相同：
     * <pre>
     *    Short.valueOf(x).compareTo(Short.valueOf(y))
     * </pre>
     *
     * @param  x 第一个要比较的 {@code short}
     * @param  y 第一个要比较的 {@code short}
     * @return 如果 {@code x == y} 则返回 {@code 0}；
     *          如果 {@code x < y} 则返回 小于 {@code 0} 的值；
     *          如果 {@code x > y} 则返回 大于 {@code 0} 的值；
     * @since 1.7
     */
    public static int compare(short x, short y) {
        return x - y;
    }

    /**
     * 用来以二进制补码形式表示 {@code short} 值的位数。
     * @since 1.5
     */
    public static final int SIZE = 16;

    /**
     * 用来以二进制补码形式表示 {@code short} 值的字节数。
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * 返回通过反转指定 {@code short} 值的二进制补码表示形式中字节的顺序而获得的值。
     *
     * @param i 要反转字节的值
     * @return 通过反转（或者交换，效果相同）指定 {@code short} 值中的字节而获得的值。
     * @since 1.5
     */
    public static short reverseBytes(short i) {
        return (short) (((i & 0xFF00) >> 8) | (i << 8));
    }


    /**
     * 通过无符号转换将参数转换为 {@code int} 。
     * 在无符号转换为 {@code int} 时，{@code int} 的高16位为零，低16位等于{@code short}参数的位。
     *
     * 因此，零和正短数值映射到数字上相等的 {@code int} 值，
     * 而负 {@code short} 值映射到等于输入的整数值加上 2<sup>16</sup> 。
     *
     * @param  x 要无符号转换转换为 {@code int} 的值
     * @return 通过无符号转换将参数转换为 {@code int}
     * @since 1.8
     */
    public static int toUnsignedInt(short x) {
        return ((int) x) & 0xffff;
    }

    /**
     * 通过无符号转换将参数转换为 {@code long} 。
     * 在无符号转换为 {@code long} 时，{@code int} 的高48位为零，低16位等于{@code short}参数的位。
     *
     * 因此，零和正短数值映射到数字上相等的 {@code long} 值，
     * 而负 {@code short} 值映射到等于输入的整数值加上 2<sup>16</sup> 。
     *
     * @param  x 要无符号转换转换为 {@code long} 的值
     * @return 通过无符号转换将参数转换为 {@code long} 。
     * @since 1.8
     */
    public static long toUnsignedLong(short x) {
        return ((long) x) & 0xffffL;
    }

    /** use serialVersionUID from JDK 1.1. for interoperability */
    private static final long serialVersionUID = 7515723908773894738L;
}
