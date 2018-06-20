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
import sun.misc.FpUtils;
import sun.misc.DoubleConsts;

/**
 * {@code Double} 类在对象中包装一个基本类型 {@code double} 的值。
 * 每个 {@code Double} 类型的对象都包含一个 {@code double} 类型的字段。
 *
 * <p>此外，该类还提供多个方法，可以将 {@code double} 转换为 {@code String}，
 * 将 {@code String} 转换为 {@code double}，也提供了其他一些处理 {@code double} 时有用的常量和方法。
 *
 * @author  Lee Boynton
 * @author  Arthur van Hoff
 * @author  Joseph D. Darcy
 * @since JDK1.0
 */
public final class Double extends Number implements Comparable<Double> {
    /**
     * 保存 {@code double} 类型的正无穷大值的常量。
     * 它等于 {@code Double.longBitsToDouble(0x7ff0000000000000L)} 返回的值。
     */
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    /**
     * 保存 {@code double} 类型的负无穷大值的常量。
     * 它等于 {@code Double.longBitsToDouble(0xfff0000000000000L)} 返回的值。
     */
    public static final double NEGATIVE_INFINITY = -1.0 / 0.0;

    /**
     * 保存 {@code double} 类型的 NaN 值的常量。
     * 它等于 {@code Double.longBitsToDouble(0x7ff8000000000000L)} 返回的值。
     */
    public static final double NaN = 0.0d / 0.0;

    /**
     * 保存 {@code double} 类型的最大正有限值的常量，最大正有限值为 (2-2<sup>-52</sup>)&middot;2<sup>1023</sup>。
     * 它等于十六进制的浮点字面值 {@code 0x1.fffffffffffffP+1023}，也等于
     * {@code Double.longBitsToDouble(0x7fefffffffffffffL)}。
     */
    public static final double MAX_VALUE = 0x1.fffffffffffffP+1023; // 1.7976931348623157e+308

    /**
     * 保存 double 类型的最小正标准值值的常量，最小正标准值为
     * {@code double}, 2<sup>-1022</sup>。
     * 它等于十六进制的浮点字面值 {@code 0x1.0p-1022}，也等于
     * {@code Double.longBitsToDouble(0x0010000000000000L)}。
     *
     * @since 1.6
     */
    public static final double MIN_NORMAL = 0x1.0p-1022; // 2.2250738585072014E-308

    /**
     * 保存 double 类型的最小正非零值的常量，最小正非零值为
     * {@code double}, 2<sup>-1074</sup>。
     * 它等于十六进制的浮点字面值 {@code 0x0.0000000000001P-1022}，也等于
     * {@code Double.longBitsToDouble(0x1L)}。
     *
     * @since 1.6
     */
    public static final double MIN_VALUE = 0x0.0000000000001P-1022; // 4.9e-324

    /**
     * 标准化 {@code double} 变量可能具有的最小指数。
     * 它等于 {@code Math.getExponent(Double.MAX_VALUE)} 返回的值。
     *
     * @since 1.6
     */
    public static final int MAX_EXPONENT = 1023;

    /**
     * 标准化 {@code double} 变量可能具有的最小指数。
     * 它等于 {@code Math.getExponent(Double.MIN_NORMAL) 返回的值。.
     *
     * @since 1.6
     */
    public static final int MIN_EXPONENT = -1022;

    /**
     * 用于表示 {@code double} 值的位数。
     *
     * @since 1.5
     */
    public static final int SIZE = 64;

    /**
     * 用来表示一个 {@code double} 的字节数。
     *
     * @since 1.8
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    /**
     * 表示基本类型 {@code Class} 的 {@code Class} 实例。
     *
     * @since JDK1.1
     */
    @SuppressWarnings("unchecked")
    public static final Class<Double>   TYPE = (Class<Double>) Class.getPrimitiveClass("double");

