var baseURL = "/Tessa/rs/shoppinglist";

$(function () {

    $('.modal-trigger').leanModal();

    $('.tooltipped').tooltip({delay: 50});

    $('.button-collapse').sideNav();
});

//Erstellen des Tables
function generateTable(table){
    $.getJSON("/Tessa/rs/shoppinglist", function(data){
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

    if(!$('#productInput').val().match("^[A-Z].{1,}$")){
        $('#productInput').addClass('invalid');
        helper=0;
    }
    else{
        $('#productInput').removeClass('invalid');
    }

    if(helper == 1){
        request['name'] = $('#productInput').val();
        request['stueck'] = $('#countRange').val();
        request['permanent'] = true;

        //Schickt Produkt an den Application Server - RestService Abfrage
        $.ajax({
            type: "POST",
            url: baseURL,
            data: JSON.stringify(request),
            contentType: "application/json",
            success: function(eintrag) {
                Materialize.toast('Eintrag gespeichert!', 4000);
                //addProductEntryToList(product, $("#productList").children().length + 1);
                clearInputFields();
            }
        }).fail(function(){
            Materialize.toast('Eintrag konnte nicht gespeichert werden!', 4000);
        });
    }
}

function clearInputFields(){
    $('#productInput').val("");
    $('#countRange').val(1);
    $('#expirationDate').val("");

    $('#labelProduct').removeClass("active");
    $('#productInput').removeClass("valid");
}