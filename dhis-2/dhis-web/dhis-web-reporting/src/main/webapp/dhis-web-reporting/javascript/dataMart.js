
$( document ).ready( function() {
	datePickerInRange( 'startDate' , 'endDate' );
	pingNotificationsTimeout();
} );

function startExport()
{
	var startDate = $( '#startDate' ).val();
	var endDate = $( '#endDate' ).val();
	
	var url = 'startExport.action?startDate=' + startDate + '&endDate=' + endDate;
	
	$( 'input[name="periodType"]').each( function() {
		url += "&periodType=" + $( this ).val();
	} );
	
	$.get( url, pingNotifications );
}

function pingNotifications()
{
	$( '#notificationDiv' ).load( '../dhis-web-commons-ajax-html/getNotifications.action?category=DATAMART&max=20' );
}

function pingNotificationsTimeout()
{
	pingNotifications();
	setTimeout( "pingNotificationsTimeout()", 2000 );
}
