/**
 * Created by Daniel on 17.05.2016.
 */
var baseURL = "/Tessa/rs/cooking";
var searchPage = "RecipeSearch.html";

//Erstellen der Tabelle
function generateTable(table){
    var tempURL = baseURL + "/c=" + $("#URLPath").text();

    $.getJSON(tempURL, function(data){
        generateInTable(data, table);
    });
}

function generateInTable(data, table) {
    for(var i = 0; i < data.length; i++) {
        var recipe = data[i];
        var row = document.createElement("tr");
        row.id = "recipe_" + recipe.id;

        var cellImg = document.createElement("td");
        cellImg.id = "RecipeImg";
        //cellImg.rowSpan = "2";

        var img = document.createElement("img");
        img.src ="data:image/png;base64," + recipe.picture;
        img.id = "img_" + recipe.id;
        cellImg.appendChild(img);
        row.appendChild(cellImg);
        var cellName = document.createElement("td");
        cellName.id="cellName";
        cellName.width = "100%";
        cellName.innerHTML = recipe.name;
        row.appendChild(cellName);

        /*
            POP-Up um das ausgewählte Rezept anzuzeigen
         */
        var temp = $('#avgrund-popup');
        temp.find('img')[0].src="data:image/png;base64," + recipe.picture;
        temp.find('#name').text(recipe.name);
        temp.find('#description').text(recipe.description);
        temp.find('#ingredients').text(recipe.ingredients);

        $(row).avgrund({
            template: temp.html(),
            width: 900,
            height: 500
        });

        table.appendChild(row);
    }
}

function searchRecipe(){
    var query = $("#suchfeld").val();
    var tempURL = baseURL + "/s=" + query;
    $("#data tr").remove();
    $.getJSON(tempURL, function(data){
        generateInTable(data, $("#data").get(0));
    });
}

function startSearch() {
    var query = $("#suchfeld").val();
    localStorage.query = query;

    if(getCurrentPage() != searchPage){
        document.location.href = searchPage;
    } else {
        localStorage.removeItem("query");
        searchRecipe();
    }
    return false;
}

function getCurrentPage() {
    var page = document.location.href;
    return page.substring(page.lastIndexOf("/") + 1);
}

$(function () {
    $("#suchfeldBlock").submit(startSearch);
    $("#suchen").click(startSearch);
});

$(document).ready(function(){
    if(getCurrentPage() == searchPage) {
        $("#suchfeld").val(localStorage.query);
        localStorage.removeItem("query");
        searchRecipe();
    }
});