    /**
     * 返回 {@code double} 参数的字符串表达形式。
     * 下面提到的所有字符都是 ASCII 字符。
     * <ul>
     * <li> 如果参数为 NaN，那么结果为字符串 "{@code NaN}"。
     * <li> 否则，结果是表示参数符号和数值（绝对值）的字符串。如果符号为负，那么结果的第一个字符是
     * '{@code -}'({@code '\u005Cu002D'})；
     * 如果符号为正，那么结果中不显示符号字符。对于数值 <i>m</i>：
     * <ul>
     * <li> 如果 <i>m</i> 为无穷大，则用字符 {@code "Infinity"} 表示；
     * 因此，正无穷大生成结果是 {@code "Infinity"}，负无穷大生成结果 {@code "-Infinity"}。
     *
     * <li> 如果 <i>m</i> 为 0，则用字符 {@code "0.0"} 表示；
     * 因此负 0 生成结果 {@code "-0.0"}，正 0 生成结果 {@code "0.0"}。
     *
     * <li> 如果 <i>m</i> 大于或等于 10<sup>-3</sup> 但小于 10<sup>7</sup>，
     * 则采用不带前导 0 的十进制形式，用 <i>m</i> 的整数部分表示，后跟 '{@code .}' ({@code '\u005Cu002E'})，
     * 再后面是表示 <i>m</i> 小数部分的一个或多个十进制数字。
     *
     * <li>如果 <i>m</i> 小于 10<sup>-3</sup> 或大于等于 10<sup>7</sup>，
     * 则使用所谓的“计算机科学记数法”表示。
     * 设 <i>n</i> 为满足 10<sup><i>n</i></sup> &le; <i>m</i> {@literal <}
     * 10<sup><i>n</i>+1</sup> 的唯一整数；
     * 然后设 <i>a</i> 为 <i>m</i> 与 10<sup><i>n</i></sup> 的精确算术商，
     * 从而 1 &le; <i>a</i> {@literal <} 10。
     * 那么，数值便表示为 <i>a</i> 的整数部分，其形式为：一个十进制数字，
     * 后跟 '{@code .}'({@code '\u005Cu002E'})，接着显示表示 <i>a</i>小数部分的十进制数字，
     * 再后面是字母 '{@code E}' ({@code '\u005Cu0045'})，最后是用十进制整数形式表示的 <i>n</i>，
     * 这与方法 {@link Integer#toString(int)} 生成的结果一样。
     * </ul>
     * </ul>
     * 必须为 <i>m</i> 或 <i>a</i> 的小数部分显示多少位呢？
     * 至少必须有一位数来表示小数部分，除此之外，
     * 需要更多（但只能和需要的一样多）位数来唯一地区分参数值和 {@code double} 类型的邻近值。
     * 这就是说，假设 <i>x</i> 是用十进制表示法表示的精确算术值，是通过对有限非 0 参数 <i>d</i> 调用此方法生成的。
     * 那么 <i>d</i> 一定是最接近 <i>x</i> 的 {@code double} 值；
     * 如果两个 {@code double} 值都同等地接近 <i>x</i>，那么 <i>d</i> 必须是其中之一，
     * 并且 <i>d</i> 的有效数字的最低有效位必须是 {@code 0}。
     *
     * <p>要创建浮点值的本地化字符串表示形式，请使用 {@link java.text.NumberFormat}.
     *
     * @param   d   要转换的 {@code double} 值。
     * @return 参数的字符串表示形式。
     */
    public static String toString(double d) {
        return FloatingDecimal.toJavaFormatString(d);
    }

