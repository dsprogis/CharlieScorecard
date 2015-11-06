/**
 * Support settings.html
 */

function managerOn() {
	_status = "";
	$.post("managefetcher", {"request-id": "manager-start"},
			function( data, status ) { // on success
				_status = data["manager-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
				syncUI_Status();
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
				syncUI_Status();
		});
}

function managerOff() {
	_status = "";
	$.post("managefetcher", {"request-id": "manager-stop"},
			function( data, status ) { // on success
				_status = data["manager-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
				syncUI_Status();
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
				syncUI_Status();
		});
}

function transformerOn() {
	_status = "";
	$.post("managefetcher", {"request-id": "transformer-start"},
			function( data, status ) { // on success
				_status = data["transformer-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
				syncUI_Status();
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
				syncUI_Status();
		});
}

function transformerOff() {
	_status = "";
	$.post("managefetcher", {"request-id": "transformer-stop"},
			function( data, status ) { // on success
				_status = data["transformer-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
				syncUI_Status();
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
				syncUI_Status();
		});
}


// TODO : rename to more suitable
function syncUI_Status() {
	_status = "";
	$.post("managefetcher", {"request-id": "get-manager-state"},
			function( data, status ) { // on success
				_status = data["manager-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
				if( "alive" == _status ) {
					$('#manager-on-off li:eq(0)').addClass("active"); // Set "Running" active			
					$('#manager-on-off li:eq(1)').removeClass("active"); // Set "Stopped" inactive
				} else {
					$('#manager-on-off li:eq(0)').removeClass("active"); // Set "Running" inactive			
					$('#manager-on-off li:eq(1)').addClass("active"); // Set "Stopped" active	
				}
//				updateTableFetchLog();								// Update the fetcher log table		
				$("#btUpdateTableFetchLog").click();
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
		});
	
	_status = "";
	$.post("managefetcher", {"request-id": "get-transformer-state"},
			function( data, status ) { // on success
				_status = data["transformer-state"];
			},'json')
			.done (function() {
				$('#settings-status').text("Status: " + _status );
				if( "alive" == _status ) {
					$('#transformer-on-off li:eq(0)').addClass("active"); // Set "Running" active			
					$('#transformer-on-off li:eq(1)').removeClass("active"); // Set "Stopped" inactive
				} else {
					$('#transformer-on-off li:eq(0)').removeClass("active"); // Set "Running" inactive			
					$('#transformer-on-off li:eq(1)').addClass("active"); // Set "Stopped" active	
				}
//				updateTableFetchLog();								// Update the fetcher log table		
				$("#btUpdateTableFetchLog").click();
			})
			.fail(function() { // on failure
				$('#settings-status').text("failed");
		});
	
	
}




