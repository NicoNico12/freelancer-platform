package net.vatri.freelanceplatform.services;

import net.vatri.freelanceplatform.models.Job;
import net.vatri.freelanceplatform.models.User;
import net.vatri.freelanceplatform.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    JobRepository jobRepository;

    public Job get(long id){
        return jobRepository.findOne(id);
    }

    public Job add(Job job){

        job.setType( job.getType().toLowerCase() );
        job.setExpertizeLevel( job.getExpertizeLevel().toLowerCase() );

        return jobRepository.save(job);
    }

    public List<Job> list(){

        List<Job> result = jobRepository.findAll();

        result.sort( (j1,j2) -> {
            return j1.getId() > j2.getId() ? -1 : 0;
        } );

        return result;
    }
    
    public List<Job> list(Map<String,Object> filter){

        List<Job> result = null; //jobRepository.findAll();
        User usr = (User) filter.get("user");
        
        if( usr != null) {
        	result = jobRepository.findByAuthor(usr);
        }

        if(result != null) {
	        result.sort( (j1,j2) -> {
	            return j1.getId() > j2.getId() ? -1 : 0;
	        } );
        }

        return result;
    }

}
