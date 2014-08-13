///////////////////// Global constants /////////////////
var QRCODE_MODE      = 0;
var TEXT_MODE 	     = 1;
////////////////////////////////////////////////////////
var objectNumber = 0;
var objects = [];
var layer ;
var stage ;

var edgeX ;
var edgeY ;
var minX ;
var minY ;
var maxX ;
var maxY ;

createStage();

function createStage() {
	var globalTimeout;

	if (globalTimeout != null)
		clearTimeout(globalTimeout);
	globalTimeout = setTimeout(function() {
		globalTimeout = null;

		var image;
		imageObj        = new Image();
		imageObj.src    = "resources/default/textures/" + document.getElementById('form:ref').value + "2D.png";
		imageObj.onload = function() {
			image = new Kinetic.Image({
				x : 0,
				y : 0,
				image : imageObj,
				width : imageObj.width,
				height : imageObj.height
			});
			//Initializations
			objectNumber = 0;
			objects      = [];
			layer        = new Kinetic.Layer();
			stage        = new Kinetic.Stage({
				container : 'container',
				width : imageObj.width,
				height : imageObj.height
			});
			layer.add(image);
			layer.draw();

			edgeX = parseInt(document.getElementById('form:edge').value);
			edgeY = edgeX;
			minX  = (stage.width() - edgeX) / 2;
			minY  = (stage.height() - edgeY) / 2;
			maxX  = minX + edgeX;
			maxY  = minY + edgeY;

			initLayer();
			stage.add(layer);
			layer.draw();
		};
	},500);
}

function initLayer() { }

function loadQRCodeByPos(id,draggable,x,y,callback) {
	var data      = getProperty('custom_text',id);
	var size      = parseInt(getProperty('size',id));
	var url       = 'qr-code-api?size=' + size + '&amp;data=' + data;
	var QRcodeImg = new Image();
	QRcodeImg.src = url;
	QRcodeImg.onload = function() {
		if (QRcodeImg.width <= edgeX && QRcodeImg.height <= edgeY) {
			QRcode = new Kinetic.Image({
				x 				: x,
				y 				: y,
				image 			: QRcodeImg,
				width 			: QRcodeImg.width,
				height 			: QRcodeImg.height
			});
			QRcode.setDraggable(draggable);
			if (draggable) 
				QRcode.setDragBoundFunc(function(pos) {
					var X = pos.x < minX ? minX	: pos.x + QRcodeImg.width  > maxX ? maxX - QRcodeImg.width  : pos.x;
					var Y = pos.y < minY ? minY	: pos.y + QRcodeImg.height > maxY ? maxY - QRcodeImg.height : pos.y;
					if (document.getElementById("x" + this.id)) {
						document.getElementById("x" + this.id).value = X;
						document.getElementById("y" + this.id).value = Y;
					}
					return ({ x : X, y : Y });
				});
			QRcodeImg.id            = "img" + id;
			QRcodeImg.style.display = "none";
			QRcode.id               = id;
			callback(QRcode,QRcodeImg);
		} else 
			alert('QR code trop grand');
	};	
}

function createTextByPos(draggable,x,y) {
	var text = new Kinetic.Text({
		x : x,
		y : y,
		text : getProperty('custom_text', objectNumber),
		fontSize : getProperty('size', objectNumber),
		fontFamily : 'Calibri',
		fill : 'white'
	});
	text.setDraggable(draggable);
	if (draggable) {
		text.setDragBoundFunc(function(pos) {
			var X = pos.x < minX ? minX : pos.x + this.width()  > maxX ? maxX - this.width()  : pos.x;
			var Y = pos.y < minY ? minY	: pos.y + this.height() > maxY ? maxY - this.height() : pos.y;
			document.getElementById("x" + this.id).value = X;
			document.getElementById("y" + this.id).value = Y;
			return ({ x : X, y : Y });
		});
	}
	text.id                 = objectNumber;
	objects[objectNumber++] = text;
	layer.add(text);
}

function truncId(node) {
	var id = node.id;
	return id.substring(id.length - 1);
}

function getProperty(property, id) {
	return document.getElementById(property + id).value;
}

function createText(draggable) {
	createTextByPos(draggable,minX,minY);
}

function loadQRCode(id,draggable,callback) {
	loadQRCodeByPos(id,draggable,minX,minY,callback);
}

function createQRCodeByPos(draggable,x,y,callback) {
	loadQRCodeByPos(objectNumber,draggable,x,y,function(QRcode,QRcodeImg) {
		objects[objectNumber++] = QRcode;
		document.getElementById('imageDiv').appendChild(QRcodeImg);
		layer.add(QRcode);
		layer.draw();
		if (callback != undefined)
			callback();
	});
} 

function createQRCode(draggable,callback) {
	loadQRCode(objectNumber,draggable,function(QRcode,QRcodeImg) {
		objects[objectNumber++] = QRcode;
		document.getElementById('imageDiv').appendChild(QRcodeImg);
		layer.add(QRcode);
		layer.draw();
		if (callback != undefined)
			callback();
	});
}