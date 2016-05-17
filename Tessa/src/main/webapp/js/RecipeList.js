/**
 * Created by Daniel on 17.05.2016.
 */
var baseURL = "/Tessa/rs/recipe";
//Erstellen der Tabelle
function generateTable(table){
    $.getJSON(baseURL, function(data){
        for(var i = 0; i < data.length; i++) {
            var recipe = data[i];
            var row = document.createElement("tr");

            var cellImg = document.createElement("td");
            cellImg.innerHTML = recipe.picture;
            row.appendChild(cellImg);

            var cellName = document.createElement("td");
            cellName.innerHTML = recipe.name;
            row.appendChild(cellName);
        }
    });
}
//<img src="data:image/png;base64," + json.image />




