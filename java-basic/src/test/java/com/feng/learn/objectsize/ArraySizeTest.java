package com.feng.learn.objectsize;

import static java.lang.System.out;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 测量数组对象头的大小
 * <p>测试环境jdk17，默认 jdk 启动参数 -XX:+UseCompressedOops -XX:FieldsAllocationStyle -XX +CompactFields</p>
 * <pre>
 * # VM mode: 64 bits
 * # Compressed references (oops): 3-bit shift
 * # Compressed class pointers: 0-bit shift and 0x800000000 base
 * # Object alignment: 8 bytes
 * #                       ref, bool, byte, char, shrt,  int,  flt,  lng,  dbl
 * # Field sizes:            4,    1,    1,    2,    2,    4,    4,    8,    8
 * # Array element sizes:    4,    1,    1,    2,    2,    4,    4,    8,    8
 * # Array base offsets:    16,   16,   16,   16,   16,   16,   16,   16,   16
 * </pre>
 *
 * <pre>
 * # VM mode: 64 bits
 * # Compressed references (oops): disabled
 * # Compressed class pointers: 0-bit shift and 0x800000000 base
 * # Object alignment: 8 bytes
 * #                       ref, bool, byte, char, shrt,  int,  flt,  lng,  dbl
 * # Field sizes:            8,    1,    1,    2,    2,    4,    4,    8,    8
 * # Array element sizes:    8,    1,    1,    2,    2,    4,    4,    8,    8
 * # Array base offsets:    16,   16,   16,   16,   16,   16,   16,   16,   16
 * </pre>
 *
 * <pre>
 *     注意点：关闭对象指针后，对象头中的类指针占用 4 byte 而不是 8 byte
 * </pre>
 */
public class ArraySizeTest {

    /**
     * <p>对象头 16 byte = 8(类引用) + 4(头) + 4(数组大小)</p>
     * <p>对象实例 =  4*n byte</p>
     * <p>对象 padding = 0 byte or 4 byte </p>
     */
    @Test
    public void testIntArraySize() {
        out.println(VM.current().details());
        for (int i = 0; i <= 8; i++) {
            out.println(ClassLayout.parseInstance(new int[i]).toPrintable());
        }
    }

    /**
     * <p>对象头 16 byte = 8(类引用) + 4(头) + 4(数组大小)</p>
     * <p>对象实例 =  8*n byte</p>
     * <p>对象 padding = 0 byte </p>
     */
    @Test
    public void testLongArraySize() {
        out.println(VM.current().details());
        int size = 9;
        for (int i = 0; i < size; i++) {
            out.println(ClassLayout.parseInstance(new long[i]).toPrintable());
        }
    }

    /**
     * <pre>
     * # VM mode: 64 bits
     * # Compressed references (oops): disabled
     * # Compressed class pointers: 0-bit shift and 0x800000000 base
     * # Object alignment: 8 bytes
     * #                       ref, bool, byte, char, shrt,  int,  flt,  lng,  dbl
     * # Field sizes:            8,    1,    1,    2,    2,    4,    4,    8,    8
     * # Array element sizes:    8,    1,    1,    2,    2,    4,    4,    8,    8
     * # Array base offsets:    16,   16,   16,   16,   16,   16,   16,   16,   16
     * </pre>
     * <p>对象头 16 byte = 8(类引用) + 4(头) + 4(数组大小)</p>
     * <pre>
     *      1. -XX:+UseCompressedOops 对象实例 =  4*n byte
     *      2. -XX:-UseCompressedOops 对象实例 =  8*n byte
     * </pre>
     * <p>对象 padding = 0 byte </p>
     */
    @Test
    public void testObjectArraySize() {
        out.println(VM.current().details());
        int size = 9;
        for (int i = 0; i < size; i++) {
            out.println(ClassLayout.parseInstance(new Object[i]).toPrintable());
        }
    }


}
