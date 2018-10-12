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
 * 此类提供 <tt>Collection</tt> 接口的骨干实现，
 * 以最大限度地减少了实现此接口所需的工作。<p>
 *
 * 要实现一个不可修改的 collection，编程人员只需扩展此类，
 * 并提供  <tt>iterator</tt> 和 <tt>size</tt> 方法的实现。
 * （<tt>iterator</tt> 方法返回的迭代器必须实现 <tt>hasNext</tt> 和 <tt>next</tt>。）<p>
 *
 * 要实现可修改的 collection，编程人员必须另外重写此类的 <tt>add</tt> 方法
 * （否则，会抛出 <tt>UnsupportedOperationException</tt>），
 * <tt>iterator</tt> 方法返回的迭代器还必须另外实现其 <tt>remove</tt> 方法。<p>
 *
 * 按照 <tt>Collection</tt> 接口规范中的建议，
 * 编程人员通常应提供一个 void （无参数）和 <tt>Collection</tt> 构造方法。<p>
 *
 * 此类中每个非抽象方法的文档详细描述了其实现。
 * 如果要实现的 collection 允许更有效的实现，则可以重写这些方法中的每个方法。<p>
 *
 * 此类是
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的成员。
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @since 1.2
 */

public abstract class AbstractCollection<E> implements Collection<E> {
    /**
     * 唯一的构造方法。（由子类构造方法调用，通常是隐式的。）
     */
    protected AbstractCollection() {
    }

    // Query Operations

    /**
     * 返回在此 collection 中的元素上进行迭代的迭代器。
     *
     * @return 在此 collection 中的元素上进行迭代的迭代器。
     */
    public abstract Iterator<E> iterator();

    public abstract int size();

