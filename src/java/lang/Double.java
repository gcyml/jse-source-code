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
     * 如果指定数在数值上为无穷大，则返回 {@code true}；否则返回 {@code false}。
     *
     * @param   v   要测试的值。
     * @return  如果参数值是正无穷大或负无穷大，则返回 {@code true}；否则返回 {@code false}。
     */
    public static boolean isInfinite(double v) {
        return (v == POSITIVE_INFINITY) || (v == NEGATIVE_INFINITY);
    }

    /**
     * 如果参数是有限浮点值，则返回 {@code true}；否则返回 {@code false}（对于NaN和无穷大参数）。
     *
     * @param d 要测试的 {@code double} 值
     * @return 如果参数是有限浮点值，则返回 {@code true}；否则返回 {@code false}
     * @since 1.8
     */
    public static boolean isFinite(double d) {
        return Math.abs(d) <= DoubleConsts.MAX_VALUE;
    }

    /**
     * Double 的值。
     *
     * @serial
     */
    private final double value;

    /**
     * 构造一个新分配的 {@code Double} 对象，它表示基本的 {@code double} 参数。
     *
     * @param   value   用 {@code Double} 表示的值。
     */
    public Double(double value) {
        this.value = value;
    }

    /**
     * 构造一个新分配的 {@code Double} 对象，表示用字符串表示的 {@code double} 类型的浮点值。
     * 该方法与 {@code valueOf} 方法一样，可将字符串转换为 {@code double} 值。
     *
     * @param  s  要转换为 {@code Double} 的字符串。
     * @throws    NumberFormatException  如果字符串不包含可解析的数字。
     * @see       java.lang.Double#valueOf(java.lang.String)
     */
    public Double(String s) throws NumberFormatException {
        value = parseDouble(s);
    }

    /**
     * 如果此 {@code Double} 值是非数字（NaN）值，则返回 {@code true}；否则返回 {@code false}。
     *
     * @return  如果此对象表示的值为 NaN，则返回 {@code true}；否则返回 {@code false}。
     */
    public boolean isNaN() {
        return isNaN(value);
    }

    /**
     * 如果此 {@code Double} 值在数值上为无穷大，则返回 {@code true}；否则返回 {@code false}。
     *
     * @return  如果此对象所表示的值是正无穷大或负无穷大，则返回 {@code true}；
     *           否则返回 {@code false}。
     */
    public boolean isInfinite() {
        return isInfinite(value);
    }

    /**
     * 返回此 {@code Double} 对象的字符串表示形式。
     * 使用此对象表示的基本 {@code double} 值被转换为一个字符串，这与带一个参数的 {@code toString} 方法完全一样。
     *
     * @return  此对象的 {@code String} 表示形式。
     * @see java.lang.Double#toString(double)
     */
    public String toString() {
        return toString(value);
    }

    /**
     * 以 {@code byte} 形式返回此 {@code Double} 的值（通过强制转换为 {@code byte}）。
     *
     * @return  转换为 {@code byte} 类型的由此对象所表示的 {@code double} 值
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.1
     */
    public byte byteValue() {
        return (byte)value;
    }

    /**
     * 以 {@code short} 形式返回此 {@code Double} 的值（通过强制转换为 {@code short}）。
     *
     * @return  转换为 {@code short} 类型的由此对象所表示的 {@code double} 值
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.1
     */
    public short shortValue() {
        return (short)value;
    }

    /**
     * 以 {@code int} 形式返回此 {@code Double} 的值（通过强制转换为 {@code int} 类型）。
     * @jls 5.1.3 Narrowing Primitive Conversions
     *
     * @return  转换为 {@code int} 类型的由此对象所表示的 {@code double} 值
     */
    public int intValue() {
        return (int)value;
    }

    /**
     * 以 {@code long} 形式返回此 {@code Double} 的值（通过强制转换为 {@code long} 类型）。
     *
     * @return  转换为 {@code long} 类型的由此对象所表示的 {@code double} 值
     * @jls 5.1.3 Narrowing Primitive Conversions
     */
    public long longValue() {
        return (long)value;
    }

    /**
     * 返回此 {@code Double} 对象的 {@code float} 值。
     *
     * @return  转换为 {@code float} 类型的由此对象所表示的 {@code double} 值
     * @jls 5.1.3 Narrowing Primitive Conversions
     * @since JDK1.0
     */
    public float floatValue() {
        return (float)value;
    }

    /**
     * 返回此 {@code Double} 对象的 {@code double} 值。
     *
     * @return 此对象所表示的 {@code double} 值
     */
    public double doubleValue() {
        return value;
    }

    /**
     * 返回此 {@code Double} 对象的哈希码。
     * 结果是此 {@code Double} 对象所表示的基本 {@code double} 值的
     * {@code long} 整数位表示形式（与 {@link #doubleToLongBits(double)} 方法生成的结果完全一样）
     * 两部分整数之间的异或 (XOR)。也就是说，哈希码就是以下表达式的值：
     *
     * <blockquote>
     *  {@code (int)(v^(v>>>32))}
     * </blockquote>
     *
     * 其中 {@code v} 的定义为：
     *
     * <blockquote>
     *  {@code long v = Double.doubleToLongBits(this.doubleValue());}
     * </blockquote>
     *
     * @return  此对象的 {@code hash code} 值。
     */
    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    /**
     * 返回 {@code double} 值的哈希值；和 {@code Double.hashCode()} 兼容。
     *
     * @param value 需要哈希的值
     * @return {@code double} 值的哈希值。
     * @since 1.8
     */
    public static int hashCode(double value) {
        long bits = doubleToLongBits(value);
        return (int)(bits ^ (bits >>> 32));
    }

    /**
     * 将此对象与指定对象比较。当且仅当参数不是 {@code null} 而是 {@code Double} 对象，
     * 且表示的 {@code Double} 值与此对象表示的 {@code double} 值相同时，结果为 {@code true}。
     * 为此，当且仅当将方法 {@link #doubleToLongBits(double)} 应用于
     * 两个值所返回的 {@code long} 值相同时，才认为这两个 {@code double} 值相同。
     注意，在大多数情况下，对于 Double 类的两个实例 d1 和 d2，当且仅当
     *
     * <p>注意，在大多数情况下，对于 {@code Double} 类的两个实例 {@code d1} 和 {@code d2}，当且仅当
     *
     * <blockquote>
     *  {@code d1.doubleValue() == d2.doubleValue()}
     * </blockquote>
     *
     * <p>为 {@code true} 时，{@code d1.equals(d2)} 的值才为 {@code true}。但是，有以下两种例外情况：
     * <ul>
     * <li>如果 {@code d1} 和 {@code d2} 都表示 {@code Double.NaN}，
     *     那么即使  {@code Double.NaN==Double.NaN} 值为 {@code false}，
     *     {@code equals} 方法也将返回 {@code true}。
     * <li>如果 {@code d1} 表示 {@code +0.0} 而 {@code d2} 表示 {@code -0.0}，
     *     或者相反，那么即使 {@code +0.0==-0.0} 值为 {@code true}，
     *     {@code equal} 测试也将返回 {@code false}。

     * </ul>
     * 此定义使得哈希表得以正确操作。
     * @param   obj   要与之进行比较的对象。
     * @return  如果对象相同，则返回 {@code true}；否则返回 {@code false}。
     * @see java.lang.Double#doubleToLongBits(double)
     */
    public boolean equals(Object obj) {
        return (obj instanceof Double)
               && (doubleToLongBits(((Double)obj).value) ==
                      doubleToLongBits(value));
    }

    /**
     * 根据 IEEE 754 浮点双精度格式 ("double format") 位布局，返回指定浮点值的表示形式。
     *
     * <p>第 63 位（掩码 {@code 0x8000000000000000L} 选定的位）表示浮点数的符号。
     * 第 62-52 位（掩码 {@code 0x7ff0000000000000L} 选定的位）表示指数。
     * 第 51-0 位（掩码 {@code 0x000fffffffffffffL} 选定的位）表示浮点数的有效数字（有时也称为尾数）。
     *
     * <p>如果参数是正无穷大，则结果为 {@code 0x7ff0000000000000L}。
     *
     * <p>如果参数是负无穷大，则结果为 {@code 0xfff0000000000000L}。
     *
     * <p>如果参数是 NaN，则结果为 {@code 0x7ff8000000000000L}。
     *
     * <p>在所有情况下，结果都是一个 {@code long} 整数，将其赋予{@link #longBitsToDouble(long)}
     * 方法将生成一个与 {@code doubleToLongBits} 的参数相同的浮点值
     *
     * @param   value   双精度 ({@code double}) 浮点数。
     * @return 表示浮点数的位。
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
     * 根据 IEEE 754 浮点“双精度格式”位布局，返回指定浮点值的表示形式，并保留 NaN 值。
     *
     * <p>第 63 位（掩码 {@code 0x8000000000000000L} 选定的位）表示浮点数的符号。
     * 第 62-52 位（掩码 {@code 0x7ff0000000000000L} 选定的位）表示指数。
     * 第 51-0 位（掩码 {@code 0x000fffffffffffffL} 选定的位）表示浮点数的有效数字（有时也称为尾数）。
     *
     * <p>如果参数是正无穷大，则结果为 {@code 0x7ff0000000000000L}。
     *
     * <p>如果参数是负无穷大，则结果为 {@code 0xfff0000000000000L}。
     *
     * <p>如果参数是 NaN，则结果为 {@code 0x7ff8000000000000L}。
     * 与 {@code doubleToLongBits} 方法不同，{@code doubleToLongBits} 并没有压缩那些
     * 将 NaN 编码为一个“规范的”NaN 值的所有位模式。
     *
     * <p>在所有情况下，结果都是一个 {@code long} 整数，
     * 将其赋予 {@link #longBitsToDouble(long)} 方法将生成一个
     * 与 {@code doubleToRawLongBits} 的参数相同的浮点值。
     *
     * @param   value   双精度 ({@code double}) 浮点数。
     * @return 表示浮点数的位。
     * @since 1.3
     */
    public static native long doubleToRawLongBits(double value);

    /**
     * 返回对应于给定位表示形式的 {@code double} 值。
     * 根据 IEEE 754 浮点“双精度格式”位布局，参数被视为浮点值表示形式。
     *
     * <p>如果参数是 {@code 0x7ff0000000000000L}，则结果为正无穷大。
     *
     * <p>如果参数是 {@code 0xfff0000000000000L}，则结果为负无穷大。
     *
     * <p>如果参数值在 {@code 0x7ff0000000000001L} 到 {@code 0x7fffffffffffffffL}
     * 之间或者在 {@code 0xfff0000000000001L} 到 {@code 0xffffffffffffffffL} 之间，
     * 则结果为 NaN。
     * Java 提供的任何 IEEE 754 浮点操作都不能区分具有不同位模式的两个同类型 NaN 值。
     * 不同的 NaN 值只能使用 {@code Double.doubleToRawLongBits} 方法区分。
     *
     * <p>在所有其他情况下，设 <i>s</i>, <i>e</i>, 和 <i>m</i> 为可以通过以下参数计算的三个值：
     *
     * <blockquote><pre>{@code
     * int s = ((bits >> 63) == 0) ? 1 : -1;
     * int e = (int)((bits >> 52) & 0x7ffL);
     * long m = (e == 0) ?
     *                 (bits & 0xfffffffffffffL) << 1 :
     *                 (bits & 0xfffffffffffffL) | 0x10000000000000L;
     * }</pre></blockquote>
     *
     * 那么浮点结果等于算术表达式
     * <i>s</i>&middot;<i>m</i>&middot;2<sup><i>e</i>-1075</sup> 的值。
     *
     * <p>注意，此方法不能返回与 {@code long} 参数具有完全相同位模式的 {@code double} NaN。
     * IEEE 754 区分了两种 NaN：quiet NaN 和 <i>signaling NaNs</i>。
     * 这两种 NaN 之间的差别在 Java 中通常是不可见的。
     * 对 signaling NaN 进行的算术运算将它们转换为具有不同（但通常类似）位模式的 quiet NaN。
     * 但是在某些处理器上，只复制 signaling NaN 也执行这种转换。
     * 特别是在复制 signaling NaN 以将其返回给调用方法时，可能会执行这种转换。
     * 因此，{@code longBitsToDouble} 可能无法返回具有 signaling NaN 位模式的 {@code double} 值。
     * 所以，对于某些 {@code long} 值，
     * {@code doubleToRawLongBits(longBitsToDouble(start))} 可能 <i>not</i> 等于 {@code start}。
     * 此外，尽管所有 NaN 位模式（不管是 quiet NaN 还是 signaling NaN）都必须在上面提到的 NaN 范围内，
     * 但表示 signaling NaN 的特定位模式与平台有关。
     *
     * @param   bits   任意 @code long} 整数。
     * @return  具有相同位模式的 {@code double} 浮点值。
     */
    public static native double longBitsToDouble(long bits);

    /**
     * 对两个 {@code Double} 对象所表示的数值进行比较。
     * 在应用到基本 {@code double} 值时，有两种方法可以比较
     * 执行此方法生成的值与执行 Java 语言数字比较运算符（{@code <, <=, ==, >=, >}）生成的值之间的区别：
     * <ul><li>
     *          此方法认为 {@code Double.NaN} 等于它自身，且大于其他所有 {@code double} 值
     *          （包括 {@code Double.POSITIVE_INFINITY}）。
     * <li>
     *          此方法认为 {@code 0.0d} 大于 {@code -0.0d}。
     * </ul>
     * 这可以确保受此方法影响的 {@code Double} 对象的 <i>自然顺序</i> 与 <i>equals 一致</i>。
     *
     * @param   anotherDouble   要比较的 {@code Double} 值。
     * @return  如果 {@code anotherDouble} 在数字上等于此 {@code Double}，
     *           则返回 {@code 0}；
     *           如果此 {@code Double} 在数字上小于 {@code anotherDouble}，
     *           则返回小于 {@code 0} 的值；
     *
     * @since   1.2
     */
    public int compareTo(Double anotherDouble) {
        return Double.compare(value, anotherDouble.value);
    }

    /**
     * 比较两个指定的 {@code double} 值。
     * 返回整数值的符号与以下调用返回的整数的符号相同：
     * <pre>
     *    new Double(d1).compareTo(new Double(d2))
     * </pre>
     *
     * @param   d1         要比较的第一个 {@code double}
     * @param   d2         要比较的第二个 {@code double}
     * @return  如果 {@code d1} 在数字上等于 {@code d2}，则返回 {@code 0}；
     *           如果 {@code d1} 在数字上小于 {@code d2}，则返回小于 {@code 0} 的值；
     *           如果 {@code d1} 在数字上大于 {@code d2}，则返回大于 {@code 0} 的值。
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
     * 通过 + 操作得到两个 {@code double} 值的和。
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
     * 返回两个 {@code double} 的最大值，
     * 通过调用了 {@link Math#max(double, double) Math.max}。
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
     * 返回两个 {@code double} 的最小值，
     * 通过调用了 {@link Math#min(double, double) Math.min}。
     *
     * @param a 第一个操作数
     * @param b 第二个操作数
     * @return {@code a} 和 {@code b} 的最小值。
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -9172774392245257468L;
}
