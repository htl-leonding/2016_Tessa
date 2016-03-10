var baseURL = "/FridgePi/rs/product";
//Erstellen der Tabelle
function generateTable(table){
    $.getJSON(baseURL, function(data){
        for(var i = 0; i < data.length; i++) {
            var product = data[i];
            var row = document.createElement("tr");

            var cellName = document.createElement("td");
            cellName.innerHTML = product.name;
            row.appendChild(cellName);
            var cellCount = document.createElement("td");
            cellCount.innerHTML = product.stueck;
            row.appendChild(cellCount);
            var cellDays = document.createElement("td");
            if(product.tage>=300){
                cellDays.innerHTML = "+300";
            }
            else{
            cellDays.innerHTML = product.tage;
            }
            if(product.tage<1) {
                cellDays.style.color="Red";
            }
            else{
                if (product.tage < 3) {
                    cellDays.style.color="Orange";
                }
                else {
                    if (product.tage < 6) {
                        cellDays.style.color="Yellow";
                    }
                    else{
                        cellDays.style.color = "Green";
                    }
                }
            }
            row.appendChild(cellDays);
            var cellBtn = document.createElement("td");
            cellBtn.id = "cellBtnEdit";
            var btn = document.createElement("BUTTON");
            btn.setAttribute('class', 'glyphicon glyphicon-edit');
            btn.id = "btn_" + product.id;
            btn.data=product.id;
            btn.onclick=function(){
                loadInElementsForEdit(this.data)
            };
            cellBtn.appendChild(btn);
            row.appendChild(cellBtn);
            table.appendChild(row);
        }
    });
}

function Send(){
    var request = {};
    var helper = 1;
    if(checkDate() == 0){
        helper = 0;
    }
    if(!document.getElementById("anzahlEingabe").value.match("^([0-9]{1,2}|100)$")||document.getElementById("anzahlEingabe").value==0){
        document.getElementById("errorAnzahl").style.color = "red";
        document.getElementById("anzahlEingabe").style.borderColor = "red";
        helper = 0;

    }
    else{
        document.getElementById("errorAnzahl").style.color = "white";
        document.getElementById("anzahlEingabe").style.borderColor = "lightgrey";
    }
    if(!document.getElementById("produktEingabe").value.match("^[A-Z].{1,}$")){
        document.getElementById("errorProdukt").style.color = "red";
        document.getElementById("produktEingabe").style.borderColor = "red";
        helper=0;
    }
    else{
        document.getElementById("errorProdukt").style.color = "white";
        document.getElementById("produktEingabe").style.borderColor = "lightgrey";
    }


    if(document.getElementById("Tag").value == "Tag"||document.getElementById("Monat").value == "Monat"||document.getElementById("Jahr").value == "Jahr"){
        helper = 0;
        document.getElementById("Tag").style.color = "red";
        document.getElementById("Tag").style.borderColor = "red";
        document.getElementById("Monat").style.color = "red";
        document.getElementById("Monat").style.borderColor = "red";
        document.getElementById("Jahr").style.color = "red";
        document.getElementById("Jahr").style.borderColor = "red";
        document.getElementById("errorHaltbar").style.color = "red;"

    }
    else{
        document.getElementById("Tag").style.color = "black";
        document.getElementById("Tag").style.borderColor = "black";
        document.getElementById("Monat").style.color = "black";
        document.getElementById("Monat").style.borderColor = "black";
        document.getElementById("Jahr").style.color = "black";
        document.getElementById("Jahr").style.borderColor = "black";
        document.getElementById("errorHaltbar").style.color = "white;"
    }

    if(helper == 1){
        request['barcode'] = document.getElementById("barcodeField").value;
        request['name'] = document.getElementById("produktEingabe").value;
        request['stueck'] = document.getElementById("anzahlEingabe").value;
        request['date'] = buildDate();

        //Schickt Produkt an den Application Server - RestService Abfrage
        $.ajax({
            type: "POST",
            url: baseURL,
            data: JSON.stringify(request),
            contentType: "application/json"
        });

        Delete();
    }
    else{
        alert("error");
    }
}

function TriggerBarcode(e) {
    if(e.keyCode === 13) {
        SendBarcode();
    }
}

