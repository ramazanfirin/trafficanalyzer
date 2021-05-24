
    
	 var points = [];
    
//    document.getElementById('play').addEventListener(
//            'click',
//            function () {
//            	//alert("Hello! I am an alert box!!");
//            	var polygon = [];
//            	for (i = 0; i < points.length; i++) {
//            		polygon[i] = points[i];
//            	}
//            	
////             	vm.polygons.push(polygon);
////             	angular.element(document.getElementById('VideoLineCreateController')).scope().addToPolygonList(polygon);
//            	angular.element(document.getElementById('VideoLineCreateController')).scope().test();
//            },
//            false
//          );
    
    
      var width = window.innerWidth;
      var height = window.innerHeight;



      var stage = new Konva.Stage({
        container: 'containerKonva',
        width: 640,
        height: 480,
      });




      var layer = new Konva.Layer();
	 

 
	  var darthVaderImg = new Konva.Image({
        width: 640,
        height: 480,
		z: -1
      });
	  
	   var imageObj1 = new Image();
      imageObj1.onload = function () {
        darthVaderImg.image(imageObj1);
        layer.draw();
      };
      
	  imageObj1.src = 'http://localhost:8080/content/images/kale.JPG';
	   layer.add(darthVaderImg);	





      var poly = new Konva.Line({
        //points: [23, 20, 23, 160, 70, 93, 150, 109, 290, 139, 270, 93],
		points: [],
        fill: 'lightgreen',
        stroke: 'lightgreen',
        //strokeWidth: 5,
		draggable: true,
        closed: true,
		opacity: 0.5,
		//tension:5
      });

      var tr = new Konva.Transformer();
	  tr.nodes([poly]);
	  
	  layer.add(tr);
	  // add the shape to the layer
      layer.add(poly);
	  
	 
      // add the layer to the stage
      stage.add(layer);
	  
	  
	  stage.on('click', function () {
        var pos = layer.getRelativePointerPosition();
		console.log ("image width: " + pos.x+ " " +pos.y);
		
		points.push( pos.x);
		points.push( pos.y);
		
		poly.attrs.points.push(pos.x);
		poly.attrs.points.push(pos.y);
		
		//poly.attrs.points = [];
		//poly.draw();
		//poly.attrs.points = points;
		//poly.draw();
		layer.draw();
		angular.element(document.getElementById('vm')).scope().makeAlert();
		//stage
      });

