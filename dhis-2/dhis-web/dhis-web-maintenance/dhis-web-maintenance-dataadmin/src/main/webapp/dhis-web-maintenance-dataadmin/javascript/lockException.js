jQuery(document).ready(function () {
    jQuery("body").bind("oust.selected", ouChanged);
    jQuery("#dataSets").bind("change", dataSetChanged);
    jQuery("#periods").bind("change", periodChanged);
});

function ouChanged( e ) {
    // arguments is only "array-like", so it doesnt have splice
    var args = Array.prototype.slice.call(arguments);
    var selectedOus = args.splice(1, args.length);

    jQuery("#organisationUnitId").val(selectedOus.join(','));

    jQuery.getJSON('getDataSets.action?id=' + selectedOus.join(','), function ( data ) {
        jQuery("#dataSets").children().remove();

        if ( data.dataSets.length == 0 ) {
            resetDataSets();
        } else {
            for ( var n in data.dataSets ) {
                var option = jQuery("<option />").attr("value", data.dataSets[n].id).text(data.dataSets[n].name)
                jQuery("#dataSets").append(option);
            }

            jQuery("#dataSets").removeAttr("disabled");
        }

        jQuery("#dataSets").trigger("change");
    });
}

function dataSetChanged( e ) {
    var dataSetId = jQuery("#dataSets option:selected").val();
    jQuery("#periods").children().remove();

    if ( !isNaN(dataSetId) ) {
        jQuery.getJSON('getPeriods.action?id=' + dataSetId, function ( data ) {
            if ( data.periods.length == 0 ) {
                resetPeriods();
            } else {
                for ( var n in data.periods ) {
                    var option = jQuery("<option />").attr("value", data.periods[n].externalId).text(data.periods[n].name);
                    jQuery("#periods").append(option);
                }

                jQuery("#periods").removeAttr("disabled");
            }

            jQuery("#periods").trigger("change");
        });
    } else {
        resetPeriods();
        jQuery("#periods").trigger("change");
    }
}

function periodChanged( e ) {
    var periods = jQuery("#periods");

    if ( periods.attr("disabled") ) {
        jQuery("#submit").attr("disabled", true);
    } else {
        jQuery("#submit").removeAttr("disabled");
    }
}

function resetDataSets() {
    var option = jQuery("<option>-- Please select an organisation unit with a dataset --</option>");
    jQuery("#dataSets").append(option);
    jQuery("#dataSets").attr("disabled", true);
}

function resetPeriods() {
    var option = jQuery("<option>-- Please select a dataset --</option>");
    jQuery("#periods").append(option);
    jQuery("#periods").attr("disabled", true);
}
