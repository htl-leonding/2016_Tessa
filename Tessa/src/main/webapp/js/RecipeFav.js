/**
 * Created by Daniel on 14.06.2016.
 */
//Erstellen der Tabelle

$(function () {
    $('.button-collapse').sideNav();

    $('#sendBtn').click(function() {
        startSearch();
    });
});


function generateTable(){
    var tempURL = baseURL + "/c=" + $("#URLPath").text();

    $.getJSON(tempURL, function(data){
        for(var i = 0; i < data.length; i++) {
            var recipe = data[i];
            addRecipeEntryToList(recipe, i)
        }
    });
}

function addRecipeEntryToList(recipe, i) {
    var list = $('#recipeList');
    var id = "recipeListItem_" + i;
    var imgsrc = "data:image/png;base64," + recipe.picture;
    var icon;
    var favour = "star_border";
    var ingredients = recipe.ingredient.split(";");

    if(recipe.favourite == true) {
        favour = "star";

        if(recipe.category == "Drink"){
            icon = "local_bar";
        } else if (recipe.category == "Nachspeise"){
            icon = "room_service";
        } else{
            icon = "restaurant";
        }

        let ingredientString = '';
        for(let ing of ingredients){
            ingredientString+=('<ul>' + ing + '</ul>');
        }

        list.append('<li id="' + id + '">' +
            '<div class="collapsible-header" style="font-size: 30px;">'
            + '<i class="material-icons" style="font-size: 30px;">' + icon + '</i>'
            +  recipe.name
            + '<a class="secondary-content" style="cursor: pointer; color: #000000;">'
            + '<i class="medium material-icons" style="font-size: 45px;" onclick="this.recipe.favourite = true">' + favour + '</i></a>'
            + '</div>'


            + '<div class="collapsible-body"  style="margin: 5px 5px 5px 5px;" >'
            + '<div style="float:left; display:block; height: 30vh; width: 10%; margin: 0 5% 0 5%;"><img src="' + imgsrc + '"></div>'
            + '<div style="margin:2% 15% 3% 20%;" >'
            + '<h4>Zutaten: </h4></br>'
            + ingredientString
            + '</div>'
            + '<div style="margin:2% 15% 3% 20%;" >'
            + '<h4>Zubereitung: </h4></br>'
            + recipe.description
            + '</div>'
            + '</div>'
            + '</li>'
        );
    }
}


function searchRecipe(){
    var query = $("#search").val();
    var tempURL = baseURL + "/s=" + query;

    $('#recipeList li').remove();
    $.getJSON(tempURL, function(data){
        for(var i = 0; i < data.length; i++) {
            var recipe = data[i];
            addRecipeEntryToList(recipe, i)
        }
    });
}

function startSearch() {
    var query = $("#search").val();
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
        $("#search").val(localStorage.query);
        localStorage.removeItem("query");
        searchRecipe();
    }
});
