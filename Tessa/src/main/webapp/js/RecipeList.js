/**
 * Created by Daniel on 17.05.2016.
 */
var baseURL = "/Tessa/rs/cooking";
var searchPage = "RecipeSearch.html";
var sliderCount = 5;

$(function () {
    $('.button-collapse').sideNav();

    $('#sendBtn').click(function() {
        startSearch();
    });

    generateSlider();
});

//Erstellen der Tabelle
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
    var favour = false;
    var ingredients = recipe.ingredient.split(";");


    if(recipe.category == "Drink"){
        icon = "local_bar";
    } else if (recipe.category == "Nachspeise"){
        icon = "room_service";
    } else{
        icon = "restaurant";
    }

    let ingredientString = '';
    for(let ing of ingredients){
        ingredientString+=('<p>' + ing + '</p>');
    }

    list.append('<li id="' + id + '">' +
        '<div class="collapsible-header" style="font-size: 30px;">'
        + '<i class="material-icons" style="font-size: 30px;">' + icon + '</i>'
        +  recipe.name
        + '<a class="secondary-content" style="cursor: pointer; color: #000000;"><i class="medium material-icons" style="font-size: 45px;">star_border</i></a>'
        + '</div>'


        + '<div class="collapsible-body" style="margin: 10px 20px 5px 20px;">' + '<img src="' + imgsrc + '">'
        +  ingredientString
        + '<div style="margin:2% 5% 3% 5%;" >' + recipe.description + '</div>'
        + '</div>'
        + '</li>'
    );
}

function generateSlider() {
    if(getCurrentPage() == "RecipePage.html"){
        var slides = $('.slides');
        var tempUrl = baseURL + "/random/" + sliderCount;
        $.getJSON(tempUrl, function(data){
            for(var i = 0; i < data.length; i++) {
                var recipe = data[i];
                slides.append('<li>' +
                    '<img src="data:image/png;base64,' + recipe.picture + '">' +
                    '<div class="caption left-align">' +
                    '<h3 class="deep-orange-text text-accent-4">'+ recipe.name + '</h3>' +
                    '<h5 class="light deep-orange-text text-accent-4">' + recipe.category + '</h5>' +
                    '</div>' +
                    '</li>')
            }
            $('.slider').slider({full_width: true});
        });
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
