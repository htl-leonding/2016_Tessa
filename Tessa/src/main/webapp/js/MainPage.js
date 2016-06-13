/**
 * Created by Daniel on 13.06.2016.
 */
var baseURL = "/Tessa/rs/cooking";
var sliderCount = 5;

$(function () {
    generateSlider();
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