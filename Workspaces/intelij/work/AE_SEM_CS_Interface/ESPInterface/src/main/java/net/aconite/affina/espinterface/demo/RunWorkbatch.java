package net.aconite.affina.espinterface.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunWorkbatch
{

    public static void main(String[] args)
    {

        RunWorkbatch obj = new RunWorkbatch();
        obj.run();

    }

    private void run()
    {

        //String[] springConfig = {"spring/batch/jobs/job-extract-users.xml"};

        //ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
        
        ConfigurableApplicationContext espContext = new ClassPathXmlApplicationContext("/test/spring/batch-config.xml");
        
        JobLauncher jobLauncher = (JobLauncher) espContext.getBean("jobLauncher");
        Job job = (Job) espContext.getBean("testJob");

        try
        {

            JobParameters param = new JobParametersBuilder().addString("age", "20").toJobParameters();
            //JobParameters param = new JobParametersBuilder().addString("name", "user_c").toJobParameters();

            JobExecution execution = jobLauncher.run(job, param);
            
            System.out.println("Exit Status : " + execution.getStatus());
            System.out.println("Exit Status : " + execution.getAllFailureExceptions());

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        System.out.println("Done");

    }

}
