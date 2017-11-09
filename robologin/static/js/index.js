$("#cloudfoundry-login").click(function(){
    $('.jumbotron').empty();
    $('.jumbotron').append('<iframe id="robocodeframe" src="http://localhost:8080/RobocodeV1/app/welcome.jsp" </iframe>');
    $('.container').css('height','764px');
    $('.container').css('width','100%');
    $('.jumbotron').css('height','100%');
    $('.jumbotron').css('width','100%');
    $('.wrapper').remove();
    $('.floor').remove();
});