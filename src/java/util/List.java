/*
 * Copyright (c) 1997, 2014, Oracle and/or its affiliates. All rights reserved.
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

import java.util.function.UnaryOperator;

/**
 * 有序的 collection（也称为<i>序列</i>）。
 * 此接口的用户可以对列表中每个元素的插入位置进行精确地控制。
 * 用户可以根据元素的整数索引（在列表中的位置）访问元素，并搜索列表中的元素。<p>
 *
 * 与 set 不同，列表通常允许重复的元素。
 * 更确切地讲，列表通常允许满足 <tt>e1.equals(e2)</tt> 的元素
 * 对 <tt>e1</tt> 和 <tt>e2</tt>，
 * 并且如果列表本身允许 null 元素的话，通常它们允许多个 null 元素。
 * 难免有人希望通过在用户尝试插入重复元素时抛出运行时异常的方法来禁止重复的列表，
 * 但我们希望这种用法越少越好。<p>
 *
 * <tt>List</tt> 接口在 <tt>iterator</tt>、 <tt>add</tt>、
 * <tt>remove</tt>、 <tt>equals</tt> 和 <tt>hashCode</tt> 方法的协定上加了一些其他约定，
 * 超过了 <tt>Collection</tt> 接口中指定的约定。为方便起见，这里也包括了其他继承方法的声明。<p>
 *
 * <tt>List</tt> 接口提供了 4 种对列表元素进行定位（索引）访问方法。
 * 列表（像 Java 数组一样）是基于 0 的。
 * 注意，这些操作可能在和某些实现（例如 <tt>LinkedList</tt> 类）的索引值成比例的时间内执行。
 * 因此，如果调用者不知道实现，那么在列表元素上迭代通常优于用索引遍历列表。<p>
 *
 * <tt>List</tt> 接口提供了特殊的迭代器，称为 <tt>ListIterator</tt>，
 * 除了允许 <tt>Iterator</tt> 接口提供的正常操作外，该迭代器还允许元素插入和替换，以及双向访问。
 * 还提供了一个方法来获取从列表中指定位置开始的列表迭代器。<p>
 *
 * <tt>List</tt> 接口提供了两种搜索指定对象的方法。从性能的观点来看，应该小心使用这些方法。
 * 在很多实现中，它们将执行高开销的线性搜索。<p>
 *
 * <tt>List</tt> 接口提供了两种在列表的任意位置高效插入和移除多个元素的方法。<p>
 *
 * 注意：尽管列表允许把自身作为元素包含在内，但建议要特别小心：
 * 在这样的列表上，<tt>equals</tt> 和 <tt>hashCode</tt> 方法不再是定义良好的。<p>
 *
 * 某些列表实现对列表可能包含的元素有限制。例如，某些实现禁止 null 元素，
 * 而某些实现则对元素的类型有限制。试图添加不合格的元素会抛出未经检查的异常，
 * 通常是 <tt>NullPointerException</tt> 或 <tt>ClassCastException</tt>。
 * 试图查询不合格的元素是否存在可能会抛出异常，也可能简单地返回 false；
 * 某些实现会采用前一种行为，而某些则采用后者。
 * 概括地说，试图对不合格元素执行操作时，如果完成该操作后不会导致在列表中插入不合格的元素，
 * 则该操作可能抛出一个异常，也可能成功，这取决于实现的选择。
 * 此接口的规范中将这样的异常标记为“可选”。
 *
 * <p>T此接口是
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的成员。
 *
 * @param <E> 此list的元素类型
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @see Set
 * @see ArrayList
 * @see LinkedList
 * @see Vector
 * @see Arrays#asList(Object[])
 * @see Collections#nCopies(int, Object)
 * @see Collections#EMPTY_LIST
 * @see AbstractList
 * @see AbstractSequentialList
 * @since 1.2
 */

public interface List<E> extends Collection<E> {
    // Query Operations

    /**
     * 返回列表中的元素数。如果列表包含多于 <tt>Integer.MAX_VALUE</tt> 个元素，
     * 则返回 <tt>Integer.MAX_VALUE</tt>。
     *
     * @return 列表中的元素数
     */
    int size();

