package miu.edu.csvservice.config;

import miu.edu.csvservice.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            jdbcTemplate.query("SELECT first_name, last_name , gpa, date_of_birth FROM student",
                    (rs, row) -> new Student(null,
                            rs.getString(1),
                            rs.getString(2),
                            rs.getDouble(3),
                            rs.getDate(4).toLocalDate()
                    )
            ).forEach(student -> log.info("Found <" + student + "> in the database."));
        }
    }
}