/*
 * Copyright (c) 2003, 2011, Oracle and/or its affiliates. All rights reserved.
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

import java.io.Serializable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;

/**
 * 这是所有 Java 语言枚举类型的公共基本类。
 *
 * 有关枚举的更多信息，包括由编译器合成的隐式声明方法的描述，
 * 可以在 <cite>The Java&trade; Language Specification</cite>
 * 的第8.9节中找到。
 *
 * <p> 请注意，使用枚举类型作为集合或者map中的key时，可以使用
 * 专用而高效的 {@linkplain java.util.EnumSet set} 和 {@linkplain
 * java.util.EnumMap map} 实现。
 *
 * @param <E> The enum type subclass
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Class#getEnumConstants()
 * @see     java.util.EnumSet
 * @see     java.util.EnumMap
 * @since   1.5
 */
public abstract class Enum<E extends Enum<E>>
        implements Comparable<E>, Serializable {
    /**
     * 在枚举声明中声明中的这个枚举常量的名称。
     * 大部分程序员应该使用 {@link #toString} 方法而不是访问这个变量。
     */
    private final String name;

    /**
     * 返回枚举常量的名称，与枚举声明中声明的完全相同。
     *
     * <b>大多数程序员应该优先使用 {@link #toString} 方法，
     * toString 可能返回的是更用户友好的名称。</b>
     * 这个方法主要特殊情况，其中正确性依赖于取得准确的名称，
     * 释放和不释放会有很大不同。
     *
     * @return 该枚举常量的名称
     */
    public final String name() {
        return name;
    }

    /**
     * 此枚举常量的序数（它在枚举声明中的位置，其中初始常量序数为零）。
     *
     * 大部分程序员不会使用这个变量，它是设计用来复杂的基于枚举的数据结构，
     * 比如 {@link java.util.EnumSet} 和 {@link java.util.EnumMap}.
     */
    private final int ordinal;

    /**
     *  返回此枚举常量的序数（它在枚举声明中的位置，其中初始常量序数为零）。
     *
     * 大部分程序员不会使用这个变量，它是设计用来复杂的基于枚举的数据结构，
     * 比如 {@link java.util.EnumSet} 和 {@link java.util.EnumMap}.
     *
     * @return 枚举常量的序数
     */
    public final int ordinal() {
        return ordinal;
    }

    /**
     * 单独的构造方法。
     * 程序员无法调用这个方法，该构造方法用于由响应枚举类型声明的编译器发出的代码。
     *
     * @param name - 此枚举常量的名称，它是用来声明该常量的标识符。
     * @param ordinal - 枚举常量的序数（它在枚举声明中的位置，其中初始常量序数为零）。
     */
    protected Enum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    /**
     * 返回枚举常量的名称，它包含在声明中。
     * 可以重写此方法，虽然一般来说没有必要。
     * 当存在更加“程序员友好的”字符串形式时，应该使用枚举类型重写此方法。
     *
     * @return 枚举常量的名称
     */
    public String toString() {
        return name;
    }

    /**
     * 当指定对象等于此枚举常量时，返回 true。
     *
     * @param other 要与此对象进行相等性比较的对象。
     * @return  如果指定对象等于此枚举常量，则返回 true。
     */
    public final boolean equals(Object other) {
        return this==other;
    }

    /**
     * 返回枚举常量的哈希码。
     *
     * @return 枚举常量的哈希码。
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * 抛出 CloneNotSupportedException。
     * 这可保证永远不会复制枚举，这对于保留其“单元素”状态是必需的。
     *
     * @return （永远没有返回值）
     */
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * 比较此枚举与指定对象的顺序。
     * 在该对象小于、等于或大于指定对象时，分别返回负整数、零或正整数。
     *
     * 枚举常量只能与相同枚举类型的其他枚举常量进行比较。
     * 该方法实现的自然顺序就是声明常量的顺序。
     */
    public final int compareTo(E o) {
        Enum<?> other = (Enum<?>)o;
        Enum<E> self = this;
        if (self.getClass() != other.getClass() && // optimization
            self.getDeclaringClass() != other.getDeclaringClass())
            throw new ClassCastException();
        return self.ordinal - other.ordinal;
    }

    /**
     * 返回与此枚举常量的枚举类型相对应的 Class 对象。
     * 当且仅当 e1.getDeclaringClass() == e2.getDeclaringClass() 时，
     * 两个枚举常量 e1 和 e2 的枚举类型才相同。
     * （该方法返回的值与{@link Object#getClass} 返回值不同，
     * {@link Object#getClass} 方法用于带有特定常量的类主体的枚举常量。）
     *
     * @return 与此枚举常量的枚举类型相对应的 Class 对象
     */
    @SuppressWarnings("unchecked")
    public final Class<E> getDeclaringClass() {
        Class<?> clazz = getClass();
        Class<?> zuper = clazz.getSuperclass();
        return (zuper == Enum.class) ? (Class<E>)clazz : (Class<E>)zuper;
    }

    /**
     * 返回带指定名称的指定枚举类型的枚举常量。
     * 名称必须与在此类型中声明枚举常量所用的标识符完全匹配。
     * （不允许使用额外的空白字符。）
     *
     * <p>需要注意特殊的枚举类型 {@code T}，在枚举中隐式声明 {@code public static T valueOf(String)} 方法
     * 来替代这个从名称映射到相应的枚举常量的方法。
     * 枚举类型的所有常量可以通过调用该类型的隐式 {@code public static T[] values()}
     * 方法来得到。
     *
     * @param <T> 其常量将返回的枚举类型
     * @param enumType 要返回常量的枚举类型的 {@code Class} 对象
     * @param name 返回常量的常量名称
     * @return 指定名称的指定枚举类型的枚举常量
     * @throws IllegalArgumentException 如果指定的枚举类型中没有该名称的常量，
     *                                   或者指定的 class 对象无法表示一个枚举常量
     * @throws NullPointerException 如果 {@code enumType} 或 {@code name} 为空
     * @since 1.5
     */
    public static <T extends Enum<T>> T valueOf(Class<T> enumType,
                                                String name) {
        T result = enumType.enumConstantDirectory().get(name);
        if (result != null)
            return result;
        if (name == null)
            throw new NullPointerException("Name is null");
        throw new IllegalArgumentException(
            "No enum constant " + enumType.getCanonicalName() + "." + name);
    }

    /**
     * enum classes cannot have finalize methods.
     */
    protected final void finalize() { }

    /**
     * prevent default deserialization
     */
    private void readObject(ObjectInputStream in) throws IOException,
        ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize enum");
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize enum");
    }
}
