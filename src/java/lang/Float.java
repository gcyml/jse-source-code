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

import sun.misc.FloatingDecimal;
import sun.misc.FloatConsts;
import sun.misc.DoubleConsts;

/**
 * {@code Float} 类在对象中包装一个基本类型 {@code float} 的值。
 * {@code Float} 类型的对象包含一个 {@code float} 类型的字段。
 *
 * <p>此外，此类提供了几种方法，可将 {@code float} 类型与 {@code String} 类型互相转换，
 * 还提供了处理 {@code Float} 类型时非常有用的其他一些常量和方法。
 *
 * @author  Lee Boynton
 * @author  Arthur van Hoff
 * @author  Joseph D. Darcy
 * @since JDK1.0
 */
public final class Float extends Number implements Comparable<Float> {
    /**
     * 保存 {@code float} 类型的正无穷大值的常量。
     * 它等于 {@code Float.intBitsToFloat(0x7f800000)} 返回的值。
     */
    public static final float POSITIVE_INFINITY = 1.0f / 0.0f;

    /**
     * A constant holding the negative infinity of type
     * {@code float}. It is equal to the value returned by
     * {@code Float.intBitsToFloat(0xff800000)}.
     */
    public static final float NEGATIVE_INFINITY = -1.0f / 0.0f;

    /**
     * 保存 {@code float} 类型的负无穷大值的常量。
     * 它等于 {@code Float.intBitsToFloat(0x7fc00000)} 返回的值。
     */
    public static final float NaN = 0.0f / 0.0f;

    /**
     * 保存 {@code float} 类型的最大正有限值的常量，即 (2-2<sup>-23</sup>)&middot;2<sup>127</sup>。
     * 它等于十六进制的浮点字面值 {@code 0x1.fffffeP+127f}，
     * 也等于 {@code Float.intBitsToFloat(0x7f7fffff)}。
     */
    public static final float MAX_VALUE = 0x1.fffffeP+127f; // 3.4028235e+38f

    /**
     * 保存 {@code float} 类型数据的最小正标准值的常量，即 2<sup>-126</sup>。
     * 它等于十六进制的浮点字面值 {@code 0x1.0p-126f}，
     * 也等于 {@code Float.intBitsToFloat(0x00800000)}。
     *
     * @since 1.6
     */
    public static final float MIN_NORMAL = 0x1.0p-126f; // 1.17549435E-38f

    /**
     * 保存 {@code float} 类型数据的最小正非零值的常量，即 2<sup>-149</sup>。
     * 它等于十六进制的浮点字面值 {@code 0x0.000002P-126f}，
     * 也等于 {@code Float.intBitsToFloat(0x1)}。
     */
    public static final float MIN_VALUE = 0x0.000002P-126f; // 1.4e-45f

    /**
     * 有限 {@code float} 变量可能具有的最大指数。
     * 它等于 {@code Math.getExponent(Float.MAX_VALUE)} 返回的值。
     *
     * @since 1.6
     */
    public static final int MAX_EXPONENT = 127;

    /**
     * 标准化 {@code float} 变量可能具有的最小指数。
     * 它等于 {@code Math.getExponent(Float.MIN_NORMAL)} 返回的值。
     *
     * @since 1.6
     */
    public static final int MIN_EXPONENT = -126;

    /**
     * 表示一个 {@code float} 值所使用的位数。
     *
     * @since 1.5
     */
    public static final int SIZE = 32;

    /**
     * 表示一个 {@code float} 值所使用的字节数。
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * 表示 {@code float} 基本类型的 {@code Class} 实例。
     *
     * @since JDK1.1
     */
    @SuppressWarnings("unchecked")
    public static final Class<Float> TYPE = (Class<Float>) Class.getPrimitiveClass("float");