    /**
     * 返回 {@code double} 参数的十六进制字符串表示形式。
     * 下面提到的所有字符都是 ASCII 字符。
     *
     * <ul>
     * <li> 如果参数为 NaN，那么结果为字符串 "{@code NaN}"。
     * <li> 否则，结果是表示参数符号和数值的字符串。如果符号为负，那么结果的第一个字符是
     * '{@code -}'({@code '\u005Cu002D'})；
     * 如果符号为正，那么结果中不显示符号字符。对于数值 <i>m</i>：
     *
     * <ul>
     * <li> 如果 <i>m</i> 为无穷大，则用字符串 {@code "Infinity"} 表示；
     * 因此，正无穷大生成结果 {@code "Infinity"}，负无穷大生成结果 {@code "-Infinity"}。
     *
     * <li> 如果 <i>m</i> 为 0，则用字符串 {@code "0x0.0p0"} 表示；
     * 因此，负 0 生成结果 {@code "-0x0.0p0"}，正 0 生成结果 {@code "0x0.0p0"}。
     *
     * <li> 如果 <i>m</i> 是具有标准化表示形式的 {@code double} 值，
     * 则使用子字符串表示有效数字和指数字段。有效数字用字符 {@code "0x1."} 表示，
     * 后跟该有效数字剩余小数部分的小写十六进制表示形式。
     * 除非所有位数都为 0，否则移除十六进制表示中的尾部 0，在所有位数都为零的情况下，
     * 可以用一个 0 表示。接下来用 {@code "p"} 表示指数，后跟无偏指数的十进制字符串，
     * 该值与通过对指数值调用 {@link Integer#toString(int) Integer.toString} 生成的值相同。
     * 和指数字段。
     *
     * <li> 如果 <i>m</i> 是非标准表示形式的 {@code double} 值，
     * 则用字符 {@code "0x0."} 表示有效数字，后跟该有效数字剩余小数部分的十六进制表示形式。
     * 移除十六进制表示中的尾部 0。然后用 {@code "p-1022"} 表示指数。
     * 注意，在非标准有效数字中，必须至少有一个非 0 数字。
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
     * <tr><td>{@code Double.MAX_VALUE}</td>
     *     <td>{@code 0x1.fffffffffffffp1023}</td>
     * <tr><td>{@code Minimum Normal Value}</td>
     *     <td>{@code 0x1.0p-1022}</td>
     * <tr><td>{@code Maximum Subnormal Value}</td>
     *     <td>{@code 0x0.fffffffffffffp-1022}</td>
     * <tr><td>{@code Double.MIN_VALUE}</td>
     *     <td>{@code 0x0.0000000000001p-1022}</td>
     * </table>
     * @param   d   要转换的 {@code double} 值。
     * @return 参数的十六进制字符串表示形式。
     * @since 1.5
     * @author Joseph D. Darcy
     */
    public static String toHexString(double d) {
        /*
         * Modeled after the "a" conversion specifier in C99, section
         * 7.19.6.1; however, the output of this method is more
         * tightly specified.
         */
        if (!isFinite(d) )
            // For infinity and NaN, use the decimal output.
            return Double.toString(d);
        else {
            // Initialized to maximum size of output.
            StringBuilder answer = new StringBuilder(24);

            if (Math.copySign(1.0, d) == -1.0)    // value is negative,
                answer.append("-");                  // so append sign info

            answer.append("0x");

            d = Math.abs(d);

            if(d == 0.0) {
                answer.append("0.0p0");
            } else {
                boolean subnormal = (d < DoubleConsts.MIN_NORMAL);

                // Isolate significand bits and OR in a high-order bit
                // so that the string representation has a known
                // length.
                long signifBits = (Double.doubleToLongBits(d)
                                   & DoubleConsts.SIGNIF_BIT_MASK) |
                    0x1000000000000000L;

                // Subnormal values have a 0 implicit bit; normal
                // values have a 1 implicit bit.
                answer.append(subnormal ? "0." : "1.");

                // Isolate the low-order 13 digits of the hex
                // representation.  If all the digits are zero,
                // replace with a single 0; otherwise, remove all
                // trailing zeros.
                String signif = Long.toHexString(signifBits).substring(3,16);
                answer.append(signif.equals("0000000000000") ? // 13 zeros
                              "0":
                              signif.replaceFirst("0{1,12}$", ""));

                answer.append('p');
                // If the value is subnormal, use the E_min exponent
                // value for double; otherwise, extract and report d's
                // exponent (the representation of a subnormal uses
                // E_min -1).
                answer.append(subnormal ?
                              DoubleConsts.MIN_EXPONENT:
                              Math.getExponent(d));
            }
            return answer.toString();
        }
    }

