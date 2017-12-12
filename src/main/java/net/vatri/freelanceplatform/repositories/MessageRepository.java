package net.vatri.freelanceplatform.repositories;

import net.vatri.freelanceplatform.models.Message;
import net.vatri.freelanceplatform.models.MessageRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


// TODO try do this without join

@Repository("messageRepository")
public interface MessageRepository extends JpaRepository<Message, Long> {
	
	List<Message> findByMessageRoom(MessageRoom room);

	List<Message> findByMessageRoom(List<MessageRoom> rooms);
	
//	List<Message> findByJob(Job job);

//	@Query("SELECT m"
//			+ " FROM Message m "
//			+ " INNER JOIN m.messageRoom mr "
//			+ " WHERE mr.job = :job AND ( mr.sender = :contractor OR mr.receiver = :contractor )")
//	List<Message> findByJobAndSenderOrReceiver(
//		@Param("job") Job job,
//		@Param("contractor") User contractor
//	);
//
//	@Query("SELECT m"
//			+ " FROM Message m "
//			+ " INNER JOIN m.messageRoom mr "
//			+ " WHERE mr.sender = :user OR mr.receiver = :user "
//			+ " ORDER BY m.id DESC")
//	List<Message> findBySenderOrReceiver(@Param("user") User me);
//
//	@Query("SELECT m"
//			+ " FROM Message m"
//			+ " INNER JOIN m.messageRoom mr "
//			+ " WHERE "
//				+ "( ( mr.sender = :me AND mr.receiver = :converser ) "
//				+ " OR "
//				+ " ( mr.sender = :converser AND mr.receiver = :me ) ) "
//				+ " AND mr.job is null "
//			+ " ORDER BY m.id DESC")
//	List<Message> findByMyConversers(@Param("me") User me, @Param("converser") User converser);

}