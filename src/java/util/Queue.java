/*
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

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util;

/**
 * 在处理元素前用于保存元素的 collection。
 * 除了基本的 {@link java.util.Collection Collection} 操作外，
 * 队列还提供其他的插入、提取和检查操作。
 * 每个方法都存在两种形式：一种抛出异常（操作失败时），
 * 另一种返回一个特殊值（{@code null} 或 {@code false}，具体取决于操作）。
 * 插入操作的后一种形式是用于专门为有容量限制的 {@code Queue} 实现设计的；
 * 在大多数实现中，插入操作不会失败。
 *
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 * <caption>Queue 方法描述</caption>
 *  <tr>
 *    <td></td>
 *    <td ALIGN=CENTER><em>抛出异常</em></td>
 *    <td ALIGN=CENTER><em>返回特殊值</em></td>
 *  </tr>
 *  <tr>
 *    <td><b>插入</b></td>
 *    <td>{@link Queue#add add(e)}</td>
 *    <td>{@link Queue#offer offer(e)}</td>
 *  </tr>
 *  <tr>
 *    <td><b>移除</b></td>
 *    <td>{@link Queue#remove remove()}</td>
 *    <td>{@link Queue#poll poll()}</td>
 *  </tr>
 *  <tr>
 *    <td><b>检查</b></td>
 *    <td>{@link Queue#element element()}</td>
 *    <td>{@link Queue#peek peek()}</td>
 *  </tr>
 * </table>
 *
 * <p>队列通常（但并非一定）以 FIFO（先进先出）的方式排序各个元素。
 * 不过优先级队列和 LIFO 队列（或堆栈）例外，前者根据提供的比较器或元素的自然顺序对元素进行排序，
 * 后者按 LIFO（后进先出）的方式对元素进行排序。
 * 无论使用哪种排序方式，
 * 队列的 <em>头</em> 都是调用 {@link #remove()} 或 {@link #poll()} 所移除的元素。
 * 在 FIFO 队列中，所有的新元素都插入队列的<em>尾</em>。其他种类的队列可能使用不同的元素放置规则。
 * 每个 {@code Queue} 实现必须指定其顺序属性。
 *
 * <p>如果可能，{@link #offer offer} 方法可插入一个元素，否则返回 {@code false}。
 * 这与 {@link java.util.Collection#add Collection.add} 方法不同，
 * 该方法只能通过抛出未经检查的异常使添加元素失败。
 * offer 方法设计用于正常的失败情况，而不是出现异常的情况，例如在容量固定（&quot;有界&quot;）的队列中。
 *
 * <p>{@link #remove()} 和 {@link #poll()} 方法可移除和返回队列的头。
 * 到底从队列中移除哪个元素是队列排序策略的功能，而该策略在各种实现中是不同的。
 * {@link #remove()} 和 {@link #poll()} 方法仅在队列为空时其行为有所不同：
 * {@link #remove()} 方法抛出一个异常，而 {@link #poll()} 方法则返回 {@code null}。
 *
 * <p>{@link #element()} 和 {@link #peek()} 返回，但不移除，队列的头。
 *
 * <p>{@code Queue} 接口并未定义<i>阻塞队列</i>的方法，而这在并发编程中是很常见的。
 * {@link java.util.concurrent.BlockingQueue} 接口定义了
 * 那些等待元素出现或等待队列中有可用空间的方法，这些方法扩展了此接口。
 *
 * <p>{@code Queue} 实现通常不允许插入 {@code null} 元素，
 * 尽管某些实现（如 {@link LinkedList}）并不禁止插入 {@code null}。
 * 即使在允许 {@code null} 的实现中，也不应该将 {@code null} 插入到 {@code Queue} 中，
 * 因为 {@code null} 也用作 {@code poll} 方法的一个特殊返回值，表明队列不包含元素。
 *
 * <p>{@code Queue} 实现通常未定义 {@code equals} 和 {@code hashCode} 方法的基于元素的版本，
 * 而是从 {@code Object} 类继承了基于身份的版本，因为对于具有相同元素但有不同排序属性的队列而言，
 * 基于元素的相等性并非总是定义良好的。
 *
 *
 * <p>此接口是
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a> 的成员。
 *
 * @see java.util.Collection
 * @see LinkedList
 * @see PriorityQueue
 * @see java.util.concurrent.LinkedBlockingQueue
 * @see java.util.concurrent.BlockingQueue
 * @see java.util.concurrent.ArrayBlockingQueue
 * @see java.util.concurrent.LinkedBlockingQueue
 * @see java.util.concurrent.PriorityBlockingQueue
 * @since 1.5
 * @author Doug Lea
 * @param <E> collection 中所保存元素的类型。
 */
public interface Queue<E> extends Collection<E> {
    /**
     * 将指定的元素插入此队列（如果立即可行且不会违反容量限制），
     * 在成功时返回 {@code true}，如果当前没有可用的空间，则抛出 {@code IllegalStateException}。
     *
     * @param e 要添加的元素
     * @return {@code true} （根据 {@link Collection#add} 的规定）
     * @throws IllegalStateException 如果由于容量的限制此时不能添加该元素
     * @throws ClassCastException 如果指定元素的类不允许将其添加到此队列
     * @throws NullPointerException 如果指定元素为 null 并且此队列不允许 null 元素
     * @throws IllegalArgumentException 如果此元素的某些属性不允许将其添加到此队列
     */
    boolean add(E e);

    /**
     * 将指定的元素插入此队列（如果立即可行且不会违反容量限制），
     * 当使用有容量限制的队列时，此方法通常要优于 {@link #add}，后者可能无法插入元素，
     * 而只是抛出一个异常。
     *
     * @param e 要添加的元素
     * @return 如果该元素已添加到此队列，则返回 {@code true}；否则返回 {@code false}
     * @throws ClassCastException 如果指定元素的类不允许将其添加到此队列
     * @throws NullPointerException 如果指定元素为 null 并且此队列不允许 null 元素
     * @throws IllegalArgumentException 如果此元素的某些属性不允许将其添加到此队列
     */
    boolean offer(E e);

    /**
     * 获取并移除此队列的头。此方法与 {@link #poll poll} 唯一的不同在于：此队列为空时将抛出一个异常。
     *
     * @return 队列的头
     * @throws NoSuchElementException 如果此队列为空
     */
    E remove();

    /**
     * 获取并移除此队列的头，如果此队列为空，则返回 {@code null}。
     *
     * @return 队列的头，如果此队列为空，则返回 {@code null}
     */
    E poll();

    /**
     * 获取，但是不移除此队列的头。此方法与 {@link #peek peek} 唯一的不同在于：
     * 此队列为空时将抛出一个异常。
     *
     * @return 队列的头
     * @throws NoSuchElementException 如果此队列为空
     */
    E element();

    /**
     * 获取但不移除此队列的头；如果此队列为空，则返回 {@code null}。
     *
     * @return 此队列的头；如果此队列为空，则返回 {@code null}
     */
    E peek();
}
