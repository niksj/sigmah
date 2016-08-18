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
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.sigmah.server.domain.base.AbstractEntityId;
import org.sigmah.server.domain.util.EntityConstants;

@Entity
@Table(name = EntityConstants.FRAMEWORK_HIERARCHY_TABLE)
public class FrameworkHierarchy extends AbstractEntityId<Integer> {
	private static final long serialVersionUID = -1164326083857156896L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = EntityConstants.FRAMEWORK_HIERARCHY_COLUMN_ID)
	private Integer id;

	@Column(name = EntityConstants.FRAMEWORK_HIERARCHY_COLUMN_LABEL, nullable = false)
	@NotNull
	private String label;

	@Column(name = EntityConstants.FRAMEWORK_HIERARCHY_COLUMN_LEVEL, nullable = false)
	@Min(value = 0)
	private int level;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = EntityConstants.FRAMEWORK_COLUMN_ID, nullable = false)
	private Framework framework;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = EntityConstants.FRAMEWORK_HIERARCHY_PARENT_HIERARCHY, nullable = true)
	private FrameworkHierarchy parentHierarchy;

	@OneToMany(mappedBy = "parentHierarchy", fetch = FetchType.LAZY)
	@OrderBy("label ASC")
	private List<FrameworkHierarchy> childrenHierarchies = new ArrayList<>();

	@OneToMany(mappedBy = "frameworkHierarchy", fetch = FetchType.LAZY)
	@OrderBy("label ASC")
	private List<FrameworkElement> frameworkElements = new ArrayList<>();

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Framework getFramework() {
		return framework;
	}

	public void setFramework(Framework framework) {
		this.framework = framework;
	}

	public FrameworkHierarchy getParentHierarchy() {
		return parentHierarchy;
	}

	public void setParentHierarchy(FrameworkHierarchy parentHierarchy) {
		this.parentHierarchy = parentHierarchy;
	}

	public List<FrameworkHierarchy> getChildrenHierarchies() {
		return childrenHierarchies;
	}

	public void setChildrenHierarchies(List<FrameworkHierarchy> childrenHierarchies) {
		this.childrenHierarchies = childrenHierarchies;
	}

	public List<FrameworkElement> getFrameworkElements() {
		return frameworkElements;
	}

	public void setFrameworkElements(List<FrameworkElement> frameworkElements) {
		this.frameworkElements = frameworkElements;
	}
}
