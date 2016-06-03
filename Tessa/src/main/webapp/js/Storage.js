var baseURL = "/Tessa/rs/product";

$(function () {
    //initCarousel();

    $('.modal-trigger').leanModal({
        dismissible: false,
        ready: initCarousel
    });

    $('.datepicker').pickadate({
        selectYears: 15,
        today: '<i class="material-icons">today</i>',
        clear: '<i class="material-icons">delete</i>',
        close: '<i class="material-icons">close</i>',
        min: new Date()
    });

    $('.button-collapse').sideNav();

    loadDatepickerTooltip();
    $('.tooltipped').tooltip({delay: 50});
});

function loadDatepickerTooltip() {
    addTooltip($('.picker__today'), "Heute");
    addTooltip($('.picker__clear'), "Löschen");
    addTooltip($('.picker__close'), "Schließen");
}

function addTooltip(element, text) {
    element.addClass('tooltipped');
    element.attr("data-position", "bottom");
    element.attr("data-tooltip", text);
}

function initCarousel() {
    var carousel = $('.carousel');
    if(!carousel.hasClass('initialized')) {
        var images = [
            "https://pixabay.com/static/uploads/photo/2013/11/20/23/00/nice-apples-214170_960_720.jpg",
            "https://pixabay.com/static/uploads/photo/2010/12/13/10/06/food-2280_960_720.jpg",
            "https://pixabay.com/static/uploads/photo/2016/03/05/21/44/berry-1239075_960_720.jpg",
            "https://pixabay.com/static/uploads/photo/2010/12/13/10/21/strawberry-2688_960_720.jpg",
            "https://pixabay.com/static/uploads/photo/2015/02/14/18/10/pineapple-636562_960_720.jpg",
            "https://pixabay.com/static/uploads/photo/2015/11/05/23/08/banana-1025109_960_720.jpg"
        ];
        var names = [
            "Apfel",
            "Birne",
            "Krischen",
            "Erdbeeren",
            "Ananas",
            "Bananen"
        ];

        for (var i = 0; i < images.length && i < names.length; i++) {
            var image = images[i];
            var name = names[i];
            carousel.append("<a class='carousel-item' onclick='setText(\"" + name + "\")'><img src='" + image + "'/></a>");
        }

        carousel.carousel();
    }
}

function setText(text) {
    $('#labelProduct').addClass("active");
    $('#productInput').val(text)
    $('#productInput').addClass("valid");
}

//Erstellen der Tabelle
function generateTable(){
    $.getJSON(baseURL, function(data){
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

    if(product.tage <= 4){
        iconColor = "orange";
    } else if(product.tage <= 2){
        iconColor = "rot";
    }

    if(days > 300) {
        days = "+300";
    }

    list.append('<li class="collection-item avatar" id="' + id + '">' +
        '<i class="material-icons circle ' + iconColor + '" style="font-size: 25px" >add_shopping_cart</i>' +
        '<input type="hidden" id="' + dbID +'" value="' + product.id + '"/>' +
        '<span class="title" style="font-size: 20px">' + product.name + '</span>' +
        '<p>' +
        'Anzahl: ' + product.stueck + ' Stück / Haltbar: ' + days + ' Tage' +
        '</p>' +
        '<a class="secondary-content" style="cursor: pointer" onclick="deleteProduct(\'' + id + '\',\'' + dbID + '\')"><i class="medium material-icons">delete</i></a>' +
        '<a class="secondary-content" style="margin-right: 8vw; cursor: pointer" onclick="raise_number(\'' + id + '\',\'' + dbID + '\')"><i class="medium material-icons">add</i></a>' +
        '<a class="secondary-content" style="margin-right: 4vw; cursor: pointer" onclick="lower_number(\'' + id + '\',\'' + dbID + '\')"><i class="medium material-icons">remove</i></a>'
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
        request['date'] = $('#expirationDate').val();

        //Schickt Produkt an den Application Server - RestService Abfrage
        $.ajax({
            type: "POST",
            url: baseURL,
            data: JSON.stringify(request),
            contentType: "application/json",
            success: function(product) {
                Materialize.toast('Produkt gespeichert!', 4000);
                addProductEntryToList(product, $("#productList").children().length + 1);
                Delete();
            }
        }).fail(function(){
            Materialize.toast('Produkt konnte nicht gespeichert werden!', 4000);
        });
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

function Delete(){
    $('#productInput').val("");
    $('#countRange').val(1);
    $('#expirationDate').val("");

    $('#labelProduct').removeClass("active");
    $('#productInput').removeClass("valid");
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
function raise_number(htmlID, dbID){
    var id = $("#" + dbID).val();
    $.ajax({
        type: "PUT",
        url: baseURL + "/" + id + "/increase",
        success: function (data){
            var listItem = $("#" + htmlID);
            var htmlData = 'Anzahl: ' + data.stueck + ' Stück / Haltbar: ' + data.tage + ' Tage';
            listItem.children("p").html(htmlData);
        }
    });
}

function lower_number(htmlID, dbID){
    var id = $("#" + dbID).val();
    $.ajax({
        type: "PUT",
        url: baseURL + "/" + id + "/decrease",
        success: function (data){
            var listItem = $("#" + htmlID);
            var htmlData = 'Anzahl: ' + data.stueck + ' Stück / Haltbar: ' + data.tage + ' Tage';
            listItem.children("p").html(htmlData);
        }
    });
}

function deleteProduct(htmlID, dbID) {
    var id = $("#" + dbID).val();
    $.ajax({
        type: "DELETE",
        url: baseURL + "/" + id,
        async: false,
        success: function (){
            $("#" + htmlID).remove();
            Materialize.toast('Produkt gespeichert!', 4000);
        }
    });

}