    /**
     * 返回 {@code float} 参数的字符串表示形式。下面提到的所有字符都是 ASCII 字符。
     * <ul>
     * <li> 如果参数是 NaN，那么结果是字符串 “{@code NaN}”。
     * <li> 否则，结果是表示参数的符号和数值（绝对值）的字符串。
     *      如果符号为负，那么结果的第一个字符是 '{@code -}' ({@code '\u005Cu002D'})；
     *      如果符号为正，则结果中不显示符号字符。至于数值 <i>m</i>：
     * <ul>
     * <li> 如果 <i>m</i> 为无穷大，则用字符串 {@code "Infinity"} 表示；
     *      因此，正无穷大生成结果 {@code "Infinity"}，负无穷大生成结果 {@code "-Infinity"}。
     * <li> 如果 <i>m</i> 为0，则用字符 {@code "0.0"} 表示；
     *      因此，负 0 生成结果 {@code "-0.0"}，正 0 生成结果 {@code "0.0"}。
     * <li> 如果 <i>m</i> 大于或等于 10<sup>-3</sup>，但小于 10<sup>7</sup>，
     *      则采用不带前导 0 的十进制形式，用 <i>m</i> 的整数部分表示，后跟 '{@code .}'({@code '\u005Cu002E'})，
     *      再后面是表示 <i>m</i> 小数部分的一个或多个十进制位数。
     * <li> 如果 <i>m</i> 小于 10<sup>-3</sup> 或大于等于 10<sup>7</sup>，
     *      则用所谓的“计算机科学记数法”表示。设 <i>n</i> 为满足 10<sup><i>n</i> </sup>&le;
     *      <i>m</i> {@literal <} 10<sup><i>n</i>+1</sup> 的唯一整数；
     *      然后设 <i>a</i> 为 <i>m</i> 与 10<sup><i>n</i></sup> 的精确算术商数值，
     *      从而 1 &le; <i>a</i> {@literal <} 10。
     *      那么，数值便表示为 <i>a</i> 的整数部分，其形式为：一个十进制位数，
     *      后跟 '{@code .}' ({@code '\u005Cu002E'})，
     *      接着是表示 <i>a</i> 小数部分的十进制位数，
     *      再后面是字母 '{@code E}' ({@code '\u005Cu0045'})，
     *      最后是用十进制整数形式表示的 <i>n</i>，这与
     *      方法 {@link java.lang.Integer#toString(int)} 生成的结果一样。
     *
     * </ul>
     * </ul>
     * 必须为 <i>m</i> 或 <i>a</i> 的小数部分打印多少位呢？
     * 至少必须有一位数来表示小数部分，除此之外，
     * 需要更多（但只能和需要的一样多）位数来唯一地区分参数值和 {@code float} 类型的邻近值。
     * 也就是说，假设 <i>x</i> 是用十进制表示法表示的精确算术值，
     * 是通过对有限非 0 参数 <i>f</i> 调用此方法生成的。
     * 那么 <i>f</i> 一定是最接近 <i>x</i> 的 {@code float} 值，
     * 如果有两个 {@code float} 值同等地接近于 <i>x</i>，
     * 那么 <i>f</i> 必须是这两个值中的一个，并且 <i>f</i> 的最低有效位必须是 {@code 0}。
     *
     * <p>要创建浮点值的本地化字符串表示形式，请使用 {@link java.text.NumberFormat} 的子类。
     *
     * @param   f   要转换的浮点型数据。
     * @return 参数的字符串表示形式。
     */
    public static String toString(float f) {
        return FloatingDecimal.toJavaFormatString(f);
    }

