function download(filename, text) {
	var element = document.createElement('a');
	element.setAttribute('href', 'data:text/plain;charset=utf-8,'
			+ encodeURIComponent(text));
	element.setAttribute('download', filename);

	element.style.display = 'none';
	document.body.appendChild(element);

	element.click();

	document.body.removeChild(element);
}

function actualizarRows(rowsShow) {
	var e = document.getElementById('formTableError:tablaError');
	e.up
	// e.setAttribute('rows', 16);

}