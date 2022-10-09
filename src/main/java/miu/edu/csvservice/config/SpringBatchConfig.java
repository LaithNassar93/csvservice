package miu.edu.csvservice.config;

import miu.edu.csvservice.domain.Student;
import miu.edu.csvservice.model.StudentCSV;
import miu.edu.csvservice.repository.StudentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private JobRepository jobRepository;


   @Bean
    public FlatFileItemReader<StudentCSV> reader() {
        FlatFileItemReader<StudentCSV> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setName("studentItemReader");
        fileItemReader.setResource(new ClassPathResource("Student.csv"));
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setLineMapper(new DefaultLineMapper() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("firstname", "lastname", "gpa", "age");
                    }

                    {
                        setDelimiter(",");
                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCSV>() {
                    {
                        setTargetType(StudentCSV.class);
                    }
                });
            }
        });
        return fileItemReader;

    }
    @Bean
    public StudentItemProcessor processor() {
        return new StudentItemProcessor();
    }


    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importStudentJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    RepositoryItemWriter<Student> repositoryItemWriter() {
        RepositoryItemWriter<Student> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(studentRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;

    }

    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<StudentCSV, Student> chunk(15)
                .reader(reader())
                .processor(processor())
                .writer(repositoryItemWriter())
                .build();
    }

    @Bean
    public JobLauncher asyncJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

}