    /**
     * 返回 {@code float} 参数的十六进制字符串表示形式。下面提到的所有字符都是 ASCII 字符。
     *
     * <ul>
     * <li> 如果参数是 NaN，那么结果是字符串 “{@code NaN}”。
     * <li> 否则，结果是表示参数的符号和数值（绝对值）的字符串。
     *      如果符号为负，那么结果的第一个字符是 '{@code -}' ({@code '\u005Cu002D'})；
     *      如果符号为正，则结果中不显示符号字符。至于数值 <i>m</i>：
     *
     * <ul>
     * <li> 如果 <i>m</i> 为无穷大，则用字符串 {@code "Infinity"} 表示；
     *      因此，正无穷大生成结果 {@code "Infinity"}，负无穷大生成结果 {@code "-Infinity"}。
     *
     * <li> 如果 <i>m</i> 为0，则用字符 {@code "0x0.0p0"} 表示；
     *      因此，负 0 生成结果 {@code "-0x0.0p0"}，正 0 生成结果 {@code "0x0.0p0"}。
     *
     * <li> 如果 <i>m</i> 是具有标准化表示形式的的 {@code float} 值，
     * 则使用子字符串表示有效位数和指数。 有效位数用字符串 {@code "0x1."} 表示，
     * 后跟该有效位数小数部分的小写十六进制表示形式。除非所有位数都为 0，
     * 否则移除十六进制表示形式中的尾部 0，在所有位数为 0 的情况下，可以用一个 0 表示。
     * 然后用 {@code "p"} 表示指数，后跟无偏指数的十进制字符串，
     * 该值与对指数值调用 {@link Integer#toString(int) Integer.toString} 生成的值相同。
     *
     * <li>如果 <i>m</i> 是具有 subnormal 表示形式的 {@code float} 值，
     * 则用字符 {@code "0x0."} 表示有效位数，后跟该有效位数小数部分的十六进制表示形式。
     * 移除十六进制表示形式中的尾部 0。然后用 {@code "p-126"} 表示指数。
     * 注意，在 subnormal 有效位数中，至少必须有一个非 0 位数。
     *
     * </ul>
     *
     * </ul>
     *
     * <table border>
     * <caption>示例</caption>
     * <tr><th>浮点值</th><th>十六进制字符串</th>
     * <tr><td>{@code 1.0}</td> <td>{@code 0x1.0p0}</td>
     * <tr><td>{@code -1.0}</td>        <td>{@code -0x1.0p0}</td>
     * <tr><td>{@code 2.0}</td> <td>{@code 0x1.0p1}</td>
     * <tr><td>{@code 3.0}</td> <td>{@code 0x1.8p1}</td>
     * <tr><td>{@code 0.5}</td> <td>{@code 0x1.0p-1}</td>
     * <tr><td>{@code 0.25}</td>        <td>{@code 0x1.0p-2}</td>
     * <tr><td>{@code Float.MAX_VALUE}</td>
     *     <td>{@code 0x1.fffffep127}</td>
     * <tr><td>{@code Minimum Normal Value}</td>
     *     <td>{@code 0x1.0p-126}</td>
     * <tr><td>{@code Maximum Subnormal Value}</td>
     *     <td>{@code 0x0.fffffep-126}</td>
     * <tr><td>{@code Float.MIN_VALUE}</td>
     *     <td>{@code 0x0.000002p-126}</td>
     * </table>
     * @param   f   要转换的 {@code float} 值。
     * @return 参数的十六进制字符串表示形式。
     * @since 1.5
     * @author Joseph D. Darcy
     */
    public static String toHexString(float f) {
        if (Math.abs(f) < FloatConsts.MIN_NORMAL
            &&  f != 0.0f ) {// float subnormal
            // Adjust exponent to create subnormal double, then
            // replace subnormal double exponent with subnormal float
            // exponent
            String s = Double.toHexString(Math.scalb((double)f,
                                                     /* -1022+126 */
                                                     DoubleConsts.MIN_EXPONENT-
                                                     FloatConsts.MIN_EXPONENT));
            return s.replaceFirst("p-1022$", "p-126");
        }
        else // double string will be the same as float string
            return Double.toHexString(f);
    }

