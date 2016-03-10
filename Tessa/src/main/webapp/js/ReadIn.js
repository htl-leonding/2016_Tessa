var baseURL = "/FridgePi/rs/product";
var datePicker;

//Löscht Einträge/Werte aus den Textfeldern
function Delete(){
    document.getElementById("produktEingabe").value = "";
    document.getElementById("anzahlEingabe").value = "";
    document.getElementById("haltbarEingabe").value = "";
}

//Sendet Einträge/Produkt an den Application Server
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


        if(!document.getElementById("haltbarEingabe").value.match("^([1-2]?[0-9]{1,2})?(3([0-5][0-9]|6[0-6]))?$")||document.getElementById("haltbarEingabe").value==0){
            document.getElementById("errorHaltbar").style.color = "red";
            document.getElementById("haltbarEingabe").style.borderColor = "red";
            helper = 0;
        }
        else{
            document.getElementById("errorHaltbar").style.color = "white";
            document.getElementById("haltbarEingabe").style.borderColor = "lightgrey";
        }

    if(helper == 1){
        request['barcode'] = "123456789"
        request['name'] = document.getElementById("produktEingabe").value;
        request['stueck'] = document.getElementById("anzahlEingabe").value;
        request['tage'] = document.getElementById("haltbarEingabe").value;

        //Schickt Produkt an den Application Server - RestService Abfrage
        $.ajax({
            type: "POST",
            url: baseURL,
            data: JSON.stringify(request),
            contentType: "application/json"
        });

        Delete();
    }
}