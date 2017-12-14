$("#cloudfoundry-login").click(function(){
    $('.jumbotron').empty();
    $('.jumbotron').append('<iframe id="robocodeframe" src="http://localhost:8080/RobocodeV1/app/welcome.jsp" scrolling="yes"> </iframe>');
    $('.container').css('height','864px');
    $('.container').css('width','100%');
    $('.jumbotron').css('height','100%');
    $('.jumbotron').css('width','100%');
    $('.jumbotron').css('padding','0%');
    $('.wrapper').remove();
    $('.floor').remove();
});