    /**
     * 返回保存用参数字符串 {@code s} 表示的 {@code double} 值的 {@code Double} 对象。
     *
     * <p> 如果 {@code s} 为 {@code null}，则抛出 {@code NullPointerException} 异常。
     *
     * <p>忽略 {@code s} 中的前导空白字符和尾部空白字符。
     * 就像调用 {@link String#trim} 方法那样移除空白；这就是说，ASCII 空格和控制字符都要移除。
     * {@code s} 的其余部分应该按词法语法规则所描述的那样构成一个 <i>FloatValue</i> ：
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
     * 其中 <i>Sign</i>, <i>FloatingPointLiteral</i>,
     * <i>HexNumeral</i>, <i>HexDigits</i>, <i>SignedInteger</i> 和
     * <i>FloatTypeSuffix</i> 与 <cite>The Java&trade; Language Specification</cite>
     * 的词法结构部分中定义的相同。,
     * 如果 {@code s} 不具有 <i>FloatValue</i> 的形式，则抛出 {@code NumberFormatException}。
     * 否则，可以认为 {@code s} 是一个以常用的“计算机科学记数法”表示的精确十进制值，
     * 或者是一个精确的十六进制值；在概念上，这个精确的数值将被转换为一个“无限精确的”二进制值，
     * 然后根据常用的 IEEE 754 浮点算法的“舍入为最接近的数”规则，将该值舍入为 {@code double} 类型，
     * 其中包括保留 0 值的符号。
     *
     * 注意，循环到最近的规则也意味着上溢和下溢行为；如果 {@code s} 的精确值在幅度上足够大
     * （大于或等于({@link #MAX_VALUE} + {@link Math#ulp(double) ulp(MAX_VALUE)}/2），
     * 则舍入为一个无穷大的 {@code double}，
     * 如果 {@code s} 的精确值足够小（幅度小于或等于 {@link #MIN_VALUE}/2），
     * 则将舍入为 0 的 浮点值。
     *
     * 最后，返回表示此 {@code double} 值的 {@code Double} 对象。
     *
     * <p> 要解释浮点值的本地化字符串表示形式，请使用 {@link java.text.NumberFormat}。
     *
     * <p>注意，尾部格式说明符、确定浮点字面值类型的说明符（{@code 1.0f} 是一个 {@code float} 值；
     * {@code 1.0d} 是一个 @code double 值）<em>不会</em>影响此方法的结果。
     * 换句话说，输入字符串的数值被直接转换为目标浮点类型。
     * 分两步的转换（先将字符串转换为 {@code float} ，然后将 {@code float} 转换为 {@code double}）
     * <em>不</em>同于直接将字符串转换为 {@code double}。
     * 例如，{@code float} 字面值 {@code 0.1f} 等于 {@code double} 值 {@code 0.10000000149011612}；
     * {@code float} 字面值 {@code 0.1f} 表示与 double 字面值 {@code 0.1} 不同的数值。
     * （数值 0.1 无法用二进制浮点数准确表示。)
     *
     * <p>为了避免因为对无效字符串调用此方法而导致抛出 {@code NumberFormatException}，
     * 可以使用以下正则表达式作为输入到屏幕的字符串：
     *
     * <pre>{@code
     *  final String Digits     = "(\\p{Digit}+)";
     *  final String HexDigits  = "(\\p{XDigit}+)";
     *  // an exponent is 'e' or 'E' followed by an optionally
     *  // signed decimal integer.
     *  final String Exp        = "[eE][+-]?"+Digits;
     *  final String fpRegex    =
     *      ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
     *       "[+-]?(" + // Optional sign character
     *       "NaN|" +           // "NaN" string
     *       "Infinity|" +      // "Infinity" string
     *
     *       // A decimal floating-point string representing a finite positive
     *       // number without a leading sign has at most five basic pieces:
     *       // Digits . Digits ExponentPart FloatTypeSuffix
     *       //
     *       // Since this method allows integer-only strings as input
     *       // in addition to strings of floating-point literals, the
     *       // two sub-patterns below are simplifications of the grammar
     *       // productions from section 3.10.2 of
     *       // The Java Language Specification.
     *
     *       // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
     *       "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
     *
     *       // . Digits ExponentPart_opt FloatTypeSuffix_opt
     *       "(\\.("+Digits+")("+Exp+")?)|"+
     *
     *       // Hexadecimal strings
     *       "((" +
     *        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
     *        "(0[xX]" + HexDigits + "(\\.)?)|" +
     *
     *        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
     *        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
     *
     *        ")[pP][+-]?" + Digits + "))" +
     *       "[fFdD]?))" +
     *       "[\\x00-\\x20]*");// Optional trailing "whitespace"
     *
     *  if (Pattern.matches(fpRegex, myString))
     *      Double.valueOf(myString); // Will not throw NumberFormatException
     *  else {
     *      // Perform suitable alternative action
     *  }
     * }</pre>
     *
     * @param      s   要解析的字符串。
     * @return     保存用 {@code String} 参数表示的值的 {@code Double}对象。
     * @throws     NumberFormatException  如果字符串不包含可解析的数。
     */
    public static Double valueOf(String s) throws NumberFormatException {
        return new Double(parseDouble(s));
    }

