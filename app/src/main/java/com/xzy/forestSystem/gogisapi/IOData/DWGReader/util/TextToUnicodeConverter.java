package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.util;

import com.xzy.forestSystem.baidu.voicerecognition.android.LibFactory;
import java.text.StringCharacterIterator;

public class TextToUnicodeConverter {
    public static String convertText(String s) {
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        StringBuffer stringbuffer = new StringBuffer();
        int[] ai = new int[s.length()];
        int i = 0;
        int j = 0;
        for (char c = stringcharacteriterator.first(); c != 65535; c = stringcharacteriterator.next()) {
            if (c == '%') {
                if (stringcharacteriterator.next() != '%') {
                    stringbuffer.append('%');
                    stringcharacteriterator.previous();
                } else {
                    char c2 = stringcharacteriterator.next();
                    switch (c2) {
                        case '%':
                            stringbuffer.append('%');
                            continue;
                        case 'C':
                        case 'c':
                            stringbuffer.append((char) 237);
                            continue;
                        case LibFactory.FORMAT_OPUS_16K /* 68 */:
                        case 'd':
                            stringbuffer.append((char) 176);
                            continue;
                        case 'O':
                        case 'o':
                            int length = stringbuffer.length();
                            ai[length] = ai[length] ^ 2;
                            j++;
                            continue;
                        case 'P':
                        case 'p':
                            stringbuffer.append((char) 241);
                            continue;
                        case 'U':
                        case 'u':
                            int length2 = stringbuffer.length();
                            ai[length2] = ai[length2] ^ 1;
                            i++;
                            continue;
                        default:
                            if (c2 >= '0' && c2 <= '9') {
                                int k = 3;
                                char c1 = (char) (c2 - '0');
                                char c3 = stringcharacteriterator.next();
                                while (c3 >= '0' && c3 <= '9') {
                                    k--;
                                    if (k > 0) {
                                        c1 = (char) ((c1 * '\n') + (c3 - '0'));
                                        c3 = stringcharacteriterator.next();
                                    }
                                }
                                stringbuffer.append(c1);
                            }
                            stringcharacteriterator.previous();
                            continue;
                    }
                }
            } else if (c != '^') {
                stringbuffer.append(c);
            } else if (stringcharacteriterator.next() == ' ') {
                stringbuffer.append('^');
            }
        }
        return Unicode.char2DOS437(stringbuffer, 2, '?');
    }
}
