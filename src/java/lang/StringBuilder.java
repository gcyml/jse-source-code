/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
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
 * 一个可变的字符序列。
 * 此类提供一个与 {@code StringBuffer} 兼容的 API，但不保证同步。
 * 该类被设计用作 {@code StringBuffer} 的一个简易替换，用在字符串缓冲区被单个线程使用的时候（这种情况很普遍）。
 * 如果可能，建议优先采用该类，因为在大多数实现中，它比 {@code StringBuffer} 要快。
 *
 * <p>在 {@code StringBuilder} 上的主要操作是 {@code append} 和 {@code insert} 方法，
 * 可重载这些方法，以接受任意类型的数据。
 * 每个方法都能有效地将给定的数据转换成字符串，然后将该字符串的字符追加或插入到字符串生成器中。
 * {@code append} 方法始终将这些字符添加到生成器的末端；而 {@code insert} 方法则在指定的点添加字符。
 * <p>
 * 例如，如果 {@code z} 引用一个当前内容为 "{@code start}" 的字符串的生成器对象，
 * 则该方法调用 {@code z.append("le")} 将使字符串生成器包含 "{@code startle}"，
 * 而 {@code z.insert(4, "le")} 将更改字符串生成器，使之包含 "{@code startle}"。
 * <p>
 * 通常，如果 sb 引用 {@code StringBuilder} 的实例，
 * 则 {@code sb.append(x)} 和 {@code sb.insert(sb.length(), x)} 具有相同的效果。
 * <p>
 * 每个字符串生成器都有一定的容量。只要字符串生成器所包含的字符序列的长度没有超出此容量，
 * 就无需分配新的内部缓冲区。如果内部缓冲区溢出，则此容量自动增大。
 *
 * <p>将 {@code StringBuilder} 的实例用于多个线程是不安全的。
 * 如果需要这样的同步，则建议使用 {@link java.lang.StringBuffer}。
 *
 * <p>除非另有说明，否则将 {@code null} 参数传递给构造函数或此类的方法将抛出 {@link NullPointerException}。
 *
 * @author      Michael McCloskey
 * @see         java.lang.StringBuffer
 * @see         java.lang.String
 * @since       1.5
 */
public final class StringBuilder
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
{

    /** use serialVersionUID for interoperability */
    static final long serialVersionUID = 4383685877147921099L;

    /**
     * 构造一个不带任何字符的字符串生成器，其初始容量为 16 个字符。
     */
    public StringBuilder() {
        super(16);
    }

    /**
     * 构造一个不带任何字符的字符串生成器，其初始容量由 {@code capacity} 参数指定。
     *
     * @param      capacity  初始容量。
     * @throws     NegativeArraySizeException  如果 {@code capacity} 参数小于 {@code 0}。
     */
    public StringBuilder(int capacity) {
        super(capacity);
    }

    /**
     * 构造一个字符串生成器，并初始化为指定的字符串内容。
     * 该字符串生成器的初始容量为 {@code 16} 加上字符串参数的长度。
     *
     * @param   str   缓冲区的初始内容。
     */
    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }

    /**
     * 构造一个字符串生成器，包含与指定的 {@code CharSequence} 相同的字符。
     * 该字符串生成器的初始容量为 {@code 16} 加上 {@code CharSequence} 参数的长度。
     *
     * @param      seq    要复制的序列。
     */
    public StringBuilder(CharSequence seq) {
        this(seq.length() + 16);
        append(seq);
    }

    @Override
    public StringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }

    @Override
    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }

    /**
     * 将指定的 {@code StringBuffer} 追加到此序列。
     * <p>
     * 按顺序追加 {@code StringBuffer} 参数中的字符，此序列将增加该参数的长度。
     * 如果 {@code sb} 为 {@code null}，则向该序列中追加 4 个 {@code "null"} 字符。
     * <p>
     * 在执行 {@code append} 方法前，让此字符序列的长度为 <i>n</i>。
     * 如果 <i>k</i> 小于 <i>n</i>，则新字符序列中索引 <i>k</i> 处的字符等于原有序列中索引 <i>k</i> 处的字符；
     * 否则它等于参数 {@code sb} 中索引 <i>k-n</i> 处的字符。
     *
     * @param   sb   要追加的 {@code StringBuffer}。
     * @return  此对象的一个引用。
     */
    public StringBuilder append(StringBuffer sb) {
        super.append(sb);
        return this;
    }

    @Override
    public StringBuilder append(CharSequence s) {
        super.append(s);
        return this;
    }

    /**
     * @throws     IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder append(CharSequence s, int start, int end) {
        super.append(s, start, end);
        return this;
    }

    @Override
    public StringBuilder append(char[] str) {
        super.append(str);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder append(char[] str, int offset, int len) {
        super.append(str, offset, len);
        return this;
    }

    @Override
    public StringBuilder append(boolean b) {
        super.append(b);
        return this;
    }

    @Override
    public StringBuilder append(char c) {
        super.append(c);
        return this;
    }

    @Override
    public StringBuilder append(int i) {
        super.append(i);
        return this;
    }

    @Override
    public StringBuilder append(long lng) {
        super.append(lng);
        return this;
    }

    @Override
    public StringBuilder append(float f) {
        super.append(f);
        return this;
    }

    @Override
    public StringBuilder append(double d) {
        super.append(d);
        return this;
    }

    /**
     * @since 1.5
     */
    @Override
    public StringBuilder appendCodePoint(int codePoint) {
        super.appendCodePoint(codePoint);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder delete(int start, int end) {
        super.delete(start, end);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder deleteCharAt(int index) {
        super.deleteCharAt(index);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder replace(int start, int end, String str) {
        super.replace(start, end, str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int index, char[] str, int offset,
                                int len)
    {
        super.insert(index, str, offset, len);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, Object obj) {
            super.insert(offset, obj);
            return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, String str) {
        super.insert(offset, str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, char[] str) {
        super.insert(offset, str);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int dstOffset, CharSequence s) {
            super.insert(dstOffset, s);
            return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int dstOffset, CharSequence s,
                                int start, int end)
    {
        super.insert(dstOffset, s, start, end);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, boolean b) {
        super.insert(offset, b);
        return this;
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, char c) {
        super.insert(offset, c);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, int i) {
        super.insert(offset, i);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, long l) {
        super.insert(offset, l);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, float f) {
        super.insert(offset, f);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public StringBuilder insert(int offset, double d) {
        super.insert(offset, d);
        return this;
    }

    @Override
    public int indexOf(String str) {
        return super.indexOf(str);
    }

    @Override
    public int indexOf(String str, int fromIndex) {
        return super.indexOf(str, fromIndex);
    }

    @Override
    public int lastIndexOf(String str) {
        return super.lastIndexOf(str);
    }

    @Override
    public int lastIndexOf(String str, int fromIndex) {
        return super.lastIndexOf(str, fromIndex);
    }

    @Override
    public StringBuilder reverse() {
        super.reverse();
        return this;
    }

    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }

    /**
     * 将{@code StringBuilder}实例的状态保存到流中（即序列化)。
     *
     * @serialData string builder中当前存储的字符数({@code int})，
     *             在string builder的字符集后面 ({@code char[]})。
     *             {@code char} 数组的长度可能大于当前存储在string builder中的字符数，
     *             在这种情况下会忽略额外的字符。
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        s.defaultWriteObject();
        s.writeInt(count);
        s.writeObject(value);
    }

    /**
     * 调用readObject从流恢复StringBuffer的状态
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        count = s.readInt();
        value = (char[]) s.readObject();
    }

}
