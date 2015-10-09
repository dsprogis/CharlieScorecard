/**
 * Support browse.html
 */


/**
 * Get the Modes out of the database
 */
function getTransportationModes() {
	$.get("processrequests", {"request-id": "get-transportation-modes"},
			function( data, status ) { // on success
				$("#trans-modes-select").empty();
				$("#trans-modes-select").append($('<option>').text("Select Transportation Mode").attr('value', "Select Transportation Mode"));
				$.each(data, function(key, val){
					$('#trans-modes-select').append($('<option>').text(val.route_mode).attr('value', val.route_type));
				});
			},'json')
			.done (function() {
				$('#trans-modes-select option:eq(1)').prop('selected', true).trigger('change'); // Default to the first item			
			})
			.fail(function() { // on failure
				alert("Request getTransportationModes() failed.\nAjax call status: " + status + "\nData returned: " + data);
		});
}

function getRouteByType( route_type ) {
	$.get('processrequests', {"request-id": "get-routes-by-type", "route-type": route_type },
		function( data, status ) { // on success
			$("#routes-select").empty();
			$("#routes-select").append($('<option>').text("Select Route").attr('value', "Select Route"));
			$.each( data, function( key, val ){
				$('#routes-select').append($('<option>').text(val.route_id + ", " + val.route_desc).attr('value', val.route_id));
			});
		},'json')
		.done (function() {
			$('#routes-select option:eq(1)').prop('selected', true).trigger('change'); // Default to the first item			
		})
		.fail(function() { // on failure
			alert("Request getRouteIdsByType failed.\nAjax call status: " + status + "\nData returned: " + data);
	});
}

function getDirectionsByRoute( route_id ) {

	$.get('processrequests', {"request-id": "get-directions-by-route", "route-id": route_id },
		function( data, status ) { // on success
			$("#direction-select").empty();
			$("#direction-select").append($('<option>').text("Select Direction").attr('value', "Select Direction"));
			$.each( data, function( key, val ){
				$('#direction-select').append($('<option>').text(val.direction_id + ", " + val.trip_headsign).attr('value', val.direction_id));
			});
	},'json')
	.done( function() {
//		$('#direction-select').val( 0 ).trigger('change');
		$('#direction-select option:eq(1)').prop('selected', true).trigger('change'); // Default to the first item		
	})
	.fail( function() { // on failure
		alert("Request getDirectionsByRoute failed.\nAjax call status: " + status + "\nData returned: " + data);
	});
}

function getServiceByRouteAndDirection( direction_id ) {
	$.get('processrequests', {"request-id": "get-service-by-route-and-direction", "route-id": $("#routes-select").val() , "direction-id": direction_id },
		function( data, status ) { // on success
			$("#service-select").empty();
			$("#service-select").append($('<option>').text("Select Service").attr('value', "Select Service"));
			$.each( data, function( key, val ){
				$('#service-select').append($('<option>').text(val.service_id + " (trips=" + val.trip_count + ")").attr('value', val.shape_id));
			});
		},'json')
		.done(function() {
			$('#service-select option:eq(1)').prop('selected', true).trigger('change'); // Default to the first item
		})
		.fail(function() { // on failure
			alert("Request getServiceByRouteAndDirection failed.\nAjax call status: " + status + "\nData returned: " + data);
	});
}

function showRouteOnMap( shape_id ) {
	var centerJSON = {};
	var vSize = 0.0;
	var hSize = 0.0;

	// GET THE BOUNDS OF THE SHAPE OF THE ROUTE SO THAT WE CAN SCALE THE GOOGLE MAP FOR BEST FIT
	$.get('processrequests', {"request-id": "get-shape-bounds", "shape-id": shape_id }).success( function( resp_bounds ) { // on success

		if (null == resp_bounds[0]){
			alert( "There is no shape data for this route." );
			$('#trans-modes-select option:eq(1)').prop('selected', true).trigger('change'); // Default to the first item			
			return;
		}
		
		centerJSON.lat = ( Number(resp_bounds[0].lat) + Number(resp_bounds[1].lat) )/2;
		centerJSON.lng = ( Number(resp_bounds[0].lng) + Number(resp_bounds[1].lng) )/2;
		vSize = Math.abs(resp_bounds[0].lat - resp_bounds[1].lat);
		hSize = Math.abs(resp_bounds[0].lng - resp_bounds[1].lng);
		
		var maxDim = ( vSize > hSize ? vSize : hSize );
		var zoomAdj = maxDim * 20.0;
		var zoom = 15.1 - zoomAdj;

		// GET THE SHAPE (POINTS) SO THAT WE CAN DRAW THE LINE ON THE GOOGLE MAP REPRESENTING THE ROUTE
		$.get('processrequests', {"request-id": "get-route-shape", "shape-id": shape_id }).success( function( resp_shape ) { // on success

			// GET THE STOPS SO THAT WE CAN PUT MARKERS ALONG THE ROUTE
			$.get('processrequests', {"request-id": "get-shape-stops", "shape-id": shape_id }).success( function( resp_stops ) { // on success

				// TODO : There must be a way to clean up this code ...
				var map = new google.maps.Map(document.getElementById('map'), {
				    zoom: parseInt(zoom),
				    center: centerJSON,
	//			    mapTypeId: [google.maps.MapTypeId.ROADMAP, 'map_style'] 
					mapTypeId: google.maps.MapTypeId.ROADMAP 
				});
				
				var styles = [ { stylers: [ { hue: "#00ffe6" }, { saturation: -20 } ]
								},
								{	featureType: "road",
									elementType: "geometry",
									stylers: [ { lightness: 100 }, { visibility: "simplified" } ]
								}
							 ];
				
				var styledMap = new google.maps.StyledMapType( styles, {name: "Styled Map"} );
				
				var flightPath = new google.maps.Polyline({
					path: resp_shape,
					geodesic: true,
					strokeColor: '#FF0000',
					strokeOpacity: 1.0,
					strokeWeight: 3
					});

				if (null == resp_stops[0]){
					alert( "There are no stops for this shape." );
				}
				else {
					for ( i in resp_stops ) {
						var coords = resp_stops[i];
						var cityCircle = new google.maps.Marker({
						    position: coords,
						    map: map
						});
					}
				}
				
				//Associate the styled map with the MapTypeId and set it to display.
				map.mapTypes.set('map_style', styledMap);
				map.setMapTypeId('map_style');
				flightPath.setMap(map);
			
			}, 'json')
			.fail(function( resp_stops ) { // on failure
				alert("Request get-route-stops failed in showRouteOnMap().\nAjax call status: " + resp_stops );
			});
		}, 'json')
		.fail(function( resp_shape ) { // on failure
			alert("Request get-route-shape failed in showRouteOnMap().\nAjax call status: " + resp_shape );
		});
	});
}


function addRouteToScheduler( modeID, routeID ) {


	$.post('processrequests', {"request-id": "add-route-to-scheduler", "mode-id": modeID, "route-id": routeID }, function( response ) { // on success
		console.log( "Added to Scheduler, Route = " + routeID );
	});

/*
	// This is the AJAX version
	$.ajax({
		  type:    "POST",
		  url:     "processrequests",
		  data:    {"request-id": "post_z_shape_used", "shape-id": shape_id },
		  success: function(data) {
			  console.log( "shape saved" );
		  },
		  error:   function(jqXHR, textStatus, errorThrown) {
		        alert("Error, status = " + textStatus + ", " +
		              "error thrown: " + errorThrown
		        );
		  }
		});
*/	
}