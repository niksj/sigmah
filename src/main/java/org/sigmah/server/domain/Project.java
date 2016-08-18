package org.sigmah.server.domain;

/*
 * #%L
 * Sigmah
 * %%
 * Copyright (C) 2010 - 2016 URD
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.sigmah.server.domain.logframe.LogFrame;
import org.sigmah.server.domain.profile.Profile;
import org.sigmah.server.domain.reminder.MonitoredPointList;
import org.sigmah.server.domain.reminder.ReminderList;
import org.sigmah.server.domain.util.EntityConstants;
import org.sigmah.shared.dto.referential.AmendmentState;
import org.sigmah.shared.dto.referential.ContainerInformation;

/**
 * <p>
 * Project domain entity.
 * </p>
 * <p>
 * Inherits {@link UserDatabase} entity.
 * </p>
 *
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = EntityConstants.PROJECT_TABLE)
public class Project extends UserDatabase {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 3838595995254049090L;

	/**
	 * The ID of the calendar attached to this project.
	 */
	@Column(name = EntityConstants.PROJECT_COLUMN_CALENDAR_ID)
	private Integer calendarId;

	@Column(name = EntityConstants.PROJECT_COLUMN_END_DATE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = EntityConstants.PROJECT_COLUMN_CLOSE_DATE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date closeDate;

	@Column(name = EntityConstants.PROJECT_COLUMN_AMENDMENT_VERSION, nullable = true)
	private Integer amendmentVersion;

	@Column(name = EntityConstants.PROJECT_COLUMN_AMENDMENT_REVISION, nullable = true)
	private Integer amendmentRevision;

	@Column(name = EntityConstants.PROJECT_COLUMN_ACTIVITY_ADVANCEMENT)
	private Integer activityAdvancement;

	@Column(name = EntityConstants.PROJECT_COLUMN_AMENDMENT_STATUS, nullable = true)
	@Enumerated(EnumType.STRING)
	private AmendmentState amendmentState;

	// --------------------------------------------------------------------------------
	//
	// FOREIGN KEYS.
	//
	// --------------------------------------------------------------------------------

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = EntityConstants.PROJECT_COLUMN_USER_MANAGER_ID, nullable = true)
	private User manager;

	// Trick: using '@ManyToOne' to avoid automatic load of the object (see '@OneToOne' lazy issue).
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = EntityConstants.PROJECT_MODEL_COLUMN_ID)
	private ProjectModel projectModel;

	// Trick: using '@ManyToOne' to avoid automatic load of the object (see '@OneToOne' lazy issue).
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = EntityConstants.PROJECT_COLUMN_CURRENT_PHASE_ID)
	private Phase currentPhase;

	// Trick: using '@ManyToOne' to avoid automatic load of the object (see '@OneToOne' lazy issue).
	@ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = EntityConstants.PROJECT_COLUMN_MONITORED_POINT_LIST_ID, nullable = true)
	private MonitoredPointList pointsList;

	// Trick: using '@ManyToOne' to avoid automatic load of the object (see '@OneToOne' lazy issue).
	@ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = EntityConstants.REMINDER_LIST_COLUMN_ID, nullable = true)
	private ReminderList remindersList;

	// Cannot use '@ManyToOne' trick ; id is set into remote table.
	@OneToOne(mappedBy = "parentProject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private LogFrame logFrame;

	@OneToMany(mappedBy = "parentProject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Phase> phases = new ArrayList<Phase>();

	@ManyToMany(mappedBy = "funded", cascade = CascadeType.ALL)
	private List<ProjectFunding> funding;

	@ManyToMany(mappedBy = "funding", cascade = CascadeType.ALL)
	private List<ProjectFunding> funded;

	@OneToMany(mappedBy = "parentProject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("date ASC")
	private List<Amendment> amendments = new ArrayList<Amendment>();

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = EntityConstants.PROJECT_COLUMN_USER_LINK_TABLE)
	protected Set<User> favoriteUsers;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "mainSite", nullable = true)
	private Site mainSite;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = EntityConstants.PROJECT_COLUMN_TEAM_MEMBERS_LINK_TABLE,
			joinColumns = @JoinColumn(name = EntityConstants.PROJECT_COLUMN_ID, referencedColumnName = EntityConstants.USER_DATABASE_COLUMN_ID),
			inverseJoinColumns = @JoinColumn(name = EntityConstants.USER_COLUMN_ID, referencedColumnName = EntityConstants.USER_COLUMN_ID),
			uniqueConstraints = @UniqueConstraint(columnNames = {
					EntityConstants.PROJECT_COLUMN_ID,
					EntityConstants.USER_COLUMN_ID
			})
	)
	private List<User> teamMembers = new ArrayList<User>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = EntityConstants.PROJECT_COLUMN_TEAM_MEMBER_PROFILES_LINK_TABLE,
			joinColumns = @JoinColumn(name = EntityConstants.PROJECT_COLUMN_ID, referencedColumnName = EntityConstants.USER_DATABASE_COLUMN_ID),
			inverseJoinColumns = @JoinColumn(name = EntityConstants.PROFILE_COLUMN_ID, referencedColumnName = EntityConstants.PROFILE_COLUMN_ID),
			uniqueConstraints = @UniqueConstraint(columnNames = {
					EntityConstants.PROJECT_COLUMN_ID,
					EntityConstants.PROFILE_COLUMN_ID
			})
	)
	private List<Profile> teamMemberProfiles = new ArrayList<Profile>();

	// --------------------------------------------------------------------------------
	//
	// METHODS.
	//
	// --------------------------------------------------------------------------------

	/**
	 * Adds a phase to the project.
	 *
	 * @param phase
	 *          The new phase.
	 */
	public void addPhase(Phase phase) {

		if (phase == null) {
			return;
		}

		phases.add(phase);
		phase.setParentProject(this);
	}

	/**
	 * Returns a serializable object with basic information about this object.
	 *
	 * @return Basic information about this project as a ContainerInformation instance.
	 */
	public ContainerInformation toContainerInformation() {
		return new ContainerInformation(getId(), getName(), getFullName(), true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void appendToString(final ToStringBuilder builder) {
		super.appendToString(builder);
		builder.append("calendarId", calendarId);
		builder.append("endDate", endDate);
		builder.append("closeDate", closeDate);
		builder.append("amendmentVersion", amendmentVersion);
		builder.append("amendmentRevision", amendmentRevision);
		builder.append("activityAdvancement", activityAdvancement);
	}

	// --------------------------------------------------------------------------------
	//
	// GETTERS & SETTERS.
	//
	// --------------------------------------------------------------------------------

	public Set<User> getFavoriteUsers() {
		return favoriteUsers;
	}

	public void setFavoriteUsers(Set<User> favoriteUsers) {
		this.favoriteUsers = favoriteUsers;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setLogFrame(LogFrame logFrame) {
		this.logFrame = logFrame;
	}

	public LogFrame getLogFrame() {
		return logFrame;
	}

	public ProjectModel getProjectModel() {
		return projectModel;
	}

	public void setProjectModel(ProjectModel model) {
		this.projectModel = model;
	}

	public Phase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase;
	}

	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

	public Integer getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(Integer calendarId) {
		this.calendarId = calendarId;
	}

	public List<ProjectFunding> getFunding() {
		return funding;
	}

	public void setFunding(List<ProjectFunding> funding) {
		this.funding = funding;
	}

	public List<ProjectFunding> getFunded() {
		return funded;
	}

	public void setFunded(List<ProjectFunding> funded) {
		this.funded = funded;
	}

	public MonitoredPointList getPointsList() {
		return pointsList;
	}

	public void setPointsList(MonitoredPointList pointsList) {
		this.pointsList = pointsList;
	}

	public ReminderList getRemindersList() {
		return remindersList;
	}

	public void setRemindersList(ReminderList remindersList) {
		this.remindersList = remindersList;
	}

	public User getManager() {
		return this.manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public AmendmentState getAmendmentState() {
		return amendmentState;
	}

	public void setAmendmentState(AmendmentState state) {
		this.amendmentState = state;
	}

	public List<Amendment> getAmendments() {
		return amendments;
	}

	public void setAmendments(List<Amendment> amendments) {
		this.amendments = amendments;
	}

	public Integer getAmendmentVersion() {
		return amendmentVersion;
	}

	public void setAmendmentVersion(Integer amendmentVersion) {
		this.amendmentVersion = amendmentVersion;
	}

	public Integer getAmendmentRevision() {
		return amendmentRevision;
	}

	public void setAmendmentRevision(Integer amendmentRevision) {
		this.amendmentRevision = amendmentRevision;
	}

	public Integer getActivityAdvancement() {
		return activityAdvancement;
	}

	public void setActivityAdvancement(Integer activityAdvancement) {
		this.activityAdvancement = activityAdvancement;
	}

	public Site getMainSite() {
		return mainSite;
	}

	public void setMainSite(Site site) {
		this.mainSite = site;
	}

	public List<User> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(List<User> teamMembers) {
		this.teamMembers = teamMembers;
	}

	public List<Profile> getTeamMemberProfiles() {
		return teamMemberProfiles;
	}

	public void setTeamMemberProfiles(List<Profile> teamMemberProfiles) {
		this.teamMemberProfiles = teamMemberProfiles;
	}

	@Transient
	public OrgUnit getOrgUnit() {
		// Get the first org unit
		for (OrgUnit orgUnit : getPartners()) {
			return orgUnit;
		}
		return null;
	}
}
