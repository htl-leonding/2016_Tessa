/**
 * Created by Daniel on 13.06.2016.
 */
var baseURL = "/Tessa/rs/cooking";
var baseURL2 = "/Tessa/rs/product";
var sliderCount = 5;

function updateClock(){
    $('#time').text(new Date().toLocaleTimeString().toLowerCase());
}

$(function () {
    $('#date').text($.datepicker.formatDate('DD, d MM, yy', new Date()));

    generateSlider();
    generateTable(document.getElementById('data'));
    setInterval(updateClock, 1000);
});

function generateSlider() {
    var slides = $('.slides');
    var tempUrl = baseURL + "/random/" + sliderCount;
    $.getJSON(tempUrl, function(data){
        for(var i = 0; i < data.length; i++) {
            var recipe = data[i];
            slides.append('<li>' +
                '<img src="data:image/png;base64,' + recipe.picture + '">' +
                '<div class="caption left-align">' +
                '<h3>'+ recipe.name + '</h3>' +
                '<h5>' + recipe.category + '</h5>' +
                '</div>' +
                '</li>')
        }
        $('.slider').slider({full_width: true});
    });
}

//Erstellen der Tabelle
function generateTable(){
    $.getJSON(baseURL2, function(data){
        for(var i = 0; i < data.length; i++) {
            var product = data[i];
            addProductEntryToList(product, i)
        }
    });
}

function addProductEntryToList(product, i) {
    var list = $('#productList');
    var iconColor = "green";
    var days = product.tage;
    var dbID = "productListItemDB_" + i;
    var id = "productListItem_" + i;

    if(product.tage <= 2){
        iconColor = "red";

        list.append('<li class="collection-item dismissable avatar" id="' + id + '">' +
            '<i class="material-icons circle ' + iconColor + '" onclick="sendToShoppingList(\'' + product.name + '\')" style="font-size: 25px; cursor: pointer" >add_shopping_cart</i>' +
            '<input type="hidden" id="' + dbID +'" value="' + product.id + '"/>' +
            '<span class="title" style="font-size: 20px">' + product.name + '</span>' +
            '<p>' + 'Anzahl: ' + product.stueck + ' Stück / Haltbar: ' + days + ' Tage' + '</p>'
            +'</li>'
        );
    } else if(product.tage <= 4){
        iconColor = "orange";
    }

    if(days > 300) {
        days = "+300";
    }
}

function sendToShoppingList(name){
    var request = {};

    request.name = name;
    request.stueck = 1;
    request.permanent = false;

    $.ajax({
        type: "POST",
        url: "/Tessa/rs/shoppinglist",
        data: JSON.stringify(request),
        contentType: "application/json",
        success: function(){
            Materialize.toast('Eintrag in die Einkaufsliste gespeichert!', 4000);
        }
    }).fail(function (){
        Materialize.toast('Eintrag konnte nicht in die Einkaufsliste gespeichert werden!', 4000);
    })
}