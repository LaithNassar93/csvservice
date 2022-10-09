package miu.edu.csvservice.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/job")
@CrossOrigin
public class JobController {
    @Autowired
    JobLauncher asyncJobLauncher;

    @Autowired
    Job processJob;

    @GetMapping
    public String job() {
        try{
            asyncJobLauncher.run(processJob, new JobParameters());
        } catch (JobInstanceAlreadyCompleteException e) {
            return "Job is already completed.";
        } catch (JobExecutionAlreadyRunningException e) {
            return "Job is already running.";
        } catch (JobParametersInvalidException e) {
            return "Job parameters are invalid";
        } catch (JobRestartException e) {
            return "Job cannot be restarted";
        }
        return "Job successfully launched!";
    }
}