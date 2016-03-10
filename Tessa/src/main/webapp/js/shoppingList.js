var baseURL = "/FridgePi/rs/shoppinglist";
function generateTable(table){
    $.getJSON("/FridgePi/rs/shoppinglist", function(data){
        for(var i = 0; i < data.length; i++) {
            var product = data[i];
            var row = document.createElement("tr");

            var cellName = document.createElement("td");
            cellName.innerHTML = product.name;
            row.appendChild(cellName);
            var cellCount = document.createElement("td");
            cellCount.innerHTML = product.stueck;
            row.appendChild(cellCount);
            //var cellBtn = document.createElement("td");
            //cellBtn.id = "cellBtnEdit"
            //var btn = document.createElement("BUTTON");
            //btn.setAttribute('class', 'glyphicon glyphicon-edit');
            //btn.id = product.id;
            //cellBtn.appendChild(btn);
            //row.appendChild(cellBtn);
            table.appendChild(row);
        }
    });
}

function Send(){
    var request = {};
    var helper = 1;
    if(!document.getElementById("anzahlEingabe").value.match("^([0-9]{1,2}|100)$")||document.getElementById("anzahlEingabe").value==0){
        document.getElementById("errorAnzahl").style.color = "red";
        document.getElementById("anzahlEingabe").style.borderColor = "red";
        helper = 0;

    }
    else{
        document.getElementById("errorAnzahl").style.color = "white";
        document.getElementById("anzahlEingabe").style.borderColor = "lightgrey";
    }
    if(!document.getElementById("produktEingabe").value.match("^([A-Za-z]|[\u00c4\u00e4\u00d6\u00f6\u00dc\u00fc\u00df])+( |[A-Za-z]|[\u00c4\u00e4\u00d6\u00f6\u00dc\u00fc\u00df])*[A-Za-z]$")){
        document.getElementById("errorProdukt").style.color = "red";
        document.getElementById("produktEingabe").style.borderColor = "red";
        helper=0;
    }
    else{
        document.getElementById("errorProdukt").style.color = "white";
        document.getElementById("produktEingabe").style.borderColor = "lightgrey";
    }

    if(helper == 1){
        request['barcode'] = "0000000000";
        request['name'] = document.getElementById("produktEingabe").value;
        request['stueck'] = document.getElementById("anzahlEingabe").value;
        request['permanent'] = true;

        //Schickt Produkt an den Application Server - RestService Abfrage
        $.ajax({
            type: "POST",
            url: baseURL,
            data: JSON.stringify(request),
            contentType: "application/json"
        });

       // Delete();
    }
    else{
        alert("error");
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