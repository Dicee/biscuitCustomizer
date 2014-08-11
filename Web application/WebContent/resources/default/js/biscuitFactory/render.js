var camera, scene, renderer;
var mouseX = 0, mouseY = 0;
var windowHalfX = window.innerWidth / 2;
var windowHalfY = window.innerHeight / 2;
var manager     = new THREE.LoadingManager();
var biscuit;
var rotate = false;

init();
animate();

function init() {
	camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 2000);
	camera.position.z = 70;
	scene = new THREE.Scene();

	var directionalLight = new THREE.DirectionalLight(0xffffff,0.1);
	directionalLight.position.set(20, 10, 50);
	directionalLight.castShadow = true;
	directionalLight.shadowDarkness = 0.3;
	directionalLight.shadowCameraVisible = false;
	directionalLight.shadowCameraRight = 100;
	directionalLight.shadowCameraLeft = -100;
	directionalLight.shadowCameraTop = 100;
	directionalLight.shadowCameraBottom = -100;

	scene.add(directionalLight);

	topLight                     = new THREE.DirectionalLight(0xffffff, 1);
	topLight.position.set(0, 0, 50);
	topLight.castShadow          = true;
	topLight.shadowDarkness      = 0.3;
//	topLight.shadowCameraVisible = true;
	topLight.shadowCameraRight   = 100;
	topLight.shadowCameraLeft    = -100;
	topLight.shadowCameraTop     = 100;
	topLight.shadowCameraBottom  = -100;
	topLight.shadowCameraNear 	 = 1;
	topLight.shadowCameraFar 	 = 100;
	
	scene.add(topLight);
	
	plane = new THREE.Mesh(new THREE.PlaneGeometry(200, 200),
			new THREE.MeshBasicMaterial({
				color : 0xe1e1e1
			}));

	plane.position.z             = 0;
	plane.name                   = 'plane';
	plane.receiveShadow          = true;
	scene.add(plane);
	
//	var axisHelper               = new THREE.AxisHelper(50);
//	scene.add(axisHelper);


	var container = document.getElementById('render_container');
	renderer = new THREE.WebGLRenderer();
	renderer.setClearColor(0xe1e1e1, 1);
	renderer.setSize(window.innerWidth/1.5,window.innerHeight/1.5);

	renderer.shadowMapEnabled = true;
	renderer.shadowMapSoft = false;

	renderer.shadowMapType = THREE.PCFSoftShadowMap;

	container.appendChild(renderer.domElement);
	container.addEventListener('mousemove',onDocumentMouseMove,false);
}

function loadObject() {
	if (biscuit != null) 
		scene.remove(biscuit);
	
	var loader  = new THREE.ImageLoader(manager);
	var texture = new THREE.Texture();
	loader.load(document.getElementById('texture_canvas').toDataURL('image/png'), function(image) {	
		texture.image       = image; 
		texture.needsUpdate = true;
	});
			
	loader  = new THREE.OBJLoader(manager);
	loader.load('resources/default/obj/' + objectName + '.obj', function(object) {
		object.traverse(function(child) {
			if (child instanceof THREE.Mesh) {
				child.material.map = texture;
				child.castShadow   = true;
			}
		});
		object.position.z = 25;
		object.name       = objectName;
		biscuit 		  = object;		
		scene.add(biscuit);
		
		rotate = true;
		busy   = false;
	});
}

function loadTexture(callback) {
	var name = document.getElementById('form:ref').value;
	var xTop = parseInt(document.getElementById('form:xTop').value);
	var yTop = parseInt(document.getElementById('form:yTop').value);
	
	var img  = new Image();
	img.src  = 'resources/default/textures/' + name + '.png';
	img.onload = function () {
		var canvas = document.getElementById('texture_canvas');
		var iW = img.width, iH = img.height;
		canvas.height = iH;
		canvas.width  = iW;
		ctx = canvas.getContext('2d');
		ctx.drawImage(this,0,0);
		//Add the customizations
		for (var i=0 ; i<objects.length ; i++) {
			var dy = (nFontSizeOptions + 1)*5;
			var x  = xTop + objects[i].x() - minX;
			var y  = yTop + objects[i].y() - minY - dy;
					
			if (getProperty('mode',i) == TEXT_MODE) {
				ctx.globalAlpha = 1.0;
				ctx.fillStyle   = 'rgba(0,0,0,0.6)';
				var size        = parseInt(getProperty("size",i));
				ctx.font        =  size + 'px "Calibri"';
				ctx.fillText(getProperty("custom_text",i),x,y + 3/4*size);
			} 
			else {
				var im = document.getElementById("img" + i);
				ctx.globalAlpha = 0.8;
				ctx.drawImage(im,x,y);
			}
		}
		callback();
	};
}

//////////---Global variables used to track the changes user-triggered that cannot be handled by a listener---////////////
var customText    = null;
var customSnippet = null;
var objectName    = null;
var mode          = 1;
//////////-------------------Global variables used to refresh the render asynchrounoulsy---------------------/////////////
var lastUpdate    = new Date().getTime();
var busy          = false;
var pendingChange = true;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function animate() {
	requestAnimationFrame(animate);

	//We check in the interface if the user has changed any of the render parameters
	var name           = document.getElementById('form:ref').value;
	var objectChanged  = name != objectName;
	pendingChange      = pendingChange || objectChanged; 
	//We update the render parameters values from the user interface
	var now    = new Date().getTime();
	
	if (objectChanged)
		lastUpdate = now;

	objectName = name;
	
	if (pendingChange) { 
		if (now - lastUpdate > 800 && !busy) {
			busy          = true;
			manager       = new THREE.LoadingManager();
			pendingChange = false;
			loadTexture(loadObject);
		}
	} else 
		lastUpdate = now;
	render();
}

var pauseStr	= '<span class="glyphicon glyphicon-pause" id="pause"></span> Pause';

var playStr	= '<span class="glyphicon glyphicon-play" id="play"></span> Play';

function reinit() {
	biscuit.rotation.y 	= 0;
	rotate 				= true;
	manageRotation();
}


function manageRotation() {
	
	document.getElementById("playpause").innerHTML = rotate ? playStr : pauseStr;	
	rotate = !rotate;
}

function render() {
	
	if (rotate) {
		biscuit.rotation.y += 0.015;
	}	
	
	renderer.render(scene, camera);		
}

function onDocumentMouseMove(event) {
	mouseX = (event.clientX - windowHalfX) / 2;
	mouseY = (event.clientY - windowHalfY) / 2;
}