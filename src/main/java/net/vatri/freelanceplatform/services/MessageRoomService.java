package net.vatri.freelanceplatform.services;

import net.vatri.freelanceplatform.models.MessageRoom;
import net.vatri.freelanceplatform.models.User;
import net.vatri.freelanceplatform.models.Job;
import net.vatri.freelanceplatform.repositories.MessageRoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageRoomService {

	@Autowired
	MessageRoomRepository messageRoomRepository;

	public MessageRoom save(MessageRoom message) {
		return messageRoomRepository.save(message);
	}

	public MessageRoom get(Long id) {
		return messageRoomRepository.findOne(id);
	}
    
    public MessageRoom findByJobAndContractor(Job job, User contractor) {
    	return messageRoomRepository.findByJobAndSenderOrReceiver(job, contractor);
    }

	public List<MessageRoom> getRoomsByUser(User me) {
		List<MessageRoom> allMessageRooms = messageRoomRepository.findBySenderOrReceiver(me);
		List<MessageRoom> result = new ArrayList<MessageRoom>();
		//todo: filter out duplicated (show as rooms)
		
		Map<String,MessageRoom> uniqueRooms = new HashMap<String, MessageRoom>();
		
		
		allMessageRooms.forEach(mr -> {
			
			// Unique hash map key "job-contributor"
			String key = mr.getJob() != null ? String.valueOf( mr.getJob().getId()) : "X";
			key += '-';
			key += (mr.getReceiver().getId() == me.getId() 
						? mr.getSender().getId() 
						: mr.getReceiver().getId());
			
			// If room not exist in result add it to unique list
			MessageRoom m2 = uniqueRooms.get(key);
			if(m2 == null) {
				uniqueRooms.put(key, mr);
			}
			
		});
		
		for(String t_key  : uniqueRooms.keySet()) {
			result.add(uniqueRooms.get(t_key));
		}
		
		return result;
	}

	public List<MessageRoom> findByMyConversers(User me, User converser) {
		return messageRoomRepository.findByMyConversers(me,converser);
	}

}
