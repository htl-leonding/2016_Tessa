var baseURL = "/Tessa/rs/shoppinglist";

$(function () {

    $('.modal-trigger').leanModal();

    $('.tooltipped').tooltip({delay: 50});

    $('.button-collapse').sideNav();
});

//Erstellen des Tables
function generateTable(){
    $.getJSON("/Tessa/rs/shoppinglist", function(data){
        for(var i = 0; i < data.length; i++) {
            var entry = data[i];
            addEntryToList(entry, i);
        }
    });
}

function addEntryToList(entry, i){
    var list = $('#entryList');
    var dbID = "entryListItemDB_" + i;
    var id = "entryListItem_" + i;

    list.append('<li class="collection-item dismissable avatar" id="' + id + '">' +
        '<i class="material-icons circle" style="font-size: 25px">shopping_cart</i>' +
        '<input type="hidden" id="' + dbID + '" value="' + entry.id + '">' +
        '<span class="title" style="font-size: 20px">' + entry.name + '</span>' +
        '<p>' +
        'Anzahl: ' + entry.stueck + ' Stück' +
        '</p>' +
        '</li>'
    );
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
            success: function(entry) {
                Materialize.toast('Eintrag gespeichert!', 4000);
                addEntryToList(entry, $("#entryList").children().length + 1);
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