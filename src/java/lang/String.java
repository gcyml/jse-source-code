/*
 * Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.io.ObjectStreamField;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * {@code String} 类代表字符串。Java 程序中的所有字符串字面值（如 {@code "abc"} ）都作为此类的实例实现。
 * <p>
 * 字符串是常量；它们的值在创建之后不能更改。
 * 字符串缓冲区支持可变的字符串。因为 String 对象是不可变的，所以可以共享。例如：
 * <blockquote><pre>
 *     String str = "abc";
 * </pre></blockquote><p>
 * 等效于：
 * <blockquote><pre>
 *     char data[] = {'a', 'b', 'c'};
 *     String str = new String(data);
 * </pre></blockquote><p>
 * 下面给出了一些如何使用字符串的更多示例：
 * <blockquote><pre>
 *     System.out.println("abc");
 *     String cde = "cde";
 *     System.out.println("abc" + cde);
 *     String c = "abc".substring(2,3);
 *     String d = cde.substring(1, 2);
 * </pre></blockquote>
 * <p>
 * {@code String} 类包括的方法可用于检查序列的
 * 单个字符、比较字符串、搜索字符串、提取子字符串、创建字符串副本
 * 并将所有字符全部转换为大写或小写。
 * 大小写映射基于 {@link java.lang.Character Character} 类指定的 Unicode 标准版。
 * <p>
 * Java 语言提供对字符串串联符号（"+"）以及将其他对象转换为字符串的特殊支持。
 * 字符串串联是通过 {@code StringBuilder}
 * （或 {@code StringBuffer}）类及其 {@code append} 方法实现的。
 * 字符串转换是通过 toString 方法实现的，
 * 该方法由 {@code Object} 类定义，并可被 Java 中的所有类继承。
 * 有关字符串串联和转换的更多信息，
 * 请参阅 Gosling、Joy 和 Steele 合著的<i>The Java Language Specification</i>。
 *
 * <p> 除非另行说明，否则将<tt>null</tt>参数传递给
 * 此类中的构造方法或方法将抛出 {@link NullPointerException}。
 *
 * <p>{@code String} 表示一个 UTF-16 格式的字符串，其中的<em>增补字符</em>由<em>代理项对</em>表示
 * （有关详细信息，请参阅 {@code Character} 类中的 <a href="Character.html#unicode">Unicode 字符表示形式</a>）。
 * 索引值是指 {@code char} 代码单元，因此增补字符在 {@code String} 中占用两个位置。
 * <p>{@code String} 类提供处理 Unicode 代码点（即字符）和 Unicode 代码单元（即 {@code char} 值）的方法。
 *
 * @author  Lee Boynton
 * @author  Arthur van Hoff
 * @author  Martin Buchholz
 * @author  Ulf Zibis
 * @see     java.lang.Object#toString()
 * @see     java.lang.StringBuffer
 * @see     java.lang.StringBuilder
 * @see     java.nio.charset.Charset
 * @since   JDK1.0
 */

