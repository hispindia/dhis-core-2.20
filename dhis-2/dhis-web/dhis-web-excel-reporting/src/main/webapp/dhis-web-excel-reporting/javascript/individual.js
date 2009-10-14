// -----------------------------------------------------------------------------
// Filter by data element group
// -----------------------------------------------------------------------------

function getOptionCombos(){
	
	clearListById('availableOptionCombos');
	
	$.get("getOptionCombos.action",
		{
		dataElementId:$("#availableDataElements").val()
		},
		
		function(xmlObject){
			var xmlObject = xmlObject.getElementsByTagName('optionCombo')[0];
			xmlObject = xmlObject.getElementsByTagName('categoryOptions')[0];		
			
			var optionComboList = byId( "availableOptionCombos" );			
			
			optionComboList.options.length = 0;		
			var optionCombos = xmlObject.getElementsByTagName( "categoryOption" );		
			for ( var i = 0; i < optionCombos.length; i++)
			{
				var id = optionCombos[ i ].getAttribute('id');
				var name = optionCombos[ i ].firstChild.nodeValue;			
				var option = document.createElement( "option" );
				option.value = id ;
				option.text = name;
				optionComboList.add( option, null );	
			}
		
		}
	,'xml');
}

// -----------------------------------------------------------------------------
// Filter by data element group
// -----------------------------------------------------------------------------

function filterByDataElementGroup( selectedDataElementGroup )
{
	  var request = new Request();

	  var requestString = 'filterAvailableDataElementsByDataElementGroup.action';
	  
	  var params = 'dataElementGroupId=' + selectedDataElementGroup;

	  var selectedList = byId( 'selectedDataElements' );

		
	  for ( var i = 0; i < selectedList.options.length; ++i)
	  {
		var selectedValue = selectedList.options[i].value;
		  
		var id = selectedValue.substring(selectedValue.indexOf(".") + 1, selectedValue.length);

		params += '&selectedDataElements=' + id;
	  }

	  // Clear the Dataelement list
	  var availableList = document.getElementById( 'availableDataElements' );
	  availableList.options.length = 0;

	  // Clear the OptionCombo list
	  availableList = document.getElementById( 'availableOptionCombos' );
	  availableList.options.length = 0;
	  
	  request.setResponseTypeXML( 'dataElementGroup' );
	  request.setCallbackSuccess( filterByDataElementGroupCompleted );
	  request.sendAsPost( params );
	  request.send( requestString );

}

function filterByDataElementGroupCompleted( dataElementGroup )
{
  var dataElements = dataElementGroup.getElementsByTagName( 'dataElements' )[0];
  var dataElementList = dataElements.getElementsByTagName( 'dataElement' );

  var availableList = document.getElementById( 'availableDataElements' );
 
  for ( var i = 0; i < dataElementList.length; i++ )
  {
    var dataElement = dataElementList[i];
    var value = dataElement.firstChild.nodeValue;
    var id = dataElement.getAttribute( 'id' );
	var option = new Option( value, id );
	
	option.onmousemove  = function(e){
		showToolTip( e, availableAllDataElements[this.value]);
	}
	
	availableList.add( option , null );
  }
  
}

// -----------------------------------------------------------------------------
// Filter available data elements
// -----------------------------------------------------------------------------

function filterAvailableDataElements()
{
	var filter = document.getElementById( 'availableDataElementsFilter' ).value;
    var list = document.getElementById( 'availableDataElements' );
    
    list.options.length = 0;
    
    for ( var id in availableAllDataElements )
    {
        var value = availableAllDataElements[id];
        
		var option = new Option( value, id );
		
		//dw_Tooltip.content_vars[id] = name;
	
		option.onmousemove  = function(e){
			showToolTip(e, name);
		}
			
		//option.setAttribute("onMouseOver",  showToolTip(byId('tooltip'), value) );
		//option.setAttribute("class", "showTip " + id );
	
        if ( value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            list.add(option, null );
        }
    }
}

function initLists()
{
    var list = document.getElementById( 'availableDataElements' );
    
    for ( id in availableAllDataElements )
    {
		var option = new Option( availableAllDataElements[id], id );
		
		option.onmousemove  = function(e){
			showToolTip(e, this.text);
		}
		
        list.add(option , null );
    }
}

// -----------------------------------------------------------------------------
// Add and Remove dataSet members
// -----------------------------------------------------------------------------

