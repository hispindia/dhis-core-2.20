jQuery( document ).ready( function()
{
    jQuery( "#name" ).focus();

    validation2( 'addValidationRuleGroupForm', function( form )
    {
        form.submit();
    }, {
        'beforeValidateHandler' : function()
        {
            selectAllById( 'groupMembers' );
            selectAllById( 'userRolesToAlert' );
        },
        'rules' : getValidationRules( "validationRuleGroup" )
    } );

    checkValueIsExist( "name", "validateValidationRuleGroup.action" );
} );
