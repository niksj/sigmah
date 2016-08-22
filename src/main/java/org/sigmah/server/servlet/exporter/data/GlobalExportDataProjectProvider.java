package org.sigmah.server.servlet.exporter.data;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.sigmah.server.dao.GlobalExportDAO;
import org.sigmah.server.dao.ProjectDAO;
import org.sigmah.server.dao.impl.GlobalExportHibernateDAO;
import org.sigmah.server.dispatch.CommandHandler;
import org.sigmah.server.domain.Organization;
import org.sigmah.server.domain.PhaseModel;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.ProjectFunding;
import org.sigmah.server.domain.ProjectModel;
import org.sigmah.server.domain.category.CategoryType;
import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.server.domain.element.QuestionElement;
import org.sigmah.server.domain.layout.LayoutConstraint;
import org.sigmah.server.domain.layout.LayoutGroup;
import org.sigmah.server.handler.GetLayoutGroupIterationsHandler;
import org.sigmah.server.handler.GetValueHandler;
import org.sigmah.server.i18n.I18nServer;
import org.sigmah.server.servlet.base.ServletExecutionContext;
import org.sigmah.server.servlet.exporter.data.cells.GlobalExportDataCell;
import org.sigmah.server.servlet.exporter.data.cells.GlobalExportLinkCell;
import org.sigmah.server.servlet.exporter.data.cells.GlobalExportStringCell;
import org.sigmah.server.servlet.exporter.data.columns.GlobalExportDataColumn;
import org.sigmah.server.servlet.exporter.data.columns.GlobalExportFlexibleElementColumn;
import org.sigmah.server.servlet.exporter.data.columns.GlobalExportIterativeGroupColumn;
import org.sigmah.server.servlet.exporter.utils.ExporterUtil;
import org.sigmah.shared.Language;
import org.sigmah.shared.command.GetLayoutGroupIterations;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.result.ListResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.layout.LayoutGroupIterationDTO;
import org.sigmah.shared.dto.referential.ProjectModelStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class Provides global export data
 * 
 * @author sherzod (v1.3)
 */