    /**
     * {@inheritDoc}
     *
     * <p>此实现返回 <tt>size() == 0</tt>。
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现在 collection 中的元素上进行迭代，
     * 并依次检查每个元素以确定其是否与指定的元素相等。
     *
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean contains(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return true;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现返回一个数组，它包含此 collection 的迭代器返回的所有元素，
     * 这些元素的排列顺序与数组的连续元素存储顺序相同，都是从索引 {@code 0} 开始。
     * 返回数组的长度等于迭代器返回的元素数，即使此 collection 的大小发生更改也是如此，
     * 这种情况可能发生在 collection 允许在迭代期间进行并发修改时。
     * {@code size} 方法只是作为一个优化提示被调用；
     * 即使迭代器返回不同的元素数，也会返回正确的结果。
     *
     * <p>此方法等效于：
     *
     *  <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray();
     * }</pre>
     */
    public Object[] toArray() {
        // Estimate size of array; be prepared to see more or fewer elements
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现返回一个数组，它包含此 collection 的迭代器返回的所有元素，
     * 这些元素的排列顺序与数组的连续元素存储顺序相同，都是从索引 {@code 0} 开始。
     * 如果迭代器返回的元素数太大，不适合指定数组，则在新分配的数组中返回这些元素，
     * 该数组的长度等于迭代器返回的元素数，
     * 即使此 collection 的大小在进行迭代期间发生更改也是如此，
     * 这种情况可能发生在 collection 允许在迭代期间进行并发修改时。
     * {@code size} 方法只是作为一个优化提示被调用；即使迭代器返回不同的元素数，
     * 也会返回正确的结果。
     *
     * <p>此方法等效于：
     *
     *  <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray(a);
     * }</pre>
     *
     * @throws ArrayStoreException  {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        T[] r = a.length >= size ? a :
                  (T[])java.lang.reflect.Array
                  .newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = iterator();

        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) { // fewer elements than expected
                if (a == r) {
                    r[i] = null; // null-terminate
                } else if (a.length < i) {
                    return Arrays.copyOf(r, i);
                } else {
                    System.arraycopy(r, 0, a, 0, i);
                    if (a.length > i) {
                        a[i] = null;
                    }
                }
                return a;
            }
            r[i] = (T)it.next();
        }
        // more elements than expected
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     * 要分配的最大数组大小。 一些VM在阵列中保留一些标题字。
     * 尝试分配更大的数组可能会导致OutOfMemoryError：请求的数组大小超过VM限制
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 当迭代器返回的元素多于预期时，重新分配在toArray中使用的数组，并完成从迭代器填充它。
     *
     * @param r 数组，充满先前存储的元素
     * @param it 此集合中正在进行的迭代器
     * @return 包含给定数组中元素的数组，以及迭代器返回的任何其他元素，修剪为size大小
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        int i = r.length;
        while (it.hasNext()) {
            int cap = r.length;
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // overflow-conscious code
                if (newCap - MAX_ARRAY_SIZE > 0)
                    newCap = hugeCapacity(cap + 1);
                r = Arrays.copyOf(r, newCap);
            }
            r[i++] = (T)it.next();
        }
        // trim if overallocated
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError
                ("Required array size too large");
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    // Modification Operations

    /**
     * {@inheritDoc}
     *
     * <p>此实现总是抛出一个
     * <tt>UnsupportedOperationException</tt>.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IllegalStateException         {@inheritDoc}
     */
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现在该 collection 上进行迭代，查找指定的元素。
     * 如果找到该元素，那么它会使用迭代器的 remove 方法从该 collection 中移除该元素。
     *
     * <p>注意，如果此 collection 的 iterator 方法所返回的迭代器无法实现 <tt>remove</tt> 方法，
     * 并且此 collection 包含指定的对象，
     * 那么此实现将抛出 <tt>UnsupportedOperationException</tt>。
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     */
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext()) {
                if (it.next()==null) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }


    // Bulk Operations

    /**
     * {@inheritDoc}
     *
     * <p>此实现在指定的 collection 上进行迭代，依次检查该迭代器返回的每个元素，
     * 查看其是否包含在此 collection 中。如果是，则返回 <tt>true</tt>；否则返回 <tt>false</tt>。
     *
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现在指定的 collection 上进行迭代，并依次将迭代器返回的每个对象添加到此 collection 中。
     *
     * <p>注意，除非重写 <tt>add</tt>，
     * 否则此实现将抛出 <tt>UnsupportedOperationException</tt>（假定指定的 collection 非空）。
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IllegalStateException         {@inheritDoc}
     *
     * @see #add(Object)
     */
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现在此 collection 上进行迭代，依次检查该迭代器返回的每个元素，
     * 以查看其是否包含在指定的 collection 中。
     * 如果是，则使用迭代器的 <tt>remove</tt> 方法将其从此 collection 中移除。
     *
     * <p>注意，如果 <tt>iterator</tt> 方法返回的迭代器无法实现 <tt>remove</tt> 方法，
     * 并且此 collection 包含一个或多个与指定 collection 共有的元素，
     * 那么此实现将抛出 <tt>UnsupportedOperationException</tt>。
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现在此 collection 上进行迭代，依次检查该迭代器返回的每个元素，
     * 以查看其是否包含在指定的 collection 中。
     * 如果不是，则使用迭代器的 <tt>remove</tt> 方法将其从此 collection 中移除。
     *
     * <p>注意，如果 <tt>iterator</tt> 方法返回的迭代器无法实现 <tt>remove</tt> 方法，
     * 并且此 collection 包含一个或多个在指定 collection 中不存在的元素，
     * 那么此实现将抛出 <tt>UnsupportedOperationException</tt>。
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     *
     * <p>此实现在此 collection 上进行迭代，并使用 <tt>Iterator.remove</tt> 操作移除每个元素。
     * 为了提高效率，多数实现可能会选择重写此方法。
     *
     * <p>注意，如果此 collection 的 <tt>iterator</tt> 方法所返回的
     * 迭代器无法实现 <tt>remove</tt> 方法，
     * 并且此 collection 非空，那么此实现将抛出 <tt>UnsupportedOperationException</tt>。
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     */
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }


    //  String conversion

    /**
     * 返回此 collection 的字符串表示形式。
     * 该字符串表示形式由 collection 元素的列表组成，这些元素按其迭代器返回的顺序排列，
     * 并用方括号 ( <tt>"[]"</tt>) 括起来。相邻元素由字符 <tt>", "</tt>（逗号加空格）分隔。
     * 通过 {@link String#valueOf(Object)} 可以将元素转换成字符串。
     *
     * @return 此 collection 的字符串表示形式
     */
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

}