    /**
     * 返回保存用参数字符串 {@code s} 表示的 {@code float} 值的 {@code Float} 对象。
     *
     * <p>如果 {@code s} 为 {@code null}， 则抛出 {@code NullPointerException} 异常。
     *
     * <p>忽略 {@code s} 中的前导空白字符和尾部空白字符。
     * 就像调用 {@link String#trim} 方法那样移除空白；也就是说， ASCII 空格和控制字符都要被移除。
     * {@code s} 的其余部分应该根据词法语法规则描述构成 <i>FloatValue</i> ：
     *
     * <blockquote>
     * <dl>
     * <dt><i>FloatValue:</i>
     * <dd><i>Sign<sub>opt</sub></i> {@code NaN}
     * <dd><i>Sign<sub>opt</sub></i> {@code Infinity}
     * <dd><i>Sign<sub>opt</sub> FloatingPointLiteral</i>
     * <dd><i>Sign<sub>opt</sub> HexFloatingPointLiteral</i>
     * <dd><i>SignedInteger</i>
     * </dl>
     *
     * <dl>
     * <dt><i>HexFloatingPointLiteral</i>:
     * <dd> <i>HexSignificand BinaryExponent FloatTypeSuffix<sub>opt</sub></i>
     * </dl>
     *
     * <dl>
     * <dt><i>HexSignificand:</i>
     * <dd><i>HexNumeral</i>
     * <dd><i>HexNumeral</i> {@code .}
     * <dd>{@code 0x} <i>HexDigits<sub>opt</sub>
     *     </i>{@code .}<i> HexDigits</i>
     * <dd>{@code 0X}<i> HexDigits<sub>opt</sub>
     *     </i>{@code .} <i>HexDigits</i>
     * </dl>
     *
     * <dl>
     * <dt><i>BinaryExponent:</i>
     * <dd><i>BinaryExponentIndicator SignedInteger</i>
     * </dl>
     *
     * <dl>
     * <dt><i>BinaryExponentIndicator:</i>
     * <dd>{@code p}
     * <dd>{@code P}
     * </dl>
     *
     * </blockquote>
     *
     * 其中 <i>Sign</i>, <i>FloatingPointLiteral</i>, <i>HexNumeral</i>,
     * <i>HexDigits</i>, <i>SignedInteger</i> 和 <i>FloatTypeSuffix</i> 与
     * <cite>The Java&trade; Language Specification</cite> 的词法结构部分中的定义相同，
     * 除了数字间不接受下划线以外。
     * 如果 {@code s} 的表示形式不是 <i>FloatValue</i>，
     * 则抛出 {@code NumberFormatException} 异常。
     * 否则， 可以认为 {@code s} 表示的是常用“计算机科学记数法”表示的精确十进制值，
     * 或者是一个精确的十六进制值；在概念上，这个精确的数值被转换一个“无限精确的”二进制值，
     * 然后根据常用 IEEE 754 浮点算法的“舍入为最接近的数”规则（其包括保留零值的符号）将该值舍入为 {@code float} 类型，
     *
     * 注意，舍入为最接近数的规则也意味着上溢和下溢行为；
     * 如果 {@code s} 的精确值在幅度上足够大（大于或等于 ({@link #MAX_VALUE} + {@link Math#ulp(float) ulp(MAX_VALUE)}/2），
     * 则舍入为 {@code float} 将会是无穷大的结果，
     * 以及如果 {@code s} 的精确值在幅度上足够小（小于或等于 {@link #MIN_VALUE}/2），
     * 舍入为 float 结果将会是 0。
     *
     * 最后，返回表示这个 {@code float} 值的 {@code Float} 对象。
     *
     * <p>要解释浮点值的本地化字符串表示形式，请使用 {@link java.text.NumberFormat} 的子类。
     *
     * <p>注意，尾部格式说明符、确定浮点字面值类型的说明符（{@code 1.0f} 是一个 {@code float} 值；
     * {@code 1.0d} 是一个 {@code double} 值）， <em>不会</em> 影响此方法的结果。
     * 换句话说，输入字符串的数值被直接转换为目标浮点类型。通常，分两步的转换
     * （先将字符串转换为 {@code double} 类型，然后将 {@code double} 类型转换为 {@code float} 类型）
     * <em>不</em> 同于直接将字符串转换为 {@code float} 类型。
     * 例如，如果首先转换为中间类型 {@code double}，然后再转换为 {@code float} 类型，则字符串<br>
     * {@code "1.00000017881393421514957253748434595763683319091796875001d"}<br>
     * 将得到 {@code float} 值 {@code 1.0000002f}；如果直接将字符串转换为 {@code float} 值，
     * 则结果将是 <code>1.000000<b>1</b>f</code>。
     *
     * <p>为了避免对无效字符串调用此方法并导致抛出 {@code NumberFormatException}，
     * {@link Double#valueOf Double.valueOf} 的文档中列出了一个正则表达式，
     * 可以用该表达式在屏幕上显示输入。
     *
     * @param   s   要解析的字符串。
     * @return  保存用 {@code String} 参数表示的值的 {@code Float}  对象。
     * @throws  NumberFormatException  如果字符串不包含可解析的数字。
     */
    public static Float valueOf(String s) throws NumberFormatException {
        return new Float(parseFloat(s));
    }

