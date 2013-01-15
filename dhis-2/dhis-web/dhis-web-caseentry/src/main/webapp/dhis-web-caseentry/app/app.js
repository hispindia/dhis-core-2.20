TR.conf = {
    init: {
		ajax: {
			jsonfy: function(r) {
				r = Ext.JSON.decode(r.responseText);
				var obj = { 
					system: {
						maxLevels: r.levels.length
					}
				};
				obj.system.rootnodes = [];
				for (var i = 0; i < r.user.ous.length; i++) {
					obj.system.rootnodes.push({id: r.user.ous[i].id, text: r.user.ous[i].name, leaf: r.user.ous[i].leaf});
				}
				
				obj.system.program = [];
				for (var i = 0; i < r.programs.length; i++) {
					obj.system.program.push({id: r.programs[i].id, name: r.programs[i].name, type: r.programs[i].type });
				}
				
				obj.system.orgunitGroup = [];
				for (var i = 0; i < r.orgunitGroups.length; i++) {
					obj.system.orgunitGroup.push({id: r.orgunitGroups[i].id, name: r.orgunitGroups[i].name });
				}
				
				obj.system.level = [];
				for (var i = 0; i < r.levels.length; i++) {
					obj.system.level.push({value: r.levels[i].value, name: r.levels[i].name});
				}
				
				return obj;
			}
		}
    },
    finals: {
        ajax: {
			path_lib: '../../dhis-web-commons/javascripts/',
            path_root: '../',
            path_commons: '../',
            path_api: '../../api/',
            path_images: 'images/',
			initialize: 'tabularInitialize.action',
			programstages_get: 'loadReportProgramStages.action',
			dataelements_get: 'loadDataElements.action',
			organisationunitchildren_get: 'getOrganisationUnitChildren.action',
			generatetabularreport_get: 'generateTabularReport.action',
			casebasedfavorite_getall: 'getTabularReports.action',
			casebasedfavorite_get: 'getTabularReport.action',
			casebasedfavorite_rename: 'updateTabularReportName.action',
			casebasedfavorite_save: 'saveTabularReport.action',
            casebasedfavorite_delete: 'deleteTabularReport.action',
			suggested_dataelement_get: 'getOptions.action',
			aggregatefavorite_getall: 'getAggregateReports.action',
			aggregatefavorite_get: 'getAggregateReport.action',
			aggregatefavorite_rename: 'updateAggregateReportName.action',
			aggregatefavorite_save: 'saveAggregateReport.action',
            aggregatefavorite_delete: 'deleteAggregateReport.action',
			generateaggregatereport_get: 'generateAggregateReport.action',
			redirect: 'index.action'
        },
        params: {
            data: {
                value: 'data',
                rawvalue: TR.i18n.regular_program,
                warning: {
					filter: TR.i18n.wm_multiple_filter_ind_de
				}
            },
            program: {
                value: 'program',
                rawvalue: TR.i18n.program
            },
            organisationunit: {
                value: 'organisationunit',
                rawvalue: TR.i18n.organisation_unit,
                warning: {
					filter: TR.i18n.wm_multiple_filter_orgunit
				}
            },
            patientProperties: {
                value: 'patientProperties',
                rawvalue: TR.i18n.patientProperties
            },
            programStage: {
                value: 'programStage',
                rawvalue: TR.i18n.program_stage
            },
            dataelement: {
                value: 'dataelement',
                rawvalue: TR.i18n.data_elements
            }
        },
        data: {
			domain: 'domain_',
		},
		download: {
            xls: 'xls',
			pdf: 'pdf',
			csv: 'csv'
        },
        cmd: {
            init: 'init_',
            none: 'none_',
			urlparam: 'id'
        }
    },
	period: {
		relativeperiodunits: {
			reportingMonth: 1,
			last3Months: 3,
			last12Months: 12,
			reportingQuarter: 1,
			last4Quarters: 4,
			lastSixMonth: 1,
			last2SixMonths: 2,
			thisYear: 1,
			lastYear: 1,
			last5Years: 5
		},
		periodtypes: [
			{id: 'Daily', name: 'Daily'},
			{id: 'Weekly', name: 'Weekly'},
			{id: 'Monthly', name: 'Monthly'},
			{id: 'BiMonthly', name: 'BiMonthly'},
			{id: 'Quarterly', name: 'Quarterly'},
			{id: 'SixMonthly', name: 'SixMonthly'},
			{id: 'Yearly', name: 'Yearly'},
			{id: 'FinancialOct', name: 'FinancialOct'},
			{id: 'FinancialJuly', name: 'FinancialJuly'},
			{id: 'FinancialApril', name: 'FinancialApril'}
		]
	},
	reportPosition: {
		POSITION_ROW_ORGUNIT_COLUMN_PERIOD: 1,
		POSITION_ROW_PERIOD_COLUMN_ORGUNIT: 2,
		POSITION_ROW_ORGUNIT_ROW_PERIOD: 3,
		POSITION_ROW_PERIOD: 4,
		POSITION_ROW_ORGUNIT: 5,
		POSITION_ROW_PERIOD_COLUMN_DATA: 6,
		POSITION_ROW_ORGUNIT_COLUMN_DATA: 7,
		POSITION_ROW_DATA: 8
	},
    statusbar: {
		icon: {
			error: 'error_s.png',
			warning: 'warning.png',
			ok: 'ok.png'
		}
	},
    layout: {
        west_width: 424,
        west_fieldset_width: 402,
		west_multiselect: 100,
        west_width_subtractor: 18,
        west_fill: 117,
        west_fill_accordion_organisationunit: 43,
        west_maxheight_accordion_organisationunit: 225,
        center_tbar_height: 31,
        east_gridcolumn_height: 30,
        form_label_width: 90,
		grid_favorite_width: 450,
		grid_favorite_height: 500,
        window_favorite_ypos: 100,
        window_confirm_width: 250,
		window_record_width: 450,
		window_record_height: 300
    }
};

Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', TR.conf.finals.ajax.path_lib + 'ext-ux');
Ext.require('Ext.ux.form.MultiSelect');
Ext.require([
    'Ext.ux.grid.FiltersFeature'
]);


