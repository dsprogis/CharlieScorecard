/**
 * Support settings.html
 */

function schedulerOn() {
	_status = "";
	
	$.post("managefetcher", {"request-id": "scheduler-on"},
			function( data, status ) { // on success
				_status = data["fetcher-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
		});

}

function schedulerOff() {
	_status = "";

	$.post("managefetcher", {"request-id": "scheduler-off"},
			function( data, status ) { // on success
				_status = data["fetcher-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
		});

}

function syncSchedulerStatus() {
	_status = "";

	$.post("managefetcher", {"request-id": "get-scheduler-runstate"},
			function( data, status ) { // on success
				_status = data["fetcher-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
				if( "run" == _status ) {
					$('#scheduler-on-off li:eq(0)').addClass("active"); // Set "On" true			
				} else {
					$('#scheduler-on-off li:eq(1)').addClass("active"); // Set "Off" true			
				}
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
		});

}