    /**
     * 返回表示指定的 {@code double} 值的 {@code Double} 实例。
     * 如果不需要新的 {@code Double} 实例，则通常应优先使用此方法，
     * 而不是构造方法 {@link #Double(double)}，
     * 因为此方法可能通过缓存经常请求的值来显著提高空间和时间性能。
     *
     * @param  d 一个 double 值。
     * @return 表示 {@code d} 的 {@code Double} 实例。
     * @since  1.5
     */
    public static Double valueOf(double d) {
        return new Double(d);
    }

    /**
     * 返回一个新的 {@code double} 值，该值被初始化为用指定 {@code String} 表示的值，
     * 这与 {@code Double} 类的 {@code valueOf} 方法一样。
     *
     * @param  s   要解析的字符串。
     * @return 由字符串参数表示的 {@code double} 值。
     * @throws NullPointerException  如果字符串为空
     * @throws NumberFormatException 如果字符串不包含可解析的 {@code double} 值。
     * @see    java.lang.Double#valueOf(String)
     * @since 1.2
     */
    public static double parseDouble(String s) throws NumberFormatException {
        return FloatingDecimal.parseDouble(s);
    }

    /**
     * 如果指定的数是一个 NaN 值，则返回 {@code true}；否则返回 {@code false}。
     *
     * @param   v   要测试的值。
     * @return  如果参数值为 NaN，则返回 {@code true}；否则返回 {@code false}。
     */
    public static boolean isNaN(double v) {
        return (v != v);
    }

    /**
     * Returns {@code true} if the specified number is infinitely
     * large in magnitude, {@code false} otherwise.
     *
     * @param   v   the value to be tested.
     * @return  {@code true} if the value of the argument is positive
     *          infinity or negative infinity; {@code false} otherwise.
     */
    public static boolean isInfinite(double v) {
        return (v == POSITIVE_INFINITY) || (v == NEGATIVE_INFINITY);
    }

    /**
     * Returns {@code true} if the argument is a finite floating-point
     * value; returns {@code false} otherwise (for NaN and infinity
     * arguments).
     *
     * @param d the {@code double} value to be tested
     * @return {@code true} if the argument is a finite
     * floating-point value, {@code false} otherwise.
     * @since 1.8
     */
    public static boolean isFinite(double d) {
        return Math.abs(d) <= DoubleConsts.MAX_VALUE;
    }

    /**
     * The value of the Double.
     *
     * @serial
     */
    private final double value;

    /**
     * Constructs a newly allocated {@code Double} object that
     * represents the primitive {@code double} argument.
     *
     * @param   value   the value to be represented by the {@code Double}.
     */
    public Double(double value) {
        this.value = value;
    }

    /**
     * Constructs a newly allocated {@code Double} object that
     * represents the floating-point value of type {@code double}
     * represented by the string. The string is converted to a
     * {@code double} value as if by the {@code valueOf} method.
     *
     * @param  s  a string to be converted to a {@code Double}.
     * @throws    NumberFormatException  if the string does not contain a
     *            parsable number.
     * @see       java.lang.Double#valueOf(java.lang.String)
     */
    public Double(String s) throws NumberFormatException {
        value = parseDouble(s);
    }

    /**
     * Returns {@code true} if this {@code Double} value is
     * a Not-a-Number (NaN), {@code false} otherwise.
     *
     * @return  {@code true} if the value represented by this object is
     *          NaN; {@code false} otherwise.
     */
    public boolean isNaN() {
        return isNaN(value);
    }

    /**
     * Returns {@code true} if this {@code Double} value is
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
     * Returns a string representation of this {@code Double} object.
     * The primitive {@code double} value represented by this
     * object is converted to a string exactly as if by the method
     * {@code toString} of one argument.
     *
     * @return  a {@code String} representation of this object.
     * @see java.lang.Double#toString(double)
     */
    public String toString() {
        return toString(value);
    }