Ext.onReady( function() {
    Ext.override(Ext.form.FieldSet,{setExpanded:function(a){var b=this,c=b.checkboxCmp,d=b.toggleCmp,e;a=!!a;if(c){c.setValue(a)}if(d){d.setType(a?"up":"down")}if(a){e="expand";b.removeCls(b.baseCls+"-collapsed")}else{e="collapse";b.addCls(b.baseCls+"-collapsed")}b.collapsed=!a;b.doComponentLayout();b.fireEvent(e,b);return b}});
    Ext.QuickTips.init();
    document.body.oncontextmenu = function(){return false;}; 
    
    Ext.Ajax.request({
        url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.initialize,
        success: function(r) {
            
    TR.init = TR.conf.init.ajax.jsonfy(r);    
    TR.init.initialize = function() {        
        TR.init.cmd = TR.util.getUrlParam(TR.conf.finals.cmd.urlparam) || TR.conf.finals.cmd.init;
    };
    
    TR.cmp = {
        region: {},
        settings: {},
        params: {
            program:{},
			programStage: {},
			dataelement: {},
			organisationunit: {},
			relativeperiod: {
				checkbox: []
			},
			fixedperiod: {},
			dataElementGroupBy:{}
        },
        options: {},
        toolbar: {
            menuitem: {}
        },
        statusbar: {},
        favorite: {
            rename: {}
        }
    };
    
    TR.util = {
        getUrlParam: function(s) {
            var output = '';
            var href = window.location.href;
            if (href.indexOf('?') > -1 ) {
                var query = href.substr(href.indexOf('?') + 1);
                var query = query.split('&');
                for (var i = 0; i < query.length; i++) {
                    if (query[i].indexOf('=') > -1) {
                        var a = query[i].split('=');
                        if (a[0].toLowerCase() === s) {
							output = a[1];
							break;
						}
                    }
                }
            }
            return unescape(output);
        },
        viewport: {
            getSize: function() {
                return {x: TR.cmp.region.center.getWidth(), y: TR.cmp.region.center.getHeight()};
            },
            getXY: function() {
                return {x: TR.cmp.region.center.x + 15, y: TR.cmp.region.center.y + 43};
            },
            getPageCenterX: function(cmp) {
                return ((screen.width/2)-(cmp.width/2));
            },
            getPageCenterY: function(cmp) {
                return ((screen.height/2)-((cmp.height/2)-100));
            },
            resizeParams: function() {
				var a = [TR.cmp.params.dataelement.panel, 
						 TR.cmp.params.organisationunit.treepanel];
				for (var i = 0; i < a.length; i++) {
					if (!a[i].collapsed) {
						a[i].fireEvent('expand');
					}
				}
			}
        },
        multiselect: {
            select: function(a, s, f) {
                var selected = a.getValue();
				var idx = a.store.findExact('id', selected);
				var name = a.store.getAt(idx).data.name;
				var valueType = a.store.getAt(idx).data.valueType;
				
                if (selected.length) {
                    var array = [];
                    Ext.Array.each(selected, function(item) {
						var data = a.store.findExact('id', item);
                        array.push({id: item, name: a.store.getAt(data).data.name, compulsory: a.store.getAt(data).data.compulsory, valueType: a.store.getAt(data).data.valueType});
                    });
                    s.store.add(array);
                }
                this.filterAvailable(a, s);
				
				if(f)
				{
					this.addFilterField( 'filterPanel', selected[0], name, valueType );
				}
            },
            selectAll: function(a, s, f) {
				var array = [];
				var elements = a.boundList.all.elements;
				for( var i=0; i< elements.length; i++ )
				{
					if( elements[i].style.display != 'none' )
					{
						var id = a.store.getAt(i).data.id;
						var name = a.store.getAt(i).data.name;
						var valueType = a.store.getAt(i).data.valueType;
						
						array.push({id: id, name: name, compulsory: a.store.getAt(i).data.compulsory, valueType: valueType});
						this.addFilterField( 'filterPanel', id, name, valueType );
					}
				}
				s.store.add(array);
                this.filterAvailable(a, s);
            },            
            unselect: function(a, s, f) {
                var selected = s.getValue();
                if (selected.length) {
                    Ext.Array.each(selected, function(item) {
                        s.store.remove(s.store.getAt(s.store.findExact('id', item)));
                    });                    
                    this.filterAvailable(a, s);
                }
				if(f)
				{
					this.removeFilterField( 'filterPanel', selected[0], a.store.getAt(a.store.findExact('id', selected)).data.valueType );
				}
            },
            unselectAll: function(a, s, f) {
                var elements = s.boundList.all.elements;
				var index = 0;
				var arr = [];
				Ext.Array.each(s.store.data.items, function(item) {
					if( elements[index].style.display != 'none' )
					{
					  arr.push(item.data.id);
					  TR.util.multiselect.removeFilterField( 'filterPanel', item.data.id, item.data.valueType );
					}
					index++;
				}); 
				s.setValue(arr);
				this.unselect(a,s);
            },
            filterAvailable: function(a, s) {
				a.store.filterBy( function(r) {
                    var filter = true;
                    s.store.each( function(r2) {
                        if (r.data.id == r2.data.id) {
                            filter = false;
                        }
                    });
                    return filter;
                });
            },
			filterSelector: function(selectors, queryString) {
                var elements = selectors.boundList.all.elements;

				for( var i=0; i< elements.length; i++ )
				{
					if( elements[i].innerHTML.toLowerCase().indexOf( queryString ) != -1 )
					{
						elements[i].style.display = 'block';
					}
					else
					{
						elements[i].style.display = 'none';
					}
				}
            },
            setHeight: function(ms, panel, fill) {
				for (var i = 0; i < ms.length; i++) {
					ms[i].setHeight(panel.getHeight() - 49);
				}
			},
			addFilterField: function( p, id, name, valueType ){
				var xtype = TR.value.covertXType(valueType);
				
				var fields = [];
				fields[0] = {
					xtype: 'label',
					id: 'filter_lb_' + id,
					text:name,
					width:(TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2
				};
				fields[1] = this.createOperatorField(valueType, id);
				fields[2] = this.createFilterField( xtype, id, name );
				
				if(valueType != 'string' && valueType != 'trueOnly' 
					&& valueType != 'bool' && valueType != 'list' ){
					id = id + '_1';
					fields[3] = {
						xtype: 'label',
						id: 'filter_lb_' + id,
						text: '-',
						width:(TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2
					};
					fields[4] = this.createOperatorField(valueType, id);
					fields[5] = this.createFilterField( xtype, id, name );
				}
				Ext.getCmp(p).add(fields);
			},
			removeFilterField: function( p, id, valueType ){
				var e1 = Ext.getCmp( 'filter_' + id );
				var e2 = Ext.getCmp( 'filter_opt_' + id );
				var e3 = Ext.getCmp( 'filter_lb_' + id );
				
				Ext.getCmp(p).remove(e1);
				Ext.getCmp(p).remove(e2);
				Ext.getCmp(p).remove(e3);
				
				if(valueType != 'string' && valueType != 'trueOnly' && valueType != 'bool'){
					id = id + '_1';
					var e4 = Ext.getCmp( 'filter_' + id );
					var e5 = Ext.getCmp( 'filter_opt_' + id );
					var e6 = Ext.getCmp( 'filter_lb_' + id );
					Ext.getCmp(p).remove(e4);
					Ext.getCmp(p).remove(e5);
					Ext.getCmp(p).remove(e6);
				}
				
				Ext.getCmp(p).doLayout();
			},
			createFilterField: function( xtype, id ){
				var params = {};
				params.xtype = xtype;
				params.id = 'filter_' + id;
				params.cls = 'tr-textfield-alt1';
				params.emptyText = TR.i18n.enter_filter_value;
				params.width = (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2 - 70;
				xtype = xtype.toLowerCase();
				if( xtype == 'datefield' )
				{
					params.format = TR.i18n.format_date;
				}
				else if( xtype == 'bool')
				{
					params.queryMode = 'local';
					params.editable = false;
					params.valueField = 'value';
					params.displayField = 'name';
					params.selectOnFocus = true;
					params.store = new Ext.data.ArrayStore({
						fields: ['value', 'name'],
						data: [['true', TR.i18n.true_value], ['false', TR.i18n.false_value]]
					});
				}
				else if( xtype == 'combobox' )
				{
					var deId = id.split('_')[1];
					params.typeAhead = true;
					params.forceSelection = true;
					params.queryMode = 'remote';
					params.valueField = 'o';
					params.displayField = 'o';
					params.store = Ext.create('Ext.data.Store', {
						fields: ['o'],
						data:[],
						proxy: {
							type: 'ajax',
							url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.suggested_dataelement_get,
							extraParams:{id: deId},
							reader: {
								type: 'json',
								root: 'options'
							}
						}
					});		
				}
				return params;
			},
			createOperatorField: function( valueType, id){
				var params = {};
				params.xtype = 'combobox';
				params.id = 'filter_opt_' + id;
				params.width = 40;
				params.queryMode = 'local';
				params.valueField = 'value';
				params.displayField = 'name';
				params.value = '=';
				
				if(valueType == 'string' || valueType == 'trueOnly' 
					|| valueType == 'bool' && valueType != 'list' ){
					params.store = new Ext.data.ArrayStore({
						fields: ['value','name'],
						data: [ ['=','='] ]
					});
				}
				else
				{
					params.store = new Ext.data.ArrayStore({
						fields: ['value','name'],
						data: [ ['=','='],
								['>','>'],
								['>=','>='],
								['<','<'],
								['<=','<='],
								['!=','!=' ] ]
					});
				}
				
				return params;
			}
        },
        positionFilter:{
			orgunit: function() {
				var o = TR.cmp.settings.positionOrgunit.value;
				
				// Orgunit is columns
				if( o==1 )
				{
					var periodStore = TR.cmp.settings.positionPeriod.store;
					periodStore.removeAll();
					periodStore.add (
						{value: 1,name: TR.i18n.rows},
						{value: 2,name: TR.i18n.columns},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionPeriodCbx').setValue( 1 );
				}
				// Orgunit is columns
				else if( o==2 )
				{
					var periodStore = TR.cmp.settings.positionPeriod.store;
					periodStore.removeAll();
					periodStore.add ({value: 1,name: TR.i18n.rows});
					Ext.getCmp('positionPeriodCbx').setValue( 1 );
					
					var dataStore = TR.cmp.settings.positionData.store;
					dataStore.removeAll();
					dataStore.add ({value: 3,name: TR.i18n.filters});
					Ext.getCmp('positionDataCbx').setValue( 3 );
				}
				// Orgunit is filters
				else if( o==3)
				{
					var periodStore = TR.cmp.settings.positionPeriod.store;
					periodStore.removeAll();
					periodStore.add (
						{value: 1,name: TR.i18n.rows},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionPeriodCbx').setValue( 1 );
					
					var dataStore = TR.cmp.settings.positionData.store;
					dataStore.removeAll();
					dataStore.add (
						{value: 2,name: TR.i18n.columns},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionDataCbx').setValue( 2 );
				}
			},
			period: function()
			{
				// Orgunit is column
				var o = TR.cmp.settings.positionOrgunit.value;
				var p = TR.cmp.settings.positionPeriod.value;
				
				var dataStore = TR.cmp.settings.positionData.store;
				dataStore.removeAll();
					
				if( o==1 ){
					if( p==1 || p==2 ){
						dataStore.add (
							{value: 3,name: TR.i18n.filters}
						);
					}
					else if( p==3 ){
						dataStore.add (
							{value: 2,name: TR.i18n.columns},
							{value: 3,name: TR.i18n.filters}
						);
					}
					Ext.getCmp('positionDataCbx').setValue( 3 );
				}
				else if( o==3 && p==1 ){
					dataStore.add (
						{value: 2,name: TR.i18n.columns},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionDataCbx').setValue( 2 );
				}
				else if( o==3 && p==3 ){
					var dataStore = TR.cmp.settings.positionData.store;
					dataStore.removeAll();
					dataStore.add (
						{value: 1,name: TR.i18n.rows}
					);
					Ext.getCmp('positionDataCbx').setValue( 1 );
				}
			}
		},
		store: {
            addToStorage: function(s, records) {
                s.each( function(r) {
                    if (!s.storage[r.data.id]) {
                        s.storage[r.data.id] = {id: r.data.id, name: TR.util.string.getEncodedString(r.data.name), parent: s.parent};
                    }
                });
                if (records) {
                    Ext.Array.each(records, function(r) {
                        if (!s.storage[r.data.id]) {
                            s.storage[r.data.id] = {id: r.data.id, name: TR.util.string.getEncodedString(r.data.name), parent: s.parent};
                        }
                    });
                }                        
            },
            loadFromStorage: function(s) {
                var items = [];
                s.removeAll();
                for (var obj in s.storage) {
                    if (s.storage[obj].parent === s.parent) {
                        items.push(s.storage[obj]);
                    }
                }
                s.add(items);
                s.sort('name', 'ASC');
            },
            containsParent: function(s) {
                for (var obj in s.storage) {
                    if (s.storage[obj].parent === s.parent) {
                        return true;
                    }
                }
                return false;
            }
        },
        notification: {
			error: function(title, text) {
				title = title || '';
				text = text || '';
				Ext.create('Ext.window.Window', {
					title: title,
					cls: 'tr-messagebox',
					iconCls: 'tr-window-title-messagebox',
					modal: true,
					width: 300,
					items: [
						{
							xtype: 'label',
							width: 40,
							text: text
						}
					]
				}).show();
				TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
				TR.cmp.statusbar.panel.update('<img src="' + TR.conf.finals.ajax.path_images + TR.conf.statusbar.icon.error + '" style="padding:0 5px 0 0"/>' + text);
			},
			warning: function(text) {
				text = text || '';
				TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
				TR.cmp.statusbar.panel.update('<img src="' + TR.conf.finals.ajax.path_images + TR.conf.statusbar.icon.warning + '" style="padding:0 5px 0 0"/>' + text);
			},
			ok: function() {
				TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
				TR.cmp.statusbar.panel.update('<img src="' + TR.conf.finals.ajax.path_images + TR.conf.statusbar.icon.ok + '" style="padding:0 5px 0 0"/>&nbsp;&nbsp;');
			}				
		},
        mask: {
            showMask: function(cmp, str) {
                if (TR.mask) {
                    TR.mask.destroy();
                }
                TR.mask = new Ext.LoadMask(cmp, {msg: str});
                TR.mask.show();
            },
            hideMask: function() {
				if (TR.mask) {
					TR.mask.hide();
				}
			}
        },
		/*FIXME:This is probably not going to work as intended with UNICODE?*/
        string: {
            getEncodedString: function(text) {
                return text.replace(/[^a-zA-Z 0-9(){}<>_!+;:?*&%#-]+/g,'');
            }
        },
        getValueFormula: function( value ){
			if( value.indexOf('"') != value.lastIndexOf('"') )
			{
				value = value.replace(/"/g,"'");
			}
			// if key is [xyz] && [=xyz]
			if( value.indexOf("'")==-1 ){
				var flag = value.match(/[>|>=|<|<=|=|!=]+[ ]*/);
			
				if( flag == null )
				{
					value = "='"+ value + "'";
				}
				else
				{
					value = value.replace( flag, flag + "'");
					value +=  "'";
				}
			}
			// if key is ['xyz'] && [='xyz']
			// if( value.indexOf("'") != value.lastIndexOf("'") )
			else
			{
				var flag = value.match(/[>|>=|<|<=|=|!=]+[ ]*/);
			
				if( flag == null )
				{
					value = "="+ value;
				}
			}
			return value;
		},
        crud: {
            favorite: {
                create: function(fn, isupdate) {
					var url = "";
					if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_save;
					}
					else
					{
						url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_save;
					}
					
					TR.util.mask.showMask(TR.cmp.favorite.window, TR.i18n.saving + '...');
					var p = TR.state.getParams();
					p.name = TR.cmp.favorite.name.getValue();
					
					if (isupdate) {
						p.uid = TR.store.favorite.getAt(TR.store.favorite.findExact('name', p.name)).data.id;
					}
					
					Ext.Ajax.request({
						url: url,
						method: 'POST',
						params: p,
						success: function() {
							TR.store.favorite.load({callback: function() {
								TR.util.mask.hideMask();
								if (fn) {
									fn();
								}
							}});
						}
					});  
                },
                update: function(fn) {
					TR.util.crud.favorite.create(fn, true);
                },
				updateName: function(name) {
                    var url = "";
					if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_rename;
					}
					else
					{
						url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_rename;
					}
					
					if (TR.store.favorite.findExact('name', name) != -1) {
						return;
					}
					TR.util.mask.showMask(TR.cmp.favorite.window, TR.i18n.renaming + '...');
					var r = TR.cmp.favorite.grid.getSelectionModel().getSelection()[0];
					Ext.Ajax.request({
						url: url,
						method: 'POST',
						params: {id: r.data.id, name: name},
						success: function() {
							TR.store.favorite.load({callback: function() {
								TR.cmp.favorite.rename.window.close();
								TR.util.mask.hideMask();
								TR.cmp.favorite.grid.getSelectionModel().select(TR.store.favorite.getAt(TR.store.favorite.findExact('name', name)));
								TR.cmp.favorite.name.setValue(name);
							}});
						}
					});
                },
                del: function(fn) {
					var baseurl = TR.conf.finals.ajax.path_root;
					if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						baseurl += TR.conf.finals.ajax.casebasedfavorite_delete;
					}
					else
					{
						baseurl += TR.conf.finals.ajax.aggregatefavorite_delete;
					}
					
					TR.util.mask.showMask(TR.cmp.favorite.window, TR.i18n.deleting + '...');
					var id = TR.cmp.favorite.grid.getSelectionModel().getSelection()[0].data.id;
					baseurl +=  "?id=" + id;
						selection = TR.cmp.favorite.grid.getSelectionModel().getSelection();
					Ext.Array.each(selection, function(item) {
						baseurl = Ext.String.urlAppend(baseurl, 'uids=' + item.data.id);
					});
					Ext.Ajax.request({
						url: baseurl,
						method: 'POST',
						success: function() {
							TR.store.favorite.load({callback: function() {
								TR.util.mask.hideMask();
								if (fn) {
									fn();
								}
							}});
						}
					}); 
                },
				run: function(id) {
					if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						this.caseBasedReport.run( id );
					}
					else
					{
						this.aggregateReport.run( id );
					}
				},			
				
				caseBasedReport:{
					run: function(id) {
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_get + '?id=' + id,
							scope: this,
							success: function(r) {
								var f = Ext.JSON.decode(r.responseText);
								
								Ext.getCmp('programCombobox').setValue( f.programId );
								Ext.getCmp('programStageCombobox').setValue( f.programStageId );
								Ext.getCmp('startDate').setValue( f.startDate );
								Ext.getCmp('endDate').setValue( f.endDate );
								Ext.getCmp('facilityLBCombobox').setValue( f.facilityLB );
								Ext.getCmp('levelCombobox').setValue( f.level );
								TR.state.orgunitIds = f.orgunitIds;
								
								// Data element
								TR.cmp.params.dataelement.objects = [];
								TR.store.dataelement.selected.removeAll();
								if (f.dataElements) {
									for (var i = 0; i < f.dataElements.length; i++) {
										TR.cmp.params.dataelement.objects.push({id: f.dataElements[i].id, name: TR.util.string.getEncodedString(f.dataElements[i].name), compulsory: f.dataElements[i].compulsory, valueType:f.dataElements[i].valueType });
									}
									TR.store.dataelement.selected.add(TR.cmp.params.dataelement.objects);
									
									if( f.singleEvent == 'false' )
									{
										var store = TR.store.dataelement.available;
										store.parent = f.programStageId;
										if (TR.util.store.containsParent(store)) {
											TR.util.store.loadFromStorage(store);
											TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
										}
										else {
											store.load({params: {programStageId: f.programStageId}});
										}
									}
								}
								
								// Program stage									
								var storeProgramStage = TR.store.programStage;
								storeProgramStage.parent = f.programStageId;
								storeProgramStage.isLoadFromFavorite = true;
								storeProgramStage.load({params: {programId: f.programId}});
								
								Ext.getCmp('programStageCombobox').setValue( f.programStageId );
								
								TR.cmp.params.organisationunit.treepanel.getSelectionModel().deselectAll();
								TR.exe.execute();
							}
						});
					}				
				},
				
				aggregateReport:{
					run: function(id) {
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_get + '?id=' + id,
							scope: this,
							success: function(r) {
								var f = Ext.JSON.decode(r.responseText);
								
								Ext.getCmp('programCombobox').setValue( f.programId );
								Ext.getCmp('programStageCombobox').setValue( f.programStageId );
								
								// Date range
								
								Ext.getCmp('startDate').setValue( f.startDate );
								Ext.getCmp('endDate').setValue( f.endDate );
								
								// Relative periods
								
								for (var i = 0; i < f.relativePeriods.length; i++) {
									var cmp = Ext.getCmp('checkbox[paramName="' + f.relativePeriods[i] + '"]');
									if (cmp) {
										cmp.setValue(true);
									}
								}
								
								// Fixed periods
								
								for (var i = 0; i < f.fixedPeriods.length; i++) {
									// var cmp = Ext.getCmp('');
									// if (cmp) {
									//	cmp.setValue(fixedPeriods);
									// } 
								}
								
								// Orgunits
								
								TR.cmp.params.organisationunit.treepanel.getSelectionModel().deselectAll();
								TR.state.orgunitIds = f.orgunitIds;
								
								// Filter values
								
								TR.value.filterValues = [];
								for (var i = 0; i < f.fixedPeriods.length; i++) {
									// var filterValue = f.fixedPeriods[i].split('_');
									// TR.value.filterValues[filterValue[0]] = filterValue[1];
									// TR.cmp.params.dataelement.objects.push({id: filterValue[0].split('_')[1], name: TR.util.string.getEncodedString(f.dataElements[i].name), compulsory: f.dataElements[i].compulsory, valueType:""});
								}
								
								Ext.getCmp('facilityLBCombobox').setValue( f.facilityLB );
								Ext.getCmp('limitOption').setValue( f.limitRecords );
								Ext.getCmp('position').setValue( f.position );
								Ext.getCmp('dataElementGroupByCbx').setValue( f.deGroupBy );
								Ext.getCmp('aggregateType').setValue( f.deGroupBy );
								
								
								Ext.getCmp('levelCombobox').setValue( f.level );
								TR.state.orgunitIds = f.orgunitIds;
								
								
								// Program stage									
								var storeProgramStage = TR.store.programStage;
								storeProgramStage.parent = f.programStageId;
								storeProgramStage.isLoadFromFavorite = true;
								storeProgramStage.load({params: {programId: f.programId}});
								
								Ext.getCmp('programStageCombobox').setValue( f.programStageId );
								
								TR.exe.execute();
							}
						});
					}				
				}
		   }
        }
	};
    
    TR.store = {
		program: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'type'],
			data:TR.init.system.program
		}),
		orgunitGroup: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'type'],
			data:TR.init.system.orgunitGroup,
			listeners: {
				load: function() {
					this.insert(0,{id:"", name: TR.i18n.none});
				}
			}
		}),
		programStage: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.programstages_get,
				reader: {
					type: 'json',
					root: 'programStages'
				}
			},
			isLoadFromFavorite: false,
			listeners:{
				load: function(s) {
					Ext.override(Ext.LoadMask, {
						 onHide: function() {
							  this.callParent();
						 }
					});
					
					if( TR.store.programStage.data.items.length > 1 )
					{
						Ext.getCmp('programStageCombobox').enable();
						Ext.getCmp('programStageCombobox').setValue( "" );
					}
					else
					{
						Ext.getCmp('programStageCombobox').disable();
						var programStageId = TR.store.programStage.data.items[0].raw.id;
						Ext.getCmp('programStageCombobox').setValue( programStageId );
						var store = TR.store.dataelement.available;
						TR.store.dataelement.available.loadData([],false);
						if( !TR.store.programStage.isLoadFromFavorite)
						{
							TR.store.dataelement.selected.loadData([],false);
						}
						store.parent = programStageId;
						
						if (TR.util.store.containsParent(store)) {
							TR.util.store.loadFromStorage(store);
							TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
						}
						else {
							store.load({params: {programStageId: programStageId}});
						}
					}
				}
			}
		}),
		dataelement: {
            available: Ext.create('Ext.data.Store', {
                fields: ['id', 'name', 'compulsory', 'valueType'],
                proxy: {
                    type: 'ajax',
                    url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.dataelements_get,
                    reader: {
                        type: 'json',
                        root: 'dataElements'
                    }
                },
                isloaded: false,
                storage: {},
                listeners: {
					load: function(s) {
						this.isloaded = true;
                        TR.util.store.addToStorage(s);
                        TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
                    }
				}
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'name', 'compulsory', 'valueType'],
                data: []
            })
        },
        datatable: null,
        getDataTableStore: function() {
			this.datatable = Ext.create('Ext.data.ArrayStore', {
				fields: TR.value.fields,
				data: TR.value.values
			});
        },
		reportFavorite: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'lastUpdated'],
			proxy: {
				type: 'ajax',
				url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_getall,
				reader: {
					type: 'json',
					root: 'tabularReports'
				}
			},
			isloaded: false,
			sorting: {
				field: 'name',
				direction: 'ASC'
			},
			sortStore: function() {
				this.sort(this.sorting.field, this.sorting.direction);
			},
			listeners: {
				load: function(s) {
					s.isloaded = !s.isloaded ? true : false;
					
					s.sortStore();
					s.each(function(r) {
						r.data.lastUpdated = r.data.lastUpdated.substr(0,16);
						r.data.icon = '<img src="' + TR.conf.finals.ajax.path_images + 'favorite.png" />';
						r.commit();
					});
				}
            }
		}),
		aggregateFavorite: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'lastUpdated'],
			proxy: {
				type: 'ajax',
				url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_getall,
				reader: {
					type: 'json',
					root: 'tabularReports'
				}
			},
			isloaded: false,
			sorting: {
				field: 'name',
				direction: 'ASC'
			},
			sortStore: function() {
				this.sort(this.sorting.field, this.sorting.direction);
			},
			listeners: {
                load: function(s) {
					s.isloaded = !s.isloaded ? true : false;
					
                    s.sortStore();
                    s.each(function(r) {
                        r.data.lastUpdated = r.data.lastUpdated.substr(0,16);
                        r.data.icon = '<img src="' + TR.conf.finals.ajax.path_images + 'favorite.png" />';
                        r.commit();
                    });
                }
            }
		}),
		periodtype: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: TR.conf.period.periodtypes
		}),
        fixedperiod: {
            available: Ext.create('Ext.data.Store', {
                fields: ['id', 'name', 'index'],
                data: [],
                setIndex: function(periods) {
					for (var i = 0; i < periods.length; i++) {
						periods[i].index = i;
					}
				},
                sortStore: function() {
					this.sort('index', 'ASC');
				}
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: []
            })
        }
	}
    
    TR.state = {
        currentPage: 1,
		total: 1,
		totalRecords: 0,
		isFilter:false,
		orderByOrgunitAsc: true,
		orderByExecutionDateByAsc: true,
		orgunitIds: [],
		generateReport: function( type, isSorted ) {
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				this.caseBasedReport.generate( type, isSorted );
			}
			else
			{
				this.aggregateReport.generate( type, isSorted );
			}
		},
		filterReport: function() {
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				this.caseBasedReport.filter();
			}
			else
			{
				
			}
		},
		getParams: function(isSorted){
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				return this.caseBasedReport.getParams(isSorted);
			}
			return this.aggregateReport.getParams(isSorted);
		},
		getURLParams: function( type, isSorted ){
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				return this.caseBasedReport.getURLParams(type, isSorted );
			}
			else
			{
				return this.aggregateReport.getURLParams(type, isSorted );
			}
		},
		paramChanged: function() {
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				return this.caseBasedReport.isParamChanged();
			}
			return true;
		},
		
		caseBasedReport: {
			generate: function( type, isSorted ) {
				// Validation
				if( !TR.state.caseBasedReport.validation.objects() )
				{
					return;
				}
				// Get url
				var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.generatetabularreport_get;
				// Export to XLS 
				if( type)
				{
					window.location.href = url + "?type=" + type + "&" + TR.state.caseBasedReport.getURLParams( isSorted );
				}
				// Show report on grid
				else
				{
					TR.util.mask.showMask(TR.cmp.region.center, TR.i18n.loading);
					Ext.Ajax.request({
						url: url,
						method: "POST",
						scope: this,
						params: this.getParams(isSorted),
						success: function(r) {
							var json = Ext.JSON.decode(r.responseText);
							if(json.message!=""){
								TR.util.notification.error(TR.i18n.error, json.message);
							}
							else{
								if( isSorted ){
									TR.store.datatable.loadData(TR.value.values,false);
								}
								else{
									TR.state.total = json.total;
									TR.state.totalRecords = json.totalRecords
									TR.value.columns = json.columns;
									TR.value.values = json.items;
									// Get fields
									var fields = [];
									fields[0] = 'id';
									for( var index=1; index < TR.value.columns.length; index++ )
									{
										fields[index] = 'col' + index;
									}
									TR.value.fields = fields;
									
									// Set data for grid
									TR.store.getDataTableStore();
									TR.datatable.getDataTable();
								}
								TR.datatable.setPagingToolbarStatus();
							}
							TR.util.mask.hideMask();
						}
					});
				}
				TR.util.notification.ok();
			},
			filter: function() {
				TR.util.mask.showMask(TR.cmp.region.center, TR.i18n.loading);
				TR.state.isFilter = true;
				var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.generatetabularreport_get;
				Ext.Ajax.request({
					url: url,
					method: "POST",
					scope: this,
					params: this.getParams(),
					success: function(r) {
						var json = Ext.JSON.decode(r.responseText);
						TR.value.values = json.items;
						TR.state.total = json.total;
						TR.state.totalRecords = json.totalRecords
						TR.value.columns = json.columns;
						TR.store.datatable.loadData(TR.value.values,false);
						
						TR.datatable.setPagingToolbarStatus();
							
						TR.util.notification.ok();
						TR.util.mask.hideMask();
					}
				});
			},
			getParams: function(isSorted) {
				var p = {};
				p.startDate = TR.cmp.settings.startDate.rawValue;
				p.endDate = TR.cmp.settings.endDate.rawValue;
				p.facilityLB = TR.cmp.settings.facilityLB.getValue();
				p.level = TR.cmp.settings.level.getValue();
				
				// orders
				p.orderByOrgunitAsc = this.orderByOrgunitAsc;
				p.orderByExecutionDateByAsc= this.orderByExecutionDateByAsc;
				
				p.programStageId = TR.cmp.params.programStage.getValue();
				p.currentPage = this.currentPage;
				
				// organisation unit
				p.orgunitIds = TR.state.orgunitIds;
				
				// Get searching values
				p.searchingValues = [];
				if( !TR.state.caseBasedReport.isParamChanged() || isSorted )
				{
					var cols = TR.datatable.datatable.columns;
					for( var k in cols )
					{
						var col = cols[k];
						if( col.name )
						{
							var params = TR.state.getFilterValueByColumn(col.name);
							for(var i in params){
								p.searchingValues.push(params[i]);
							}
						}
					}
				}
				else
				{
					// Data elements
					TR.cmp.params.dataelement.selected.store.each( function(r) {
						p.searchingValues.push( r.data.id +  '_false_' );
					});
				}
				return p;
			},
			getURLParams: function( isSorted ) {
				var p = "";
				p += "startDate=" + TR.cmp.settings.startDate.rawValue;
				p += "&endDate=" + TR.cmp.settings.endDate.rawValue;
				p += "&facilityLB=" + TR.cmp.settings.facilityLB.getValue();
				p += "&level=" + TR.cmp.settings.level.getValue();
				p += "&orderByOrgunitAsc=" + this.orderByOrgunitAsc;
				p += "&orderByExecutionDateByAsc=" + this.orderByExecutionDateByAsc;
				p += "&programStageId=" + TR.cmp.params.programStage.getValue();
				
				// organisation units
				for( var i=0; i<TR.state.orgunitIds.length; i++ ){
					p += '&orgunitIds=' + TR.state.orgunitIds[i];
				}
				
				if( !TR.state.caseBasedReport.isParamChanged() || isSorted )
				{
					var cols = TR.datatable.datatable.columns;
					for( var k in cols )
					{
						var col = cols[k];
						if( col.name )
						{
							var params = TR.state.getFilterValueByColumn(col.name);
							for(var i in params){
								p += "&searchingValues=" + params[i];
							}
						}
					}
				}
				else
				{
					// Data elements
					TR.cmp.params.dataelement.selected.store.each( function(r) {
						p += "&searchingValues=" + r.data.id + '_false_';
					});
				}
				
				return p;
			},
			getFilterValueByColumn: function( colname ) {
				var grid = TR.datatable.datatable;
				var cols = grid.columns;
				var editor = grid.getStore().getAt(0);
				var params = [];
				for( var i=0; i<cols.length; i++ )
				{
					var col = cols[i];
					var p = "";
					if( col.name && col.name == colname )
					{
						var filters = grid.filters.getFilterData();
						var value = "";
						for( var i=0; i<filters.length; i++ )
						{
							var filter = filters[i];
							if(col.dataIndex == filter.field)
							{
								var compare = '=';
								if( filter.data.comparison == 'lt')
									compare = '<' ;
								else if( filter.data.comparison == 'gt' )
									compare = '>' ;
								value = compare + "'"+ filter.data.value + "'";
								var hidden = (col.hidden==undefined)? false : col.hidden;
								if( value!=null && value!= ''){	
									var value = TR.util.getValueFormula(value);
									p = colname + '_' + hidden + "_" + value;
								}
								params.push(p);
							}
						}
						if (value=='')
						{
							p = colname + '_' + col.hidden + "_";
							params.push(p);
						}
						return params;
					}
				}		
				params.push(colname + "_false_");
				return params
			},
			isParamChanged: function() {
				if( TR.store.datatable && TR.store.datatable.data.length > 0 )
				{
					var orgUnitCols = TR.init.system.maxLevels + 1 - TR.cmp.settings.level.getValue();
					var orgUnitColsInTable =  ( TR.datatable.datatable.columns.length 
										- TR.cmp.params.dataelement.selected.store.data.length - 2 );
					if( orgUnitCols!=orgUnitColsInTable )
					{
						return true;
					}
					
					var colNames=[];
					TR.cmp.params.dataelement.selected.store.each( function(r) {
						colNames.push( r.data.id );
					});
						
					var cols = TR.datatable.datatable.columns;
					var colDataLen = 0;
					for( var i=0; i<cols.length; i++ )
					{
						var col = cols[i];
						if( col.name && col.name.indexOf('meta_')==-1 )
						{
							colDataLen ++;
							if( colNames.indexOf(col.name) == -1 )
								return true;
						}
					}
					if( colDataLen < colNames.length )
					{
						return true;
					}
					
					return !TR.state.isFilter;
				}
				return true;
			},
			validation: {
				objects: function() {
					
					if (TR.cmp.settings.program.getValue() == null) {
						TR.util.notification.error(TR.i18n.et_no_programs, TR.i18n.et_no_programs);
						return false;
					}
					
					if( !TR.cmp.settings.startDate.isValid() )
					{
						var message = TR.i18n.start_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
					
					if( !TR.cmp.settings.endDate.isValid() )
					{
						var message = TR.i18n.end_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
				
					if (TR.state.orgunitIds.length == 0) {
						TR.util.notification.error(TR.i18n.et_no_orgunits, TR.i18n.em_no_orgunits);
						return false;
					}
					
					if (Ext.getCmp('programStageCombobox').getValue() == '') {
						TR.util.notification.error(TR.i18n.em_no_program_stage, TR.i18n.em_no_program_stage);
						return false;
					}
					
					return true;
				},
				response: function(r) {
					if (!r.responseText) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_invalid_uid, TR.i18n.em_invalid_uid);
						return false;
					}
					return true;
				},
				value: function() {
					if (!TR.value.values.length) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_no_data, TR.i18n.em_no_data);
						return false;
					}
					return true;
				}
			}
		},
		
		aggregateReport: {
			generate: function( type, isSorted ) {
				// Validation
				if( !TR.state.aggregateReport.validation.objects() )
				{
					return;
				}
				// Get url
				var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.generateaggregatereport_get;
				// Export to XLS 
				if( type)
				{
					window.location.href = url + "?type="+ type + "&" + TR.state.aggregateReport.getURLParams(isSorted );
				}
				// Show report on grid
				else
				{
					TR.util.mask.showMask(TR.cmp.region.center, TR.i18n.loading);
					Ext.Ajax.request({
						url: url,
						method: "POST",
						scope: this,
						params: this.getParams(isSorted),
						success: function(r) {
							var json = Ext.JSON.decode(r.responseText);
							if(json.message!=""){
								TR.util.notification.error(TR.i18n.error, json.message);
							}
							else{
								if( isSorted ){
									TR.store.datatable.loadData(TR.value.values,false);
								}
								else{
									TR.value.columns = json.columns;
									TR.value.values = json.items;
									// Get fields
									var fields = [];
									for( var index=0; index < TR.value.columns.length; index++ )
									{
										fields[index] = 'col' + index;
									}
									TR.value.fields = fields;
									
									// Set data for grid
									TR.store.getDataTableStore();
									TR.datatable.getDataTable();
									TR.datatable.hidePagingBar();
								}
							}
							TR.util.mask.hideMask();
						}
					});
				}
				TR.util.notification.ok();
			},
			filter: function() {
			
			},
			getPosition: function() {
				// 1 - Rows
				// 2 - Columns
				// 3 - Filter
				var positionOrgunit = Ext.getCmp('positionOrgunitCbx').getValue();
				var positionPeriod = Ext.getCmp('positionPeriodCbx').getValue();
				var positionData = Ext.getCmp('positionDataCbx').getValue();
				
				// 1
				if( positionOrgunit==1 && positionPeriod==2 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT_COLUMN_PERIOD;
				}
				// 2
				if( positionOrgunit==2 && positionPeriod==1 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_PERIOD_COLUMN_ORGUNIT;
				}
				// 3
				if( positionOrgunit==1 && positionPeriod==1 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT_ROW_PERIOD;
				}
				// 4
				if( positionOrgunit==3 && positionPeriod==1 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_PERIOD;
				}
				// 5
				if( positionOrgunit==1 && positionPeriod==3 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT;
				}
				//6
				if( positionOrgunit==3 && positionPeriod==1 && positionData==2 )
				{
					return TR.conf.reportPosition.POSITION_ROW_PERIOD_COLUMN_DATA;
				}
				//7
				if( positionOrgunit==1 && positionPeriod==3 && positionData==2 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT_COLUMN_DATA;
				}
				// 8
				if( positionOrgunit==3 && positionPeriod==3 && positionData==1 )
				{
					return TR.conf.reportPosition.POSITION_ROW_DATA;
				}
				return '';
			},
			getParams: function( isSorted ) {
				var p = {};
				p.programStageId = TR.cmp.params.programStage.getValue();
				p.aggregateType = Ext.getCmp('aggregateType').getValue().aggregateType;
				p.orgunitIds = TR.state.orgunitIds;
				p.limit = Ext.getCmp('limitOption').getValue();
				
				var position = TR.state.aggregateReport.getPosition();
				if( TR.cmp.settings.dataElementGroupBy.getValue() != null ){
					p.dataElementId = TR.cmp.settings.dataElementGroupBy.getValue().split('_')[1];
				}
				
				// Filter values
				p.deFilters = [];
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					var deId = r.data.id;
					var filterOpt = Ext.getCmp('filter_opt_' + deId).rawValue;
					var filterValue = Ext.getCmp('filter_' + deId).rawValue;
					var filter = deId.split('_')[1] + "_" + filterOpt + "_" + filterValue;
					
					var valueType = r.data.valueType;
					if( valueType != 'string' && valueType != 'trueOnly' 
						&& valueType != 'bool' && valueType != 'list')
						{
							var deId = deId + '_1';
							filterOpt = Ext.getCmp('filter_opt_' + deId).rawValue;
							filterValue = Ext.getCmp('filter_' + deId).rawValue;
							filter += "_" + filterOpt + "_" + filterValue;
						}
					p.deFilters.push( filter );
					
				});
				
				// Fixed periods
				
				p.periodIds = [];
				TR.cmp.params.fixedperiod.selected.store.each( function(r) {
					p.periodIds.push( r.data.id );
				});
				
				// Relative periods
				
				var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
				p.relativePeriods = [];
				Ext.Array.each(relativePeriodList, function(item) {
					if(item.getValue() && !item.hidden){
						p.relativePeriods.push(item.paramName);
					}
				});
				
				// Period range
				
				p.startDate = TR.cmp.settings.startDate.rawValue;
				p.endDate = TR.cmp.settings.endDate.rawValue;
			
				p.facilityLB = TR.cmp.settings.facilityLB.getValue();
				p.position = position;
				
				return p;
			},
			getURLParams: function(isSorted) {
				var p = "";
				
				p = "programStageId=" + TR.cmp.params.programStage.getValue();
				p += "&aggregateType=" + Ext.getCmp('aggregateType').getValue().aggregateType;
				p += "&orgunitIds=" + TR.state.orgunitIds;
				p += "&limit=" + Ext.getCmp('limitOption').getValue();
				
				var position = TR.state.aggregateReport.getPosition();
				if( TR.cmp.settings.dataElementGroupBy.getValue() != null ){
					p += "&dataElementId=" + TR.cmp.settings.dataElementGroupBy.getValue().split('_')[1];
				}
				
				// Filter values
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					var deId = r.data.id;
					var filterOpt = Ext.getCmp('filter_opt_' + deId).rawValue;
					var filterValue = Ext.getCmp('filter_' + deId).rawValue;
					var filter = deId.split('_')[1] + "_" + filterOpt + "_" + filterValue;
					
					var valueType = r.data.valueType;
					if( valueType != 'string' && valueType != 'trueOnly' 
						&& valueType != 'bool' && valueType != 'list')
						{
							var deId = deId + '_1';
							filterOpt = Ext.getCmp('filter_opt_' + deId).rawValue;
							filterValue = Ext.getCmp('filter_' + deId).rawValue;
							filter += "_" + filterOpt + "_" + filterValue;
						}
					p += "&deFilters=" + filter;
					
				});
				
				// Fixed periods
				
				p.periodIds = [];
				TR.cmp.params.fixedperiod.selected.store.each( function(r) {
					p += "&periodIds=" + r.data.id;
				});
				
				// Relative periods
				
				var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
				p.relativePeriods = [];
				Ext.Array.each(relativePeriodList, function(item) {
					if(item.getValue() && !item.hidden){
						p += "&relativePeriods=" + item.paramName;
					}
				});
				
				// Period range
				
				p += "&startDate=" + TR.cmp.settings.startDate.rawValue;
				p += "&endDate=" + TR.cmp.settings.endDate.rawValue;
			
				p += "&facilityLB=" + TR.cmp.settings.facilityLB.getValue();
				p += "&position=" + position;
				
				
				return p;
			},
			getFilterValueByColumn: function( colname ) {
				
			},
			isParamChanged: function() {
			
			},
			validation: {
				objects: function() {
					if (TR.cmp.settings.program.getValue() == null) {
						TR.util.notification.error(TR.i18n.et_no_programs, TR.i18n.et_no_programs);
						return false;
					}
					
					if( TR.cmp.settings.startDate.rawValue != "" 
						&& !TR.cmp.settings.startDate.isValid() )
					{
						var message = TR.i18n.start_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
					
					if( TR.cmp.settings.endDate.rawValue != "" 
						&& !TR.cmp.settings.endDate.isValid() )
					{
						var message = TR.i18n.end_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
				
					if (TR.state.orgunitIds.length == 0) {
						TR.util.notification.error(TR.i18n.et_no_orgunits, TR.i18n.em_no_orgunits);
						return false;
					}
					
					if (Ext.getCmp('programStageCombobox').getValue() == '') {
						TR.util.notification.error(TR.i18n.em_no_program_stage, TR.i18n.em_no_program_stage);
						return false;
					}
					
					if( TR.cmp.settings.startDate.rawValue=="" 
						&& TR.cmp.settings.endDate.rawValue=="" 
						&& TR.cmp.params.fixedperiod.selected.store.data.items.length == 0 )
					{
						var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
						var flag = false;
						Ext.Array.each(relativePeriodList, function(item) {
							if(item.getValue()){
								flag = true;
							}
						});
						
						if( !flag )
						{
							TR.util.notification.error(TR.i18n.em_no_period, TR.i18n.em_no_period);
							return false;
						}
					}
					
					TR.cmp.params.dataelement.selected.store.each( function(r) {
						var deId = r.data.id;
						var filterValue = Ext.getCmp('filter_' + deId).getValue();
						if( filterValue == null ){
							TR.util.notification.error(TR.i18n.fill_filter_values_for_all_selected_data_elements, TR.i18n.fill_filter_values_for_all_selected_data_elements);
							return false;
						}
					});
					
					var periodInt = 0;
					if( TR.cmp.settings.startDate.rawValue!="" 
						&& TR.cmp.settings.endDate.rawValue!="") 
					{
						periodInt++;
					}
					if( TR.cmp.params.fixedperiod.selected.store.data.items.length > 0 )
					{
						periodInt++;
					}
					var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
					Ext.Array.each(relativePeriodList, function(item) {
						if(item.getValue()){
							periodInt++;
						}
					});
					if( Ext.getCmp('limitOption').getValue() && periodInt>1)
					{
						TR.util.notification.error(TR.i18n.select_only_one_period, TR.i18n.select_only_one_period);
						return false;
					}
					
					var position = TR.state.aggregateReport.getPosition();
					if( !( position== TR.conf.reportPosition.POSITION_ROW_ORGUNIT_COLUMN_DATA
						|| position== TR.conf.reportPosition.POSITION_ROW_DATA )
						&& TR.cmp.params.dataelement.selected.store.data.items.length == 0) {
						TR.util.notification.error(TR.i18n.em_no_dataelement, TR.i18n.em_no_dataelement);
						return false;
					};
					
					if( position==TR.conf.reportPosition.POSITION_ROW_DATA 
						&& TR.cmp.settings.dataElementGroupBy.getValue()==null ){
						TR.util.notification.error(TR.i18n.select_data_element_for_grouping, TR.i18n.select_data_element_for_grouping);
						return false;
					}
					
					return true;
				},
				response: function(r) {
					if (!r.responseText) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_invalid_uid, TR.i18n.em_invalid_uid);
						return false;
					}
					return true;
				},
				value: function() {
					if (!TR.value.values.length) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_no_data, TR.i18n.em_no_data);
						return false;
					}
					return true;
				}
			}
		}
   };
    
    TR.value = {
		columns: [],
		fields: [],
		values: [],
		covertValueType: function( type )
		{
			type = type.toLowerCase();
			if( type == 'date' )
			{
				return type;
			}
			if(type == 'number')
			{
				return 'float';
			}
			if( type == 'int' || type == 'positiveNumber'  || type == 'negativeNumber' )
			{
				return 'numeric';
			}
			if( type == 'bool' || type == 'yes/no' || type == 'trueOnly' )
			{
				return 'boolean';
			}
			if( type == 'combo')
			{
				return 'list';
			}
			return 'string';
		},
		covertXType: function( type )
		{
			if( type == 'date' )
			{
				return 'datefield';
			}
			if( type == 'number' || type == 'int' || type == 'positiveNumber'  || type == 'negativeNumber' )
			{
				return 'numberfield';
			}
			if( type == 'combo' || type == 'list' || type == 'trueOnly' )
			{
				return 'combobox';
			}
			return 'textfield';
		},
	};
      
    TR.datatable = {
        datatable: null,
		getDataTable: function() {
			var cols = this.createColTable();
			// grid
			this.datatable = Ext.create('Ext.grid.Panel', {
                height: TR.util.viewport.getSize().y - 58,
				id: 'gridTable',
				columns: cols,
				scroll: 'both',
				title: TR.cmp.settings.program.rawValue + " - " + TR.cmp.params.programStage.rawValue + " " + TR.i18n.report,
				selType: 'cellmodel',
				features: [{
					ftype: 'filters',
					autoReload: true,
					encode: true,
					local: false,
					buildQuery : function (filters) {
						TR.exe.filter();
					},
					filters: []
				}],
				bbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						id:'firstPageBtn',
						width: 22,
						handler: function() {
							TR.exe.paging(1);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						id:'previousPageBtn',
						width: 22,
						handler: function() {
							TR.exe.paging( eval(TR.cmp.settings.currentPage.rawValue) - 1 );
						}
					},
					{
						xtype: 'label',
						id:'separate1Lbl',
						text: '|'
					},
					{
						xtype: 'label',
						id:'pageLbl',
						text: TR.i18n.page
					},
					{
						xtype: 'textfield',
						cls: 'tr-textfield-alt1',
						id:'currentPage',
						value: TR.state.currentPage,
						listeners: {
							added: function() {
								TR.cmp.settings.currentPage = this;
							},						
							specialkey: function( textfield, e, eOpts ){
								
								if (e.keyCode == e.ENTER)
								{
									var oldValue = TR.state.currentPage;
									var newValue = textfield.rawValue;
									if( newValue < 1 || newValue > TR.state.total )
									{
										textfield.setValue(oldValue);
									}
									else
									{
										TR.exe.paging( newValue );
									}
								}
							}
						},
					},
					{
						xtype: 'label',
						id:'totalPageLbl',
						text: ' of ' + TR.state.total + ' | '
					},
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						id:'nextPageBtn',
						handler: function() {
							TR.exe.paging( eval(TR.cmp.settings.currentPage.rawValue) + 1 );
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						id:'lastPageBtn',
						handler: function() {
							TR.exe.paging( TR.state.total );
						}
					},
					{
						xtype: 'label',
						id:'separate2Lbl',
						text: '|'
					},
					{
						xtype: 'button',
						id:'refreshBtn',
						icon: 'images/refresh.png',
						handler: function() {
							TR.exe.paging( TR.cmp.settings.currentPage.rawValue );
						}
					},
					'->',
					{
						xtype: 'label',
						id: 'totalEventLbl',
						style: 'margin-right:18px;',
						text: TR.state.totalRecords + ' ' + TR.i18n.events
					},
				], 
				store: TR.store.datatable
			});
			
			Ext.override(Ext.grid.header.Container, { 
				sortAscText: TR.i18n.asc,
				sortDescText: TR.i18n.desc, 
				columnsText: TR.i18n.show_hide_columns });

			TR.cmp.region.center.removeAll(true);
			TR.cmp.region.center.add(this.datatable);		
          	
            return this.datatable;
            
        },
		createColTable:function(){
			var cols = [];
				
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				var orgUnitCols = ( TR.init.system.maxLevels + 1 - TR.cmp.settings.level.getValue() );
				var index = 0;
				
				// id of event
				
				cols[index] = {
					header: TR.value.columns[index].name, 
					dataIndex: 'id',
					height: TR.conf.layout.east_gridcolumn_height,
					sortable: false,
					draggable: false,
					hidden: true,
					hideable: false,
					menuDisabled: true
				};
				
				// report date
				
				cols[++index] = {
					header: TR.value.columns[index].name, 
					dataIndex: 'col' + index,
					height: TR.conf.layout.east_gridcolumn_height,
					sortable: false,
					draggable: false,
					hideable: false,
					menuDisabled: true
				};
							
				// Org unit level columns
				
				for( var i = 0; i <orgUnitCols; i++ )
				{
					cols[++index] = {
						header: TR.value.columns[index].name, 
						dataIndex: 'col' + index,
						height: TR.conf.layout.east_gridcolumn_height,
						name: 'meta_' + index,
						sortable: false,
						draggable: false,
						hideable: false,
						menuDisabled: true
					}
				}
				
				// Data element columns
				
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					cols[++index] = TR.datatable.createColumn( r.data.valueType, r.data.id, r.data.compulsory, r.data.name, index );
				});
			
			}
			else
			{
				for(var i in TR.value.columns)
				{
					cols[i] = this.createColumn( "textfield","id" + i, false, TR.value.columns[i].name, i );
				}
			}
			return cols;
		},
		createColumn: function( type, id, compulsory, colname, index ){
			var objectType = id.split('_')[0];
			var objectId = id.split('_')[1];
			
			var params = {};
			params.header = colname;
			params.dataIndex = 'col' + index;
			params.name = id;
			params.hidden = eval(TR.value.columns[index].hidden );
			params.menuFilterText = TR.value.filter;
			params.sortable = false;
			params.draggable = true;
			params.isEditAllowed = true;
			params.compulsory = compulsory;
			
			params.editor = {}; 
			params.editor.xtype = TR.value.covertXType( type ); 
			params.editor.editable = true;

			params.filter = {};
			params.filter.type = TR.value.covertValueType( type );
				
			type = type.toLowerCase();
			if( type == 'date' )
			{
				params.renderer = Ext.util.Format.dateRenderer( TR.i18n.format_date );
				params.filter.dateFormat = TR.i18n.format_date;
				params.filter.beforeText = TR.i18n.before;
				params.filter.afterText = TR.i18n.after;
				params.filter.onText = TR.i18n.on;
				
				params.editor.format = TR.i18n.format_date;
			}
			else if( type == 'bool' || type == 'trueonly' )
			{
				params.editor.xtype = 'combobox';
				params.editor.queryMode = 'local';
				params.editor.editable = true;
				params.editor.valueField = 'value';
				params.editor.displayField = 'name';
				params.editor.selectOnFocus = true;
				if( type.toLowerCase() == 'bool' ){
					params.editor.store = new Ext.data.ArrayStore({
						fields: ['value', 'name'],
						data: [['', ''],['true', TR.i18n.true_value], ['false', TR.i18n.false_value]]
					});
				}
				else{
					params.editor.store = new Ext.data.ArrayStore({
						fields: ['value', 'name'],
						data: [['', ''], ['true', TR.i18n.true_value]]
					});
				}
			}
			else if( type == 'list' )
			{
				params.editor.xtype = 'combobox';
				params.editor.typeAhead = true;
				params.editor.selectOnFocus = true;
				params.editor.triggerAction = 'all';
				params.editor.transform = 'light';
				params.editor.lazyRender = true;
				params.editor.forceSelection = true;
				params.editor.hideTrigger = true;
				params.editor.validateOnBlur = true;
				params.editor.queryMode = 'remote';
				params.editor.valueField = 'o';
				params.editor.displayField = 'o';
				params.editor.store = Ext.create('Ext.data.Store', {
					fields: ['o'],
					data:[],
					expandData: true,
					proxy: {
						type: 'ajax',
						url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.suggested_dataelement_get,
						extraParams:{id: objectId},
						reader: {
							type: 'json',
							root: 'options'
						}
					}
				})		
			}
			
			return params;
		},
        setPagingToolbarStatus: function() {
			TR.datatable.showPagingBar();
			Ext.getCmp('currentPage').enable();
			Ext.getCmp('totalEventLbl').setText( TR.state.totalRecords + ' ' + TR.i18n.events );
			Ext.getCmp('totalPageLbl').setText( ' of ' + TR.state.total + ' | ' );
			if( TR.state.totalRecords== 0 )
			{
				Ext.getCmp('currentPage').setValue('');
				Ext.getCmp('currentPage').setValue('');
				Ext.getCmp('currentPage').disable();
				Ext.getCmp('firstPageBtn').disable();
				Ext.getCmp('previousPageBtn').disable();
				Ext.getCmp('nextPageBtn').disable();
				Ext.getCmp('lastPageBtn').disable();
	
				Ext.getCmp('btnClean').disable();
				Ext.getCmp('btnSortBy').disable();
			}
			else
			{
				Ext.getCmp('btnClean').enable();
				Ext.getCmp('btnSortBy').enable();
				Ext.getCmp('currentPage').setValue(TR.state.currentPage);
				
				if( TR.state.currentPage == TR.state.total 
					&& TR.state.total== 1 )
				{
					Ext.getCmp('firstPageBtn').disable();
					Ext.getCmp('previousPageBtn').disable();
					Ext.getCmp('nextPageBtn').disable();
					Ext.getCmp('lastPageBtn').disable();
				}
				else if( TR.state.currentPage == TR.state.total )
				{
					Ext.getCmp('firstPageBtn').enable();
					Ext.getCmp('previousPageBtn').enable();
					Ext.getCmp('nextPageBtn').disable();
					Ext.getCmp('lastPageBtn').disable();
				}
				else if( TR.state.currentPage == 1 )
				{
					Ext.getCmp('firstPageBtn').disable();
					Ext.getCmp('previousPageBtn').disable();
					Ext.getCmp('nextPageBtn').enable();
					Ext.getCmp('lastPageBtn').enable();
				}
				else
				{
					Ext.getCmp('firstPageBtn').enable();
					Ext.getCmp('previousPageBtn').enable();
					Ext.getCmp('nextPageBtn').enable();
					Ext.getCmp('lastPageBtn').enable();
				} 
			}
        },
		hidePagingBar: function(){
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('totalEventLbl').setVisible(false);
			Ext.getCmp('totalPageLbl').setVisible(false);
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('firstPageBtn').setVisible(false);
			Ext.getCmp('previousPageBtn').setVisible(false);
			Ext.getCmp('nextPageBtn').setVisible(false);
			Ext.getCmp('lastPageBtn').setVisible(false);
			Ext.getCmp('refreshBtn').setVisible(false);
			Ext.getCmp('pageLbl').setVisible(false);
			Ext.getCmp('separate1Lbl').setVisible(false);
			Ext.getCmp('separate2Lbl').setVisible(false);
		},
		showPagingBar: function(){
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('totalEventLbl').setVisible(true);
			Ext.getCmp('totalPageLbl').setVisible(true);
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('firstPageBtn').setVisible(true);
			Ext.getCmp('previousPageBtn').setVisible(true);
			Ext.getCmp('nextPageBtn').setVisible(true);
			Ext.getCmp('lastPageBtn').setVisible(true);
			Ext.getCmp('refreshBtn').setVisible(true);
			Ext.getCmp('pageLbl').setVisible(true);
			Ext.getCmp('separate1Lbl').setVisible(true);
			Ext.getCmp('separate2Lbl').setVisible(true);
		}
    };
        
	TR.exe = {
		execute: function( type, isSorted ) {
			TR.state.generateReport( type, isSorted );
		},
		filter: function() {
			TR.state.isFilter = true;
			TR.state.filterReport();
		},
		paging: function( currentPage )
		{
			TR.state.currentPage = currentPage;
			TR.state.filterReport();
			Ext.getCmp('currentPage').setValue( currentPage );	
			TR.datatable.setPagingToolbarStatus();
		},
		reset: function() {
			TR.store.datatable.loadData([],false);
			this.execute();
		},
		datatable: function() {
			TR.store.getDataTableStore();
			TR.datatable.getDataTable();
			TR.datatable.setPagingToolbarStatus();
		}
    };
	
	Ext.apply(Ext.form.VTypes, {
		daterange : function(val, field) {
			var date = field.parseDate(val);
	 
			if(!date){
				return;
			}
			if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
				var start = Ext.getCmp(field.startDateField);
				start.setMaxValue(date);
				start.validate();
				this.dateRangeMax = date;
			}
			else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
				var end = Ext.getCmp(field.endDateField);
				end.setMinValue(date);
				end.validate();
				this.dateRangeMin = date;
			}
			/*
			 * Always return true since we're only using this vtype to set the
			 * min/max allowed values (these are tested for after the vtype test)
			 */
			return true;
		}
	});

    TR.viewport = Ext.create('Ext.container.Viewport', {
        layout: 'border',
        renderTo: Ext.getBody(),
        isrendered: false,
        items: [
            {
                region: 'west',
                preventHeader: true,
                collapsible: true,
                collapseMode: 'mini',
                items: [
				{
					xtype: 'toolbar',
					style: 'padding-top:1px; border-style:none',
					bodyStyle: 'border-style:none; background-color:transparent; padding:4px 0 0 8px',
                    items: [
						{
							xtype: 'panel',
							bodyStyle: 'border-style:none; background-color:transparent; padding:4px 0 0 8px',
							items: [
								Ext.create('Ext.form.Panel', {
								bodyStyle: 'border-style:none; background-color:transparent; padding:3px 0 0 0',
                                items: [
								{
									xtype: 'label',
									text: TR.i18n.report_type,
									style: 'font-size:11px; font-weight:bold; padding:0 0 0 3px'
								},
								{
									xtype: 'radiogroup',
									id: 'reportTypeGroup',
									columns: 2,
									vertical: true,
									items: [
									{
										boxLabel: TR.i18n.case_based_report,
										name: 'reportType',
										inputValue: 'true',
										listeners: {
											change: function (cb, nv, ov) {
												if(nv)
												{
													Ext.getCmp('limitOption').setVisible(false);
													Ext.getCmp('dataElementGroupByCbx').setVisible(false);
													Ext.getCmp('aggregateType').setVisible(false);
													Ext.getCmp('downloadPdfIcon').setVisible(false);
													Ext.getCmp('downloadCvsIcon').setVisible(false);
													Ext.getCmp('levelCombobox').setVisible(true);
													Ext.getCmp('selectedDataelements').setVisible(true);
													
													Ext.getCmp('filterPanel').setVisible(false);
													Ext.getCmp('relativePeriodsDiv').setVisible(false); 
													Ext.getCmp('fixedPeriodsDiv').setVisible(false);
													Ext.getCmp('dateRangeDiv').expand();
												}
											}
										}
									}, 
									{
										boxLabel: TR.i18n.aggregated_report,
										name: 'reportType',
										inputValue: 'false',
										checked: true,
										listeners: {
											change: function (cb, nv, ov) {
												if(nv)
												{
													Ext.getCmp('limitOption').setVisible(true);
													Ext.getCmp('dataElementGroupByCbx').setVisible(true);
													Ext.getCmp('aggregateType').setVisible(true);
													Ext.getCmp('downloadPdfIcon').setVisible(true);
													Ext.getCmp('downloadCvsIcon').setVisible(true);
													Ext.getCmp('levelCombobox').setVisible(false);
													Ext.getCmp('selectedDataelements').setVisible(false);
													
													Ext.getCmp('filterPanel').setVisible(true);
													Ext.getCmp('fixedPeriodsDiv').setVisible(true);
													Ext.getCmp('relativePeriodsDiv').setVisible(true);
													Ext.getCmp('dateRangeDiv').expand();
												}
											}
										}
									}]
								}]
							}),
							{ bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
							{
								xtype: 'combobox',
								cls: 'tr-combo',
								name: TR.init.system.programs,
								id: 'programCombobox',
								fieldLabel: TR.i18n.program,
								labelStyle: 'font-weight:bold',
								labelAlign: 'top',
								emptyText: TR.i18n.please_select,
								queryMode: 'local',
								editable: false,
								valueField: 'id',
								displayField: 'name',
								width: TR.conf.layout.west_fieldset_width,
								store: TR.store.program,
								listeners: {
									added: function() {
										TR.cmp.settings.program = this;
									},
									select: function(cb) {
										TR.state.isFilter = false;
										var pId = cb.getValue();
										
										// PROGRAM-STAGE										
										var storeProgramStage = TR.store.programStage;
										TR.store.dataelement.available.removeAll();
										TR.store.dataelement.selected.removeAll();
										storeProgramStage.parent = pId;
										TR.store.dataelement.isLoadFromFavorite = false;
										storeProgramStage.load({params: {programId: pId}});
										
										// FILTER-VALUES FIELDS
										Ext.getCmp('filterPanel').removeAll();
									}
								}
							},
							{
								xtype: 'combobox',
								cls: 'tr-combo',
								id:'programStageCombobox',
								fieldLabel: TR.i18n.program_stage,
								labelStyle: 'font-weight:bold',
								labelAlign: 'top',
								emptyText: TR.i18n.please_select,
								queryMode: 'local',
								editable: false,
								valueField: 'id',
								displayField: 'name',
								width: TR.conf.layout.west_fieldset_width,
								store: TR.store.programStage,
								listeners: {
									added: function() {
										TR.cmp.params.programStage = this;
									},  
									select: function(cb) {
										TR.state.isFilter = false;
										var store = TR.store.dataelement.available;
										TR.store.dataelement.selected.loadData([],false);
										store.parent = cb.getValue();
										
										if (TR.util.store.containsParent(store)) {
											TR.util.store.loadFromStorage(store);
											TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
										}
										else {
											store.load({params: {programStageId: cb.getValue()}});
										}
									} 
								}
							},
							]
						}]
					},                            
					{
						xtype: 'panel',
                        bodyStyle: 'border-style:none; border-top:2px groove #eee; padding:10px 10px 0 10px;',
                        layout: 'fit',
                        items: [
							{
								xtype: 'panel',
								layout: 'accordion',
								activeOnTop: true,
								cls: 'tr-accordion',
								bodyStyle: 'border:0 none',
								height: 430,
								items: [
							
									// DATE-RANGE
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.period_range + '</div>',
										id: 'dateRangeDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'datefield',
												cls: 'tr-textfield-alt1',
												id: 'startDate',
												fieldLabel: TR.i18n.start_date,
												labelWidth: 90,
												editable: true,
												allowBlank:true,
												invalidText: TR.i18n.the_date_is_not_valid,
												style: 'margin-right:8px',
												width: TR.conf.layout.west_fieldset_width - 20,
												format: TR.i18n.format_date,
												value: new Date((new Date()).setMonth((new Date()).getMonth()-3)),
												maxValue: new Date(),
												listeners: {
													added: function() {
														TR.cmp.settings.startDate = this;
													},
													change:function(){
														var startDate =  TR.cmp.settings.startDate.getValue();
														Ext.getCmp('endDate').setMinValue(startDate);
													}
												}
											},
											{
												xtype: 'datefield',
												cls: 'tr-textfield-alt1',
												id: 'endDate',
												fieldLabel: TR.i18n.end_date,
												labelWidth: 90,
												editable: true,
												allowBlank:true,
												invalidText: TR.i18n.the_date_is_not_valid,
												width: TR.conf.layout.west_fieldset_width - 20,
												format: TR.i18n.format_date,
												value: new Date(),
												minValue: new Date((new Date()).setMonth((new Date()).getMonth()-3)),
												listeners: {
													added: function() {
														TR.cmp.settings.endDate = this;
													},
													change:function(){
														var endDate =  TR.cmp.settings.endDate.getValue();
														Ext.getCmp('startDate').setMaxValue( endDate );
													}
												}
											}
										]
									},
									
									// RELATIVE PERIODS
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.relative_periods + '</div>',
										id: 'relativePeriodsDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:0 0 0 10px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		TR.cmp.params.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: TR.i18n.months,
																cls: 'tr-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'reportingMonth',
																boxLabel: TR.i18n.last_month
															},
															{
																xtype: 'checkbox',
																paramName: 'last3Months',
																boxLabel: TR.i18n.last_3_months
															},
															{
																xtype: 'checkbox',
																paramName: 'last12Months',
																boxLabel: TR.i18n.last_12_months,
																checked: true
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:0 0 0 32px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		TR.cmp.params.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: TR.i18n.quarters,
																cls: 'tr-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'reportingQuarter',
																boxLabel: TR.i18n.last_quarter
															},
															{
																xtype: 'checkbox',
																paramName: 'last4Quarters',
																boxLabel: TR.i18n.last_4_quarters
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:0 0 0 32px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		TR.cmp.params.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: TR.i18n.six_months,
																cls: 'tr-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'lastSixMonth',
																boxLabel: TR.i18n.last_six_month
															},
															{
																xtype: 'checkbox',
																paramName: 'last2SixMonths',
																boxLabel: TR.i18n.last_two_six_month
															}
														]
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'panel',
														layout: 'anchor',
														bodyStyle: 'border-style:none; padding:5px 0 0 10px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		TR.cmp.params.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: TR.i18n.years,
																cls: 'tr-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'thisYear',
																boxLabel: TR.i18n.this_year
															},
															{
																xtype: 'checkbox',
																paramName: 'lastYear',
																boxLabel: TR.i18n.last_year
															},
															{
																xtype: 'checkbox',
																paramName: 'last5Years',
																boxLabel: TR.i18n.last_5_years
															}
														]
													}
												]
											}
										], 
										listeners: {
											added: function() {
												TR.cmp.params.relativeperiod.panel = this;
											}
										}
									},
									
									// FIXED PERIODS
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.fixed_periods + '</div>',
										id: 'fixedPeriodsDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														style: 'margin-bottom:8px',
														width: 253,
														valueField: 'id',
														displayField: 'name',
														fieldLabel: TR.i18n.select_type,
														labelStyle: 'padding-left:7px;',
														labelWidth: 90,
														editable: false,
														queryMode: 'remote',
														store: TR.store.periodtype,
														periodOffset: 0,
														listeners: {
															select: function() {
																var pt = new PeriodType(),
																	periodType = this.getValue();
																var periods = pt.get(periodType).generatePeriods({
																	offset: this.periodOffset,
																	filterFuturePeriods: true,
																	reversePeriods: true
																});

																TR.store.fixedperiod.available.setIndex(periods);
																TR.store.fixedperiod.available.loadData(periods);
																TR.util.multiselect.filterAvailable(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
															}
														}
													},
													{
														xtype: 'button',
														text: 'Prev year',
														style: 'margin-left:4px',
														height: 24,
														handler: function() {
															var cb = this.up('panel').down('combobox');
															if (cb.getValue()) {
																cb.periodOffset--;
																cb.fireEvent('select');
															}
														}
													},
													{
														xtype: 'button',
														text: 'Next year',
														style: 'margin-left:3px',
														height: 24,
														handler: function() {
															var cb = this.up('panel').down('combobox');
															if (cb.getValue() && cb.periodOffset < 0) {
																cb.periodOffset++;
																cb.fireEvent('select');
															}
														}
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'multiselect',
														name: 'availableFixedPeriods',
														cls: 'tr-toolbar-multiselect-left',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: 180,
														valueField: 'id',
														displayField: 'name',
														store: TR.store.fixedperiod.available,
														tbar: [
															{
																xtype: 'label',
																text: TR.i18n.available,
																cls: 'tr-toolbar-multiselect-left-label'
															},
															'->',
															{
																xtype: 'button',
																icon: 'images/arrowright.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.select(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.selectAll(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
																}
															},
															' '
														],
														listeners: {
															added: function() {
																TR.cmp.params.fixedperiod.available = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.select(this, TR.cmp.params.fixedperiod.selected);
																}, this);
															}
														}
													},
													{
														xtype: 'multiselect',
														name: 'selectedFixedPeriods',
														cls: 'tr-toolbar-multiselect-right',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: 180,
														displayField: 'name',
														valueField: 'id',
														ddReorder: false,
														queryMode: 'local',
														store: TR.store.fixedperiod.selected,
														tbar: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselectAll(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselect(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
																}
															},
															'->',
															{
																xtype: 'label',
																text: TR.i18n.selected,
																cls: 'tr-toolbar-multiselect-right-label'
															}
														],
														listeners: {
															added: function() {
																TR.cmp.params.fixedperiod.selected = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.unselect(TR.cmp.params.fixedperiod.available, this);
																}, this);
															}
														}
													}
												]
											}
										],
										listeners: {
											added: function() {
												TR.cmp.params.fixedperiod.panel = this;
											}
										}
									},
									
									// ORGANISATION UNIT
									{
										title: '<div style="height:17px;background-image:url(images/organisationunit.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.organisation_units + '</div>',
										id: 'orgunitDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'combobox',
												cls: 'tr-combo',
												id: 'facilityLBCombobox',
												fieldLabel: TR.i18n.use_data_from_level,
												labelWidth: 135,
												emptyText: TR.i18n.please_select,
												queryMode: 'local',
												editable: false,
												valueField: 'value',
												displayField: 'name',
												width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor,
												store:  new Ext.data.ArrayStore({
													fields: ['value', 'name'],
													data: [['all', TR.i18n.all], ['childrenOnly', TR.i18n.children_only], ['selected', TR.i18n.selected]],
												}),
												value: 'all',
												listeners: {
													added: function() {
														TR.cmp.settings.facilityLB = this;
													}
												}
											},
											{
												xtype: 'combobox',
												cls: 'tr-combo',
												name: TR.init.system.orgunitGroup,
												id: 'orgGroupCombobox',
												emptyText: TR.i18n.please_select,
												hidden: true,
												queryMode: 'local',
												editable: false,
												valueField: 'id',
												displayField: 'name',
												fieldLabel: TR.i18n.orgunit_groups,
												labelWidth: 135,
												emptyText: TR.i18n.please_select,
												width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor,
												store: TR.store.orgunitGroup,
												listeners: {
													added: function() {
														TR.cmp.settings.orgunitGroup = this;
													}
												}
											},
											{
												xtype: 'treepanel',
												cls: 'tr-tree',
												id: 'treeOrg',
												width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor,
												autoScroll: true,
												multiSelect: true,
												isrendered: false,
												storage: {},
												addToStorage: function(objects) {
													for (var i = 0; i < objects.length; i++) {
														this.storage[objects[i].id] = objects[i];
													}
												},
												selectRoot: function() {
													if (this.isrendered) {
														if (!this.getSelectionModel().getSelection().length) {
															this.getSelectionModel().select(this.getRootNode());
														}
													}
												},
												findNameById: function(id) {
													var name = this.store.getNodeById(id) ? this.store.getNodeById(id).data.text : null;
													if (!name) {
														for (var k in this.storage) {
															if (k == id) {
																name = this.storage[k].name;
															}
														}
													}
													return name;
												},
												store: Ext.create('Ext.data.TreeStore', {
													proxy: {
														type: 'ajax',
														url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.organisationunitchildren_get
													},
													root: {
														id: 0,
                                                        text: "/",
														expanded: true,
														children: TR.init.system.rootnodes
													}
												}),
												rootVisible: false, 
												listeners: {
													added: function() {
														TR.cmp.params.organisationunit.treepanel = this;
													},
													afterrender: function( treePanel, eOpts )
													{
														treePanel.getSelectionModel().select( treePanel.getRootNode() );
														TR.state.orgunitIds = [];
														var orgunitid = treePanel.getSelectionModel().getSelection()[0].data.id;
														if(orgunitid==0){
															for( var i in TR.init.system.rootnodes){
																 TR.state.orgunitIds.push( TR.init.system.rootnodes[i].id );
															}
														}
														else{
															TR.state.orgunitIds.push( orgunitid );
														}													
													},
													itemclick : function(view,rec,item,index,eventObj){
														TR.state.orgunitIds = [];
														var selectedNodes = TR.cmp.params.organisationunit.treepanel.getSelectionModel().getSelection();
														for( var i=0; i<selectedNodes.length; i++ ){
															TR.state.orgunitIds.push( selectedNodes[i].data.id);
														}
													}
												}
											}
										],
										listeners: {
											added: function() {
												TR.cmp.params.organisationunit.panel = this;
											},
											expand: function() {
												TR.cmp.params.organisationunit.treepanel.setHeight(TR.cmp.params.organisationunit.panel.getHeight() - TR.conf.layout.west_fill_accordion_organisationunit - 25 );
											}
										}
									},
									
									// DATA ELEMENTS
									{
										title: '<div style="height:17px;background-image:url(images/data.png); background-repeat:no-repeat; padding-left:20px;">' + TR.i18n.data_elements + '</div>',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'toolbar',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														cls: 'tr-toolbar-multiselect-left',
														items: [
															{
																xtype: 'label',	
																text: TR.i18n.available,
																cls: 'tr-toolbar-multiselect-left-label'
															},
															'->',
															{
																xtype: 'button',
																icon: 'images/arrowright.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.select(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, true);
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.selectAll(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, true);
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															},
															''
														]
													},
													{
														xtype: 'toolbar',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														cls: 'tr-toolbar-multiselect-left',
														items: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselectAll(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, true);
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselect(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, true);
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															},
															'->',
															{
																xtype: 'label',
																text: TR.i18n.selected,
																cls: 'tr-toolbar-multiselect-right-label'
															}
														]
													},	
													{
														xtype: 'multiselect',
														name: 'availableDataelements',
														cls: 'tr-toolbar-multiselect-left',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: 190,
														displayField: 'name',
														valueField: 'id',
														queryMode: 'remote',
														store: TR.store.dataelement.available,
														tbar: [
															{
																xtype: 'textfield',
																emptyText: TR.i18n.filter,
																id: 'deFilterAvailable',
																width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2 - 64,
																listeners: {			
																	specialkey: function( textfield, e, eOpts ){
																		if ( e.keyCode == e.ENTER )
																		{
																			TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, textfield.rawValue.toLowerCase());	
																		}
																	}
																}
															},
															{
																xtype: 'button',
																icon: 'images/filter.png',
																tooltip: TR.i18n.filter,
																width: 24,
																handler: function() {
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															},
															{
																xtype: 'image',
																src: 'images/grid-split.png',
																width: 1,
																height: 14
															},
															{
																xtype: 'button',
																icon: 'images/clear-filter.png',
																tooltip: TR.i18n.clear,
																width: 24,
																handler: function() {
																	Ext.getCmp('deFilterAvailable').setValue('');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															}
														],
														listeners: {
															added: function() {
																TR.cmp.params.dataelement.available = this;
															},                                                                
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.select(this, TR.cmp.params.dataelement.selected, true);
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}, this);																
															}
														}
													},											
													{
														xtype: 'multiselect',
														name: 'selectedDataelements',
														cls: 'tr-toolbar-multiselect-right',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: 190,
														displayField: 'name',
														valueField: 'id',
														ddReorder: true,
														queryMode: 'remote',
														store: TR.store.dataelement.selected,
														tbar: [
															{
																xtype: 'textfield',
																emptyText: TR.i18n.filter,
																id: 'deFilterSelected',
																width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2 - 64,
																listeners: {			
																	specialkey: function( textfield, e, eOpts ){
																		if ( e.keyCode == e.ENTER )
																		{
																			TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, textfield.rawValue.toLowerCase());	
																		}
																	}
																}
															},
															{
																xtype: 'button',
																icon: 'images/filter.png',
																tooltip: TR.i18n.filter,
																width: 24,
																handler: function() {
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															},
															{
																xtype: 'image',
																src: 'images/grid-split.png',
																width: 1,
																height: 14
															},
															{
																xtype: 'button',
																icon: 'images/clear-filter.png',
																tooltip: TR.i18n.clear,
																width: 24,
																handler: function() {
																	Ext.getCmp('deFilterSelected').setValue('');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															}
														],
														listeners: {
															added: function() {
																TR.cmp.params.dataelement.selected = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.unselect(TR.cmp.params.dataelement.available, this, true);
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}, this);
															}
														}
													},
												]
											},
										],
										listeners: {
											added: function() {
												TR.cmp.params.dataelement.panel = this;
											}
										}
									},
									
									// FILTER BY
									{
										title: '<div style="height:17px;background-image:url(images/data.png); background-repeat:no-repeat; padding-left:20px;">' + TR.i18n.filter_values + '</div>',
										hideCollapseTool: true,
										layout:{
											padding:'10 10 0 10'
										},
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												id: 'filterPanel',
												bodyStyle: 'border-style:none',
												autoScroll: true,
												height: 225,
												width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) ,
												items: []
											}
										]
									},
											
									// OPTIONS
									{
										title: '<div style="height:17px;background-image:url(images/options.png); background-repeat:no-repeat; padding-left:20px;">' + TR.i18n.options + '</div>',
										hideCollapseTool: true,
										cls: 'tr-accordion-options',
										items: [
											{
												xtype: 'fieldset',
												title: TR.i18n.position,
												layout: 'anchor',
												collapsible: false,
												collapsed: false,
												defaults: {
													anchor: '100%',
													labelStyle: 'padding-left:4px;'
												},
												items: [
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														id: 'positionOrgunitCbx',
														fieldLabel: TR.i18n.orgunit,
														labelWidth: 135,
														emptyText: TR.i18n.please_select,
														queryMode: 'local',
														editable: false,
														valueField: 'value',
														displayField: 'name',
														width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
														store:  new Ext.data.ArrayStore({
															fields: ['value', 'name'],
															data: [ ['1', TR.i18n.rows], 
																	['2', TR.i18n.columns], 
																	['3', TR.i18n.filters] ]
														}),
														value: '1',
														listeners: {
															added: function() {
																TR.cmp.settings.positionOrgunit = this;
															},
															change: function (cb, nv, ov) {
																TR.util.positionFilter.orgunit();
																var position = TR.state.aggregateReport.getPosition();
																if( position!=TR.conf.reportPosition.POSITION_ROW_DATA){
																	Ext.getCmp('dataElementGroupByCbx').disable();
																	Ext.getCmp('limitOption').disable();
																}
																else{
																	Ext.getCmp('dataElementGroupByCbx').enable();
																	Ext.getCmp('limitOption').enable();
																}
															}
														}
													},
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														id: 'positionPeriodCbx',
														fieldLabel: TR.i18n.period,
														labelWidth: 135,
														emptyText: TR.i18n.please_select,
														queryMode: 'local',
														editable: false,
														valueField: 'value',
														displayField: 'name',
														width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
														store:  new Ext.data.ArrayStore({
															fields: ['value', 'name'],
															data: [ ['1', TR.i18n.rows], 
																	['2', TR.i18n.columns], 
																	['3', TR.i18n.filters] ]
														}),
														value: '2',
														listeners: {
															added: function() {
																TR.cmp.settings.positionPeriod = this;
															},
															change: function (cb, nv, ov) {
																TR.util.positionFilter.period();
																var position = TR.state.aggregateReport.getPosition();
																if( position!=TR.conf.reportPosition.POSITION_ROW_DATA){
																	Ext.getCmp('dataElementGroupByCbx').disable();
																	Ext.getCmp('limitOption').disable();
																}
																else{
																	Ext.getCmp('dataElementGroupByCbx').enable();
																	Ext.getCmp('limitOption').enable();
																}
															}
														}
													},
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														id: 'positionDataCbx',
														fieldLabel: TR.i18n.data,
														labelWidth: 135,
														emptyText: TR.i18n.please_select,
														queryMode: 'local',
														editable: false,
														valueField: 'value',
														displayField: 'name',
														width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
														store:  new Ext.data.ArrayStore({
															fields: ['value', 'name'],
															data: [ ['3', TR.i18n.filters] ]
														}),
														value: '3',
														listeners: {
															added: function() {
																TR.cmp.settings.positionData = this;
															},
															change: function (cb, nv, ov) {
																var position = TR.state.aggregateReport.getPosition();
																if( position!=TR.conf.reportPosition.POSITION_ROW_DATA){
																	Ext.getCmp('dataElementGroupByCbx').disable();
																	Ext.getCmp('limitOption').disable();
																}
																else{
																	Ext.getCmp('dataElementGroupByCbx').enable();
																	Ext.getCmp('limitOption').enable();
																}
															}
														}
													}
												]
											},
											{
												xtype: 'fieldset',
												layout: 'anchor',
												collapsible: false,
												collapsed: false,
												defaults: {
														anchor: '100%',
														labelStyle: 'padding-left:4px;'
												},
												items: [
												{
													xtype: 'radiogroup',
													id: 'aggregateType',
													fieldLabel: TR.i18n.aggregate_type,
													labelWidth: 135,
													columns: 3,
													vertical: true,
													items: [{
														boxLabel: TR.i18n.count,
														name: 'aggregateType',
														inputValue: 'count',
														checked: true
													}, 
													{
														boxLabel: TR.i18n.sum,
														name: 'aggregateType',
														inputValue: 'sum'
													}, 
													{
														boxLabel: TR.i18n.avg,
														name: 'aggregateType',
														inputValue: 'avg'
													}]
												},
												{
													xtype: 'combobox',
													cls: 'tr-combo',
													id:'levelCombobox',
													hidden: true,
													fieldLabel: TR.i18n.show_hierachy_from_level,
													labelWidth: 135,
													name: TR.conf.finals.programs,
													emptyText: TR.i18n.please_select,
													queryMode: 'local',
													editable: false,
													valueField: 'value',
													displayField: 'name',
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 30,
													store: Ext.create('Ext.data.Store', {
														fields: ['value', 'name'],
														data: TR.init.system.level,
													}),
													value: '1',
													listeners: {
														added: function() {
															TR.cmp.settings.level = this;
														}
													}
												},
												{
													xtype: 'combobox',
													cls: 'tr-combo',
													id: 'dataElementGroupByCbx',
													fieldLabel: TR.i18n.group_by,
													labelWidth: 135,
													emptyText: TR.i18n.please_select,
													queryMode: 'local',
													editable: true,
													disabled: true,
													valueField: 'id',
													displayField: 'name',
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 30,
													store: TR.store.dataelement.available,
													listeners: {
														added: function() {
															TR.cmp.settings.dataElementGroupBy = this;
														}
													}
												},
												{
													xtype: 'numberfield',
													id: 'limitOption',
													fieldLabel: TR.i18n.limit_records,
													labelSeparator: '',
													labelWidth: 135,
													editable: true,
													allowBlank:true,
													disabled: true,
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 30,
													minValue: 1,
													listeners: {
														added: function() {
															TR.cmp.settings.limitOption = this;
														}
													}
												}
												]
											},
										]
									}
								
								]
							}
						]
					}
				],
                listeners: {
                    added: function() {
                        TR.cmp.region.west = this;
                    },
                    collapse: function() {                    
                        this.collapsed = true;
                        TR.cmp.toolbar.resizewest.setText('>>>');
                    },
                    expand: function() {
                        this.collapsed = false;
                        TR.cmp.toolbar.resizewest.setText('<<<');
                    }
                }
            },
			// button for main form
            {
                id: 'center',
                region: 'center',
                layout: 'fit',
                bodyStyle: 'padding-top:0px, padding-bottom:0px',
                tbar: {
                    xtype: 'toolbar',
                    cls: 'tr-toolbar',
                    height: TR.conf.layout.center_tbar_height,
                    defaults: {
                        height: 26
                    },
                    items: [
                        {
                            xtype: 'button',
                            name: 'resizewest',
							cls: 'tr-toolbar-btn-2',
                            text: '<<<',
                            tooltip: TR.i18n.show_hide_settings,
                            handler: function() {
                                var p = TR.cmp.region.west;
                                if (p.collapsed) {
                                    p.expand();
                                }
                                else {
                                    p.collapse();
                                }
                            },
                            listeners: {
                                added: function() {
                                    TR.cmp.toolbar.resizewest = this;
                                }
                            }
                        },
                        {
                            xtype: 'button',
							cls: 'tr-toolbar-btn-1',
                            text: TR.i18n.update,
							handler: function() {
                                if( !TR.state.paramChanged() )
								{
									TR.exe.filter();
								}
								else
								{
									TR.exe.execute();
								}
                            }
                        },
					{
						xtype: 'button',
						text: TR.i18n.clear_filter,
						id: 'btnClean',
						disabled: true,
						handler: function() {
							var grid = TR.datatable.datatable;
							var cols = grid.columns;
							var editor = grid.getStore().getAt(0);
							var colLen = cols.length;
							for( var i=1; i<colLen; i++ )
							{
								var col = cols[i];
								var dataIndex = col.dataIndex;
								TR.store.datatable.first().data[dataIndex] = "";
							}
							
							TR.exe.execute();
						}
					},
					{
						xtype: 'button',
						text: TR.i18n.sort_by,
						id: 'btnSortBy',
						disabled: true,
						execute: function() {
							TR.exe.execute(false, true );
						},
						listeners: {
							afterrender: function(b) {
								this.menu = Ext.create('Ext.menu.Menu', {
									margin: '2 0 0 0',
									shadow: false,
									showSeparator: false,
									items: [
										{
											text: TR.i18n.asc,
											iconCls: 'tr-menu-item-asc',
											minWidth: 105,
											handler: function() {
												TR.state.orderByOrgunitAsc = "true";
												b.execute();
											}
										},
										{
											text: TR.i18n.desc,
											iconCls: 'tr-menu-item-desc',
											minWidth: 105,
											handler: function() {
												TR.state.orderByOrgunitAsc = "false";
												b.execute();
											}
										}
									]                                            
								});
							}
						}
					},
					{
						xtype: 'button',
						cls: 'tr-toolbar-btn-2',
						text: TR.i18n.favorites + '..',
						listeners: {
							afterrender: function(b) {
								this.menu = Ext.create('Ext.menu.Menu', {
									shadow: false,
									showSeparator: false,
									items: [
										{
											text: TR.i18n.manage_favorites,
											iconCls: 'tr-menu-item-edit',
											handler: function() {
												if (TR.cmp.favorite.window) {
													TR.cmp.favorite.window.show();
												}
												else {
													TR.cmp.favorite.window = Ext.create('Ext.window.Window', {
														title: TR.i18n.manage_favorites,
														iconCls: 'tr-window-title-favorite',
														bodyStyle: 'padding:8px; background-color:#fff',
														region: 'center',
														width: TR.conf.layout.grid_favorite_width,
														height: TR.conf.layout.grid_favorite_height,
														closeAction: 'hide',
														resizable: false,
														modal: true,
														resetForm: function() {
															TR.cmp.favorite.name.setValue('');
														},
														items: [
															{
																xtype: 'form',
																bodyStyle: 'border-style:none',
																items: [
																	{
																		xtype: 'textfield',
																		cls: 'tr-textfield',
																		fieldLabel: TR.i18n.name,
																		maxLength: 160,
																		enforceMaxLength: true,
																		labelWidth: TR.conf.layout.form_label_width,
																		width: TR.conf.layout.grid_favorite_width - 28,
																		listeners: {
																			added: function() {
																				TR.cmp.favorite.name = this;
																			},
																			change: function() {
																				TR.cmp.favorite.save.xable();
																			}
																		}
																	}
																]
															},
															{
																xtype: 'grid',
																width: TR.conf.layout.grid_favorite_width - 28,
																scroll: 'vertical',
																multiSelect: true,
																columns: [
																	{
																		dataIndex: 'name',
																		width: TR.conf.layout.grid_favorite_width - 139,
																		style: 'display:none'
																	},
																	{
																		dataIndex: 'lastUpdated',
																		width: 111,
																		style: 'display:none'
																	}
																],
																setHeightInWindow: function(store) {
																	var window = this.up('window');																	
																	this.setHeight(window.getHeight() - 105);																	
																	this.doLayout();
																	this.up('window').doLayout();
																},
																store: TR.store.favorite,
																tbar: {
																	id: 'favorite_t',
																	cls: 'tr-toolbar-tbar',
																	defaults: {
																		height: 24
																	},
																	items: [
																		{
																			text: TR.i18n.sort_by + '..',
																			cls: 'tr-toolbar-btn-2',
																			listeners: {
																				added: function() {
																					TR.cmp.favorite.sortby = this;
																				},
																				afterrender: function(b) {
																					this.addCls('tr-menu-togglegroup');
																					this.menu = Ext.create('Ext.menu.Menu', {
																						margin: '-1 0 0 -1',
																						shadow: false,
                                                                                        showSeparator: false,
																						width: 109,
																						height: 67,
																						items: [
																							{
																								xtype: 'radiogroup',
																								cls: 'tr-radiogroup',
																								columns: 1,
																								vertical: true,
																								items: [
																									{
																										boxLabel: TR.i18n.name,
																										name: 'sortby',
																										handler: function() {
																											if (this.getValue()) {
																												var store = TR.store.favorite;
																												store.sorting.field = 'name';
																												store.sorting.direction = 'ASC';
																												store.sortStore();
																												this.up('menu').hide();
																											}
																										}
																									},
																									{
																										boxLabel:  TR.i18n.last_updated,
																										name: 'sortby',
																										checked: true,
																										handler: function() {
																											if (this.getValue()) {
																												var store = TR.store.favorite;
																												store.sorting.field = 'lastUpdated';
																												store.sorting.direction = 'DESC';
																												store.sortStore();
																												this.up('menu').hide();
																											}
																										}
																									}
																								]
																							}
																						]
																					});
																				}
																			}
																		},
																		'->',
																		{
																			text: TR.i18n.rename,
																			cls: 'tr-toolbar-btn-2',
																			disabled: true,
																			xable: function() {
																				if (TR.cmp.favorite.grid.getSelectionModel().getSelection().length == 1) {
																					TR.cmp.favorite.rename.button.enable();
																				}
																				else {
																					TR.cmp.favorite.rename.button.disable();
																				}
																			},
																			handler: function() {
																				var selected = TR.cmp.favorite.grid.getSelectionModel().getSelection()[0];
																				var w = Ext.create('Ext.window.Window', {
																					title: TR.i18n.rename_favorite,
																					layout: 'fit',
																					width: TR.conf.layout.window_confirm_width,
																					bodyStyle: 'padding:10px 5px; background-color:#fff; text-align:center',
																					modal: true,
																					cmp: {},
																					items: [
																						{
																							xtype: 'textfield',
																							cls: 'tr-textfield',
																							maxLength: 160,
																							enforceMaxLength: true,
																							value: selected.data.name,
																							listeners: {
																								added: function() {
																									this.up('window').cmp.name = this;
																								},
																								change: function() {
																									this.up('window').cmp.rename.xable();
																								}
																							}
																						}
																					],
																					bbar: [
																						{
																							xtype: 'label',
																							style: 'padding-left:2px; line-height:22px; font-size:10px; color:#666; width:50%',
																							listeners: {
																								added: function() {
																									TR.cmp.favorite.rename.label = this;
																								}
																							}
																						},
																						'->',
																						{
																							text: TR.i18n.cancel,
																							handler: function() {
																								this.up('window').close();
																							}
																						},
																						{
																							text: TR.i18n.rename,
																							disabled: true,
																							xable: function() {
																								var value = this.up('window').cmp.name.getValue();
																								if (value) {
																									if (TR.store.favorite.findExact('name', value) == -1) {
																										this.enable();
																										TR.cmp.favorite.rename.label.setText('');
																										return;
																									}
																									else {
																										TR.cmp.favorite.rename.label.setText(TR.i18n.name_already_in_use);
																									}
																								}
																								this.disable();
																							},
																							handler: function() {
																								TR.util.crud.favorite.updateName(this.up('window').cmp.name.getValue());
																							},
																							listeners: {
																								afterrender: function() {
																									this.up('window').cmp.rename = this;
																								},
																								change: function() {
																									this.xable();
																								}
																							}
																						}
																					],
																					listeners: {
																						afterrender: function() {
																							TR.cmp.favorite.rename.window = this;
																						}
																					}
																				});
																				w.setPosition((screen.width/2)-(TR.conf.layout.window_confirm_width/2), TR.conf.layout.window_favorite_ypos + 100, true);
																				w.show();
																			},
																			listeners: {
																				added: function() {
																					TR.cmp.favorite.rename.button = this;
																				}
																			}
																		},
																		{
																			text: TR.i18n.delete_object,
																			cls: 'tr-toolbar-btn-2',
																			disabled: true,
																			xable: function() {
																				if (TR.cmp.favorite.grid.getSelectionModel().getSelection().length) {
																					TR.cmp.favorite.del.enable();
																				}
																				else {
																					TR.cmp.favorite.del.disable();
																				}
																			},
																			handler: function() {
																				var sel = TR.cmp.favorite.grid.getSelectionModel().getSelection();
																				if (sel.length) {
																					var str = '';
																					for (var i = 0; i < sel.length; i++) {
																						var out = sel[i].data.name.length > 35 ? (sel[i].data.name.substr(0,35) + '...') : sel[i].data.name;
																						str += '<br/>' + out;
																					}
																					var w = Ext.create('Ext.window.Window', {
																						title: TR.i18n.delete_favorite,
																						width: TR.conf.layout.window_confirm_width,
																						bodyStyle: 'padding:10px 5px; background-color:#fff; text-align:center',
																						modal: true,
																						items: [
																							{
																								html: TR.i18n.are_you_sure,
																								bodyStyle: 'border-style:none'
																							},
																							{
																								html: str,
																								cls: 'tr-window-confirm-list'
																							}                                                                                                    
																						],
																						bbar: [
																							{
																								text: TR.i18n.cancel,
																								handler: function() {
																									this.up('window').close();
																								}
																							},
																							'->',
																							{
																								text: TR.i18n.delete_object,
																								handler: function() {
																									this.up('window').close();
																									TR.util.crud.favorite.del(function() {
																										TR.cmp.favorite.name.setValue('');
																										TR.cmp.favorite.window.down('grid').setHeightInWindow(TR.store.favorite);
																									});                                                                                                        
																								}
																							}
																						]
																					});
																					w.setPosition((screen.width/2)-(TR.conf.layout.window_confirm_width/2), TR.conf.layout.window_favorite_ypos + 100, true);
																					w.show();
																				}
																			},
																			listeners: {
																				added: function() {
																					TR.cmp.favorite.del = this;
																				}
																			}
																		}
																	]
																},
																listeners: {
																	added: function() {
																		TR.cmp.favorite.grid = this;
																	},
																	itemclick: function(g, r) {
																		TR.cmp.favorite.name.setValue(r.data.name);
																		TR.cmp.favorite.rename.button.xable();
																		TR.cmp.favorite.del.xable();
																	},
																	itemdblclick: function() {
																		if (TR.cmp.favorite.save.xable()) {
																			TR.cmp.favorite.save.handler();
																		}
																	}
																}
															}
														],
														bbar: {
															cls: 'tr-toolbar-bbar',
															defaults: {
																height: 24
															},
															items: [
																{
																	xtype: 'label',
																	style: 'padding-left:2px; line-height:22px; font-size:10px; color:#666; width:70%',
																	listeners: {
																		added: function() {
																			TR.cmp.favorite.label = this;
																		}
																	}
																},																
																'->',
																{
																	text: TR.i18n.save,
																	disabled: true,
																	xable: function() {
																		if (TR.cmp.favorite.name.getValue()) {
																			var index = TR.store.favorite.findExact('name', TR.cmp.favorite.name.getValue());
																			if (index != -1) {
																				this.enable();
																				TR.cmp.favorite.label.setText('');
																				return true;
																			}
																			else {
																				this.enable();
																				TR.cmp.favorite.label.setText('');
																				return true;
																			}
																		}
																		else {
																			TR.cmp.favorite.label.setText('');
																		}
																		
																		this.disable();
																		return false;
																	},
																	handler: function() {
																		if (this.xable()) {
																			var value = TR.cmp.favorite.name.getValue();
																			if (TR.store.favorite.findExact('name', value) != -1) {
																				var item = value.length > 40 ? (value.substr(0,40) + '...') : value;
																				var w = Ext.create('Ext.window.Window', {
																					title: TR.i18n.save_favorite,
																					width: TR.conf.layout.window_confirm_width,
																					bodyStyle: 'padding:10px 5px; background-color:#fff; text-align:center',
																					modal: true,
																					items: [
																						{
																							html: TR.i18n.are_you_sure,
																							bodyStyle: 'border-style:none'
																						},
																						{
																							html: '<br/>' + item,
																							cls: 'tr-window-confirm-list'
																						}
																					],
																					bbar: [
																						{
																							text: TR.i18n.cancel,
																							handler: function() {
																								this.up('window').close();
																							}
																						},
																						'->',
																						{
																							text: TR.i18n.overwrite,
																							handler: function() {
																								this.up('window').close();
																								TR.util.crud.favorite.update(function() {
																									TR.cmp.favorite.window.resetForm();
																								});
																								
																							}
																						}
																					]
																				});
																				w.setPosition((screen.width/2)-(TR.conf.layout.window_confirm_width/2), TR.conf.layout.window_favorite_ypos + 100, true);
																				w.show();
																			}
																			else {
																				TR.util.crud.favorite.create(function() {
																					TR.cmp.favorite.window.resetForm();
																					TR.cmp.favorite.window.down('grid').setHeightInWindow(TR.store.favorite);
																				});
																			}                                                                                    
																		}
																	},
																	listeners: {
																		added: function() {
																			TR.cmp.favorite.save = this;
																		}
																	}
																}
															]
														},
														listeners: {
															show: function() {                                               
																TR.cmp.favorite.save.xable();
																this.down('grid').setHeightInWindow(TR.store.favorite);
															}
														}
													});
													var w = TR.cmp.favorite.window;
													w.setPosition((screen.width/2)-(TR.conf.layout.grid_favorite_width/2), TR.conf.layout.window_favorite_ypos, true);
													w.show();
												}
											},
											listeners: {
												added: function() {
													TR.cmp.toolbar.menuitem.datatable = this;
												}
											}
										},
										'-',
										{
											xtype: 'grid',
											cls: 'tr-menugrid',
											width: 420,
											scroll: 'vertical',
											columns: [
												{
													dataIndex: 'icon',
													width: 25,
													style: 'display:none'
												},
												{
													dataIndex: 'name',
													width: 285,
													style: 'display:none'
												},
												{
													dataIndex: 'lastUpdated',
													width: 110,
													style: 'display:none'
												}
											],
											setHeightInMenu: function(store) {
												var h = store.getCount() * 26,
													sh = TR.util.viewport.getSize().y * 0.6;
												this.setHeight(h > sh ? sh : h);
												this.doLayout();
												this.up('menu').doLayout();
											},
											store: TR.store.favorite,
											listeners: {
												itemclick: function(g, r) {
													g.getSelectionModel().select([], false);
													this.up('menu').hide();
													TR.util.crud.favorite.run(r.data.id);
												}
											}
										}
									],
									listeners: {
										show: function() {
											if (!TR.store.favorite.isloaded) {
												TR.store.favorite.load({scope: this, callback: function() {
													this.down('grid').setHeightInMenu(TR.store.favorite);
												}});
											}
											else {
												this.down('grid').setHeightInMenu(TR.store.favorite);
											}
										}
									}
								});
							}
						}
					},
					{
						xtype: 'button',
						text: TR.i18n.download + '..',
						execute: function(type) {
							TR.exe.execute( type );
						},
						listeners: {
							afterrender: function(b) {
								this.menu = Ext.create('Ext.menu.Menu', {
									margin: '2 0 0 0',
									shadow: false,
									showSeparator: false,
									items: [
										{
											text: TR.i18n.xls,
											iconCls: 'tr-menu-item-xls',
											minWidth: 105,
											handler: function() {
												b.execute(TR.conf.finals.download.xls);
											}
										},
										{
											text: TR.i18n.pdf,
											iconCls: 'tr-menu-item-pdf',
											id: 'downloadPdfIcon',
											minWidth: 105,
											handler: function() {
												b.execute(TR.conf.finals.download.pdf);
											}
										},
										{
											text: TR.i18n.csv,
											iconCls: 'tr-menu-item-csv',
											id: 'downloadCvsIcon',
											minWidth: 105,
											handler: function() {
												b.execute(TR.conf.finals.download.csv);
											}
										}										
									]                                            
								});
							}
						}
					},
					'->',
					{
						xtype: 'button',
						cls: 'tr-toolbar-btn-2',
						text: 'Exit',
						handler: function() {
							window.location.href = TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.redirect;
						}
					},]
                },
                bbar: {
					items: [
						{
							xtype: 'panel',
							cls: 'tr-statusbar',
							height: 24,
							listeners: {
								added: function() {
									TR.cmp.statusbar.panel = this;
								}
							}
						}
					]
				},					
                listeners: {
                    added: function() {
                        TR.cmp.region.center = this;
                    },
                    resize: function() {
						if (TR.cmp.statusbar.panel) {
							TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
						}
					}
                }
            },
            {
                region: 'east',
                preventHeader: true,
                collapsible: true,
                collapsed: true,
                collapseMode: 'mini',
                listeners: {
                    afterrender: function() {
                        TR.cmp.region.east = this;
                    }
                }
            }
        ],
        listeners: {
            afterrender: function(vp) {
                TR.init.initialize(vp);
            },
            resize: function(vp) {
                TR.cmp.region.west.setWidth(TR.conf.layout.west_width);
                
				TR.util.viewport.resizeParams();
                
                if (TR.datatable.datatable) {
                    TR.datatable.datatable.setHeight( TR.util.viewport.getSize().y - 68 );
                }
            } 
        }
    });
    
    }});
}); 
