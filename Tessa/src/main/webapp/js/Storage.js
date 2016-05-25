var baseURL = "/Tessa/rs/product";

$(function () {
    //initCarousel();

    $('.modal-trigger').leanModal({
        dismissible: false,
        ready: initCarousel
    });

    $('.datepicker').pickadate({
        selectYears: 15,
        today: 'Heute',
        clear: 'Löschen',
        close: 'Schließen',
        min: new Date()
    });
});

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
function generateTable(table){
    $.getJSON(baseURL, function(data){
        for(var i = 0; i < data.length; i++) {
            var product = data[i];
            var row = document.createElement("tr");
            row.className += ' productRow';

            var cellName = document.createElement("td");
            cellName.className += ' productRow';
            cellName.innerHTML = product.name;
            row.appendChild(cellName);
            var cellCount = document.createElement("td");
            cellCount.className += ' productRow';
            cellCount.innerHTML = product.stueck;
            row.appendChild(cellCount);
            var cellDays = document.createElement("td");
            cellDays.className += ' productRow';
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
        url: baseURL + "/" + id,
        async: false
    });
}

function showModal(){
    $("#myModal3").modal('toggle');
    $("#myModal3").modal('show');
}