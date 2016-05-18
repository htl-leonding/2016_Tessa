/**
 * Created by Daniel on 17.05.2016.
 */
var baseURL = "/Tessa/rs/cooking/c=";
//Erstellen der Tabelle
function generateTable(table){
    baseURL += $("#URLPath").text();

    $.getJSON(baseURL, function(data){
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
            cellName.innerHTML = recipe.name;
            row.appendChild(cellName);

            var temp = $('#avgrund-popup');
            temp.find('img')[0].src="data:image/png;base64," + recipe.picture;
            temp.find('#name').text(recipe.name);
            temp.find('#description').text(recipe.description);
            temp.find('#ingredients').text(recipe.ingredients);

            $(row).avgrund({
               template: temp.html(),
                width: 400,
                height: 300
            });

            table.appendChild(row);
        }
    });
}


