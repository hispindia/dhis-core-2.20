
// -----------------------------------------------------------------------------
// Organisation unit selection listener
// -----------------------------------------------------------------------------

function organisationUnitSelected( orgUnitIds )
{
    window.location.href = 'organisationUnit.action';
}

selection.setListenerFunction( organisationUnitSelected );

// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showOrganisationUnitDetails( unitId )
{
    var request = new Request();
    request.setResponseTypeXML( 'organisationUnit' );
    request.setCallbackSuccess( organisationUnitReceived );
    request.send( 'getOrganisationUnit.action?id=' + unitId );
}

function organisationUnitReceived( unitElement )
{
    setFieldValue( 'nameField', getElementValue( unitElement, 'name' ) );
    setFieldValue( 'shortNameField', getElementValue( unitElement, 'shortName' ) );
    setFieldValue( 'openingDateField', getElementValue( unitElement, 'openingDate' ) );
    
    var closedDate = getElementValue( unitElement, 'closedDate' );
    setFieldValue( 'closedDateField', closedDate ? closedDate : '[' + none + ']' );

    var commentValue = getElementValue( unitElement, 'comment' );
    setFieldValue( 'commentField', commentValue ? commentValue.replace( /\n/g, '<br>' ) : '[' + none + ']' );
    
    var active = getElementValue( unitElement, 'active' );
    setFieldValue( 'activeField', active == 'true' ? yes : no );
    
    showDetails();
}

// -----------------------------------------------------------------------------
// Remove organisation unit
// -----------------------------------------------------------------------------

function removeOrganisationUnit( unitId, unitName )
{
    var result = window.confirm( confirm_to_delete_org_unit + '\n\n' + unitName );
    
    if ( result )
    {
        var request = new Request();
        request.setResponseTypeXML( 'message' );
        request.setCallbackSuccess( removeOrganisationUnitCompleted );
        request.send( 'removeOrganisationUnit.action?id=' + unitId );
    }
}

function removeOrganisationUnitCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        window.location.href = 'organisationUnit.action';
    }
    else if ( type = 'error' )
    {
        setFieldValue( 'warningField', message );
        
        showWarning();
    }
}

// -----------------------------------------------------------------------------
// Add organisation unit
// -----------------------------------------------------------------------------

function validateAddOrganisationUnit()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( addValidationCompleted );
    request.send( 'validateOrganisationUnit.action?name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&code=' + getFieldValue( 'code' ) +
        '&openingDate=' + getFieldValue( 'openingDate' ) );

    return false;
}

function addValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'addOrganisationUnitForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( adding_the_org_unit_failed + ':\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}

function getFieldValue( fieldId )
{
    return document.getElementById( fieldId ).value;
}

// -----------------------------------------------------------------------------
// Update organisation unit
// -----------------------------------------------------------------------------

function validateUpdateOrganisationUnit()
{
    var request = new Request();
    request.setResponseTypeXML( 'message' );
    request.setCallbackSuccess( updateValidationCompleted );
    request.send( 'validateOrganisationUnit.action?id=' + getFieldValue( 'id' ) +
        '&name=' + getFieldValue( 'name' ) +
        '&shortName=' + getFieldValue( 'shortName' ) +
        '&code=' + getFieldValue( 'code' ) +
        '&openingDate=' + getFieldValue( 'openingDate' ) +
        '&closedDate=' + getFieldValue( 'closedDate' ) );

    return false;
}

function updateValidationCompleted( messageElement )
{
    var type = messageElement.getAttribute( 'type' );
    var message = messageElement.firstChild.nodeValue;
    
    if ( type == 'success' )
    {
        var form = document.getElementById( 'updateOrganisationUnitForm' );
        form.submit();
    }
    else if ( type == 'error' )
    {
        window.alert( saving_the_org_unit_failed + ':\n' + message );
    }
    else if ( type == 'input' )
    {
        document.getElementById( 'message' ).innerHTML = message;
        document.getElementById( 'message' ).style.display = 'block';
    }
}
