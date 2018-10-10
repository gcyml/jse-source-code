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

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <i>Collection 层次结构</i> 中的根接口。
 * Collection 表示一组对象，这些对象也称为 collection 的<i>元素</i>。
 * 一些 collection 允许有重复的元素，而另一些则不允许。
 * 一些 collection 是有序的，而另一些则是无序的。
 * JDK 不提供此接口的任何<i>直接</i>实现：
 * 它提供更具体的子接口（如 <tt>Set</tt> 和 <tt>List</tt>）实现。
 * 此接口通常用来传递 collection，并在需要最大普遍性的地方操作这些 collection。
 *
 * <p><i>包 (bag) </i> 或<i>多集合 (multiset)</i>
 * （可能包含重复元素的无序 collection）应该直接实现此接口。
 *
 * <p>所有通用的 <tt>Collection</tt> 实现类
 * （通常通过它的一个子接口间接实现 <tt>Collection</tt>）应该提供两个“标准”构造方法：
 * 一个是 void（无参数）构造方法，用于创建空 collection；
 * 另一个是带有<tt>Collection</tt> 类型单参数的构造方法，
 * 用于创建一个具有与其参数相同元素新的 collection。
 * 实际上，后者允许用户复制任何 collection，以生成所需实现类型的一个等效 collection。
 * 尽管无法强制执行此约定（因为接口不能包含构造方法），
 * 但是 Java 平台库中所有通用的 <tt>Collection</tt> 实现都遵从它。
 *
 * <p>此接口中包含的“破坏性”方法，是指可修改其所操作的 collection 的那些方法，
 * 如果此 collection 不支持该操作，
 * 则指定这些方法抛出 <tt>UnsupportedOperationException</tt>。
 * 如果是这样，那么在调用对该 collection 无效时，这些方法可能，但并不一定抛出
 * <tt>UnsupportedOperationException</tt>。
 * 例如，如果要添加的 collection 为空且不可修改，
 * 则对该 collection 调用 {@link #addAll(Collection)} 方法时，
 * 可能但并不一定抛出异常。
 *
 * <p><a name="optional-restrictions">
 * 一些 collection 实现对它们可能包含的元素有所限制。</a>
 * 例如，某些实现禁止 null 元素，而某些实现则对元素的类型有限制。
 * 试图添加不合格的元素将抛出一个未经检查的异常，
 * 通常是 <tt>NullPointerException</tt> 或 <tt>ClassCastException</tt>。
 * 试图查询是否存在不合格的元素可能抛出一个异常，或者只是简单地返回 false；
 * 某些实现将表现出前一种行为，而某些实现则表现后一种。
 * 较为常见的是，试图对某个不合格的元素执行操作且
 * 该操作的完成不会导致将不合格的元素插入 collection 中，
 * 将可能抛出一个异常，也可能操作成功，这取决于实现本身。
 * 这样的异常在此接口的规范中标记为“可选”。
 *
 * <p>由每个 collection 来确定其自身的同步策略。
 * 在没有实现的强烈保证的情况下，
 * 调用由另一进程正在更改的 collection 的方法可能会出现不确定行为；
 * 这包括直接调用，将 collection 传递给可能执行调用的方法，
 * 以及使用现有迭代器检查 collection。
 *
 * <p>Collections Framework 接口中的很多方法是
 * 根据 {@link Object#equals(Object) equals} 方法定义的。
 * 例如，{@link #contains(Object) contains(Object o)} 方法的规范声明：
 * “当且仅当此 collection 包含至少一个满足
 * <tt>(o==null ? e==null : o.equals(e))</tt> 的元素 <tt>e</tt> 时，
 * 返回 <tt>true</tt>。”<i>不</i>应将此规范理解为它暗指
 * 调用具有非空参数 <tt>o</tt> 的 <tt>Collection.contains</tt> 方法会导致
 * 为任意的 <tt>e</tt> 元素调用 <tt>o.equals(e)</tt> 方法。
 * 可随意对各种实现执行优化，只要避免调用 <tt>equals</tt> 即可，
 * 例如，通过首先比较两个元素的哈希码。
 * （{@link Object#hashCode()} 规范保证哈希码不相等的两个对象不会相等）。
 * 较为常见的是，各种 Collections Framework 接口的实现
 * 可随意利用底层 {@link Object} 方法的指定行为，而不管实现程序认为它是否合适。
 *
 * <p>Some collection operations which perform recursive traversal of the
 * collection may fail with an exception for self-referential instances where
 * the collection directly or indirectly contains itself. This includes the
 * {@code clone()}, {@code equals()}, {@code hashCode()} and {@code toString()}
 * methods. Implementations may optionally handle the self-referential scenario,
 * however most current implementations do not do so.
 *
 * <p>此接口是
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的一个成员。
 *
 * @implSpec
 * The default method implementations (inherited or otherwise) do not apply any
 * synchronization protocol.  If a {@code Collection} implementation has a
 * specific synchronization protocol, then it must override default
 * implementations to apply that protocol.
 *
 * @param <E> the type of elements in this collection
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Set
 * @see     List
 * @see     Map
 * @see     SortedSet
 * @see     SortedMap
 * @see     HashSet
 * @see     TreeSet
 * @see     ArrayList
 * @see     LinkedList
 * @see     Vector
 * @see     Collections
 * @see     Arrays
 * @see     AbstractCollection
 * @since 1.2
 */

public interface Collection<E> extends Iterable<E> {
    // Query Operations

    /**
     * 返回此 collection 中的元素数。
     * 如果此 collection 包含的元素大于 <tt>Integer.MAX_VALUE</tt>，
     * 则返回 <tt>Integer.MAX_VALUE</tt>。
     *
     * @return 此 collection 中的元素数
     */
    int size();

    /**
     * 如果此 collection 不包含元素，则返回 <tt>true</tt>。
     *
     * @return 如果此 collection 不包含元素，则返回 <tt>true</tt>
     */
    boolean isEmpty();

    /**
     * 如果此 collection 包含指定的元素，则返回 <tt>true</tt>。
     * 更确切地讲，当且仅当此 collection 至少包含一个满足
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>
     * 的元素 <tt>e</tt> 时，返回 <tt>true</tt>。
     *
     * @param o 测试在此 collection 中是否存在的元素
     * @return 如果此 collection 包含指定的元素，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定元素的类型与此 collection 不兼容
     *         （<a href="#optional-restrictions">可选</a>）。
     * @throws NullPointerException 如果指定的元素为 null，
     *         并且此 collection 不允许 null 元素
     *         （<a href="#optional-restrictions">可选</a>）
     */
    boolean contains(Object o);

    /**
     * 返回在此 collection 的元素上进行迭代的迭代器。
     * 关于元素返回的顺序没有任何保证
     * （除非此 collection 是某个能提供保证顺序的类实例）。
     *
     * @return 在此 collection 的元素上进行迭代的 <tt>Iterator</tt>
     */
    Iterator<E> iterator();

    /**
     * 返回包含此 collection 中所有元素的数组。
     * 如果 collection 对其迭代器返回的元素顺序做出了某些保证，
     * 那么此方法必须以相同的顺序返回这些元素。
     *
     * <p>返回的数组将是“安全的”，因为此 collection 并不维护对返回数组的任何引用。
     * （换句话说，即使 collection 受到数组的支持，此方法也必须分配一个新的数组）。
     * 因此，调用者可以随意修改返回的数组。
     *
     * <p>此方法充当了基于数组的 API 与基于 collection 的 API 之间的桥梁。
     *
     * @return 包含此 collection 中所有元素的数组
     */
    Object[] toArray();

    /**
     * 返回包含此 collection 中所有元素的数组；
     * 返回数组的运行时类型与指定数组的运行时类型相同。
     * 如果指定的数组能容纳该 collection，则返回包含此 collection 元素的数组。
     * 否则，将分配一个具有指定数组的运行时类型和此 collection 大小的新数组。
     *
     * <p>如果指定的数组能容纳 collection，
     * 并有剩余空间（即数组的元素比 collection 的元素多），
     * 那么会将数组中紧接 collection 尾部的元素设置为 <tt>null</tt>。
     * （<i>只有</i>在调用者知道此 collection 没有包含任何 <tt>null</tt> 元素时
     * 才能用此方法确定 collection 的长度。）
     *
     * <p>如果此 collection 对其迭代器返回的元素顺序做出了某些保证，
     * 那么此方法必须以相同的顺序返回这些元素。
     *
     * <p>像 {@link #toArray()} 方法一样，
     * 此方法充当基于数组的 API 与基于 collection 的 API 之间的桥梁。
     * 更进一步说，此方法允许对输出数组的运行时类型进行精确控制，
     * 并且在某些情况下，可以用来节省分配开销。
     *
     * <p>假定 <tt>x</tt> 是只包含字符串的一个已知 collection。
     * 以下代码用来将 collection 转储到一个新分配的 <tt>String</tt> 数组：
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * 注意， <tt>toArray(new Object[0])</tt>
     * 和 <tt>toArray()</tt> 在功能上是相同的。
     *
     * @param <T> the runtime type of the array to contain the collection
     * @param a 存储此 collection 元素的数组（如果其足够大）；
     *        否则，将为此分配一个具有相同运行时类型的新数组。
     * @return 包含此 collection 中所有元素的数组
     * @throws ArrayStoreException 如果指定数组的运行时类型不是
     *         此 collection 每个元素运行时类型的超类型
     * @throws NullPointerException 如果指定的数组为 null
     */
    <T> T[] toArray(T[] a);

    // Modification Operations

    /**
     * 确保此 collection 包含指定的元素（可选操作）。
     * 如果此 collection 由于调用而发生更改，则返回 <tt>true</tt>。
     * （如果此 collection 不允许有重复元素，并且已经包含了指定的元素，
     * 则返回 <tt>false</tt>。）<p>
     *
     * 支持此操作的 collection 可以限制哪些元素能添加到此 collection 中来。
     * 需要特别指出的是，一些 collection 拒绝添加 <tt>null</tt> 元素，
     * 其他一些 collection 将对可以添加的元素类型强加限制。
     * Collection 类应该在其文档中清楚地指定能添加哪些元素方面的所有限制。<p>
     *
     * 如果 collection 由于某些原因（已经包含该元素的原因除外）拒绝添加特定的元素，
     * 那么它<i>必须</i>抛出一个异常（而不是返回 <tt>false</tt>）。
     * 这确保了在此调用返回后，collection 总是包含指定的元素。
     *
     * @param e 确定此 collection 中是否存在的元素
     * @return 如果此 collection 由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果此 collection 不支持 <tt>add</tt> 操作
     * @throws ClassCastException 果指定元素的类不允许它添加到此 collection 中
     * @throws NullPointerException 如果指定的元素为 null，并且此 collection 不允许 null 元素
     * @throws IllegalArgumentException 如果元素的某属性不允许它添加到此 collection 中
     * @throws IllegalStateException 如果由于插入限制，元素不能在此时间添加
     */
    boolean add(E e);

    /**
     * 从此 collection 中移除指定元素的单个实例，如果存在的话（可选操作）。
     * 更确切地讲，如果此 collection 包含一个或多个满足
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt> 的元素 <tt>e</tt>，
     * 则移除这样的元素。如果此 collection 包含指定的元素（或者此 collection 由于调用而发生更改），
     * 则返回 <tt>true</tt> 。
     *
     * @param o 要从此 collection 中移除的元素（如果存在）。
     * @return 如果此调用将移除一个元素，则返回<tt>true</tt>
     * @throws ClassCastException 如果指定元素的类型与此 collection 不兼容
     *         (<a href="#optional-restrictions">可选</a>)。
     * @throws NullPointerException 如果指定的元素为 null，并且此 collection 不允许 null 元素
     *         (<a href="#optional-restrictions">可选</a>)
     * @throws UnsupportedOperationException 如果此 collection 不支持 <tt>remove</tt> 操作
     */
    boolean remove(Object o);


    // Bulk Operations

    /**
     * 如果此 collection 包含指定 collection 中的所有元素，则返回 <tt>true</tt>。
     *
     * @param  c 将检查是否包含在此 collection 中的 collection
     * @return 如果此 collection 包含指定 collection 中的所有元素，则返回 <tt>true</tt>
     * @throws ClassCastException 如果指定 collection 中有一个或多个元素的类型与此 collection 不兼容
     *         (<a href="#optional-restrictions">可选</a>)。
     * @throws NullPointerException 如果指定 collection 包含一个或多个 null 元素，
     *         并且此 collection 不允许 null 元素(<a href="#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see    #contains(Object)
     */
    boolean containsAll(Collection<?> c);

    /**
     * 将指定 collection 中的所有元素都添加到此 collection 中（可选操作）。
     * 如果在进行此操作的同时修改指定的 collection，那么此操作行为是不确定的。
     * （这意味着如果指定的 collection 是此 collection，并且此 collection 为非空，
     * 那么此调用的行为是不确定的。）
     *
     * @param c 包含要添加到此 collection 的元素的 collection
     * @return 如果此 collection 由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果此 collection 不支持 <tt>addAll</tt> 方法
     * @throws ClassCastException 如果指定 collection 中某个元素的类不允许它添加到此 collection 中
     * @throws NullPointerException 如果指定 collection 包含 null 元素，
     *         并且此 collection 不支持 null 元素，或者指定的 collection 为 null
     * @throws IllegalArgumentException 如果指定 collection 的元素的某属性不允许它添加到此 collection 中
     * @throws IllegalStateException 如果由于插入限制，不是所有的元素都能在此时间添加
     * @see #add(Object)
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * 移除此 collection 中那些也包含在指定 collection 中的所有元素（可选操作）。
     * 此调用返回后，collection 中将不包含任何与指定 collection 相同的元素。
     *
     * @param c 要从此 collection 移除的元素的 collection
     * @return 如果此 collection 由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果此 collection 不支持 <tt>removeAll</tt> 方法
     * @throws ClassCastException 如果此 collection 中一个或多个元素的类型与指定 collection 不兼容
     *         (<a href="#optional-restrictions">可选</a>)。
     * @throws NullPointerException 如果此 collection 包含一个或多个 null 元素，
     *         并且指定的 collection 不支持 null 元素 (<a href="#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean removeAll(Collection<?> c);

    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate.  Errors or runtime exceptions thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @implSpec
     * The default implementation traverses all elements of the collection using
     * its {@link #iterator}.  Each matching element is removed using
     * {@link Iterator#remove()}.  If the collection's iterator does not
     * support removal then an {@code UnsupportedOperationException} will be
     * thrown on the first matching element.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if elements cannot be removed
     *         from this collection.  Implementations may throw this exception if a
     *         matching element cannot be removed or if, in general, removal is not
     *         supported.
     * @since 1.8
     */
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * 仅保留此 collection 中那些也包含在指定 collection 的元素（可选操作）。
     * 换句话说，移除此 collection 中未包含在指定 collection 中的所有元素。
     *
     * @param c  包含保留在此 collection 中的元素的 collection
     * @return 如果此 collection 由于调用而发生更改，则返回 <tt>true</tt>
     * @throws UnsupportedOperationException 如果此 collection 不支持 <tt>retainAll</tt> 操作
     * @throws ClassCastException 如果此 collection 中一个或多个元素的类型与指定 collection 不兼容
     *         (<a href="#optional-restrictions">可选</a>)
     * @throws NullPointerException 如果此 collection 包含一个或多个 null 元素，
     *         并且指定的 collection 不支持 null 元素 (<a href="#optional-restrictions">可选</a>)，
     *         或者指定的 collection 为 null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    boolean retainAll(Collection<?> c);

    /**
     * 移除此 collection 中的所有元素（可选操作）。此方法返回后，除非抛出一个异常。
     *
     * @throws UnsupportedOperationException 如果此 collection 不支持 <tt>clear</tt> 操作
     */
    void clear();


    // Comparison and hashing

    /**
     * 比较此 collection 与指定对象是否相等。 <p>
     *
     * 当 <tt>Collection</tt> 接口没有对 <tt>Object.equals</tt> 的常规协定添加任何约定时，
     * “直接”实现该 <tt>Collection</tt> 接口
     * （换句话说，创建一个 <tt>Collection</tt>，但它不是 <tt>Set</tt> 或 <tt>List</tt> 的类）的
     * 程序员选择重写 <tt>Object.equals</tt> 方法时必须小心。
     * 没必要这样做，最简单的方案是依靠 <tt>Object</tt>' 的实现，然而实现者可能希望实现“值比较”，
     * 而不是默认的“引用比较”。（<tt>List</tt> 和 <tt>Set</tt> 接口要求进行这样的值比较。）<p>
     *
     * <tt>Object.equals</tt> 方法的常规协定声称相等必须是对称的
     * （换句话说，当且仅当存在 <tt>b.equals(a)</tt> 时，才存在 <tt>a.equals(b)</tt>）。
     * <tt>List.equals</tt> 和 <tt>Set.equals</tt> 的协定声称 list 只能与 list 相等，
     * set 只能与 set 相等。
     * 因此，对于一个既不实现 <tt>List</tt>  又不实现 <tt>Set</tt> 接口的 collection 类，
     * 当将此 collection 与任何list或 set 进行比较时，
     * 常规的 <tt>equals</tt> 方法必须返回 <tt>false</tt>。
     * （按照相同的逻辑，不可能编写一个同时正确实现 <tt>Set</tt> 和 <tt>List</tt> 接口的类。）
     *
     * @param o 要与此 collection 进行相等性比较的对象。
     * @return 如果指定对象与此 collection 相等，则返回  <tt>true</tt>
     *
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     * @see List#equals(Object)
     */
    boolean equals(Object o);

    /**
     * 返回此 collection 的哈希码值。
     * 当 <tt>Collection</tt> 接口没有为 <tt>Object.hashCode</tt> 方法的常规协定添加任何约束时，
     * 为了满足 <tt>Object.hashCode</tt> 方法的常规协定，
     * 程序员应该注意任何重写 <tt>Object.equals</tt> 方法的类必须重写 <tt>Object.hashCode</tt> 方法。
     * 需要特别指出的是， <tt>c1.equals(c2)</tt> 暗示着 <tt>c1.hashCode()==c2.hashCode()</tt>。
     *
     * @return 此 collection 的哈希码值
     *
     * @see Object#hashCode()
     * @see Object#equals(Object)
     */
    int hashCode();

    /**
     * Creates a {@link Spliterator} over the elements in this collection.
     *
     * Implementations should document characteristic values reported by the
     * spliterator.  Such characteristic values are not required to be reported
     * if the spliterator reports {@link Spliterator#SIZED} and this collection
     * contains no elements.
     *
     * <p>The default implementation should be overridden by subclasses that
     * can return a more efficient spliterator.  In order to
     * preserve expected laziness behavior for the {@link #stream()} and
     * {@link #parallelStream()}} methods, spliterators should either have the
     * characteristic of {@code IMMUTABLE} or {@code CONCURRENT}, or be
     * <em><a href="Spliterator.html#binding">late-binding</a></em>.
     * If none of these is practical, the overriding class should describe the
     * spliterator's documented policy of binding and structural interference,
     * and should override the {@link #stream()} and {@link #parallelStream()}
     * methods to create streams using a {@code Supplier} of the spliterator,
     * as in:
     * <pre>{@code
     *     Stream<E> s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)
     * }</pre>
     * <p>These requirements ensure that streams produced by the
     * {@link #stream()} and {@link #parallelStream()} methods will reflect the
     * contents of the collection as of initiation of the terminal stream
     * operation.
     *
     * @implSpec
     * The default implementation creates a
     * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
     * from the collections's {@code Iterator}.  The spliterator inherits the
     * <em>fail-fast</em> properties of the collection's iterator.
     * <p>
     * The created {@code Spliterator} reports {@link Spliterator#SIZED}.
     *
     * @implNote
     * The created {@code Spliterator} additionally reports
     * {@link Spliterator#SUBSIZED}.
     *
     * <p>If a spliterator covers no elements then the reporting of additional
     * characteristic values, beyond that of {@code SIZED} and {@code SUBSIZED},
     * does not aid clients to control, specialize or simplify computation.
     * However, this does enable shared use of an immutable and empty
     * spliterator instance (see {@link Spliterators#emptySpliterator()}) for
     * empty collections, and enables clients to determine if such a spliterator
     * covers no elements.
     *
     * @return a {@code Spliterator} over the elements in this collection
     * @since 1.8
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a sequential {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     * @since 1.8
     */
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a possibly parallel {@code Stream} with this collection as its
     * source.  It is allowable for this method to return a sequential stream.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a parallel {@code Stream} from the
     * collection's {@code Spliterator}.
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     * @since 1.8
     */
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
