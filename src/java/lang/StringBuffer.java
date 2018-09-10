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

import java.util.Arrays;

/**
 * 线程安全的可变字符序列。
 * 一个类似于 {@link String} 的字符串缓冲区，但不能修改。
 * 虽然在任意时间点上它都包含某种特定的字符序列，但通过某些方法调用可以改变该序列的长度和内容。
 * <p>
 * 可将字符串缓冲区安全地用于多个线程。
 * 可以在必要时对这些方法进行同步，
 * 因此任意特定实例上的所有操作就好像是以串行顺序发生的，该顺序与所涉及的每个线程进行的方法调用顺序一致。
 * <p>
 * {@code StringBuffer} 上的主要操作是 {@code append} 和 insert 方法，
 * 可重载这些方法，以接受任意类型的数据。
 * 每个方法都能有效地将给定的数据转换成字符串，然后将该字符串的字符追加或插入到字符串缓冲区中。
 * {@code append} 方法始终将这些字符添加到缓冲区的末端；而 {@code insert} 方法则在指定的点添加字符。
 * <p>
 * 例如，如果 {@code z} 引用一个当前内容为 "{@code start}" 的字符串缓冲区对象，
 * 则该方法调用 {@code z.append("le")} 将使字符串缓冲区包含 "{@code startle}"，
 * 而 {@code z.insert(4, "le")} 将更改字符串缓冲区，使之包含 "{@code startle}"。
 * <p>
 * 通常，如果 sb 引用 {@code StringBuffer} 的实例，
 * 则 {@code sb.append(x)} 和 {@code sb.insert(sb.length(), x)} 具有相同的效果。
 * <p>
 * 当发生与源序列有关的操作（如源序列中的追加或插入操作）时，
 * 该类只在执行此操作的字符串缓冲区上而不是在源上实现同步。
 * 请注意，虽然 {@code StringBuffer} 被设计为可以安全地从多个线程同时使用，但是如果构造函数或者
 * {@code append} 或者 {@code insert} 操作传递了跨线程共享的源序列，
 * 则调用代码必须确保操作在期间具有一致且不变的源序列视图。
 * 这可以通过在操作调用期间持有锁的调用者，通过使用不可变的源序列，或者不跨线程共享源序列来实现。
 * <p>
 * 每个字符串缓冲区都有一定的容量。
 * 只要字符串缓冲区所包含的字符序列的长度没有超出此容量，就无需分配新的内部缓冲区数组。
 * 如果内部缓冲区溢出，则此容量自动增大。
 * <p>
 * 除非另有说明，否则将 {@code null} 参数传递给构造函数或此类的方法将抛出 {@link NullPointerException}。
 * <p>
 * 从 JDK 5 开始，为该类补充了一个单个线程使用的等价类，即 {@link StringBuilder}。
 * 与该类相比，通常应该优先使用 {@link StringBuilder} 类，
 * 因为它支持所有相同的操作，但由于它不执行同步，所以速度更快。
 *
 * @author      Arthur van Hoff
 * @see     java.lang.StringBuilder
 * @see     java.lang.String
 * @since   JDK1.0
 */
 public final class StringBuffer
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
{

    /**
     * toString 返回的最后一个值的缓存。 每当 StringBuffer 被修改时清空。
     */
    private transient char[] toStringCache;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    static final long serialVersionUID = 3388685877147921107L;

    /**
     * 构造一个其中不带字符的字符串缓冲区，其初始容量为 16 个字符。
     */
    public StringBuffer() {
        super(16);
    }

    /**
     * 构造一个不带字符，但具有指定初始容量的字符串缓冲区。
     *
     * @param      capacity  初始容量。
     * @exception  NegativeArraySizeException  如果 {@code capacity} 参数小于 {@code 0}。
     */
    public StringBuffer(int capacity) {
        super(capacity);
    }

    /**
     * 构造一个字符串缓冲区，并将其内容初始化为指定的字符串内容。
     * 该字符串的初始容量为 {@code 16} 加上字符串参数的长度。
     *
     * @param   str   缓冲区的初始内容。
     */
    public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
    }

    /**
     * 构造一个字符串缓冲区，它包含与指定的 {@code CharSequence} 相同的字符。
     * 该字符串缓冲区的初始容量为 16 加上 {@code CharSequence} 参数的长度。
     * <p>
     * 如果指定的 {@code CharSequence} 的长度小于或等于 0，则返回容量为 {@code 16} 的空缓冲区。
     *
     * @param      seq   要复制的序列。
     * @since 1.5
     */
    public StringBuffer(CharSequence seq) {
        this(seq.length() + 16);
        append(seq);
    }

    @Override
    public synchronized int length() {
        return count;
    }

    @Override
    public synchronized int capacity() {
        return value.length;
    }


    @Override
    public synchronized void ensureCapacity(int minimumCapacity) {
        super.ensureCapacity(minimumCapacity);
    }

    /**
     * @since      1.5
     */
    @Override
    public synchronized void trimToSize() {
        super.trimToSize();
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @see        #length()
     */
    @Override
    public synchronized void setLength(int newLength) {
        toStringCache = null;
        super.setLength(newLength);
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @see        #length()
     */
    @Override
    public synchronized char charAt(int index) {
        if ((index < 0) || (index >= count))
            throw new StringIndexOutOfBoundsException(index);
        return value[index];
    }

    /**
     * @since      1.5
     */
    @Override
    public synchronized int codePointAt(int index) {
        return super.codePointAt(index);
    }

    /**
     * @since     1.5
     */
    @Override
    public synchronized int codePointBefore(int index) {
        return super.codePointBefore(index);
    }

    /**
     * @since     1.5
     */
    @Override
    public synchronized int codePointCount(int beginIndex, int endIndex) {
        return super.codePointCount(beginIndex, endIndex);
    }

    /**
     * @since     1.5
     */
    @Override
    public synchronized int offsetByCodePoints(int index, int codePointOffset) {
        return super.offsetByCodePoints(index, codePointOffset);
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public synchronized void getChars(int srcBegin, int srcEnd, char[] dst,
                                      int dstBegin)
    {
        super.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @see        #length()
     */
    @Override
    public synchronized void setCharAt(int index, char ch) {
        if ((index < 0) || (index >= count))
            throw new StringIndexOutOfBoundsException(index);
        toStringCache = null;
        value[index] = ch;
    }

    @Override
    public synchronized StringBuffer append(Object obj) {
        toStringCache = null;
        super.append(String.valueOf(obj));
        return this;
    }

    @Override
    public synchronized StringBuffer append(String str) {
        toStringCache = null;
        super.append(str);
        return this;
    }

    /**
     * 将指定的 {@code StringBuffer} 追加到此序列中。
     * <p>
     * 按顺序将 {@code StringBuffer} 参数中的字符追加到此 {@code StringBuffer} 中，
     * 并使 {@code StringBuffer} 在长度上增加该参数的长度。
     * 如果 {@code sb} 为 {@code null}，则将 4 个 {@code "null"} 字符追加到此 {@code StringBuffer} 中。
     * <p>
     * 在执行 {@code append} 方法前，让 {@code StringBuffer} 中包含的原有字符序列的长度为 <i>n</i>。
     * 如果 <i>k</i> 小于 <i>n</i>，则新字符序列中索引 <i>k</i> 处的字符等于原有序列中索引 <i>k</i> 处的字符；
     * 否则它等于参数 {@code sb} 中索引 <i>k-n</i> 处的字符。
     * <p>
     * 该方法在 {@code this}（目标）对象上实现同步，但不在源（{@code sb}）上实现同步。
     *
     * @param   sb   要追加的 {@code StringBuffer}。
     * @return  该对象的一个引用。
     * @since 1.4
     */
    public synchronized StringBuffer append(StringBuffer sb) {
        toStringCache = null;
        super.append(sb);
        return this;
    }

    /**
     * @since 1.8
     */
    @Override
    synchronized StringBuffer append(AbstractStringBuilder asb) {
        toStringCache = null;
        super.append(asb);
        return this;
    }

    /**
     * 将指定的 {@code CharSequence} 追加到该序列。
     * <p>
     * 按顺序将 {@code CharSequence} 参数中的字符追加到该序列中，使该序列增加该参数的长度。
     *
     * <p>该方法的结果与调用 this.append(s, 0, s.length()) 的结果完全相同；
     *
     * <p>该方法在 {@code this}（目标）对象上实现同步，但不在源（{@code s}）上实现同步。
     *
     * <p>如果 {@code s} 为 {@code null}，则追加 4 个 {@code "null"} 字符。
     *
     * @param   s 要追加的 {@code CharSequence}。
     * @return  此对象的一个引用。
     * @since 1.5
     */
    @Override
    public synchronized StringBuffer append(CharSequence s) {
        toStringCache = null;
        super.append(s);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @since      1.5
     */
    @Override
    public synchronized StringBuffer append(CharSequence s, int start, int end)
    {
        toStringCache = null;
        super.append(s, start, end);
        return this;
    }

    @Override
    public synchronized StringBuffer append(char[] str) {
        toStringCache = null;
        super.append(str);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public synchronized StringBuffer append(char[] str, int offset, int len) {
        toStringCache = null;
        super.append(str, offset, len);
        return this;
    }

    @Override
    public synchronized StringBuffer append(boolean b) {
        toStringCache = null;
        super.append(b);
        return this;
    }

    @Override
    public synchronized StringBuffer append(char c) {
        toStringCache = null;
        super.append(c);
        return this;
    }

    @Override
    public synchronized StringBuffer append(int i) {
        toStringCache = null;
        super.append(i);
        return this;
    }

    /**
     * @since 1.5
     */
    @Override
    public synchronized StringBuffer appendCodePoint(int codePoint) {
        toStringCache = null;
        super.appendCodePoint(codePoint);
        return this;
    }

    @Override
    public synchronized StringBuffer append(long lng) {
        toStringCache = null;
        super.append(lng);
        return this;
    }

    @Override
    public synchronized StringBuffer append(float f) {
        toStringCache = null;
        super.append(f);
        return this;
    }

    @Override
    public synchronized StringBuffer append(double d) {
        toStringCache = null;
        super.append(d);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     * @since      1.2
     */
    @Override
    public synchronized StringBuffer delete(int start, int end) {
        toStringCache = null;
        super.delete(start, end);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     * @since      1.2
     */
    @Override
    public synchronized StringBuffer deleteCharAt(int index) {
        toStringCache = null;
        super.deleteCharAt(index);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     * @since      1.2
     */
    @Override
    public synchronized StringBuffer replace(int start, int end, String str) {
        toStringCache = null;
        super.replace(start, end, str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     * @since      1.2
     */
    @Override
    public synchronized String substring(int start) {
        return substring(start, count);
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @since      1.4
     */
    @Override
    public synchronized CharSequence subSequence(int start, int end) {
        return super.substring(start, end);
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     * @since      1.2
     */
    @Override
    public synchronized String substring(int start, int end) {
        return super.substring(start, end);
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     * @since      1.2
     */
    @Override
    public synchronized StringBuffer insert(int index, char[] str, int offset,
                                            int len)
    {
        toStringCache = null;
        super.insert(index, str, offset, len);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public synchronized StringBuffer insert(int offset, Object obj) {
        toStringCache = null;
        super.insert(offset, String.valueOf(obj));
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public synchronized StringBuffer insert(int offset, String str) {
        toStringCache = null;
        super.insert(offset, str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public synchronized StringBuffer insert(int offset, char[] str) {
        toStringCache = null;
        super.insert(offset, str);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @since      1.5
     */
    @Override
    public StringBuffer insert(int dstOffset, CharSequence s) {
        // Note, synchronization achieved via invocations of other StringBuffer methods
        // after narrowing of s to specific type
        // Ditto for toStringCache clearing
        super.insert(dstOffset, s);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @since      1.5
     */
    @Override
    public synchronized StringBuffer insert(int dstOffset, CharSequence s,
            int start, int end)
    {
        toStringCache = null;
        super.insert(dstOffset, s, start, end);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public  StringBuffer insert(int offset, boolean b) {
        // Note, synchronization achieved via invocation of StringBuffer insert(int, String)
        // after conversion of b to String by super class method
        // Ditto for toStringCache clearing
        super.insert(offset, b);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public synchronized StringBuffer insert(int offset, char c) {
        toStringCache = null;
        super.insert(offset, c);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuffer insert(int offset, int i) {
        // Note, synchronization achieved via invocation of StringBuffer insert(int, String)
        // after conversion of i to String by super class method
        // Ditto for toStringCache clearing
        super.insert(offset, i);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuffer insert(int offset, long l) {
        // Note, synchronization achieved via invocation of StringBuffer insert(int, String)
        // after conversion of l to String by super class method
        // Ditto for toStringCache clearing
        super.insert(offset, l);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuffer insert(int offset, float f) {
        // Note, synchronization achieved via invocation of StringBuffer insert(int, String)
        // after conversion of f to String by super class method
        // Ditto for toStringCache clearing
        super.insert(offset, f);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuffer insert(int offset, double d) {
        // Note, synchronization achieved via invocation of StringBuffer insert(int, String)
        // after conversion of d to String by super class method
        // Ditto for toStringCache clearing
        super.insert(offset, d);
        return this;
    }

    /**
     * @since      1.4
     */
    @Override
    public int indexOf(String str) {
        // Note, synchronization achieved via invocations of other StringBuffer methods
        return super.indexOf(str);
    }

    /**
     * @since      1.4
     */
    @Override
    public synchronized int indexOf(String str, int fromIndex) {
        return super.indexOf(str, fromIndex);
    }

    /**
     * @since      1.4
     */
    @Override
    public int lastIndexOf(String str) {
        // Note, synchronization achieved via invocations of other StringBuffer methods
        return lastIndexOf(str, count);
    }

    /**
     * @since      1.4
     */
    @Override
    public synchronized int lastIndexOf(String str, int fromIndex) {
        return super.lastIndexOf(str, fromIndex);
    }

    /**
     * @since   JDK1.0.2
     */
    @Override
    public synchronized StringBuffer reverse() {
        toStringCache = null;
        super.reverse();
        return this;
    }

    @Override
    public synchronized String toString() {
        if (toStringCache == null) {
            toStringCache = Arrays.copyOfRange(value, 0, count);
        }
        return new String(toStringCache, true);
    }

    /**
     * StringBuffer的可序列化字段。
     *
     * @serialField value  char[]
     *              StringBuffer 的后备字符数组。
     * @serialField count int
     *              StringBuffer 的字符数。
     * @serialField shared  boolean
     *              后背数组是否共享的标志位。反序列化时忽略该值。
     */
    private static final java.io.ObjectStreamField[] serialPersistentFields =
    {
        new java.io.ObjectStreamField("value", char[].class),
        new java.io.ObjectStreamField("count", Integer.TYPE),
        new java.io.ObjectStreamField("shared", Boolean.TYPE),
    };

    /**
     * 调用readObject从流恢复StringBuffer的状态。
     */
    private synchronized void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        java.io.ObjectOutputStream.PutField fields = s.putFields();
        fields.put("value", value);
        fields.put("count", count);
        fields.put("shared", false);
        s.writeFields();
    }

    /**
     * 调用readObject从流恢复StringBuffer的状态。
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        java.io.ObjectInputStream.GetField fields = s.readFields();
        value = (char[])fields.get("value", null);
        count = fields.get("count", 0);
    }
}
