trackerCapture.controller('OverdueEventsController',
         function($scope,
                $modal,
                $location,
                $translate,
                orderByFilter,
                DateUtils,
                TEIService,
                TEIGridService,
                TranslationService,
                AttributesFactory,
                ProgramFactory,
                DHIS2EventFactory,
                storage) {

    TranslationService.translate();
    
    $scope.today = DateUtils.format(moment());
    
    $scope.selectedOuMode = 'SELECTED';
    $scope.report = {};
    $scope.displayMode = {};
    $scope.printMode = false;
    
    //watch for selection of org unit from tree
    $scope.$watch('selectedOrgUnit', function() {
        $scope.reportFinished = false;
        $scope.reportStarted = false;
        if( angular.isObject($scope.selectedOrgUnit)){            
            storage.set('SELECTED_OU', $scope.selectedOrgUnit);            
            $scope.loadPrograms($scope.selectedOrgUnit);
        }
    });
    
    //load programs associated with the selected org unit.
    $scope.loadPrograms = function(orgUnit) {        
        $scope.selectedOrgUnit = orgUnit;        
        if (angular.isObject($scope.selectedOrgUnit)){
            ProgramFactory.getAll().then(function(programs){
                $scope.programs = programs;                
                if($scope.programs.length === 1){
                    $scope.selectedProgram = $scope.programs[0];
                }
                else{
                    if(angular.isObject($scope.selectedProgram)){
                        var continueLoop = true;
                        for(var i=0; i<programs.length && continueLoop; i++){
                            if(programs[i].id === $scope.selectedProgram.id){
                                $scope.selectedProgram = programs[i];
                                continueLoop = false;
                            }
                        }
                        if(continueLoop){
                            $scope.selectedProgram = null;
                        }
                    }
                    else{
                        $scope.selectedProgram = null;
                    }
                }
            });
        }        
    };
    
    //watch for selection of program
    $scope.$watch('selectedProgram', function() {   
        $scope.reportFinished = false;
        $scope.reportStarted = false;        
        if (angular.isObject($scope.selectedProgram)){
            $scope.generateReport();
        }
    });
    
    //watch for selection of ouMode
    $scope.$watch('selectedOuMode', function() {   
        $scope.reportFinished = false;
        $scope.reportStarted = false;
        if (angular.isObject($scope.selectedProgram)){
            $scope.generateReport();
        }
    });    
    
    $scope.generateReport = function(){
        
        if($scope.selectedProgram && $scope.selectedOuMode){
            
            $scope.reportFinished = false;
            $scope.reportStarted = true;        
            $scope.programStages = [];
            $scope.filterTypes = {};
            $scope.filterText = {};

            angular.forEach($scope.selectedProgram.programStages, function(stage){
                $scope.programStages[stage.id] = stage;
            });

            AttributesFactory.getByProgram($scope.selectedProgram).then(function(atts){            
                $scope.gridColumns = TEIGridService.generateGridColumns(atts, $scope.selectedOuMode);

                $scope.gridColumns.push({name: $translate('event_name'), id: 'eventName', type: 'string', displayInListNoProgram: false, showFilter: false, show: true});
                $scope.filterTypes['eventName'] = 'string';
                $scope.gridColumns.push({name: $translate('due_date'), id: 'dueDate', type: 'date', displayInListNoProgram: false, showFilter: false, show: true});
                $scope.filterTypes['dueDate'] = 'date';
                $scope.filterText['dueDate']= {};                
            });  

            //fetch TEIs for the selected program and orgunit/mode
            TEIService.search($scope.selectedOrgUnit.id, 
                                $scope.selectedOuMode,
                                null,
                                'program=' + $scope.selectedProgram.id,
                                null,
                                $scope.pager,
                                false).then(function(data){                     

                //process tei grid
                var teis = TEIGridService.format(data,true);
                $scope.overdueEvents = [];
                DHIS2EventFactory.getByOrgUnitAndProgram($scope.selectedOrgUnit.id, $scope.selectedOuMode, $scope.selectedProgram.id, null, null).then(function(eventList){
                    
                    angular.forEach(eventList, function(ev){
                        if(ev.dueDate){
                            ev.dueDate = DateUtils.format(ev.dueDate);
                            if( ev.trackedEntityInstance && 
                                !ev.eventDate && 
                                ev.dueDate < $scope.today){
                                
                                var overDue = {};
                                angular.copy(teis.rows[ev.trackedEntityInstance],overDue);
                                angular.extend(overDue,{eventName: $scope.programStages[ev.programStage].name, dueDate: ev.dueDate, followup: ev.followup});
                                
                                $scope.overdueEvents.push(overDue);
                            }
                        }
                    });
                    
                    //sort overdue events by their due dates - this is default
                    $scope.overdueEvents = orderByFilter($scope.overdueEvents, '-dueDate');
                    $scope.overdueEvents.reverse();

                    $scope.reportFinished = true;
                    $scope.reportStarted = false;
                });
            });
        }
    };    
    
    $scope.showHideColumns = function(){
        
        $scope.hiddenGridColumns = 0;
        
        angular.forEach($scope.gridColumns, function(gridColumn){
            if(!gridColumn.show){
                $scope.hiddenGridColumns++;
            }
        });
        
        var modalInstance = $modal.open({
            templateUrl: 'views/column-modal.html',
            controller: 'ColumnDisplayController',
            resolve: {
                gridColumns: function () {
                    return $scope.gridColumns;
                },
                hiddenGridColumns: function(){
                    return $scope.hiddenGridColumns;
                }
            }
        });

        modalInstance.result.then(function (gridColumns) {
            $scope.gridColumns = gridColumns;
        }, function () {
        });
    };
    
    $scope.sortGrid = function(gridHeader){
        if ($scope.sortHeader === gridHeader.id){
            $scope.reverse = !$scope.reverse;
            return;
        }        
        $scope.sortHeader = gridHeader.id;
        $scope.reverse = false;
        
        $scope.overdueEvents = orderByFilter($scope.overdueEvents, $scope.sortHeader);
        
        if($scope.reverse){
            $scope.overdueEvents.reverse();
        }
    };
    
    $scope.searchInGrid = function(gridColumn){
        
        $scope.currentFilter = gridColumn;
       
        for(var i=0; i<$scope.gridColumns.length; i++){
            
            //toggle the selected grid column's filter
            if($scope.gridColumns[i].id === gridColumn.id){
                $scope.gridColumns[i].showFilter = !$scope.gridColumns[i].showFilter;
            }            
            else{
                $scope.gridColumns[i].showFilter = false;
            }
        }
    };    
    
    $scope.removeStartFilterText = function(gridColumnId){
        $scope.filterText[gridColumnId].start = undefined;
    };
    
    $scope.removeEndFilterText = function(gridColumnId){
        $scope.filterText[gridColumnId].end = undefined;
    };
    
    $scope.showDashboard = function(tei){
        $location.path('/dashboard').search({tei: tei.id,                                            
                                            program: $scope.selectedProgram ? $scope.selectedProgram.id: null});
    };
    
    $scope.generateReportData = function(){
        return TEIGridService.getData($scope.overdueEvents, $scope.gridColumns);
    };
    
    $scope.generateReportHeader = function(){
        return TEIGridService.getHeader($scope.gridColumns);
    };
});