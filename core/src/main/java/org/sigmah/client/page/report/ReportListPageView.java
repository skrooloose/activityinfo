/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;


/**
 * View of the ReportList Page
 */
//public class ReportListPageView extends AbstractEditorGridView<ReportDefinitionDTO, ReportsPage>
//        implements ReportsPage.View {
//
//    private MappingComboBox<Integer> dayCombo;
//    private String[] weekdays;
//    private final Dispatcher service;
//    private final EventBus eventBus;
//    private int selectedReportId;
//    private Button editButton;
//
//    @Inject
//    public ReportListPageView(EventBus eventBus, Dispatcher service) {
//        super();
//        this.eventBus = eventBus;
//        this.service = service;
//
//        setLayout(new FitLayout());
//        setHeaderVisible(false);
//        weekdays = LocaleInfo.getCurrentLocale().getDateTimeConstants().weekdays();
//    }
//
//    @Override
//	public void init(ReportsPage presenter, ListStore<ReportDefinitionDTO> store) {
//        super.init(presenter, store);
//    }
//
//    @Override
//    protected Grid<ReportDefinitionDTO> createGridAndAddToContainer(Store store) {
//
//        EditorGrid<ReportDefinitionDTO> grid = new EditorGrid<ReportDefinitionDTO>((ListStore) store, createColumnModel());
//        grid.addListener(Events.BeforeEdit, new Listener<GridEvent<ReportDefinitionDTO>>() {
//            @Override
//			public void handleEvent(GridEvent<ReportDefinitionDTO> be) {
//                ReportDefinitionDTO report = be.getModel();
//                be.setCancelled(!(report.getFrequency() == ReportFrequency.Monthly ||
//                        report.getFrequency() == ReportFrequency.Weekly ||
//                        report.getFrequency() == ReportFrequency.Daily));
//            }
//        });
//
//        GroupingView view = new GroupingView();
//        view.setShowGroupedColumn(false);
//        view.setForceFit(true);
//        view.setGroupRenderer(new GridGroupRenderer() {
//            @Override
//			public String render(GroupColumnData data) {
//                return data.group;
//            }
//        });
//        grid.setView(view);
//
//        grid.setAutoExpandColumn("title");
//        grid.setAutoExpandMin(250);
//
//        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<ReportDefinitionDTO>>() {
//            @Override
//			public void handleEvent(GridEvent<ReportDefinitionDTO> event) {
//                if (event.getColIndex() == 1) {
//                    presenter.onTemplateSelected(event.getModel());
//                }
//            }
//        });
//        
//        grid.addListener(Events.CellClick, new Listener<GridEvent<ReportDefinitionDTO>>() {
//            @Override
//			public void handleEvent(GridEvent<ReportDefinitionDTO> event) {
//            	editButton.setEnabled(true);
//            	selectedReportId = event.getModel().getId();            
//            	}
//        });
//
//        add(grid);
//
//        return grid;
//    }
//
//    private ColumnModel createColumnModel() {
//        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
//
//        columns.add(new ColumnConfig("databaseName", I18N.CONSTANTS.database(), 100));
//
//        ColumnConfig name = new ColumnConfig("title", I18N.CONSTANTS.name(), 250);
//        name.setRenderer(new GridCellRenderer<ReportDefinitionDTO>() {
//            @Override
//			public Object render(ReportDefinitionDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
//                return "<b>" + model.getTitle() + "</b><br>" + model.getDescription();
//            }
//        });
//        columns.add(name);
//
//        ColumnConfig frequency = new ColumnConfig("frequency", I18N.CONSTANTS.reportingFrequency(), 100);
//        frequency.setRenderer(new GridCellRenderer<ReportDefinitionDTO>() {
//            @Override
//			public Object render(ReportDefinitionDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
//                if (model.getFrequency() == ReportFrequency.Monthly) {
//                    return I18N.CONSTANTS.monthly();
//                } else if (model.getFrequency() == ReportFrequency.Weekly) {
//                    return I18N.CONSTANTS.weekly();
//                } else if (model.getFrequency() == ReportFrequency.Daily) {
//                    return I18N.CONSTANTS.daily();
//                } else if (model.getFrequency() == ReportFrequency.Adhoc) {
//                    return "ad hoc";
//                }
//                return "-";
//            }
//        });
//        columns.add(frequency);
//
//        ColumnConfig day = new ColumnConfig("day", I18N.CONSTANTS.day(), 100);
//        day.setRenderer(new GridCellRenderer<ReportDefinitionDTO>() {
//            @Override
//			public Object render(ReportDefinitionDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ReportDefinitionDTO> store, Grid<ReportDefinitionDTO> grid) {
//                if (model.getFrequency() == ReportFrequency.Weekly) {
//                    return model.getDay() < 7 ? weekdays[model.getDay()] :
//                            weekdays[6];
//                } else if (model.getFrequency() == ReportFrequency.Monthly) {
//                    return Integer.toString(model.getDay());
//                } else {
//                    return "";
//                }
//            }
//        });
//        columns.add(day);
//
//        ColumnConfig subscribed = new ColumnConfig("subscribed", I18N.CONSTANTS.emailSubscription(), 100);
//        subscribed.setRenderer(new GridCellRenderer<ReportDefinitionDTO>() {
//            @Override
//            public Object render(ReportDefinitionDTO model, String property, ColumnData columnData, int rowIndex, int colIndex, ListStore<ReportDefinitionDTO> store, Grid<ReportDefinitionDTO> grid) {
//                if (model.isSubscribed()) {
//                    return I18N.CONSTANTS.yes();
//                } else {
//                    return "";
//                }
//            }
//        });
//
//        final MappingComboBox<Boolean> subCombo = new MappingComboBox();
//        subCombo.add(true, I18N.CONSTANTS.yes());
//        subCombo.add(false, I18N.CONSTANTS.no());
//
//        CellEditor subEditor = new CellEditor(subCombo) {
//            @Override
//            public Object preProcessValue(Object o) {
//                return subCombo.wrap((Boolean) o);
//            }
//
//            @Override
//            public Object postProcessValue(Object o) {
//                return ((MappingComboBox.Wrapper) o).getWrappedValue();
//            }
//        };
//        subscribed.setEditor(subEditor);
//        columns.add(subscribed);
//
//        return new ColumnModel(columns);
//    }
//
//
//    @Override
//    protected void initToolBar() {
//        toolBar.addSaveSplitButton();
//        toolBar.add(new SeparatorToolItem());
//
//        final Menu dbMenu = new Menu();
//        dbMenu.addListener(Events.BeforeShow, new Listener<BaseEvent>() {
//            @Override
//            public void handleEvent(BaseEvent be) {
//                if (dbMenu.getItemCount() == 0) {
//                    service.execute(new GetSchema(), new MaskingAsyncMonitor(ReportListPageView.this,
//                            I18N.CONSTANTS.loading()), new Got<SchemaDTO>() {
//
//                        @Override
//                        public void got(SchemaDTO result) {
//                            for (final UserDatabaseDTO db : result.getDatabases()) {
//                                MenuItem item = new MenuItem(db.getName());
//                                item.setIcon(IconImageBundle.ICONS.database());
//                                item.addListener(Events.Select, new Listener<BaseEvent>() {
//                                    @Override
//                                    public void handleEvent(BaseEvent be) {
//                                        presenter.onNewReport(db.getId());
//                                    }
//                                });
//                                dbMenu.add(item);
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//        Button newButton = new Button(I18N.CONSTANTS.newText(), IconImageBundle.ICONS.add());
//        newButton.setMenu(dbMenu);
//        toolBar.add(newButton);
//        
//        editButton = new Button(I18N.CONSTANTS.edit(), IconImageBundle.ICONS.edit());
//        editButton.addListener(Events.OnClick, new Listener<BaseEvent>(){
//        	@Override
//        	public void handleEvent(BaseEvent be){
//        		eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new ReportDesignPageState(selectedReportId)));        		
//        	}
//        });
//        editButton.setEnabled(false);
//        toolBar.add(editButton);
//    }
//
//
//}