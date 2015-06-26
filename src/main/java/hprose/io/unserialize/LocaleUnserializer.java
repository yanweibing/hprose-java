/**********************************************************\
|                                                          |
|                          hprose                          |
|                                                          |
| Official WebSite: http://www.hprose.com/                 |
|                   http://www.hprose.org/                 |
|                                                          |
\**********************************************************/
/**********************************************************\
 *                                                        *
 * LocaleUnserializer.java                                *
 *                                                        *
 * Locale unserializer class for Java.                    *
 *                                                        *
 * LastModified: Jun 25, 2015                             *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import static hprose.io.HproseTags.TagEmpty;
import static hprose.io.HproseTags.TagNull;
import static hprose.io.HproseTags.TagRef;
import static hprose.io.HproseTags.TagString;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Locale;

final class LocaleUnserializer implements HproseUnserializer {

    public final static LocaleUnserializer instance = new LocaleUnserializer();

    private static Locale toLocale(String s) {
        String[] items = s.split("_");
        if (items.length == 1) {
            return new Locale(items[0]);
        }
        if (items.length == 2) {
            return new Locale(items[0], items[1]);
        }
        return new Locale(items[0], items[1], items[2]);
    }

    private static Locale toLocale(HproseReader reader, String s) throws IOException {
        Locale locale = toLocale(s);
        reader.refer.set(locale);
        return locale;
    }

    private static Locale toLocale(Object obj) {
        if (obj instanceof Locale) {
            return (Locale)obj;
        }
        if (obj instanceof char[]) {
            return toLocale(new String((char[])obj));
        }
        return toLocale(obj.toString());
    }

    final static Locale read(ByteBuffer buffer, HproseReader reader) throws IOException {
        int tag = buffer.get();
        switch(tag) {
            case TagNull:
            case TagEmpty: return null;
            case TagString: return toLocale(reader, ValueReader.readString(buffer));
            case TagRef: return toLocale(reader.readRef(buffer));
            default: throw ValueReader.castError(reader.tagToString(tag), Locale.class);
        }
    }

    final static Locale read(InputStream stream, HproseReader reader) throws IOException {
        int tag = stream.read();
        switch(tag) {
            case TagNull:
            case TagEmpty: return null;
            case TagString: return toLocale(reader, ValueReader.readString(stream));
            case TagRef: return toLocale(reader.readRef(stream));
            default: throw ValueReader.castError(reader.tagToString(tag), Locale.class);
        }
    }

    public final Object read(HproseReader reader, ByteBuffer buffer, Class<?> cls, Type type) throws IOException {
        return read(buffer, reader);
    }

    public final Object read(HproseReader reader, InputStream stream, Class<?> cls, Type type) throws IOException {
        return read(stream, reader);
    }

}