    /**
     * 如果列表不包含元素，则返回 <tt>true</tt>。
     *
     * @return 如果列表不包含元素，则返回 <tt>true</tt>
     */
    boolean isEmpty();

    /**
     * 如果列表包含指定的元素，则返回 <tt>true</tt>。
     * 更确切地讲，
     * 当且仅当列表包含满足 <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>
     * 的元素 <tt>e</tt> 时才返回 <tt>true</tt>。
     *
     * @param o 要测试列表中是否存在的元素
     * @return 如果列表包含指定的元素，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定元素的类型和此列表不兼容
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果指定的元素为 null，并且此列表不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     */
    boolean contains(Object o);

    /**
     * 返回按适当顺序在列表的元素上进行迭代的迭代器。
     *
     * @return 按适当顺序在列表的元素上进行迭代的迭代器
     */
    Iterator<E> iterator();

    /**
     * 返回按适当顺序包含列表中的所有元素的数组（从第一个元素到最后一个元素）。
     *
     * <p>由于此列表不维护对返回数组的任何引用，因而它将是“安全的”。
     * （换句话说，即使数组支持此列表，此方法也必须分配一个新数组）。
     * 因此，调用者可以随意修改返回的数组。
     *
     * <p>此方法充当基于数组的 API 与基于 collection 的 API 之间的桥梁。
     *
     * @return 按适当顺序包含该列表中所有元素的数组
     * @see Arrays#asList(Object[])
     */
    Object[] toArray();

    /**
     * 返回按适当顺序（从第一个元素到最后一个元素）包含列表中所有元素的数组；
     * 返回数组的运行时类型是指定数组的运行时类型。
     * 如果指定数组能容纳列表，则在其中返回该列表。
     * 否则，分配具有指定数组的运行时类型和此列表大小的新数组。
     *
     * <p>如果指定数组能容纳列表，并剩余空间（即数组的元素比列表的多），
     * 那么会将数组中紧随列表尾部的元素设置为 null。
     * （只有 在调用者知道列表不包含任何 null 元素时此方法才能用于确定列表的长度）。
     *
     * <p>像 {@link #toArray()} 方法一样，
     * 此方法充当基于数组的 API 与基于 collection 的 API 之间的桥梁。
     * 更进一步说，此方法允许对输出数组的运行时类型进行精确控制，
     * 在某些情况下，可以用来节省分配开销。
     *
     * <p>假定 <tt>x</tt> 是只包含字符串的一个已知列表。
     * 以下代码用来将该列表转储到一个新分配的 <tt>String</tt> 数组：
     *
     * <pre>{@code
     *     String[] y = x.toArray(new String[0]);
     * }</pre>
     *
     * 注意， <tt>toArray(new Object[0])</tt> 和 <tt>toArray()</tt> 在功能上是相同的。
     *
     * @param a 要存储列表中元素的数组，如果它足够大的话；否则为此目的分配一个运行时类型相同的新数组。
     * @return 包含列表中元素的数组
     * @throws ArrayStoreException 如果指定数组的运行时类型不是此列表中每个元素的运行时类型的超类型
     * @throws NullPointerException 如果指定数组为 null
     */
    <T> T[] toArray(T[] a);


    // Modification Operations

    /**
     * 向列表的尾部添加指定的元素（可选操作）。
     *
     * <p>支持该操作的列表可能对列表可以添加的元素有一些限制。
     * 特别是某些列表将拒绝添加 null 元素，其他列表将在可能添加的元素类型上施加限制。
     * List 类应该在它们的文档中明确指定有关添加元素的所有限制。
     *
     * @param e 要添加到列表的元素
     * @return （根据 {@link Collection#add} 的规定）<tt>true</tt>
     * @throws UnsupportedOperationException 如果列表不支持 <tt>add</tt> 操作
     * @throws ClassCastException 如果指定元素的类不允许它添加到此列表
     * @throws NullPointerException 如果指定的元素为 null，并且此列表不允许 null 元素
     * @throws IllegalArgumentException 如果此元素的某些属性不允许它添加到此列表
     */
    boolean add(E e);

