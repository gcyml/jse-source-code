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

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import sun.misc.SharedSecrets;

/**
 * 对<tt>List</tt>接口实现的动态数组。实现了所有的list可选操作，以及允许包括<tt>null</tt>在内的所有元素，
 * 。除了实现List接口外，这个类也提供了一些用来操作内部存储<tt>List</tt>的数组大小的方法。
 * （这个类大致相当于<tt>Vector</tt>，除了它是不同步的。）
 *
 * <p><tt>size</tt>，<tt>isEmpty</tt>，<tt>get</tt>，<tt>set</tt>，<tt>iterator</tt>， 和<tt>listIterator</tt> 操作都是在<i>恒定时间</i>内运行。
 * <tt>add</tt>操作以分摊的恒定时间运行，即添加n个元素的运行时间是O(n)。所有其他操作都是花费线性时间(粗略地说).
 * 相比于<tt>LinkedList</tt>，常数因子较低。
 *
 * <p>每个<tt>ArrayList</tt>实例都有一个<i>容量</i>。 容量是用于存储列表中元素的数组大小值。它至少和列表大小一样大。
 * 随着元素被添加到列表中，其容量会自动扩增。并未指定增长策略的细节，因为这不只是添加元素会带来分摊固定时间开销那样简单。
 *
 * <p>在使用<t>ensureCapacity</t>来添加大量元素前，应用可以增加<tt>ArrayList</tt>的容量。
 * 这可能会减少增量重新分配的时间。
 *
 * <p><strong>需要注意的是这里的实现不是同步的。</strong>
 * 如果多线程同时访问<tt>ArrayList</tt>实例，而其中有一个线程更改了列表结构，则它<i>必须</i>在外部同步。
 * (结构修改是指添加或删除了一个或多个元素，或者明显修改了底层数组的大小；
 * 仅仅修改元素值并不会修改结构。)  这通常通过同步一些自然封装该列表的对象来完成.
 *
 * 如果不存在这样的对象，那么列表应该使用{@link Collections#synchronizedList Collections.synchronizedList}方法列出列表。
 * 为了避免意外的非同步访问列表这最好是在创建时完成:<pre>
 *   List list = Collections.synchronizedList(new ArrayList(...));</pre>
 *
 * <p><a name="fail-fast">
 * 这个类的 {@link #iterator() iterator} 和 {@link #listIterator(int) listIterator} 方法返回的迭代器是<em>fail-fast</em>的:</a>
 * 如果这个列表是在迭代器创建以后任何时刻修改了结构，除了是通过迭代器自身的 {@link ListIterator#remove() remove} 或
 * {@link ListIterator#add(Object) add} 方法修改的, 迭代器将会抛出错误 {@link ConcurrentModificationException}。
 * 因此，对于并发修改，迭代器很快就会完全失败, 而不是在将来某个不确定的时间冒着任意的不确定性行为的风险。
 *
 * <p>请注意 迭代器fail-fast 行为无法得到保证，因为一般来说，
 * 在存在非同步并发修改的情况下无法做出任何硬性保证.
 * Fail-fast 的迭代器尽最大努力抛出 {@code ConcurrentModificationException}。
 * 因此, 为提高这类迭代器的正确性而编写编写一个依赖于此异常的程序是错误的:  <i>迭代器的 fail-fast 行为应该用于检测 bug 。</i>
 *
 * <p>这个类是<a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的成员.
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Collection
 * @see     List
 * @see     LinkedList
 * @see     Vector
 * @since   1.2
 */

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * 默认初始容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 用于空实例的共享空数组实例
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 为默认大小的空实例构建的共享空数组实例。
     * 和 EMPTY_ELEMENTDATA 区别在于添加第一个元素需要膨胀多少
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * ArrayList 元素的缓存数组。
     * ArrayList的容量是这个缓存数组的长度。 当添加第一个元素时，
     * 任意 elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA 的 空ArrayList
     * 将展开 DEFAULT_CAPACITY。
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * ArrayList的大小（它包含的元素的数量）。
     * @serial
     */
    private int size;

    /**
     * 用指定的初始容量构建一个空列表。
     *
     * @param  initialCapacity  列表的初始容量
     * @throws IllegalArgumentException 如果指定的初始容量为负
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    /**
     * 构造一个初始容量为 10 的空列表。
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 构造一个包含指定 collection 的元素的列表，这些元素是按照该 collection 的迭代器返回它们的顺序排列的。
     *
     * @param c 其元素将放置在此列表中的 collection
     * @throws NullPointerException 如果指定的 collection 为 null
     */
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * 将此 <tt>ArrayList</tt> 实例的容量调整为列表的当前大小。
     * 应用程序可以使用此操作来最小化 <tt>ArrayList</tt> 实例的存储量。
     */
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
              ? EMPTY_ELEMENTDATA
              : Arrays.copyOf(elementData, size);
        }
    }

    /**
     * 如有必要，增加此 <tt>ArrayList</tt> 实例的容量，
     * 以确保它至少能够容纳最小容量参数所指定的元素数。
     *
     * @param   minCapacity   所需的最小容量
     */
    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
            // any size if not default element table
            ? 0
            // larger than default for default empty table. It's already
            // supposed to be at default size.
            : DEFAULT_CAPACITY;

        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }

    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
        }

    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * 要分配的最大数组大小。
     * 有一些VMs会在数组中保留一些头部字。
     * 尝试分配较大的数组可能会导致OutOfMemoryError：请求的数组大小超过VM限制
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 增加容量大小以保证至少能容纳指定最小容量数的元素。
     *
     * @param minCapacity 所需的最小容量
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    /**
     * 返回此列表中的元素数。
     *
     * @return 此列表中的元素数
     */
    public int size() {
        return size;
    }

    /**
     * 如果此列表中没有元素，则返回 <tt>true</tt> 。
     *
     * @return 如果此列表中没有元素，则返回<tt>true</tt>
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 如果此列表中包含指定的元素，则返回 <tt>true</tt> 。
     * 更确切地讲，当且仅当此列表包含至少一个满足<tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt> 的元素 e 时，
     * 则返回 <tt>true</tt> 。
     *
     * @param o 测试此列表中是否存在的元素
     * @return 如果此列表包含特定的元素，则返回 <tt>true</tt>
     */
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * 返回此列表中首次出现的指定元素的索引, 或如果此列表不包含元素，则返回 -1。
     * 更确切地讲，返回满足 <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最低索引 <tt>i</tt> ，如果不存在此类索引，则返回 -1。
     */
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * 返回此列表中最后一次出现的指定元素的索引，或如果此列表不包含索引，则返回 -1。
     * 更确切地讲，返回满足 <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最高索引 <tt>i</tt> ，如果不存在此类索引，则返回 -1。
     */
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * 返回此 <tt>ArrayList</tt> 实例的浅表副本。（不复制这些元素本身。）
     *
     * @return 此 <tt>ArrayList</tt> 实例的一个副本
     */
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    /**
     * 按适当顺序（从第一个到最后一个元素）返回包含此列表中所有元素的数组。
     *
     * <p> 由于此列表不维护对返回数组的任何引用，，因而它将是“安全的”。
     * （换句话说，此方法必须分配一个新的数组）。因此，调用者可以自由地修改返回的数组。
     *
     * <p> 此方法担当基于数组的 API 和基于 collection 的 API 之间的桥梁。
     *
     * @return 含此列表中所有元素的数组（按适当顺序）
     */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    /**
     * 按适当顺序（从第一个到最后一个元素）返回包含此列表中所有元素的数组；
     * 返回数组的运行时类型是指定数组的运行时类型。
     * 如果指定的数组能容纳列表，则将该列表返回此处。
     * 否则，将分配一个具有指定数组的运行时类型和此列表大小的新数组。
     *
     * <p>如果指定的数组能容纳队列，并有剩余的空间（即数组的元素比队列多），
     * 那么会将数组中紧接 <tt>collection</tt> 尾部的元素设置为 <tt>null</tt>。
     * （<i>仅</i> 在调用者知道列表中不包含任何 <tt>null</tt> 元素时才能用此方法确定列表长度）。
     *
     * @param a 要在其中存储列表元素的数组（如果它足够大）；
     *          否则，为此分配一个具有相同运行时类型的新数组。
     * @return 包含列表元素的数组
     * @throws ArrayStoreException 如果指定数组的运行时类型不是此列表每个元素的运行时类型的超类型
     * @throws NullPointerException 如果指定数组为 null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // Positional Access Operations

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * 返回此列表中指定位置上的元素。
     *
     * @param  index 要返回元素的索引
     * @return 此列表中指定位置上的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }

    /**
     * 用指定的元素替代此列表中指定位置上的元素。
     *
     * @param index 要替代的元素的索引
     * @param element 存储在指定位置上的元素
     * @return 以前位于该指定位置上的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        rangeCheck(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    /**
     * 将指定的元素添加到此列表的尾部。
     *
     * @param e 要添加到此列表中的元素
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }

    /**
     * 将指定的元素插入此列表中的指定位置。
     * 向右移动当前位于该位置的元素（如果有）以及所有后续元素（将其索引加 1）。
     *
     * @param index 指定元素所插入位置的索引
     * @param element 要插入的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);  // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }

    /**
     * 移除此列表中指定位置上的元素。向左移动所有后续元素（将其索引减 1）。
     *
     * @param index 要移除的元素的索引
     * @return 从列表中移除的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        rangeCheck(index);

        modCount++;
        E oldValue = elementData(index);

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work

        return oldValue;
    }

    /**
     * 移除此列表中首次出现的指定元素（如果存在）。如果列表不包含此元素，则列表不做改动。
     * 更确切地讲，移除满足<tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最低索引 <tt>i</tt> 的元素（如果存在此类元素）。
     *
     * 如果列表中包含指定的元素，则返回 <tt>true</tt> （或者等同于这种情况：如果列表由于调用而发生更改，则返回 <tt>true</tt>）。
     *
     * @param o 要从此列表中移除的元素（如果存在）
     * @return 如果此列表包含指定的元素，则返回 <tt>true</tt>
     */
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    /*
     * 私有的删除元素方法，跳过边界检查以及不返回被删除的值。
     */
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }

    /**
     * 移除此列表中的所有元素。此调用返回后，列表将为空。
     */
    public void clear() {
        modCount++;

        // clear to let GC do its work
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    /**
     * 按照指定 collection 的迭代器所返回的元素顺序，
     * 将该 collection 中的所有元素添加到此列表的尾部。
     * 如果正在进行此操作时修改指定的 collection ，那么此操作的行为是不确定的。
     * （这意味着如果指定的 collection 是此列表且此列表是非空的，那么此调用的行为是不确定的）。
     *
     * @param c 包含要添加到此 collection 的元素的 collection
     * @return 如果此 collection 由于调用而发生更改，则返回<tt>true</tt>
     * @throws NullPointerException 如果指定的 collection 为 null
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * 从指定的位置开始，将指定 collection 中的所有元素插入到此列表中。
     * 向右移动当前位于该位置的元素（如果有）以及所有后续元素（增加其索引）。
     * 新元素将按照指定 collection 的迭代器所返回的元素顺序出现在列表中。
     *
     * @param index 插入指定 collection 中的首个元素的位置索引
     * @param c 包含要添加到此列表中元素的 collection
     * @return 如果此列表由于调用而发生更改，则返回 <tt>true</tt>
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException 如果指定的 collection 为 null
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * 移除列表中索引在 {@code fromIndex} （包括）和 {@code toIndex} （不包括）之间的所有元素。
     * 向左移动所有后续元素（减小其索引）。此调用将列表缩短了 {@code (toIndex - fromIndex)} 个元素。
     * （如果 {@code toIndex==fromIndex}，则此操作无效。）
     *
     * @throws IndexOutOfBoundsException 如果 {@code fromIndex} 或
     *         {@code toIndex} 超出范围
     *         ({@code fromIndex < 0 ||
     *          fromIndex >= size() ||
     *          toIndex > size() ||
     *          toIndex < fromIndex})
     */
    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // clear to let GC do its work
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }

    /**
     * 检查给出的索引是否在范围内。如果不在，抛出一个 适当的 运行时异常。
     * 当索引为负时，此方法 **不** 做检查：它总是在数组使用之前立即使用，
     * 如果索引为负则会抛出一个ArrayIndexOutOfBoundsException。
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * 一个 add 和 addAll 使用的边界检查的版本
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * 构建 IndexOutOfBoundsException 详细信息。
     * 在许多可能的错误处理代码的重构中， 这个“大纲”对于服务器和 VMs都表现最佳。
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    /**
     * 从列表中移除指定 collection 中包含的其所有元素。
     *
     * @param c 包含从此列表中移除的元素的 collection
     * @return 如果此列表由于调用而发生更改，则返回 {@code true}
     * @throws ClassCastException 如果此列表中的元素的类和指定的 collection 不兼容
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException  如果此列表包含一个 null 元素，并且指定的 collection 不允许 null 元素
     * (<a href="Collection.html#optional-restrictions">可选</a>),
     *         ，或者指定的 collection 为 null
     * @see Collection#contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    /**
     * 仅在列表中保留指定 collection 中所包含的元素。
     * 换句话说，该方法从列表中移除未包含在指定 collection 中的所有元素。
     *
     * @param c 包含将保留在此列表中的元素的 collection
     * @return 如果此列表由于调用而发生更改，则返回 {@code true}
     * @throws ClassCastException 如果此列表的元素的类和指定的 collection 不兼容
     * (<a href="Collection.html#optional-restrictions">可选</a>)
     * @throws NullPointerException  如果此列表包含一个 null 元素，并且指定的 collection 不允许 null 元素
     * (<a href="Collection.html#optional-restrictions">可选</a>),
     *         或者指定的 collection 为 null
     * @see Collection#contains(Object)
     */
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            if (r != size) {
                System.arraycopy(elementData, r,
                                 elementData, w,
                                 size - r);
                w += size - r;
            }
            if (w != size) {
                // clear to let GC do its work
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 将 <tt>ArrayList</tt> 实例的状态保存为一个stream（即序列化）。
     *
     * @serialData <tt>ArrayList</tt> 实例的底层数组长度是 emitted (int),
     * 按照所有元素(每个<tt>Object</tt>) 的正确顺序排列。
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // Write out element count, and any hidden stuff
        int expectedModCount = modCount;
        s.defaultWriteObject();

        // Write out size as capacity for behavioural compatibility with clone()
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * 从stream中重新构建 <tt>ArrayList</tt> 实例 (即反序列化).
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        elementData = EMPTY_ELEMENTDATA;

        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in capacity
        s.readInt(); // ignored

        if (size > 0) {
            // be like clone(), allocate array based upon size not capacity
            int capacity = calculateCapacity(elementData, size);
            SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, capacity);
            ensureCapacityInternal(size);

            Object[] a = elementData;
            // Read in all elements in the proper order.
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    /**
     * 返回列表中元素的列表迭代器（按适当顺序），从列表的指定位置开始。
     * 指定的索引表示{@link ListIterator#next next} 的初始调用所返回的第一个元素。
     * {@link ListIterator#previous previous} 方法的初始调用将返回索引比指定索引少 1 的元素。
     *
     * <p> 返回的迭代器是 <a href="#fail-fast"><i>fail-fast</i></a> 的。
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    /**
     * 返回此列表元素的列表迭代器（按适当顺序）。
     *
     * <p> 返回的迭代器是 <a href="#fail-fast"><i>fail-fast</i></a> 的。
     *
     * @see #listIterator(int)
     */
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    /**
     * R返回以恰当顺序在此列表的元素上进行迭代的迭代器。
     *
     * <p> 返回的迭代器是 <a href="#fail-fast"><i>fail-fast</i></a> 的。
     *
     * @return 在此 collection 中的元素上进行迭代的迭代器。
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * AbstractList.Itr 的优化版本
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * AbstractList.ListItr 的优化版本
     */
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                ArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * 返回列表中指定的 {@code fromIndex}（包括 ）和 {@code toIndex}（不包括）之间的部分视图。
     * (如果 {@code fromIndex} 和 {@code toIndex} 相等，则返回的列表为空）。
     * 返回的列表由此列表支持，因此返回列表中的非结构性更改将反映在此列表中，反之亦然。
     * 返回的列表支持此列表支持的所有可选列表操作。
     *
     * <p>此方法省去了显式范围操作（此操作通常针对数组存在）。
     * 通过传递 subList 视图而非整个列表，期望列表的任何操作可用作范围操作。
     * 例如，下面的语句从列表中移除了元素的范围：
     * <pre>
     *      list.subList(from, to).clear();
     * </pre>
     * 可以对 {@link #indexOf(Object)} 和 {@link #lastIndexOf(Object)}  构造类似的语句，
     * 而且 {@link Collections} 类中的所有算法都可以应用于 subList。
     *
     * <p> 如果支持列表（即此列表）通过任何其他方式（而不是通过返回的列表）从<i>结构上修改</i>，
     * 则此方法返回的列表语义将变为未定义（从结构上修改是指更改列表的大小，
     * 或者以其他方式打乱列表，使正在进行的迭代产生错误的结果）。
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }

    private class SubList extends AbstractList<E> implements RandomAccess {
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex,
                               parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayList.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) {
                        return;
                    }
                    final Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i != size && modCount == expectedModCount) {
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // update once at end of iteration to reduce heap write traffic
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator<E>(ArrayList.this, offset,
                                               offset + this.size, this.modCount);
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * 在此列表中的元素上创建一个<em><a href="Spliterator.html#binding">后期绑定</a></em>
     * and <em>fail-fast</em> 的{@link Spliterator}
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, and {@link Spliterator#ORDERED}.
     * Overriding implementations should document the reporting of additional
     * characteristic values.
     *
     * @return a {@code Spliterator} over the elements in this list
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator<>(this, 0, -1, 0);
    }

    /** Index-based split-by-two, lazily initialized Spliterator */
    static final class ArrayListSpliterator<E> implements Spliterator<E> {

        /*
         * If ArrayLists were immutable, or structurally immutable (no
         * adds, removes, etc), we could implement their spliterators
         * with Arrays.spliterator. Instead we detect as much
         * interference during traversal as practical without
         * sacrificing much performance. We rely primarily on
         * modCounts. These are not guaranteed to detect concurrency
         * violations, and are sometimes overly conservative about
         * within-thread interference, but detect enough problems to
         * be worthwhile in practice. To carry this out, we (1) lazily
         * initialize fence and expectedModCount until the latest
         * point that we need to commit to the state we are checking
         * against; thus improving precision.  (This doesn't apply to
         * SubLists, that create spliterators with current non-lazy
         * values).  (2) We perform only a single
         * ConcurrentModificationException check at the end of forEach
         * (the most performance-sensitive method). When using forEach
         * (as opposed to iterators), we can normally only detect
         * interference after actions, not before. Further
         * CME-triggering checks apply to all other possible
         * violations of assumptions for example null or too-small
         * elementData array given its size(), that could only have
         * occurred due to interference.  This allows the inner loop
         * of forEach to run without any further checks, and
         * simplifies lambda-resolution. While this does entail a
         * number of checks, note that in the common case of
         * list.stream().forEach(a), no checks or other computation
         * occur anywhere other than inside forEach itself.  The other
         * less-often-used methods cannot take advantage of most of
         * these streamlinings.
         */

        private final ArrayList<E> list;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index
        private int expectedModCount; // initialized when fence set

        /** Create new spliterator covering the given  range */
        ArrayListSpliterator(ArrayList<E> list, int origin, int fence,
                             int expectedModCount) {
            this.list = list; // OK if null unless traversed
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() { // initialize fence to size on first use
            int hi; // (a specialized variant appears in method forEach)
            ArrayList<E> lst;
            if ((hi = fence) < 0) {
                if ((lst = list) == null)
                    hi = fence = 0;
                else {
                    expectedModCount = lst.modCount;
                    hi = fence = lst.size;
                }
            }
            return hi;
        }

        public ArrayListSpliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                new ArrayListSpliterator<E>(list, lo, index = mid,
                                            expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                @SuppressWarnings("unchecked") E e = (E)list.elementData[i];
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc; // hoist accesses and checks from loop
            ArrayList<E> lst; Object[] a;
            if (action == null)
                throw new NullPointerException();
            if ((lst = list) != null && (a = lst.elementData) != null) {
                if ((hi = fence) < 0) {
                    mc = lst.modCount;
                    hi = lst.size;
                }
                else
                    mc = expectedModCount;
                if ((i = index) >= 0 && (index = hi) <= a.length) {
                    for (; i < hi; ++i) {
                        @SuppressWarnings("unchecked") E e = (E) a[i];
                        action.accept(e);
                    }
                    if (lst.modCount == mc)
                        return;
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) (getFence() - index);
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // figure out which elements are to be removed
        // any exception thrown from the filter predicate at this stage
        // will leave the collection unmodified
        int removeCount = 0;
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                removeSet.set(i);
                removeCount++;
            }
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

        // shift surviving elements left over the spaces left by removed elements
        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            final int newSize = size - removeCount;
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                i = removeSet.nextClearBit(i);
                elementData[j] = elementData[i];
            }
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            this.size = newSize;
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            elementData[i] = operator.apply((E) elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }
}
