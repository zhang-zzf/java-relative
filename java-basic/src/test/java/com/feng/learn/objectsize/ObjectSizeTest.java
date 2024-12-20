package com.feng.learn.objectsize;

import static java.lang.System.out;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
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
 * -XX:-UseCompressedOops
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
 * -XX:-UseCompressedOops  -XX:-UseCompressedClassPointers
 * # VM mode: 64 bits
 * # Compressed references (oops): disabled
 * # Compressed class pointers: disabled
 * # Object alignment: 8 bytes
 * #                       ref, bool, byte, char, shrt,  int,  flt,  lng,  dbl
 * # Field sizes:            8,    1,    1,    2,    2,    4,    4,    8,    8
 * # Array element sizes:    8,    1,    1,    2,    2,    4,    4,    8,    8
 * # Array base offsets:    24,   24,   24,   24,   24,   24,   24,   24,   24
 * </pre>
 *
 * <pre>
 *     注意点：jdk17 中，以下2个参数互不干扰
 *     1. -XX:-UseCompressedOops 控制是否压缩对象指针；
 *     1. -XX:-UseCompressedClassPointers 控制是否压缩对象头中的类指针
 * </pre>
 */
public class ObjectSizeTest {

    @Test
    public void testObject() throws InterruptedException {
        out.println(VM.current().details());
        out.println("*** new Object()");
        // java.lang.Object object internals:
        // OFF  SZ   TYPE DESCRIPTION               VALUE
        //   0   8        (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
        //   8   4        (object header: class)    0x00000d68
        //  12   4        (object alignment gap)
        // Instance size: 16 bytes
        // Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
        out.println(ClassLayout.parseInstance(new Object()).toPrintable());
        out.println("*** new Integer()");
        // java.lang.Integer object internals:
        // OFF  SZ   TYPE DESCRIPTION               VALUE
        //   0   8        (object header: mark)     0x0000002a2cdbd301 (hash: 0x2a2cdbd3; age: 0)
        //   8   4        (object header: class)    0x000492a0
        //  12   4    int Integer.value             0
        // Instance size: 16 bytes
        // Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
        out.println(ClassLayout.parseInstance(0).toPrintable());
        out.println("*** new Byte()");
        // java.lang.Byte object internals:
        // OFF  SZ   TYPE DESCRIPTION               VALUE
        //   0   8        (object header: mark)     0x00000022f578b101 (hash: 0x22f578b1; age: 0)
        //   8   4        (object header: class)    0x0003e490
        //  12   1   byte Byte.value                0
        //  13   3        (object alignment gap)
        // Instance size: 16 bytes
        // Space losses: 0 bytes internal + 3 bytes external = 3 bytes total
        out.println(ClassLayout.parseInstance((byte) 0).toPrintable());
        out.println("*** new Long()");
        // java.lang.Long object internals:
        // OFF  SZ   TYPE DESCRIPTION               VALUE
        //   0   8        (object header: mark)     0x000000075493c001 (hash: 0x075493c0; age: 0)
        //   8   4        (object header: class)    0x0004b800
        //  12   4        (alignment/padding gap)
        //  16   8   long Long.value                0
        // Instance size: 24 bytes
        // Space losses: 4 bytes internal + 0 bytes external = 4 bytes total
        out.println(ClassLayout.parseInstance(0L).toPrintable());
        out.println("*** new Entry()");
        // com.feng.learn.objectsize.ObjectSizeTest$Entry object internals:
        // OFF  SZ               TYPE DESCRIPTION               VALUE
        //   0   8                    (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
        //   8   4                    (object header: class)    0x00cc5220
        //  12   4                int Entry.i                   0
        //  16   8               long Entry.l                   0
        //  24   2              short Entry.s                   0
        //  26   2               char Entry.c                    
        //  28   1            boolean Entry.b                   false
        //  29   3                    (alignment/padding gap)
        //  32   4   java.lang.Object Entry.o                   null
        //  36   4                    (object alignment gap)
        // Instance size: 40 bytes
        // Space losses: 3 bytes internal + 4 bytes external = 7 bytes total
        out.println(ClassLayout.parseInstance(new Entry()).toPrintable());
    }

    // 4 + 2 + 8 + 8 + 2 = 24byte
    static class Entry {
        int i;
        short s;
        long l;
        Object o;
        char c;
        boolean b;
    }

}