    /**
     * Returns the value of this {@code Double} as a {@code byte}
     * after a narrowing primitive conversion.
     *
     * @return  the {@code double} value represented by this object
     *          converted to type {@code byte}
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.1
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * Returns the value of this {@code Double} as a {@code short}
     * after a narrowing primitive conversion.
     *
     * @return  the {@code double} value represented by this object
     *          converted to type {@code short}
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.1
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     * Returns the value of this {@code Double} as an {@code int}
     * after a narrowing primitive conversion.
     * @jls 5.1.3 Narrowing Primitive Conversions
     *
     * @return  the {@code double} value represented by this object
     *          converted to type {@code int}
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * Returns the value of this {@code Double} as a {@code long}
     * after a narrowing primitive conversion.
     *
     * @return  the {@code double} value represented by this object
     *          converted to type {@code long}
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public long longValue() {
        return (long)value;
    }

    /**
     * Returns the value of this {@code Double} as a {@code float}
     * after a narrowing primitive conversion.
     *
     * @return  the {@code double} value represented by this object
     *          converted to type {@code float}
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.0
     */
    public float floatValue() {
        return (float)value;
    }

    /**
     * Returns the {@code double} value of this {@code Double} object.
     *
     * @return the {@code double} value represented by this object
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Returns a hash code for this {@code Double} object. The
     * result is the exclusive OR of the two halves of the
     * {@code long} integer bit representation, exactly as
     * produced by the method {@link #doubleToLongBits(double)}, of
     * the primitive {@code double} value represented by this
     * {@code Double} object. That is, the hash code is the value
     * of the expression:
     *
     * <blockquote>
     *  {@code (int)(v^(v>>>32))}
     * </blockquote>
     *
     * where {@code v} is defined by:
     *
     * <blockquote>
     *  {@code long v = Double.doubleToLongBits(this.doubleValue());}
     * </blockquote>
     *
     * @return  a {@code hash code} value for this object.
     */
    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    /**
     * Returns a hash code for a {@code double} value; compatible with
     * {@code Double.hashCode()}.
     *
     * @param value the value to hash
     * @return a hash code value for a {@code double} value.
     * @since 1.8
     */
    public static int hashCode(double value) {
        long bits = doubleToLongBits(value);
        return (int)(bits ^ (bits >>> 32));
    }

    /**
     * Compares this object against the specified object.  The result
     * is {@code true} if and only if the argument is not
     * {@code null} and is a {@code Double} object that
     * represents a {@code double} that has the same value as the
     * {@code double} represented by this object. For this
     * purpose, two {@code double} values are considered to be
     * the same if and only if the method {@link
     * #doubleToLongBits(double)} returns the identical
     * {@code long} value when applied to each.
     *
     * <p>Note that in most cases, for two instances of class
     * {@code Double}, {@code d1} and {@code d2}, the
     * value of {@code d1.equals(d2)} is {@code true} if and
     * only if
     *
     * <blockquote>
     *  {@code d1.doubleValue() == d2.doubleValue()}
     * </blockquote>
     *
     * <p>also has the value {@code true}. However, there are two
     * exceptions:
     * <ul>
     * <li>If {@code d1} and {@code d2} both represent
     *     {@code Double.NaN}, then the {@code equals} method
     *     returns {@code true}, even though
     *     {@code Double.NaN==Double.NaN} has the value
     *     {@code false}.
     * <li>If {@code d1} represents {@code +0.0} while
     *     {@code d2} represents {@code -0.0}, or vice versa,
     *     the {@code equal} test has the value {@code false},
     *     even though {@code +0.0==-0.0} has the value {@code true}.
     * </ul>
     * This definition allows hash tables to operate properly.
     * @param   obj   the object to compare with.
     * @return  {@code true} if the objects are the same;
     *          {@code false} otherwise.
     * @see java.lang.Double#doubleToLongBits(double)
     */
    public boolean equals(Object obj) {
        return (obj instanceof Double)
               && (doubleToLongBits(((Double)obj).value) ==
                      doubleToLongBits(value));
    }

