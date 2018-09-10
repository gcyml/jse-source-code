/*
 * Copyright (c) 1996, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.io;

/**
 * 类通过实现 java.io.Serializable 接口以启用其序列化功能。
 * 未实现此接口的类将无法使其任何状态序列化或反序列化。
 * 可序列化类的所有子类型本身都是可序列化的。序列化接口没有方法或字段，仅用于标识可序列化的语义。
 *
 * <p>要允许不可序列化类的子类型序列化，
 * 可以假定该子类型负责保存和恢复超类型的公用 (public)、受保护的 (protected)
 * 和（如果可访问）包 (package) 字段的状态。
 * 仅在子类型扩展的类有一个可访问的无参数构造方法来初始化该类的状态时，才可以假定子类型有此职责。
 * 如果不是这种情况，则声明一个类为可序列化类是错误的。该错误将在运行时检测到。
 *
 * <p>在反序列化过程中，将使用该类的公用或受保护的无参数构造方法初始化不可序列化类的字段。
 * 可序列化的子类必须能够访问无参数构造方法。可序列化子类的字段将从该流中恢复。
 *
 * <p>当遍历一个图形时，可能会遇到不支持 Serializable 接口的对象。
 * 在此情况下，将抛出 NotSerializableException，并将标识不可序列化对象的类。
 *
 * <p>在序列化和反序列化过程中需要特殊处理的类必须使用下列准确签名来实现特殊方法：
 *
 * <PRE>
 * private void writeObject(java.io.ObjectOutputStream out)
 *     throws IOException
 * private void readObject(java.io.ObjectInputStream in)
 *     throws IOException, ClassNotFoundException;
 * private void readObjectNoData()
 *     throws ObjectStreamException;
 * </PRE>
 *
 * <p>writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以恢复它。
 * 通过调用 out.defaultWriteObject 可以调用保存 Object 的字段的默认机制。
 * 该方法本身不需要涉及属于其超类或子类的状态。
 * 通过使用 writeObject 方法或使用 DataOutput 支持的用于基本数据类型的方法
 * 将各个字段写入 ObjectOutputStream，状态可以被保存。
 *
 * <p>eadObject 方法负责从流中读取并恢复类字段。
 * 它可以调用 in.defaultReadObject 来调用默认机制，以恢复对象的非静态和非瞬态字段。
 * defaultReadObject 方法使用流中的信息来分配流中通过当前对象中相应指定字段保存的对象的字段。
 * 这用于处理类演化后需要添加新字段的情形。该方法本身不需要涉及属于其超类或子类的状态。
 * 通过使用 writeObject 方法或使用 DataOutput 支持的用于基本数据类型的方法
 * 将各个字段写入 ObjectOutputStream，状态可以被保存。
 *
 * <p>在序列化流不列出给定类作为将被反序列化对象的超类的情况下，
 * readObjectNoData 方法负责初始化特定类的对象状态。
 * 这在接收方使用的反序列化实例类的版本不同于发送方，
 * 并且接收者版本扩展的类不是发送者版本扩展的类时发生。
 * 在序列化流已经被篡改时也将发生；
 * 因此，不管源流是“敌意的”还是不完整的，
 * readObjectNoData 方法都可以用来正确地初始化反序列化的对象。
 *
 * <p>将对象写入流时需要指定要使用的替代对象的可序列化类，应使用准确的签名来实现此特殊方法：
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER Object writeReplace() throws ObjectStreamException;
 * </PRE><p>
 *
 * 此 writeReplace 方法将由序列化调用，前提是如果此方法存在，
 * 而且它可以通过被序列化对象的类中定义的一个方法访问。
 * 因此，该方法可以拥有私有 (private)、受保护的 (protected) 和包私有 (package-private) 访问。
 * 子类对此方法的访问遵循 java 访问规则。
 *
 * <p>在从流中读取类的一个实例时需要指定替代的类应使用的准确签名来实现此特殊方法。
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER Object readResolve() throws ObjectStreamException;
 * </PRE><p>
 *
 * <p>序列化运行时使用一个称为 serialVersionUID 的版本号与每个可序列化类相关联，
 * 该序列号在反序列化过程中用于验证序列化对象的发送者和接收者是否为该对象加载了与序列化兼容的类。
 * 如果接收者加载的该对象的类的 serialVersionUID 与对应的发送者的类的版本号不同，
 * 则反序列化将会导致 {@link InvalidClassException}。
 * 可序列化类可以通过声明名为 <code>"serialVersionUID"</code> 的字段
 * （该字段必须是静态 (static)、最终 (final) 的 <code>long</code> 型字段）
 * 显式声明其自己的 serialVersionUID：
 *
 * <PRE>
 * ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;
 * </PRE>
 *
 * 如果可序列化类未显式声明 serialVersionUID，
 * 则序列化运行时将基于该类的各个方面计算该类的默认 serialVersionUID 值，
 * 如“Java(TM) 对象序列化规范”中所述。
 * 不过，<em>强烈建议</em> 所有可序列化类都显式声明 serialVersionUID 值，
 * 原因是计算默认的 serialVersionUID 对类的详细信息具有较高的敏感性，
 * 根据编译器实现的不同可能千差万别，
 * 这样在反序列化过程中可能会导致意外的 <code>InvalidClassException</code>。
 * 因此，为保证 serialVersionUID 值跨不同 java 编译器实现的一致性，
 * 序列化类必须声明一个明确的 serialVersionUID 值。
 * 还强烈建议使用 <code>private</code> 修饰符显示声明 serialVersionUID（如果可能），
 * 原因是这种声明仅应用于直接声明类 -- serialVersionUID 字段作为继承成员没有用处。
 * 数组类不能声明一个明确的 serialVersionUID，因此它们总是具有默认的计算值，
 * 但是数组类没有匹配 serialVersionUID 值的要求。
 *
 * @author  unascribed
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 * @see java.io.ObjectOutput
 * @see java.io.ObjectInput
 * @see java.io.Externalizable
 * @since   JDK1.1
 */
public interface Serializable {
}
