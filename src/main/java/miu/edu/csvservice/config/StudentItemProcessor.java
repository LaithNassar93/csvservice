package miu.edu.csvservice.config;

import miu.edu.csvservice.domain.Student;
import miu.edu.csvservice.model.StudentCSV;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.util.Date;

public class StudentItemProcessor implements ItemProcessor<StudentCSV, Student> {

    @Override
    public Student process(final StudentCSV studentCSV) throws Exception {
        return new Student(null,studentCSV.getFirstname(), studentCSV.getLastname(), studentCSV.getGpa(), LocalDate.of(LocalDate.now().minusYears(studentCSV.getAge()).getYear(), 1,1));
    }

}