    /**
     * 返回表示指定的 {@code float} 值的 {@code Float} 实例。如果不需要新的 {@code Float} 实例，
     * 则通常应优先使用此方法，而不是构造方法 {@link #Float(float)}，
     * 因为此方法可能通过缓存经常请求的值来显著提高空间和时间性能。
     *
     * @param  f 一个浮点值。
     * @return 一个表示 {@code f} 的 {@code Float} 实例。
     * @since  1.5
     */
    public static Float valueOf(float f) {
        return new Float(f);
    }

    /**
     * 返回一个新的 {@code float} 值，该值被初始化为用指定 {@code String} 表示的值，
     * 这与 {@code Float} 类的 {@code valueOf} 方法一样。
     *
     * @param  s 要解析的字符串。
     * @return 用字符串参数表示的 {@code float} 值。
     * @throws NullPointerException  如果字符串为空
     * @throws NumberFormatException 如果字符串不包含可解析的 {@code float} 值。
     * @see    java.lang.Float#valueOf(String)
     * @since 1.2
     */
    public static float parseFloat(String s) throws NumberFormatException {
        return FloatingDecimal.parseFloat(s);
    }

    /**
     * 如果指定的数是一个非数字 (NaN) 值，则返回 {@code true}，
     * 否则返回 {@code false}。
     *
     * @param   v   要测试的值。
     * @return  如果参数为 NaN，则返回 {@code true}，否则返回 {@code false}。
     */
    public static boolean isNaN(float v) {
        return (v != v);
    }

    /**
     * 如果指定数的数值是无穷大，则返回 {@code true}；否则返回 {@code false}。
     *
     * @param   v   要测试的值。
     * @return  如果参数是正无穷大或负无穷大，则返回 {@code true}；否则返回 {@code false}。
     */
    public static boolean isInfinite(float v) {
        return (v == POSITIVE_INFINITY) || (v == NEGATIVE_INFINITY);
    }


    /**
     * 如果参数是一个有限的浮点值则返回 {@code true}，否则则返回 {@code false} （若参数为 NAN 或无穷大）。
     *
     * @param f 要测试的 {@code float} 值。
     * @return 如果参数是有限浮点值，则返回 {@code true}；否则返回 {@code false}。
     * @since 1.8
     */
     public static boolean isFinite(float f) {
        return Math.abs(f) <= FloatConsts.MAX_VALUE;
    }

    /**
     * Float 的值。
     *
     * @serial
     */
    private final float value;

    /**
     * 构造一个新分配的 {@code Float} 对象，它表示基本的 {@code float} 参数。
     *
     * @param   value   用 {@code Float} 表示的值。
     */
    public Float(float value) {
        this.value = value;
    }

    /**
     * 构造一个新分配的 {@code Float} 对象，它表示转换为 {@code float} 类型的参数。
     *
     * @param   value   the value to be represented by the {@code Float}.
     */
    public Float(double value) {
        this.value = (float)value;
    }

    /**
     * 构造一个新分配的 {@code Float} 对象，它表示用字符串表示的 {@code float} 类型的浮点值。
     * 字符串将被转换为 {@code float} 值，这与 {@code valueOf} 方法一样。
     *
     * @param      s   将转换为 {@code Float} 值的字符串。
     * @throws  NumberFormatException  如果字符串不包含可解析的数字。
     * @see        java.lang.Float#valueOf(java.lang.String)
     */
    public Float(String s) throws NumberFormatException {
        value = parseFloat(s);
    }

    /**
     * 如果指定的数是一个非数字 (NaN) 值，则返回 {@code true}；否则返回 {@code false}。
     *
     * @return  如果参数为 NaN，则返回 {@code true}；否则返回 {@code false}。
     */
    public boolean isNaN() {
        return isNaN(value);
    }

    /**
     * 如果此 {@code Float} 值的大小是无穷大，则返回 {@code true}；否则返回 {@code false}。
     *
     * @return  如果此对象表示的值是正无穷大或负无穷大，则返回 {@code true}；
     *           否则返回 {@code false}。
     */
    public boolean isInfinite() {
        return isInfinite(value);
    }

    /**
     * 返回此 {@code Float} 对象的字符串表示形式。
     * 使用此对象表示的基本 {@code float} 值被转换为一个 {@code String}，
     * 此方法与带一个参数的 {@code toString} 方法完全一样。
     *
     * @return  此对象的 {@code String} 表示。
     * @see java.lang.Float#toString(float)
     */
    public String toString() {
        return Float.toString(value);
    }