public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** 用来存储字符的值。 */
    private final char value[];

    /** 字符串的哈希码 Cache */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;

    /**
     * Class String is special cased within the Serialization Stream Protocol.
     *
     * A String instance is written into an ObjectOutputStream according to
     * <a href="{@docRoot}/../platform/serialization/spec/output.html">
     * Object Serialization Specification, Section 6.2, "Stream Elements"</a>
     */
    private static final ObjectStreamField[] serialPersistentFields =
        new ObjectStreamField[0];

    /**
     * 初始化一个新创建的 {@code String} 对象，
     * 使其表示一个空字符序列。注意，由于 {@code String} 是不可变的，
     * 所以无需使用此构造方法。
     */
    public String() {
        this.value = "".value;
    }

    /**
     * 初始化一个新创建的 {@code String} 对象，
     * 使其表示一个与参数相同的字符序列；
     * 换句话说，新创建的字符串是该参数字符串的副本。
     * 由于 {@code String} 是不可变的，所以无需使用此构造方法，
     * 除非需要 {@code original} 的显式副本。
     *
     * @param  original
     *         一个 {@code String}
     */
    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }

    /**
     * 分配一个新的 {@code String}，
     * 使其表示字符数组参数中当前包含的字符序列。
     * 该字符数组的内容已被复制；
     * 后续对字符数组的修改不会影响新创建的字符串。
     *
     * @param  value
     *         字符串的初始值
     */
    public String(char value[]) {
        this.value = Arrays.copyOf(value, value.length);
    }

    /**
     * 分配一个新的 {@code String}，它包含取自字符数组参数一个子数组的字符。
     * {@code offset} 参数是子数组第一个字符的索引，
     * {@code count} 参数指定子数组的长度。
     * 该子数组的内容已被复制；后续对字符数组的修改不会影响新创建的字符串。
     *
     * @param  value
     *         作为字符源的数组
     *
     * @param  offset
     *         初始偏移量
     *
     * @param  count
     *         长度
     *
     * @throws  IndexOutOfBoundsException
     *          如果 {@code offset} 和 {@code count} 参数索引字符
     *          超出 {@code value} 数组的范围
     */
    public String(char value[], int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count <= 0) {
            if (count < 0) {
                throw new StringIndexOutOfBoundsException(count);
            }
            if (offset <= value.length) {
                this.value = "".value;
                return;
            }
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > value.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
        this.value = Arrays.copyOfRange(value, offset, offset+count);
    }

    /**
     * 分配一个新的 {@code String}，
     * 它包含 <a href="Character.html#unicode"> Unicode 代码点</a> 数组参数一个子数组的字符。
     * {@code offset} 参数是该子数组第一个代码点的索引，
     * {@code count} 参数指定子数组的长度。
     * 将该子数组的内容转换为 {@code char}；
     * 后续对 {@code int} 数组的修改不会影响新创建的字符串
     *
     * @param  codePoints
     *         作为 Unicode 代码点的源的数组
     *
     * @param  offset
     *         初始偏移量
     *
     * @param  count
     *         长度
     *
     * @throws  IllegalArgumentException
     *          如果在 {@code codePoints} 中发现任何无效的 Unicode 代码点
     *
     * @throws  IndexOutOfBoundsException
     *          如果 {@code offset} 和 {@code count} 参数索引字符
     *          If the {@code offset} and {@code count} arguments index
     *          超出 {@code codePoints} 数组的范围
     *
     * @since  1.5
     */
    public String(int[] codePoints, int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count <= 0) {
            if (count < 0) {
                throw new StringIndexOutOfBoundsException(count);
            }
            if (offset <= codePoints.length) {
                this.value = "".value;
                return;
            }
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > codePoints.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }

        final int end = offset + count;

        // Pass 1: Compute precise size of char[]
        int n = count;
        for (int i = offset; i < end; i++) {
            int c = codePoints[i];
            if (Character.isBmpCodePoint(c))
                continue;
            else if (Character.isValidCodePoint(c))
                n++;
            else throw new IllegalArgumentException(Integer.toString(c));
        }

        // Pass 2: Allocate and fill in char[]
        final char[] v = new char[n];

        for (int i = offset, j = 0; i < end; i++, j++) {
            int c = codePoints[i];
            if (Character.isBmpCodePoint(c))
                v[j] = (char)c;
            else
                Character.toSurrogates(c, v, j++);
        }

        this.value = v;
    }

    /**
     * 分配一个新的 {@code String}，它是根据一个 8 位整数值数组的子数组构造的。
     *
     * <p> {@code offset} 参数是该子数组的第一个 byte 的索引，
     * {@code count} 参数指定子数组的长度。
     *
     * <p> 子数组中的每个 {@code byte} 都按照上述方法转换为 {@code char}。
     *
     * @deprecated 该方法无法将字节正确地转换为字符。
     * 从 JDK&nbsp;1.1 开始，完成该转换的首选方法是使用带有 {@link
     * java.nio.charset.Charset}、字符集名称，
     * 或使用平台默认字符集的 {@code String} 构造方法。
     *
     * @param  ascii
     *         要转换为字符的 byte
     *
     * @param  hibyte
     *         每个 16 位 Unicode 代码单元的前 8 位
     *
     * @param  offset
     *         初始偏移量
     * @param  count
     *         长度
     *
     * @throws  IndexOutOfBoundsException
     *          如果 {@code offset} 或 {@code count} 参数无效
     *
     * @see  #String(byte[], int)
     * @see  #String(byte[], int, int, java.lang.String)
     * @see  #String(byte[], int, int, java.nio.charset.Charset)
     * @see  #String(byte[], int, int)
     * @see  #String(byte[], java.lang.String)
     * @see  #String(byte[], java.nio.charset.Charset)
     * @see  #String(byte[])
     */
    @Deprecated
    public String(byte ascii[], int hibyte, int offset, int count) {
        checkBounds(ascii, offset, count);
        char value[] = new char[count];

        if (hibyte == 0) {
            for (int i = count; i-- > 0;) {
                value[i] = (char)(ascii[i + offset] & 0xff);
            }
        } else {
            hibyte <<= 8;
            for (int i = count; i-- > 0;) {
                value[i] = (char)(hibyte | (ascii[i + offset] & 0xff));
            }
        }
        this.value = value;
    }

    /**
     * 分配一个新的 {@code String}，它包含根据一个 8 位整数值数组构造的字符。
     * 所得字符串中的每个字符 <i>c</i> 都是根据 byte 数组中的相应组件 <i>b</i> 构造的，
     * 如下所示：
     *
     * <blockquote><pre>
     *     <b><i>c</i></b> == (char)(((hibyte &amp; 0xff) &lt;&lt; 8)
     *                         | (<b><i>b</i></b> &amp; 0xff))
     * </pre></blockquote>
     *
     * @deprecated 该方法无法将字节正确地转换为字符。
     * 从 JDK&nbsp;1.1 开始，完成该转换的首选方法是使用带有 {@link
     * java.nio.charset.Charset}、字符集名称，
     * 或使用平台默认字符集的 {@code String} 构造方法。
     *
     * @param  ascii
     *         要转换为字符的 byte
     *
     * @param  hibyte
     *         每个 16 位 Unicode 代码单元的前 8 位
     *
     * @see  #String(byte[], int, int, java.lang.String)
     * @see  #String(byte[], int, int, java.nio.charset.Charset)
     * @see  #String(byte[], int, int)
     * @see  #String(byte[], java.lang.String)
     * @see  #String(byte[], java.nio.charset.Charset)
     * @see  #String(byte[])
     */
    @Deprecated
    public String(byte ascii[], int hibyte) {
        this(ascii, hibyte, 0, ascii.length);
    }

    /* Common private utility method used to bounds check the byte array
     * and requested offset & length values used by the String(byte[],..)
     * constructors.
     */
    private static void checkBounds(byte[] bytes, int offset, int length) {
        if (length < 0)
            throw new StringIndexOutOfBoundsException(length);
        if (offset < 0)
            throw new StringIndexOutOfBoundsException(offset);
        if (offset > bytes.length - length)
            throw new StringIndexOutOfBoundsException(offset + length);
    }

    /**
     * 通过使用指定的字符集解码指定的 byte 子数组，构造一个新的 {@code String}。
     * 新 {@code String} 的长度是一个字符集函数，因此可能不等于子数组的长度
     *
     * <p> 当给定 byte 在给定字符集中无效的情况下，此构造方法的行为没有指定。
     * 如果需要对解码过程进行更多控制，
     * 则应该使用 {@link java.nio.charset.CharsetDecoder} 类。
     *
     * @param  bytes
     *         要解码为字符的 byte
     *
     * @param  offset
     *         要解码的第一个 byte 的索引
     *
     * @param  length
     *         要解码的 byte 数

     * @param  charsetName
     *         受支持 {@linkplain java.nio.charset.Charset charset}
     *         charset 的名称
     *
     * @throws  UnsupportedEncodingException
     *          如果指定的字符集不受支持
     *
     * @throws  IndexOutOfBoundsException
     *          如果 {@code offset} 和 {@code length} 参数索引字符超出 {@code bytes} 数组的范围
     *
     * @since  JDK1.1
     */
    public String(byte bytes[], int offset, int length, String charsetName)
            throws UnsupportedEncodingException {
        if (charsetName == null)
            throw new NullPointerException("charsetName");
        checkBounds(bytes, offset, length);
        this.value = StringCoding.decode(charsetName, bytes, offset, length);
    }

    /**
     * 通过使用指定的 {@linkplain java.nio.charset.Charset charset} 解码指定的 byte 子数组，
     * 构造一个新的 {@code String}。
     * 新 {@code String} 的长度是字符集的函数，因此可能不等于子数组的长度。
     *
     * <p> 此方法总是使用此字符集的默认替代字符串替代错误输入
     * (malformed-input) 和不可映射字符 (unmappable-character) 序列。
     * 如果需要对解码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetDecoder} 类。
     *
     * @param  bytes
     *         要解码为字符的 byte
     *
     * @param  offset
     *         要解码的第一个 byte 的索引
     *
     * @param  length
     *         T要解码的 byte 数
     *
     * @param  charset
     *         用来解码 {@code bytes} 的 {@linkplain java.nio.charset.Charset charset}
     *
     * @throws  IndexOutOfBoundsException
     *          如果 {@code offset} 和 {@code length} 参数索引字符超出 {@code bytes} 数组的范围
     *
     * @since  1.6
     */
    public String(byte bytes[], int offset, int length, Charset charset) {
        if (charset == null)
            throw new NullPointerException("charset");
        checkBounds(bytes, offset, length);
        this.value =  StringCoding.decode(charset, bytes, offset, length);
    }

    /**
     * 通过使用指定的 {@linkplain java.nio.charset.Charset charset} 解码指定的 byte 数组，
     * 构造一个新的 {@code String}。
     * 新 {@code String} 的长度是字符集的函数，因此可能不等于 byte 数组的长度。
     *
     * <p> 当给定 byte 在给定字符集中无效的情况下，此构造方法的行为没有指定。
     * 如果需要对解码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetDecoder} 类。
     *
     * @param  bytes
     *         要解码为字符的 byte
     *
     * @param  charsetName
     *         受支持的 {@linkplain java.nio.charset.Charset charset} 的名称
     *
     * @throws  UnsupportedEncodingException
     *          如果指定字符集不受支持
     *
     * @since  JDK1.1
     */
    public String(byte bytes[], String charsetName)
            throws UnsupportedEncodingException {
        this(bytes, 0, bytes.length, charsetName);
    }

    /**
     * 通过使用指定的 {@linkplain java.nio.charset.Charset charset} 解码指定的 byte 数组，
     * 构造一个新的 {@code String}。
     * 新 {@code String} 的长度是字符集的函数，因此可能不等于 byte 数组的长度。
     *
     * <p> 此方法总是使用此字符集的默认替代字符串替代错误输入和不可映射字符序列。
     * 如果需要对解码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetDecoder} 类。
     *
     * @param  bytes
     *         要解码为字符的 byte
     *
     * @param  charset
     *         要用来解码 {@code bytes} 的 {@linkplain java.nio.charset.Charset charset}
     *
     * @since  1.6
     */
    public String(byte bytes[], Charset charset) {
        this(bytes, 0, bytes.length, charset);
    }

    /**
     * 通过使用平台的默认字符集解码指定的 byte 子数组，构造一个新的 {@code String}。
     * 新 {@code String} 的长度是字符集的函数，因此可能不等于该子数组的长度。
     *
     * <p> 当给定 byte 在给定字符集中无效的情况下，此构造方法的行为没有指定。
     * 如果需要对解码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetDecoder} 类。
     *
     * @param  bytes
     *         要解码为字符的 byte
     *
     * @param  offset
     *         要解码的第一个 byte 的索引
     *
     * @param  length
     *         要解码的 byte 数
     *
     * @throws  IndexOutOfBoundsException
     *          如果 {@code offset} 和 {@code length} 参数索引字符超出 {@code bytes} 数组的范围
     *
     * @since  JDK1.1
     */
    public String(byte bytes[], int offset, int length) {
        checkBounds(bytes, offset, length);
        this.value = StringCoding.decode(bytes, offset, length);
    }

    /**
     * 通过使用平台的默认字符集解码指定的 byte 子数组，构造一个新的 {@code String}。
     * 新 {@code String} 的长度是字符集的函数，因此可能不等于该子数组的长度。
     *
     * <p> 当给定 byte 在给定字符集中无效的情况下，此构造方法的行为没有指定。
     * 如果需要对解码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetDecoder} 类。
     *
     * @param  bytes
     *         要解码为字符的 byte
     *
     * @since  JDK1.1
     */
    public String(byte bytes[]) {
        this(bytes, 0, bytes.length);
    }

    /**
     * 分配一个新的字符串，它包含字符串缓冲区参数中当前包含的字符序列。
     * 该字符串缓冲区的内容已被复制，后续对它的修改不会影响新创建的字符串。
     *
     * @param  buffer
     *         A {@code StringBuffer}
     */
    public String(StringBuffer buffer) {
        synchronized(buffer) {
            this.value = Arrays.copyOf(buffer.getValue(), buffer.length());
        }
    }

    /**
     * 分配一个新的字符串，它包含字符串生成器参数中当前包含的字符序列。
     * 该字符串生成器的内容已被复制，后续对它的修改不会影响新创建的字符串。
     *
     * <p> 提供此构造方法是为了简化到 {@code StringBuilder} 的迁移。
     * 通过 {@code toString} 方法从字符串生成器中获取字符串可能运行的更快，因此通常作为首选。
     *
     * @param   builder
     *          一个 {@code StringBuilder}
     *
     * @since  1.5
     */
    public String(StringBuilder builder) {
        this.value = Arrays.copyOf(builder.getValue(), builder.length());
    }

    /*
    * 打包私有构造函数，共享值数组以获得速度。
    * 该构造函数期望使用总是 share==true 来调用。
    * 我们需要一个单独的构造函数，
    * 因为我们已经有了一个公共的构造函数 String(char[])，它可以复制给定的char[]。
    * Package private constructor which shares value array for speed.
    * this constructor is always expected to be called with share==true.
    * a separate constructor is needed because we already have a public
    * String(char[]) constructor that makes a copy of the given char[].
    */
    String(char[] value, boolean share) {
        // assert share : "unshared not supported";
        this.value = value;
    }

    /**
     * 返回此字符串的长度。
     * 长度等于字符串中 <a href="Character.html#unicode">Unicode 代码单元</a> 的数量。
     *
     * @return  此对象表示的字符序列的长度。
     */
    public int length() {
        return value.length;
    }

    /**
     * 当且仅当 {@link #length()} 为 {@code 0} 时返回 {@code true}。
     * Returns {@code true} if, and only if, {@link #length()} is {@code 0}.
     *
     * @return 如果 {@link #length()} 为 0，则返回 {@code true}；否则返回 {@code false}。
     *
     * @since 1.6
     */
    public boolean isEmpty() {
        return value.length == 0;
    }

    /**
     * 返回指定索引处的 {@code char} 值。索引范围为从 {@code 0} 到 {@code length() - 1}。
     * 序列的第一个 {@code char} 值位于索引 {@code 0} 处，第二个位于索引 {@code 1} 处，
     * 依此类推，这类似于数组索引。
     *
     * <p>如果索引指定的 {@code char} 值是 <a href="Character.html#unicode">代理项</a>，
     * 则返回代理项值。
     *
     * @param      index   {@code char} 值的索引v。
     * @return     此字符串指定索引处的 {@code char} 值。
     *             第一个 {@code char} 值位于索引 {@code 0} 处。
     * @exception  IndexOutOfBoundsException  如果 {@code index} 参数为负或小于此字符串的长度。
     */
    public char charAt(int index) {
        if ((index < 0) || (index >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }

    /**
     * 返回指定索引处的字符（Unicode 代码点）。
     * 索引引用 {@code char} 值（Unicode 代码单元），其范围从从 {@code 0} 到 {@link #length()}{@code  - 1}。
     *
     * <p> 如果给定索引指定的 {@code char} 值属于高代理项范围，则后续索引小于此 {@code String} 的长度；
     * 如果后续索引处的 {@code char} 值属于低代理项范围，则返回该代理项对相应的增补代码点。
     * 否则，返回给定索引处的 {@code char} 值。
     *
     * @param      index {@code char} 值的索引
     * @return     {@code index} 处字符的代码点值
     * @exception  IndexOutOfBoundsException   如果 {@code index} 参数为负或小于此字符串的长度。
     * @since      1.5
     */
    public int codePointAt(int index) {
        if ((index < 0) || (index >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return Character.codePointAtImpl(value, index, value.length);
    }

    /**
     * 返回指定索引之前的字符（Unicode 代码点）。
     * 索引引用 {@code char} 值（Unicode 代码单元），
     * 其范围从从 {@code 1} 到 {@link CharSequence#length() length}。
     *
     * <p> 如果 {@code (index - 1)} 处的 {@code char} 值属于低代理项范围，
     * 则 {@code (index - 2)} 为非负；
     * 如果 {@code (index - 2)} 处的 {@code char} 值属于高低理项范围，
     * 则返回该代理项对的增补代码点值。
     * 如果 {@code index - 1} 处的 {@code char} 值是未配对的低（高）代理项，
     * 则返回代理项值。
     *
     * @param     index 应返回的代码点之后的索引
     * @return    给定索引前面的 Unicode 代码点。
     * @exception IndexOutOfBoundsException 如果 {@code index} 参数小于 1 或大于此字符串的长度。
     * @since     1.5
     */
    public int codePointBefore(int index) {
        int i = index - 1;
        if ((i < 0) || (i >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return Character.codePointBeforeImpl(value, index, 0);
    }

    /**
     * 返回此 {@code String} 的指定文本范围中的 Unicode 代码点数。
     * 文本范围始于指定的 {@code beginIndex}，一直到索引 {@code endIndex - 1} 处的 {@code char}。
     * 因此，该文本范围的长度（用 {@code char} 表示）是 {@code endIndex-beginIndex}。
     * 该文本范围内每个未配对的代理项计为一个代码点。
     *
     * @param beginIndex 文本范围的第一个 {@code char} 的索引。
     * @param endIndex 文本范围的最后一个 {@code char} 之后的索引。
     * @return 指定文本范围中 Unicode 代码点的数量
     * @exception IndexOutOfBoundsException 如果 {@code beginIndex} 为负，
     * 或 {@code endIndex} 大于此 {@code String} 的长度，或 {@code beginIndex} 大于 {@code endIndex}。
     * @since  1.5
     */
    public int codePointCount(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > value.length || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        return Character.codePointCountImpl(value, beginIndex, endIndex - beginIndex);
    }

    /**
     * 返回此 {@code String} 中从给定的 {@code index} 处偏移 {@code codePointOffset} 个代码点的索引。
     * 文本范围内由 {@code index} 和 {@code codePointOffset} 给定的未配对代理项各计为一个代码点。
     *
     * @param index 要偏移的索引
     * @param codePointOffset 代码点中的偏移量
     * @return {@code String} 的索引
     * @exception IndexOutOfBoundsException 如果 {@code index} 为负或大于此 {@code String} 的长度；
     * 或者 {@code codePointOffset} 为正，
     * 且以 {@code index} 开头子字符串的代码点比 {@code codePointOffset} 少；
     * 如果 {@code codePointOffset} 为负，
     * 且 {@code index} 前面子字符串的代码点比 {@code codePointOffset} 的绝对值少。
     * @since 1.5
     */
    public int offsetByCodePoints(int index, int codePointOffset) {
        if (index < 0 || index > value.length) {
            throw new IndexOutOfBoundsException();
        }
        return Character.offsetByCodePointsImpl(value, 0, value.length,
                index, codePointOffset);
    }

    /**
     * 将该字符串中的字符从 dstBegin 开始复制到 dst 中。
     * 此方法不执行任何范围检查
     */
    void getChars(char dst[], int dstBegin) {
        System.arraycopy(value, 0, dst, dstBegin, value.length);
    }

    /**
     * 将字符从此字符串复制到目标字符数组。
     * <p>
     * 要复制的第一个字符位于索引 {@code srcBegin} 处；
     * 要复制的最后一个字符位于索引 {@code srcEnd-1} 处
     * （因此要复制的字符总数是 {@code srcEnd-srcBegin}）。
     * 要复制到 dst 子数组的字符从索引 {@code dstBegin} 处开始，并结束于索引：
     * <blockquote><pre>
     *     dstBegin + (srcEnd-srcBegin) - 1
     * </pre></blockquote>
     *
     * @param      srcBegin   字符串中要复制的第一个字符的索引。
     * @param      srcEnd     字符串中要复制的最后一个字符之后的索引。
     * @param      dst        目标数组。
     * @param      dstBegin   目标数组中的起始偏移量。
     * @exception IndexOutOfBoundsException 如果下列任何一项为 true：
     *            <ul><li>{@code srcBegin} 为负。
     *            <li>{@code srcBegin} 大于 {@code srcEnd}
     *            <li>{@code srcEnd} 大于此字符串的长度
     *            <li>{@code dstBegin} 为负
     *            <li>{@code dstBegin+(srcEnd-srcBegin)} 大于 {@code dst.length}</ul>
     */
    public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd > value.length) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
        }
        System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

    /**
     * 将字符从此字符串复制到目标 byte 数组中。
     * 每个 byte 接收相应字符的 8 个低位。不复制每个字符的高位，它们不参与任何方式的转换。
     *
     * <p>要复制的第一个字符位于索引 {@code srcBegin} 处；
     * 要复制的最后一个字符位于索引 {@code srcEnd-1} 处
     * （因此要复制的字符总数是 {@code srcEnd-srcBegin}）。
     * 要复制到 dst 子数组的字符从索引 {@code dstBegin} 处开始，并结束于索引：
     * <blockquote><pre>
     *     dstBegin + (srcEnd-srcBegin) - 1
     * </pre></blockquote>
     *
     * @deprecated  该方法无法将字符正确转换为字节。
     * 从 JDK&nbsp;1.1 起，完成该转换的首选方法是通过 {@link #getBytes()} 方法，
     * 该方法使用平台的默认字符集。
     *
     * @param  srcBegin
     *         字符串中要复制的第一个字符的索引
     *
     * @param  srcEnd
     *         字符串中要复制的第一个字符的索引
     *
     * @param  dst
     *         目标数组
     *
     * @param  dstBegin
     *         目标数组中的起始偏移量
     *
     * @throws  IndexOutOfBoundsException
     *          如果下列任何一项为 true：
     *          <ul>
     *            <li>{@code srcBegin} 为负。
     *            <li>{@code srcBegin} 大于 {@code srcEnd}
     *            <li>{@code srcEnd} 大于此字符串的长度
     *            <li>{@code dstBegin} 为负
     *            <li>{@code dstBegin+(srcEnd-srcBegin)} 大于 {@code dst.length}
     *          </ul>
     */
    @Deprecated
    public void getBytes(int srcBegin, int srcEnd, byte dst[], int dstBegin) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd > value.length) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
        }
        Objects.requireNonNull(dst);

        int j = dstBegin;
        int n = srcEnd;
        int i = srcBegin;
        char[] val = value;   /* avoid getfield opcode */

        while (i < n) {
            dst[j++] = (byte)val[i++];
        }
    }

    /**
     * 使用指定的字符集将此 {@code String} 编码为 byte 序列，
     * 并将结果存储到一个新的 byte 数组中。
     *
     * <p> 当此字符串不能使用给定的字符集编码时，此方法的行为没有指定。
     * 如果需要对编码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetEncoder} 类。
     *
     * @param  charsetName
     *          受支持的 {@linkplain java.nio.charset.Charset charset} 名称
     *
     * @return  所得 byte 数组
     *
     * @throws  UnsupportedEncodingException
     *           如果指定的字符集不受支持
     *
     * @since  JDK1.1
     */
    public byte[] getBytes(String charsetName)
            throws UnsupportedEncodingException {
        if (charsetName == null) throw new NullPointerException();
        return StringCoding.encode(charsetName, value, 0, value.length);
    }

    /**
     * 使用给定的 {@linkplain java.nio.charset.Charset charset}
     * 将此 {@code String} 编码到 byte 序列，并将结果存储到新的 byte 数组。
     *
     * <p> 此方法总是使用此字符集的默认替代 byte 数组替代错误输入和不可映射字符序列。
     * 如果需要对编码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetEncoder} 类。
     *
     * @param  charset
     *         用于编码 {@code String} 的 {@linkplain java.nio.charset.Charset}
     *
     * @return  所得 byte 数组
     *
     * @since  1.6
     */
    public byte[] getBytes(Charset charset) {
        if (charset == null) throw new NullPointerException();
        return StringCoding.encode(charset, value, 0, value.length);
    }

    /**
     * 使用平台的默认字符集将此 {@code String} 编码为 byte 序列，
     * 并将结果存储到一个新的 byte 数组中。
     *
     * <p> 当此字符串不能使用给定的字符集编码时，此方法的行为没有指定。
     * 如果需要对编码过程进行更多控制，则应该使用 {@link java.nio.charset.CharsetEncoder} 类。
     *
     * @return  所得 byte 数组
     *
     * @since      JDK1.1
     */
    public byte[] getBytes() {
        return StringCoding.encode(value, 0, value.length);
    }

    /**
     * 将此字符串与指定的对象比较。当且仅当该参数不为 {@code null}，
     * 并且是与此对象表示相同字符序列的 {@code String} 对象时，结果才为 {@code true}。
     *
     * @param  anObject
     *         与此 {@code String} 进行比较的对象。
     *
     * @return  如果给定对象表示的 {@code String} 与此 {@code String} 相等，
     * 则返回 {@code true}；否则返回 {@code false}。
     *
     * @see  #compareTo(String)
     * @see  #equalsIgnoreCase(String)
     */
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 将此字符串与指定的 {@code StringBuffer} 比较。
     * 当且仅当此 {@code String} 与指定 {@code StringBuffer} 表示相同的字符序列时，
     * 结果才为 {@code true}。
     *
     * @param  sb
     *         要与此 {@code String} 比较的 {@code StringBuffer}
     *
     * @return  如果此 {@code String} 与指定 {@code StringBuffer} 表示相同的字符序列，
     * 则返回 {@code true}；否则返回 {@code false}。
     *
     * @since  1.4
     */
    public boolean contentEquals(StringBuffer sb) {
        return contentEquals((CharSequence)sb);
    }

    private boolean nonSyncContentEquals(AbstractStringBuilder sb) {
        char v1[] = value;
        char v2[] = sb.getValue();
        int n = v1.length;
        if (n != sb.length()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (v1[i] != v2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将此字符串与指定的 {@code CharSequence} 比较。
     * 当且仅当此 {@code String} 与指定序列表示相同的 char 值序列时，结果才为 {@code true}。
     *
     * @param  cs
     *         要与此 {@code String} 比较的序列
     *
     * @return  如果此 {@code String} 与指定序列表示相同的 char 值序列，
     * 则返回 {@code true}；否则返回 {@code false}。
     *
     * @since  1.5
     */
    public boolean contentEquals(CharSequence cs) {
        // Argument is a StringBuffer, StringBuilder
        if (cs instanceof AbstractStringBuilder) {
            if (cs instanceof StringBuffer) {
                synchronized(cs) {
                   return nonSyncContentEquals((AbstractStringBuilder)cs);
                }
            } else {
                return nonSyncContentEquals((AbstractStringBuilder)cs);
            }
        }
        // Argument is a String
        if (cs instanceof String) {
            return equals(cs);
        }
        // Argument is a generic CharSequence
        char v1[] = value;
        int n = v1.length;
        if (n != cs.length()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (v1[i] != cs.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将此 {@code String} 与另一个 {@code String} 比较，不考虑大小写。
     * 如果两个字符串的长度相同，并且其中的相应字符都相等（忽略大小写），则认为这两个字符串是相等的。
     *
     * <p> 在忽略大小写的情况下，如果下列至少一项为 true，
     * 则认为 {@code c1} 和 {@code c2} 这两个字符相同：
     * <ul>
     *   <li> 这两个字符相同（使用 {@code ==} 运算符进行比较）
     *   <li> 对每个字符应用方法 {@link java.lang.Character#toUpperCase(char)}
     *        生成相同的结果
     *   <li> 对每个字符应用方法 {@link java.lang.Character#toLowerCase(char)}
     *        生成相同的结果
     * </ul>
     *
     * @param  anotherString
     *         与此 {@code String} 进行比较的{@code String}
     *
     * @return  如果参数不为 {@code null}，且这两个 {@code String} 相等（忽略大小写），
     * 则返回 {@code true}；否则返回 {@code false}。
     *
     * @see  #equals(Object)
     */
    public boolean equalsIgnoreCase(String anotherString) {
        return (this == anotherString) ? true
                : (anotherString != null)
                && (anotherString.value.length == value.length)
                && regionMatches(true, 0, anotherString, 0, value.length);
    }

    /**
     * 按字典顺序比较两个字符串。该比较基于字符串中各个字符的 Unicode 值。
     * 按字典顺序将此 {@code String} 对象表示的字符序列与参数字符串所表示的字符序列进行比较。
     * 如果按字典顺序此 {@code String} 对象位于参数字符串之前，则比较结果为一个负整数。
     * 如果按字典顺序此 {@code String} 对象位于参数字符串之后，则比较结果为一个正整数。
     * 如果这两个字符串相等，则结果为零；
     * {@code compareTo} 只在方法 {@link #equals(Object)} 返回 {@code true} 时才返回 {@code 0}。
     * <p>
     * 这是字典排序的定义。
     * 如果这两个字符串不同，那么它们要么在某个索引处的字符不同（该索引对二者均为有效索引），
     * 要么长度不同，或者同时具备这两种情况。
     * 如果它们在一个或多个索引位置上的字符不同，假设 <i>k</i> 是这类索引的最小值；
     * 则在位置 <i>k</i> 上具有较小值的那个字符串（使用 &lt; 运算符确定），
     * 其字典顺序在其他字符串之前。
     * 在这种情况下，{@code compareTo} 返回这两个字符串在位置 {@code k} 处两个char 值的差，即值：
     * <blockquote><pre>
     * this.charAt(k)-anotherString.charAt(k)
     * </pre></blockquote>
     * 如果没有字符不同的索引位置，则较短字符串的字典顺序在较长字符串之前。
     * 在这种情况下， {@code compareTo} 返回这两个字符串长度的差，即值：
     * <blockquote><pre>
     * this.length()-anotherString.length()
     * </pre></blockquote>
     *
     * @param   anotherString   要比较的 {@code String}。
     * @return  如果参数字符串等于此字符串，则返回值 {@code 0}；
     *           如果此字符串按字典顺序小于字符串参数，则返回一个小于 {@code 0} 的值；
     *           如果此字符串按字典顺序大于字符串参数，则返回一个大于 {@code 0} 的值。
     */
    public int compareTo(String anotherString) {
        int len1 = value.length;
        int len2 = anotherString.value.length;
        int lim = Math.min(len1, len2);
        char v1[] = value;
        char v2[] = anotherString.value;

        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }

    /**
     * 一个对 {@code String} 对象进行排序的 Comparator，
     * 作用与 {@code compareToIgnoreCase} 相同。此比较器是可序列化的。
     * <p>
     * 注意，Comparator <em>不</em> 考虑语言环境，
     * 因此可能导致在某些语言环境中的排序效果不理想。
     * java.text 包提供 <em>Collators</em> 完成与语言环境有关的排序。
     *
     * @see     java.text.Collator#compare(String, String)
     * @since   1.2
     */
    public static final Comparator<String> CASE_INSENSITIVE_ORDER
                                         = new CaseInsensitiveComparator();
    private static class CaseInsensitiveComparator
            implements Comparator<String>, java.io.Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability
        private static final long serialVersionUID = 8575799808933029326L;

        public int compare(String s1, String s2) {
            int n1 = s1.length();
            int n2 = s2.length();
            int min = Math.min(n1, n2);
            for (int i = 0; i < min; i++) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(i);
                if (c1 != c2) {
                    c1 = Character.toUpperCase(c1);
                    c2 = Character.toUpperCase(c2);
                    if (c1 != c2) {
                        c1 = Character.toLowerCase(c1);
                        c2 = Character.toLowerCase(c2);
                        if (c1 != c2) {
                            // No overflow because of numeric promotion
                            return c1 - c2;
                        }
                    }
                }
            }
            return n1 - n2;
        }

        /** Replaces the de-serialized object. */
        private Object readResolve() { return CASE_INSENSITIVE_ORDER; }
    }

    /**
     * 按字典顺序比较两个字符串，不考虑大小写。
     * 此方法返回一个整数，其符号与使用规范化的字符串调用 {@code compareTo} 所得符号相同，
     * 规范化字符串的大小写差异已通过对每个字符调用
     * {@code Character.toLowerCase(Character.toUpperCase(character))}
     * 消除。
     * <p>
     * 注意，此方法不 考虑语言环境，因此可能导致在某些语言环境中的排序效果不理想。
     * java.text 包提供 <em>collators</em> 完成与语言环境有关的排序。
     *
     * @param   str   要比较的 {@code String}。
     * @return  根据指定 String 大于、等于还是小于此 String（不考虑大小写），
     *          分别返回一个负整数、0 或一个正整数。
     * @see     java.text.Collator#compare(String, String)
     * @since   1.2
     */
    public int compareToIgnoreCase(String str) {
        return CASE_INSENSITIVE_ORDER.compare(this, str);
    }

    /**
     * 测试两个字符串区域是否相等。
     * <p>
     * 将此 {@code String} 对象的一个子字符串与参数 other 的一个子字符串进行比较。
     * 如果这两个子字符串表示相同的字符序列，则结果为 true。
     * 要比较的此 {@code String} 对象的子字符串从索引 {@code toffset} 处开始，长度为 {@code len}。
     * 要比较的 other 的子字符串从索引 {@code ooffset} 处开始，长度为 {@code len}。
     * 当且仅当下列至少一项为 true 时，结果才为 {@code false} ：
     * <ul><li>{@code toffset} 为负。
     * <li>{@code ooffset} 为负。
     * <li>{@code toffset+len} 大于此 {@code String} 对象的长度。
     * <li>{@code ooffset+len} 大于另一个参数的长度。
     * <li>存在某个小于 {@code len} 的非负整数 <i>k</i>，它满足：
     * {@code this.charAt(toffset + }<i>k</i>{@code ) != other.charAt(ooffset + }
     * <i>k</i>{@code )}
     * </ul>
     *
     * @param   toffset   字符串中子区域的起始偏移量。
     * @param   other     字符串参数。
     * @param   ooffset   字符串参数中子区域的起始偏移量。
     * @param   len       要比较的字符数。
     * @return  如果此字符串的指定子区域完全匹配字符串参数的指定子区域，则返回{@code true}；
     *          否则返回 {@code false}。
     */
    public boolean regionMatches(int toffset, String other, int ooffset,
            int len) {
        char ta[] = value;
        int to = toffset;
        char pa[] = other.value;
        int po = ooffset;
        // Note: toffset, ooffset, or len might be near -1>>>1.
        if ((ooffset < 0) || (toffset < 0)
                || (toffset > (long)value.length - len)
                || (ooffset > (long)other.value.length - len)) {
            return false;
        }
        while (len-- > 0) {
            if (ta[to++] != pa[po++]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试两个字符串区域是否相等。
     * <p>
     * 将此 {@code String} 对象的一个子字符串与参数 {@code other} 的一个子字符串进行比较。
     * 如果这两个子字符串表示相同的字符序列，则结果为 {@code true} ，
     * 当且仅当 {@code ignoreCase} 为 true 时忽略大小写。
     * 要比较的此 {@code String} 对象的子字符串从索引 {@code toffset} 处开始，长度为 {@code len}。
     * 要比较的 {@code other} 的子字符串从索引 {@code ooffset} 处开始，长度为 {@code len}。
     * 当且仅当下列至少一项为 true 时，结果才为 {@code false} ：
     * <ul><li>{@code toffset} 为负。
     * <li>{@code ooffset} 为负。
     * <li>{@code toffset+len} 大于此 {@code String} 对象的长度。
     * <li>{@code ooffset+len} 大于另一个参数的长度。
     * <li>{@code ignoreCase} 为 {@code false}，且存在某个小于 {@code len}
     * 的非负整数 <i>k</i>，即：
     * <blockquote><pre>
     * this.charAt(toffset+k) != other.charAt(ooffset+k)
     * </pre></blockquote>
     * <li>{@code ignoreCase} 为 {@code true}，且存在某个小于 {@code len} and there is some nonnegative
     * 的非负整数 <i>k</i>，即：
     * <blockquote><pre>
     * Character.toLowerCase(this.charAt(toffset+k)) !=
     Character.toLowerCase(other.charAt(ooffset+k))
     * </pre></blockquote>
     * 以及：
     * <blockquote><pre>
     * Character.toUpperCase(this.charAt(toffset+k)) !=
     *         Character.toUpperCase(other.charAt(ooffset+k))
     * </pre></blockquote>
     * </ul>
     *
     * @param   ignoreCase   如果为 {@code true}，则比较字符时忽略大小写。
     * @param   toffset      此字符串中子区域的起始偏移量。
     * @param   other        字符串参数。
     * @param   ooffset      字符串参数中子区域的起始偏移量。
     * @param   len          要比较的字符数。
     * @return  如果此字符串的指定子区域匹配字符串参数的指定子区域，
     *          则返回 {@code true}；否则返回 {@code false}。
     *          是否完全匹配或考虑大小写取决于 {@code ignoreCase} 参数。
     */
    public boolean regionMatches(boolean ignoreCase, int toffset,
            String other, int ooffset, int len) {
        char ta[] = value;
        int to = toffset;
        char pa[] = other.value;
        int po = ooffset;
        // Note: toffset, ooffset, or len might be near -1>>>1.
        if ((ooffset < 0) || (toffset < 0)
                || (toffset > (long)value.length - len)
                || (ooffset > (long)other.value.length - len)) {
            return false;
        }
        while (len-- > 0) {
            char c1 = ta[to++];
            char c2 = pa[po++];
            if (c1 == c2) {
                continue;
            }
            if (ignoreCase) {
                // If characters don't match but case may be ignored,
                // try converting both characters to uppercase.
                // If the results match, then the comparison scan should
                // continue.
                char u1 = Character.toUpperCase(c1);
                char u2 = Character.toUpperCase(c2);
                if (u1 == u2) {
                    continue;
                }
                // Unfortunately, conversion to uppercase does not work properly
                // for the Georgian alphabet, which has strange rules about case
                // conversion.  So we need to make one last check before
                // exiting.
                if (Character.toLowerCase(u1) == Character.toLowerCase(u2)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 测试此字符串从指定索引开始的子字符串是否以指定前缀开始。
     *
     * @param   prefix    前缀。
     * @param   toffset   在此字符串中开始查找的位置。
     * @return  如果参数表示的字符序列是此对象从索引 {@code toffset} 处开始的子字符串前缀，
     *          则返回 {@code true}；否则返回 {@code false}。
     *          如果 {@code toffset} 为负或大于此 {@code String} 对象的长度，
     *          则结果为 {@code false}；否则结果与以下表达式的结果相同：
     *          <pre>
     *          this.substring(toffset).startsWith(prefix)
     *          </pre>
     */
    public boolean startsWith(String prefix, int toffset) {
        char ta[] = value;
        int to = toffset;
        char pa[] = prefix.value;
        int po = 0;
        int pc = prefix.value.length;
        // Note: toffset might be near -1>>>1.
        if ((toffset < 0) || (toffset > value.length - pc)) {
            return false;
        }
        while (--pc >= 0) {
            if (ta[to++] != pa[po++]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试此字符串是否以指定的前缀开始。
     *
     * @param   prefix   前缀。
     * @return  如果参数表示的字符序列是此字符串表示的字符序列的前缀，
     *          则返回 {@code true}；否则返回 {@code false}。
     *          还要注意，如果参数是空字符串，或者等于此 {@code String} 对象（用 {@link #equals(Object)} 方法确定），
     *          则返回 {@code true}。
     * @since   1. 0
     */
    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0);
    }

    /**
     * 测试此字符串是否以指定的后缀结束。
     *
     * @param   suffix   后缀。
     * @return  如果参数表示的字符序列是此对象表示的字符序列的后缀，
     *          则返回 {@code true}；否则返回 {@code false}。
     *          注意，如果参数是空字符串，或者等于此 {@code String} 对象（用 {@link #equals(Object)} 方法确定），
     *          则返回 {@code true}。
     */
    public boolean endsWith(String suffix) {
        return startsWith(suffix, value.length - suffix.value.length);
    }

    /**
     * 返回此字符串的哈希码。 {@code String} 对象的哈希码根据以下公式计算：
     * <blockquote><pre>
     * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
     * </pre></blockquote>
     * 使用 {@code int} 算法，这里 {@code s[i]} 是字符串的第 <i>i</i>
     * 个字符，{@code n} 是字符串的长度，{@code ^} 表示求幂。
     * （空字符串的哈希值为0。）
     *
     * @return  此对象的哈希码值。
     */
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

    /**
     * 返回指定字符在此字符串中第一次出现处的索引。
     * 如果在此 {@code String} 对象表示的字符序列中出现值为 {@code ch} 的字符，
     * 则返回第一次出现该字符的索引（以 Unicode 代码单元表示）。
     * 对于 0 到 0xFFFF（包括 0 和 0xFFFF）范围内的 ch 的值，返回值是
     * <blockquote><pre>
     * this.charAt(<i>k</i>) == ch
     * </pre></blockquote>
     * 为 true 的最小 <i>k</i> 值。
     * 对于其他 {@code ch} 值，返回值是
     * <blockquote><pre>
     * this.codePointAt(<i>k</i>) == ch
     * </pre></blockquote>
     * 为 true 的最小 <i>k</i> 值。
     * 无论哪种情况，如果此字符串中没有这样的字符，则返回 {@code -1}。
     *
     * @param   ch   个字符（Unicode 代码点）。
     * @return  在此对象表示的字符序列中第一次出现该字符的索引；
     *          如果未出现该字符，则返回 {@code -1}。
     */
    public int indexOf(int ch) {
        return indexOf(ch, 0);
    }

    /**
     * 返回在此字符串中第一次出现指定字符处的索引，从指定的索引开始搜索。
     * <p>
     * 在此 {@code String} 对象表示的字符序列中，
     * 如果带有值 {@code ch} 的字符的索引不小于 {@code fromIndex}，
     * 则返回第一次出现该值的索引。对于 0 到 0xFFFF（包括 0 和 0xFFFF）范围内的 {@code ch} 值，返回值是
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &gt;= fromIndex)
     * </pre></blockquote>
     * 为 true 的最小<i>k</i>值。
     * 对于其他 {@code ch} 值，返回值是
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &gt;= fromIndex)
     * </pre></blockquote>
     * 为 true 的最小 <i>k</i> 值。
     * 无论哪种情况，
     * 如果此字符串中 {@code fromIndex} 或之后的位置没有这样的字符出现，则返回 {@code -1}。
     *
     * <p>
     * {@code fromIndex} 的值没有限制。如果它为负，则与它为 0 的效果同样：将搜索整个字符串。
     * 如果它大于此字符串的长度，则与它等于此字符串长度的效果相同：返回 {@code -1}。
     *
     * <p>所有索引都在 {@code char} 值中指定（Unicode 代码单元）。
     *
     * @param   ch          一个字符（Unicode 代码点）。
     * @param   fromIndex   开始搜索的索引。
     * @return 在此对象表示的字符序列中第一次出现的大于或等于 {@code fromIndex} 的字符的索引；
     *         如果未出现该字符，则返回 {@code -1}。
     */
    public int indexOf(int ch, int fromIndex) {
        final int max = value.length;
        if (fromIndex < 0) {
            fromIndex = 0;
        } else if (fromIndex >= max) {
            // Note: fromIndex might be near -1>>>1.
            return -1;
        }

        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            final char[] value = this.value;
            for (int i = fromIndex; i < max; i++) {
                if (value[i] == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            return indexOfSupplementary(ch, fromIndex);
        }
    }

    /**
     * Handles (rare) calls of indexOf with a supplementary character.
     */
    private int indexOfSupplementary(int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            final char[] value = this.value;
            final char hi = Character.highSurrogate(ch);
            final char lo = Character.lowSurrogate(ch);
            final int max = value.length - 1;
            for (int i = fromIndex; i < max; i++) {
                if (value[i] == hi && value[i + 1] == lo) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 返回指定字符在此字符串中最后一次出现处的索引。
     * 对于 0 到 0xFFFF（包括 0 和 0xFFFF）范围内的 {@code ch} 的值，
     * 返回的索引（Unicode 代码单元）是
     * <blockquote><pre>
     * this.charAt(<i>k</i>) == ch
     * </pre></blockquote>
     * 为 true 的最大<i>k</i>值。
     * 对于其他 {@code ch} 值，返回值是
     * <blockquote><pre>
     * this.codePointAt(<i>k</i>) == ch
     * </pre></blockquote>
     * 为 true 的最大<i>k</i>值。
     * 无论哪种情况，如果此字符串中没有这样的字符出现，则返回 {@code -1}。
     * 从最后一个字符开始反向搜索此 {@code String}。
     *
     * @param   ch   一个字符（Unicode 代码点）。
     * @return  在此对象表示的字符序列中最后一次出现该字符的索引；
     *          如果未出现该字符，则返回 {@code -1}。
     */
    public int lastIndexOf(int ch) {
        return lastIndexOf(ch, value.length - 1);
    }

    /**
     * 返回指定字符在此字符串中最后一次出现处的索引，从指定的索引处开始进行反向搜索。
     * 对于 0 到 0xFFFF（包括 0 和 0xFFFF）范围内的 {@code ch}  值，返回的索引是
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &lt;= fromIndex)
     * </pre></blockquote>
     * 为 true 的最大<i>k</i>值。
     * 对于其他 {@code ch} 值，返回值是
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &lt;= fromIndex)
     * </pre></blockquote>
     * 为 true 的最大<i>k</i>值。
     * 无论哪种情况，如果此字符串中没有这样的字符出现，则返回 {@code -1}。
     *
     * <p>所有的索引都以 {@code char} 值指定（Unicode 代码单元）。
     *
     * @param   ch          一个字符（Unicode 代码点）。
     * @param   fromIndex   开始搜索的索引。 {@code fromIndex} 的值没有限制。
     *          如果它大于等于此字符串的长度，则与它小于此字符串长度减 1 的效果相同：
     *          将搜索整个字符串。如果它为负，则与它为 -1 的效果相同：返回 -1。
     * @return 在此对象表示的字符序列（小于等于 {@code fromIndex}）中最后一次出现该字符的索引；
     *         如果在该点之前未出现该字符，则返回 {@code -1}。
     */
    public int lastIndexOf(int ch, int fromIndex) {
        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            final char[] value = this.value;
            int i = Math.min(fromIndex, value.length - 1);
            for (; i >= 0; i--) {
                if (value[i] == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            return lastIndexOfSupplementary(ch, fromIndex);
        }
    }

    /**
     * Handles (rare) calls of lastIndexOf with a supplementary character.
     */
    private int lastIndexOfSupplementary(int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            final char[] value = this.value;
            char hi = Character.highSurrogate(ch);
            char lo = Character.lowSurrogate(ch);
            int i = Math.min(fromIndex, value.length - 2);
            for (; i >= 0; i--) {
                if (value[i] == hi && value[i + 1] == lo) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 返回指定子字符串在此字符串中第一次出现处的索引。
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     *
     * <p>返回的索引是满足下式的最小 <i>k</i> 值：
     * <blockquote><pre>
     * this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * 如果不存在这样的 <i>k</i> 值，则返回 {@code -1}。
     *
     * @param   str   任意字符串。
     * @return  如果字符串参数作为一个子字符串在此对象中出现，
     *           则返回第一个这种子字符串的第一个字符的索引；
     *           如果它不作为一个子字符串出现，则返回 {@code -1}。
     */
    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    /**
     * 返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
     *
     * <p>返回的索引是满足下式的最小 <i>k</i> 值：
     * <blockquote><pre>
     * <i>k</i> &gt;= fromIndex {@code &&} this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * 如果不存在这样的 <i>k</i> 值，则返回 {@code -1}。
     *
     * @param   str         要搜索的子字符串。
     * @param   fromIndex   开始搜索的索引位置。
     * @return  指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
     *           如果没有这种情况出现，则返回 {@code -1}。
     */
    public int indexOf(String str, int fromIndex) {
        return indexOf(value, 0, value.length,
                str.value, 0, str.value.length, fromIndex);
    }

    /**
     * String 和 AbstractStringBuilder 用于搜索的共享的代码。
     * Code shared by String and AbstractStringBuilder to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   fromIndex    the index to begin searching from.
     */
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
            String target, int fromIndex) {
        return indexOf(source, sourceOffset, sourceCount,
                       target.value, 0, target.value.length,
                       fromIndex);
    }

    /**
     * Code shared by String and StringBuffer to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   targetOffset offset of the target string.
     * @param   targetCount  count of the target string.
     * @param   fromIndex    the index to begin searching from.
     */
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[i] != first) {
                while (++i <= max && source[i] != first);
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                        == target[k]; j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    /**
     * 返回指定子字符串在此字符串中第一次出现处的索引。
     * 空字符 ""  的最后一次出现被认为是在索引值 {@code this.length()} 处出现的。
     *
     * <p>返回的索引是满足下式的最大 <i>k</i> 值：
     * <blockquote><pre>
     * this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * 如果不存在这样的 <i>k</i> 值，则返回 {@code -1}。
     *
     * @param   str  要搜索的子字符串。
     * @return  如果字符串参数作为一个子字符串在此对象中出现一次或多次，
     *           则返回最后一个这种子字符串的第一个字符。
     *           如果它不作为一个子字符串出现，则返回 则返回 {@code -1}。
     */
    public int lastIndexOf(String str) {
        return lastIndexOf(str, value.length);
    }

    /**
     * 返回指定子字符串在此字符串中最后一次出现处的索引，从指定的索引开始反向搜索。
     *
     * <p>返回的整数是满足下式的最大 <i>k</i> 值：
     * <blockquote><pre>
     * <i>k</i> {@code <=} fromIndex {@code &&} this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * 如果不存在这样的 <i>k</i> 值，则返回 {@code -1}。
     *
     * @param   str         要搜索的子字符串。
     * @param   fromIndex   开始搜索的索引位置。
     * @return  指定子字符串在此字符串中最后一次出现处的索引，
     *           从指定的索引开始反向搜索。
     *           如果它不作为一个子字符串出现，则返回 则返回 {@code -1}。
     */
    public int lastIndexOf(String str, int fromIndex) {
        return lastIndexOf(value, 0, value.length,
                str.value, 0, str.value.length, fromIndex);
    }

    /**
     * Code shared by String and AbstractStringBuilder to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   fromIndex    the index to begin searching from.
     */
    static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
            String target, int fromIndex) {
        return lastIndexOf(source, sourceOffset, sourceCount,
                       target.value, 0, target.value.length,
                       fromIndex);
    }

    /**
     * Code shared by String and StringBuffer to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   targetOffset offset of the target string.
     * @param   targetCount  count of the target string.
     * @param   fromIndex    the index to begin searching from.
     */
    static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        /*
         * Check arguments; return immediately where possible. For
         * consistency, don't check for null str.
         */
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }
        /* Empty string always matches. */
        if (targetCount == 0) {
            return fromIndex;
        }

        int strLastIndex = targetOffset + targetCount - 1;
        char strLastChar = target[strLastIndex];
        int min = sourceOffset + targetCount - 1;
        int i = min + fromIndex;

    startSearchForLastChar:
        while (true) {
            while (i >= min && source[i] != strLastChar) {
                i--;
            }
            if (i < min) {
                return -1;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start) {
                if (source[j--] != target[k--]) {
                    i--;
                    continue startSearchForLastChar;
                }
            }
            return start - sourceOffset + 1;
        }
    }

    /**
     * 返回一个新的字符串，它是此字符串的一个子字符串。
     * 该子字符串从指定索引处的字符开始，直到此字符串末尾。
     * <p> 示例：
     * <blockquote><pre>
     * "unhappy".substring(2) returns "happy"
     * "Harbison".substring(3) returns "bison"
     * "emptiness".substring(9) returns "" (an empty string)
     * </pre></blockquote>
     *
     * @param      beginIndex   起始索引（包括）。
     * @return     指定的子字符串。
     * @exception  IndexOutOfBoundsException  如果 {@code beginIndex} 为负或大于此 {@code String} 对象的长度。
     */
    public String substring(int beginIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        int subLen = value.length - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
    }

    /**
     * 返回一个新字符串，它是此字符串的一个子字符串。
     * 该子字符串从指定的 {@code beginIndex} 处开始，直到索引 {@code endIndex - 1} 处的字符。
     * 因此，该子字符串的长度为 {@code endIndex-beginIndex}。
     * <p>
     * 示例：
     * <blockquote><pre>
     * "hamburger".substring(4, 8) returns "urge"
     * "smiles".substring(1, 5) returns "mile"
     * </pre></blockquote>
     *
     * @param      beginIndex   起始索引（包括）。
     * @param      endIndex     结束索引（不包括）。
     * @return     指定的子字符串。
     * @exception  IndexOutOfBoundsException   如果 {@code beginIndex} 为负，或 {@code endIndex} 大于此 {@code String} 对象的长度，
     *              或 {@code beginIndex} 大于 {@code endIndex}。
     */
    public String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > value.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        return ((beginIndex == 0) && (endIndex == value.length)) ? this
                : new String(value, beginIndex, subLen);
    }

    /**
     * 返回一个新的字符序列，它是此序列的一个子序列。
     *
     * <p> 此方法这种形式的调用：
     *
     * <blockquote><pre>
     * str.subSequence(begin,&nbsp;end)</pre></blockquote>
     *
     * 与以下调用的行为完全相同：
     *
     * <blockquote><pre>
     * str.substring(begin,&nbsp;end)</pre></blockquote>
     *
     * @apiNote
     * 定义此方法使 {@code String} 类能够实现 {@link CharSequence} 接口。
     *
     * @param   beginIndex   起始索引（包括）。
     * @param   endIndex     结束索引（不包括）。
     * @return  指定子序列。
     *
     * @throws  IndexOutOfBoundsException
     *          如果 {@code beginIndex} 或 endIndex 为负，
     *          如果 {@code endIndex} 大于 {@code length()}
     *          或 {@code beginIndex} 大于 {@code endIndex}
     *
     * @since 1.4
     * @spec JSR-51
     */
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return this.substring(beginIndex, endIndex);
    }

    /**
     * 将指定字符串连接到此字符串的结尾。
     * <p>
     * 如果参数字符串的长度为 {@code 0}，则返回此 {@code String} 对象。
     * 否则，创建一个新的 {@code String} 对象，
     * 用来表示由此 {@code String} 对象表示的字符序列和
     * 参数字符串表示的字符序列连接而成的字符序列。<p>
     * 示例：
     * <blockquote><pre>
     * "cares".concat("s") returns "caress"
     * "to".concat("get").concat("her") returns "together"
     * </pre></blockquote>
     *
     * @param   str   连接到此 {@code String} 结尾的 {@code String}。
     * @return  一个字符串，它表示在此对象字符后连接字符串参数字符而成的字符。
     */
    public String concat(String str) {
        int otherLen = str.length();
        if (otherLen == 0) {
            return this;
        }
        int len = value.length;
        char buf[] = Arrays.copyOf(value, len + otherLen);
        str.getChars(buf, len);
        return new String(buf, true);
    }

    /**
     * 返回一个新的字符串，
     * 它是通过用 {@code newChar} 替换此字符串中出现的所有 {@code oldChar} 得到的。
     * <p>
     * 如果 {@code oldChar} 在此 {@code String} 对象表示的字符序列中没有出现，
     * 则返回对此 {@code String} 对象的引用。
     * 否则，创建一个新的 {@code String} 对象，
     * 它所表示的字符序列除了所有的 {@code oldChar} 都被替换为 {@code newChar} 之外，
     * 与此 {@code String} 对象表示的字符序列相同。
     * <p>
     * 示例：
     * <blockquote><pre>
     * "mesquite in your cellar".replace('e', 'o')
     *         returns "mosquito in your collar"
     * "the war of baronets".replace('r', 'y')
     *         returns "the way of bayonets"
     * "sparring with a purple porpoise".replace('p', 't')
     *         returns "starring with a turtle tortoise"
     * "JonL".replace('q', 'x') returns "JonL" (no change)
     * </pre></blockquote>
     *
     * @param   oldChar   原字符。
     * @param   newChar    新字符。
     * @return  一个从此字符串派生的字符串，
     *           它将此字符串中的所有 {@code oldChar} 替代为 {@code newChar}。
     */
    public String replace(char oldChar, char newChar) {
        if (oldChar != newChar) {
            int len = value.length;
            int i = -1;
            char[] val = value; /* avoid getfield opcode */

            while (++i < len) {
                if (val[i] == oldChar) {
                    break;
                }
            }
            if (i < len) {
                char buf[] = new char[len];
                for (int j = 0; j < i; j++) {
                    buf[j] = val[j];
                }
                while (i < len) {
                    char c = val[i];
                    buf[i] = (c == oldChar) ? newChar : c;
                    i++;
                }
                return new String(buf, true);
            }
        }
        return this;
    }

    /**
     * 告知此字符串是否匹配给定的 <a href="../util/regex/Pattern.html#sum">正则表达式</a>。
     *
     * <p> 调用此方法的 <i>str</i>{@code .matches(}<i>regex</i>{@code )} 形式与以下表达式产生的结果完全相同：
     *
     * <blockquote>
     * {@link java.util.regex.Pattern}.{@link java.util.regex.Pattern#matches(String,CharSequence)
     * matches(<i>regex</i>, <i>str</i>)}
     * </blockquote>
     *
     * @param   regex
     *          用来匹配此字符串的正则表达式
     *
     * @return  当且仅当此字符串匹配给定的正则表达式时，返回 {@code true}
     *
     * @throws  PatternSyntaxException
     *          如果正则表达式的语法无效
     *
     * @see java.util.regex.Pattern
     *
     * @since 1.4
     * @spec JSR-51
     */
    public boolean matches(String regex) {
        return Pattern.matches(regex, this);
    }

    /**
     * 当且仅当此字符串包含指定的 char 值序列时，返回 true。
     *
     * @param s 要搜索的序列
     * @return 如果此字符串包含 s，则返回 true，否则返回 false
     * @since 1.5
     */
    public boolean contains(CharSequence s) {
        return indexOf(s.toString()) > -1;
    }

    /**
     * 使用给定的 replacement 替换此字符串匹配给定的 <a
     * href="../util/regex/Pattern.html#sum">正则表达式</a> 的第一个子字符串。
     *
     * <p> 调用此方法的
     * <i>str</i>{@code .replaceFirst(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * 形式与以下表达式产生的结果完全相同：
     *
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#matcher(java.lang.CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceFirst replaceFirst}(<i>repl</i>)
     * </code>
     * </blockquote>
     *
     *<p>
     * 注意，在替代字符串中使用反斜杠 ({@code \}) 和美元符号 ({@code $}) 与
     * 将其视为字面值替代字符串所得的结果可能不同；
     * 请参阅 {@link java.util.regex.Matcher#replaceFirst}。
     * 如有需要，可使用 {@link java.util.regex.Matcher#quoteReplacement} 取消这些字符的特殊含义。
     *
     * @param   regex
     *          用来匹配此字符串的正则表达式
     * @param   replacement
     *          用来替换第一个匹配项的字符串
     *
     * @return  所得 {@code String}
     *
     * @throws  PatternSyntaxException
     *          如果正则表达式的语法无效
     *
     * @see java.util.regex.Pattern
     *
     * @since 1.4
     * @spec JSR-51
     */
    public String replaceFirst(String regex, String replacement) {
        return Pattern.compile(regex).matcher(this).replaceFirst(replacement);
    }

    /**
     * 使用给定的 replacement 替换此字符串所有匹配给定的 <a
     * href="../util/regex/Pattern.html#sum">正则表达式</a>的子字符串。
     *
     * <p> 调用此方法的
     * <i>str</i>{@code .replaceAll(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * 形式与以下表达式产生的结果完全相同：
     *
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#matcher(java.lang.CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceAll replaceAll}(<i>repl</i>)
     * </code>
     * </blockquote>
     *
     *<p>
     * 注意，在替代字符串中使用反斜杠 ({@code \}) 和美元符号 ({@code $}) 与
     * 将其视为字面值替代字符串所得的结果可能不同；
     * 请参阅 {@link java.util.regex.Matcher#replaceFirst}。
     * 如有需要，可使用 {@link java.util.regex.Matcher#quoteReplacement} 取消这些字符的特殊含义。
     *
     * @param   regex
     *          用来匹配此字符串的正则表达式
     * @param   replacement
     *          用来替换每个匹配项的字符串
     *
     * @return  所得 {@code String}
     *
     * @throws  PatternSyntaxException
     *          如果正则表达式的语法无效
     *
     * @see java.util.regex.Pattern
     *
     * @since 1.4
     * @spec JSR-51
     */
    public String replaceAll(String regex, String replacement) {
        return Pattern.compile(regex).matcher(this).replaceAll(replacement);
    }

    /**
     * 使用指定的字面值替换序列替换此字符串所有匹配字面值目标序列的子字符串。
     * 该替换从字符串的开头朝末尾执行，
     * 例如，用 "b" 替换字符串 "aaa" 中的 "aa" 将生成 "ba" 而不是 "ab"。
     *
     * @param  target 要被替换的 char 值序列
     * @param  replacement char 值的替换序列
     * @return  所得 String
     * @since 1.5
     */
    public String replace(CharSequence target, CharSequence replacement) {
        return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(
                this).replaceAll(Matcher.quoteReplacement(replacement.toString()));
    }

    /**
     * 根据匹配给定的 <a href="../util/regex/Pattern.html#sum">正则表达式</a> 来拆分此字符串。
     *
     * <p> 此方法返回的数组包含此字符串的子字符串，每个子字符串都由另一个匹配给定表达式的子字符串终止，
     * 或者由此字符串末尾终止。数组中的子字符串按它们在此字符串中出现的顺序排列。
     * 如果表达式不匹配输入的任何部分，那么所得数组只具有一个元素，即此字符串。
     * The array returned by this method contains each substring of this
     * string that is terminated by another substring that matches the given
     * expression or is terminated by the end of the string.  The substrings in
     * the array are in the order in which they occur in this string.  If the
     * expression does not match any part of the input then the resulting array
     * has just one element, namely this string.
     *
     * <p> 当此字符串的开头存在正向宽度匹配时，在结果数组的开头包含空的前导子字符串。
     * 然而，开头的零宽度匹配从不会产生这样的空前导子串。
     *
     * <p> {@code limit} 参数控制模式应用的次数，因此影响所得数组的长度。
     * 如果该限制 <i>n</i> 大于 0，则模式将被最多应用 <i>n</i>&nbsp;-&nbsp;1 次，
     * 数组的长度将不会大于 <i>n</i>，而且数组的最后一项将包含所有超出最后匹配的定界符的输入。
     * 如果 <i>n</i> 为非正，那么模式将被应用尽可能多的次数，而且数组可以是任何长度。
     * 如果 <i>n</i> 为 0，那么模式将被应用尽可能多的次数，
     * 数组可以是任何长度，并且结尾空字符串将被丢弃。
     *
     * <p> 例如，字符串 {@code "boo:and:foo"} 使用这些参数可生成以下结果：
     *
     * <blockquote><table cellpadding=1 cellspacing=0 summary="Split example showing regex, limit, and result">
     * <tr>
     *     <th>Regex</th>
     *     <th>Limit</th>
     *     <th>结果</th>
     * </tr>
     * <tr><td align=center>:</td>
     *     <td align=center>2</td>
     *     <td>{@code { "boo", "and:foo" }}</td></tr>
     * <tr><td align=center>:</td>
     *     <td align=center>5</td>
     *     <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><td align=center>:</td>
     *     <td align=center>-2</td>
     *     <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><td align=center>o</td>
     *     <td align=center>5</td>
     *     <td>{@code { "b", "", ":and:f", "", "" }}</td></tr>
     * <tr><td align=center>o</td>
     *     <td align=center>-2</td>
     *     <td>{@code { "b", "", ":and:f", "", "" }}</td></tr>
     * <tr><td align=center>o</td>
     *     <td align=center>0</td>
     *     <td>{@code { "b", "", ":and:f" }}</td></tr>
     * </table></blockquote>
     *
     * <p> 调用此方法的
     * <i>str.</i>{@code split(}<i>regex</i>{@code ,}&nbsp;<i>n</i>{@code )}
     * 形式与以下表达式产生的结果完全相同：
     *
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#split(java.lang.CharSequence,int) split}(<i>str</i>,&nbsp;<i>n</i>)
     * </code>
     * </blockquote>
     *
     *
     * @param  regex
     *         定界正则表达式
     *
     * @param  limit
     *         结果阈值，如上所述
     *
     * @return  字符串数组，它是根据给定正则表达式的匹配拆分此字符串确定的
     *
     * @throws  PatternSyntaxException
     *          如果正则表达式的语法无效
     *
     * @see java.util.regex.Pattern
     *
     * @since 1.4
     * @spec JSR-51
     */
    public String[] split(String regex, int limit) {
        /* fastpath if the regex is a
         (1)one-char String and this character is not one of the
            RegEx's meta characters ".$|()[{^?*+\\", or
         (2)two-char String and the first char is the backslash and
            the second is not the ascii digit or ascii letter.
         */
        char ch = 0;
        if (((regex.value.length == 1 &&
             ".$|()[{^?*+\\".indexOf(ch = regex.charAt(0)) == -1) ||
             (regex.length() == 2 &&
              regex.charAt(0) == '\\' &&
              (((ch = regex.charAt(1))-'0')|('9'-ch)) < 0 &&
              ((ch-'a')|('z'-ch)) < 0 &&
              ((ch-'A')|('Z'-ch)) < 0)) &&
            (ch < Character.MIN_HIGH_SURROGATE ||
             ch > Character.MAX_LOW_SURROGATE))
        {
            int off = 0;
            int next = 0;
            boolean limited = limit > 0;
            ArrayList<String> list = new ArrayList<>();
            while ((next = indexOf(ch, off)) != -1) {
                if (!limited || list.size() < limit - 1) {
                    list.add(substring(off, next));
                    off = next + 1;
                } else {    // last one
                    //assert (list.size() == limit - 1);
                    list.add(substring(off, value.length));
                    off = value.length;
                    break;
                }
            }
            // If no match was found, return this
            if (off == 0)
                return new String[]{this};

            // Add remaining segment
            if (!limited || list.size() < limit)
                list.add(substring(off, value.length));

            // Construct result
            int resultSize = list.size();
            if (limit == 0) {
                while (resultSize > 0 && list.get(resultSize - 1).length() == 0) {
                    resultSize--;
                }
            }
            String[] result = new String[resultSize];
            return list.subList(0, resultSize).toArray(result);
        }
        return Pattern.compile(regex).split(this, limit);
    }

    /**
     * 根据匹配给定的 <a href="../util/regex/Pattern.html#sum">正则表达式</a> 来拆分此字符串。
     *
     * <p> 该方法的作用就像是使用给定的表达式和限制参数 0 来调用两参数 {@link
     * #split(String, int) split} 方法。
     * 因此，所得数组中不包括结尾空字符串。
     *
     * <p> 例如，字符串 {@code "boo:and:foo"} 使用这些表达式可生成以下结果：
     *
     * <blockquote><table cellpadding=1 cellspacing=0 summary="Split examples showing regex and result">
     * <tr>
     *  <th>Regex</th>
     *  <th>结果</th>
     * </tr>
     * <tr><td align=center>:</td>
     *     <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><td align=center>o</td>
     *     <td>{@code { "b", "", ":and:f" }}</td></tr>
     * </table></blockquote>
     *
     *
     * @param  regex
     *         定界正则表达式
     *
     * @return  字符串数组，它是根据给定正则表达式的匹配拆分此字符串确定的
     *
     * @throws  PatternSyntaxException
     *          如果正则表达式的语法无效
     *
     * @see java.util.regex.Pattern
     *
     * @since 1.4
     * @spec JSR-51
     */
    public String[] split(String regex) {
        return split(regex, 0);
    }

    /**
     * Returns a new String composed of copies of the
     * {@code CharSequence elements} joined together with a copy of
     * the specified {@code delimiter}.
     *
     * <blockquote>For example,
     * <pre>{@code
     *     String message = String.join("-", "Java", "is", "cool");
     *     // message returned is: "Java-is-cool"
     * }</pre></blockquote>
     *
     * Note that if an element is null, then {@code "null"} is added.
     *
     * @param  delimiter the delimiter that separates each element
     * @param  elements the elements to join together.
     *
     * @return a new {@code String} that is composed of the {@code elements}
     *         separated by the {@code delimiter}
     *
     * @throws NullPointerException If {@code delimiter} or {@code elements}
     *         is {@code null}
     *
     * @see java.util.StringJoiner
     * @since 1.8
     */
    public static String join(CharSequence delimiter, CharSequence... elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        // Number of elements not likely worth Arrays.stream overhead.
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    /**
     * Returns a new {@code String} composed of copies of the
     * {@code CharSequence elements} joined together with a copy of the
     * specified {@code delimiter}.
     *
     * <blockquote>For example,
     * <pre>{@code
     *     List<String> strings = new LinkedList<>();
     *     strings.add("Java");strings.add("is");
     *     strings.add("cool");
     *     String message = String.join(" ", strings);
     *     //message returned is: "Java is cool"
     *
     *     Set<String> strings = new LinkedHashSet<>();
     *     strings.add("Java"); strings.add("is");
     *     strings.add("very"); strings.add("cool");
     *     String message = String.join("-", strings);
     *     //message returned is: "Java-is-very-cool"
     * }</pre></blockquote>
     *
     * Note that if an individual element is {@code null}, then {@code "null"} is added.
     *
     * @param  delimiter a sequence of characters that is used to separate each
     *         of the {@code elements} in the resulting {@code String}
     * @param  elements an {@code Iterable} that will have its {@code elements}
     *         joined together.
     *
     * @return a new {@code String} that is composed from the {@code elements}
     *         argument
     *
     * @throws NullPointerException If {@code delimiter} or {@code elements}
     *         is {@code null}
     *
     * @see    #join(CharSequence,CharSequence...)
     * @see    java.util.StringJoiner
     * @since 1.8
     */
    public static String join(CharSequence delimiter,
            Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    /**
     * 使用给定 {@code Locale} 的规则将此 {@code String} 中的所有字符都转换为小写。
     * 大小写映射关系基于 {@link java.lang.Character Character} 类指定的 Unicode 标准版。
     * 由于大小写映射关系并不总是 1:1 的字符映射关系，
     * 因此所得 {@code String} 的长度可能不同于原 {@code String}。
     * <p>
     * 下表中给出了几个小写映射关系的示例：
     * <table border="1" summary="Lowercase mapping examples showing language code of locale, upper case, lower case, and description">
     * <tr>
     *   <th>语言环境</th>
     *   <th>大写字母</th>
     *   <th>小写字母</th>
     *   <th>描述</th>
     * </tr>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <td>&#92;u0130</td>
     *   <td>&#92;u0069</td>
     *   <td>大写字母 I，上面有点 -&gt; 小写字母 i</td>
     * </tr>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <td>&#92;u0049</td>
     *   <td>&#92;u0131</td>
     *   <td>大写字母 I -&gt; 小写字母 i，无点</td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <td>French Fries</td>
     *   <td>french fries</td>
     *   <td>将字符串中的所有字符都小写</td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <td><img src="doc-files/capiota.gif" alt="capiota"><img src="doc-files/capchi.gif" alt="capchi">
     *       <img src="doc-files/captheta.gif" alt="captheta"><img src="doc-files/capupsil.gif" alt="capupsil">
     *       <img src="doc-files/capsigma.gif" alt="capsigma"></td>
     *   <td><img src="doc-files/iota.gif" alt="iota"><img src="doc-files/chi.gif" alt="chi">
     *       <img src="doc-files/theta.gif" alt="theta"><img src="doc-files/upsilon.gif" alt="upsilon">
     *       <img src="doc-files/sigma1.gif" alt="sigma"></td>
     *   <td>将字符串中的所有字符都小写</td>
     * </tr>
     * </table>
     *
     * @param locale 使用此语言环境的大小写转换规则
     * @return 要转换为小写的 {@code String}
     * @see     java.lang.String#toLowerCase()
     * @see     java.lang.String#toUpperCase()
     * @see     java.lang.String#toUpperCase(Locale)
     * @since   1.1
     */
    public String toLowerCase(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }

        int firstUpper;
        final int len = value.length;

        /* Now check if there are any characters that need to be changed. */
        scan: {
            for (firstUpper = 0 ; firstUpper < len; ) {
                char c = value[firstUpper];
                if ((c >= Character.MIN_HIGH_SURROGATE)
                        && (c <= Character.MAX_HIGH_SURROGATE)) {
                    int supplChar = codePointAt(firstUpper);
                    if (supplChar != Character.toLowerCase(supplChar)) {
                        break scan;
                    }
                    firstUpper += Character.charCount(supplChar);
                } else {
                    if (c != Character.toLowerCase(c)) {
                        break scan;
                    }
                    firstUpper++;
                }
            }
            return this;
        }

        char[] result = new char[len];
        int resultOffset = 0;  /* result may grow, so i+resultOffset
                                * is the write location in result */

        /* Just copy the first few lowerCase characters. */
        System.arraycopy(value, 0, result, 0, firstUpper);

        String lang = locale.getLanguage();
        boolean localeDependent =
                (lang == "tr" || lang == "az" || lang == "lt");
        char[] lowerCharArray;
        int lowerChar;
        int srcChar;
        int srcCount;
        for (int i = firstUpper; i < len; i += srcCount) {
            srcChar = (int)value[i];
            if ((char)srcChar >= Character.MIN_HIGH_SURROGATE
                    && (char)srcChar <= Character.MAX_HIGH_SURROGATE) {
                srcChar = codePointAt(i);
                srcCount = Character.charCount(srcChar);
            } else {
                srcCount = 1;
            }
            if (localeDependent ||
                srcChar == '\u03A3' || // GREEK CAPITAL LETTER SIGMA
                srcChar == '\u0130') { // LATIN CAPITAL LETTER I WITH DOT ABOVE
                lowerChar = ConditionalSpecialCasing.toLowerCaseEx(this, i, locale);
            } else {
                lowerChar = Character.toLowerCase(srcChar);
            }
            if ((lowerChar == Character.ERROR)
                    || (lowerChar >= Character.MIN_SUPPLEMENTARY_CODE_POINT)) {
                if (lowerChar == Character.ERROR) {
                    lowerCharArray =
                            ConditionalSpecialCasing.toLowerCaseCharArray(this, i, locale);
                } else if (srcCount == 2) {
                    resultOffset += Character.toChars(lowerChar, result, i + resultOffset) - srcCount;
                    continue;
                } else {
                    lowerCharArray = Character.toChars(lowerChar);
                }

                /* Grow result if needed */
                int mapLen = lowerCharArray.length;
                if (mapLen > srcCount) {
                    char[] result2 = new char[result.length + mapLen - srcCount];
                    System.arraycopy(result, 0, result2, 0, i + resultOffset);
                    result = result2;
                }
                for (int x = 0; x < mapLen; ++x) {
                    result[i + resultOffset + x] = lowerCharArray[x];
                }
                resultOffset += (mapLen - srcCount);
            } else {
                result[i + resultOffset] = (char)lowerChar;
            }
        }
        return new String(result, 0, len + resultOffset);
    }

    /**
     * 使用默认语言环境的规则将此 {@code String} 中的所有字符都转换为小写。这等效于调用
     * {@code toLowerCase(Locale.getDefault())}.
     * <p>
     * <b>注：</b> 此方法与语言环境有关，如果用于应独立于语言环境解释的字符串，
     * 则可能生成不可预料的结果。
     * 示例有编程语言标识符、协议键、HTML 标记。
     * 例如，{@code "TITLE".toLowerCase()} 在 Turkish（土耳其语）语言环境中返回 {@code "t\u005Cu0131tle"}，
     * 其中“\u005Cu0131”是 LATIN SMALL LETTER DOTLESS I 字符。
     * 对于与语言环境有关的字符，要获得正确的结果，
     * 请使用 {@code toLowerCase(Locale.ROOT)}。
     * <p>
     * @return  要转换为小写的 {@code String}。
     * @see     java.lang.String#toLowerCase(Locale)
     */
    public String toLowerCase() {
        return toLowerCase(Locale.getDefault());
    }

    /**
     * 使用给定 {@code Locale} 的规则将此 {@code String} 中的所有字符都转换为大写。
     * 大小写映射关系基于 {@link java.lang.Character Character} 类指定的 Unicode 标准版。
     * 由于大小写映射关系并不总是 1:1 的字符映射关系，因此所得 {@code String} 的长度可能不同于原 {@code String}。
     * <p>
     * 下表中给出了几个与语言环境有关和 1:M 大小写映射关系的一些示例。
     *
     * <table border="1" summary="Examples of locale-sensitive and 1:M case mappings. Shows Language code of locale, lower case, upper case, and description.">
     * <tr>
     *   <th>语言环境的代码</th>
     *   <th>小写</th>
     *   <th>大写</th>
     *   <th>描述</th>
     * </tr>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <td>&#92;u0069</td>
     *   <td>&#92;u0130</td>
     *   <td>小写字母 i -&gt; 大写字幕 I，上面有点</td>
     * </tr>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <td>&#92;u0131</td>
     *   <td>&#92;u0049</td>
     *   <td>小写字母 i，无点 -&gt; 大写字母 I</td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <td>&#92;u00df</td>
     *   <td>&#92;u0053 &#92;u0053</td>
     *   <td>小写字母 sharp s -&gt; 两个字母: SS</td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <td>Fahrvergn&uuml;gen</td>
     *   <td>FAHRVERGN&Uuml;GEN</td>
     *   <td></td>
     * </tr>
     * </table>
     * @param locale 使用此语言环境的大小写转换规则
     * @return 要转换为大写的 {@code String}
     * @see     java.lang.String#toUpperCase()
     * @see     java.lang.String#toLowerCase()
     * @see     java.lang.String#toLowerCase(Locale)
     * @since   1.1
     */
    public String toUpperCase(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }

        int firstLower;
        final int len = value.length;

        /* Now check if there are any characters that need to be changed. */
        scan: {
            for (firstLower = 0 ; firstLower < len; ) {
                int c = (int)value[firstLower];
                int srcCount;
                if ((c >= Character.MIN_HIGH_SURROGATE)
                        && (c <= Character.MAX_HIGH_SURROGATE)) {
                    c = codePointAt(firstLower);
                    srcCount = Character.charCount(c);
                } else {
                    srcCount = 1;
                }
                int upperCaseChar = Character.toUpperCaseEx(c);
                if ((upperCaseChar == Character.ERROR)
                        || (c != upperCaseChar)) {
                    break scan;
                }
                firstLower += srcCount;
            }
            return this;
        }

        /* result may grow, so i+resultOffset is the write location in result */
        int resultOffset = 0;
        char[] result = new char[len]; /* may grow */

        /* Just copy the first few upperCase characters. */
        System.arraycopy(value, 0, result, 0, firstLower);

        String lang = locale.getLanguage();
        boolean localeDependent =
                (lang == "tr" || lang == "az" || lang == "lt");
        char[] upperCharArray;
        int upperChar;
        int srcChar;
        int srcCount;
        for (int i = firstLower; i < len; i += srcCount) {
            srcChar = (int)value[i];
            if ((char)srcChar >= Character.MIN_HIGH_SURROGATE &&
                (char)srcChar <= Character.MAX_HIGH_SURROGATE) {
                srcChar = codePointAt(i);
                srcCount = Character.charCount(srcChar);
            } else {
                srcCount = 1;
            }
            if (localeDependent) {
                upperChar = ConditionalSpecialCasing.toUpperCaseEx(this, i, locale);
            } else {
                upperChar = Character.toUpperCaseEx(srcChar);
            }
            if ((upperChar == Character.ERROR)
                    || (upperChar >= Character.MIN_SUPPLEMENTARY_CODE_POINT)) {
                if (upperChar == Character.ERROR) {
                    if (localeDependent) {
                        upperCharArray =
                                ConditionalSpecialCasing.toUpperCaseCharArray(this, i, locale);
                    } else {
                        upperCharArray = Character.toUpperCaseCharArray(srcChar);
                    }
                } else if (srcCount == 2) {
                    resultOffset += Character.toChars(upperChar, result, i + resultOffset) - srcCount;
                    continue;
                } else {
                    upperCharArray = Character.toChars(upperChar);
                }

                /* Grow result if needed */
                int mapLen = upperCharArray.length;
                if (mapLen > srcCount) {
                    char[] result2 = new char[result.length + mapLen - srcCount];
                    System.arraycopy(result, 0, result2, 0, i + resultOffset);
                    result = result2;
                }
                for (int x = 0; x < mapLen; ++x) {
                    result[i + resultOffset + x] = upperCharArray[x];
                }
                resultOffset += (mapLen - srcCount);
            } else {
                result[i + resultOffset] = (char)upperChar;
            }
        }
        return new String(result, 0, len + resultOffset);
    }

    /**
     * 使用默认语言环境的规则将此 {@code String} 中的所有字符都转换为大写。此方法等效于
     * {@code toUpperCase(Locale.getDefault())}.
     * <p>
     * <b>注：</b> 此方法与语言环境有关，如果用于应独立于语言环境解释的字符串，则可能生成不可预料的结果。
     * 示例有编程语言标识符、协议键、HTML 标记。
     * 例如，{@code "title".toUpperCase()} 在 Turkish（土耳其语）语言环境中返回 "{@code "T\u005Cu0130TLE"}，
     * 其中“\u005Cu0130”是 LATIN CAPITAL LETTER I WITH DOT ABOVE 字符。
     * 对于与语言环境有关的字符，要获得正确的结果，请使用
     * {@code toUpperCase(Locale.ROOT)}.
     * <p>
     * @return  要转换为大写的 {@code String}。
     * @see     java.lang.String#toUpperCase(Locale)
     */
    public String toUpperCase() {
        return toUpperCase(Locale.getDefault());
    }

    /**
     * 返回字符串的副本，忽略前导空白和尾部空白。
     * <p>
     * 如果此 {@code String} 对象表示一个空字符序列，
     * 或者此 {@code String} 对象表示的字符序列的第一个和最后一个字符的代码都大于 {@code '\u005Cu0020'}（空格字符），
     * 则返回对此 {@code String} 对象的引用。
     * <p>
     * 否则，若字符串中没有代码大于 {@code '\u005Cu0020'} 的字符，
     * 则创建并返回一个表示空字符串的新 {@code String} 对象。
     * <p>
     * 否则，假定 <i>k</i>  为字符串中代码大于 {@code '\u005Cu0020'} 的第一个字符的索引，
     * <i>m</i> 为字符串中代码大于 {@code '\u005Cu0020'} 的最后一个字符的索引。
     * 创建一个新的 {@code String} 对象，它表示此字符串中从索引 <i>k</i> 处的字符开始，
     * 到索引 <i>m</i> 处的字符结束的子字符串，即 {@code this.substring(k, m + 1)} 的结果。
     * <p>
     * 此方法可用于截去字符串开头和末尾的空白（如上所述）。
     *
     * @return  此字符串移除了前导和尾部空白的副本；
     *          如果没有前导和尾部空白，则返回此字符串。
     */
    public String trim() {
        int len = value.length;
        int st = 0;
        char[] val = value;    /* avoid getfield opcode */

        while ((st < len) && (val[st] <= ' ')) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < value.length)) ? substring(st, len) : this;
    }

    /**
     * 返回此对象本身（它已经是一个字符串！）。
     *
     * @return  字符串本身。
     */
    public String toString() {
        return this;
    }

    /**
     * 将此字符串转换为一个新的字符数组。
     *
     * @return 一个新分配的字符数组，它的长度是此字符串的长度，
     *         它的内容被初始化为包含此字符串表示的字符序列。
     */
    public char[] toCharArray() {
        // Cannot use Arrays.copyOf because of class initialization order issues
        char result[] = new char[value.length];
        System.arraycopy(value, 0, result, 0, value.length);
        return result;
    }

    /**
     * 使用指定的格式字符串和参数返回一个格式化字符串。
     *
     * <p> 始终使用 {@link
     * java.util.Locale#getDefault() Locale.getDefault()} 返回的语言环境。
     *
     * @param  format
     *         A <a href="../util/Formatter.html#syntax">格式字符串</a>
     *
     * @param  args
     *         格式字符串中由格式说明符引用的参数。
     *         如果还有格式说明符以外的参数，则忽略这些额外的参数。
     *         参数的数目是可变的，可以为 0。
     *         参数的最大数目受
     *         <cite>The Java&trade; Virtual Machine Specification</cite>
     *         所定义的 Java 数组最大维度的限制。
     *         有关 {@code null} 参数的行为依赖于 <a
     *         href="../util/Formatter.html#syntax">转换</a>。
     *
     * @throws  java.util.IllegalFormatException
     *          如果格式字符串中包含非法语法、与给定的参数不兼容的格式说明符，
     *          格式字符串给定的参数不够，或者存在其他非法条件。
     *          有关所有可能的格式化错误的规范，请参阅 formatter 类规范的
     *          <a href="../util/Formatter.html#detail">详细信息</a> 一节。
     *
     * @return  一个格式化字符串
     *
     * @see  java.util.Formatter
     * @since  1.5
     */
    public static String format(String format, Object... args) {
        return new Formatter().format(format, args).toString();
    }

    /**
     * 使用指定的语言环境、格式字符串和参数返回一个格式化字符串。
     *
     * @param  l
     *         格式化过程中要应用的 {@linkplain java.util.Locale 语言环境}。
     *         如果 {@code l} 为 {@code null}，则不进行本地化。
     *
     * @param  format
     *         <a href="../util/Formatter.html#syntax">格式字符串</a>
     *
     * @param  args
     *         格式字符串中由格式说明符引用的参数。
     *         如果还有格式说明符以外的参数，则忽略这些额外的参数。
     *         参数的数目是可变的，可以为 0。
     *         参数的最大数目受
     *         <cite>The Java&trade; Virtual Machine Specification</cite>
     *         所定义的 Java 数组最大维度的限制。
     *         有关 {@code null} 参数的行为依赖于 <a
     *         href="../util/Formatter.html#syntax">转换</a>。
     *
     * @throws  java.util.IllegalFormatException
     *          如果格式字符串中包含非法语法、与给定的参数不兼容的格式说明符，
     *          格式字符串给定的参数不够，或者存在其他非法条件。
     *          有关所有可能的格式化错误的规范，请参阅 formatter 类规范的
     *          <a href="../util/Formatter.html#detail">详细信息</a> 一节。
     *
     * @return  一个格式化字符串
     *
     * @see  java.util.Formatter
     * @since  1.5
     */
    public static String format(Locale l, String format, Object... args) {
        return new Formatter(l).format(format, args).toString();
    }

    /**
     * 返回 {@code Object}  参数的字符串表示形式。
     *
     * @param   obj   一个 {@code Object}。
     * @return  如果参数为 {@code null}， 则字符串等于 {@code "null"}；
     *          否则，返回 {@code obj.toString()} 的值。
     * @see     java.lang.Object#toString()
     */
    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    /**
     * 返回 {@code char} 数组参数的字符串表示形式。
     * 字符数组的内容已被复制，后续修改不会影响新创建的字符串。
     *
     * @param   data     char 数组。
     * @return  一个 {@code String}，它包含字符数组的字符。
     */
    public static String valueOf(char data[]) {
        return new String(data);
    }

    /**
     * 返回 {@code char} 数组参数的特定子数组的字符串表示形式。
     * <p>
     * {@code offset} 参数是子数组的第一个字符的索引。
     * {@code count} 参数指定子数组的长度。
     * 字符数组的内容已被复制，后续修改不会影响新创建的字符串。
     *
     * @param   data     字符数组。
     * @param   offset   子数组的初始偏移量。
     * @param   count    子数组的长度。
     * @return  一个{@code String}，它表示在字符数组参数的子数组中包含的字符序列。
     * @exception IndexOutOfBoundsException 如果 {@code offset} 为负， {@code count} 为负，
     *          或者 {@code offset+count} 大于 {@code data.length}。
     */
    public static String valueOf(char data[], int offset, int count) {
        return new String(data, offset, count);
    }

    /**
     * 等同于 {@link #valueOf(char[], int, int)}.
     *
     * @param   data     字符数组。
     * @param   offset   子数组的初始偏移量。
     * @param   count    子数组的长度。
     * @return  一个{@code String}，它表示在字符数组参数的子数组中包含的字符序列。
     * @exception IndexOutOfBoundsException 如果 {@code offset} 为负， {@code count} 为负，
     *          或者 {@code offset+count} 大于 {@code data.length}。
     */
    public static String copyValueOf(char data[], int offset, int count) {
        return new String(data, offset, count);
    }

    /**
     * 等同于 {@link #valueOf(char[])}.
     *
     * @param   data   字符数组。
     * @return  一个 {@code String}，它包含字符数组的字符。
     */
    public static String copyValueOf(char data[]) {
        return new String(data);
    }

    /**
     * 返回 {@code boolean} 参数的字符串表示形式。
     *
     * @param   b   一个 {@code boolean}.
     * @return  如果参数为 {@code true}，则返回一个等于 {@code "true"} 的字符串；
     *          否则，返回一个等于 {@code "false"} 的字符串。
     */
    public static String valueOf(boolean b) {
        return b ? "true" : "false";
    }

    /**
     * 返回 {@code char} 参数的字符串表示形式。
     *
     * @param   c   一个 {@code char}。
     * @return 一个长度为 {@code 1} 的字符串，它包含参数 {@code c} 的单个字符。
     */
    public static String valueOf(char c) {
        char data[] = {c};
        return new String(data, true);
    }

    /**
     * 返回 {@code int} 参数的字符串表示形式。
     * <p>
     * 该表示形式恰好是单参数的 {@code Integer.toString} 方法返回的结果。
     *
     * @param   i   一个 {@code int}。
     * @return  {@code int} 参数的字符串表示形式。
     * @see     java.lang.Integer#toString(int, int)
     */
    public static String valueOf(int i) {
        return Integer.toString(i);
    }

    /**
     * 返回 {@code long}  参数的字符串表示形式。
     * <p>
     * 该表示形式恰好是单参数的 {@code Long.toString} 方法返回的结果。
     *
     * @param   l   一个 {@code long}。
     * @return  {@code long} 参数的字符串表示形式。
     * @see     java.lang.Long#toString(long)
     */
    public static String valueOf(long l) {
        return Long.toString(l);
    }

    /**
     * 返回 {@code float} 参数的字符串表示形式。
     * <p>
     * 该表示形式恰好是单参数的 {@code Float.toString} 方法返回的结果。
     *
     * @param   f   一个 {@code float}。
     * @return  {@code float} 参数的字符串表示形式。
     * @see     java.lang.Float#toString(float)
     */
    public static String valueOf(float f) {
        return Float.toString(f);
    }

    /**
     * 返回 {@code double} 参数的字符串表示形式。
     * <p>
     * 该表示形式恰好是单参数的 {@code Double.toString} 方法返回的结果。
     *
     * @param   d   一个 {@code double}。
     * @return  {@code double} 参数的字符串表示形式。
     * @see     java.lang.Double#toString(double)
     */
    public static String valueOf(double d) {
        return Double.toString(d);
    }

    /**
     * 返回字符串对象的规范化表示形式。
     * <p>
     * 一个初始为空的字符串池，它由类 {@code String} 私有地维护。
     * <p>
     * 当调用 intern 方法时，
     * 如果池已经包含一个等于此 {@code String} 对象的字符串（用 {@link #equals(Object)} 方法确定），
     * 则返回池中的字符串。
     * 否则，将此 {@code String} 对象添加到池中，并返回此 {@code String} 对象的引用。
     * <p>
     * 它遵循以下规则：对于任意两个字符串 {@code s} 和 {@code t}，
     * 当且仅当 {@code s.equals(t)} 为 {@code true} 时，
     * {@code s.intern() == t.intern()} 才为 {@code true}。
     * <p>
     * 所有字面值字符串和字符串赋值常量表达式都使用 intern 方法进行操作。
     * 字符串字面值在
     * <cite>The Java&trade; Language Specification</cite> 的 3.10.5 节中定义.
     *
     * @return  一个字符串，内容与此字符串相同，但一定取自具有唯一字符串的池。
     */
    public native String intern();
}
