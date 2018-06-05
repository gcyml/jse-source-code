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

/**
 * Boolean 类将基本类型为 {@code boolean} 的值包装在一个对象中。
 * 一个 {@code Boolean} 类型的对象只包含一个类型为 {@code Boolean} 的字段。
 * <p>
 * 此外，此类还为 {@code boolean} 和 @code String} 的相互转换提供了许多方法，
 * 并提供了处理 {@code boolean} 时非常有用的其他一些常量和方法。
 *
 * @author  Arthur van Hoff
 * @since   JDK1.0
 */
public final class Boolean implements java.io.Serializable,
                                      Comparable<Boolean>
{
    /**
     * 对应基值 {@code true}的 {@code Boolean} 对象
     */
    public static final Boolean TRUE = new Boolean(true);

    /**
     * 对应基值 {@code false} 的 {@code Boolean} 对象。
     */
    public static final Boolean FALSE = new Boolean(false);

    /**
     * 表示基本类型 boolean 的 Class 对象。
     *
     * @since   JDK1.1
     */
    @SuppressWarnings("unchecked")
    public static final Class<Boolean> TYPE = (Class<Boolean>) Class.getPrimitiveClass("boolean");

    /**
     * Boolean 的值.
     *
     * @serial
     */
    private final boolean value;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -3665804199014368530L;

    /**
     * 分配一个表示 {@code value} 参数的{@code Boolean} 对象。
     *
     * <p><b>注意：一般情况下都不宜使用该构造方法。若不需要 <i>新</i> 的实例，
     * 则静态工厂 {@link #valueOf(boolean)} 通常是更好的选择。
     * 这有可能显著提高空间和时间性能。</b>
     *
     * @param   value   {@code Boolean}的值。
     */
    public Boolean(boolean value) {
        this.value = value;
    }

    /**
     * 如果 String 参数不为 {@code null} 且在忽略大小写时等于 {@code "true"}，
     * 则分配一个表示 {@code true} 值 的 {@code Boolean} 对象。
     * 否则分配一个表示 {@code false} 值的 {@code Boolean} 对象。示例：
     * <p>
     * {@code new Boolean("True")} 生成一个表示 {@code true} 的 {@code Boolean} 对象。<br>
     * {@code new Boolean("yes")} 生成一个表示 {@code false} 的 {@code Boolean} 对象。
     *
     * @param   s   要转换为 {@code Boolean} 的字符串。
     */
    public Boolean(String s) {
        this(parseBoolean(s));
    }

    /**
     * 将字符串参数解析为 boolean 值。
     * 如果 String 参数不为 {@code null} 且在忽略大小写时等于 {@code "true"}，
     * 则返回的 {@code boolean} 表示 {@code true} 值。 <p>
     * 示例： {@code Boolean.parseBoolean("True")} 返回 {@code true}.<br>
     * 示例： {@code Boolean.parseBoolean("yes")} 返回 {@code false}.
     *
     * @param      s   包含要解析的布尔表达式的 {@code String}
     * @return     String 参数所表示的布尔值
     * @since 1.5
     */
    public static boolean parseBoolean(String s) {
        return ((s != null) && s.equalsIgnoreCase("true"));
    }

    /**
     * 将此 {@code Boolean} 对象的值作为基本布尔值返回。
     *
     * @return  此对象的基本 {@code boolean} 值。
     */
    public boolean booleanValue() {
        return value;
    }

    /**
     * 返回一个表示指定 {@code boolean} {@code Boolean} 实例。
     * 如果指定的 {@code boolean} 值为 {@code true}， 则该方法返回 {@code Boolean.TRUE}；
     * 如果为 {@code false}， 则返回 {@code Boolean.FALSE}。
     * 如果不需要新的 {@code Boolean} 实例，则应优先使用此方法，
     * 而不是构造方法 {@link #Boolean(boolean)}， 因为此方法有可能大大提高空间和时间性能。
     *
     * @param  b 布尔值。
     * @return a 一个表示 {@code b} 的 {@code Boolean} 实例
     * @since  1.4
     */
    public static Boolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }

    /**
     * 返回一个用指定的字符串表示值的 {@code Boolean} 值。
     * 如果字符串参数不为 {@code null} 且在忽略大小写时等于 {@code "true"}，
     * 则返回的 {@code Boolean} 表示 true 值。
     *
     * @param   s   字符串。
     * @return  该字符串所表示的 {@code Boolean} 值。
     */
    public static Boolean valueOf(String s) {
        return parseBoolean(s) ? TRUE : FALSE;
    }

    /**
     * 返回一个表示指定布尔值的 {@code String} 对象。
     * 如果指定布尔值为 {@code true}，则将返回字符串 {@code "true"}，否则将返回字符串 {@code "false"} 。
     *
     * @param b  要转换的布尔值
     * @return 指定 {@code boolean} 值的字符串表达式
     * @since 1.4
     */
    public static String toString(boolean b) {
        return b ? "true" : "false";
    }

    /**
     * 返回表示该布尔值的 {@code String} 对象。
     * 如果该对象表示 @code true} 值，则返回等于 {@code "true"} 的字符串。
     * 否则返回等于 {@code "false"} 的字符串。
     *
     * @return  此对象的字符串表示形式。
     */
    public String toString() {
        return value ? "true" : "false";
    }

    /**
     * 返回该 {@code Boolean} 对象的哈希码。
     *
     * @return  如果此对象表示 {@code true} 则返回整数 1231；
     * 如果表示 {@code false} 则返回整数 1237。
     */
    // TODO: 为什么是1231 和 1237
    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    /**
     * 返回 {@code boolean} 值的哈希码；
     * 和 {@code Boolean.hashCode()} 兼容。
     *
     * @param value 需要哈希的值
     * @return {@code boolean} 值的哈希码.
     * @since 1.8
     */
    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

   /**
     * 当且仅当参数不为 {@code null}，而是一个与此对象一样，
    * 都表示同一个 {@code Boolean} 值的 {@code boolean} 对象时，才返回 {@code true}。
     *
     * @param   obj   比较的对象。
     * @return  如果这些布尔对象表示相同的值，则返回 {@code true}；否则返回 {@code false}。
     */
    public boolean equals(Object obj) {
        if (obj instanceof Boolean) {
            return value == ((Boolean)obj).booleanValue();
        }
        return false;
    }

    /**
     * 当且仅当以参数命名的系统属性存在，且等于{@code "true"} 字符串才返回{@code "true"}。
     * （从 Java <small><sup>TM</sup></small> 平台的 1.0.2 版本开始，字符串的测试不再区分大小写。）
     * 通过 {@code getProperty} 方法可访问系统属性，此方法由 {@code System} 类定义。
     * <p>
     * 如果没有以指定名称命名的属性或者指定名称为空或 null，则返回 {@code false}。
     *
     * @param   name   系统属性名。
     * @return  系统属性的 {@code boolean} 值。
     * @throws  SecurityException for the same reasons as
     *          {@link System#getProperty(String) System.getProperty}
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static boolean getBoolean(String name) {
        boolean result = false;
        try {
            result = parseBoolean(System.getProperty(name));
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        return result;
    }

    /**
     * 将此 {@code Boolean} 实例与其他实例进行比较。
     *
     * @param   b 要进行比较的 {@code Boolean} 实例
     * @return  如果对象与参数表示的布尔值相同，则返回零；
     *           如果此对象表示 true，参数表示 false，则返回一个正值；
     *           如果此对象表示 false，参数表示 true，则返回一个负值
     * @throws  NullPointerException 如果参数为 {@code null}
     * @see     Comparable
     * @since  1.5
     */
    public int compareTo(Boolean b) {
        return compare(this.value, b.value);
    }

    /**
     * 比较两个 {@code boolean} 值。
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Boolean.valueOf(x).compareTo(Boolean.valueOf(y))
     * </pre>
     *
     * @param  x 第一个需要比较的 {@code boolean}
     * @param  y 第二个需要比较的 {@code boolean}
     * @return 如果 {@code x == y} 则返回{@code 0} ；
     *          如果 {@code !x && y} 则返回小于{@code 0} 的值；
     *          如果 {@code x && !y} 则返回大于{@code 0} 的值；
     * @since 1.7
     */
    public static int compare(boolean x, boolean y) {
        return (x == y) ? 0 : (x ? 1 : -1);
    }

    /**
     * 返回逻辑和操作后的{@code boolean} 结果。
     *
     * @param a 第一个运算数
     * @param b 第二个运算数
     * @return {@code a} 和 {@code b} 的逻辑和
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static boolean logicalAnd(boolean a, boolean b) {
        return a && b;
    }

    /**
     * 返回逻辑或操作后的{@code boolean} 结果。
     *
     * @param a 第一个运算数
     * @param b 第二个运算数
     * @return {@code a} 和 {@code b} 的逻辑或
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static boolean logicalOr(boolean a, boolean b) {
        return a || b;
    }

    /**
     * 返回逻辑异或操作后的{@code boolean} 结果。
     *
     * @param a 第一个运算数
     * @param b 第二个运算数
     * @return {@code a} 和 {@code b} 的逻辑异或
     * @see java.util.function.BinaryOperator
     * @since 1.8
     */
    public static boolean logicalXor(boolean a, boolean b) {
        return a ^ b;
    }
}
