
package com.github.cecchisandrone.smarthome.client.slack;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "is_channel", "created", "creator", "is_archived", "is_general", "is_member",
		"members", "topic", "purpose", "num_members" })
public class Channel {

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("is_channel")
	private Boolean isChannel;
	@JsonProperty("created")
	private Integer created;
	@JsonProperty("creator")
	private String creator;
	@JsonProperty("is_archived")
	private Boolean isArchived;
	@JsonProperty("is_general")
	private Boolean isGeneral;
	@JsonProperty("is_member")
	private Boolean isMember;
	@JsonProperty("members")
	private List<String> members = null;
	@JsonProperty("num_members")
	private Integer numMembers;

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The isChannel
	 */
	@JsonProperty("is_channel")
	public Boolean getIsChannel() {
		return isChannel;
	}

	/**
	 * 
	 * @param isChannel
	 *            The is_channel
	 */
	@JsonProperty("is_channel")
	public void setIsChannel(Boolean isChannel) {
		this.isChannel = isChannel;
	}

	/**
	 * 
	 * @return The created
	 */
	@JsonProperty("created")
	public Integer getCreated() {
		return created;
	}

	/**
	 * 
	 * @param created
	 *            The created
	 */
	@JsonProperty("created")
	public void setCreated(Integer created) {
		this.created = created;
	}

	/**
	 * 
	 * @return The creator
	 */
	@JsonProperty("creator")
	public String getCreator() {
		return creator;
	}

	/**
	 * 
	 * @param creator
	 *            The creator
	 */
	@JsonProperty("creator")
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 
	 * @return The isArchived
	 */
	@JsonProperty("is_archived")
	public Boolean getIsArchived() {
		return isArchived;
	}

	/**
	 * 
	 * @param isArchived
	 *            The is_archived
	 */
	@JsonProperty("is_archived")
	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}

	/**
	 * 
	 * @return The isGeneral
	 */
	@JsonProperty("is_general")
	public Boolean getIsGeneral() {
		return isGeneral;
	}

	/**
	 * 
	 * @param isGeneral
	 *            The is_general
	 */
	@JsonProperty("is_general")
	public void setIsGeneral(Boolean isGeneral) {
		this.isGeneral = isGeneral;
	}

	/**
	 * 
	 * @return The isMember
	 */
	@JsonProperty("is_member")
	public Boolean getIsMember() {
		return isMember;
	}

	/**
	 * 
	 * @param isMember
	 *            The is_member
	 */
	@JsonProperty("is_member")
	public void setIsMember(Boolean isMember) {
		this.isMember = isMember;
	}

	/**
	 * 
	 * @return The members
	 */
	@JsonProperty("members")
	public List<String> getMembers() {
		return members;
	}

	/**
	 * 
	 * @param members
	 *            The members
	 */
	@JsonProperty("members")
	public void setMembers(List<String> members) {
		this.members = members;
	}

	/**
	 * 
	 * @return The numMembers
	 */
	@JsonProperty("num_members")
	public Integer getNumMembers() {
		return numMembers;
	}

	/**
	 * 
	 * @param numMembers
	 *            The num_members
	 */
	@JsonProperty("num_members")
	public void setNumMembers(Integer numMembers) {
		this.numMembers = numMembers;
	}

}