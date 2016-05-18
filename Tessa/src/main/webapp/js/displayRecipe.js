/**
 * Created by Daniel on 17.05.2016.
 */

var baseURL = "/Tessa/rs/cooking/";
//Erstellen der Tabelle
function generateTable(){
    $.getJSON(baseURL, function(data){
            var recipe = data[i];
            var title = document.createElement("h1");
            title.innerHTML = recipe.name;

            var img = document.createElement("img");
            img.src ="data:image/png;base64," + recipe.picture;

            var description = document.createElement("div");
            description.innerHTML = recipe.description;

            var ingredients = document.createElement("div");
            ingredients.innerHTML = recipe.ingredients;
    });
}




