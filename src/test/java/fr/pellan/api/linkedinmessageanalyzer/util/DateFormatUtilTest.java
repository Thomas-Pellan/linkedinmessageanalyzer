package fr.pellan.api.linkedinmessageanalyzer.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DateFormatUtilTest {

    @Autowired
    DateFormatUtil util;

    @Test
    void formatter_test_messages() {

        String str = "2022-07-18 13:02:02 UTC";

        assertThat(util.getDateFromLinkedinMessage(str)).isNotNull();

        str = "2022-07-18 13:02:04";
        assertThat(util.getDateFromLinkedinMessage(str)).isNotNull();

        assertThat(util.getDateFromLinkedinMessage(null)).isNull();
    }

    @Test
    void formatter_test_invitations() {

        String str = "4/5/22, 9:36 AM";
        assertThat(util.getDateFromLinkedinInvitation(str)).isNotNull();

        str = "2/8/22, 11:29 PM";
        assertThat(util.getDateFromLinkedinInvitation(str)).isNotNull();

        str = "2/25/22, 11:29 PM";
        assertThat(util.getDateFromLinkedinInvitation(str)).isNotNull();

        assertThat(util.getDateFromLinkedinInvitation(null)).isNull();
    }
}
