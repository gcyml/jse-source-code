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

import java.util.function.Consumer;

/**
 * {@code List} 和 {@code Deque} 接口的双链表列表实现。
 * 实现所有可选的列表操作，并且允许所有元素（包括 {@code null}）。
 *
 * <p>所有操作都是按照双重链接列表的需要执行的。
 * 在列表中编索引的操作将从开头或结尾遍历列表（从靠近指定索引的一端）。
 *
 * <p><strong>注意，此实现不是同步的。</strong>
 * 如果多个线程同时访问一个链接列表，而其中至少一个线程从结构上修改了该列表，
 * 则它 <i> 必须 </i>保持外部同步。
 * （结构修改指添加或删除一个或多个元素的任何操作；仅设置元素的值不是结构修改。）
 * 这一般通过对自然封装该列表的对象进行同步操作来完成。
 *
 * 如果不存在这样的对象，则应该使用 {@link Collections#synchronizedList Collections.synchronizedList} 方法
 * 来“包装”该列表。最好在创建时完成这一操作，以防止对列表进行意外的不同步访问，如下所示：<pre>
 *   List list = Collections.synchronizedList(new LinkedList(...));</pre>
 *
 * <p>此类的 {@code iterator} 和 {@code listIterator} 方法返回的迭代器是 <i>快速失败</i> 的：
 * 在迭代器创建之后，如果从结构上对列表进行修改，除非通过迭代器自身的 {@code remove} 或 {@code add} 方法，
 * 其他任何时间任何方式的修改，迭代器都将抛出 {@link ConcurrentModificationException} 。
 * 因此，面对并发的修改，迭代器很快就会完全失败，而不冒将来不确定的时间任意发生不确定行为的风险。
 *
 * <p>注意，迭代器的快速失败行为不能得到保证，
 * 一般来说，存在不同步的并发修改时，不可能作出任何硬性保证。
 * 快速失败迭代器尽最大努力抛出 {@code ConcurrentModificationException} 。
 * 因此，编写依赖于此异常的程序的方式是错误的，
 * 正确做法是：<i>迭代器的快速失败行为应该仅用于检测程序错误。</i>
 *
 * <p>此类是 <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的成员。
 *
 * @author  Josh Bloch
 * @see     List
 * @see     ArrayList
 * @since 1.2
 * @param <E> the type of elements held in this collection
 */