    /**
     * 从此列表中移除第一次出现的指定元素（如果存在）（可选操作）。
     * 如果列表不包含元素，则不更改列表。
     * 更确切地讲，移除满足
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最低索引 <tt>i</tt> 的元素（如果存在这样的元素）。
     * 如果此列表已包含指定元素（或者此列表由于调用而发生更改），则返回 <tt>true</tt>。
     *
     * @param o 要从该列表中移除的元素，如果存在的话
     * @return 如果列表包含指定的元素，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定元素的类型和此列表不兼容
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果指定的元素是 null，并且此列表不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException 如果列表不支持 <tt>remove</tt> 操作
     */
    boolean remove(Object o);


    // Bulk Modification Operations

    /**
     * 如果列表包含指定 collection 的所有元素，则返回 <tt>true</tt>。
     *
     * @param  c 要在列表中检查其包含性的 collection
     * @return 如果列表包含指定 collection 的所有元素，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定 collection 中的一个或多个元素的类型和此列表不兼容
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果指定的 collection 包含一个或多个 null 元素，
     *         并且此列表不允许 null 元素 (<a href="Collection.html#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     *
     * @see #contains(Object)
     */
    boolean containsAll(Collection<?> c);

    /**
     * 添加指定 collection 中的所有元素到此列表的结尾，
     * 顺序是指定 collection 的迭代器返回这些元素的顺序（可选操作）。
     * 如果在操作正在进行中修改了指定的 collection，
     * 那么此操作的行为是不确定的（注意，如果指定的 collection 是此列表，
     * 并且它是非空的，则会发生这种情况。）
     *
     * @param c 包含要添加到此列表的元素的 collection
     * @return 如果此列表由于调用而发生更改，则返回<tt>true</tt>
     * @throws UnsupportedOperationException 如果列表不支持 <tt>addAll</tt> 操作
     * @throws ClassCastException 如果指定 collection 的元素的类不允许它添加到此列表
     * @throws NullPointerException 如果指定的 collection 包含一个或多个 null 元素，并且该列表不允许 null 元素，
     *         或者指定的 collection 为 null
     * @throws IllegalArgumentException 如果指定 collection 的元素的某些属性不允许它添加此列表
     * @see #add(Object)
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * 将指定 collection 中的所有元素都插入到列表中的指定位置（可选操作）。
     * 将当前处于该位置的元素（如果有的话）和所有后续元素向右移动（增加其索引）。
     * 新元素将按照它们通过指定 collection 的迭代器所返回的顺序出现在此列表中。
     * 如果在操作正在进行中修改了指定的 collection，那么该操作的行为是不确定的
     * （注意，如果指定的 collection 是此列表，并且它是非空的，则会发生这种情况。）
     *
     * @param index 将指定 collection 的第一个元素所插入位置的索引
     * @param c 包含要添加到此列表的元素的 collection
     * @return 如果此列表由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果列表不支持 <tt>addAll</tt> 操作
     * @throws ClassCastException  如果指定 collection 中某个元素的类不允许它添加到此列表
     * @throws NullPointerException 如果指定的 collection 包含一个或多个 null 元素，
     *         并且该列表不允许 null 元素，或者指定的 collection 为 null
     * @throws IllegalArgumentException 如果指定 collection 的元素的某些属性不允许它添加到此列表
     * @throws IndexOutOfBoundsException 如果索引超出范围
     *         (<tt>index &lt; 0 || index &gt; size()</tt>)
     */
    boolean addAll(int index, Collection<? extends E> c);