    /**
     * Returns a representation of the specified floating-point value
     * according to the IEEE 754 floating-point "double
     * format" bit layout.
     *
     * <p>Bit 63 (the bit that is selected by the mask
     * {@code 0x8000000000000000L}) represents the sign of the
     * floating-point number. Bits
     * 62-52 (the bits that are selected by the mask
     * {@code 0x7ff0000000000000L}) represent the exponent. Bits 51-0
     * (the bits that are selected by the mask
     * {@code 0x000fffffffffffffL}) represent the significand
     * (sometimes called the mantissa) of the floating-point number.
     *
     * <p>If the argument is positive infinity, the result is
     * {@code 0x7ff0000000000000L}.
     *
     * <p>If the argument is negative infinity, the result is
     * {@code 0xfff0000000000000L}.
     *
     * <p>If the argument is NaN, the result is
     * {@code 0x7ff8000000000000L}.
     *
     * <p>In all cases, the result is a {@code long} integer that, when
     * given to the {@link #longBitsToDouble(long)} method, will produce a
     * floating-point value the same as the argument to
     * {@code doubleToLongBits} (except all NaN values are
     * collapsed to a single "canonical" NaN value).
     *
     * @param   value   a {@code double} precision floating-point number.
     * @return the bits that represent the floating-point number.
     */
    public static long doubleToLongBits(double value) {
        long result = doubleToRawLongBits(value);
        // Check for NaN based on values of bit fields, maximum
        // exponent and nonzero significand.
        if ( ((result & DoubleConsts.EXP_BIT_MASK) ==
              DoubleConsts.EXP_BIT_MASK) &&
             (result & DoubleConsts.SIGNIF_BIT_MASK) != 0L)
            result = 0x7ff8000000000000L;
        return result;
    }

    /**
     * Returns a representation of the specified floating-point value
     * according to the IEEE 754 floating-point "double
     * format" bit layout, preserving Not-a-Number (NaN) values.
     *
     * <p>Bit 63 (the bit that is selected by the mask
     * {@code 0x8000000000000000L}) represents the sign of the
     * floating-point number. Bits
     * 62-52 (the bits that are selected by the mask
     * {@code 0x7ff0000000000000L}) represent the exponent. Bits 51-0
     * (the bits that are selected by the mask
     * {@code 0x000fffffffffffffL}) represent the significand
     * (sometimes called the mantissa) of the floating-point number.
     *
     * <p>If the argument is positive infinity, the result is
     * {@code 0x7ff0000000000000L}.
     *
     * <p>If the argument is negative infinity, the result is
     * {@code 0xfff0000000000000L}.
     *
     * <p>If the argument is NaN, the result is the {@code long}
     * integer representing the actual NaN value.  Unlike the
     * {@code doubleToLongBits} method,
     * {@code doubleToRawLongBits} does not collapse all the bit
     * patterns encoding a NaN to a single "canonical" NaN
     * value.
     *
     * <p>In all cases, the result is a {@code long} integer that,
     * when given to the {@link #longBitsToDouble(long)} method, will
     * produce a floating-point value the same as the argument to
     * {@code doubleToRawLongBits}.
     *
     * @param   value   a {@code double} precision floating-point number.
     * @return the bits that represent the floating-point number.
     * @since 1.3
     */
    public static native long doubleToRawLongBits(double value);

    /**
     * Returns the {@code double} value corresponding to a given
     * bit representation.
     * The argument is considered to be a representation of a
     * floating-point value according to the IEEE 754 floating-point
     * "double format" bit layout.
     *
     * <p>If the argument is {@code 0x7ff0000000000000L}, the result
     * is positive infinity.
     *
     * <p>If the argument is {@code 0xfff0000000000000L}, the result
     * is negative infinity.
     *
     * <p>If the argument is any value in the range
     * {@code 0x7ff0000000000001L} through
     * {@code 0x7fffffffffffffffL} or in the range
     * {@code 0xfff0000000000001L} through
     * {@code 0xffffffffffffffffL}, the result is a NaN.  No IEEE
     * 754 floating-point operation provided by Java can distinguish
     * between two NaN values of the same type with different bit
     * patterns.  Distinct values of NaN are only distinguishable by
     * use of the {@code Double.doubleToRawLongBits} method.
     *
     * <p>In all other cases, let <i>s</i>, <i>e</i>, and <i>m</i> be three
     * values that can be computed from the argument:
     *
     * <blockquote><pre>{@code
     * int s = ((bits >> 63) == 0) ? 1 : -1;
     * int e = (int)((bits >> 52) & 0x7ffL);
     * long m = (e == 0) ?
     *                 (bits & 0xfffffffffffffL) << 1 :
     *                 (bits & 0xfffffffffffffL) | 0x10000000000000L;
     * }</pre></blockquote>
     *
     * Then the floating-point result equals the value of the mathematical
     * expression <i>s</i>&middot;<i>m</i>&middot;2<sup><i>e</i>-1075</sup>.
     *
     * <p>Note that this method may not be able to return a
     * {@code double} NaN with exactly same bit pattern as the
     * {@code long} argument.  IEEE 754 distinguishes between two
     * kinds of NaNs, quiet NaNs and <i>signaling NaNs</i>.  The
     * differences between the two kinds of NaN are generally not
     * visible in Java.  Arithmetic operations on signaling NaNs turn
     * them into quiet NaNs with a different, but often similar, bit
     * pattern.  However, on some processors merely copying a
     * signaling NaN also performs that conversion.  In particular,
     * copying a signaling NaN to return it to the calling method
     * may perform this conversion.  So {@code longBitsToDouble}
     * may not be able to return a {@code double} with a
     * signaling NaN bit pattern.  Consequently, for some
     * {@code long} values,
     * {@code doubleToRawLongBits(longBitsToDouble(start))} may
     * <i>not</i> equal {@code start}.  Moreover, which
     * particular bit patterns represent signaling NaNs is platform
     * dependent; although all NaN bit patterns, quiet or signaling,
     * must be in the NaN range identified above.
     *
     * @param   bits   any {@code long} integer.
     * @return  the {@code double} floating-point value with the same
     *          bit pattern.
     */
    public static native double longBitsToDouble(long bits);

