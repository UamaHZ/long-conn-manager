package cn.com.uama.longconnmanager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        assertEquals(Integer.parseInt("000022"), 22);
    }

    @Test
    public void testWSMessageCode() throws Exception {
        WSMessageCode code = WSMessageCode.create(1, 1, 1);
        assertEquals(code.toText(), "1100000001");
    }
}