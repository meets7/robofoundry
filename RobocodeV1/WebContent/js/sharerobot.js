$("#submit").click(function shareRobot(){
		var robotname=document.getElementById("displayrobots").value;
		var username=document.getElementById("orgusers").value;
		$.ajax({
			url: 'sharerobot',
			type: 'POST',
			data: {"robotname":robotname,"username":username},
			async : false,
			success : function(html) { 
				$('#statusmessage').html("Success");
				$('#statusmessage').css("background-color","#44b548");
				$('#statusmessage').delay(5000).fadeOut('slow');
			}
		});  
		event.preventDefault();
	});