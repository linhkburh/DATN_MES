function initDataEdit(res){
	if (typeof beforeEdit == 'function') {
        beforeEdit(res);
    }
    for (var prop in res) {
        // select
        $("select[name$='." + prop + "']").each(function () {
            var temp = res[prop];
            if (temp === true)
                temp = 'true';
            if (temp === false)
                temp = 'false';
            $(this).val(temp);
        });

        // input, textarea
        $("input[name$='." + prop + "'][type!='radio']" + ", textarea[name$='." + prop + "']").each(function () {
            $(this).val(res[prop]);
        });

        // radio
        $('input[type="radio"][name$=".' + prop + '"]').each(function () {
            // Gan gia tri
            //$(this).val(res[prop]);
            // Check theo id
            //$('#'+prop+res[prop]).prop('checked', true);
            $('input[name$=".' + prop + '"][value="' + res[prop] + '"]').prop('checked', true);
        });
        // checkbox
        $('input[type="checkbox"][name$=".' + prop + '"]').each(function () {
            $(this).prop('checked', res[prop]);
        });
    }
    //select2
    $('#divDetail select').each(function (iIndex) {
        $(this).select2();
    });
    $('#btnDel').css('display', '');
    $('#divGrid').css('display', 'none');
    $('#divDetail').css('display', 'inline');

    if (typeof afterEdit == 'function')
        afterEdit(id, res);
}

function delMultiRow(){
	alert("Chưa phát triển chức năng này");
	$($('#tblSearchResult input[type="checkbox"][name="cboxAction"]')).each(function () {
		var idValid = $(this).attr("id");
		if($(this).prop('checked')){
			console.log($(this).attr("value"));
		}
	});
}
/*$( ".mr-auto .nav-item" ).bind( "click", function(event) {
    var clickedItem = $( this );
    $( ".mr-auto .nav-item" ).each( function() {
        $( this ).removeClass( "active" );
    });
    clickedItem.addClass( "active" );
});*/