    /**
     * 将此 {@code Float} 值以 {@code byte} 形式返回（强制转换为 {@code byte}）。
     *
     * @return  此对象表示的 {@code float} 值，该值被转换为 {@code byte} 类型
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * 将此 {@code Float} 值以 {@code short} 形式返回（强制转换为 {@code short}）。
     *
     * @return  此对象表示的 {@code float} 值，该值被转换为 {@code short} 类型
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.1
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     * 将此 {@code Float} 值以 {@code int} 形式返回（强制转换为 {@code int} 类型）。
     *
     * @return  此对象表示的 {@code float} 值，该值被转换为 {@code int} 类型
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * 将此 {@code Float} 值以 {@code long} 形式返回（强制转换为 {@code long} 类型）。
     *
     * @return  此对象表示的 {@code float} 值，该值被转换为 {@code long} 类型
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public long longValue() {
        return (long)value;
    }

    /**
     * 返回此 {@code Float} 对象的 {@code float} 值。
     *
     * @return 此对象表示的 {@code float} 值。
     */
    public float floatValue() {
        return value;
    }

    /**
     * 经过原始拓宽转换返回此 {@code Float} 对象的 {@code double} 值。
     *
     * @return 此对象表示的 {@code float} 值被转换为 {@code double} 类型，并返回转换的结果。
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)value;
    }

    /**
     * 返回此 {@code Float} 对象的哈希码。
     * 结果是此 {@code Float} 对象表示的基本 {@code float} 值的整数位表示形式，
     *
     * @return 此对象的哈希码值。
     */
    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    /**
     * 返回 {@code float} 值的哈希值；和 {@code Float.hashCode()} 兼容。
     *
     * @param value 要哈希的值
     * @return {@code float} 值的哈希值。
     * @since 1.8
     */
    public static int hashCode(float value) {
        return floatToIntBits(value);
    }

    /**
     * 将此对象与指定对象进行比较。
     * 当且仅当参数不是 {@code null} 而是 {@code Float} 对象，
     * 且表示的 {@code float} 值与此对象表示的 {@code float} 值相同时，结果为 {@code true}。
     * 为此，当且仅当将方法 {@link #floatToIntBits(float)} 应用于两个值所返回的 {@code int} 值相同时，
     * 才认为这两个 {@code float} 值相同。
     *
     * <p>注意，在大多数情况下，对于 {@code Float} 类的
     * 两个实例 {@code f1} 和 {@code f2}，当且仅当
     *
     * <blockquote><pre>
     *   f1.floatValue() == f2.floatValue()
     * </pre></blockquote>
     *
     * <p>的值为 {@code true} 时，{@code f1.equals(f2)} 的值才为 {@code true}。但是，有以下两种例外情况：
     * <ul>
     * <li>如果 {@code f1} 和 {@code f2} 都表示 {@code Float.NaN}，
     *     那么即使 {@code Float.NaN==Float.NaN} 的值为 {@code false}，
     *     {@code equals} 方法也将返回 {@code true}。
     * <li>如果 {@code f1} 表示 {@code +0.0f}，而 {@code f2} 表示 {@code -0.0f}，
     *     或相反，那么即使 {@code 0.0f==-0.0f} 的值为 {@code true}，
     *     {@code equals} 方法也将返回 {@code false}。
     * </ul>
     *
     * 该定义使得哈希表得以正确操作。
     *
     * @param obj 要比较的对象
     * @return  如果对象是相同的，则返回 {@code true}；否则返回 {@code false}。
     * @see java.lang.Float#floatToIntBits(float)
     */
    public boolean equals(Object obj) {
        return (obj instanceof Float)
               && (floatToIntBits(((Float)obj).value) == floatToIntBits(value));
    }

