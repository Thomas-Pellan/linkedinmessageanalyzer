package fr.pellan.api.linkedinmessageanalyzer.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CsvParserUtil {

    public <T> List<T> parseCsvFile(MultipartFile file, Class<T> className){

        List<T> objects;
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<T> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(className)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            objects = csvToBean.parse();

        } catch (IOException e) {
            log.error("parseCsvFile : error on file {} parsing to class {}", file.getOriginalFilename(), className, e);
            return new ArrayList();
        }
        return objects;
    }
}