function SendBarcode() {
    var barcode = document.getElementById("BarcodeEingabe");
    $.getJSON(baseURL + "/barcode=" + barcode.value, function(data){
        if(data.hasOwnProperty("id")){
            var productName = document.getElementById("produktEingabe");
            var productCount = document.getElementById("anzahlEingabe");
            var hiddenBarcode = document.getElementById("barcodeField");
            productName.value = data.name;
            productCount.value = data.stueck;
            hiddenBarcode.value = barcode.value;
            $("#myModal2").modal("hide");
            $("#myModal").modal("show");
        }
        barcode.value = "";
    });
}

function buildDate(){
    if(document.getElementById("Tag").value < 10 || document.getElementById("Monat").value <10){
        if(document.getElementById("Tag").value < 10&&document.getElementById("Monat").value <10){
            return "0"+document.getElementById("Tag").value +".0"+document.getElementById("Monat").value+"."+document.getElementById("Jahr").value;
        }
        else{
           if(document.getElementById("Tag").value < 10) {
               return "0"+document.getElementById("Tag").value +"."+document.getElementById("Monat").value+"."+document.getElementById("Jahr").value;
           }
           else{
               return document.getElementById("Tag").value +".0"+document.getElementById("Monat").value+"."+document.getElementById("Jahr").value;
           }
        }
    }
    else{
        return document.getElementById("Tag").value +"."+document.getElementById("Monat").value+"."+document.getElementById("Jahr").value;
    }
}
function checkDate() {
    var currentDate = new Date();

    if (currentDate.getFullYear() == document.getElementById("Jahr").value) {
            if (currentDate.getMonth() > document.getElementById("Monat").value) {
                return 0;
            }
            else {
                if (currentDate.getMonth() < document.getElementById("Monat").value) {
                    return 1;
                }
                else {
                    if (currentDate.getDate() <= document.getElementById("Tag").value) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                }
            }
        }

    else {
        if(currentDate.getFullYear() < document.getElementById("Jahr").value){
            return 1;
        }
        else{
            return 0;
        }
    }


}
function Delete(){
    document.getElementById("produktEingabe").value = "";
    document.getElementById("anzahlEingabe").value = "";
    document.getElementById("Tag").value = "Tag";
    document.getElementById("Monat").value = "Monat";
    document.getElementById("Jahr").value = "Jahr";
}
// Change the selector if needed
var $table = $('table.scroll'),
    $bodyCells = $table.find('tbody tr:first').children(),
    colWidth;

// Adjust the width of thead cells when window resizes
$(window).resize(function() {
    // Get the tbody columns width array
    colWidth = $bodyCells.map(function() {
        return $(this).width();
    }).get();

    // Set the width of thead columns
    $table.find('thead tr').children().each(function(i, v) {
        $(v).width(colWidth[i]);
    });
}).resize(); // Trigger resize handler

//Edit a product
function raise_number(){
    if(parseInt(document.getElementById("contentVar").textContent)<100) {
        document.getElementById("contentVar").innerHTML = parseFloat(document.getElementById("contentVar").textContent) + 1;
        var id = document.getElementById("productId").value;
        $.ajax({
            type: "PUT",
            url: baseURL + "/" + id + "/increase"
        });
    }

}

function lower_number(){
    if(parseInt(document.getElementById("contentVar").textContent)>1) {
        document.getElementById("contentVar").innerHTML = parseFloat(document.getElementById("contentVar").textContent) - 1;
        var id = document.getElementById("productId").value;
        $.ajax({
            type: "PUT",
            url: baseURL + "/" + id + "/decrease"
        });
    }

}

function loadInElementsForEdit(id){
    showModal();
    $.getJSON(baseURL, function(data){
       var product;
        for(var i = 0; i < data.length; i++) {
            if(data[i].id == id ){
                product = data[i];
            }
        }
        document.getElementById("contentVar").innerHTML = product.stueck;
        document.getElementById("productId").value = id;
        document.getElementById("modal_product").innerHTML = product.name;
    });
}

function deleteProduct() {
    var id = document.getElementById("productId").value;
    $.ajax({
        type: "DELETE",
        url: baseURL + "/" + id
    });
}

function showModal(){
    $("#myModal3").modal('toggle');
    $("#myModal3").modal('show');
}