    /**
     * 从列表中移除指定 collection 中包含的其所有元素（可选操作）。
     *
     * @param c 包含从此列表中移除的元素的 collection
     * @return 如果此列表由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果列表不支持 <tt>removeAll</tt> 操作
     * @throws ClassCastException 如果此列表中的元素的类和指定的 collection 不兼容
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果此列表包含一个 null 元素，
     *         并且指定的 collection 不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean removeAll(Collection<?> c);

    /**
     * 仅在列表中保留指定 collection 中所包含的元素（可选操作）。
     * 换句话说，该方法从列表中移除未包含在指定 collection 中的所有元素。
     *
     * @param c 包含将保留在此列表中的元素的 collection
     * @return 如果此列表由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果列表不支持 <tt>retainAll</tt> 操作
     * @throws ClassCastException 如果此列表的元素的类和指定的 collection 不兼容
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果此列表包含一个 null 元素，
     *         并且指定的 collection 不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean retainAll(Collection<?> c);

    /**
     * Replaces each element of this list with the result of applying the
     * operator to that element.  Errors or runtime exceptions thrown by
     * the operator are relayed to the caller.
     *
     * @implSpec
     * The default implementation is equivalent to, for this {@code list}:
     * <pre>{@code
     *     final ListIterator<E> li = list.listIterator();
     *     while (li.hasNext()) {
     *         li.set(operator.apply(li.next()));
     *     }
     * }</pre>
     *
     * If the list's list-iterator does not support the {@code set} operation
     * then an {@code UnsupportedOperationException} will be thrown when
     * replacing the first element.
     *
     * @param operator the operator to apply to each element
     * @throws UnsupportedOperationException if this list is unmodifiable.
     *         Implementations may throw this exception if an element
     *         cannot be replaced or if, in general, modification is not
     *         supported
     * @throws NullPointerException if the specified operator is null or
     *         if the operator result is a null value and this list does
     *         not permit null elements
     *         (<a href="Collection.html#optional-restrictions">optional</a>)
     * @since 1.8
     */
    default void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final ListIterator<E> li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.next()));
        }
    }

    /**
     * Sorts this list according to the order induced by the specified
     * {@link Comparator}.
     *
     * <p>All elements in this list must be <i>mutually comparable</i> using the
     * specified comparator (that is, {@code c.compare(e1, e2)} must not throw
     * a {@code ClassCastException} for any elements {@code e1} and {@code e2}
     * in the list).
     *
     * <p>If the specified comparator is {@code null} then all elements in this
     * list must implement the {@link Comparable} interface and the elements'
     * {@linkplain Comparable natural ordering} should be used.
     *
     * <p>This list must be modifiable, but need not be resizable.
     *
     * @implSpec
     * The default implementation obtains an array containing all elements in
     * this list, sorts the array, and iterates over this list resetting each
     * element from the corresponding position in the array. (This avoids the
     * n<sup>2</sup> log(n) performance that would result from attempting
     * to sort a linked list in place.)
     *
     * @implNote
     * This implementation is a stable, adaptive, iterative mergesort that
     * requires far fewer than n lg(n) comparisons when the input array is
     * partially sorted, while offering the performance of a traditional
     * mergesort when the input array is randomly ordered.  If the input array
     * is nearly sorted, the implementation requires approximately n
     * comparisons.  Temporary storage requirements vary from a small constant
     * for nearly sorted input arrays to n/2 object references for randomly
     * ordered input arrays.
     *
     * <p>The implementation takes equal advantage of ascending and
     * descending order in its input array, and can take advantage of
     * ascending and descending order in different parts of the same
     * input array.  It is well-suited to merging two or more sorted arrays:
     * simply concatenate the arrays and sort the resulting array.
     *
     * <p>The implementation was adapted from Tim Peters's list sort for Python
     * (<a href="http://svn.python.org/projects/python/trunk/Objects/listsort.txt">
     * TimSort</a>).  It uses techniques from Peter McIlroy's "Optimistic
     * Sorting and Information Theoretic Complexity", in Proceedings of the
     * Fourth Annual ACM-SIAM Symposium on Discrete Algorithms, pp 467-474,
     * January 1993.
     *
     * @param c the {@code Comparator} used to compare list elements.
     *          A {@code null} value indicates that the elements'
     *          {@linkplain Comparable natural ordering} should be used
     * @throws ClassCastException if the list contains elements that are not
     *         <i>mutually comparable</i> using the specified comparator
     * @throws UnsupportedOperationException if the list's list-iterator does
     *         not support the {@code set} operation
     * @throws IllegalArgumentException
     *         (<a href="Collection.html#optional-restrictions">optional</a>)
     *         if the comparator is found to violate the {@link Comparator}
     *         contract
     * @since 1.8
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    default void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
        ListIterator<E> i = this.listIterator();
        for (Object e : a) {
            i.next();
            i.set((E) e);
        }
    }

    /**
     * 从列表中移除所有元素（可选操作）。此调用返回后该列表将是空的。
     *
     * @throws UnsupportedOperationException 如果列表不支持 <tt>clear</tt> 操作
     */
    void clear();


    // Comparison and hashing

    /**
     * 比较指定的对象与列表是否相等。
     * 当且仅当指定的对象也是一个列表、两个列表有相同的大小，
     * 并且两个列表中的所有相应的元素对 <i>相等</i> 时才返回 <tt>true</tt>
     * （ 如果 <tt>(e1==null ? e2==null : e1.equals(e2))</tt>)，
     * 则两个元素 <tt>e1</tt> 和 <tt>e2</tt> 是 <i>相等</i> 的）。
     * 换句话说，如果所定义的两个列表以相同的顺序包含相同的元素，那么它们是相等的。
     * 该定义确保了 equals 方法在 <tt>List</tt> 接口的不同实现间正常工作。
     *
     * @param o 要与此列表进行相等性比较的对象
     * @return 如果指定对象与此列表相等，则返回 <tt>true</tt>
     */
    boolean equals(Object o);

    /**
     * 返回列表的哈希码值。列表的哈希码定义为以下计算的结果：
     * <pre>{@code
     *     int hashCode = 1;
     *     for (E e : list)
     *         hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
     * }</pre>
     * 这确保了 <tt>list1.equals(list2)</tt> 意味着
     * 对于任何两个列表 <tt>list1</tt> 和 <tt>list2</tt> 而言，
     * 可实现 <tt>list1.hashCode()==list2.hashCode()</tt>，
     * 正如 {@link Object#hashCode} 的常规协定所要求的。
     *
     * @return 此列表的哈希码值
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    int hashCode();


    // Positional Access Operations

    /**
     * 返回列表中指定位置的元素。
     *
     * @param index 要返回的元素的索引
     * @return 列表中指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    E get(int index);

    /**
     * 用指定元素替换列表中指定位置的元素（可选操作）。
     *
     * @param index 要替换的元素的索引
     * @param element 要在指定位置存储的元素
     * @return 以前在指定位置的元素
     * @throws UnsupportedOperationException 如果列表不支持 <tt>set</tt> 操作
     * @throws ClassCastException 如果指定元素的类不允许它添加到此列表
     * @throws NullPointerException 如果指定的元素为 null，并且此列表不允许 null 元素
     * @throws IllegalArgumentException 如果指定元素的某些属性不允许它添加到此列表
     * @throws IndexOutOfBoundsException 如果索引超出范围
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    E set(int index, E element);

    /**
     * 在列表的指定位置插入指定元素（可选操作）。
     * 将当前处于该位置的元素（如果有的话）和所有后续元素向右移动（在其索引中加 1）。
     *
     * @param index 要在其中插入指定元素处的索引
     * @param element 要插入的元素
     * @throws UnsupportedOperationException 如果列表不支持 <tt>add</tt> 操作
     * @throws ClassCastException 如果指定元素的类不允许它添加到此列表
     * @throws NullPointerException 如果指定的元素为 null，并且此列表不允许 null 元素
     * @throws IllegalArgumentException 如果指定元素的某些属性不允许它添加到此列表
     * @throws IndexOutOfBoundsException 如果索引超出范围
     *         (<tt>index &lt; 0 || index &gt; size()</tt>)
     */
    void add(int index, E element);

    /**
     * 移除列表中指定位置的元素（可选操作）。
     * 将所有的后续元素向左移动（将其索引减 1）。返回从列表中移除的元素。
     *
     * @param index 要移除的元素的索引
     * @return 以前在指定位置的元素
     * @throws UnsupportedOperationException 如果列表不支持 <tt>remove</tt> 操作
     * @throws IndexOutOfBoundsException 如果索引超出范围
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    E remove(int index);


    // Search Operations

    /**
     * 返回此列表中第一次出现的指定元素的索引；
     * 如果此列表不包含该元素，则返回 -1。
     * 更确切地讲，返回满足
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最低索引 <tt>i</tt>；
     * 如果没有这样的索引，则返回 -1。
     *
     * @param o 要搜索的元素
     * @return 此列表中第一次出现的指定元素的索引，如果列表不包含该元素，则返回 -1
     * @throws ClassCastException 如果指定元素的类型和此列表不兼容（）
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException  如果指定的元素是 null，并且此列表不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选l</a>)
     */
    int indexOf(Object o);

    /**
     * 返回此列表中最后出现的指定元素的索引；
     * 如果列表不包含此元素，则返回 -1。
     * 更确切地讲，返回满足
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最高索引 <tt>i</tt>；
     * 如果没有这样的索引，则返回 -1。
     *
     * @param o 要搜索的元素
     * @return 列表中最后出现的指定元素的索引；如果列表不包含此元素，则返回 -1
     * @throws ClassCastException 如果指定元素的类型和此列表不兼容
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果指定的元素是 null，并且此列表不允许 null 元素
     *         (<a href="Collection.html#optional-restrictions">可选</a>)
     */
    int lastIndexOf(Object o);


    // List Iterators

    /**
     * 返回此列表元素的列表迭代器（按适当顺序）。
     *
     * @return 此列表元素的列表迭代器（按适当顺序）
     */
    ListIterator<E> listIterator();

    /**
     * 返回列表中元素的列表迭代器（按适当顺序），从列表的指定位置开始。
     * 指定的索引表示 {@link ListIterator#next next} 的初始调用所返回的第一个元素。
     * {@link ListIterator#previous previous} 方法的初始调用将返回索引比指定索引少 1 的元素。
     *
     * @param index 从列表迭代器返回的第一个元素的索引（通过调用 {@link ListIterator#next next} 方法）
     * @return 此列表中元素的列表迭代器（按适当顺序），从列表中的指定位置开始
     * @throws IndexOutOfBoundsException 如果索引超出范围
     *         ({@code index < 0 || index > size()})
     */
    ListIterator<E> listIterator(int index);

    // View

    /**
     * 返回列表中指定的 <tt>fromIndex</tt>（包括 ）和 <tt>toIndex</tt>（不包括）之间的部分视图。
     * （如果 <tt>fromIndex</tt> 和 <tt>toIndex</tt> 相等，则返回的列表为空）。
     * 返回的列表由此列表支持，因此返回列表中的非结构性更改将反映在此列表中，反之亦然。
     * 返回的列表支持此列表支持的所有可选列表操作。<p>
     *
     * 此方法省去了显式范围操作（此操作通常针对数组存在）。
     * 通过传递 subList 视图而非整个列表，期望列表的任何操作可用作范围操作。
     * 例如，下面的语句从列表中移除了元素的范围：
     * <pre>{@code
     *      list.subList(from, to).clear();
     * }</pre>
     * 可以对 <tt>indexOf</tt> 和 <tt>lastIndexOf</tt> 构造类似的语句，
     * 而且 <tt>Collections</tt> 类中的所有算法都可以应用于 subList。<p>
     *
     * 如果支持列表（即此列表）通过任何其他方式（而不是通过返回的列表）<i>从结构上修改</i>，
     * 则此方法返回的列表语义将变为未定义
     * （从结构上修改是指更改列表的大小，或者以其他方式打乱列表，使正在进行的迭代产生错误的结果）。
     *
     * @param fromIndex subList 的低端（包括）
     * @param toIndex subList 的高端（不包括）
     * @return 列表中指定范围的视图
     * @throws IndexOutOfBoundsException 非法的端点值
     *         (<tt>fromIndex &lt; 0 || toIndex &gt; size ||
     *         fromIndex &gt; toIndex</tt>)
     */
    List<E> subList(int fromIndex, int toIndex);

    /**
     * Creates a {@link Spliterator} over the elements in this list.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
     * {@link Spliterator#ORDERED}.  Implementations should document the
     * reporting of additional characteristic values.
     *
     * @implSpec
     * The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
     * from the list's {@code Iterator}.  The spliterator inherits the
     * <em>fail-fast</em> properties of the list's iterator.
     *
     * @implNote
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     *
     * @return a {@code Spliterator} over the elements in this list
     * @since 1.8
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED);
    }
}
