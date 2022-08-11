package fr.pellan.api.linkedinmessageanalyzer.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class DateFormatUtilTest {

    @Autowired
    DateFormatUtil util;

    @Test
    void formatter_test() {

        String str = "2022-07-18 13:02:02 UTC";
        Assert.notNull(util.getDateFromLinkedinString(str), "Date should be parsed");

        str = "2022-07-18 13:02:04";
        Assert.notNull(util.getDateFromLinkedinString(str), "Date should be parsed even without suffix");

        Assert.isNull(util.getDateFromLinkedinString(null), "Date should not be parsed");
    }
}
