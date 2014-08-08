///////////////////// Global constants /////////////////
var nFontSizeOptions = 5;
var nScaleOptions    = 6;
////////////////////////////////////////////////////////

var rect = new Kinetic.Rect({
	x : minX,
	y : minY,
	width : edgeX,
	height : edgeY,
	fillAlpha : 0,
	stroke : 'red',
	strokeWidth : 1
});
layer.add(rect);
layer.draw();

function preview() {
	pendingChange = true;
}

function onload() {
	document.getElementById("numberOfObjects").value = 0;
}

function updateObject(i) {
	if (getProperty('mode', i) == TEXT_MODE) {
		// TEXT MODE
		var text = objects[i];
		var oldText = text.text();
		var oldFont = text.fontSize();
		text.setText(getProperty('custom_text',i));
		text.setFontSize(getProperty('size',i));
		if (text.height() > edgeY || text.width() > edgeX) {
			alert('Votre texte est trop long pour être affiché dans le biscuit. Essayez de diminuer la taille de la police.');
			text.setText(oldText);
			text.setFontSize(oldFont);
		}
		//Replace the text at a legal position if necessary
		if (text.x() + text.width() > maxX)
			text.setX(maxX - text.width());

		if (text.y() + text.height() > maxY)
			text.setY(maxY - text.height());
		layer.draw();
	} else {
		// QR CODE MODE
		var QRCode    = objects[i];
		loadQRCode(i,true,function (newQRCode,newQRCodeImg) {
			if (newQRCode.width() <= edgeX && newQRCode.height() <= edgeY) {
				//Replace the object at a legal position if necessary
				newQRCode.setX(QRCode.x() + newQRCode.width()  > maxX ? maxX - newQRCode.width()  : QRCode.x());
				newQRCode.setY(QRCode.y() + newQRCode.height() > maxY ? maxY - newQRCode.height() : QRCode.y());

				//Update and draw
				QRCode.destroy();
				layer.add(newQRCode);
				objects[i] = newQRCode;
				document.body.removeChild(document.getElementById('img' + i));
				document.body.appendChild(newQRCodeImg);
				layer.draw();
			} else 
				alert("Votre QR code est trop grand pour être affiché dans la zone d'édition");
		});
	}
}

function recompact() {
	objects = objects.filter(function(item) { return item != undefined; });
	
	for (var i=0 ; i<objects.length ; i++) 
		objects[i].id = i;
	
	// Renaming the objects' div ids
	var reindex = function(node,i) { 
		console.log("reindex " + node.name + " -> " + i); 
		if (node != null) {
			if (node.id  ) node.id   = node.id  .substring(0,node.id  .length - 1) + i; 
			if (node.name) node.name = node.name.substring(0,node.name.length - 1) + i;
		}
	};
	var nodes = document.getElementById("formDiv").childNodes;
	var elts  = [ "custom_text","delete","size","x","y","mode","img" ];
	for (var i = 0, k = 0, node = nodes.item(i) ; i<nodes.length ; node = nodes.item(++i)) 
		if (node.id && node.id.contains("object")) {
			var id = parseInt(node.id.split("object").join(""));
			reindex(node,k);
			for (var j=0 ; j<elts.length ; j++)
				reindex(document.getElementById(elts[j] + id),k);
			k++;
		}
}

function deleteObject(child) {
	var id = truncId(child);
	// delete the HTML elements
	document.getElementById("formDiv").removeChild(document.getElementById("object" + id));
	var img = document.getElementById("img" + id);
	if (img) document.body.removeChild(img);
		
	// delete the Kinetic element
	objects[id].destroy();
	layer.draw();

	// update the hidden field which contains the number of remaining objects
	var noo = document.getElementById("numberOfObjects");
	noo.value = parseInt(noo.value) - 1;

	// delete the object in the array and recompact the array
	delete objects[id];
	recompact();
	objectNumber--;
}

var globalTimeout;

function waitBeforeUpdate(child) {
	if (globalTimeout != null) 
		clearTimeout(globalTimeout);
	globalTimeout = setTimeout(function() {
			globalTimeout = null;  
			updateObject(truncId(child));
		}, 500);  
}

function addObject(mode) {
	if (objectNumber >= 5) {
		alert("Nombre maximum d'objets atteint");
	} else {
		var noo = document.getElementById("numberOfObjects");
		noo.value = parseInt(noo.value) + 1;

		var selectOptions = "";
		if (mode == TEXT_MODE) 
			for (var i=2 ; i<nFontSizeOptions + 2 ; i++)
				selectOptions += '<option value="' + (5*i) + '">Taille ' + (5*i) + '</option>';
		else
			for (var i=1 ; i<=nScaleOptions ; i++)
				selectOptions += '<option value="' + i + '">Taille ' + (i*10) + '</option>';
		
		var domString = 
			  '<div class="form-group">'
			+ '<div class="input-group">'
			+ '<input type="text" class="form-control" name="custom_text' + objectNumber + '" id="custom_text' + objectNumber + '" value="Votre texte" onkeyup="waitBeforeUpdate(this)"/>'
			+ '<span class="input-group-addon glyphicon glyphicon-remove clickable" id="delete' + objectNumber + '" onclick="deleteObject(this)"></span></div></div>'
			+ '<select name="size' + objectNumber + '" id="size' + objectNumber + '" onchange="updateObject(truncId(this))" class="form-control">' + selectOptions + '</select>'
			+ '<input type="text" class="hidden" id="x' + objectNumber + '" name="x' + objectNumber + '" value="' + minX + '" />'
			+ '<input type="text" class="hidden" id="y' + objectNumber + '" name="y' + objectNumber + '" value="' + minY + '" />'
			+ '<input type="text" class="hidden" id="mode' + objectNumber + '" name="mode' + objectNumber + '" value="' + mode + '" />';

		var newObject       = document.createElement("div");
		newObject.id        = "object" + objectNumber;
		newObject.innerHTML = domString;
		var formdiv = document.getElementById('formDiv');
		formdiv.appendChild(newObject);

		if (mode == TEXT_MODE) createText(true);
		else                   createQRCode(true);
		layer.draw();
	}
}