function addDataSetMembers()
{
	var list = document.getElementById( 'availableDataElements' );
	
	var listOptionCombo = document.getElementById( 'availableOptionCombos' );

    if ( listOptionCombo.selectedIndex != -1 )
    {
        var id = list.options[list.selectedIndex].value + "." + listOptionCombo[listOptionCombo.selectedIndex].value;

        dataSetMembers[id] = list.options[list.selectedIndex].text + " - " + listOptionCombo[listOptionCombo.selectedIndex].text;
    
		//dw_Tooltip.content_vars[id] = dataSetMembers[id];
	
		var option =  new Option( dataSetMembers[id], id );
	
		option.onmousemove  = function(e){
			showToolTip( e, this.text);
		}
		
		//option.setAttribute("class", "showTip " + id );
	
		byId( 'selectedDataElements' ).add(option, null );
		
		
		listOptionCombo.remove( listOptionCombo.selectedIndex );
    }
}

function removeDataSetMembers()
{
	var listOptionCombo = document.getElementById( 'availableOptionCombos' );

	var list = document.getElementById( 'selectedDataElements' );

	var selectedIndex = list.selectedIndex;
	
    if ( selectedIndex != -1 )
    {
		var selectedValue = list.options[selectedIndex].value;

        var id = selectedValue.substring(selectedValue.indexOf(".") + 1, selectedValue.length);

		var name = dataSetMembers[selectedValue].substring(dataSetMembers[selectedValue].lastIndexOf(" - ") + 2, dataSetMembers[selectedValue].length);
		
		listOptionCombo.add( new Option( name, id ), null );
    
        list.remove( selectedIndex );
    }
}


function filterDataSetMembers()
{
	var filter = document.getElementById( 'dataSetMembersFilter' ).value;
    
	var list = document.getElementById( 'selectedDataElements' );
    
    list.options.length = 0;
    
    for ( var id in dataSetMembers )
    {
        var value = dataSetMembers[id];
        
        if (value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            list.add( new Option( value, id ), null );
        }
    }
}

// -----------------------------------------------------------------------------
// Get Period by PeriodType
// -----------------------------------------------------------------------------

function getPeriods(){

	var request = new Request();
	request.setResponseTypeXML( 'xmlObject' );
	request.setCallbackSuccess( getListPeriodCompleted );
	request.send( '../dhis-web-commons-ajax/getPeriods.action?name=' + $("#availabelPeriodTypes").val()); 	
}

function getListPeriodCompleted( xmlObject ){
	
	clearListById('availablePeriods');
	var nodes = xmlObject.getElementsByTagName('period');
	for ( var i = 0; i < nodes.length; i++ )
    {
        node = nodes.item(i);  
        var id = node.getElementsByTagName('id')[0].firstChild.nodeValue;
        var name = node.getElementsByTagName('name')[0].firstChild.nodeValue;
		addOption('availablePeriods', name, id);
    }
}

// -----------------------------------------------------------------------------
// Generate individual report excel
// -----------------------------------------------------------------------------

function generateIndividualReportExcel(){
	
	// get operands
	var operands = new Array();
	var selectedDataElements = byId('selectedDataElements').options;
	for ( var i = 0; i < selectedDataElements.length; i++ )
	{
		operands[i] = selectedDataElements[i].value;
	}	
	
	// get periods
	var periods = new Array();
	selectedDataElements = byId('selectedPeriods').options;
	for ( var i = 0; i < selectedDataElements.length; i++ )
	{
		periods[i] = selectedDataElements[i].value;
	}
	
	$("#loading").showAtCenter( true );	
		$.post("generateIndividualReportExcel.action",{
		operands:operands,
		periods:periods
		},function(data){		
			window.location = "downloadExcelOutput.action";
			deleteDivEffect();
			$("#loading").hide();		
		},'xml');
}

// -----------------------------------------------------------------------------
function showToolTip( e, value){
	
	var tooltipDiv = byId('tooltip');
	tooltipDiv.style.display = 'block';
	
	var posx = 0;
    var posy = 0;
	
    if (!e) var e = window.event;
    if (e.pageX || e.pageY)
    {
        posx = e.pageX;
        posy = e.pageY;
    }
    else if (e.clientX || e.clientY)
    {
        posx = e.clientX;
        posy = e.clientY;
    }
	
	
	tooltipDiv.style.left= posx  + 8 + 'px';
	tooltipDiv.style.top = posy  + 8 + 'px';
	tooltipDiv.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +   value;
}

function hideToolTip(){
	byId('tooltip').style.display = 'none';
}