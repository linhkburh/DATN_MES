// jQuery Alert Dialogs Plugin
//
// Version 1.0
//
// Cory S.N. LaViska
// A Beautiful Site (http://abeautifulsite.net/)
// 29 December 2008
//
// This is an altered version form Aurélien Malisart <aurelien.malisart@gmail.com>
//
// Visit http://github.com/aurels/jquery.alerts
// Visit http://abeautifulsite.net/notebook/87 for more information
//
(function($) {
	
	$.alerts = {
		
		// These properties can be read/written by accessing $.alerts.propertyName from your scripts at any time
		
		verticalOffset: -75,                // vertical offset of the dialog from center screen, in pixels
		horizontalOffset: 0,                // horizontal offset of the dialog from center screen, in pixels/
		repositionOnResize: true,           // re-centers the dialog on window resize
		overlayOpacity: .01,                // transparency level of overlay
		overlayColor: '#FFF',               // base color of overlay
		draggable: true,                    // make the dialogs draggable (requires UI Draggables plugin)
		okButton: '&nbsp;OK&nbsp;',         // text for the OK button (not used anymore in this version)
		cancelButton: '&nbsp;Cancel&nbsp;', // text for the Cancel button (not used anymore in this version)
		dialogClass: null,                  // if specified, this class will be applied to all dialogs
		
		// Public methods
		
		alert: function(message, ok, callback) {
			$.alerts._show(message, null, ok, null, 'alert', function(result) {
				if( callback ) callback(result);
			});
		},
		confirm: function(message, ok, cancel, callback) {
			$.alerts._show(message, null, ok, cancel, 'confirm', function(result) {
				if( callback ) callback(result);
			});
		},
			
		prompt: function(message, value, ok, cancel, callback) {
			$.alerts._show(message, value, ok, cancel, 'prompt', function(result) {
				if( callback ) callback(result);
			});
		},
		
		// Private methods
		
		_show: function(msg, value, ok, cancel, type, callback) {
			var noti;
			if (ok == 'en') {
				noti = "MESSAGE";
				ok = "Ok";
				cancel = "Cancle";
			} else {
				noti = "Th\u00F4ng b\u00E1o";
				ok = "\u0110\u1ED3ng \u00FD";
				cancel = "H\u1EE7y b\u1ECF";
			}
			$.alerts._hide();
			$.alerts._overlay('show');
			
			$("BODY").append(
			  //'<div class="Table"><div id="popup_container" class="alert alert-success"><div class="Row"><div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" id="popup_message"></div></div></div></div>');
                          '<div id="popup_container">' +
			    '<div id="popup_content" style="pa">' + '<div class="alert-header"><span id="ui-id-2" class="ui-dialog-title">'+noti+'</span></div>' +
			      '<div id="popup_message"></div>' +
				'</div>' +
			  '</div>');  
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 99999,
				padding: 0,
				margin: 0
			});

			//$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				//minWidth: $("#popup_container").outerWidth(),
				//maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			switch( type ) {
				case 'alert':
					$("#popup_message").after('<div id="popup_panel"><a id="popup_ok" class="btn blue">'+ok+'</a></div>');
					$("#popup_ok").click( function() {
						$.alerts._hide();
						callback(true);
					});
					$("#popup_ok").focus().keypress( function(e) {
						if( e.keyCode == 13 || e.keyCode == 27 ) $("#popup_ok").trigger('click');
					});
				break;
				case 'confirm':
					$("#popup_message").after('<div id="popup_panel"><a id="popup_ok" class="btn blue">'+ok+'</a><a id="popup_cancel" class="btn gray">'+cancel+'</a></div>');
					$("#popup_ok").click( function() {
						$.alerts._hide();
						if( callback ) callback(true);
					});
					$("#popup_cancel").click( function() {
						$.alerts._hide();
						if( callback ) callback(false);
					});
					// $("#popup_ok").focus();
					$("#popup_ok, #popup_cancel").keypress( function(e) {
						if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
						if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
					});
				break;
				case 'prompt':
					$("#popup_message").append('<br /><input type="hidden" id="action" /><input type="hidden" id="method" /><textarea class="form-control" rows="3" id="popup_prompt" cols=""></textarea>').after('<div id="popup_panel"><input type="button" class="btn blue" value="' + ok + '" id="popup_ok" /> <input type="button" class="btn gray" value="' + cancel + '" id="popup_cancel" /></div>');
					$("#popup_prompt").width( $("#popup_message").width() );
					$("#popup_ok").click( function() {
						var val = $("#popup_prompt").val();
						var action = $("#action").val();
						var method = $("#method").val();
						$.alerts._hide();
						if( callback ) callback( val );
						if(promptOk){
							promptOk(val, action, method);
						}
					});
					$("#popup_cancel").click( function() {
						$.alerts._hide();
						if( callback ) callback( null );
					});
//					$("#popup_prompt, #popup_ok, #popup_cancel").keypress( function(e) {
//						if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
//						if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
//					});
					//if( value ) $("#popup_prompt").val(value);
					//$("#popup_prompt").focus().select();
				break;
			}
			
			// Make draggable
			// if( $.alerts.draggable ) {
			// 				try {
			// 					//$("#popup_container").draggable({ handle: $("#popup_title") });
			// 					//$("#popup_title").css({ cursor: 'move' });
			// 				} catch(e) { /* requires jQuery UI draggables */ }
			// 			}
		},
		
		_hide: function() {
			$("#popup_container").remove();
			$.alerts._overlay('hide');
			$.alerts._maintainPosition(false);
		},
		
		_overlay: function(status) {
			switch( status ) {
				case 'show':
					$.alerts._overlay('hide');
					$("BODY").append('<div id="popup_overlay" class="ui-widget-overlay ui-front"></div>');
//					$("#popup_overlay").css({
//						position: 'absolute',
//						zIndex: 99998,
//						top: '0px',
//						left: '0px',
//						width: '100%',
//						height: $(document).height(),
//						background: $.alerts.overlayColor,
//						opacity: $.alerts.overlayOpacity                                                
//					});
				break;
				case 'hide':
					$("#popup_overlay").remove();
				break;
			}
		},
		
		_reposition: function() {
			var top = ((window.innerHeight / 2) - ($("#popup_container").outerHeight())) + $.alerts.verticalOffset;
			var left = (($(window).width() / 2) - ($("#popup_container").outerWidth() / 2)) + $.alerts.horizontalOffset;
			if( top < 0 ) top = 0;
			if( left < 0 ) left = 0;
			
			// IE6 fix
			if( $.browser.msie && parseInt($.browser.version) <= 6 ) top = top + $(window).scrollTop();
			
			$("#popup_container").css({
				top: top + 'px',
				left: left + 'px'
			});
			$("#popup_overlay").height( $(document).height() );
		},
		
		_maintainPosition: function(status) {
			if( $.alerts.repositionOnResize ) {
				switch(status) {
					case true:
						$(window).bind('resize', function() {
							$.alerts._reposition();
						});
					break;
					case false:
						$(window).unbind('resize');
					break;
				}
			}
		}
		
	}
	
	// Shortuct functions
	jAlert = function(message, ok, callback) {
		$.alerts.alert(message, ok, callback);
	}
		
	jConfirm = function(message, ok, cancel, callback) {
		$.alerts.confirm(message, ok, cancel, callback);
	};
		
	jPrompt = function(message, value, ok, cancel, callback) {
		$.alerts.prompt(message, value, ok, cancel, callback);
	};
	
})(jQuery);