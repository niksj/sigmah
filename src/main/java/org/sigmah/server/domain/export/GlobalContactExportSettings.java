package org.sigmah.server.domain.export;

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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.sigmah.server.domain.Organization;
import org.sigmah.server.domain.base.AbstractEntityId;
import org.sigmah.server.domain.util.EntityConstants;
import org.sigmah.shared.util.ExportUtils;

@Entity
@Table(name = EntityConstants.GLOBAL_CONTACT_EXPORT_SETTINGS_TABLE)
public class GlobalContactExportSettings extends AbstractEntityId<Integer> {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -2722884637221828205L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_ID)
	private Integer id;

	/**
	 * Used only for global level exports.
	 */
	@Column(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_EXPORT_FORMAT, nullable = true)
	@Enumerated(EnumType.STRING)
	private ExportUtils.ExportFormat exportFormat;

	/**
	 * Used for all levels of exports.
	 */
	@Column(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_DEFAULT_ORG_EXPORT_FORMAT, nullable = true)
	@Enumerated(EnumType.STRING)
	private ExportUtils.ExportFormat defaultOrganizationExportFormat;

	@Column(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_LAST_EXPORT_DATE)
	private Date lastExportDate;

	/**
	 * In days.
	 */
	@Column(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_AUTO_EXPORT_FREQUENCY)
	private Integer autoExportFrequency;

	/**
	 * In months.
	 */
	@Column(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_AUTO_DELETE_FREQUENCY)
	private Integer autoDeleteFrequency;

	@Column(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_LOCALE, length = EntityConstants.GLOBAL_EXPORT_SETTINGS_LOCALE_MAX_LENGTH, nullable = false)
	@NotNull
	@Size(max = EntityConstants.GLOBAL_EXPORT_SETTINGS_LOCALE_MAX_LENGTH)
	private String locale;

	// --------------------------------------------------------------------------------
	//
	// FOREIGN KEYS.
	//
	// --------------------------------------------------------------------------------

	@ManyToOne
	@JoinColumn(name = EntityConstants.GLOBAL_EXPORT_SETTINGS_COLUMN_ORGANIZATION_ID, nullable = false)
	@NotNull
	private Organization organization;

	// --------------------------------------------------------------------------------
	//
	// METHODS.
	//
	// --------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void appendToString(final ToStringBuilder builder) {
		builder.append("exportFormat", exportFormat);
		builder.append("defaultOrganizationExportFormat", defaultOrganizationExportFormat);
		builder.append("lastExportDate", lastExportDate);
		builder.append("autoExportFrequency", autoExportFrequency);
		builder.append("autoDeleteFrequency", autoDeleteFrequency);
		builder.append("locale", locale);
	}

	// --------------------------------------------------------------------------------
	//
	// GETTERS & SETTERS.
	//
	// --------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public ExportUtils.ExportFormat getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(ExportUtils.ExportFormat exportFormat) {
		this.exportFormat = exportFormat;
	}

	public ExportUtils.ExportFormat getDefaultOrganizationExportFormat() {
		return defaultOrganizationExportFormat;
	}

	public void setDefaultOrganizationExportFormat(ExportUtils.ExportFormat defaultOrganizationExportFormat) {
		this.defaultOrganizationExportFormat = defaultOrganizationExportFormat;
	}

	public Date getLastExportDate() {
		return lastExportDate;
	}

	public void setLastExportDate(Date lastExportDate) {
		this.lastExportDate = lastExportDate;
	}

	public Integer getAutoExportFrequency() {
		return autoExportFrequency;
	}

	public void setAutoExportFrequency(Integer autoExportFrequency) {
		this.autoExportFrequency = autoExportFrequency;
	}

	public Integer getAutoDeleteFrequency() {
		return autoDeleteFrequency;
	}

	public void setAutoDeleteFrequency(Integer autoDeleteFrequency) {
		this.autoDeleteFrequency = autoDeleteFrequency;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
