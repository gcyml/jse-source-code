/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
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

import java.io.InvalidObjectException;
import sun.misc.SharedSecrets;

/**
 * 此类实现 <tt>Set</tt> 接口，由哈希表（实际上是一个 <tt>HashMap</tt> 实例）支持。
 * 它不保证 set 的迭代顺序；特别是它不保证该顺序恒久不变。此类允许使用 <tt>null</tt> 元素。
 *
 * <p>此类为基本操作提供了稳定性能，这些基本操作包括
 * <tt>add</tt>、<tt>remove</tt>、<tt>contains</tt> 和 <tt>size</tt>，
 * 假定哈希函数将这些元素正确地分布在桶中。
 * 对此 set 进行迭代所需的时间与 <tt>HashSet</tt> 实例的大小（元素的数量）
 * 和底层 <tt>HashMap</tt> 实例（桶的数量）的“容量”的和成比例。
 * 因此，如果迭代性能很重要，则不要将初始容量设置得太高（或将加载因子设置得太低）。
 *
 * <p><strong>注意，此实现不是同步的。</strong>
 * 如果多个线程同时访问一个哈希 set，而其中至少一个线程修改了该 set，那么 <i>它必须</i> 保持外部同步。
 * 这通常是通过对自然封装该 set 的对象执行同步操作来完成的。
 *
 * 如果不存在这样的对象，
 * 则应该使用 {@link Collections#synchronizedSet Collections.synchronizedSet} 方法来“包装” set。
 * 最好在创建时完成这一操作，以防止对该 set 进行意外的不同步访问：<pre>
 *   Set s = Collections.synchronizedSet(new HashSet(...));</pre>
 *
 * <p>此类的 <tt>iterator</tt> 方法返回的迭代器是 <i>快速失败</i> 的：
 * 在创建迭代器之后，如果对 set 进行修改，除非通过迭代器自身的 <tt>remove</tt> 方法，
 * 否则在任何时间以任何方式对其进行修改，Iterator 都将抛出 {@link ConcurrentModificationException}。
 * 因此，面对并发的修改，迭代器很快就会完全失败，而不冒将来在某个不确定时间发生任意不确定行为的风险。
 *
 * <p>注意，迭代器的快速失败行为无法得到保证，
 * 因为一般来说，不可能对是否出现不同步并发修改做出任何硬性保证。
 * 快速失败迭代器在尽最大努力抛出 <tt>ConcurrentModificationException</tt>。
 * 因此，为提高这类迭代器的正确性而编写一个依赖于此异常的程序是错误做法：
 * <i>迭代器的快速失败行为应该仅用于检测 bug。</i>
 *
 * <p>此类是
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的成员。
 *
 * @param <E> 此 set 所维护的元素的类型
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Collection
 * @see     Set
 * @see     TreeSet
 * @see     HashMap
 * @since   1.2
 */

