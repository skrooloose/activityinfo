/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorGroup;
import org.sigmah.shared.dto.ProvidesKey;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckCascade;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * UI Component that allows the user to select a list of Indicators
 * 
 * @author Alex Bertram
 */
public class IndicatorTreePanel extends ContentPanel {

//<<<<<<< HEAD
//    private final Dispatcher service;
//
//    private TreeLoader<ModelData> loader;
//    private TreeStore<ModelData> store;
//    private TreePanel<ModelData> tree;
//    private ToolBar toolBar;
//    private StoreFilterField filter;
//    private AsyncMonitor monitor; 
//    private boolean multipleSelection;
//    
//    public IndicatorTreePanel(Dispatcher service, final boolean multipleSelection, AsyncMonitor monitor) {
//        this.service = service;
//        this.setHeading(I18N.CONSTANTS.indicators());
//        this.setIcon(IconImageBundle.ICONS.indicator());
//        this.setLayout(new FitLayout());
//        this.setScrollMode(Style.Scroll.NONE);
//        this.monitor = monitor;
//     
//        loader = new Loader();
//        store = new TreeStore<ModelData>(loader);
//        store.setKeyProvider(new ModelKeyProvider<ModelData>() {
//            @Override
//            public String getKey(ModelData model) {
//            	if (model instanceof ProvidesKey) {
//            		return ((ProvidesKey)model).getKey();
//                } else if (model == null) {
//                	throw new RuntimeException("Did not expect model to be null: assigning keys in IndicatorTreePanel");
//                }
//                throw new RuntimeException("Unknown type: expected activity, userdb, indicator or indicatorgroup");
//            }
//        });
//
//        tree = new TreePanel<ModelData>(store);
//        
//        tree.setCheckable(true);
//        tree.getStyle().setNodeCloseIcon(null);
//        tree.getStyle().setNodeOpenIcon(null);
//        tree.setLabelProvider(new ModelStringProvider<ModelData>() {
//            public String getStringValue(ModelData model, String property) {
//                String name = model.get("name");
//                if (model instanceof IndicatorDTO) {
//                    return name;
//                } else {
//                	if (name == null) {
//                		name="noname";//I18N.CONSTANTS.noNameEntered();
//                	}
//                    return "<b>" + name + "</b>";
//                }
//            }
//        });
//        tree.setStateId("indicatorPanel");
//        tree.setStateful(true);
//        tree.setAutoSelect(true);
//        tree.addListener(Events.BrowserEvent, new Listener<TreePanelEvent<ModelData>>() {
//
//            public void handleEvent(TreePanelEvent<ModelData> be) {
//                if (be.getEventTypeInt() == Event.ONKEYPRESS) {
//                    if (!toolBar.isVisible()) {
//                        toolBar.setVisible(true);
//                    }
//                    filter.focus();
//                }
//            }
//        });
//        tree.addListener(Events.CheckChange, new Listener<TreePanelEvent<ModelData>>() {
//            public void handleEvent(TreePanelEvent<ModelData> event) {
//
//                // when a user checks a parent node, expand all
//                // child nodes
//                if (!(event.getItem() instanceof IndicatorDTO) &&
//                        event.isChecked()) {
//                    tree.setExpanded(event.getItem(), true);
//                }
//
//                // for single select, assure that only one indicator is selected
//                if (!multipleSelection && event.isChecked()) {
//                    for (ModelData model : tree.getCheckedSelection()) {
//                        if (model != event.getItem()) {
//                            tree.setChecked(model, false);
//                        }
//                    }
//                }
//            }
//        });
//        add(tree);
//        createFilterBar();
//    }
//
//    private void createFilterBar() {
//        toolBar = new ToolBar();
//        toolBar.add(new LabelToolItem(I18N.CONSTANTS.search()));
//        filter = new FilterField();
//        filter.addListener(Events.Blur, new Listener<BaseEvent>() {
//            public void handleEvent(BaseEvent be) {
//                if (filter.getRawValue() == null || filter.getRawValue().length() == 0) {
//                    toolBar.setVisible(false);
//                }
//            }
//        });
//        toolBar.add(filter);
//        toolBar.setVisible(false);
//        filter.bind(store);
//        setTopComponent(toolBar);
//    }
//
//    private class Proxy implements DataProxy<List<ModelData>> {
//        private SchemaDTO schema;
//        public void load(DataReader<List<ModelData>> listDataReader, Object parent, final AsyncCallback<List<ModelData>> callback) {
//
//            if (parent == null) {
//                service.execute(new GetSchema(), monitor, new AsyncCallback<SchemaDTO>() {
//                    public void onFailure(Throwable caught) {
//                        callback.onFailure(caught);
//                    }
//
//                    public void onSuccess(SchemaDTO result) {
//                        schema = result;
//                        callback.onSuccess(new ArrayList<ModelData>(schema.getDatabases()));
//                    }
//                });
//            } else if (parent instanceof UserDatabaseDTO) {
//                callback.onSuccess(new ArrayList<ModelData>(((UserDatabaseDTO) parent).getActivities()));
//
//            } else if (parent instanceof ActivityDTO) {
//            	callback.onSuccess(new ArrayList<ModelData>(((ActivityDTO) parent).groupIndicators()));
//            } else if (parent instanceof IndicatorGroup) {
//            	IndicatorGroup group = ((IndicatorGroup) parent);
//                ArrayList<ModelData> list = new ArrayList<ModelData>();
//                    for (IndicatorDTO indicator : group.getIndicators()) {
//                        list.add(indicator);
//                    }
//                callback.onSuccess(list);
//            }
//        }
//    }
//
//    /**
//     * @return the list of selected indicators
//     */
//    public List<IndicatorDTO> getSelection() {
//        List<IndicatorDTO> list = new ArrayList<IndicatorDTO>();
//        for (ModelData model : tree.getCheckedSelection()) {
//            if (model instanceof IndicatorDTO) {
//                list.add((IndicatorDTO) model);
//            }
//        }
//        return list;
//    }
//    
//    public void addCheckChangedListener(Listener<TreePanelEvent> listener)
//    {
//    	tree.addListener(Events.OnDoubleClick, listener);
//    }
//
//    /**
//     *
//     * @return the list of the ids of selected indicators
//     */
//    public List<Integer> getSelectedIds() {
//        List<Integer> list = new ArrayList<Integer>();
//
//        for (ModelData model : tree.getCheckedSelection()) {
//            if (model instanceof IndicatorDTO) {
//                list.add(((IndicatorDTO) model).getId());
//            }
//        }
//        return list;
//    }
//
//    public void setMultipleSelection(boolean multipleSelection) {
//=======
	private final Dispatcher service;

