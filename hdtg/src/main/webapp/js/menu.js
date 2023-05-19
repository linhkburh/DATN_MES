function menuAll(locale,vitualRootMenuJson) {
	var menuObj = JSON.parse(vitualRootMenuJson);
	$('#navbar-nav').html('');
	if(menuObj.children==undefined)
		return;
	for (var i = 0; i < menuObj.children.length; i++)
		menuDyn(locale,$('#navbar-nav'), menuObj.children[i]);
	menu();
	/*
	 $('#navbar-nav > li > ul').each(function(){
        var parentWidth = $(this).parent('li').width();
        var minWidth = $(this).width();
        parentWidth = parentWidth < minWidth ? minWidth : parentWidth;
        $(this).css('width', parentWidth);
        $(this).css('min-width', parentWidth);
    }); 
	 * */
	//styleMenu();
}
function menuDyn(locale, parent, mn) {
	// Ve li
	var li = $("<li/>");
	li.appendTo(parent);
	// Ve the a thuoc li
	var a = $('<a/>');
	if(locale=='en')
		a.html(mn.nameEn);
	else
		a.html(mn.name);
	a.attr({
		id:mn.id,
		href : mn.action,
		style : "background-color: transparent;"
	}).appendTo(li);
	// Ve cac menu con
	if (mn.children != undefined && mn.children.length > 0) {
		if (parent.attr('id') == 'navbar-nav') {
			a.addClass("dropdown-toggle");
			a.attr("data-toggle", "dropdown");
		} else {
			li.addClass("dropdown-submenu");
			a.addClass("subdrop");

		}
		var ul = $('<ul/>');
		ul.addClass('dropdown-menu');
		ul.appendTo(li);
		for (var i = 0; i < mn.children.length; i++)
			menuDyn(locale, ul, mn.children[i]);
	} else {
		a.addClass("sub-menu no-caret");
	}

}
function styleMenu(){
	$('.dropdown-menu a.dropdown-toggle').on('click', function(e) {
		  if (!$(this).next().hasClass('show')) {
		    $(this).parents('.dropdown-menu').first().find('.show').removeClass("show");
		  }
		  var $subMenu = $(this).next(".dropdown-menu");
		  $subMenu.toggleClass('show');


		  $(this).parents('li.nav-item.dropdown.show').on('hidden.bs.dropdown', function(e) {
		    $('.dropdown-submenu .show').removeClass("show");
		  });

		  return false;
		});

		/* for TOP LEVEL MENU */
		$('.dropdown>.dropdown-toggle').click(function() {
		  if ($(this).hasClass('caretDown')) {
		    $(this).removeClass('caretDown');
		  } else {
		    $(this).addClass('caretDown');
		  }
		});

		/* for SUB MENUS */
		$('.dropdown-submenu>.dropdown-toggle').click(function() {
		  if ($(this).hasClass('caretDown')) {
		    $(this).removeClass('caretDown');
		  } else {
		    $(this).addClass('caretDown');
		  }
		});
}