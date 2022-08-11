package fr.pellan.api.linkedinmessageanalyzer.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
public class DateFormatUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String SUFFIX = "UTC";

    public LocalDateTime getDateFromLinkedinString(String dateTime){

        if(StringUtils.isBlank(dateTime)){
            return null;
        }

        try {
            return LocalDateTime.parse(dateTime.replaceAll(SUFFIX, "").trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            log.error("getDateFromLinkedinString : invalid date detected {}", dateTime, e);
        }
        return null;
    }
}