    /**
     * 根据 IEEE 754 浮点“单一格式”位布局，返回指定浮点值的表示形式。
     *
     * <p>第 31 位（掩码 {@code 0x80000000} 选定的位）表示浮点数的符号。
     * 第 30-23 位（掩码 {@code 0x7f800000} 选定的位）表示指数。
     * 第 22-0 位（掩码 {@code 0x007fffff}) 选定的位）表示浮点数的有效位数（有时也称为尾数）。
     *
     * <p>如果参数为正无穷大，则结果为 {@code 0x7f800000}。
     *
     * <p>如果参数为负无穷大，则结果为 {@code 0xff800000}。
     *
     * <p>如果参数为 NaN，则结果为 {@code 0x7fc00000}。
     *
     * <p>在所有情况下，结果都是一个整数，
     * 将其赋予 {@link #intBitsToFloat(int)} 方法将生成一个浮点值，
     * 该浮点值与 floatToIntBits 的参数相同（而所有 NaN 值则会生成一个“规范”NaN 值）。
     *
     * @param   value   一个浮点数。
     * @return 表示浮点数的位。
     */
    public static int floatToIntBits(float value) {
        int result = floatToRawIntBits(value);
        // Check for NaN based on values of bit fields, maximum
        // exponent and nonzero significand.
        if ( ((result & FloatConsts.EXP_BIT_MASK) ==
              FloatConsts.EXP_BIT_MASK) &&
             (result & FloatConsts.SIGNIF_BIT_MASK) != 0)
            result = 0x7fc00000;
        return result;
    }

    /**
     * 根据 IEEE 754 浮点“单一格式”位布局，
     * 返回指定浮点值的表示形式，并保留非数字 (NaN) 值。
     *
     * <p>第 31 位（掩码 {@code 0x80000000} 选定的位）表示浮点数的符号。
     * 第 30-23 位（掩码 {@code 0x7f800000} 选定的位）表示指数。
     * 第 22-0 位（掩码 {@code 0x007fffff}) 选定的位）表示浮点数的有效位数（有时也称为尾数）。
     *
     * <p>如果参数为正无穷大，则结果为 {@code 0x7f800000}。
     *
     * <p>如果参数为负无穷大，则结果为 {@code 0xff800000}。
     *
     * <p>如果参数为 NaN，则结果是表示实际 NaN 值的整数。
     * 与 {@code floatToIntBits} 方法不同，
     * {@code floatToRawIntBits} 不压缩所有将 NaN 编码为一个“规范”NaN 值的位模式。
     *
     * <p>在所有情况下，结果都是一个整数，
     * 将其赋予 {@link #intBitsToFloat(int)} 方法将生成
     * 一个与 {@code floatToRawIntBits} 的参数相同的浮点值，
     *
     * @param   value   一个浮点数字。
     * @return 表示浮点数字的位。
     * @since 1.3
     */
    public static native int floatToRawIntBits(float value);

    /**
     * 返回对应于给定位表示形式的 {@code float} 值。
     * 根据 IEEE 754 浮点“单一格式”位布局，该参数被视为浮点值表示形式。
     *
     * <p>如果参数为 {@code 0x7f800000}，则结果为正无穷大。
     *
     * <p>如果参数为 {@code 0xff800000}，则结果为负无穷大。
     *
     * <p>如果参数值在 {@code 0x7f800001} 到 {@code 0x7fffffff} 或
     * 在 {@code 0xff800001} 到 {@code 0xffffffff} 之间，则结果为 NaN。
     * Java 提供的任何 IEEE 754 浮点操作都不能区分具有不同位模式的两个同类型 NaN 值。
     * 不同的 NaN 值只能使用 {@code Float.floatToRawIntBits} 方法区分。
     *
     * <p>在所有其他情况下，设 <i>s</i>, <i>e</i>, 和 <i>m</i> 为可以通过以下参数进行计算的三个值；
     *
     * <blockquote><pre>{@code
     * int s = ((bits >> 31) == 0) ? 1 : -1;
     * int e = ((bits >> 23) & 0xff);
     * int m = (e == 0) ?
     *                 (bits & 0x7fffff) << 1 :
     *                 (bits & 0x7fffff) | 0x800000;
     * }</pre></blockquote>
     *
     * 那么浮点结果等于算术表达式
     * <i>s</i>&middot;<i>m</i>&middot;2<sup><i>e</i>-150</sup>
     * 的值。
     *
     * <p>注意，此方法不能返回与 {@code int} 参数具有完全相同位模式的 {@code float} NaN 值。
     * IEEE 754 区分了两种 NaN：quiet NaN 和 <i>signaling NaNs</i>。
     * 这两种 NaN 之间的差别在 Java 中通常是不可见的。
     * 对 signaling NaN 进行的算术运算将它们转换为具有不同（但通常类似）位模式的 quiet NaN。
     * 但在某些只复制 signaling NaN 的处理器上也执行这种转换。
     * 特别是在复制 signaling NaN 以将其返回给调用方法时，可能会执行这种转换。
     * 因此，{@code intBitsToFloat} 可能无法返回具有 signaling NaN 位模式的 {@code float} 值。
     * 所以，对于某些 {@code int} 值，
     * {@code floatToRawIntBits(intBitsToFloat(start))} 可能 <i>不</i> 等于 {@code start}。
     * 此外，尽管所有 NaN 位模式（不管是 quiet NaN 还是 signaling NaN）都
     * 必须在前面确定的 NaN 范围内，但表示 signaling NaN 的特定位模式是与平台有关。
     *
     * @param   bits   一个整数。
     * @return  具有相同位模式的 {@code float} 浮点值。
     */
    public static native float intBitsToFloat(int bits);

