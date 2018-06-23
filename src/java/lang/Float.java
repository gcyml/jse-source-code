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
     * Returns a {@code Float} instance representing the specified
     * {@code float} value.
     * If a new {@code Float} instance is not required, this method
     * should generally be used in preference to the constructor
     * {@link #Float(float)}, as this method is likely to yield
     * significantly better space and time performance by caching
     * frequently requested values.
     *
     * @param  f a float value.
     * @return a {@code Float} instance representing {@code f}.
     * @since  1.5
     */
    public static Float valueOf(float f) {
        return new Float(f);
    }

    /**
     * Returns a new {@code float} initialized to the value
     * represented by the specified {@code String}, as performed
     * by the {@code valueOf} method of class {@code Float}.
     *
     * @param  s the string to be parsed.
     * @return the {@code float} value represented by the string
     *         argument.
     * @throws NullPointerException  if the string is null
     * @throws NumberFormatException if the string does not contain a
     *               parsable {@code float}.
     * @see    java.lang.Float#valueOf(String)
     * @since 1.2
     */
    public static float parseFloat(String s) throws NumberFormatException {
        return FloatingDecimal.parseFloat(s);
    }

    /**
     * Returns {@code true} if the specified number is a
     * Not-a-Number (NaN) value, {@code false} otherwise.
     *
     * @param   v   the value to be tested.
     * @return  {@code true} if the argument is NaN;
     *          {@code false} otherwise.
     */
    public static boolean isNaN(float v) {
        return (v != v);
    }

    /**
     * Returns {@code true} if the specified number is infinitely
     * large in magnitude, {@code false} otherwise.
     *
     * @param   v   the value to be tested.
     * @return  {@code true} if the argument is positive infinity or
     *          negative infinity; {@code false} otherwise.
     */
    public static boolean isInfinite(float v) {
        return (v == POSITIVE_INFINITY) || (v == NEGATIVE_INFINITY);
    }


    /**
     * Returns {@code true} if the argument is a finite floating-point
     * value; returns {@code false} otherwise (for NaN and infinity
     * arguments).
     *
     * @param f the {@code float} value to be tested
     * @return {@code true} if the argument is a finite
     * floating-point value, {@code false} otherwise.
     * @since 1.8
     */
     public static boolean isFinite(float f) {
        return Math.abs(f) <= FloatConsts.MAX_VALUE;
    }

    /**
     * The value of the Float.
     *
     * @serial
     */
    private final float value;

    /**
     * Constructs a newly allocated {@code Float} object that
     * represents the primitive {@code float} argument.
     *
     * @param   value   the value to be represented by the {@code Float}.
     */
    public Float(float value) {
        this.value = value;
    }

    /**
     * Constructs a newly allocated {@code Float} object that
     * represents the argument converted to type {@code float}.
     *
     * @param   value   the value to be represented by the {@code Float}.
     */
    public Float(double value) {
        this.value = (float)value;
    }

    /**
     * Constructs a newly allocated {@code Float} object that
     * represents the floating-point value of type {@code float}
     * represented by the string. The string is converted to a
     * {@code float} value as if by the {@code valueOf} method.
     *
     * @param      s   a string to be converted to a {@code Float}.
     * @throws  NumberFormatException  if the string does not contain a
     *               parsable number.
     * @see        java.lang.Float#valueOf(java.lang.String)
     */
    public Float(String s) throws NumberFormatException {
        value = parseFloat(s);
    }

    /**
     * Returns {@code true} if this {@code Float} value is a
     * Not-a-Number (NaN), {@code false} otherwise.
     *
     * @return  {@code true} if the value represented by this object is
     *          NaN; {@code false} otherwise.
     */
    public boolean isNaN() {
        return isNaN(value);
    }

    /**
     * Returns {@code true} if this {@code Float} value is
     * infinitely large in magnitude, {@code false} otherwise.
     *
     * @return  {@code true} if the value represented by this object is
     *          positive infinity or negative infinity;
     *          {@code false} otherwise.
     */
    public boolean isInfinite() {
        return isInfinite(value);
    }

    /**
     * Returns a string representation of this {@code Float} object.
     * The primitive {@code float} value represented by this object
     * is converted to a {@code String} exactly as if by the method
     * {@code toString} of one argument.
     *
     * @return  a {@code String} representation of this object.
     * @see java.lang.Float#toString(float)
     */
    public String toString() {
        return Float.toString(value);
    }

    /**
     * Returns the value of this {@code Float} as a {@code byte} after
     * a narrowing primitive conversion.
     *
     * @return  the {@code float} value represented by this object
     *          converted to type {@code byte}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * Returns the value of this {@code Float} as a {@code short}
     * after a narrowing primitive conversion.
     *
     * @return  the {@code float} value represented by this object
     *          converted to type {@code short}
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.1
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     * Returns the value of this {@code Float} as an {@code int} after
     * a narrowing primitive conversion.
     *
     * @return  the {@code float} value represented by this object
     *          converted to type {@code int}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * Returns value of this {@code Float} as a {@code long} after a
     * narrowing primitive conversion.
     *
     * @return  the {@code float} value represented by this object
     *          converted to type {@code long}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public long longValue() {
        return (long)value;
    }

    /**
     * Returns the {@code float} value of this {@code Float} object.
     *
     * @return the {@code float} value represented by this object
     */
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this {@code Float} as a {@code double}
     * after a widening primitive conversion.
     *
     * @return the {@code float} value represented by this
     *         object converted to type {@code double}
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)value;
    }

    /**
     * Returns a hash code for this {@code Float} object. The
     * result is the integer bit representation, exactly as produced
     * by the method {@link #floatToIntBits(float)}, of the primitive
     * {@code float} value represented by this {@code Float}
     * object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    /**
     * Returns a hash code for a {@code float} value; compatible with
     * {@code Float.hashCode()}.
     *
     * @param value the value to hash
     * @return a hash code value for a {@code float} value.
     * @since 1.8
     */
    public static int hashCode(float value) {
        return floatToIntBits(value);
    }

    /**

     * Compares this object against the specified object.  The result
     * is {@code true} if and only if the argument is not
     * {@code null} and is a {@code Float} object that
     * represents a {@code float} with the same value as the
     * {@code float} represented by this object. For this
     * purpose, two {@code float} values are considered to be the
     * same if and only if the method {@link #floatToIntBits(float)}
     * returns the identical {@code int} value when applied to
     * each.
     *
     * <p>Note that in most cases, for two instances of class
     * {@code Float}, {@code f1} and {@code f2}, the value
     * of {@code f1.equals(f2)} is {@code true} if and only if
     *
     * <blockquote><pre>
     *   f1.floatValue() == f2.floatValue()
     * </pre></blockquote>
     *
     * <p>also has the value {@code true}. However, there are two exceptions:
     * <ul>
     * <li>If {@code f1} and {@code f2} both represent
     *     {@code Float.NaN}, then the {@code equals} method returns
     *     {@code true}, even though {@code Float.NaN==Float.NaN}
     *     has the value {@code false}.
     * <li>If {@code f1} represents {@code +0.0f} while
     *     {@code f2} represents {@code -0.0f}, or vice
     *     versa, the {@code equal} test has the value
     *     {@code false}, even though {@code 0.0f==-0.0f}
     *     has the value {@code true}.
     * </ul>
     *
     * This definition allows hash tables to operate properly.
     *
     * @param obj the object to be compared
     * @return  {@code true} if the objects are the same;
     *          {@code false} otherwise.
     * @see java.lang.Float#floatToIntBits(float)
     */
    public boolean equals(Object obj) {
        return (obj instanceof Float)
               && (floatToIntBits(((Float)obj).value) == floatToIntBits(value));
    }

    /**
     * Returns a representation of the specified floating-point value
     * according to the IEEE 754 floating-point "single format" bit
     * layout.
     *
     * <p>Bit 31 (the bit that is selected by the mask
     * {@code 0x80000000}) represents the sign of the floating-point
     * number.
     * Bits 30-23 (the bits that are selected by the mask
     * {@code 0x7f800000}) represent the exponent.
     * Bits 22-0 (the bits that are selected by the mask
     * {@code 0x007fffff}) represent the significand (sometimes called
     * the mantissa) of the floating-point number.
     *
     * <p>If the argument is positive infinity, the result is
     * {@code 0x7f800000}.
     *
     * <p>If the argument is negative infinity, the result is
     * {@code 0xff800000}.
     *
     * <p>If the argument is NaN, the result is {@code 0x7fc00000}.
     *
     * <p>In all cases, the result is an integer that, when given to the
     * {@link #intBitsToFloat(int)} method, will produce a floating-point
     * value the same as the argument to {@code floatToIntBits}
     * (except all NaN values are collapsed to a single
     * "canonical" NaN value).
     *
     * @param   value   a floating-point number.
     * @return the bits that represent the floating-point number.
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
     * Returns a representation of the specified floating-point value
     * according to the IEEE 754 floating-point "single format" bit
     * layout, preserving Not-a-Number (NaN) values.
     *
     * <p>Bit 31 (the bit that is selected by the mask
     * {@code 0x80000000}) represents the sign of the floating-point
     * number.
     * Bits 30-23 (the bits that are selected by the mask
     * {@code 0x7f800000}) represent the exponent.
     * Bits 22-0 (the bits that are selected by the mask
     * {@code 0x007fffff}) represent the significand (sometimes called
     * the mantissa) of the floating-point number.
     *
     * <p>If the argument is positive infinity, the result is
     * {@code 0x7f800000}.
     *
     * <p>If the argument is negative infinity, the result is
     * {@code 0xff800000}.
     *
     * <p>If the argument is NaN, the result is the integer representing
     * the actual NaN value.  Unlike the {@code floatToIntBits}
     * method, {@code floatToRawIntBits} does not collapse all the
     * bit patterns encoding a NaN to a single "canonical"
     * NaN value.
     *
     * <p>In all cases, the result is an integer that, when given to the
     * {@link #intBitsToFloat(int)} method, will produce a
     * floating-point value the same as the argument to
     * {@code floatToRawIntBits}.
     *
     * @param   value   a floating-point number.
     * @return the bits that represent the floating-point number.
     * @since 1.3
     */
    public static native int floatToRawIntBits(float value);

    /**
     * Returns the {@code float} value corresponding to a given
     * bit representation.
     * The argument is considered to be a representation of a
     * floating-point value according to the IEEE 754 floating-point
     * "single format" bit layout.
     *
     * <p>If the argument is {@code 0x7f800000}, the result is positive
     * infinity.
     *
     * <p>If the argument is {@code 0xff800000}, the result is negative
     * infinity.
     *
     * <p>If the argument is any value in the range
     * {@code 0x7f800001} through {@code 0x7fffffff} or in
     * the range {@code 0xff800001} through
     * {@code 0xffffffff}, the result is a NaN.  No IEEE 754
     * floating-point operation provided by Java can distinguish
     * between two NaN values of the same type with different bit
     * patterns.  Distinct values of NaN are only distinguishable by
     * use of the {@code Float.floatToRawIntBits} method.
     *
     * <p>In all other cases, let <i>s</i>, <i>e</i>, and <i>m</i> be three
     * values that can be computed from the argument:
     *
     * <blockquote><pre>{@code
     * int s = ((bits >> 31) == 0) ? 1 : -1;
     * int e = ((bits >> 23) & 0xff);
     * int m = (e == 0) ?
     *                 (bits & 0x7fffff) << 1 :
     *                 (bits & 0x7fffff) | 0x800000;
     * }</pre></blockquote>
     *
     * Then the floating-point result equals the value of the mathematical
     * expression <i>s</i>&middot;<i>m</i>&middot;2<sup><i>e</i>-150</sup>.
     *
     * <p>Note that this method may not be able to return a
     * {@code float} NaN with exactly same bit pattern as the
     * {@code int} argument.  IEEE 754 distinguishes between two
     * kinds of NaNs, quiet NaNs and <i>signaling NaNs</i>.  The
     * differences between the two kinds of NaN are generally not
     * visible in Java.  Arithmetic operations on signaling NaNs turn
     * them into quiet NaNs with a different, but often similar, bit
     * pattern.  However, on some processors merely copying a
     * signaling NaN also performs that conversion.  In particular,
     * copying a signaling NaN to return it to the calling method may
     * perform this conversion.  So {@code intBitsToFloat} may
     * not be able to return a {@code float} with a signaling NaN
     * bit pattern.  Consequently, for some {@code int} values,
     * {@code floatToRawIntBits(intBitsToFloat(start))} may
     * <i>not</i> equal {@code start}.  Moreover, which
     * particular bit patterns represent signaling NaNs is platform
     * dependent; although all NaN bit patterns, quiet or signaling,
     * must be in the NaN range identified above.
     *
     * @param   bits   an integer.
     * @return  the {@code float} floating-point value with the same bit
     *          pattern.
     */
    public static native float intBitsToFloat(int bits);

    /**
     * Compares two {@code Float} objects numerically.  There are
     * two ways in which comparisons performed by this method differ
     * from those performed by the Java language numerical comparison
     * operators ({@code <, <=, ==, >=, >}) when
     * applied to primitive {@code float} values:
     *
     * <ul><li>
     *          {@code Float.NaN} is considered by this method to
     *          be equal to itself and greater than all other
     *          {@code float} values
     *          (including {@code Float.POSITIVE_INFINITY}).
     * <li>
     *          {@code 0.0f} is considered by this method to be greater
     *          than {@code -0.0f}.
     * </ul>
     *
     * This ensures that the <i>natural ordering</i> of {@code Float}
     * objects imposed by this method is <i>consistent with equals</i>.
     *
     * @param   anotherFloat   the {@code Float} to be compared.
     * @return  the value {@code 0} if {@code anotherFloat} is
     *          numerically equal to this {@code Float}; a value
     *          less than {@code 0} if this {@code Float}
     *          is numerically less than {@code anotherFloat};
     *          and a value greater than {@code 0} if this
     *          {@code Float} is numerically greater than
     *          {@code anotherFloat}.
     *
     * @since   1.2
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Float anotherFloat) {
        return Float.compare(value, anotherFloat.value);
    }

    /**
     * Compares the two specified {@code float} values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new Float(f1).compareTo(new Float(f2))
     * </pre>
     *
     * @param   f1        the first {@code float} to compare.
     * @param   f2        the second {@code float} to compare.
     * @return  the value {@code 0} if {@code f1} is
     *          numerically equal to {@code f2}; a value less than
     *          {@code 0} if {@code f1} is numerically less than
     *          {@code f2}; and a value greater than {@code 0}
     *          if {@code f1} is numerically greater than
     *          {@code f2}.
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
     * Adds two {@code float} values together as per the + operator.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the sum of {@code a} and {@code b}
     * @jls 4.2.4 Floating-Point Operations
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float sum(float a, float b) {
        return a + b;
    }

    /**
     * Returns the greater of two {@code float} values
     * as if by calling {@link Math#max(float, float) Math.max}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the greater of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    /**
     * Returns the smaller of two {@code float} values
     * as if by calling {@link Math#min(float, float) Math.min}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the smaller of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -2671257302660747028L;
}