	private TreeLoader<ModelData> loader;
	private TreeStore<ModelData> store;
	private TreePanel<ModelData> tree;
	private ToolBar toolBar;
	private StoreFilterField filter;
	private AsyncMonitor monitor;
	private boolean multipleSelection;

	public IndicatorTreePanel(Dispatcher service,
			final boolean multipleSelection, AsyncMonitor monitor) {
		this.service = service;

		this.setHeading(I18N.CONSTANTS.indicators());
		this.setIcon(IconImageBundle.ICONS.indicator());
		this.setLayout(new FitLayout());
		this.setScrollMode(Style.Scroll.NONE);
		this.monitor = monitor;

		loader = new Loader();
		store = new TreeStore<ModelData>(loader);
		store.setKeyProvider(new ModelKeyProvider<ModelData>() {
			List<String> keys = new ArrayList<String>();

			@Override
			public String getKey(ModelData model) {
				if (model instanceof ProvidesKey) {
					return ((ProvidesKey) model).getKey();
				} else if (model == null) {
					throw new RuntimeException(
							"Did not expect model to be null: assigning keys in IndicatorTreePanel");
				}
				throw new RuntimeException(
						"Unknown type: expected activity, userdb, indicator or indicatorgroup");
			}
		});

		tree = new TreePanel<ModelData>(store);
		tree.setCheckable(true);

		setAllNodesCheckable();
		
		/*
		 * 
		 *  No. No. No! NO!! NONONO!!! Seriously. Don't.
		 *  
		 *  1. In postbacks, complete model is serialized in a cookie.
		 *  2. Cookie gets send, server can't handle cookie size of half the database
		 *  3. You wonder where the HTTP 413 comes from, and seriously consider hiring cookiemonster.
		 */
		// tree.setAutoExpand(true);
		
		tree.getStyle().setNodeCloseIcon(null);
		tree.getStyle().setNodeOpenIcon(null);
		tree.setLabelProvider(new ModelStringProvider<ModelData>() {
			public String getStringValue(ModelData model, String property) {
				String name = model.get("name");
				if (model instanceof IndicatorDTO) {
					return name;
				} else {
					if (name == null) {
						name = "noname";
					}
					return "<b>" + name + "</b>";
				}
			}
		});
		tree.setStateId("indicatorPanel");
		tree.setStateful(true);
		tree.setAutoSelect(true);
		tree.addListener(Events.BrowserEvent,
				new Listener<TreePanelEvent<ModelData>>() {

					public void handleEvent(TreePanelEvent<ModelData> be) {
						if (be.getEventTypeInt() == Event.ONKEYPRESS) {
							if (!toolBar.isVisible()) {
								toolBar.setVisible(true);
							}
							filter.focus();
						}
					}
				});

		add(tree);
		createFilterBar();
	}

