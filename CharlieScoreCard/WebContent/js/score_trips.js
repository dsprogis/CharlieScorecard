/**
 *   Support for score_trips.html
 */

function initTripsView() { 

	var margin = { top: 50, right: 0, bottom: 100, left: 30 },
		width = 800 - margin.left - margin.right,
		height = 430 - margin.top - margin.bottom,
		gridSize = Math.floor(width / 24),
		buckets = 9,
		colors = ["#0000ff","#6666ff","#9999ff","#ffffff","#ffcccc","#ff9999","#ff6666","#ff3333","#ff0000"];

	var svg = d3.select("#chart").append("svg")												// Create the SVG
		.attr("width", width + margin.left + margin.right)
		.attr("height", height + margin.top + margin.bottom)
		.append("g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	// GET THE DATA
	$.get('processrequests', {"request-id": "get-heatmap-data" }).success( function( data ) { // on success

		
		var colorScale = d3.scale.quantile()
		.domain([0, buckets - 1, d3.max(data, function (d) { return d.value; })])
		.range(colors);

		var cards = svg.selectAll(".hour")												// Create card objects
			.data(data, function(d) {return d.day+':'+d.hour;});
	
		cards.enter().append("rect")													// Create grid of rects
			.attr("x", function(d) { return (d.hour - 1) * gridSize; })
			.attr("y", function(d) { return (d.day - 1) * gridSize; })
			.attr("rx", 4)
			.attr("ry", 4)
			.attr("class", "hour bordered")
			.attr("width", gridSize)
			.attr("height", gridSize)
			.style("fill", colors[0]);													// Start with all colors=[0]
	
		cards.transition().duration(1000)												// Transition all colors to their index values
			.style("fill", function(d) { return colorScale(d.value); });
	
		cards.enter().append("text")
			.text(function(d) { return d.value; })
			.attr("x", function(d) { return ((d.hour - 1) * gridSize) + 4; })
			.attr("y", function(d) { return ((d.day - 1) * gridSize) + (gridSize-4); });
	
		cards.exit().remove();
		
		
	
	}, 'json')
	.fail(function( data ) { // on failure
		alert("Request get-heatmap-data failed in initTripsView().\nAjax call status: " + data );
	});
	
		
}
  