public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
{
    static final long serialVersionUID = -5024744406713321676L;

    private transient HashMap<E,Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    /**
     * 构造一个新的空 set，其底层 <tt>HashMap</tt> 实例的默认初始容量是 16，加载因子是 0.75。
     */
    public HashSet() {
        map = new HashMap<>();
    }

    /**
     * 构造一个包含指定 collection 中的元素的新 set。
     * 使用默认的加载因子 0.75 和足以包含指定 collection 中所有元素的初始容量来创建 <tt>HashMap</tt>。
     *
     * @param c 其中的元素将存放在此 set 中的 collection
     * @throws NullPointerException 如果指定的 collection 为 null
     */
    public HashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }

    /**
     * 构造一个新的空 set，其底层 <tt>HashMap</tt> 实例具有指定的初始容量和指定的加载因子。
     *
     * @param      initialCapacity   哈希映射的初始容量
     * @param      loadFactor        哈希映射的加载因子
     * @throws     IllegalArgumentException 如果初始容量小于零，或者加载因子为非正数
     */
    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * 构造一个新的空 set，其底层 <tt>HashMap</tt> 实例具有指定的初始容量和默认的加载因子（0.75）。
     *
     * @param      initialCapacity   哈希表的初始容量
     * @throws     IllegalArgumentException 如果初始容量小于零
     */
    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    /**
     * 构造一个新的空链表hashset。
     * （此包的私有构造器仅使用了LinkedHashSet。）
     * 后备HashMap实例是具有指定初始容量和指定加载因子的LinkedHashMap。
     *
     * @param      initialCapacity   哈希映射的初始容量
     * @param      loadFactor        哈希映射的加载因子
     * @param      dummy             忽略（将此构造函数与其他int，float构造函数区分开。）
     * @throws     IllegalArgumentException 如果初始容量小于零，或者加载因子为非正数
     */
    HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * 返回对此 set 中元素进行迭代的迭代器。返回元素的顺序并不是特定的。
     *
     * @return 对此 set 中元素进行迭代的 Iterator
     * @see ConcurrentModificationException
     */
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * 返回此 set 中的元素的数量（set 的容量）。
     *
     * @return 此 set 中的元素的数量（set 的容量）
     */
    public int size() {
        return map.size();
    }

    /**
     * 如果此 set 不包含任何元素，则返回 <tt>true</tt>。
     *
     * @return 如果此 set 不包含任何元素，则返回 <tt>true</tt>
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 如果此 set 包含指定元素，则返回 <tt>true</tt>。 更确切地讲，当且仅当此 set 包含一个满足
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>
     * 的 <tt>e</tt> 元素时，返回 <tt>true</tt>。
     *
     * @param o 其在此 set 中的存在已得到测试的元素
     * @return 如果此 set 包含指定元素，则返回 <tt>true</tt>
     */
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * 如果此 set 中尚未包含指定元素，则添加指定元素。更确切地讲，如果此 set 没有包含满足
     * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>
     * 的元素 <tt>e2</tt>，则向此 set 添加指定的元素 <tt>e</tt>。
     * 如果此 set 已包含该元素，则该调用不更改 set 并返回 <tt>false</tt>。
     *
     * @param e 将添加到此 set 中的元素
     * @return 如果此 set 尚未包含指定元素，则返回 <tt>true</tt>
     */
    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }

    /**
     * 如果指定元素存在于此 set 中，则将其移除。更确切地讲，如果此 set 包含一个满足
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>
     * 的元素 <tt>e</tt>，则将其移除。如果此 set 已包含该元素，
     * 则返回 <tt>true</tt>（或者：如果此 set 因调用而发生更改，则返回 <tt>true</tt>）。
     * （一旦调用返回，则此 set 不再包含该元素）。
     *
     * @param o 如果存在于此 set 中则需要将其移除的对象
     * @return 如果 set 包含指定元素，则返回 <tt>true</tt>
     */
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    /**
     * 从此 set 中移除所有元素。此调用返回后，该 set 将为空。
     */
    public void clear() {
        map.clear();
    }

    /**
     * 返回此 <i>HashSet</i> 实例的浅表副本：并没有复制这些元素本身。
     *
     * @return 此 set 的浅表副本
     */
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            HashSet<E> newSet = (HashSet<E>) super.clone();
            newSet.map = (HashMap<E, Object>) map.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /**
     * 将此 <tt>HashSet</tt> 实例的状态保存到流中（即序列化它）。
     *
     * @serialData 后备 <tt>HashMap</tt> 实例的容量（int），和它的加载因子（float），
     *            后跟set的大小（它包含的元素数）（int），后跟所有的它的元素（每个对象）
     *            没有特定的顺序。
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out HashMap capacity and load factor
        s.writeInt(map.capacity());
        s.writeFloat(map.loadFactor());

        // Write out size
        s.writeInt(map.size());

        // Write out all elements in the proper order.
        for (E e : map.keySet())
            s.writeObject(e);
    }

    /**
     * 从流中重构 <tt>HashSet</tt> 实例（即反序列化它）。
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // Read capacity and verify non-negative.
        int capacity = s.readInt();
        if (capacity < 0) {
            throw new InvalidObjectException("Illegal capacity: " +
                                             capacity);
        }

        // Read load factor and verify positive and non NaN.
        float loadFactor = s.readFloat();
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new InvalidObjectException("Illegal load factor: " +
                                             loadFactor);
        }

        // Read size and verify non-negative.
        int size = s.readInt();
        if (size < 0) {
            throw new InvalidObjectException("Illegal size: " +
                                             size);
        }
        // Set the capacity according to the size and load factor ensuring that
        // the HashMap is at least 25% full but clamping to maximum capacity.
        capacity = (int) Math.min(size * Math.min(1 / loadFactor, 4.0f),
                HashMap.MAXIMUM_CAPACITY);

        // Constructing the backing map will lazily create an array when the first element is
        // added, so check it before construction. Call HashMap.tableSizeFor to compute the
        // actual allocation size. Check Map.Entry[].class since it's the nearest public type to
        // what is actually created.

        SharedSecrets.getJavaOISAccess()
                     .checkArray(s, Map.Entry[].class, HashMap.tableSizeFor(capacity));

        // Create backing HashMap
        map = (((HashSet<?>)this) instanceof LinkedHashSet ?
               new LinkedHashMap<E,Object>(capacity, loadFactor) :
               new HashMap<E,Object>(capacity, loadFactor));

        // Read in all elements in the proper order.
        for (int i=0; i<size; i++) {
            @SuppressWarnings("unchecked")
                E e = (E) s.readObject();
            map.put(e, PRESENT);
        }
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * set.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
     * {@link Spliterator#DISTINCT}.  Overriding implementations should document
     * the reporting of additional characteristic values.
     *
     * @return a {@code Spliterator} over the elements in this set
     * @since 1.8
     */
    public Spliterator<E> spliterator() {
        return new HashMap.KeySpliterator<E,Object>(map, 0, -1, 0, 0);
    }
}
