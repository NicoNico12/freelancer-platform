package net.vatri.freelanceplatform.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private MessageRoom messageRoom;

	@Column(length = 64000)
	@Size(min = 2)
	private String text;

	private Date created;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MessageRoom getMessageRoom() {
		return messageRoom;
	}

	public void setMessageRoom(MessageRoom messageRoom) {
		this.messageRoom = messageRoom;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@PrePersist
	public void postPersist() {
		getMessageRoom().setLastMessage(this);
		setCreated(new Date());
	}
	

}
