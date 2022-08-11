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
    void formatter_test_messages() {

        String str = "2022-07-18 13:02:02 UTC";
        Assert.notNull(util.getDateFromLinkedinMessage(str), "Date should be parsed");

        str = "2022-07-18 13:02:04";
        Assert.notNull(util.getDateFromLinkedinMessage(str), "Date should be parsed even without suffix");

        Assert.isNull(util.getDateFromLinkedinMessage(null), "Date should not be parsed");
    }

    @Test
    void formatter_test_invitations() {

        String str = "4/5/22, 9:36 AM";
        Assert.notNull(util.getDateFromLinkedinInvitation(str), "Date should be parsed");

        str = "2/8/22, 11:29 PM";
        Assert.notNull(util.getDateFromLinkedinInvitation(str), "Date should be parsed");

        str = "2/25/22, 11:29 PM";
        Assert.notNull(util.getDateFromLinkedinInvitation(str), "Date should be parsed");

        Assert.isNull(util.getDateFromLinkedinInvitation(null), "Date should not be parsed");
    }
}
