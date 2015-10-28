/**
 *   Support for score_realtime.html
 */

function showVehiclesOnTrips( route_id ) {
	var vehiclelist = [];
	var shapelist = [];
	var shape_points = [];
	var centerJSON = {};
	var vSize = 0.0;
	var hSize = 0.0;
	var zoom = 0.0;

	jQuery.ajaxSetup({async:false});
	
	// Get the Trips
	$.get('processrequests', {"request-id": "get-current-trips", "route-id": route_id }).success( function( resp_vehicles ) { // on success

		vehiclelist = resp_vehicles;
		if (null == resp_vehicles[0]){
			alert( "There is no current trips for this route." );
			return;
		}

		// Get the unique shapes for trips in route (expect only two, but might be more)
		for( var i=0; i < resp_vehicles.length; i++ ) {
			var trip = resp_vehicles[i];
			if( shapelist.indexOf(trip.shape_id) == -1 ) {
				shapelist.push( trip.shape_id );
			}
		}
		
	}, 'json')
	.fail(function( resp_vehicles ) { // on failure
		alert("Request get-current-trips failed in showVehiclesOnTrips().\nAjax call status: " + resp_vehicles );
	});	

	// TODO: THIS ROUTE ASSUMES ONE ROUTE SHAPE & PUTS ALL VEHICLES ON IT 
	// FOR NOW, PAINT ONE LINE REPRESENTING THE ROUTE FOR ALL TRIPS
	// BUT PUT ALL THE VEHICLES ON THE ROUTE (THEY MAY END UP FLOATING
	var shape_id = shapelist[0];
	
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
		zoom = 15.1 - zoomAdj;

	})
	.fail(function( resp_bounds ) { // on failure
		alert("Request get-current-trips failed in showVehiclesOnTrips().\nAjax call status: " + resp_bounds );
	});	

	
	// GET THE SHAPE (POINTS) SO THAT WE CAN DRAW THE LINE ON THE GOOGLE MAP REPRESENTING THE ROUTE
	$.get('processrequests', {"request-id": "get-route-shape", "shape-id": shape_id }).success( function( resp_shape ) { // on success
		
		shape_points = resp_shape;

	}, 'json')
	.fail(function( resp_shape ) { // on failure
		alert("Request get-current-trips failed in showVehiclesOnTrips().\nAjax call status: " + resp_shape );
	});

		
	// GET THE STOPS SO THAT WE CAN PUT MARKERS ALONG THE ROUTE
	$.get('processrequests', {"request-id": "get-shape-stops", "shape-id": shape_id }).success( function( resp_stops ) { // on success

		// TODO : There must be a way to clean up this code ...
		var map = new google.maps.Map(document.getElementById('score_realtime_map'), {
		    zoom: parseInt(zoom),
		    center: centerJSON,
//		    mapTypeId: [google.maps.MapTypeId.ROADMAP, 'map_style'] 
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
			path: shape_points,
			geodesic: true,
			strokeColor: '#FF0000',
			strokeOpacity: 1.0,
			strokeWeight: 3
			});

			// Origins, anchor positions and coordinates of the marker increase in the X
			// direction to the right and in the Y direction down.
			var stop_image = {
				url: 'images/stop.png',
				// This marker is 20 pixels wide by 20 pixels high.
				size: new google.maps.Size(20, 20),
				// The origin for this image is (0, 0).
				origin: new google.maps.Point(0, 0),
				// The anchor for this image
				anchor: new google.maps.Point(10, 10)
			};
		
			// ADD THE BUS STOPS
			if (null == resp_stops[0]){
				alert( "There are no stops for this shape." );
			}
			else {
				for ( i in resp_stops ) {
					var coords = resp_stops[i];
					var busStops = new google.maps.Marker({
					    position: coords,
					    map: map,
					    optimized: false,
					    zIndex: 3,
					    icon: stop_image
					});
				}
			}

			
			// Origins, anchor positions and coordinates of the marker increase in the X
			// direction to the right and in the Y direction down.
			var t_image = {
				url: 'images/mbta.sm.png',
				// This marker is 20 pixels wide by 20 pixels high.
				size: new google.maps.Size(20, 20),
				// The origin for this image is (0, 0).
				origin: new google.maps.Point(0, 0),
				// The anchor for this image
				anchor: new google.maps.Point(10, 10)
			};
			// ADD THE BUSES
			var buscoord = new Object();
			if (null == vehiclelist[0]){
				alert( "There are no buses on the route now." );
			}
			else {
				for ( j in vehiclelist ) {
					buscoord.lat = Number(vehiclelist[j].vehicle_lat);
					buscoord.lng = Number(vehiclelist[j].vehicle_lon);
					var cityBuses = new google.maps.Marker({
					    position: buscoord,
					    map: map,
					    optimized: false,
					    zIndex: 5,
					    icon: t_image });
				}
			}
			
		//Associate the styled map with the MapTypeId and set it to display.
		map.mapTypes.set('map_style', styledMap);
		map.setMapTypeId('map_style');
		flightPath.setMap(map);
	
	}, 'json')
	.fail(function( resp_stops ) { // on failure
		alert("Request get-current-trips failed in showVehiclesOnTrips().\nAjax call status: " + resp_stops );
	});


	
	jQuery.ajaxSetup({async:true});
}
