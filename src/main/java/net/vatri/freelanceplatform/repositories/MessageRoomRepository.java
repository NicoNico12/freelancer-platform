package net.vatri.freelanceplatform.repositories;

import net.vatri.freelanceplatform.models.Job;
import net.vatri.freelanceplatform.models.MessageRoom;
import net.vatri.freelanceplatform.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


// TODO try do this without join

@Repository("messageRoomRepository")
public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
	
	List<MessageRoom> findByJob(Job job);

	@Query("SELECT mr"
			+ " FROM MessageRoom mr "
			+ " WHERE mr.job = :job AND ( mr.sender = :contractor OR mr.receiver = :contractor )")
	MessageRoom findByJobAndSenderOrReceiver(
		@Param("job") Job job,
		@Param("contractor") User contractor
	);

	@Query("SELECT mr"
			+ " FROM MessageRoom mr "
			+ " WHERE mr.sender = :user OR mr.receiver = :user "
			+ " ORDER BY mr.id DESC")
	List<MessageRoom> findBySenderOrReceiver(@Param("user") User me);

	@Query("SELECT mr"
			+ " FROM MessageRoom mr"
			+ " WHERE "
				+ "( ( mr.sender = :me AND mr.receiver = :converser ) "
				+ " OR "
				+ " ( mr.sender = :converser AND mr.receiver = :me ) ) "
				+ " AND mr.job is null "
			+ " ORDER BY mr.id DESC")
	List<MessageRoom> findByMyConversers(@Param("me") User me, @Param("converser") User converser);

}