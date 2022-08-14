package fr.pellan.api.linkedinmessageanalyzer.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Slf4j
@Service
public class DateFormatUtil {

    private static final DateTimeFormatter DATE_FORMATTER_MESSAGE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMATTER_INVITATION = DateTimeFormatter.ofPattern("M/d/yy, h:mm a").localizedBy(Locale.US);

    private static final String SUFFIX = "UTC";

    public LocalDateTime getDateFromLinkedinMessage(String dateTime){

        return parseDate(DATE_FORMATTER_MESSAGE, dateTime);
    }

    public LocalDateTime getDateFromLinkedinInvitation(String dateTime){

        return parseDate(DATE_FORMATTER_INVITATION, dateTime);
    }

    private LocalDateTime parseDate(DateTimeFormatter formatter, String dateTime){

        if(StringUtils.isBlank(dateTime)){
            return null;
        }

        try {
            return LocalDateTime.parse(dateTime.replace(SUFFIX, "").trim(), formatter);
        } catch (DateTimeParseException e) {
            log.error("parseDate : invalid date detected {}", dateTime, e);
        }
        return null;
    }
}
