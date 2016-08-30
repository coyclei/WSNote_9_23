package com.ws.coyc.wsnote;

import com.ws.coyc.wsnote.Utils.Calc;
import com.ws.coyc.wsnote.Utils.DateUtils;

import org.junit.Test;

import java.util.Date;

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

        System.out.print("ExampleUnitTest..........");
        Date date = new Date();
        System.out.println(DateUtils.getYear(date));
        System.out.println(DateUtils.getMouth(date));
        System.out.println(DateUtils.getDay(date));

    }
}