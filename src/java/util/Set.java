/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.util;

/**
 * 一个不包含重复元素的 collection。
 * 更确切地讲，set 不包含满足 <code>e1.equals(e2)</code> 的元素对 <code>e1</code> 和 <code>e2</code>，并且最多包含一个 null 元素。
 * 正如其名称所暗示的，此接口模仿了数学上的 <i>set</i> 抽象。
 *
 * <p>在所有构造方法以及 <tt>add</tt>、<tt>equals</tt> 和 <tt>hashCode</tt> 方法的协定上，
 * Set 接口还加入了其他规定，这些规定超出了从 <tt>Collection</tt> 接口所继承的内容。
 * 出于方便考虑，它还包括了其他继承方法的声明（这些声明的规范已经专门针对 <tt>Set</tt> 接口进行了修改，
 * 但是没有包含任何其他的规定）。
 *
 * <p>对这些构造方法的其他规定是（不要奇怪），
 * 所有构造方法必须创建一个不包含重复元素的 set（正如上面所定义的）。
 *
 * <p>注：如果将可变对象用作 set 元素，那么必须极其小心。
 * 如果对象是 set 中某个元素，以一种影响 <tt>equals</tt> 比较的方式改变对象的值，
 * 那么 set 的行为就是不确定的。此项禁止的一个特殊情况是不允许某个 set 包含其自身作为元素。
 *
 * <p>某些 set 实现对其所包含的元素有所限制。例如，某些实现禁止 null 元素，
 * 而某些则对其元素的类型所有限制。试图添加不合格的元素会抛出未经检查的异常，
 * 通常是 <tt>NullPointerException</tt> 或 <tt>ClassCastException</tt>。
 * 试图查询不合格的元素是否存在可能会抛出异常，也可能简单地返回 false；
 * 某些实现会采用前一种行为，而某些则采用后者。
 * 概括地说，试图对不合格元素执行操作时，
 * 如果完成该操作后不会导致在 set 中插入不合格的元素，则该操作可能抛出一个异常，
 * 也可能成功，这取决于实现的选择。此接口的规范中将这样的异常标记为“可选”。
 *
 * <p>此接口是
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的成员。
 *
 * @param <E> 此 set 所维护元素的类型
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @see List
 * @see SortedSet
 * @see HashSet
 * @see TreeSet
 * @see AbstractSet
 * @see Collections#singleton(java.lang.Object)
 * @see Collections#EMPTY_SET
 * @since 1.2
 */

public interface Set<E> extends Collection<E> {
    // Query Operations

    /**
     * 返回 set 中的元素数（其容量）。如果 set 包含多个 <tt>Integer.MAX_VALUE</tt> 元素，
     * 则返回 <tt>Integer.MAX_VALUE</tt>。
     *
     * @return 此 set 中的元素数（其容量）
     */
    int size();

    /**
     * 如果 set 不包含元素，则返回 <tt>true</tt>。
     *
     * @return 如果 set 不包含元素，则返回 <tt>true</tt>
     */
    boolean isEmpty();

    /**
     * 如果 set 包含指定的元素，则返回 <tt>true</tt>。
     * 更确切地讲，当且仅当 set 包含满足
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>
     * 的元素 <tt>e</tt> 时返回 <tt>true</tt>。
     *
     * @param o 要测试此 set 中是否存在的元素
     * @return 如果此 set 包含指定的元素，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定元素的类型与此 set 不兼容
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果指定的元素为 null 并且此 set 不允许 null 元素
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     */
    boolean contains(Object o);

    /**
     * 返回在此 set 中的元素上进行迭代的迭代器。
     * 返回的元素没有特定的顺序（除非此 set 是某个提供顺序保证的类的实例）。
     *
     * @return 在此 set 中的元素上进行迭代的迭代器
     */
    Iterator<E> iterator();

    /**
     * 返回一个包含 set 中所有元素的数组。
     * 如果此 set 对其迭代器返回的元素的顺序作出了某些保证，那么此方法也必须按相同的顺序返回这些元素。
     *
     * <p>由于此 set 不维护对返回数组的任何引用，因而它是安全的。
     * （换句话说，即使此 set 受到数组的支持，此方法也必须分配一个新的数组）。
     * 因此，调用者可以随意修改返回的数组。
     *
     * <p>此方法充当基于数组的 API 与基于 collection 的 API 之间的桥梁。
     *
     * @return 包含此 set 中所有元素的数组
     */
    Object[] toArray();