@Singleton
public class GlobalExportDataProjectProvider extends GlobalExportDataProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExportDataProvider.class);

	private final Injector injector;

	@Inject
	public GlobalExportDataProjectProvider(final Injector injector) {
		super();
		this.injector = injector;
	}

	public Map<String, List<GlobalExportDataCell[]>> generateGlobalExportData(final Integer organizationId, EntityManager entityManager, final I18nServer i18nTranslator,
			final Language language, final ServletExecutionContext context) throws Exception {
		if (entityManager == null) {
			entityManager = injector.getInstance(EntityManager.class);
		}

		final GlobalExportDAO exportDAO = injector.getInstance(GlobalExportHibernateDAO.class);
		final Organization organization = entityManager.find(Organization.class, organizationId);
		final List<ProjectModel> pModels = exportDAO.getProjectModelsByOrganization(organization);

		final ProjectDAO projectDao = injector.getInstance(ProjectDAO.class);
		final List<Project> projects = projectDao.getProjects(pModels);

		// project model and its projects
		final Map<String, List<Project>> pModelProjectsMap = new HashMap<String, List<Project>>();
		for (final Project project : projects) {
			if (project.getDateDeleted() == null) {
				final String pModelName = project.getProjectModel().getName();

				List<Project> pModelProjects = pModelProjectsMap.get(pModelName);
				if (pModelProjects == null) {
					pModelProjects = new ArrayList<Project>();
					pModelProjectsMap.put(pModelName, pModelProjects);
				}
				pModelProjects.add(project);
			}
		}

		// project model and its globally exportable fields
		final Map<String, List<GlobalExportDataColumn>> pModelElementsMap = new TreeMap<String, List<GlobalExportDataColumn>>();
		for (final ProjectModel projectModel : pModels) {
			if (projectModel.getStatus() != ProjectModelStatus.DRAFT) {
				final String pModelName = projectModel.getName();

				final List<GlobalExportDataColumn> pModelElements = new ArrayList<GlobalExportDataColumn>();
				pModelElementsMap.put(pModelName, pModelElements);

				// detail elements
				ExporterUtil.fillElementList(pModelElements, projectModel.getProjectDetails().getLayout());

				// phase elements
				for (final PhaseModel phaseModel : projectModel.getPhaseModels()) {
					ExporterUtil.fillElementList(pModelElements, phaseModel.getLayout());
				}
			}
		}

		// final CommandHandler<GetValue, ValueResult> handler = new GetValueHandler();
		final CommandHandler<GetValue, ValueResult> handler = injector.getInstance(GetValueHandler.class);
		final CommandHandler<GetLayoutGroupIterations, ListResult<LayoutGroupIterationDTO>> iterationsHandler = injector.getInstance(GetLayoutGroupIterationsHandler.class);

		final Map<String, List<GlobalExportDataCell[]>> pModelExportDataMap = new LinkedHashMap<String, List<GlobalExportDataCell[]>>();

		// categories
		final Set<CategoryType> categories = new HashSet<>();

		// collect export data
		for (final String pModelName : pModelElementsMap.keySet()) {

			// if no project for a given project model, skip even headers for
			// flexible elements
			if (pModelProjectsMap.get(pModelName) == null)
				continue;

			final List<GlobalExportDataColumn> elements = pModelElementsMap.get(pModelName);
			final List<GlobalExportDataCell[]> exportData = new ArrayList<GlobalExportDataCell[]>();
			pModelExportDataMap.put(pModelName, exportData);

			// field titles
			final List<GlobalExportDataCell> titles = new ArrayList<GlobalExportDataCell>();

			// layout group tabs
			final Map<String, List<GlobalExportDataCell[]>> layoutGroupsData = new LinkedHashMap<>();

			// special fields for BI
			titles.add(new GlobalExportStringCell(i18nTranslator.t(language, "permanentId")));
			titles.add(new GlobalExportStringCell(i18nTranslator.t(language, "projectActivePhase")));

			boolean isFirstLine = true;
			// projects
			for (final Project project : pModelProjectsMap.get(pModelName)) {

				final List<GlobalExportDataCell> values = new ArrayList<GlobalExportDataCell>();

				// special fields for BI
				values.add(new GlobalExportStringCell(String.valueOf(project.getId())));
				if (project.getCloseDate() == null) {
					values.add(new GlobalExportStringCell(project.getCurrentPhase().getPhaseModel().getName()));
				} else {
					values.add(new GlobalExportStringCell(i18nTranslator.t(language, "closedProject")));
				}

				// fields
				for (final GlobalExportDataColumn column : elements) {

					if (column instanceof GlobalExportFlexibleElementColumn) {

						FlexibleElement element = ((GlobalExportFlexibleElementColumn)column).getFlexibleElement();

						// command to get element value
						final String elementName = "element." + element.getClass().getSimpleName();
						final GetValue command = new GetValue(project.getId(), element.getId(), elementName, null);

						try {

							final ValueResult valueResult = handler.execute(command, null);

							// prepare value and label
							ExporterUtil.ValueLabel pair = null;
						  /* DEF FLEXIBLE */
							if (elementName.equals("element.DefaultFlexibleElement")) {
								pair = ExporterUtil.getDefElementPair(valueResult, element, project, entityManager, i18nTranslator, language);

							} else /* BUDGET */ if (elementName.equals("element.BudgetElement")) {
								// budget is a special case where the element corresponds to 3 columns
								if (isFirstLine) {
									ExporterUtil.addBudgetTitles(titles, element, i18nTranslator, language);
								}
								ExporterUtil.addBudgetValues(values, valueResult, element, i18nTranslator, language);
								continue;
							} else /* CHECKBOX */if (elementName.equals("element.CheckboxElement")) {
								pair = ExporterUtil.getCheckboxElementPair(valueResult, element, i18nTranslator, language);
							} else /* TEXT AREA */if (elementName.equals("element.TextAreaElement")) {
								pair = ExporterUtil.getTextAreaElementPair(valueResult, element);

							}/* TRIPLET */
							if (elementName.equals("element.TripletsListElement")) {
								pair = ExporterUtil.getTripletPair(element, valueResult);

							}/* CHOICE */
							if (elementName.equals("element.QuestionElement")) {
								// choice is a special case where the element corresponds to 2 columns and 1 additional tab
								if (isFirstLine) {
									ExporterUtil.addChoiceTitles(titles, categories, element, i18nTranslator, language);
								}
								ExporterUtil.addChoiceValues(values, valueResult, element);
								continue;
							}

							// titles

							if (isFirstLine) {
								titles.add(new GlobalExportStringCell(pair != null ? pair.getFormattedLabel() : null));
							}

							// values
							values.add(new GlobalExportStringCell(ExporterUtil.pairToValueString(pair)));

						} catch (Exception e) {
							LOGGER.error("Failed to get the value of element '" + element.getId() + "' of project '" + project.getId() + "'.", e);
						}

					} else if (column instanceof GlobalExportIterativeGroupColumn) {
						LayoutGroup group = ((GlobalExportIterativeGroupColumn)column).getLayoutGroup();
						List<LayoutConstraint> allConstraints = group.getConstraints();
						List<LayoutConstraint> constraints = new ArrayList<>();

						// keeping only exportable constraints
						for (LayoutConstraint constraint : allConstraints) {
							if (constraint.getElement().isGloballyExportable()) {
								constraints.add(constraint);
							}
						}

						if (constraints.isEmpty()) {
							continue;
						}

						// command to get element value
						final String groupName = pModelName + "_" + group.getTitle();
						final GetLayoutGroupIterations command = new GetLayoutGroupIterations(group.getId(), project.getId(), -1);

						try {

							if (isFirstLine) {
								titles.add(new GlobalExportStringCell(group.getTitle()));

								// iterative layout group columns titles
								ArrayList<GlobalExportDataCell[]> groupTitles = new ArrayList<>();
								List<GlobalExportDataCell> columns = new ArrayList<GlobalExportDataCell>();

								columns.add(new GlobalExportStringCell(i18nTranslator.t(language, "projectId")));
								columns.add(new GlobalExportStringCell(i18nTranslator.t(language, "projectCode")));
								columns.add(new GlobalExportStringCell(i18nTranslator.t(language, "projectTitle")));
								columns.add(new GlobalExportStringCell(i18nTranslator.t(language, "iterationName")));

								for (LayoutConstraint constraint : constraints) {
									FlexibleElement element = constraint.getElement();
									String elementName = element.getClass().getSimpleName();
									if (elementName.equals("QuestionElement")) {
										// choice is a special case where the element corresponds to 2 columns and 1 additional tab
										final QuestionElement questionElement = (QuestionElement) element;
										String choiceLabel = element.getLabel();

										columns.add(new GlobalExportStringCell(choiceLabel));
										if (questionElement.getCategoryType() != null) {
											columns.add(new GlobalExportStringCell(choiceLabel + " (" + questionElement.getCategoryType().getLabel() + ") " + i18nTranslator.t(language, "categoryId")));
											categories.add(((QuestionElement) element).getCategoryType());
										}
									} else {
										columns.add(new GlobalExportStringCell(element.getLabel()));
									}
								}
								groupTitles.add(columns.toArray(new GlobalExportDataCell[columns.size()]));
								layoutGroupsData.put(groupName, groupTitles);
							}

							final ListResult<LayoutGroupIterationDTO> iterationsResult = iterationsHandler.execute(command, null);

							values.add(new GlobalExportLinkCell(String.valueOf(iterationsResult.getSize()), groupName));

							// iterative layout group values
							List<GlobalExportDataCell[]> groupValues = layoutGroupsData.get(groupName);
							for (LayoutGroupIterationDTO iteration : iterationsResult.getList()) {
								List<GlobalExportDataCell> columns = new ArrayList<>();
								// default columns
								columns.add(new GlobalExportStringCell(String.valueOf(project.getId())));
								columns.add(new GlobalExportStringCell(project.getName()));
								columns.add(new GlobalExportStringCell(project.getFullName()));
								columns.add(new GlobalExportStringCell(iteration.getName()));

								for (LayoutConstraint constraint : constraints) {
									FlexibleElement element = constraint.getElement();
									String elementName = element.getClass().getSimpleName();
									GetValue cmd = new GetValue(project.getId(), constraint.getElement().getId(), "element." + constraint.getElement().getClass().getSimpleName(), null, iteration.getId());
									try {
										final ValueResult iterationValueResult = handler.execute(cmd, null);

										// prepare value and label
										ExporterUtil.ValueLabel pair = null;
						  			/* CHECKBOX */
										if (elementName.equals("CheckboxElement")) {
											pair = ExporterUtil.getCheckboxElementPair(iterationValueResult, element, i18nTranslator, language);
										} else /* TEXT AREA */if (elementName.equals("TextAreaElement")) {
											pair = ExporterUtil.getTextAreaElementPair(iterationValueResult, element);
										}/* TRIPLET */
										if (elementName.equals("TripletsListElement")) {
											pair = ExporterUtil.getTripletPair(element, iterationValueResult);
										}/* CHOICE */
										if (elementName.equals("QuestionElement")) {
											// choice is a special case where the element corresponds to 2 columns and 1 additional tab
											ExporterUtil.ChoiceValue choiceValue = new ExporterUtil.ChoiceValue((QuestionElement) element, iterationValueResult);

											columns.add(new GlobalExportStringCell(choiceValue.getValueLabels()));
											if (((QuestionElement)element).getCategoryType() != null) {
												columns.add(new GlobalExportStringCell(choiceValue.getValueIds()));
											}
											continue;
										}
										columns.add(new GlobalExportStringCell(ExporterUtil.pairToValueString(pair)));

									} catch (Exception e) {
										// no value found in database : empty cells
										columns.add(new GlobalExportStringCell(""));
										if (elementName.equals("QuestionElement")) {
											columns.add(new GlobalExportStringCell(""));
										}
									}
								}

								groupValues.add(columns.toArray(new GlobalExportDataCell[columns.size()]));
							}

						} catch (Exception e) {
							LOGGER.error("Failed to get iterations of group '" + group.getId() + "' of project '" + project.getId() + "'.", e);
						}
					}
				}

				// add titles
				if (isFirstLine) {
					exportData.add(titles.toArray(new GlobalExportDataCell[titles.size()]));
					isFirstLine = false;
				}

				// add values
				exportData.add(values.toArray(new GlobalExportDataCell[values.size()]));

				// add iterative layout groups tabs
				for(String groupName : layoutGroupsData.keySet()) {
					pModelExportDataMap.put(groupName, layoutGroupsData.get(groupName));
				}

			}// projects

		}

		addProjectFundings(projects, pModelExportDataMap, i18nTranslator, language);

		addCategories(categories, pModelExportDataMap, i18nTranslator, language);

		return pModelExportDataMap;
	}

	private void addProjectFundings(List<Project> projects, Map<String, List<GlobalExportDataCell[]>> exportDataMap, I18nServer i18nTranslator, Language language) {
		List<GlobalExportDataCell[]> dataFundings = new ArrayList<>();

		GlobalExportDataCell[] row = new GlobalExportDataCell[7];
		row[0] = new GlobalExportStringCell(String.valueOf(i18nTranslator.t(language, "fundingId")));
		row[1] = new GlobalExportStringCell(String.valueOf(i18nTranslator.t(language, "fundingCode")));
		row[2] = new GlobalExportStringCell(String.valueOf(i18nTranslator.t(language, "fundingTitle")));
		row[3] = new GlobalExportStringCell(String.valueOf(i18nTranslator.t(language, "fundedId")));
		row[4] = new GlobalExportStringCell(String.valueOf(i18nTranslator.t(language, "fundedCode")));
		row[5] = new GlobalExportStringCell(String.valueOf(i18nTranslator.t(language, "fundedTitle")));
		row[6] = new GlobalExportStringCell(String.valueOf(i18nTranslator.t(language, "fundingAmount")));
		dataFundings.add(row);

		for(Project project : projects) {
			List<ProjectFunding> fundings = project.getFunded();
			if(project.getFunded() != null) {
				for(ProjectFunding funding : fundings) {
					row = new GlobalExportDataCell[7];
					row[0] = new GlobalExportStringCell(String.valueOf(project.getId()));
					row[1] = new GlobalExportStringCell(project.getName());
					row[2] = new GlobalExportStringCell(project.getFullName());
					row[3] = new GlobalExportStringCell(String.valueOf(funding.getFunded().getId()));
					row[4] = new GlobalExportStringCell(funding.getFunded().getName());
					row[5] = new GlobalExportStringCell(funding.getFunded().getFullName());
					row[6] = new GlobalExportStringCell(String.valueOf(funding.getPercentage()));
					dataFundings.add(row);
				}
			}
		}

		exportDataMap.put(i18nTranslator.t(language, "projectsFundings"), dataFundings);
	}

}
