/*
 * Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * <tt>CharSequence</tt> 是 <code>char</code> 值的一个可读序列。
 * 此接口对许多不同种类的 <code>char</code> 序列提供统一的只读访问。
 * <code>char</code> 值表示 <i>Basic Multilingual Plane (BMP)</i> 或代理项中的一个字符。
 * 有关详细信息，请参阅 <a href="Character.html#unicode">Unicode 字符表示形式</a>。
 *
 * <p> 此接口不修改 {@link java.lang.Object#equals(java.lang.Object) equals} 和
 * {@link java.lang.Object#hashCode() hashCode} 方法的常规协定。
 * 因此，通常未定义比较实现 <tt>CharSequence</tt> 的两个对象的结果。
 * 每个对象都可以通过一个不同的类实现，而且不能保证每个类能够测试其实例与其他类的实例的相等性。
 * 因此，使用任意 <tt>CharSequence</tt> 实例作为集合中的元素或映射中的键是不合适的。</p>
 *
 * @author Mike McCloskey
 * @since 1.4
 * @spec JSR-51
 */

public interface CharSequence {

    /**
     * 返回此字符序列的长度。该长度是序列中的 16 位 <code>char</code> 数。
     *
     * @return  此序列中的 <code>char</code> 数
     */
    int length();

    /**
     * 返回指定索引的 <code>char</code> 值。索引范围是从零到 <tt>length() - 1</tt>。
     * 对于数组索引，序列的第一个 <code>char</code> 值是索引零，下一个是索引一，依此类推。
     *
     * <p>如果索引指定的 <code>char</code> 值是<a href="{@docRoot}/java/lang/Character.html#unicode">代理项</a>，
     * 则返回代理项的值。
     *
     * @param   index   要返回的 <code>char</code> 值的索引
     *
     * @return  指定的 <code>char</code> 值
     *
     * @throws  IndexOutOfBoundsException
     *           如果 <tt>index</tt> 参数为负或不小于 <tt>length()</tt>
     */
    char charAt(int index);

    /**
     * 返回一个新的 <code>CharSequence</code>，它是此序列的子序列。
     * 子序列从指定序列的 <code>char</code> 值开始，并在索引 <tt>end - 1</tt> 的 <code>char</code> 值结束。
     * 返回序列的长度（ <code>char</code>s 中）是 <tt>end - start</tt>，
     * 因此，如果 <tt>start == end</tt>，则返回一个空序列。
     *
     * @param   start   开始索引（包括）
     * @param   end     结束索引（不包括）
     *
     * @return  指定的子序列
     *
     * @throws  IndexOutOfBoundsException
     *          如果 <tt>start</tt> 或 <tt>end</tt> 为负，
     *          <tt>end</tt> 大于 length() 或者 <tt>start</tt> 大于 <tt>end</tt>
     */
    CharSequence subSequence(int start, int end);

    /**
     * 返回一个包含此序列中字符的字符串，该字符串与此序列的顺序相同。字符串的长度就是此序列的长度。
     *
     * @return  一个完全由此序列的字符组成的字符串
     */
    public String toString();

    /**
     * Returns a stream of {@code int} zero-extending the {@code char} values
     * from this sequence.  Any char which maps to a <a
     * href="{@docRoot}/java/lang/Character.html#unicode">surrogate code
     * point</a> is passed through uninterpreted.
     *
     * <p>If the sequence is mutated while the stream is being read, the
     * result is undefined.
     *
     * @return an IntStream of char values from this sequence
     * @since 1.8
     */
    public default IntStream chars() {
        class CharIterator implements PrimitiveIterator.OfInt {
            int cur = 0;

            public boolean hasNext() {
                return cur < length();
            }

            public int nextInt() {
                if (hasNext()) {
                    return charAt(cur++);
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void forEachRemaining(IntConsumer block) {
                for (; cur < length(); cur++) {
                    block.accept(charAt(cur));
                }
            }
        }

        return StreamSupport.intStream(() ->
                Spliterators.spliterator(
                        new CharIterator(),
                        length(),
                        Spliterator.ORDERED),
                Spliterator.SUBSIZED | Spliterator.SIZED | Spliterator.ORDERED,
                false);
    }

    /**
     * Returns a stream of code point values from this sequence.  Any surrogate
     * pairs encountered in the sequence are combined as if by {@linkplain
     * Character#toCodePoint Character.toCodePoint} and the result is passed
     * to the stream. Any other code units, including ordinary BMP characters,
     * unpaired surrogates, and undefined code units, are zero-extended to
     * {@code int} values which are then passed to the stream.
     *
     * <p>If the sequence is mutated while the stream is being read, the result
     * is undefined.
     *
     * @return an IntStream of Unicode code points from this sequence
     * @since 1.8
     */
    public default IntStream codePoints() {
        class CodePointIterator implements PrimitiveIterator.OfInt {
            int cur = 0;

            @Override
            public void forEachRemaining(IntConsumer block) {
                final int length = length();
                int i = cur;
                try {
                    while (i < length) {
                        char c1 = charAt(i++);
                        if (!Character.isHighSurrogate(c1) || i >= length) {
                            block.accept(c1);
                        } else {
                            char c2 = charAt(i);
                            if (Character.isLowSurrogate(c2)) {
                                i++;
                                block.accept(Character.toCodePoint(c1, c2));
                            } else {
                                block.accept(c1);
                            }
                        }
                    }
                } finally {
                    cur = i;
                }
            }

            public boolean hasNext() {
                return cur < length();
            }

            public int nextInt() {
                final int length = length();

                if (cur >= length) {
                    throw new NoSuchElementException();
                }
                char c1 = charAt(cur++);
                if (Character.isHighSurrogate(c1) && cur < length) {
                    char c2 = charAt(cur);
                    if (Character.isLowSurrogate(c2)) {
                        cur++;
                        return Character.toCodePoint(c1, c2);
                    }
                }
                return c1;
            }
        }

        return StreamSupport.intStream(() ->
                Spliterators.spliteratorUnknownSize(
                        new CodePointIterator(),
                        Spliterator.ORDERED),
                Spliterator.ORDERED,
                false);
    }
}
