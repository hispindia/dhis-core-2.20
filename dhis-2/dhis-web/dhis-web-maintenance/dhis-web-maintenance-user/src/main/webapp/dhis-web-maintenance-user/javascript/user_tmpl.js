/* user.js already exists. This file is for user.vm */

jQuery( document ).ready( function()
{
	tableSorter( 'userList' );
	selection.setListenerFunction( orgUnitSelected );
} );

function orgUnitSelected( orgUnitIds )
{
	window.location.href = "user.action";
}
