var addToList = (event) => {

    let text = document.getElementById("input-field").value;
    let payload = {
        text: text
    }
    
    var xhr = new XMLHttpRequest();
    xhr.open('POST', './ToDoServlet');
    xhr.setRequestHeader('content-type', 'application/json');
    xhr.send(JSON.stringify(payload));

    xhr.onreadystatechange = () => {
        if (xhr.readyState == 4 && (xhr.status == 201 || xhr.status == 200)) {
            var responseJSON = xhr.response;
            console.log(responseJSON);
            var addedData = JSON.parse(responseJSON);
            addToPage(addedData);
        }
    }


}

var getAllToDos = () => {

    var xhr = new XMLHttpRequest();
    xhr.open('GET', './ToDoServlet');
    xhr.send();

    xhr.onreadystatechange = () => {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var responseJSON = xhr.response;
            // console.log("response");
            var data = JSON.parse(responseJSON);
            buildList(data);
        }
    }

}

var buildList = (data) => {
	data.data.forEach((entry) => {

		let row =	`<div class="row" id="c${entry.id}">
	                <div class="checkbox">
	                    <input type="checkbox" name="c${entry.id}">
	                </div>
	                <div class="text">
	                    <p>${entry.text}</p>
	                </div>
	                <div class="delete">
	                    <button onclick="deleteFromList(event)">Delete</button>
	                </div>
	            </div>`;
	    document.getElementById("content").innerHTML += row;

	});
    

    data.data.forEach((entry) => {
        let checkbox = document.querySelector("#c"+entry.id + " .checkbox input");
        checkbox.addEventListener("change", (event) => {
            patchStatus(event);
        });

        console.log(entry.status);

        if (entry.status) {
            checkbox.click();
        } 
    });

	
}

var deleteFromList = (event) => {

    console.log(event);
    console.log('delete');
    let id = event.target.parentElement.parentElement.id;
    id = id.substring(1);
    console.log(id);

    var xhrdel = new XMLHttpRequest();
    xhrdel.open('DELETE', './ToDoServlet?id='+id);
    xhrdel.onreadystatechange = () => {
		console.log(xhrdel.status);
        if (xhrdel.readyState==4 && (xhrdel.status == 200 || xhrdel.status == 201)) {
			console.log("response");
            document.getElementById("c"+id).outerHTML = "";
        }
    }

    xhrdel.send();

}

var patchStatus = (event) => {

    let id = event.target.name;
    id = id.substring(1);
    console.log(id);

    var xhrpatch = new XMLHttpRequest();
    xhrpatch.open('PATCH', './ToDoServlet?id=' + id + "&status=" + event.target.checked);
    xhrpatch.onreadystatechange = () => {
        if (xhrpatch.readyState == 4 && (xhrpatch.status == 200 || xhrpatch.status == 201)){
            strikeThrough(event.target);
        }
    }

    xhrpatch.send();

}

var addToPage = (addedData) => {
	let row = document.createElement("div");
    row.classList.add("row");
    row.id = "c" + addedData.id;
	let rowStr =`<div class="checkbox">
	                <input type="checkbox" name="c${addedData.id}">
	            </div>
	            <div class="text">
	                <p>${addedData.text}</p>
	            </div>
	            <div class="delete">
	                <button>Delete</button>
	            </div>`;
    row.innerHTML = rowStr;
	document.getElementById("content").appendChild(row);

    let checkbox = document.querySelector("#c"+addedData.id + " .checkbox input");
        checkbox.addEventListener("change", (event) => {
            strikeThrough(event.target);
    });  
	
}

var strikeThrough = (target) => {
    let id = target.parentElement.parentElement.id;
    // console.log(target);
    // console.log(id);

    if (target.checked) {       
        document.querySelector("#" + id + " .text p").style.textDecoration = "line-through";
    } else {
        document.querySelector("#" + id + " .text p").style.textDecoration = "none";
    }
}

getAllToDos();