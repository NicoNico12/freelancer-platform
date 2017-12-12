package net.vatri.freelanceplatform.frontend.controllers;

import net.vatri.freelanceplatform.helpers.FreelancePlatformHelper;
import net.vatri.freelanceplatform.models.Bid;
import net.vatri.freelanceplatform.models.Job;
import net.vatri.freelanceplatform.models.User;
import net.vatri.freelanceplatform.services.BidService;
import net.vatri.freelanceplatform.services.CategoryService;
import net.vatri.freelanceplatform.services.JobService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/job")
public class JobController extends AbstractController{

    @Autowired
    JobService jobService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BidService bidService;

    @GetMapping
    public String listJobs(Model model, HttpServletRequest request){
    	
    	String filt = request.getParameter("filter");
    	User me = getCurrentUser();
    	boolean isMyJobsPage = false;
    	
    	if( filt != null && filt.equals("myjobs") && me != null) {
    		Map<String, Object> filter = new HashMap<>();
    		filter.put("user", me );
            model.addAttribute("jobs", jobService.list(filter));
            
            isMyJobsPage = true;
    	} else {
    		model.addAttribute("jobs", jobService.list());
    	}
    	
    	model.addAttribute("isMyJobsPage", isMyJobsPage);
    	
        return "frontend/job/jobs";
    }

    @GetMapping("/view/{id}")
    public String viewJob(Model model, @PathVariable("id") long id){

        Job job = jobService.get(id);

        model.addAttribute("job", job);

        // Get my bid for the job and assign to the view
        Bid myBid = null;

        // Check if logged in:
        User currentUser = super.getCurrentUser();
        if( currentUser != null){
            myBid = bidService.getUsersBidByJob(currentUser, job);
            if(myBid != null) {
	        	// New line to <br>
	            myBid.setProposal(FreelancePlatformHelper.nl2br(myBid.getProposal()));
            }
        }

        model.addAttribute("myBid", myBid);
        
        model.addAttribute("me", getCurrentUser());

        return "frontend/job/view_job";
    }

    @GetMapping("/create")
    public String createJob(Model model){
        model.addAttribute("categories", categoryService.list());
        return "frontend/job/create_job";
    }

    @PostMapping("/save")
    public String saveJob(
            @RequestParam(name = "id", required = false) Long id,
            @ModelAttribute Job job,
            Model model){

        if(job.getTitle().isEmpty() /*|| 1==1*/){
            model.addAttribute("error", "Title required");
            return "frontend/job/create_job";
        }

        // Set current loged user ID as author
        User author = super.getCurrentUser();
        if(author == null){
            System.out.println("Please login to add a job!");
            return null;
        }
        job.setAuthor(author);

        Job savedJob = null;
        if( id != null && id > 0){
// TODO
        } else {
            savedJob = jobService.add(job);
        }
        return "redirect:/job/view/" + savedJob.getId();
    }

    @GetMapping("/bids/{jobId}")
    public String viewBids(Model model, @PathVariable("jobId") long jobId) {
    	
    	Job job = jobService.get(jobId);
    	
    	User me = getCurrentUser();
    	
    	if( job == null || job.getAuthor().getId() != me.getId() ) {
    		System.out.println("Job not found or you don't have privileges");
    		return "redirect:/job/view/" + jobId;
    	}
    	
    	List<Bid> bids = bidService.findByJob(job);
    	
    	model.addAttribute("job", job);
    	model.addAttribute("bids", bids);
    	
    	// If me != author
    	return "frontend/job/view_bids";
    }

   

}