    /**
     * Compares two {@code Double} objects numerically.  There
     * are two ways in which comparisons performed by this method
     * differ from those performed by the Java language numerical
     * comparison operators ({@code <, <=, ==, >=, >})
     * when applied to primitive {@code double} values:
     * <ul><li>
     *          {@code Double.NaN} is considered by this method
     *          to be equal to itself and greater than all other
     *          {@code double} values (including
     *          {@code Double.POSITIVE_INFINITY}).
     * <li>
     *          {@code 0.0d} is considered by this method to be greater
     *          than {@code -0.0d}.
     * </ul>
     * This ensures that the <i>natural ordering</i> of
     * {@code Double} objects imposed by this method is <i>consistent
     * with equals</i>.
     *
     * @param   anotherDouble   the {@code Double} to be compared.
     * @return  the value {@code 0} if {@code anotherDouble} is
     *          numerically equal to this {@code Double}; a value
     *          less than {@code 0} if this {@code Double}
     *          is numerically less than {@code anotherDouble};
     *          and a value greater than {@code 0} if this
     *          {@code Double} is numerically greater than
     *          {@code anotherDouble}.
     *
     * @since   1.2
     */
    public int compareTo(Double anotherDouble) {
        return Double.compare(value, anotherDouble.value);
    }

    /**
     * Compares the two specified {@code double} values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new Double(d1).compareTo(new Double(d2))
     * </pre>
     *
     * @param   d1        the first {@code double} to compare
     * @param   d2        the second {@code double} to compare
     * @return  the value {@code 0} if {@code d1} is
     *          numerically equal to {@code d2}; a value less than
     *          {@code 0} if {@code d1} is numerically less than
     *          {@code d2}; and a value greater than {@code 0}
     *          if {@code d1} is numerically greater than
     *          {@code d2}.
     * @since 1.4
     */
    public static int compare(double d1, double d2) {
        if (d1 < d2)
            return -1;           // Neither val is NaN, thisVal is smaller
        if (d1 > d2)
            return 1;            // Neither val is NaN, thisVal is larger

        // Cannot use doubleToRawLongBits because of possibility of NaNs.
        long thisBits    = Double.doubleToLongBits(d1);
        long anotherBits = Double.doubleToLongBits(d2);

        return (thisBits == anotherBits ?  0 : // Values are equal
                (thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
                 1));                          // (0.0, -0.0) or (NaN, !NaN)
    }

    /**
     * Adds two {@code double} values together as per the + operator.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the sum of {@code a} and {@code b}
     * @jls 4.2.4 Floating-Point Operations
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static double sum(double a, double b) {
        return a + b;
    }

    /**
     * Returns the greater of two {@code double} values
     * as if by calling {@link Math#max(double, double) Math.max}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the greater of {@code a} and {@code b}
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    /**
     * Returns the smaller of two {@code double} values
     * as if by calling {@link Math#min(double, double) Math.min}.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the smaller of {@code a} and {@code b}.
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -9172774392245257468L;
}