    /**
     * 返回一个包含此 set 中所有元素的数组；
     * 返回数组的运行时类型是指定数组的类型。
     * 如果指定的数组能容纳该 set，则它将在其中返回。
     * 否则，将分配一个具有指定数组的运行时类型和此 set 大小的新数组。
     *
     * <p>如果指定的数组能容纳此 set，并有剩余的空间（即该数组的元素比此 set 多），
     * 那么会将列表中紧接该 set 尾部的元素设置为 <tt>null</tt>。
     * （<i>只有</i> 在调用者知道此 set 不包含任何 null 元素时才能用此方法确定此 set 的长度）。
     *
     * <p>如果此 set 对其迭代器返回的元素的顺序作出了某些保证，那么此方法也必须按相同的顺序返回这些元素。
     *
     * <p>像 {@link #toArray()} 方法一样，此方法充当基于数组的 API 与基于 collection 的 API 之间的桥梁。
     * 更进一步说，此方法允许对输出数组的运行时类型上进行精确控制，
     * 在某些情况下，可以用来节省分配开销。
     *
     * <p>假定 <tt>x</tt> 是只包含字符串的一个已知 set。
     * 以下代码用来将该 set 转储到一个新分配的 <tt>String</tt> 数组：
     * array of <tt>String</tt>:
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * 注意， <tt>toArray(new Object[0])</tt> 和 <tt>toArray()</tt> 在功能上是相同的。
     *
     * @param a 存储此 set 中元素的数组（如果其足够大）；否则将为此分配一个具有相同运行时类型的新数组。
     * @return 包含此 set 中所有元素的数组
     * @throws ArrayStoreException 如果指定数组的运行时类型不是此 set 中所有元素的运行时类型的超类型
     * @throws NullPointerException 如果指定的数组为 null
     */
    <T> T[] toArray(T[] a);


    // Modification Operations

    /**
     * 如果 set 中尚未存在指定的元素，则添加此元素（可选操作）。
     * 更确切地讲，如果此 set 没有包含满足
     * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>
     * 的元素 <tt>e2</tt>，则向该 set 中添加指定的元素 <tt>e</tt>。
     * 如果此 set 已经包含该元素，则该调用不改变此 set 并返回 <tt>false</tt>。
     * 结合构造方法上的限制，这就可以确保 set 永远不包含重复的元素。
     *
     * <p>上述规定并未暗示 set 必须接受所有元素；
     * set 可以拒绝添加任意特定的元素，包括 <tt>null</tt>，并抛出异常，
     * 这与 {@link Collection#add Collection.add} 规范中所描述的一样。
     * 每个 set 实现应该明确地记录对其可能包含元素的所有限制。
     *
     * @param e 要添加到 set 中的元素
     * @return 如果 set 尚未包含指定的元素，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果此 set 不支持 <tt>add</tt> 操作
     * @throws ClassCastException 如果指定元素的类不允许它添加到此 set
     * @throws NullPointerException 如果指定的元素为 null 并且此 set 不允许 null 元素
     * @throws IllegalArgumentException 如果指定元素的某些属性不允许它添加到此 set
     */
    boolean add(E e);


    /**
     * 如果 set 中存在指定的元素，则将其移除（可选操作）。
     * 更确切地讲，如果此 set 中包含满足
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>
     * 的元素 <tt>e</tt>，则移除它。
     * 如果此 set 包含指定的元素（或者此 set 由于调用而发生更改），则返回 <tt>true</tt>
     * （一旦调用返回，则此 set 不再包含指定的元素）。
     *
     * @param o 从 set 中移除的对象（如果存在）
     * @return 如果此 set 包含指定的对象，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定元素的类型与此 set 不兼容
     * (<a href="Collection.html#optional-restrictions">可选l</a>)
     * @throws NullPointerException 如果指定的元素为 null，并且此 set 不允许 null 元素
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws UnsupportedOperationException 如果此 set 不支持 <tt>remove</tt> 操作
     */
    boolean remove(Object o);


    // Bulk Operations

    /**
     * 如果此 set 包含指定 collection 的所有元素，则返回 <tt>true</tt>。
     * 如果指定的 collection 也是一个 set，
     * 那么当该 collection 是此 set 的 <i>子集</i> 时返回 <tt>true</tt>。
     *
     * @param  c 检查是否包含在此 set 中的 collection
     * @return 如果此 set 包含指定 collection 中的所有元素，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定 collection 中的一个或多个元素的类型与此 set 不兼容
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException  如果指定的 collection 包含一个或多个 null 元素并且此 set 不允许 null 元素
     * (<a href="Collection.html#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see    #contains(Object)
     */
    boolean containsAll(Collection<?> c);