public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{
    transient int size = 0;

    /**
     * 指向第一个节点的指针。
     * 不变式：(first == null && last == null) ||
     *            (first.prev == null && first.item != null)
     */
    transient Node<E> first;

    /**
     * 指向最后一个节点的指针。
     * 不变式：(first == null && last == null) ||
     *            (last.next == null && last.item != null)
     */
    transient Node<E> last;

    /**
     * 构造一个空列表。
     */
    public LinkedList() {
    }

    /**
     * 构造一个包含指定 collection 中的元素的列表，
     * 这些元素按其 collection 的迭代器返回的顺序排列。
     *
     * @param  c 要将其元素放入此列表的 collection
     * @throws NullPointerException 如果指定的 collection 为 null
     */
    public LinkedList(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    /**
     * 连接 e 作为第一个元素。
     */
    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
        modCount++;
    }

    /**
     * 连接 e 作为最后一个元素。
     */
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }

    /**
     * 在节点 succ 前插入元素 e。
     */
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null)
            first = newNode;
        else
            pred.next = newNode;
        size++;
        modCount++;
    }

    /**
     * 取消和非空的第一个节点 f 的链接。
     */
    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null; // help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }

    /**
     * 取消和非空的最后一个节点 f 的链接。
     */
    private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        modCount++;
        return element;
    }

    /**
     * 取消和非空节点 x 的链接。
     */
    E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        modCount++;
        return element;
    }

    /**
     * 返回此列表的第一个元素。
     *
     * @return 此列表的第一个元素
     * @throws NoSuchElementException 如果此列表为空
     */
    public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    /**
     * 返回此列表的最后一个元素。
     *
     * @return 此列表的最后一个元素
     * @throws NoSuchElementException 如果此列表为空
     */
    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }

    /**
     * 移除并返回此列表的第一个元素。
     *
     * @return 此列表的第一个元素
     * @throws NoSuchElementException 如果此列表为空
     */
    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }

    /**
     * 移除并返回此列表的最后一个元素。
     *
     * @return 此列表的最后一个元素
     * @throws NoSuchElementException 如果此列表为空
     */
    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return unlinkLast(l);
    }

    /**
     * 将指定元素插入此列表的开头。
     *
     * @param e 要添加的元素
     */
    public void addFirst(E e) {
        linkFirst(e);
    }

    /**
     * 将指定元素添加到此列表的结尾。
     *
     * <p>此方法等效于 {@link #add} 。
     *
     * @param e 要添加的元素
     */
    public void addLast(E e) {
        linkLast(e);
    }

    /**
     * 如果此列表包含指定元素，则返回 {@code true} 。
     * 更确切地讲，当且仅当此列表包含至少一个满足 <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>
     * 的元素 {@code e} 时返回 {@code true}。
     *
     * @param o 要测试在此列表中是否存在的元素
     * @return 如果此列表包含指定元素，则返回 {@code true}
     */
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * 返回此列表的元素数。
     *
     * @return 此列表的元素数
     */
    public int size() {
        return size;
    }

    /**
     * 将指定元素添加到此列表的结尾。
     *
     * <p>此方法等效于 {@link #addLast} 。
     *
     * @param e 要添加到此列表的元素
     * @return {@code true} （根据 {@link Collection#add} 的规定）
     */
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    /**
     * 从此列表中移除首次出现的指定元素（如果存在）。如果列表不包含该元素，则不作更改。
     * 更确切地讲，移除具有满足
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最低索引 {@code i} 的元素（如果存在这样的元素）。
     * 如果此列表已包含指定元素（或者此列表由于调用而发生更改），则返回 {@code true}。
     *
     * @param o 要从此列表删除的元素，如果存在
     * @return 如果此列表包含指定元素，则返回 {@code true}
     */
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 添加指定 collection 中的所有元素到此列表的结尾，
     * 顺序是指定 collection 的迭代器返回这些元素的顺序。
     * 如果指定的 collection 在操作过程中被修改，则此操作的行为是不确定的。
     * （注意，如果指定 collection 就是此列表并且非空，则此操作的行为是不确定的。）
     *
     * @param c 包含要添加到此列表的元素的 collection
     * @return 如果此列表由于调用而更改，则返回 {@code true}
     * @throws NullPointerException 如果指定的 collection 为 null
     */
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    /**
     * 将指定 collection 中的所有元素从指定位置开始插入此列表。
     * 移动当前在该位置上的元素（如果有），所有后续元素都向右移（增加其索引）。
     * 新元素将按由指定 collection 的迭代器返回的顺序在列表中显示。
     *
     * @param index 在其中插入指定 collection 中第一个元素的索引
     * @param c 包含要添加到此列表的元素的 collection
     * @return 如果此列表由于调用而更改，则返回 {@code true}
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException 如果指定的 collection 为 null
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;

        Node<E> pred, succ;
        if (index == size) {
            succ = null;
            pred = last;
        } else {
            succ = node(index);
            pred = succ.prev;
        }

        for (Object o : a) {
            @SuppressWarnings("unchecked") E e = (E) o;
            Node<E> newNode = new Node<>(pred, e, null);
            if (pred == null)
                first = newNode;
            else
                pred.next = newNode;
            pred = newNode;
        }

        if (succ == null) {
            last = pred;
        } else {
            pred.next = succ;
            succ.prev = pred;
        }

        size += numNew;
        modCount++;
        return true;
    }

    /**
     * 从此列表中移除所有元素。
     * 调用此函数后列表将为空。
     */
    public void clear() {
        // Clearing all of the links between nodes is "unnecessary", but:
        // - helps a generational GC if the discarded nodes inhabit
        //   more than one generation
        // - is sure to free memory even if there is a reachable Iterator
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
        modCount++;
    }


    // Positional Access Operations

    /**
     * 返回此列表中指定位置处的元素。
     *
     * @param index 要返回的元素的索引
     * @return 列表中指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    /**
     * 将此列表中指定位置的元素替换为指定的元素。
     *
     * @param index 要替换的元素的索引
     * @param element 要在指定位置存储的元素
     * @return 以前在指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    /**
     * 在此列表中指定的位置插入指定的元素。
     * 移动当前在该位置处的元素（如果有），所有后续元素都向右移（在其索引中添加 1）。
     *
     * @param index 要在其中插入指定元素的索引
     * @param element 要插入的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    /**
     * 移除此列表中指定位置处的元素。
     * 将任何后续元素向左移（从索引中减 1）。返回从列表中删除的元素。
     *
     * @param index 要移除的元素的索引
     * @return 以前在指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    /**
     * 判断给定参数是否为存在的元素的索引。
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    /**
     * 判断给定参数是否为迭代器或添加操作的有效位置的索引。
     */
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    /**
     * 构造 IndexOutOfBoundsException 详细信息。
     * 在错误处理代码的许多有可能的重构中，
     * 这个“提纲”对于服务器和用户端 VMs 都表现最佳。
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * 返回指定索引的（非空）节点。
     */
    Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // Search Operations

    /**
     * 返回此列表中首次出现的指定元素的索引，如果此列表中不包含该元素，则返回 -1。
     * 更确切地讲，返回满足 <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最低索引 {@code i} ；如果没有此索引，则返回 -1。
     *
     * @param o 要搜索的元素
     * @return 此列表中首次出现的指定元素的索引，如果此列表中不包含该元素，则返回 -1
     */
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    /**
     * 返回此列表中最后出现的指定元素的索引，如果此列表中不包含该元素，则返回 -1。
     * 更确切地讲，返回满足
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * 的最高索引 {@code i}；如果没有此索引，则返回 -1。
     *
     * @param o 要搜索的元素
     * @return 此列表中最后出现的指定元素的索引；如果此列表中不包含该元素，则返回 -1
     */
    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (x.item == null)
                    return index;
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.item))
                    return index;
            }
        }
        return -1;
    }

    // Queue operations.

    /**
     * 获取但不移除此列表的头（第一个元素）。
     *
     * @return 此列表的头，如果此列表为空，则返回 {@code null}
     * @since 1.5
     */
    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    /**
     * 获取但不移除此列表的头（第一个元素）。
     *
     * @return 列表的头
     * @throws NoSuchElementException if this list is empty
     * @since 1.5
     */
    public E element() {
        return getFirst();
    }

    /**
     * 获取并移除此列表的头（第一个元素）
     *
     * @return 此列表的头，如果此列表为空，则返回 {@code null}
     * @since 1.5
     */
    public E poll() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    /**
     * 获取并移除此列表的头（第一个元素）。
     *
     * @return 列表的头
     * @throws NoSuchElementException 如果此列表为空
     * @since 1.5
     */
    public E remove() {
        return removeFirst();
    }

    /**
     * 将指定元素添加到此列表的末尾（最后一个元素）。
     *
     * @param e 要添加的元素
     * @return {@code true}（根据 {@link Queue#offer} 的规定）
     * @since 1.5
     */
    public boolean offer(E e) {
        return add(e);
    }

    // Deque operations
    /**
     * 在此列表的开头插入指定的元素。
     *
     * @param e 要插入的元素
     * @return {@code true}（根据 {@link Deque#offerFirst} 的规定）
     * @since 1.6
     */
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * 在此列表末尾插入指定的元素。
     *
     * @param e 要插入的元素
     * @return {@code true}（根据 {@link Deque#offerLast} 的规定）
     * @since 1.6
     */
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * 获取但不移除此列表的第一个元素；如果此列表为空，则返回 {@code null}。
     *
     * @return 此列表的第一个元素；如果此列表为空，则返回 {@code null}
     * @since 1.6
     */
    public E peekFirst() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
     }

    /**
     * 获取但不移除此列表的最后一个元素；如果此列表为空，则返回 {@code null}。
     *
     * @return the last element of this list, or {@code null}
     *         if this list is empty
     * @since 1.6
     */
    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.item;
    }

    /**
     * 获取并移除此列表的第一个元素；如果此列表为空，则返回 {@code null} 。
     *
     * @return 此列表的第一个元素；如果此列表为空，则返回 {@code null}
     * @since 1.6
     */
    public E pollFirst() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    /**
     * 获取并移除此列表的最后一个元素；如果此列表为空，则返回 {@code null} 。
     *
     * @return 此列表的最后一个元素；如果此列表为空，则返回 {@code null}
     * @since 1.6
     */
    public E pollLast() {
        final Node<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }

    /**
     * 将元素推入此列表所表示的堆栈。换句话说，将该元素插入此列表的开头。
     *
     * <p>此方法等效于 {@link #addFirst}。
     *
     * @param e 要推入的元素
     * @since 1.6
     */
    public void push(E e) {
        addFirst(e);
    }

    /**
     * 从此列表所表示的堆栈处弹出一个元素。换句话说，移除并返回此列表的第一个元素。
     *
     * <p>此方法等效于 {@link #removeFirst()}。
     *
     * @return 此列表开头的元素（它是此列表所表示的堆栈的顶部）
     * @throws NoSuchElementException if this list is empty
     * @since 1.6
     */
    public E pop() {
        return removeFirst();
    }

    /**
     * 从此列表中移除第一次出现的指定元素（从头部到尾部遍历列表时）。
     * 如果列表不包含该元素，则不作更改。
     *
     * @param o 要从此列表中移除的元素（如果存在）
     * @return 如果该列表已包含指定元素，则返回 {@code true}
     * @since 1.6
     */
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    /**
     * 从此列表中移除最后一次出现的指定元素（从头部到尾部遍历列表时）。
     * 如果列表不包含该元素，则不作更改。
     *
     * @param o 要从此列表中移除的元素（如果存在）
     * @return 如果该列表已包含指定元素，则返回 {@code true}
     * @since 1.6
     */
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回此列表中的元素的列表迭代器（按适当顺序），从列表中指定位置开始。
     * 遵守 {@code List.listIterator(int)} 的常规协定。<p>
     *
     * 列表迭代器是 <i>快速失败</i> 的：在迭代器创建之后，
     * 如果从结构上对列表进行修改，除非通过列表迭代器自身的 {@code remove} 或 {@code add} 方法，
     * 其他任何时间任何方式的修改，列表迭代器都将抛出 {@code ConcurrentModificationException}。
     * 因此，面对并发的修改，迭代器很快就会完全失败，而不冒将来不确定的时间任意发生不确定行为的风险。
     *
     * @param index 要从列表迭代器返回的第一个元素的索引（通过调用 {@code next} 方法）
     * @return 此列表中的元素的 ListIterator（按适当顺序），从列表中指定位置开始
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @see List#listIterator(int)
     */
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index);
        return new ListItr(index);
    }

    private class ListItr implements ListIterator<E> {
        private Node<E> lastReturned;
        private Node<E> next;
        private int nextIndex;
        private int expectedModCount = modCount;

        ListItr(int index) {
            // assert isPositionIndex(index);
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        public boolean hasNext() {
            return nextIndex < size;
        }

        public E next() {
            checkForComodification();
            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        public E previous() {
            checkForComodification();
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next = (next == null) ? last : next.prev;
            nextIndex--;
            return lastReturned.item;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            checkForComodification();
            if (lastReturned == null)
                throw new IllegalStateException();

            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        public void set(E e) {
            if (lastReturned == null)
                throw new IllegalStateException();
            checkForComodification();
            lastReturned.item = e;
        }

        public void add(E e) {
            checkForComodification();
            lastReturned = null;
            if (next == null)
                linkLast(e);
            else
                linkBefore(e, next);
            nextIndex++;
            expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (modCount == expectedModCount && nextIndex < size) {
                action.accept(next.item);
                lastReturned = next;
                next = next.next;
                nextIndex++;
            }
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * @since 1.6
     */
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    /**
     * 通过 ListItr.previous 提供逆向顺序迭代器的适配器。
     */
    private class DescendingIterator implements Iterator<E> {
        private final ListItr itr = new ListItr(size());
        public boolean hasNext() {
            return itr.hasPrevious();
        }
        public E next() {
            return itr.previous();
        }
        public void remove() {
            itr.remove();
        }
    }

    @SuppressWarnings("unchecked")
    private LinkedList<E> superClone() {
        try {
            return (LinkedList<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /**
     * 返回此 {@code LinkedList} 的浅表副本。（这些元素本身没有复制。）
     *
     * @return 此 {@code LinkedList}  实例的浅表副本
     */
    public Object clone() {
        LinkedList<E> clone = superClone();

        // Put clone into "virgin" state
        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;

        // Initialize clone with our elements
        for (Node<E> x = first; x != null; x = x.next)
            clone.add(x.item);

        return clone;
    }

    /**
     * 返回以适当顺序（从第一个元素到最后一个元素）包含此列表中所有元素的数组。
     *
     * <p>由于此列表不维护对返回数组的任何引用，因而它将是“安全的”。（换句话说，此方法必须分配一个新数组）。
     * 因此，调用者可以随意修改返回的数组。
     *
     * <p>此方法充当基于数组的 API 与基于 collection 的 API 之间的桥梁。
     *
     * @return 以适当顺序包含此列表中所有元素的数组。
     */
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    /**
     * 返回以适当顺序（从第一个元素到最后一个元素）包含此列表中所有元素的数组；
     * 返回数组的运行时类型为指定数组的类型。如果指定数组能容纳列表，则在其中返回该列表。
     * 否则，分配具有指定数组的运行时类型和此列表大小的新数组。
     *
     * <p>如果指定数组能容纳列表，并有剩余空间（即数组比列表元素多），
     * 则紧跟在列表末尾的数组元素会被设置为 {@code null}。
     * （ <i>只有</i> 在调用者知道列表不包含任何 {@code null} 元素时，才可使用此方法来确定列表的长度。）
     *
     * <p>像 {@link #toArray()} 方法一样，此方法充当基于数组的 API 与基于 collection 的 API 之间的桥梁。
     * 更进一步说，此方法允许对输出数组的运行时类型上进行精确控制，在某些情况下，可以用来节省分配开销。
     *
     * <p>假定 {@code x} 是只包含字符串的一个已知列表。
     * 以下代码可用来将该列表转储到一个新分配的 {@code String} 数组：
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * 注意，{@code toArray(new Object[0])} 和 {@code toArray()} 和 toArray() 在功能上是相同的。
     *
     * @param a 要在其中存储列表元素的数组（如果它足够大）；
     *          否则，为其分配具有相同运行时类型的新数组
     * @return 包含列表元素的数组
     * @throws ArrayStoreException 如果指定数组的运行时类型不是此列表中每个元素的运行时类型的超类型
     * @throws NullPointerException 如果指定的数组为 null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    private static final long serialVersionUID = 876323262645176354L;

    /**
     * Saves the state of this {@code LinkedList} instance to a stream
     * (that is, serializes it).
     *
     * @serialData The size of the list (the number of elements it
     *             contains) is emitted (int), followed by all of its
     *             elements (each an Object) in the proper order.
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out size
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (Node<E> x = first; x != null; x = x.next)
            s.writeObject(x.item);
    }

    /**
     * Reconstitutes this {@code LinkedList} instance from a stream
     * (that is, deserializes it).
     */
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // Read in size
        int size = s.readInt();

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++)
            linkLast((E)s.readObject());
    }

    /**
     * 在这个列表的元素上创建一个 <em><a href="Spliterator.html#binding">迟绑定</a></em>
     * 和 <em>快速失败</em> 的 {@link Spliterator} 。
     *
     * <p>{@code Spliterator} 报告了 {@link Spliterator#SIZED} 和
     * {@link Spliterator#ORDERED}。 重写实现了应记录附加特征值的文档。
     *
     * @implNote
     * {@code Spliterator} 另外也报告了 {@link Spliterator#SUBSIZED}
     * 以及实现了 {@code trySplit} 用来允许有限的并行性..
     *
     * @return 列表元素的 {@code Spliterator}
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new LLSpliterator<E>(this, -1, 0);
    }

    /** A customized variant of Spliterators.IteratorSpliterator */
    static final class LLSpliterator<E> implements Spliterator<E> {
        static final int BATCH_UNIT = 1 << 10;  // batch array size increment
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        final LinkedList<E> list; // null OK unless traversed
        Node<E> current;      // current node; null until initialized
        int est;              // size estimate; -1 until first needed
        int expectedModCount; // initialized when est set
        int batch;            // batch size for splits

        LLSpliterator(LinkedList<E> list, int est, int expectedModCount) {
            this.list = list;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEst() {
            int s; // force initialization
            final LinkedList<E> lst;
            if ((s = est) < 0) {
                if ((lst = list) == null)
                    s = est = 0;
                else {
                    expectedModCount = lst.modCount;
                    current = lst.first;
                    s = est = lst.size;
                }
            }
            return s;
        }

        public long estimateSize() { return (long) getEst(); }

        public Spliterator<E> trySplit() {
            Node<E> p;
            int s = getEst();
            if (s > 1 && (p = current) != null) {
                int n = batch + BATCH_UNIT;
                if (n > s)
                    n = s;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do { a[j++] = p.item; } while ((p = p.next) != null && j < n);
                current = p;
                batch = j;
                est = s - j;
                return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Node<E> p; int n;
            if (action == null) throw new NullPointerException();
            if ((n = getEst()) > 0 && (p = current) != null) {
                current = null;
                est = 0;
                do {
                    E e = p.item;
                    p = p.next;
                    action.accept(e);
                } while (p != null && --n > 0);
            }
            if (list.modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            Node<E> p;
            if (action == null) throw new NullPointerException();
            if (getEst() > 0 && (p = current) != null) {
                --est;
                E e = p.item;
                current = p.next;
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

}