	private void createFilterBar() {
		toolBar = new ToolBar();
		toolBar.add(new LabelToolItem(I18N.CONSTANTS.search()));
		filter = new FilterField();
		filter.addListener(Events.Blur, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				if (filter.getRawValue() == null
						|| filter.getRawValue().length() == 0) {
					toolBar.setVisible(false);
				}
			}
		});
		toolBar.add(filter);
		toolBar.setVisible(false);
		filter.bind(store);
		setTopComponent(toolBar);
	}

	private class Proxy implements DataProxy<List<ModelData>> {
		private SchemaDTO schema;

		public void load(DataReader<List<ModelData>> listDataReader,
				Object parent, final AsyncCallback<List<ModelData>> callback) {

			if (parent == null) {
				service.execute(new GetSchema(), monitor,
						new AsyncCallback<SchemaDTO>() {
							public void onFailure(Throwable caught) {
								callback.onFailure(caught);
							}

							public void onSuccess(SchemaDTO result) {
								schema = result;
								callback.onSuccess(new ArrayList<ModelData>(
										schema.getDatabases()));
							}
						});
			} else if (parent instanceof UserDatabaseDTO) {
				callback.onSuccess(new ArrayList<ModelData>(
						((UserDatabaseDTO) parent).getActivities()));

			} else if (parent instanceof ActivityDTO) {
				ActivityDTO acitvity = (ActivityDTO)parent;
				List<IndicatorGroup> groupIndicators = acitvity.groupIndicators();
				List<ModelData> children = new ArrayList<ModelData>();
				for (IndicatorGroup group : groupIndicators) {
					if (group.getName() == null) {
						for (IndicatorDTO indicator : group.getIndicators()) {
							children.add(indicator);
						}
					} else {
						children.add(group);
					}
				}
				callback.onSuccess(children);
				
				
			} else if (parent instanceof IndicatorGroup) {
				IndicatorGroup group = ((IndicatorGroup) parent);
				ArrayList<ModelData> list = new ArrayList<ModelData>();
				for (IndicatorDTO indicator : group.getIndicators()) {
					list.add(indicator);
				}
				callback.onSuccess(list);
			}
		}
	}

	/**
	 * @return the list of selected indicators
	 */
	public List<IndicatorDTO> getSelection() {
		List<IndicatorDTO> list = new ArrayList<IndicatorDTO>();
		for (ModelData model : tree.getCheckedSelection()) {
			if (model instanceof IndicatorDTO) {
				list.add((IndicatorDTO) model);
			}
		}
		return list;
	}

	public void addCheckChangedListener(Listener<TreePanelEvent> listener) {
		tree.addListener(Events.CheckChange, listener);
	}

	/**
	 * 
	 * @return the list of the ids of selected indicators
	 */
	public List<Integer> getSelectedIds() {
		List<Integer> list = new ArrayList<Integer>();

		for (ModelData model : tree.getCheckedSelection()) {
			if (model instanceof IndicatorDTO) {
				list.add(((IndicatorDTO) model).getId());
			}
		}
		return list;
	}

	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
		if (multipleSelection) {
			tree.setCheckStyle(TreePanel.CheckCascade.CHILDREN);
		} else {
			tree.setCheckStyle(TreePanel.CheckCascade.NONE);
		}
	}

	public IndicatorTreePanel(Dispatcher service,
			final boolean multipleSelection) {
		this(service, multipleSelection, null);
	}

	private class Loader extends BaseTreeLoader<ModelData> {
		public Loader() {
			super(new Proxy());
		}

		@Override
		public boolean hasChildren(ModelData parent) {
			return !(parent instanceof IndicatorDTO);
		}
	}

	private static class FilterField extends StoreFilterField {
		@Override
		protected boolean doSelect(Store store, ModelData parent,
				ModelData record, String property, String filter) {

			String keywords[] = filter.toLowerCase().split("\\s+");
			String name = ((String) record.get("name")).toLowerCase();
			for (String keyword : keywords) {
				if (name.indexOf(keyword) == -1) {
					return false;
				}
			}
			return true;
		}
	}

	public boolean isMultipleSelection() {
		return multipleSelection;
	}

	private boolean isSingleSelect() {
		return !multipleSelection;
	}

	public void clearSelection() {
		for (IndicatorDTO indicator : getSelection()) {
			tree.getSelectionModel().deselect(indicator);
			tree.setChecked(indicator, false);
		}
	}

	public void setChecked(IndicatorDTO indicator, boolean b) {
		tree.setChecked(indicator, b);
	}
	
	public void setLeafCheckableOnly() {
		tree.setCheckNodes(TreePanel.CheckNodes.LEAF);
	}
	
	public void setAllNodesCheckable() {
		tree.setCheckNodes(TreePanel.CheckNodes.BOTH);
		tree.setCheckStyle(CheckCascade.CHILDREN);
	}


}