    /**
     * 如果 set 中没有指定 collection 中的所有元素，则将其添加到此 set 中（可选操作）。
     * 如果指定的 collection 也是一个 set，则 <tt>addAll</tt> 操作会实际修改此 set，
     * 这样其值是两个 set 的一个 <i>并集</i>。如果操作正在进行的同时修改了指定的 collection，
     * 则此操作的行为是不确定的。
     *
     * @param  c 包含要添加到此 set 中的元素的 collection
     * @return 如果此 set 由于调用而发生更改，则返回 <tt>true</tt>
     *
     * @throws UnsupportedOperationException 如果 set 不支持 <tt>addAll</tt> 操作
     * @throws ClassCastException 如果某些指定 collection 元素的类不允许它添加到此 set
     * @throws NullPointerException 如果指定的 collection 包含一个或多个 null 元素并且此 set 不允许 null 元素，
     *          或者指定的 collection 为 null
     * @throws IllegalArgumentException 如果指定 collection 元素的某些属性不允许它添加到此 set
     * @see #add(Object)
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * 仅保留 set 中那些包含在指定 collection 中的元素（可选操作）。
     * 换句话说，移除此 set 中所有未包含在指定 collection 中的元素。
     * 如果指定的 collection 也是一个 set，则此操作会实际修改此 set，
     * 这样其值是两个 set 的一个 <i>交集</i>。
     *
     * @param  c 包含要保留到此 set 中的元素的 collection
     * @return 如果此 set 由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果此 set 不支持 <tt>retainAll</tt> 操作
     * @throws ClassCastException 如果此 set 元素的类与指定的 collection 不兼容
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果此 set 包含 null 元素并且指定的 collection 不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see #remove(Object)
     */
    boolean retainAll(Collection<?> c);

    /**
     * 移除 set 中那些包含在指定 collection 中的元素（可选操作）。
     * 如果指定的 collection 也是一个 set，则此操作会实际修改此 set，
     * 这样其值是两个 set 的一个 <i>不对称差集</i>。
     *
     * @param  c 包含要从此 set 中移除的元素的 collection
     * @return 如果此 set 由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果此 set 不支持 <tt>removeAll</tt> 操作
     * @throws ClassCastException 如果此 set 元素的类与指定的 collection 不兼容
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果此 set 包含 null 元素并且指定的 collection 不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean removeAll(Collection<?> c);

    /**
     * 移除此 set 中的所有元素（可选操作）。此调用返回后该 set 将是空的。
     *
     * @throws UnsupportedOperationException 如果此 set 不支持 <tt>clear</tt> 方法
     */
    void clear();


    // Comparison and hashing

    /**
     * 比较指定对象与此 set 的相等性。
     * 如果指定的对象也是一个 set，两个 set 的大小相同，
     * 并且指定 set 的所有成员都包含在此 set 中（或者，此 set 的所有成员都包含在指定的 set 中也一样），
     * 则返回 <tt>true</tt>。此定义确保了 equals 方法可在不同的 set 接口实现间正常工作。
     *
     * @param o 要与此 set 进行相等性比较的对象
     * @return 如果指定的对象等于此 set，则返回 <tt>true</tt>
     */
    boolean equals(Object o);

    /**
     * 返回 set 的哈希码值。
     * 一个 set 的哈希码定义为此 set 中所有元素的哈希码和，
     * 其中 <tt>null</tt> 元素的哈希码定义为零。
     * 这就确保对于任意两个 set <tt>s1</tt> 和 <tt>s2</tt> 而言，
     * <tt>s1.equals(s2)</tt>  就意味着 s1.hashCode()==s2.hashCode()，
     * 正如 Object.hashCode() 的常规协定所要求的那样。
     * {@link Object#hashCode}.
     *
     * @return 此 set 的哈希码值
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    int hashCode();

    /**
     * Creates a {@code Spliterator} over the elements in this set.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#DISTINCT}.
     * Implementations should document the reporting of additional
     * characteristic values.
     *
     * @implSpec
     * The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
     * from the set's {@code Iterator}.  The spliterator inherits the
     * <em>fail-fast</em> properties of the set's iterator.
     * <p>
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SIZED}.
     *
     * @implNote
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     *
     * @return a {@code Spliterator} over the elements in this set
     * @since 1.8
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT);
    }
}
