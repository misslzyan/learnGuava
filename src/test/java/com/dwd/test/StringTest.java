package com.dwd.test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by bjduanweidong on 2017/4/1.
 */
public class StringTest {

    private static final Logger logger = Logger.getLogger(StringTest.class);

    @Test
    public void testJoin(){
        Joiner joiner = Joiner.on(";").useForNull("adafasf");
        String result = joiner.join("a","b",null,"c");
        logger.debug(result);
        Joiner j = Joiner.on(";").useForNull("dd");
        logger.debug(j.join(null,"b","m"));
        String r = Joiner.on(".").skipNulls().join("a","b","d",null);
        logger.debug(r);
        String r2 = Joiner.on(".").skipNulls().join(new String[]{"a","b",null});
        logger.debug(r2);
    }

    @Test
    public void testSpliter(){
        Iterable<String> split = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split("foo,bar,,   qux");
        logger.debug(split);
        for(String s :split){
            logger.debug("'"+s+"'");
        }


        Iterable<String> it = Splitter.on(',').trimResults(CharMatcher.is('_')).split("_a ,_b_,c__");

        for(String s :it){
            logger.debug("'"+s+"'");
        }
    }
}