    /**
     * 比较两个 {@code Float} 对象所表示的数值。
     * 在应用到基本 {@code float} 值时，有两种方法可以比较执行此方法产生的值
     * 与执行 Java 语言的数字比较运算符（{@code <, <=, ==, >=, >}）产生的那些值之间的区别：
     *
     * <ul><li>
     *          此方法认为 {@code Float.NaN} 等于其自身，
     *          且大于其他所有 {@code float} 值（包括 {@code Float.POSITIVE_INFINITY}）。
     * <li>
     *          此方法认为 {@code 0.0f} 大于 {@code -0.0f}。
     * </ul>
     *
     * 这可以确保受此方法影响的 {@code Float} 对象
     * 的 <i> 自然顺序 </i>  与 <i> equals 一致</i>。
     *
     * @param   anotherFloat   要比较的 {@code Float} 值。
     * @return  如果 {@code anotherFloat} 在数字上等于此 {@code Float}，则返回 {@code 0}；
     *           如果 {@code anotherFloat} 在数字上小于此 {@code Float}，则返回小于 {@code 0} 的值；
     *           如果 {@code anotherFloat} 在数字上大于此 {@code Float}，则返回大于 {@code 0} 的值。
     *
     * @since   1.2
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Float anotherFloat) {
        return Float.compare(value, anotherFloat.value);
    }

    /**
     * 比较两个指定的 {@code float} 值。返回整数值的符号与以下调用返回整数的符号相同：
     * <pre>
     *    new Float(f1).compareTo(new Float(f2))
     * </pre>
     *
     * @param   f1        要比较的第一个 {@code float}  值。
     * @param   f2        要比较的第二个 {@code float}  值。
     * @return  如果 {@code f1} 在数字上等于此 {@code f2}，则返回值为 {@code 0}；
     *           如果 {@code f1} 在数字上小于此 {@code f2}，则返回小于 {@code 0} 的值；
     *           如果 {@code f1} 在数字上大于此 {@code f2}，则返回大于 {@code 0} 的值。
     * @since 1.4
     */
    public static int compare(float f1, float f2) {
        if (f1 < f2)
            return -1;           // Neither val is NaN, thisVal is smaller
        if (f1 > f2)
            return 1;            // Neither val is NaN, thisVal is larger

        // Cannot use floatToRawIntBits because of possibility of NaNs.
        int thisBits    = Float.floatToIntBits(f1);
        int anotherBits = Float.floatToIntBits(f2);

        return (thisBits == anotherBits ?  0 : // Values are equal
                (thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                 1));                          // (0.0, -0.0) or (NaN, !NaN)
    }

    /**
     * 通过 + 运算符把两个 {@code float} 值相加。
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的和
     * @jls 4.2.4 Floating-Point Operations
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float sum(float a, float b) {
        return a + b;
    }

    /**
     * 通过调用 {@link Math#max(float, float) Math.max} 返回
     * 两个{@code float} 值的最大值。
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的最大值
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    /**
     * 通过调用 {@link Math#min(float, float) Math.min} 返回
     * 两个{@code float} 值的最小值。
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的最小值
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -2671257302660747028L;
}
