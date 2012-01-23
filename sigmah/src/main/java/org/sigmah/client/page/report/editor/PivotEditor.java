package org.sigmah.client.page.report.editor;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.table.AbstractPivot;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.google.inject.Inject;

public class PivotEditor extends AbstractPivot implements AbstractEditor {

	private PivotTableReportElement table;
	
	@Inject
	public PivotEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr) {
		super(eventBus, service, stateMgr);
		
		addIndicatorPanelListener();
		addPartnerPanelListener();
		addAdminPanelListener();
	}

	private void addIndicatorPanelListener() {
		indicatorPanel.addListenerToStore(Store.DataChanged,
				new Listener<BaseEvent>() {

					@Override
					public void handleEvent(BaseEvent be) {

						if (table != null) {
							
							Set<Integer> indicators = table.getFilter().getRestrictions(
									DimensionType.Indicator);
							Iterator<Integer> itr = indicators.iterator();

							while (itr.hasNext()) {
								indicatorPanel.setSelection(itr.next(), true);
							}
						}

					}
				});
	}
	

	private void addPartnerPanelListener() {
		partnerPanel.addListenerToStore(Store.DataChanged,
				new Listener<BaseEvent>() {

					@Override
					public void handleEvent(BaseEvent be) {

						if (table != null) {
							Set<Integer> partners = table.getFilter().getRestrictions(
									DimensionType.Partner);
							Iterator<Integer> itr = partners.iterator();

							while (itr.hasNext()) {
								partnerPanel.setSelection(itr.next(), true);
							}
						}

					}
				});
	}
	
	private void addAdminPanelListener() {
		adminPanel.addListenerToStore(Store.DataChanged,
				new Listener<BaseEvent>() {

					@Override
					public void handleEvent(BaseEvent be) {

						if (table != null) {
							Set<Integer> adminLevels = table.getFilter().getRestrictions(
									DimensionType.AdminLevel);
							
							Iterator<Integer> itr = adminLevels.iterator();

							while (itr.hasNext()) {
								adminPanel.setSelection(itr.next(), true);
							}
						}

					}
				});
	}
	
	public void bindReportElement(final PivotTableReportElement table) {

		this.table = table;
		Date minDate = table.getFilter().getMinDate();
		datePanel.setMinDate(minDate);

		Date maxDate = table.getFilter().getMaxDate();
		datePanel.setMaxDate(maxDate);
		
		List<Dimension> colDim = table.getColumnDimensions();
		getColStore().add(colDim);

		List<Dimension> rowDim = table.getRowDimensions();
		getRowStore().add(colDim);

		
		onUIAction(UIActions.REFRESH);
	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public void bindReportElement(ReportElement element) {
		bindReportElement(element);
	}

	@Override
	public ReportElement getReportElement() {
		return createElement();
	}

}